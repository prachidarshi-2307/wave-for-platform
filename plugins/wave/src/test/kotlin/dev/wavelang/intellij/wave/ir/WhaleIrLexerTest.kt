package dev.wavelang.intellij.wave.ir

import com.intellij.psi.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WhaleIrLexerTest {

  @Test
  fun testKeywords() {
    val lexer = WhaleIrLexer()
    lexer.start("module func ret add")
    
    assertEquals(WhaleIrTokens.KEYWORD, lexer.tokenType)
    assertEquals("module", lexer.bufferSequence.substring(lexer.tokenStart, lexer.tokenEnd))
    lexer.advance()
    
    assertEquals(TokenType.WHITE_SPACE, lexer.tokenType)
    lexer.advance()
    
    assertEquals(WhaleIrTokens.KEYWORD, lexer.tokenType)
    assertEquals("func", lexer.bufferSequence.substring(lexer.tokenStart, lexer.tokenEnd))
    lexer.advance()
    
    assertEquals(TokenType.WHITE_SPACE, lexer.tokenType)
    lexer.advance()

    assertEquals(WhaleIrTokens.KEYWORD, lexer.tokenType)
    assertEquals("ret", lexer.bufferSequence.substring(lexer.tokenStart, lexer.tokenEnd))
    lexer.advance()

    assertEquals(TokenType.WHITE_SPACE, lexer.tokenType)
    lexer.advance()

    assertEquals(WhaleIrTokens.KEYWORD, lexer.tokenType)
    assertEquals("add", lexer.bufferSequence.substring(lexer.tokenStart, lexer.tokenEnd))
    lexer.advance()
    
    assertEquals(null, lexer.tokenType)
  }

  @Test
  fun testLlvmSpecificTokensAreIdentifiers() {
    val lexer = WhaleIrLexer()
    // These should NOT be parsed as keywords in Whale IR
    lexer.start("undef poison landingpad catchpad cleanuppad")
    
    assertEquals(WhaleIrTokens.IDENT, lexer.tokenType)
    assertEquals("undef", lexer.bufferSequence.substring(lexer.tokenStart, lexer.tokenEnd))
    lexer.advance()
    
    lexer.advance() // whitespace
    
    assertEquals(WhaleIrTokens.IDENT, lexer.tokenType)
    assertEquals("poison", lexer.bufferSequence.substring(lexer.tokenStart, lexer.tokenEnd))
  }

  @Test
  fun testTypes() {
    val lexer = WhaleIrLexer()
    lexer.start("void ptr label i32 i64")
    
    val expectedTokens = listOf(
      WhaleIrTokens.TYPE, TokenType.WHITE_SPACE,
      WhaleIrTokens.TYPE, TokenType.WHITE_SPACE,
      WhaleIrTokens.TYPE, TokenType.WHITE_SPACE,
      WhaleIrTokens.TYPE, TokenType.WHITE_SPACE,
      WhaleIrTokens.TYPE
    )
    
    for (expectedType in expectedTokens) {
      assertEquals(expectedType, lexer.tokenType)
      lexer.advance()
    }
  }

  @Test
  fun testValueIdsAndLabels() {
    val lexer = WhaleIrLexer()
    lexer.start("%0 @main ^entry:")
    
    assertEquals(WhaleIrTokens.VALUE_ID, lexer.tokenType)
    assertEquals("%0", lexer.bufferSequence.substring(lexer.tokenStart, lexer.tokenEnd))
    lexer.advance()
    
    lexer.advance() // space
    
    assertEquals(WhaleIrTokens.VALUE_ID, lexer.tokenType)
    assertEquals("@main", lexer.bufferSequence.substring(lexer.tokenStart, lexer.tokenEnd))
    lexer.advance()
    
    lexer.advance() // space
    
    assertEquals(WhaleIrTokens.LABEL, lexer.tokenType)
    assertEquals("^entry:", lexer.bufferSequence.substring(lexer.tokenStart, lexer.tokenEnd))
  }
}

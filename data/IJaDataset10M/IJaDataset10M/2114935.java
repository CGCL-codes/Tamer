package de.denkselbst.sentrick.tokeniser.token.features;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import de.denkselbst.sentrick.tokeniser.token.NonAlphanumericToken;
import de.denkselbst.sentrick.tokeniser.token.NumberToken;
import de.denkselbst.sentrick.tokeniser.token.Token;
import de.denkselbst.sentrick.tokeniser.token.WhitespaceToken;
import de.denkselbst.sentrick.tokeniser.token.WordToken;

public class BooleanFeaturesTest {

    private Token word = new WordToken("peace", 0);

    private Token whitespace = new WhitespaceToken(" ", 0);

    private Token number = new NumberToken("42", 0);

    private Token nonalphanum = new NonAlphanumericToken("!", 0);

    private Token lowercaseFirst = new WordToken("lowercase", 0);

    private Token uppercaseFirst = new WordToken("Uppercase", 0);

    private Token allUppercase = new WordToken("SHOUTING", 0);

    @Before
    public void setup() {
        ((NonAlphanumericToken) nonalphanum).setPotentialSentenceBoundaryCharacter(true);
    }

    @Test
    public void isWordToken() {
        assertTrue("Wrong value.", word.getBooleanFeature(BooleanFeature.IS_WORD_TOKEN));
        assertFalse("Wrong value.", number.getBooleanFeature(BooleanFeature.IS_WORD_TOKEN));
        assertFalse("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.IS_WORD_TOKEN));
        assertFalse("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.IS_WORD_TOKEN));
    }

    @Test
    public void isWhiteSpaceToken() {
        assertTrue("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.IS_WHITESPACE_TOKEN));
        assertFalse("Wrong value.", word.getBooleanFeature(BooleanFeature.IS_WHITESPACE_TOKEN));
        assertFalse("Wrong value.", number.getBooleanFeature(BooleanFeature.IS_WHITESPACE_TOKEN));
        assertFalse("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.IS_WHITESPACE_TOKEN));
    }

    @Test
    public void isNumberToken() {
        assertTrue("Wrong value.", number.getBooleanFeature(BooleanFeature.IS_NUMBER_TOKEN));
        assertFalse("Wrong value.", word.getBooleanFeature(BooleanFeature.IS_NUMBER_TOKEN));
        assertFalse("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.IS_NUMBER_TOKEN));
        assertFalse("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.IS_NUMBER_TOKEN));
    }

    @Test
    public void isNonalphaNumericToken() {
        assertFalse("Wrong value.", word.getBooleanFeature(BooleanFeature.IS_NONALPHANUMERIC_TOKEN));
        assertFalse("Wrong value.", number.getBooleanFeature(BooleanFeature.IS_NONALPHANUMERIC_TOKEN));
        assertFalse("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.IS_NONALPHANUMERIC_TOKEN));
        assertTrue("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.IS_NONALPHANUMERIC_TOKEN));
    }

    @Test
    public void isMarkupToken() {
        assertFalse("Wrong value.", word.getBooleanFeature(BooleanFeature.IS_MARKUP_TOKEN));
        assertFalse("Wrong value.", number.getBooleanFeature(BooleanFeature.IS_MARKUP_TOKEN));
        assertFalse("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.IS_MARKUP_TOKEN));
        assertFalse("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.IS_MARKUP_TOKEN));
    }

    @Test
    public void lowercaseFirstLetter() {
        assertTrue("Wrong value.", lowercaseFirst.getBooleanFeature(BooleanFeature.LOWERCASE_FIRST_LETTER));
        assertFalse("Wrong value.", uppercaseFirst.getBooleanFeature(BooleanFeature.LOWERCASE_FIRST_LETTER));
        assertFalse("Wrong value.", allUppercase.getBooleanFeature(BooleanFeature.LOWERCASE_FIRST_LETTER));
        assertFalse("Only WordTokens can have LOWERCASE_FIRST_LETTER = true.", whitespace.getBooleanFeature(BooleanFeature.LOWERCASE_FIRST_LETTER));
        assertFalse("Only WordTokens can have LOWERCASE_FIRST_LETTER = true.", number.getBooleanFeature(BooleanFeature.LOWERCASE_FIRST_LETTER));
        assertFalse("Only WordTokens can have LOWERCASE_FIRST_LETTER = true.", nonalphanum.getBooleanFeature(BooleanFeature.LOWERCASE_FIRST_LETTER));
    }

    @Test
    public void uppercaseFirstLetter() {
        assertFalse("Wrong value.", lowercaseFirst.getBooleanFeature(BooleanFeature.UPPERCASE_FIRST_LETTER));
        assertTrue("Wrong value.", uppercaseFirst.getBooleanFeature(BooleanFeature.UPPERCASE_FIRST_LETTER));
        assertTrue("Wrong value.", allUppercase.getBooleanFeature(BooleanFeature.UPPERCASE_FIRST_LETTER));
        assertFalse("Only WordTokens can have UPPERCASE_FIRST_LETTER = true.", whitespace.getBooleanFeature(BooleanFeature.UPPERCASE_FIRST_LETTER));
        assertFalse("Only WordTokens can have UPPERCASE_FIRST_LETTER = true.", number.getBooleanFeature(BooleanFeature.UPPERCASE_FIRST_LETTER));
        assertFalse("Only WordTokens can have UPPERCASE_FIRST_LETTER = true.", nonalphanum.getBooleanFeature(BooleanFeature.UPPERCASE_FIRST_LETTER));
    }

    @Test
    public void allUppercase() {
        assertFalse("Wrong value.", lowercaseFirst.getBooleanFeature(BooleanFeature.ALL_UPPERCASE));
        assertFalse("Wrong value.", uppercaseFirst.getBooleanFeature(BooleanFeature.ALL_UPPERCASE));
        assertTrue("Wrong value.", allUppercase.getBooleanFeature(BooleanFeature.ALL_UPPERCASE));
        assertTrue("Wrong value.", new WordToken("A", 0).getBooleanFeature(BooleanFeature.ALL_UPPERCASE));
        assertFalse("Only WordTokens can have ALL_UPPERCASE = true.", whitespace.getBooleanFeature(BooleanFeature.ALL_UPPERCASE));
        assertFalse("Only WordTokens can have ALL_UPPERCASE = true.", number.getBooleanFeature(BooleanFeature.ALL_UPPERCASE));
        assertFalse("Only WordTokens can have ALL_UPPERCASE = true.", nonalphanum.getBooleanFeature(BooleanFeature.ALL_UPPERCASE));
    }

    @Test
    public void isPotentialBoundaryCharacter() {
        assertFalse("Wrong value.", word.getBooleanFeature(BooleanFeature.IS_POTENTIAL_BOUNDARY_CHARACTER));
        assertFalse("Wrong value.", number.getBooleanFeature(BooleanFeature.IS_POTENTIAL_BOUNDARY_CHARACTER));
        assertFalse("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.IS_POTENTIAL_BOUNDARY_CHARACTER));
        assertTrue("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.IS_POTENTIAL_BOUNDARY_CHARACTER));
        assertFalse("Wrong value.", new NonAlphanumericToken(";", 0).getBooleanFeature(BooleanFeature.IS_POTENTIAL_BOUNDARY_CHARACTER));
    }

    @Test
    public void containsOneOrMoreLinebreaks() {
        assertFalse("Wrong value.", word.getBooleanFeature(BooleanFeature.CONTAINS_ONE_OR_MORE_LINEBREAKS));
        assertFalse("Wrong value.", number.getBooleanFeature(BooleanFeature.CONTAINS_ONE_OR_MORE_LINEBREAKS));
        assertFalse("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.CONTAINS_ONE_OR_MORE_LINEBREAKS));
        assertFalse("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.CONTAINS_ONE_OR_MORE_LINEBREAKS));
        assertTrue("Wrong value.", new WhitespaceToken("\n\n  ", 0).getBooleanFeature(BooleanFeature.CONTAINS_ONE_OR_MORE_LINEBREAKS));
        assertTrue("Wrong value.", new WhitespaceToken("\n\n\n", 0).getBooleanFeature(BooleanFeature.CONTAINS_ONE_OR_MORE_LINEBREAKS));
        assertTrue("Wrong value.", new WhitespaceToken("\n\t\n", 0).getBooleanFeature(BooleanFeature.CONTAINS_ONE_OR_MORE_LINEBREAKS));
        assertTrue("Wrong value.", new WhitespaceToken("\n\t ", 0).getBooleanFeature(BooleanFeature.CONTAINS_ONE_OR_MORE_LINEBREAKS));
        assertTrue("Wrong value.", new WhitespaceToken("\n  ", 0).getBooleanFeature(BooleanFeature.CONTAINS_ONE_OR_MORE_LINEBREAKS));
        assertFalse("Wrong value.", new WhitespaceToken("  \t ", 0).getBooleanFeature(BooleanFeature.CONTAINS_ONE_OR_MORE_LINEBREAKS));
    }

    @Test
    public void containsTwoOrMoreLinebreaks() {
        assertFalse("Wrong value.", word.getBooleanFeature(BooleanFeature.CONTAINS_TWO_OR_MORE_LINEBREAKS));
        assertFalse("Wrong value.", number.getBooleanFeature(BooleanFeature.CONTAINS_TWO_OR_MORE_LINEBREAKS));
        assertFalse("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.CONTAINS_TWO_OR_MORE_LINEBREAKS));
        assertFalse("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.CONTAINS_TWO_OR_MORE_LINEBREAKS));
        assertTrue("Wrong value.", new WhitespaceToken("\n\n  ", 0).getBooleanFeature(BooleanFeature.CONTAINS_TWO_OR_MORE_LINEBREAKS));
        assertTrue("Wrong value.", new WhitespaceToken("\n\n\n", 0).getBooleanFeature(BooleanFeature.CONTAINS_TWO_OR_MORE_LINEBREAKS));
        assertTrue("Wrong value.", new WhitespaceToken("\n\t\n", 0).getBooleanFeature(BooleanFeature.CONTAINS_TWO_OR_MORE_LINEBREAKS));
        assertFalse("Wrong value.", new WhitespaceToken("\n\t ", 0).getBooleanFeature(BooleanFeature.CONTAINS_TWO_OR_MORE_LINEBREAKS));
        assertFalse("Wrong value.", new WhitespaceToken("  \t ", 0).getBooleanFeature(BooleanFeature.CONTAINS_TWO_OR_MORE_LINEBREAKS));
        assertFalse("Wrong value.", new WhitespaceToken("\n  ", 0).getBooleanFeature(BooleanFeature.CONTAINS_TWO_OR_MORE_LINEBREAKS));
    }

    @Test
    public void isPotentialBoundary() {
        assertFalse("Wrong value.", word.getBooleanFeature(BooleanFeature.IS_POTENTIAL_BOUNDARY));
        assertFalse("Wrong value.", number.getBooleanFeature(BooleanFeature.IS_POTENTIAL_BOUNDARY));
        assertFalse("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.IS_POTENTIAL_BOUNDARY));
        assertTrue("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.IS_POTENTIAL_BOUNDARY));
        assertFalse("Wrong value.", new NonAlphanumericToken(";", 0).getBooleanFeature(BooleanFeature.IS_POTENTIAL_BOUNDARY));
    }

    @Test
    public void isSingleLetter() {
        assertFalse("Wrong value.", word.getBooleanFeature(BooleanFeature.IS_SINGLE_LETTER));
        assertFalse("Wrong value.", number.getBooleanFeature(BooleanFeature.IS_SINGLE_LETTER));
        assertFalse("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.IS_SINGLE_LETTER));
        assertFalse("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.IS_SINGLE_LETTER));
        assertTrue("Wrong value.", new WordToken("a", 0).getBooleanFeature(BooleanFeature.IS_SINGLE_LETTER));
        assertTrue("Wrong value.", new WordToken("A", 0).getBooleanFeature(BooleanFeature.IS_SINGLE_LETTER));
        assertFalse("Wrong value.", new NonAlphanumericToken(";", 0).getBooleanFeature(BooleanFeature.IS_SINGLE_LETTER));
        assertFalse("Wrong value.", new NumberToken("7", 0).getBooleanFeature(BooleanFeature.IS_SINGLE_LETTER));
        assertFalse("Wrong value.", new WhitespaceToken(" ", 0).getBooleanFeature(BooleanFeature.IS_SINGLE_LETTER));
    }

    @Test
    public void isBracket() {
        assertFalse("Wrong value.", word.getBooleanFeature(BooleanFeature.IS_BRACKET));
        assertFalse("Wrong value.", number.getBooleanFeature(BooleanFeature.IS_BRACKET));
        assertFalse("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.IS_BRACKET));
        assertFalse("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.IS_BRACKET));
        assertFalse("Wrong value.", new NonAlphanumericToken(";", 0).getBooleanFeature(BooleanFeature.IS_BRACKET));
        assertTrue("Wrong value.", new NonAlphanumericToken("(", 0).getBooleanFeature(BooleanFeature.IS_BRACKET));
        assertTrue("Wrong value.", new NonAlphanumericToken(")", 0).getBooleanFeature(BooleanFeature.IS_BRACKET));
    }

    @Test
    public void isQuote() {
        assertFalse("Wrong value.", word.getBooleanFeature(BooleanFeature.IS_QUOTE));
        assertFalse("Wrong value.", number.getBooleanFeature(BooleanFeature.IS_QUOTE));
        assertFalse("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.IS_QUOTE));
        assertFalse("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.IS_QUOTE));
        assertFalse("Wrong value.", new NonAlphanumericToken(";", 0).getBooleanFeature(BooleanFeature.IS_QUOTE));
        assertTrue("Wrong value.", new NonAlphanumericToken("'", 0).getBooleanFeature(BooleanFeature.IS_QUOTE));
        assertTrue("Wrong value.", new NonAlphanumericToken("\"", 0).getBooleanFeature(BooleanFeature.IS_QUOTE));
    }

    @Test
    public void isHyphen() {
        assertFalse("Wrong value.", word.getBooleanFeature(BooleanFeature.IS_HYPHEN));
        assertFalse("Wrong value.", number.getBooleanFeature(BooleanFeature.IS_HYPHEN));
        assertFalse("Wrong value.", whitespace.getBooleanFeature(BooleanFeature.IS_HYPHEN));
        assertFalse("Wrong value.", nonalphanum.getBooleanFeature(BooleanFeature.IS_HYPHEN));
        assertFalse("Wrong value.", new NonAlphanumericToken(";", 0).getBooleanFeature(BooleanFeature.IS_HYPHEN));
        assertTrue("Wrong value.", new NonAlphanumericToken("-", 0).getBooleanFeature(BooleanFeature.IS_HYPHEN));
        assertTrue("Wrong value.", new NonAlphanumericToken("‐", 0).getBooleanFeature(BooleanFeature.IS_HYPHEN));
    }

    @Test
    public void containsVowel() {
        assertFalse("Wrong value.", new WordToken("Hrmpf", 0).getBooleanFeature(BooleanFeature.CONTAINS_VOWEL));
        assertTrue("Wrong value.", new WordToken("a", 0).getBooleanFeature(BooleanFeature.CONTAINS_VOWEL));
        assertTrue("Wrong value.", new WordToken("A", 0).getBooleanFeature(BooleanFeature.CONTAINS_VOWEL));
        assertTrue("Wrong value.", new WordToken("Argh", 0).getBooleanFeature(BooleanFeature.CONTAINS_VOWEL));
        assertTrue("Wrong value.", new WordToken("blurb", 0).getBooleanFeature(BooleanFeature.CONTAINS_VOWEL));
        assertFalse("Only WordTokens can have CONTAINS_VOWEL = true.", whitespace.getBooleanFeature(BooleanFeature.CONTAINS_VOWEL));
        assertFalse("Only WordTokens can have CONTAINS_VOWEL = true.", number.getBooleanFeature(BooleanFeature.CONTAINS_VOWEL));
        assertFalse("Only WordTokens can have CONTAINS_VOWEL = true.", nonalphanum.getBooleanFeature(BooleanFeature.CONTAINS_VOWEL));
    }

    @Test
    public void containsConsonant() {
        assertTrue("Wrong value.", new WordToken("Hrmpf", 0).getBooleanFeature(BooleanFeature.CONTAINS_CONSONANT));
        assertFalse("Wrong value.", new WordToken("a", 0).getBooleanFeature(BooleanFeature.CONTAINS_CONSONANT));
        assertFalse("Wrong value.", new WordToken("A", 0).getBooleanFeature(BooleanFeature.CONTAINS_CONSONANT));
        assertTrue("Wrong value.", new WordToken("Argh", 0).getBooleanFeature(BooleanFeature.CONTAINS_CONSONANT));
        assertTrue("Wrong value.", new WordToken("blurb", 0).getBooleanFeature(BooleanFeature.CONTAINS_CONSONANT));
        assertFalse("Only WordTokens can have CONTAINS_VOWEL = true.", whitespace.getBooleanFeature(BooleanFeature.CONTAINS_CONSONANT));
        assertFalse("Only WordTokens can have CONTAINS_VOWEL = true.", number.getBooleanFeature(BooleanFeature.CONTAINS_CONSONANT));
        assertFalse("Only WordTokens can have CONTAINS_VOWEL = true.", nonalphanum.getBooleanFeature(BooleanFeature.CONTAINS_CONSONANT));
    }

    @Test
    public void isComma() {
        assertTrue("Wrong value.", new NonAlphanumericToken(",", 0).getBooleanFeature(BooleanFeature.IS_COMMA));
        assertFalse("Wrong value.", new NonAlphanumericToken(";", 0).getBooleanFeature(BooleanFeature.IS_COMMA));
        assertFalse("Only NonAlphanumeric tokens can have IS_COMMA = true.", whitespace.getBooleanFeature(BooleanFeature.IS_COMMA));
        assertFalse("Only NonAlphanumeric tokens can have IS_COMMA = true.", word.getBooleanFeature(BooleanFeature.IS_COMMA));
        assertFalse("Only NonAlphanumeric tokens can have IS_COMMA = true.", number.getBooleanFeature(BooleanFeature.IS_COMMA));
    }

    @Test
    public void isColon() {
        assertTrue("Wrong value.", new NonAlphanumericToken(":", 0).getBooleanFeature(BooleanFeature.IS_COLON));
        assertFalse("Wrong value.", new NonAlphanumericToken(";", 0).getBooleanFeature(BooleanFeature.IS_COLON));
        assertFalse("Only NonAlphanumeric tokens can have IS_COLON = true.", whitespace.getBooleanFeature(BooleanFeature.IS_COLON));
        assertFalse("Only NonAlphanumeric tokens can have IS_COLON = true.", word.getBooleanFeature(BooleanFeature.IS_COLON));
        assertFalse("Only NonAlphanumeric tokens can have IS_COLON = true.", number.getBooleanFeature(BooleanFeature.IS_COLON));
    }

    @Test
    public void isPeriod() {
        assertTrue("Wrong value.", new NonAlphanumericToken(".", 0).getBooleanFeature(BooleanFeature.IS_PERIOD));
        assertFalse("Wrong value.", new NonAlphanumericToken(";", 0).getBooleanFeature(BooleanFeature.IS_PERIOD));
        assertFalse("Only NonAlphanumeric tokens can have IS_COLON = true.", whitespace.getBooleanFeature(BooleanFeature.IS_PERIOD));
        assertFalse("Only NonAlphanumeric tokens can have IS_COLON = true.", word.getBooleanFeature(BooleanFeature.IS_PERIOD));
        assertFalse("Only NonAlphanumeric tokens can have IS_COLON = true.", number.getBooleanFeature(BooleanFeature.IS_PERIOD));
    }
}

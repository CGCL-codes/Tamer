package com.google.i18n.pseudolocalization.methods;

import com.google.i18n.pseudolocalization.PseudolocalizationException;
import com.google.i18n.pseudolocalization.PseudolocalizationPipeline;
import com.google.i18n.pseudolocalization.PseudolocalizationTestCase;
import java.util.HashSet;
import java.util.Set;

/**
 * Test for {@link Accenter}.
 */
public class AccenterTest extends PseudolocalizationTestCase {

    private static Set<Character> skipCheck;

    static {
        skipCheck = new HashSet<Character>();
        skipCheck.add('0');
        skipCheck.add('1');
        skipCheck.add('2');
        skipCheck.add('3');
        skipCheck.add('4');
        skipCheck.add('5');
        skipCheck.add('6');
        skipCheck.add('7');
        skipCheck.add('8');
        skipCheck.add('9');
    }

    public void testAllAccented() throws PseudolocalizationException {
        PseudolocalizationPipeline pipeline = PseudolocalizationPipeline.buildPipeline("accents");
        for (char ch = 0x20; ch < 0x7F; ++ch) {
            String result = runPipeline(pipeline, String.valueOf(ch));
            assertGoodSubstitution(ch, result);
        }
    }

    public void testAllExtended() throws PseudolocalizationException {
        PseudolocalizationPipeline pipeline = PseudolocalizationPipeline.buildPipeline("accents:extended");
        for (char ch = 0x20; ch < 0x7F; ++ch) {
            String result = runPipeline(pipeline, String.valueOf(ch));
            assertGoodSubstitution(ch, result);
        }
    }

    public void testOnce() throws PseudolocalizationException {
        PseudolocalizationPipeline pipeline = PseudolocalizationPipeline.buildPipeline("accents");
        assertEquals("Ĥéļļö <br> ţĥéŕé", runPreparsedHtml(pipeline));
    }

    public void testTwice() throws PseudolocalizationException {
        PseudolocalizationPipeline pipeline = PseudolocalizationPipeline.buildPipeline("accents", "accents");
        assertEquals("Ĥéļļö <br> ţĥéŕé", runPreparsedHtml(pipeline));
    }

    /**
   * Assert that the supplied substitution is a good one.  Checks:
   * <ul>
   * <li>The character is changed
   * <li>The basic character properties remain the same (letter, uppercase,
   *     lowercase, digit, space characte)
   * <li>If a digit, the numeric value remains the same
   * </ul>  
   * 
   * @param ch
   * @param substitution
   */
    private void assertGoodSubstitution(char ch, String substitution) {
        String charName = "0x" + Integer.toHexString(ch) + " (" + ch + ')';
        switch(substitution.length()) {
            case 0:
                fail("empty substitution for " + charName);
                break;
            case 1:
                char actualChar = substitution.charAt(0);
                assertTrue(charName + " didn't change", ch != actualChar);
                if (!skipCheck.contains(ch)) {
                    assertEquals("isLetter(" + charName + ")", Character.isLetter(ch), Character.isLetter(actualChar));
                    assertEquals("isUpperCase(" + charName + ")", Character.isUpperCase(ch), Character.isUpperCase(actualChar));
                    assertEquals("isLowerCase(" + charName + ")", Character.isLowerCase(ch), Character.isLowerCase(actualChar));
                    assertEquals("isDigit(" + charName + ")", Character.isDigit(ch), Character.isDigit(actualChar));
                    assertEquals("isSpaceChar(" + charName + ")", Character.isSpaceChar(ch), Character.isSpaceChar(actualChar));
                    if (Character.isDigit(ch)) {
                        assertEquals("getNumericValue(" + charName + ")", Character.getNumericValue(ch), Character.getNumericValue(actualChar));
                    }
                }
                break;
            default:
                break;
        }
    }
}

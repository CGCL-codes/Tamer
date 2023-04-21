package org.kaveh.commons.farsi.word;

import org.kaveh.commons.farsi.letter.LatinLetter;

/**
 * LatinWord is a place holder for methods which will manipulate a single word.
 * 
 * @author Kaveh Ranjbar
 * 
 */
public class LatinWord {

    public static String replaceFarsiDigit(String stringWithFarsiDigits) {
        String result = "";
        for (int i = 0; i < stringWithFarsiDigits.length(); i++) {
            result = result + LatinLetter.replaceFarsiDigitWithLatin(stringWithFarsiDigits.charAt(i));
        }
        return result;
    }
}

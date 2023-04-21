package org.kaveh.commons.farsi.letter;

import org.kaveh.commons.farsi.constatns.FarsiLetterConstants;
import org.kaveh.commons.farsi.constatns.LatinLetterConstants;
import org.kaveh.commons.farsi.providers.LatinLetterProvider;
import org.kaveh.commons.farsi.utils.ConstantReflectionUtilies;

/**
 * Latin Letter is a place holder for static methods which will provide services
 * at letter level.
 * 
 * @author Kaveh Ranjbar
 * 
 */
public class LatinLetter {

    public static boolean isLatinDigit(char inputLetter) {
        return (LatinLetterProvider.getDigits().contains(String.valueOf(inputLetter))) ? true : false;
    }

    public static char replaceFarsiDigitWithLatin(char inputDigit) {
        char result = inputDigit;
        if (FarsiLetter.isFarsiDigit(inputDigit)) {
            result = ConstantReflectionUtilies.findMatchByTranslation(inputDigit, FarsiLetterConstants.Digits.class, LatinLetterConstants.Digits.class);
        }
        return result;
    }
}

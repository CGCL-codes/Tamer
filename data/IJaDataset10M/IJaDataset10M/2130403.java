package org.aitools.programd.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * <code>InputNormalizer</code> replaces <code>Substituter</code> as the
 * utility class for performing various stages of <a
 * href="http://aitools.org/aiml/TR/2001/WD-aiml/#section-input-normalization">input
 * normalization </a>. Substitutions of other types are now handled
 * independently by their respective processors.
 * 
 * @since 4.1.3
 */
public class InputNormalizer {

    /** An empty string. */
    private static final String EMPTY_STRING = "";

    /**
     * Splits an input into sentences, as defined by the
     * <code>sentenceSplitters</code>.
     * 
     * @param sentenceSplitters the sentence splitters to use
     * @param input the input to split
     * @return the input split into sentences
     */
    @SuppressWarnings("boxing")
    public static List<String> sentenceSplit(List<String> sentenceSplitters, String input) {
        if (input == null) {
            return null;
        }
        List<String> result = Collections.checkedList(new ArrayList<String>(), String.class);
        int inputLength = input.length();
        if (inputLength == 0) {
            result.add(EMPTY_STRING);
            return result;
        }
        ArrayList<Integer> splitterIndices = new ArrayList<Integer>();
        for (String splitter : sentenceSplitters) {
            int index = input.indexOf(splitter);
            while (index != -1) {
                splitterIndices.add(new Integer(index));
                index = input.indexOf(splitter, index + 1);
            }
        }
        if (splitterIndices.size() == 0) {
            result.add(input);
            return result;
        }
        Collections.sort(splitterIndices);
        ListIterator<Integer> indices = splitterIndices.listIterator();
        int previousIndex = indices.next();
        while (indices.hasNext()) {
            int nextIndex = indices.next();
            if (nextIndex == previousIndex + 1) {
                indices.previous();
                indices.previous();
                indices.remove();
            }
            previousIndex = nextIndex;
        }
        indices = splitterIndices.listIterator();
        int startIndex = 0;
        int endIndex = inputLength - 1;
        while (indices.hasNext()) {
            endIndex = indices.next();
            result.add(input.substring(startIndex, endIndex + 1).trim());
            startIndex = endIndex + 1;
        }
        if (startIndex < inputLength - 1) {
            result.add(input.substring(startIndex).trim());
        }
        return result;
    }

    /**
     * <p>
     * Performs <a
     * href="http://aitools.org/aiml/TR/2001/WD-aiml/#section-pattern-fitting-normalizations">pattern-fitting
     * </a> normalization on an input.
     * </p>
     * <p>
     * This is best used to produce a caseless representation of the effective
     * match string when presenting match results to a user; however, if used
     * when actually performing the match it will result in the case of
     * wildcard-captured values being lost. Some amendment of the specification
     * is probably in order.
     * </p>
     * 
     * @param input the string to pattern-fit
     * @return the pattern-fitted input
     */
    public static String patternFit(String input) {
        input = XMLKit.removeMarkup(input);
        StringCharacterIterator iterator = new StringCharacterIterator(input);
        StringBuilder result = new StringBuilder(input.length());
        for (char aChar = iterator.first(); aChar != CharacterIterator.DONE; aChar = iterator.next()) {
            if (!Character.isLetterOrDigit(aChar) && aChar != '*' && aChar != '_') {
                result.append(' ');
            } else {
                result.append(Character.toUpperCase(aChar));
            }
        }
        return result.toString();
    }

    /**
     * <p>
     * Performs a partial <a
     * href="http://aitools.org/aiml/TR/2001/WD-aiml/#section-pattern-fitting-normalizations">pattern-fitting
     * </a> normalization on an input -- partial because it does <i>not </i>
     * convert letters to uppercase.
     * </p>
     * <p>
     * This is used when sending patterns to the Graphmaster so that wildcard
     * contents can be captured and case retained. Some amendment of the
     * specification is probably in order.
     * </p>
     * 
     * @param input the string to pattern-fit
     * @return the pattern-fitted input
     */
    public static String patternFitIgnoreCase(String input) {
        input = XMLKit.removeMarkup(input);
        StringCharacterIterator iterator = new StringCharacterIterator(input);
        StringBuilder result = new StringBuilder(input.length());
        for (char aChar = iterator.first(); aChar != CharacterIterator.DONE; aChar = iterator.next()) {
            if (!Character.isLetterOrDigit(aChar)) {
                result.append(' ');
            } else {
                result.append(aChar);
            }
        }
        return result.toString();
    }
}

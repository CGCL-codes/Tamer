package util;

import java.util.*;

/**
 * A comparator that compares strings that might contain one or more
 * numeric substrings, considering their numeric values in addition to
 * their usual alphabetical order.
 */
public class NumericStringComparator implements Comparator {

    /**
     * Compares two strings to see which is greater, taking into
     * account any numbers starting at identical indices within the strings.
     * For instance, according to this comparator "x9" comes
     * before "x10", not after as would be dictated by a pure
     * alphabetical sort.
     *
     * See parent method for parameter and return value descriptions.
     */
    public int compare(Object o1, Object o2) {
        String a = (String) o1, b = (String) o2;
        int aLength = a.length(), bLength = b.length();
        int lengthToCompare = Math.min(aLength, bLength);
        int alphaResult = 0;
        char aChar = 0, bChar = 0;
        for (int i = 0; i < lengthToCompare; i++) {
            aChar = a.charAt(i);
            bChar = b.charAt(i);
            boolean aInNum = Character.isDigit(aChar), bInNum = Character.isDigit(bChar);
            if (aInNum && bInNum) {
                if (alphaResult == 0 && aChar != bChar) {
                    alphaResult = (aChar < bChar) ? -1 : 1;
                }
            } else if (alphaResult != 0) {
                if (aInNum) return 1;
                if (bInNum) return -1;
                if (!aInNum && !bInNum) return alphaResult;
            } else {
                if (aChar != bChar) return (aChar < bChar) ? -1 : 1;
            }
        }
        if (aLength == bLength) return (aChar == bChar) ? 0 : ((aChar < bChar) ? -1 : 1);
        return (aLength < bLength) ? -1 : 1;
    }
}

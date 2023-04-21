package org.skunk.swing.text;

import javax.swing.text.*;

/**
 * an EntryFilter that accepts only positive integers within the given range
 */
public class PositiveIntegerEntryFilter implements EntryFilter {

    private int min, max;

    /**
     * constructs a filter with the given minimum and maximum values.
     * @param min the minimum accepted value
     * @param max the maximum accepted value
     */
    public PositiveIntegerEntryFilter(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean accepts(int offs, String str, AttributeSet a, String wholeString) {
        try {
            char[] charArray = str.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (c < '0' || c > '9') return false;
            }
            StringBuffer sb = new StringBuffer(wholeString);
            sb.insert(offs, str);
            String newString = sb.toString();
            if (newString.startsWith("0") && newString.length() > 1) return false;
            int parsed = Integer.parseInt(sb.toString());
            if (parsed < min || parsed > max) return false;
        } catch (NumberFormatException nafta) {
            return false;
        }
        return true;
    }
}

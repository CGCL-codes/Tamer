package org.gaugebook.swing;

import javax.swing.*;
import javax.swing.text.*;

/**
 * The class JFrequencyTextField
 *
 * @author Michael Nagler
 * @version 1.0
 */
public class JFrequencyTextField extends JTextField {

    public JFrequencyTextField(int cols) {
        super(cols);
        setHorizontalAlignment(super.RIGHT);
        setDocument(new FrequencyDocument(cols));
    }

    static class FrequencyDocument extends PlainDocument {

        private int maxlen;

        public FrequencyDocument(int maxlen) {
            this.maxlen = maxlen;
        }

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            }
            char[] nums = str.toCharArray();
            char[] result = new char[nums.length];
            int count = 0;
            for (int i = 0; i < nums.length && getLength() + 1 <= maxlen; i++) {
                if (Character.isDigit(nums[i]) || nums[i] == '.') {
                    result[count++] = nums[i];
                }
            }
            super.insertString(offs, new String(nums, 0, count), a);
        }
    }
}

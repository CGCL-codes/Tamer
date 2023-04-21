package org.magiclight.common;

import java.awt.Color;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

/**
 *
 * @author Mtec030
 */
public class LongInputVerifier extends InputVerifier {

    private long lo = Long.MIN_VALUE;

    private long hi = Long.MAX_VALUE;

    private Color err = Color.RED;

    private Color org = null;

    /** Creates a new instance of IntegerInputVerifier
	 * @param lo
	 * @param hi
	 */
    public LongInputVerifier(long lo, long hi) {
        this.lo = lo;
        this.hi = hi;
    }

    public boolean verify(JComponent input) {
        if (input instanceof JFormattedTextField) {
            if (org == null) org = input.getBackground();
            JFormattedTextField ftf = (JFormattedTextField) input;
            JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
            if (formatter != null) {
                String text = ftf.getText();
                long v = MLUtil.asLong(text);
                if (v < lo || v > hi) {
                    input.setBackground(err);
                    return (false);
                }
                input.setBackground(org);
                return (true);
            }
            input.setBackground(org);
        }
        return (true);
    }
}

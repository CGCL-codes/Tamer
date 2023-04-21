package com.nepxion.swing.textfield.number;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import com.nepxion.swing.textfield.JBasicTextField;
import com.nepxion.swing.tip.balloon.JBalloonTip;

public class JNumberTextField extends JBasicTextField {

    public JNumberTextField() {
        this(Double.MIN_VALUE);
    }

    public JNumberTextField(double minimumValue) {
        this(minimumValue, Double.MAX_VALUE);
    }

    public JNumberTextField(int maximumLength, int decimalLength) {
        this(maximumLength, decimalLength, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public JNumberTextField(double minimumValue, double maximumValue) {
        this(NumberDocument.MAXIMUM_LENGTH, 0, minimumValue, maximumValue);
    }

    public JNumberTextField(int maximumLength, int decimalLength, double minimumValue, double maximumValue) {
        super();
        setDocument(new NumberDocument(maximumLength, decimalLength, minimumValue, maximumValue) {

            public void insertString(int offset, String value, AttributeSet attributeSet) throws BadLocationException {
                boolean isExclude = exclude(value);
                if (isExclude) {
                    return;
                }
                try {
                    super.insertString(offset, value, attributeSet);
                } catch (NumberFormatException e) {
                    showTip(e.getMessage(), JBalloonTip.TIP_ICON_ERROR);
                } catch (IllegalArgumentException e) {
                    showTip(e.getMessage(), JBalloonTip.TIP_ICON_WARNING);
                }
            }
        });
    }

    public boolean exclude(String value) {
        return false;
    }
}

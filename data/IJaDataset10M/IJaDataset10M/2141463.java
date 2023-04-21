package ise.calculator;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.BorderFactory;

/**
 * A 48 x 30 JButton. Note that 48/30 = 1.6, which is very close to the
 * golden ratio, so certainly these buttons look very good.
 */
public class RectangleButton extends JButton {

    private Dimension size = new Dimension(48, 30);

    public RectangleButton(String text) {
        super(text);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    public Dimension getPreferredSize() {
        return size;
    }

    public Dimension getMinimumSize() {
        return size;
    }

    public Dimension getMaximumSize() {
        return size;
    }
}

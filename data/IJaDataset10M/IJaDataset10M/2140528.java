package org.rapla.gui.toolkit;

import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.Action;

public class RaplaButton extends JButton {

    private static final long serialVersionUID = 1L;

    public static int SMALL = -1;

    public static int LARGE = 1;

    public static int DEFAULT = 0;

    private static Insets smallInsets = new Insets(0, 0, 0, 0);

    private static Insets largeInsets = new Insets(5, 10, 5, 10);

    public RaplaButton(String text, int style) {
        this(style);
        setText(text);
    }

    public RaplaButton(int style) {
        if (style == SMALL) {
            setMargin(smallInsets);
        } else if (style == LARGE) {
            setMargin(largeInsets);
        } else {
            setMargin(null);
        }
    }

    public void setAction(Action action) {
        String oldText = null;
        if (action.getValue(Action.NAME) == null) oldText = getText();
        super.setAction(action);
        if (oldText != null) setText(oldText);
    }

    public RaplaButton() {
    }
}

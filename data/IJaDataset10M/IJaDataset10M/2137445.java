package ch.fork.AdHocRailway.ui;

import java.awt.Insets;
import javax.swing.Action;
import javax.swing.JButton;

public class SmallToolbarButton extends JButton {

    private static final Insets insets = new Insets(1, 1, 1, 1);

    public SmallToolbarButton(Action a) {
        super(a);
        setMargin(insets);
        setFocusable(false);
        setText("");
    }
}

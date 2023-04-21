package org.betex.gui;

import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;

public class SmallButton extends JButton {

    public SmallButton(String text) {
        this(text, 10);
    }

    public SmallButton(String text, float size) {
        super(text);
        setMargin(new Insets(0, 7, 0, 7));
        setFont(getFont().deriveFont(Font.BOLD, size));
    }
}

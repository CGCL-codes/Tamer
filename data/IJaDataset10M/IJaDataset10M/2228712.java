package view;

import javax.swing.*;

public class VJButton extends JButton implements IStyles {

    public VJButton() {
        super();
        this.setFont(FONT_L);
    }

    public VJButton(String label) {
        this();
        this.setText(label);
    }
}

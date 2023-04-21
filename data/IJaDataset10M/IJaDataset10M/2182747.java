package com.leclercb.taskunifier.gui.swing.buttons;

import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.leclercb.commons.gui.swing.layouts.WrapLayout;

public class TUButtonsPanel extends JPanel {

    private boolean removeText;

    public TUButtonsPanel(JButton... buttons) {
        this(false, buttons);
    }

    public TUButtonsPanel(boolean removeText, JButton... buttons) {
        this.removeText = removeText;
        this.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        this.setLayout(new WrapLayout(FlowLayout.RIGHT));
        if (buttons == null) return;
        for (JButton button : buttons) this.addButton(button);
    }

    public void addButton(JButton button) {
        if (button == null) return;
        if (this.removeText) button.setText("");
        this.add(button);
    }
}

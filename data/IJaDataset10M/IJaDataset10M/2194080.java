package org.homeunix.thecave.moss.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;

public class MossTristateCheckBox extends JCheckBox {

    public static final long serialVersionUID = 0;

    public static final int UNSELECTED = 0;

    public static final int SELECTED = 1;

    public static final int PARTIALLY_SELECTED = 2;

    private int state;

    private boolean enabled = true;

    public MossTristateCheckBox(String label) {
        super(label);
        this.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent arg0) {
                if (enabled) MossTristateCheckBox.this.setValue((state + 1) % 2);
            }
        });
    }

    public int getValue() {
        return state;
    }

    public void setValue(int value) {
        if (value >= 0 && value <= 2) state = value; else value = 2;
        if (value == 0) {
            super.setSelected(false);
            super.setEnabled(true);
        } else if (value == 1) {
            super.setSelected(true);
            super.setEnabled(true);
        } else {
            super.setSelected(true);
            super.setEnabled(false);
        }
    }

    @Override
    public void setEnabled(boolean arg0) {
        enabled = arg0;
        super.setEnabled(arg0);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setSelected(boolean arg0) {
        setValue(SELECTED);
    }

    @Override
    public boolean isSelected() {
        return (getValue() != UNSELECTED);
    }
}

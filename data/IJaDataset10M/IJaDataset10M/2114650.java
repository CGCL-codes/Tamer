package org.greyfire.transcendancy.ui;

import java.awt.Color;
import javax.swing.JPanel;

public abstract class SecondaryPanel extends JPanel {

    public SecondaryPanel() {
        super(true);
        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);
    }

    public SecondaryPanel(boolean doublebuffer) {
        super(doublebuffer);
        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);
    }

    public abstract void onOpen();

    public abstract void onClose();

    public abstract void redraw();
}

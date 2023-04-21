package de.laidback.racoon.gui.impl.swing;

import javax.swing.JFrame;
import de.laidback.racoon.gui.IGui;

public class SimpleSwingGUIStarter implements IGui {

    /**
	 * Startet die interne grafische Oberfl�che.
	 */
    private JFrame frame = null;

    private boolean ready = false;

    public void show() {
        if (frame == null) {
            frame = new MainFrame();
        }
        if (!frame.isVisible()) {
            frame.setLocation(100, 100);
            frame.pack();
            frame.setSize(600, 400);
            frame.setVisible(true);
        } else {
            frame.requestFocus();
        }
        ready = true;
    }

    public String getComponentTitle() {
        return "Simple Swing Debug GUI";
    }

    public String getComponentVersion() {
        return "0.0";
    }

    public String getComponentVendor() {
        return "Thomas Berger";
    }

    public boolean isReady() {
        return this.ready;
    }

    public void update() {
    }
}

package home.jes.ui.jazz.swing;

import javax.swing.*;

public class JMenuBarTagHandler extends JComponentTagHandler {

    public JMenuBarTagHandler() {
        super(JMenuBar.class);
    }

    public void addChild(Object parent, Object child) {
        if (child instanceof JMenu) {
            JMenuBar menuBar = (JMenuBar) parent;
            menuBar.add((JMenu) child);
        } else {
            super.addChild(parent, child);
        }
    }
}

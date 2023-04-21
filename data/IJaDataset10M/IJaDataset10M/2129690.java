package ui.menu;

import javax.swing.JMenu;

@SuppressWarnings("serial")
public class HelpMenu extends JMenu {

    public HelpMenu() {
        super("Help");
        add(new AboutItem());
    }
}

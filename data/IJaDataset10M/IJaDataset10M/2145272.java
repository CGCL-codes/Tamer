package gui.menu.bonuses;

import javax.swing.JMenu;

public class BonusesMenu extends JMenu {

    public BonusesMenu() {
        super("Bonuses");
        add(new PortalsMenuItem());
    }
}

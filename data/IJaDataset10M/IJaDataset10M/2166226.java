package gui.menu.context;

import javax.swing.JPopupMenu;

public class EntityMenu extends JPopupMenu {

    public EntityMenu() {
        super();
        add(new PlanetPropertiesMenuItem());
        add(new SetOrbitMenuItem());
        add(new DeleteEntityMenuItem());
    }
}

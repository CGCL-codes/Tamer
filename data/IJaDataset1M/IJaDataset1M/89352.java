package frames.controls;

import java.awt.MenuItem;
import java.awt.MenuShortcut;
import menu.TypedMenuItem;

/**
 * Dieses Menue ist ein eigens an dieses Programm
 * angepasstes MenueItem
 * 
 * @author Etzlstorfer Andreas
 *
 */
public class DrawMenuItem extends MenuItem implements TypedMenuItem {

    public static final long serialVersionUID = 0;

    /**
     * Typ des Menues
     */
    private DrawMenuItemType type;

    /**
     * Konstruktor der Klasse DrawMenuItem
     * 
     * @param label Titel des Menues
     * @param type Typ des Menues
     */
    public DrawMenuItem(String label, DrawMenuItemType type) {
        super(label);
        this.type = type;
    }

    /**
     * Konstruktor der Klasse DrawMenuItem
     * 
     * @param label Titel des Menues
     * @param key Shortkey fuer dieses Menue
     * @param type Typ des Menues
     */
    public DrawMenuItem(String label, int key, DrawMenuItemType type) {
        super(label, new MenuShortcut(key));
        this.type = type;
    }

    /**
     * Konstruktor der Klasse DrawMenuItem
     * 
     * @param label Titel des Menues
     * @param s Shortkey fuer dieses Menue
     * @param type Typ des Menues
     */
    public DrawMenuItem(String label, MenuShortcut s, DrawMenuItemType type) {
        super(label, s);
        this.type = type;
    }

    /**
     * Konstruktor der Klasse DrawMenuItem
     * 
     * @param label Titel des Menues
     */
    public DrawMenuItem(String label) {
        super(label);
        this.type = DrawMenuItemType.SEPERATOR;
    }

    public DrawMenuItemType getType() {
        return type;
    }

    public void setType(DrawMenuItemType type) {
        this.type = type;
    }
}

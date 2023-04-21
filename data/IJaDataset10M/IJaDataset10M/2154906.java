package frames.controls;

import geometry.base.SelectableEntity;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Diese Klasse ist ein einfacher JButton, besitzt jedoch
 * ein Entity, das auf Knopfdruck dann ins Hauptfenster
 * eingefuegt werden soll
 * 
 * @author Etzlstorfer Andreas, Manfred Schwarz
 *
 */
public class ToolEntityButton extends JButton {

    private static final long serialVersionUID = 0;

    /**
     * Entity zu dem dieser Button gesetzt ist
     */
    private SelectableEntity ent;

    /**
     * Konstruktor der Klasse Toolbutton
     * 
     * @param text Text des Buttons
     * @param icon Icon des Buttons
     * @param ent Entity das zu diesem Button gehoert
     */
    public ToolEntityButton(String text, Icon icon, SelectableEntity ent) {
        super(text, icon);
        this.ent = ent;
    }

    /**
     * Gibt das Entity zurueck, an dem der Toolbutton besetzt ist
     * 
     * @return Entity
     */
    public SelectableEntity getEntity() {
        return ent;
    }
}

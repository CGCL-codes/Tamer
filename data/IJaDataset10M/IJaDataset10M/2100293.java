package jshomeorg.simplytrain.gui.renderer;

import javax.swing.JComboBox;
import jshomeorg.simplytrain.service.route;

/**
 *
 * @author js
 */
public class routeCBfiller implements comboboxeditorfiller {

    /** Creates a new instance of routeCBfiller */
    public routeCBfiller() {
    }

    public void fill(JComboBox cb) {
        for (route r : route.allroutes.values()) {
            cb.addItem(r);
        }
    }
}

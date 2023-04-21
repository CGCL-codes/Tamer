package cw.boardingschoolmanagement.gui.component;

import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author ManuelG
 */
public class CWComboBox extends JComboBox {

    CWComboBox() {
    }

    CWComboBox(Vector<?> items) {
        super(items);
    }

    CWComboBox(Object[] items) {
        super(items);
    }

    CWComboBox(ComboBoxModel aModel) {
        super(aModel);
    }
}

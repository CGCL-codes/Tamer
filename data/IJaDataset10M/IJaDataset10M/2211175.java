package org.gvsig.gui.beans.swing;

import java.awt.GraphicsEnvironment;
import javax.swing.JComboBox;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class JComboBoxFonts extends JComboBox {

    private static final long serialVersionUID = -1096327332248927959L;

    public JComboBoxFonts() {
        super();
        initialize();
    }

    private void initialize() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        for (int i = 0; i < fontNames.length; i++) {
            addItem(fontNames[i]);
        }
        ;
    }
}

package org.openejb.ui.jedi.openejb11.ejb.gui;

import java.awt.GridBagLayout;
import javax.swing.JComponent;

/**
 * GUI for editing top-level OpenEJB JAR properties.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.3 $
 */
public class PluginEditor extends JComponent {

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    private boolean modified = false;

    public PluginEditor() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeEvents();
    }

    private void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);
    }

    private void initializeEvents() {
    }

    public void clearModified() {
        modified = false;
    }

    public boolean isModified() {
        return modified;
    }
}

package org.jdmp.gui.dataset;

import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JMenu;
import org.ujmp.core.interfaces.GUIObject;

public class ClusterDataSetMenu extends JMenu {

    private static final long serialVersionUID = -8739876327447006598L;

    public ClusterDataSetMenu(JComponent component, DataSetGUIObject o, GUIObject owner) {
        super("Clustering");
        setMnemonic(KeyEvent.VK_C);
    }
}

package org.jdmp.gui.dataset;

import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JMenu;
import org.ujmp.core.interfaces.GUIObject;

public class ClassifyDataSetMenu extends JMenu {

    private static final long serialVersionUID = -3617725656170970206L;

    public ClassifyDataSetMenu(JComponent component, DataSetGUIObject o, GUIObject owner) {
        super("Classifier");
        setMnemonic(KeyEvent.VK_C);
        add(new ClassifyJDMPMenu(component, o, owner));
        add(new ClassifyWekaMenu(component, o, owner));
        add(new ClassifyLibSVMMenu(component, o, owner));
        add(new ClassifyLibLinearMenu(component, o, owner));
    }
}

package org.tidelaget.gui.action;

import org.tidelaget.gui.*;
import org.tidelaget.gui.panel.*;
import org.tidelaget.gui.tree.*;
import java.awt.event.*;
import javax.swing.*;

public class RunClassAction extends TAction {

    public RunClassAction(String actionMapping) {
        super(actionMapping, "Run class");
    }

    public void actionPerformed(ActionEvent e) {
        TIDENode node = IDEController.get().getLastSelectedNode();
        if (node instanceof ClassNode) ((ClassNode) node).run();
    }
}

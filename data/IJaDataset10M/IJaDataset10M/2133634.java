package org.tide.gui.action;

import org.tidelaget.gui.*;
import org.tidelaget.gui.action.*;
import org.tidelaget.gui.tree.event.*;
import org.tidelaget.gui.panel.*;
import org.tidelaget.gui.tree.*;
import org.tide.gui.*;
import org.tide.gui.tree.*;
import org.tide.tomcataccess.*;
import org.tide.webapp.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class AddRoleToSCAction extends TAction {

    public AddRoleToSCAction(String actionMapping) {
        super(actionMapping, "Add role to security constraint");
    }

    public void actionPerformed(ActionEvent e) {
        org.tidelaget.core.Logger.println("AddRoleToSCAction");
        TTreeEvent te = (TTreeEvent) e;
        TIDENode[] sourceNodes = te.getSourceNodes();
        TIDESecurityConstraintNode scNode = (TIDESecurityConstraintNode) te.getDestNode();
        for (int i = 0; i < sourceNodes.length; i++) {
            TIDERoleNode roleNode = (TIDERoleNode) sourceNodes[i];
            org.tidelaget.core.Logger.println("AddRoleToSCAction - adding role " + roleNode + " to sc " + scNode);
            ((SecurityConstraint) scNode.getUserObject()).addRole((Role) roleNode.getUserObject());
            TIDETree tt = te.getDestTree();
            ((TIDETreeModel) tt.getModel()).insertNodeInto(roleNode, tt.findNodeRecurse("Roles", scNode), 0);
        }
    }
}

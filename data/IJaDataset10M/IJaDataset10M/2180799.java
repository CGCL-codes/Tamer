package org.gvt.action;

import java.util.*;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.gvt.model.CompoundModel;
import org.gvt.model.NodeModel;
import org.gvt.ChisioMain;
import org.gvt.command.*;
import org.gvt.editpart.*;

/**
 * Action for deleting the compound node without deleting the inner nodes.
 * Children are kept, they are taken out from compound node and stay in the same
 * absolute location.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class CreateCompoundFromSelectedAction extends Action {

    CompoundModel root;

    ChisioMain main;

    /**
	 * Constructor
	 */
    public CreateCompoundFromSelectedAction(ChisioMain main) {
        super("Create Compound from Selected");
        this.setToolTipText("Create Compound from Selected");
        this.main = main;
    }

    public void run() {
        ScrollingGraphicalViewer viewer = main.getViewer();
        Iterator selectedObjects = ((IStructuredSelection) viewer.getSelection()).iterator();
        root = (CompoundModel) ((ChsRootEditPart) viewer.getRootEditPart().getChildren().get(0)).getModel();
        List nodes = new ArrayList();
        boolean createCompound = true;
        while (selectedObjects.hasNext()) {
            Object editpart = selectedObjects.next();
            if (editpart instanceof ChsNodeEditPart) {
                NodeModel node = ((ChsNodeEditPart) editpart).getNodeModel();
                if (node.getParentModel() == root) {
                    nodes.add(node);
                } else {
                    createCompound = false;
                    break;
                }
            }
        }
        if (createCompound) {
            CreateCompoundFromSelectedCommand command = new CreateCompoundFromSelectedCommand(nodes);
            command.execute();
            main.getHighlightLayer().refreshHighlights();
        } else {
            MessageBox box = new MessageBox(main.getShell(), SWT.ICON_WARNING);
            box.setMessage("All selected nodes should belong to the root graph!");
            box.setText("Chisio");
            box.open();
        }
    }
}

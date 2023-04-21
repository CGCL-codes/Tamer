package org.yawlfoundation.yawl.editor.actions.element;

import org.yawlfoundation.yawl.editor.actions.net.YAWLSelectedNetAction;
import org.yawlfoundation.yawl.editor.elements.model.YAWLCompositeTask;
import org.yawlfoundation.yawl.editor.net.NetGraph;
import org.yawlfoundation.yawl.editor.swing.TooltipTogglingWidget;
import org.yawlfoundation.yawl.editor.swing.element.SelectUnfoldingNetDialog;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class SetUnfoldingNetAction extends YAWLSelectedNetAction implements TooltipTogglingWidget {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    private static final SelectUnfoldingNetDialog netDialog = new SelectUnfoldingNetDialog();

    private NetGraph graph;

    private YAWLCompositeTask task;

    {
        putValue(Action.SHORT_DESCRIPTION, getDisabledTooltipText());
        putValue(Action.NAME, "Unfold to net...");
        putValue(Action.LONG_DESCRIPTION, "Specify the net this task unfolds to.");
        putValue(Action.SMALL_ICON, getPNGIcon("shape_ungroup"));
        putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_U));
    }

    public SetUnfoldingNetAction(YAWLCompositeTask task, NetGraph graph) {
        super();
        this.task = task;
        this.graph = graph;
    }

    public void actionPerformed(ActionEvent event) {
        netDialog.setTask(graph, task);
        netDialog.setVisible(true);
        graph.clearSelection();
    }

    public String getEnabledTooltipText() {
        return " Specify the net this task unfolds to ";
    }

    public String getDisabledTooltipText() {
        return " You must have a composite task selected" + " to specify the net it unfolds to ";
    }
}

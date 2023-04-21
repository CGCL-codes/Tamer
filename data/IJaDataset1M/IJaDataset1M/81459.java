package org.yawlfoundation.yawl.editor.actions.net;

import org.jgraph.event.GraphSelectionEvent;
import org.yawlfoundation.yawl.editor.net.NetGraph;
import org.yawlfoundation.yawl.editor.specification.SpecificationSelectionListener;
import org.yawlfoundation.yawl.editor.specification.SpecificationSelectionSubscriber;
import org.yawlfoundation.yawl.editor.swing.TooltipTogglingWidget;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class DecreaseSizeAction extends YAWLSelectedNetAction implements TooltipTogglingWidget, SpecificationSelectionSubscriber {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    private static final DecreaseSizeAction INSTANCE = new DecreaseSizeAction();

    {
        putValue(Action.SHORT_DESCRIPTION, getDisabledTooltipText());
        putValue(Action.NAME, "Decrease Size");
        putValue(Action.LONG_DESCRIPTION, "Decrease size of currently selected net elements.");
        putValue(Action.SMALL_ICON, getPNGIcon("shape_group"));
        putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_D));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK));
    }

    private DecreaseSizeAction() {
        SpecificationSelectionListener.getInstance().subscribe(this, new int[] { SpecificationSelectionListener.STATE_NO_ELEMENTS_SELECTED, SpecificationSelectionListener.STATE_ONE_OR_MORE_ELEMENTS_SELECTED });
    }

    ;

    public static DecreaseSizeAction getInstance() {
        return INSTANCE;
    }

    public void actionPerformed(ActionEvent event) {
        final NetGraph graph = getGraph();
        if (graph != null) {
            graph.decreaseSelectedVertexSize();
        }
    }

    public String getEnabledTooltipText() {
        return " Decrease Size of selected items ";
    }

    public String getDisabledTooltipText() {
        return " You must have a number of net elements selected" + " to decrease their size ";
    }

    public void receiveGraphSelectionNotification(int state, GraphSelectionEvent event) {
        switch(state) {
            case SpecificationSelectionListener.STATE_NO_ELEMENTS_SELECTED:
                {
                    setEnabled(false);
                    break;
                }
            case SpecificationSelectionListener.STATE_ONE_OR_MORE_ELEMENTS_SELECTED:
                {
                    setEnabled(true);
                    break;
                }
        }
    }
}

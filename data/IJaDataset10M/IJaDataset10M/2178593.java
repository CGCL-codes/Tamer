package net.fuhrparkservice.client.eventhandling;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.fuhrparkservice.client.workflow.FahrzeugVerleihWorkflowControl;

public class NextAction extends AbstractAction implements ListSelectionListener {

    /**
	 * 
	 */
    private final FahrzeugVerleihWorkflowControl fahrzeugVerleihWorkflowControl;

    public NextAction(FahrzeugVerleihWorkflowControl fahrzeugVerleihWorkflowControl) {
        super("Next");
        this.fahrzeugVerleihWorkflowControl = fahrzeugVerleihWorkflowControl;
        this.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.fahrzeugVerleihWorkflowControl.next();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (((ListSelectionModel) e.getSource()).getLeadSelectionIndex() >= 0) this.setEnabled(true);
    }
}

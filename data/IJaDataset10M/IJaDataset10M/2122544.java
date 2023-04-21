package at.rc.tacos.client.ui.controller;

import java.util.Calendar;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.net.message.RemoveMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * Opens the editor to edit the selected entry
 * 
 * @author Michael
 */
public class PersonalDeleteEntryAction extends Action {

    private TableViewer viewer;

    /**
	 * Default class construtor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
    public PersonalDeleteEntryAction(TableViewer viewer) {
        this.viewer = viewer;
        setText("Eintrag l�schen");
        setToolTipText("L�scht den Dienstplaneintrag");
    }

    @Override
    public void run() {
        ISelection selection = viewer.getSelection();
        RosterEntry entry = (RosterEntry) ((IStructuredSelection) selection).getFirstElement();
        boolean cancelConfirmed = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Dienstplaneintrag l�schen", "M�chten Sie den Dienstplaneintrag wirklich l�schen?");
        if (!cancelConfirmed) return;
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minutes);
        entry.setRealEndOfWork(cal.getTimeInMillis());
        UpdateMessage<RosterEntry> updateMessage = new UpdateMessage<RosterEntry>(entry);
        updateMessage.asnchronRequest(NetWrapper.getSession());
        RemoveMessage<RosterEntry> removeMessage = new RemoveMessage<RosterEntry>(entry);
        removeMessage.asnchronRequest(NetWrapper.getSession());
    }
}

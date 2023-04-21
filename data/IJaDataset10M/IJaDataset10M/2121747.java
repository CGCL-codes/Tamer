package ca.sqlpower.wabit.swingui.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.apache.http.client.ClientProtocolException;
import ca.sqlpower.wabit.swingui.WabitSwingSessionContext;
import ca.sqlpower.wabit.swingui.WabitSwingSessionImpl;

/**
 * A Swing Action that will send a request to a Wabit Server to delete the
 * given Wabit Workspace on the server.
 */
public class DeleteWabitServerWorkspaceAction extends AbstractAction {

    private final WabitSwingSessionContext context;

    public DeleteWabitServerWorkspaceAction(WabitSwingSessionContext context) {
        super("Delete this workspace");
        this.context = context;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            WabitSwingSessionImpl activeSwingSession = (WabitSwingSessionImpl) context.getActiveSwingSession();
            if (activeSwingSession == null) {
                JOptionPane.showMessageDialog(context.getFrame(), "That button refreshes the current workspace,\n" + "but there is no workspace selected right now.");
                return;
            }
            int choice = JOptionPane.showConfirmDialog(context.getFrame(), "By deleting this workspace, " + "you will not be able to recover any of its contents.\n" + "Are you sure you want to delete it?", "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                activeSwingSession.delete();
            }
        } catch (ClientProtocolException ex) {
            throw new RuntimeException(ex);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

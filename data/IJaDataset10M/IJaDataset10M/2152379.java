package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.esrf.Tango.DevError;
import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.containers.sub.dialogs.SetEquipmentArgumentsDialog;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

/**
 * Sets the values of all Tango attributes contained in the selected snapshot,
 * to the given command.
 * <UL>
 * <LI>Opens a confirmation popup; if the user cancels, does nothing
 * <LI>Gets the selected snapshot
 * <LI>Calls setEquipmentWithCommand on it
 * <LI>Logs the action's success or failure
 * </UL>
 * 
 * @author Slim Ayadi
 */
@SuppressWarnings("serial")
public class ValidateSetEquipmentwithCommandAction extends BensikinAction {

    static final Logger logger = LoggerFactory.getLogger(ValidateSetEquipmentwithCommandAction.class);

    private SetEquipmentArgumentsDialog setEquipmentArgumentsDialog = null;

    /**
     * Standard action constructor that sets the action's name.
     * 
     * @param setEquipmentArgumentsDialog
     * 
     * @param _name
     *            The action name
     */
    public ValidateSetEquipmentwithCommandAction(final SetEquipmentArgumentsDialog m_setEquipmentArgumentsDialog, final String _name) {
        putValue(Action.NAME, _name);
        setEquipmentArgumentsDialog = m_setEquipmentArgumentsDialog;
    }

    @Override
    public void actionPerformed(final ActionEvent arg0) {
        final String msgTitle = Messages.getMessage("DIALOGS_SET_EQUIPMENTS_CONFIRM_TITLE");
        final String msgConfirm = Messages.getMessage("DIALOGS_SET_EQUIPMENTS_WITH_COMMAND_CONFIRM_LABEL");
        final String msgCancel = Messages.getMessage("DIALOGS_SET_EQUIPMENTS_CANCEL");
        final String msgValidate = Messages.getMessage("DIALOGS_SET_EQUIPMENTS_VALIDATE");
        final Object[] options = { msgValidate, msgCancel };
        final SnapshotDetailTabbedPane tabbedPane = SnapshotDetailTabbedPane.getInstance();
        final Snapshot snapshotToUse = ((SnapshotDetailTabbedPaneContent) tabbedPane.getSelectedComponent()).getSnapshot();
        try {
            final int confirm = JOptionPane.showOptionDialog(null, msgConfirm, msgTitle, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if (confirm == JOptionPane.OK_OPTION) {
                setEquipmentArgumentsDialog.setVisible(false);
                final String Result = snapshotToUse.setEquipmentWithCommand(setEquipmentArgumentsDialog.getCommandName(), setEquipmentArgumentsDialog.getOption());
                String msg = Messages.getLogMessage("SET_EQUIPMENTS_Command_ACTION_OK");
                if (Result != null) {
                    msg += "\n" + Result;
                } else {
                    msg = Messages.getLogMessage("SET_EQUIPMENTS_COMMAND_ACTION_KO");
                }
                logger.info(msg);
            }
        } catch (final SnapshotingException e) {
            final DevError[] devErrorTab = e.getDevErrorTab();
            int i = 0;
            do {
                if (devErrorTab[i].reason.indexOf("method on") > 0) {
                    logger.error(devErrorTab[i].reason);
                }
            } while (++i < devErrorTab.length);
            final String msg = Messages.getLogMessage("SET_EQUIPMENTS_COMMAND_ACTION_KO");
            logger.error(msg, e);
            return;
        }
    }
}

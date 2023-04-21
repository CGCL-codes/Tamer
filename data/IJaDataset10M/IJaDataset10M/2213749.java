package de.innot.avreclipse.ui.editors;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.forms.IManagedForm;
import de.innot.avreclipse.ui.actions.ActionType;
import de.innot.avreclipse.ui.dialogs.ChangeMCUDialog;

/**
 * A <code>IFormPart</code> that adds an action to the form toolbar to change the MCU type.
 * 
 * @see AbstractActionPart
 * 
 * @author Thomas Holland
 * @since 2.3
 * 
 */
public class MCUChangeActionPart extends AbstractActionPart {

    @Override
    protected IAction[] getAction() {
        ActionType type = ActionType.CHANGE_MCU;
        Action changeAction = new Action() {

            @Override
            public void run() {
                String filename = "";
                IManagedForm mform = getManagedForm();
                Object container = mform.getContainer();
                if (container instanceof ByteValuesFormEditor) {
                    ByteValuesFormEditor page = (ByteValuesFormEditor) container;
                    filename = page.getFilename();
                }
                ChangeMCUDialog changeMCUDialog = new ChangeMCUDialog(getManagedForm().getForm().getShell(), getByteValues(), filename);
                if (changeMCUDialog.open() == ChangeMCUDialog.OK) {
                    String newmcuid = changeMCUDialog.getResult();
                    String oldmcuid = getByteValues().getMCUId();
                    if (oldmcuid.equals(newmcuid)) {
                        return;
                    }
                    getByteValues().setMCUId(newmcuid, true);
                    markDirty();
                }
            }
        };
        type.setupAction(changeAction);
        IAction[] allactions = new IAction[1];
        allactions[0] = changeAction;
        return allactions;
    }
}

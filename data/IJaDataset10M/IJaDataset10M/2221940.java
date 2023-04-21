package uk.ac.reload.straker.views.resources;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import uk.ac.reload.straker.datamodel.resources.ResourceGroupEntry;

/**
 * "New Resource Group" Dialog
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewResourceGroupDialog.java,v 1.3 2006/07/10 11:50:34 phillipus Exp $
 */
public class NewResourceGroupDialog {

    /**
     * Owner Shell
     */
    private Shell _shell;

    /**
     * Parent Group to add to
     */
    private ResourceGroupEntry _parentGroup;

    /**
     * The group name
     */
    private String _groupName;

    /**
	 * Constructor
	 * @param parentGroup Parent Group to add to
	 */
    public NewResourceGroupDialog(Shell shell, ResourceGroupEntry parentGroup) {
        _shell = shell;
        _parentGroup = parentGroup;
    }

    /**
     * @return The Group Name or null if not set
     */
    public String getGroupName() {
        return _groupName;
    }

    /**
     * Throw up a dialog asking for a Resource Group name
     * @return True if the user entered valid input, false if cancelled
     */
    public boolean open() {
        InputDialog dialog = new InputDialog(_shell, "New Resource Group", "Enter name", "", new InputValidator());
        int code = dialog.open();
        if (code == Window.OK) {
            _groupName = dialog.getValue();
            try {
                _parentGroup.addNewGroupEntry(_groupName);
            } catch (Exception ex) {
                ex.printStackTrace();
                MessageDialog.openError(_shell, "File error", ex.getMessage());
                return false;
            }
            return true;
        } else {
            _groupName = null;
            return false;
        }
    }

    /**
     * Validate user input
     */
    private class InputValidator implements IInputValidator {

        public String isValid(String newText) {
            if ("".equals(newText.trim())) {
                return "A name must be specified";
            }
            if (_parentGroup.hasChildEntry(ResourceGroupEntry.class, newText)) {
                return "Group already exists.";
            }
            return null;
        }
    }
}

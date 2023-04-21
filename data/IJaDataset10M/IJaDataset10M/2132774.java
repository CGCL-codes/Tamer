package org.o14x.alpha.ui.commands.rename;

import org.eclipse.jface.dialogs.IInputValidator;
import org.o14x.alpha.util.Messages;

/**
 * 
 * 
 * @author Olivier DANGREAUX
 */
public class RenameInputValidator implements IInputValidator {

    private String lastName;

    /**
	 * Creates a new instance of RenameInputValidator.
	 * 
	 * @param The last name of the file to rename.
	 */
    public RenameInputValidator(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String isValid(String newText) {
        String errorMessage = null;
        if (lastName.equals(newText)) {
            errorMessage = Messages.getString("RenameInputValidator.new_name_message");
        }
        return errorMessage;
    }
}

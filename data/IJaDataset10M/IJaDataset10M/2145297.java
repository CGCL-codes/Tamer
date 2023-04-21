package net.sourceforge.syncyoursecrets.gui.dialogs;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * The DummyInputValidator will accept any input.
 * 
 * @author Jan Petranek
 * 
 */
public class DummyInputValidator implements IInputValidator {

    /**
	 * Always accepts the input.
	 * 
	 * @param newText
	 *            text to accept
	 * @return always returns null
	 * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
	 */
    public String isValid(String newText) {
        return null;
    }
}

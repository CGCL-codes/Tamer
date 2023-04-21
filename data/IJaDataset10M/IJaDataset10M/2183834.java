package com.anaxima.eslink.tools.ui;

import java.util.regex.Pattern;
import org.eclipse.core.resources.IFile;

/**
 * Implementation of a <code>AbstractSelectionStatusValidator</code> to validate the type of an element. This validator
 * accepts file selection matchin a name pattern.
 */
public class FileSelectionNameValidator extends AbstractSelectionStatusValidator {

    /** File type to accept. */
    private static final Class[] FILE_CLASS = new Class[] { IFile.class };

    /** Name patterns. */
    private Pattern[] _acceptedPattern = null;

    /**
	 * Create a new status validator for the given name patterns and with the given multi selection switch.
	 * <p>
	 * 
	 * @param argAcceptedPatterns
	 *            The name patterns accepted by this validator.
	 * @param argAllowMultiSelect
	 *            If set to <code>true</code>, the validator allows multiple selection.
	 */
    public FileSelectionNameValidator(String[] argAcceptedPatterns, boolean argAllowMultiSelect) {
        super(FILE_CLASS, argAllowMultiSelect, null);
        _acceptedPattern = new Pattern[argAcceptedPatterns.length];
        for (int i = 0; i < argAcceptedPatterns.length; i++) {
            _acceptedPattern[i] = Pattern.compile(argAcceptedPatterns[i]);
        }
    }

    /**
	 * @see com.anaxima.eslink.tools.ui.AbstractSelectionStatusValidator#isValid(java.lang.Object)
	 */
    @Override
    protected boolean isValid(Object argSelectedElement) {
        if (argSelectedElement instanceof IFile) {
            String name = ((IFile) argSelectedElement).getName();
            for (int i = 0; i < _acceptedPattern.length; i++) {
                if (_acceptedPattern[i].matcher(name).matches()) {
                    return true;
                }
            }
        }
        return false;
    }
}

package net.sourceforge.nattable.data.validate;

import net.sourceforge.nattable.edit.editor.AbstractCellEditor;

/**
 * Exception for handling validation failures.
 * As the API should not be modified for the handling of this exception,
 * it is a RuntimeException.
 * To make use of this exception it can be thrown on validation errors within
 * {@link IDataValidator#validate(int, int, Object)}. 
 * The handling of this exception is done within {@link AbstractCellEditor}
 * where the message is stored and showed within a dialog on trying to commit.
 * 
 * @author fipro
 *
 */
public class ValidationFailedException extends RuntimeException {

    private static final long serialVersionUID = 8965433867324718901L;

    public ValidationFailedException(String message) {
        super(message);
    }

    public ValidationFailedException(String message, Throwable t) {
        super(message, t);
    }
}

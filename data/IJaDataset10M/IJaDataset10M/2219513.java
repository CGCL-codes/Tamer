package net.oesterholt.jxmlnote.exceptions;

/**
 * This exception is thrown when a XMLNoteStyle is being applied that doesn't exist
 * in the XMLNoteStyles object of the XMLNoteDocument. 
 * @author hans
 *
 */
public class NoStyleException extends Exception {

    /**
	 * Version
	 */
    private static final long serialVersionUID = 1L;

    public NoStyleException(String styleId) {
        super(String.format("There's no style with id '%s'", styleId));
    }
}

package org.colombbus.tangara.ide.model.codeeditor;

import org.colombbus.tangara.ide.controller.action.FindAndReplaceAction;

/**
 * Exception for a sequence not found into a sentence.
 *
 * @see FindAndReplaceAction
 *
 * @author Aurelien Bourdon <aurelien.bourdon@gmail.com>
 */
@SuppressWarnings("serial")
public class SequenceNotFoundException extends Exception {

    public SequenceNotFoundException(String message) {
        super(message);
    }

    public SequenceNotFoundException(Throwable cause) {
        super(cause);
    }

    public SequenceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

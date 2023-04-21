package org.posterita.exceptions;

/**
 * @author ashley 
 * Jun 3, 2008
 */
public class NoCashJournalPresentException extends OperationException {

    public NoCashJournalPresentException(String msg) {
        super(msg);
    }
}

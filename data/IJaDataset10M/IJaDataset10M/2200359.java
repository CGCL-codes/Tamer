package org.loon.framework.game.simple.core.store;

public class InvalidRecordIDException extends RecordStoreException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public InvalidRecordIDException(String message) {
        super(message);
    }

    public InvalidRecordIDException() {
        super();
    }
}

package org.loon.framework.game.simple.core.store;

public class RecordStoreNotFoundException extends RecordStoreException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public RecordStoreNotFoundException(String message) {
        super(message);
    }

    public RecordStoreNotFoundException() {
        super();
    }
}

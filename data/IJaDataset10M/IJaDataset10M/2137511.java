package com.yama.ykes.exceptions;

import com.yama.ykes.data.Ripiano;
import com.yama.ykes.data.Scaffale;

public class RemoveShelfException extends YKesException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -643463415356339077L;

    private Scaffale scaffale;

    private Ripiano ripiano;

    public RemoveShelfException(Scaffale scaffale, Ripiano ripiano) {
        this.scaffale = scaffale;
        this.ripiano = ripiano;
    }

    public String getMessage() {
        return "Cannot remove shelf " + ripiano.getId() + " in " + scaffale.getId() + " : shelf not found.";
    }
}

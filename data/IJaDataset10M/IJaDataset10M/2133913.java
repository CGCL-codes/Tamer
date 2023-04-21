package com.w20e.socrates.rendering;

public class Secret extends Input {

    /**
	 * UID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Construct 'secret' type, usually used for password entry.
	 */
    public Secret(String id) {
        super(id);
        setType("secret");
    }
}

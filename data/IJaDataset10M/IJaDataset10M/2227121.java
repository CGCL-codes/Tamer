package com.hardcode.gdbms.driver.exceptions;

public class BadFieldDriverException extends OpenDriverException {

    private String description = "";

    public BadFieldDriverException(String l, Throwable exception) {
        super(l, exception);
        init();
    }

    public BadFieldDriverException(String name, Throwable exception, String description) {
        super(name, exception);
        this.description = description;
        init();
    }

    /**
	 *
	 */
    private void init() {
        messageKey = "error_field_layer";
        formatString = "Can�t load field of the driver: %(driver) " + "\n" + "incorrect type: " + description;
    }
}

package com.fatsatsuma.spreadsheets;

import java.io.Serializable;

/**
 * @author $Id: DefaultImportMessage.java,v 1.1 2007/06/07 17:58:02 gevans Exp $
 */
public class DefaultImportMessage implements Serializable, ImportMessage {

    private static final long serialVersionUID = -7993761028367375731L;

    private String message;

    public DefaultImportMessage(String message) {
        this.message = message;
    }

    public String getCellInformation() {
        return null;
    }

    public int getLine() {
        return -1;
    }

    public String getMessage() {
        return message;
    }
}

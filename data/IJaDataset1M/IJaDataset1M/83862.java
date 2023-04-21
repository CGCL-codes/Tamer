package de.javagimmicks.apps.chat.model;

import java.io.Serializable;

public class Exit implements Serializable {

    private static final long serialVersionUID = 6427711467212983476L;

    public static final Exit INSTANCE = new Exit();

    private Exit() {
    }
}

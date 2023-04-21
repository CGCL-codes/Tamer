package net.sf.rmoffice.core;

import net.sf.rmoffice.meta.enums.ToDoType;

/**
 * 
 */
public class ToDo {

    private final String message;

    private final ToDoType type;

    ToDo() {
        message = "";
        type = ToDoType.SYSTEM;
    }

    ToDo(String message, ToDoType type) {
        this.message = message;
        this.type = type;
    }

    /**
	 * @param message
	 */
    public ToDo(String message) {
        this.message = message;
        this.type = ToDoType.USER;
    }

    public String getMessage() {
        return message;
    }

    public ToDoType getType() {
        return type;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[" + type.name() + "] " + message;
    }
}

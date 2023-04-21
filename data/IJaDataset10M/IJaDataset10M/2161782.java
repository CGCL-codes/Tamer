package civquest.io.xmlSaveGame;

import civquest.io.SaveGameException;

/**
 */
public class UnsavableDataTypeException extends SaveGameException {

    public UnsavableDataTypeException(Class<?> type, String comment) {
        super("We don't support saving objects of type " + type + ". " + "Use the concept of Persistents instead! " + comment);
    }

    public UnsavableDataTypeException(Class<?> type) {
        this(type, "");
    }
}

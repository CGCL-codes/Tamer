package umlc.symbolTable;

/**
 *
 * @author  Ryan
 * @version 
 */
public class EntryExistsException extends java.lang.Exception {

    /**
     * Creates new <code>EntryExistsException</code> without detail message.
     */
    public EntryExistsException() {
    }

    /**
     * Constructs an <code>EntryExistsException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public EntryExistsException(String msg) {
        super(msg);
    }
}

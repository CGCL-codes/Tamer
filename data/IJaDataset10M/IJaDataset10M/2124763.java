package javax.swing.undo;

/**
 * An exception which indicates that an editing action cannot be
 * redone.
 *
 * @author Andrew Selkirk (aselkirk@sympatico.ca)
 * @author Sascha Brawer (brawer@dandelis.ch)
 */
public class CannotRedoException extends RuntimeException {

    /**
   * Constructs a new instance of a <code>CannotRedoException</code>.
   */
    public CannotRedoException() {
        super();
    }
}

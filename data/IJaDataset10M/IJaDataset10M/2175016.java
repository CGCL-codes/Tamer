package br.com.pleno.gp.exception;

/**
 *
 * @author Lourival
 */
public class ImpossivelSetarSuperiorException extends Exception {

    /**
     * Creates a new instance of <code>ImpossivelSetarSuperiorException</code> without detail message.
     */
    public ImpossivelSetarSuperiorException() {
    }

    /**
     * Constructs an instance of <code>ImpossivelSetarSuperiorException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ImpossivelSetarSuperiorException(String msg) {
        super(msg);
    }
}

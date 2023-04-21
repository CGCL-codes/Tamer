package org.tigris.subversion.javahl;

/**
 * This exception is thrown whenever something goes wrong in the
 * Subversion JavaHL binding's JNI interface.
 */
public class ClientException extends NativeException {

    private static final long serialVersionUID = 1L;

    /**
     * This constructor is only used by the native library.
     *
     * @param message A description of the problem.
     * @param source The error's source.
     * @param aprError Any associated APR error code for a wrapped
     * <code>svn_error_t</code>.
     */
    ClientException(String message, String source, int aprError) {
        super(message, source, aprError);
    }

    /**
     * A conversion routine for maintaining backwards compatibility.
     * @param t The exception to (potentially) convert.
     * @return <code>t</code> coerced or converted into a
     * <code>ClientException</code>.
     */
    static ClientException fromException(Throwable t) {
        if (t instanceof ClientException) {
            return (ClientException) t;
        } else {
            return new ClientException(t.getMessage(), null, -1);
        }
    }
}

package groovytools.builder;

/**
 * Base class for all {@link MetaBuilder} exceptions.
 *
 * @author didge
 * @version $Id: MetaBuilderException.java 35 2008-08-29 20:59:09Z didge $
 */
public class MetaBuilderException extends RuntimeException {

    public MetaBuilderException() {
    }

    public MetaBuilderException(String message) {
        super(message);
    }

    public MetaBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetaBuilderException(Throwable cause) {
        super(cause);
    }
}

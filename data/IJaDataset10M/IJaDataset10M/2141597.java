package skycastle.util.identifier;

import java.io.Serializable;

/**
 * An identifier for some type of object. 
 *
 * @author Hans H�ggstr�m
 */
public interface Identifier<T> extends Serializable {

    /**
     * @return the identifier as a string.
     */
    String getAsString();
}

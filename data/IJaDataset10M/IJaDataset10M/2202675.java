package org.apache.commons.collections.functors;

import java.io.Serializable;
import org.apache.commons.collections.Closure;

/**
 * Closure implementation that does nothing.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 646777 $ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Stephen Colebourne
 */
public class NOPClosure implements Closure, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = 3518477308466486130L;

    /** Singleton predicate instance */
    public static final Closure INSTANCE = new NOPClosure();

    /**
     * Factory returning the singleton instance.
     * 
     * @return the singleton instance
     * @since Commons Collections 3.1
     */
    public static Closure getInstance() {
        return INSTANCE;
    }

    /**
     * Constructor
     */
    private NOPClosure() {
        super();
    }

    /**
     * Do nothing.
     * 
     * @param input  the input object
     */
    public void execute(Object input) {
    }
}

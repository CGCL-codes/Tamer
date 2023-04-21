package org.enerj.apache.commons.collections.functors;

import java.io.Serializable;
import org.enerj.apache.commons.collections.Factory;
import org.enerj.apache.commons.collections.FunctorException;

/**
 * Factory implementation that always throws an exception.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 348444 $ $Date: 2005-11-23 14:06:56 +0000 (Wed, 23 Nov 2005) $
 *
 * @author Stephen Colebourne
 */
public final class ExceptionFactory implements Factory, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = 7179106032121985545L;

    /** Singleton predicate instance */
    public static final Factory INSTANCE = new ExceptionFactory();

    /**
     * Factory returning the singleton instance.
     * 
     * @return the singleton instance
     * @since Commons Collections 3.1
     */
    public static Factory getInstance() {
        return INSTANCE;
    }

    /**
     * Restricted constructor.
     */
    private ExceptionFactory() {
        super();
    }

    /**
     * Always throws an exception.
     * 
     * @return never
     * @throws FunctorException always
     */
    public Object create() {
        throw new FunctorException("ExceptionFactory invoked");
    }
}

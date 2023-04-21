package jaxlib.naming;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: AbstractObjectFactory.java 2267 2007-03-16 08:33:33Z joerg_wassmer $
 */
public abstract class AbstractObjectFactory extends Object implements ObjectFactory {

    protected AbstractObjectFactory() {
        super();
    }

    public abstract Object getObjectInstance(final Object refObj, final Name name, final Context nameCtx, final Hashtable<?, ?> env) throws Exception;

    @Override
    public boolean equals(final Object o) {
        return (o == this) || ((o != null) && (getClass() == o.getClass()));
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

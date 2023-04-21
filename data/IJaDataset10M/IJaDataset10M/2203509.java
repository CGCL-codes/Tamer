package maze.common.velocity.impl;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import org.apache.velocity.util.ArrayIterator;
import org.apache.velocity.util.EnumerationIterator;
import org.apache.velocity.util.introspection.Info;
import org.apache.velocity.util.introspection.UberspectImpl;

/**
 * 
 * @author Normunds Mazurs
 */
public class UberspectImplExt extends UberspectImpl {

    public UberspectImplExt() {
        super();
    }

    @Override
    @SuppressWarnings({ "rawtypes" })
    public Iterator getIterator(Object obj, Info i) throws Exception {
        if (obj.getClass().isArray()) {
            return new ArrayIterator(obj);
        } else if (obj instanceof Iterable) {
            return ((Iterable) obj).iterator();
        } else if (obj instanceof Map) {
            return ((Map) obj).values().iterator();
        } else if (obj instanceof Iterator) {
            if (log.isDebugEnabled()) {
                log.debug("The iterative object in the #foreach() loop at " + i + " is of type java.util.Iterator.  Because " + "it is not resettable, if used in more than once it " + "may lead to unexpected results.");
            }
            return ((Iterator) obj);
        } else if (obj instanceof Enumeration) {
            if (log.isDebugEnabled()) {
                log.debug("The iterative object in the #foreach() loop at " + i + " is of type java.util.Enumeration.  Because " + "it is not resettable, if used in more than once it " + "may lead to unexpected results.");
            }
            return new EnumerationIterator((Enumeration) obj);
        }
        log.info("Could not determine type of iterator in #foreach loop at " + i);
        return null;
    }
}

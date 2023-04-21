package ca.cbc.sportwire.util;

import java.util.Date;
import java.util.Map;
import org.apache.log4j.Category;

/**
 * <p><b>MRUCacheMap.java</b>: 
 *
 * </p>
 *
 * Created: Mon Jan 14 15:24:06 2002
 * <pre>
 * $Log: not supported by cvs2svn $
 * Revision 1.1  2002/01/14 21:51:46  garym
 * bug fixes in topics and config files migration to Extended Properties
 *
 *
 * </pre>
 * @author <a href="mailto:garym@teledyn.com">Gary Lawrence Murphy</a>
 * @version $Id: MRUCacheMap.java,v 1.2 2002-01-15 08:28:50 garym Exp $
 */
public class MRUCacheMap extends TTLCacheMap {

    public MRUCacheMap() {
    }

    /**
	 * <code>get</code>: returns the result from the underlying
	 * representation and resets the TTL field to the default
	 *
	 * @param key an <code>Object</code> value
	 * @return an <code>Object</code> value
	 */
    public Object get(Object key) {
        Object result = getMap().get(key);
        Date d = new Date();
        if (result != null) {
            ((TTLEntry) result).ttl = d.getTime() + getTTL();
            result = ((TTLEntry) result).value;
        }
        return result;
    }
}

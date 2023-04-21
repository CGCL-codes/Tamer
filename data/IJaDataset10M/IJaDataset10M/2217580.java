package ca.cbc.sportwire.servlet.data;

import java.util.Hashtable;
import org.apache.velocity.context.Context;
import org.apache.commons.collections.ExtendedProperties;
import ca.cbc.sportwire.util.PathUtil;
import org.apache.log4j.Category;

/**
 * <p><b>HashtableFactory.java</b>: This class is a necessity for
 * building hashtables from Velocity templates -- Velocity syntax docs
 * say you can use [ $var, 'const', $var2 ] syntax for arraylists, but
 * we couldn't get it to work and it was faster to code this class
 * than to reverse engineer the velocity.  HashtableFactory is a
 * singleton that is declared as a site-wide bean in the
 * <tt>sportpage.conf</tt>; to use it:
 * <pre>
 * #set ($params = params.newTable())
 * $params.put('key', $value )
 * $xml.transform($xsl, $params)
 * </pre>
 * this assumes a line in sportpage.conf that reads
 * <pre>
 * default.params.bean=ca.cbc.sportwire.servlet.data.HashtableFactory
 * </pre>
 * </p>
 *
 * Created: Thu Mar 20 11:12:15 2003
 * <pre>
 * $Log: not supported by cvs2svn $
 *
 * </pre>
 * @author <a href="mailto:garym@teledyn.com">Gary Lawrence Murphy</a>
 * @version $Id: HashtableFactory.java,v 1.1 2003-03-20 20:42:37 garym Exp $
 */
public class HashtableFactory {

    private static final String HASHTABLEFACTORY_PROPERTY = "_htf";

    private HashtableFactory() {
    }

    private HashtableFactory(ExtendedProperties sc) {
    }

    /**
	 * <code>getInstance</code>: returns a factory for use in a
	 * template
	 *
	 * @param sc an <code>ExtendedProperties</code> value
	 * @return the <code>HashtableFactory</code> value
	 */
    public static synchronized HashtableFactory getInstance(ExtendedProperties sc) {
        HashtableFactory result = null;
        if (sc.containsKey(HASHTABLEFACTORY_PROPERTY)) {
            result = (HashtableFactory) sc.getProperty(HASHTABLEFACTORY_PROPERTY);
        } else {
            result = new HashtableFactory(sc);
            synchronized (sc) {
                sc.setProperty(HASHTABLEFACTORY_PROPERTY, result);
            }
        }
        return result;
    }

    public Hashtable newTable() {
        return new Hashtable();
    }
}

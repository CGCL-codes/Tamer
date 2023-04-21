package net.frede.toolbox.properties;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision: 1.1.1.1 $
 */
public class CompositeProperties extends NodeProperties {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Map children = new Hashtable();

    /**
	 * the logger that will log any abnormal outputs out of this instance.
	 */
    static Logger logger = Logger.getLogger(CompositeProperties.class);

    /**
	 * default constructor
	 */
    public CompositeProperties() {
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public Properties getFullProperties() {
        Properties back = new Properties();
        Iterator it = subPropertyNames();
        while (it.hasNext()) {
            String key = (String) it.next();
            NodeProperties p = getSubProperties(key);
            Properties tmp = p.getFullProperties();
            back.putAll(tmp);
            tmp = null;
        }
        return back;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param heading
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public NodeProperties getSubProperties(String heading) {
        NodeProperties back = (NodeProperties) children.get(heading);
        if (back == null) {
            back = new SubProperties(heading, this);
            if (back.size() != 0) {
                children.put(heading, back);
            }
        }
        return back;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param heading
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public static Properties getSubProperties(Properties source, String heading) {
        NodeProperties back = new SubProperties(heading, source);
        return back;
    }

    /**
	 * used to run the class.
	 * 
	 * @param args
	 *            parameters array in input
	 */
    public static void main(String[] args) {
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public Iterator subPropertyNames() {
        ArrayList back = new ArrayList();
        Enumeration e = propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            int i = key.indexOf(SEPARATOR);
            if (i != -1) {
                String newKey = key.substring(0, i);
                if (!back.contains(newKey)) {
                    back.add(newKey);
                    logger.info("adding " + newKey);
                } else {
                    logger.debug("rejecting " + newKey);
                }
            }
        }
        return back.iterator();
    }
}

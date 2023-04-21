package org.exolab.jms.client;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;

/**
 * Implementation of the {@link ObjectFactory} interface that creates {@link
 * JmsConnectionFactory} instances given a corresponding {@link Reference}
 *
 * @author <a href="mailto:jima@comware.com.au">Jim Alateras</a>
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.2 $ $Date: 2005/03/18 03:36:37 $
 * @see JmsConnectionFactory
 * @see JmsXAConnectionFactory
 */
public class JmsConnectionFactoryBuilder implements ObjectFactory {

    /**
     * Creates an object using the location or reference information specified.
     * This only constructs
     *
     * @param object      the object containing location or reference
     *                    information that can be used in creating an object.
     *                    May be <code>null</code>
     * @param name        the name of this object relative to <code>context</code>,
     *                    or <code>null</code> if no name is specified.
     * @param context     the context relative to which the <code>name</code>
     *                    parameter is specified, or <code>null</code> if
     *                    <code>name</code> is relative to the default initial
     *                    context.
     * @param environment the environment used in creating the object. May be
     *                    <code>null</code>
     * @return the object created oird <code>null</code> if an object can't be
     *         created.
     * @throws Exception if this object factory encountered an exception while
     *                   attempting to create an object, and no other object
     *                   factories are to be tried.
     */
    public Object getObjectInstance(Object object, Name name, Context context, Hashtable environment) throws Exception {
        Object result = null;
        if (object instanceof Reference) {
            Reference ref = (Reference) object;
            String clazz = ref.getClassName();
            if (clazz.equals(JmsConnectionFactory.class.getName()) || clazz.equals(JmsXAConnectionFactory.class.getName())) {
                StringRefAddr serverClass = (StringRefAddr) ref.get("serverClass");
                String serverClassName = (String) serverClass.getContent();
                HashMap properties = new HashMap();
                Enumeration iter = ref.getAll();
                while (iter.hasMoreElements()) {
                    StringRefAddr addr = (StringRefAddr) iter.nextElement();
                    properties.put(addr.getType(), addr.getContent());
                }
                if (clazz.equals(JmsConnectionFactory.class.getName())) {
                    result = new JmsConnectionFactory(serverClassName, properties, environment);
                } else {
                    result = new JmsXAConnectionFactory(serverClassName, properties, environment);
                }
            }
        }
        return result;
    }
}

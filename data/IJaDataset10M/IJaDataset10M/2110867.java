package joelib.util;

import java.util.Map;
import org.apache.log4j.Category;
import joelib.process.JOEProcessException;

/**
 *  Some helper methods for calling classes which accept properties ({@link PropertyAcceptor}).
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.12 $, $Date: 2004/08/28 21:56:34 $
 */
public class JOEPropertyHelper {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.util.JOEPropertyHelper");

    /**
     *  Don't let anyone instantiate this class
     */
    private JOEPropertyHelper() {
    }

    /**
     *  Sets the property attribute of the JOEPropertyHelper class
     *
     * @param  property                 The new property value
     * @param  availProperties          The new property value
     * @param  obj                      The new property value
     * @exception  JOEProcessException  Description of the Exception
     */
    public static void setProperty(String property, Map availProperties, Object obj) throws JOEProcessException {
        if (availProperties == null) {
            return;
        }
        availProperties.put(property, obj);
    }

    /**
     *  Gets single process property.
     *
     * @param  property                 Description of the Parameter
     * @param  availProperties          Description of the Parameter
     * @return                          The property value
     * @exception  JOEProcessException  Description of the Exception
     */
    public static Object getProperty(String property, Map availProperties) {
        if (availProperties == null) {
            return null;
        }
        return availProperties.get(property);
    }

    /**
     *  Gets single process property or default value if not defined or <tt>null
     *  </tt>.
     *
     * @param  process                  Description of the Parameter
     * @param  property                 Description of the Parameter
     * @param  availProperties          Description of the Parameter
     * @return                          The property value
     * @exception  JOEProcessException  Description of the Exception
     */
    public static Object getProperty(PropertyAcceptor propAcceptor, String property, Map availProperties) {
        if (availProperties == null) {
            return null;
        }
        Object objProperty = availProperties.get(property);
        if (objProperty != null) {
            return objProperty;
        }
        JOEProperty[] acceptedProperties = propAcceptor.acceptedProperties();
        String propName;
        for (int i = 0; i < acceptedProperties.length; i++) {
            propName = acceptedProperties[i].propName;
            if (propName.equals(property)) {
                return acceptedProperties[i].getDefaultProperty();
            }
        }
        return null;
    }

    /**
     *  Description of the Method
     *
     * @param  process                  Description of the Parameter
     * @param  availProperties          Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  JOEProcessException  Description of the Exception
     */
    public static boolean checkProperties(PropertyAcceptor propAcceptor, Map availProperties) {
        JOEProperty[] acceptedProperties = propAcceptor.acceptedProperties();
        if (acceptedProperties == null) {
            return true;
        }
        String propName;
        String representation;
        for (int i = 0; i < acceptedProperties.length; i++) {
            propName = acceptedProperties[i].propName;
            representation = acceptedProperties[i].representation;
            if (!acceptedProperties[i].isOptional()) {
                if ((availProperties == null) || !availProperties.containsKey(propName)) {
                    logger.error("Process parameter/property '" + propName + "'" + " with type '" + representation + "' is missing in " + propAcceptor.getClass().getName() + ".");
                }
            } else {
                if (availProperties != null) {
                    Object prop = availProperties.get(acceptedProperties[i].propName);
                    if ((prop != null) && !(prop.getClass().getName().equals(representation))) {
                        logger.error("Parameter/property '" + propName + "'" + " should be of type '" + representation + "' not of type '" + prop.getClass().getName() + "' in " + propAcceptor.getClass().getName() + ".");
                    }
                }
            }
        }
        return true;
    }
}

package org.easypeas.objectcreation;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.easypeas.EasyPeasConstants;

/**
 * A Factory class that provides an instance of an ObjectCreator, whose
 * responsibility is to create instances of classes based upon the class name.<br>
 * 
 * @author S Owen
 * 
 */
public class ObjectCreatorFactory {

    private static Logger logger = Logger.getLogger("org.easypeas.objectcreation.ObjectCreatorFactory");

    private static ObjectCreatorFactory instance = new ObjectCreatorFactory();

    /**
	 * Instantiates a new object creator factory. Private to force use of the
	 * getInstance method
	 */
    private ObjectCreatorFactory() {
    }

    ;

    /**
	 * Gets the single instance of ObjectCreatorFactory.
	 * 
	 * @return single instance of ObjectCreatorFactory
	 */
    public static ObjectCreatorFactory getInstance() {
        return instance;
    }

    /**
	 * Returns an implemtation of ObjectCreator.<br>
	 * Currently the default ObjectCreator implementation is
	 * {@link ReflectionObjectCreator}<br>
	 * which can be overridden with the Servlet init param
	 * easypeas.objectcreator
	 * 
	 * @return the object creator
	 */
    public ObjectCreator creator() {
        String property = System.getProperty(EasyPeasConstants.EASYPEA_OBJECTCREATOR);
        logger.finer("Creating new ObjectCreator for property:" + property);
        ObjectCreator result = null;
        if (property == null) {
            result = new ReflectionObjectCreator();
        } else {
            try {
                result = (ObjectCreator) Class.forName(property).newInstance();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error creating overridden ObjectCreator named:" + property, e);
            }
        }
        return result;
    }
}

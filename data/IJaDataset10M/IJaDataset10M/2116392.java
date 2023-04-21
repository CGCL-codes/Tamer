package info.jmonit.config.runtime;

import java.lang.reflect.Constructor;
import info.jmonit.logger.ConsoleLogger;
import info.jmonit.logger.Logger;
import info.jmonit.logger.LoggerFactory;

/**
 * Support class for factories.
 *
 * @author <a href="mailto:ndeloof@sourceforge.net">ndeloof</a>
 */
public class FactorySupport {

    /** console logger used when FactorySupport is used to build a logger */
    private static Logger console = new ConsoleLogger(FactorySupport.class);

    /** Logger */
    private static Logger logger = LoggerFactory.getLogger(FactorySupport.class);

    /**
     * Constructor
     */
    public FactorySupport() {
        super();
    }

    /**
     * Create an instance of the specified class using constructor Args. Catch
     * any exception.
     *
     * @param className name of the class to instanciate
     * @param constructorArgs constructor arguments
     * @return instance
     */
    public static Object silentlyCreate(String className, Object[] constructorArgs) {
        try {
            Class clazz = Class.forName(className);
            Constructor[] constructors = clazz.getConstructors();
            for (int i = 0; i < constructors.length; i++) {
                if (constructors[i].getParameterTypes().length == constructorArgs.length) {
                    return constructors[i].newInstance(constructorArgs);
                }
            }
        } catch (Exception e) {
            if (logger != null) {
                logger.error("Failed to create a " + className + " instance", e);
            } else {
                console.error("Failed to create a " + className + " instance", e);
            }
        }
        return null;
    }

    /**
     * Create an instance of the specified class using default constructor.
     * Catch any exception.
     *
     * @param className name of the class to instanciate
     * @return instance
     */
    public static Object silentlyCreate(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (Exception e) {
            if (logger != null) {
                logger.error("Failed to create a " + className + " instance", e);
            } else {
                console.error("Failed to create a " + className + " instance", e);
            }
        }
        return null;
    }
}

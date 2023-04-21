package org.mged.magetab.error;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A factory class for generating error items.  You should use the static {@link
 * #getErrorItemFactory(ClassLoader)} method to return the factory that can
 * subsequently be used to return error items.  This factory, by default, can
 * construct error items using {@link ErrorItem}s from the enum or the numeric
 * code of the error as listed in the classpath resource
 * "META-INF/magetab/errorcodes-core.properties".
 * <p/>
 * If you wish to extend the set of errorcodes available, you should include on
 * your classpath a resource called "META-INF/magetab/errorcodes.properties".
 * These will be loaded automatically and available for use in this factory. You
 * can optionally recover an {@link ErrorCode} item using the {@link
 * #lookupErrorMessage(int)} method.  The error codes loaded should have the
 * form of a standard java properties file, where the key is the numeric value
 * of the error code and the value is the string representing the error message
 * associated with this error code.
 * <p/>
 * If you wish to manually set a different location for custom error codes, you
 * should retrieve an ErrorItemFactory as normal, and then add the location of
 * the resource to it using the {@link #loadErrorCodeExtensions(String)}
 * method.
 * <p/>
 * Note that the {@link #getErrorItemFactory(ClassLoader)} method uses the
 * supplied classloader to return a factory instance that can load resources
 * from that loader.  If there is a chance that you have an extension component
 * that uses a different classloader to this factory class, you should always
 * use this form of the method.
 *
 * @author Tony Burdett
 * @date 24-Jul-2009
 */
public class ErrorItemFactory {

    private static Map<ClassLoader, ErrorItemFactory> factories = new HashMap<ClassLoader, ErrorItemFactory>();

    /**
   * Get the {@link ErrorItemFactory} that can be used to generate error items
   * using any resources accessible from the supplied classloader.  Note that,
   * because errorcode resources may be spread across different classloaders,
   * you are required to supply one here.  Normally <code>getClass().getClassLoader()</code>
   * will suffice to allow this factory to discover required resources, unless
   * you want to load errorcodes explicitly from e.g. an online resource.
   * <p/>
   * Always use this form of the method if there is a chance that you have an
   * extension component that uses a different classloader to this factory
   * class.  Otherwise, not all extension resources are guaranteed to be
   * discovered
   *
   * @param loader the ClassLoader that this factory uses to search for
   *               resources
   * @return the ErrorItemFactory that can find error codes using the given
   *         loader
   */
    public static ErrorItemFactory getErrorItemFactory(ClassLoader loader) {
        if (factories.containsKey(loader)) {
            return factories.get(loader);
        } else {
            ErrorItemFactory factory = new ErrorItemFactory(loader);
            factories.put(loader, factory);
            return factory;
        }
    }

    /**
   * Get the {@link ErrorItemFactory} that can be used to generate error items
   * using any resources accessible from the classloader that loaded this
   * factory class.  Errorcode resources may be spread across different
   * classloaders, so if you are unsure whether resources are accessible from
   * the classloader that loaded this class use the parameterised version {@link
   * #getErrorItemFactory(ClassLoader)}.  Normally calling that form of the
   * method with <code>getClass().getClassLoader()</code> will suffice to allow
   * this factory to discover required resources, unless you want to load
   * errorcodes explicitly from e.g. an online resource.
   * <p/>
   * Be advised that you should <b>ONLY</b> use this form if you are sure that
   * the classloader that loaded this factory has access to all the resources
   * you require.  This will normally be true if you only requre the core set of
   * error codes and have provided no extensions, depending on the deployment
   * environment.
   *
   * @return the ErrorItemFactory that can find error codes using the
   *         classloader for this class
   */
    public static ErrorItemFactory getErrorItemFactory() {
        ClassLoader loader = ErrorItemFactory.class.getClassLoader();
        if (factories.containsKey(loader)) {
            return factories.get(loader);
        } else {
            ErrorItemFactory factory = new ErrorItemFactory(loader);
            factories.put(loader, factory);
            return factory;
        }
    }

    private ClassLoader loader;

    private Map<Integer, String> errorCodes;

    private Log log = LogFactory.getLog(this.getClass());

    private ErrorItemFactory(ClassLoader loader) {
        this.loader = loader;
        errorCodes = new HashMap<Integer, String>();
        try {
            Enumeration<URL> resources = loader.getResources("META-INF/magetab/errorcodes-core.properties");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                log.info("Loading core MAGE-TAB error codes from " + url.toString());
                Properties props = new Properties();
                props.load(url.openStream());
                for (Object key : props.keySet()) {
                    int code = Integer.parseInt(key.toString());
                    String message = props.get(key).toString();
                    if (!errorCodes.containsKey(code)) {
                        log.debug("Adding error code " + code + " to known list");
                        errorCodes.put(code, message);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Unable to access classpath resource to load " + "core error codes, these will be ignored");
        }
        try {
            Enumeration<URL> resources = loader.getResources("META-INF/magetab/errorcodes.properties");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                log.info("Loading extension MAGE-TAB error codes from " + url.toString());
                Properties props = new Properties();
                props.load(url.openStream());
                for (Object key : props.keySet()) {
                    int code = Integer.parseInt(key.toString());
                    String message = props.get(key).toString();
                    if (!errorCodes.containsKey(code)) {
                        log.debug("Adding custom error code " + code + " to known list");
                        errorCodes.put(code, message);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Unable to access classpath resource to load " + "extension error codes, these will be ignored");
        }
    }

    /**
   * Generates an ErrorItem from the ErrorCode enum.  Use this if you want to
   * conveniently access one of the core errors from your code using the {@link
   * ErrorCode} enum.
   *
   * @param errorMessage      the local, application specific error message
   * @param errorCode         the ErrorCode enum object describing the error
   * @param classCausingError the class in your code that caused the error
   * @return a new ErrorItem encapsulating the available error information
   */
    public ErrorItem generateErrorItem(String errorMessage, ErrorCode errorCode, Class classCausingError) {
        ErrorItem item = new ErrorItemImpl(lookupErrorMessage(errorCode.getIntegerValue()), errorCode.getIntegerValue(), classCausingError.getSimpleName());
        item.setComment(errorMessage);
        return item;
    }

    /**
   * Generates an ErrorItem from the numeric error code.  This code will be
   * retrieved from the set of error codes supplied.  Error codes are recovered
   * from several places.  Firstly, they are obtained from the core set of error
   * codes bundled with this parser, using the resource at
   * "META-INF/magetab/errorcodes-core.properties".  Secondly, they are
   * recovered from any supplied extensions in the proper location,
   * "META-INF/magetab/errorcodes.properties".  Finally they are obtained from
   * any custom locations added to this factory.  The numeric value supplied is
   * compared to the known set of codes and if found, used to generate the error
   * item.  If not, an {@link IllegalArgumentException} is thrown.
   *
   * @param errorMessage      the local, application specific error message
   * @param errorCode         the numeric error code
   * @param classCausingError the class in your code that caused the error
   * @return a new ErrorItem encapsulating the available error information
   * @throws IllegalArgumentException if the numeric value cannot be found in
   *                                  the current set of error codes
   */
    public ErrorItem generateErrorItem(String errorMessage, int errorCode, Class classCausingError) throws IllegalArgumentException {
        if (!errorCodes.containsKey(errorCode)) {
            throw new IllegalArgumentException("The error code " + errorCode + " is not recognised");
        }
        ErrorItem item = new ErrorItemImpl(lookupErrorMessage(errorCode), errorCode, classCausingError.getSimpleName());
        item.setComment(errorMessage);
        return item;
    }

    /**
   * Generates a fully qualified ErrorItem, given details about the type of
   * error. The numeric code should specify the type of error, and will be
   * retrieved from the set of error codes supplied.  The numeric value supplied
   * is compared to the known set of codes and if found, used to generate the
   * error item.  If not, an {@link IllegalArgumentException} is thrown.
   * <p/>
   * To use this form, you should know all the details about the error - where
   * exactly in the file the problematic line was and the calling class that
   * resulted in the error being generated.  You should also specify a unique
   * identifier for this error.  If any of this information is missing, use
   * another form of this method, and modify these fields later.
   *
   * @param id           the id of this item
   * @param parsedFile   the file being parsed
   * @param errorCode    the numeric error code
   * @param line         the line at which this error occurred
   * @param col          the column at which this error occured
   * @param errorMessage the local, application specific error message
   * @param inCaller     the class in your code that caused the error
   * @return a new ErrorItem encapsulating the available error information
   * @throws IllegalArgumentException if the numeric value cannot be found in
   *                                  the current set of error codes
   */
    public ErrorItem generateErrorItem(int id, String parsedFile, int errorCode, int line, int col, String errorMessage, String inCaller) throws IllegalArgumentException {
        if (!errorCodes.containsKey(errorCode)) {
            throw new IllegalArgumentException("The error code " + errorCode + " is not recognised");
        }
        ErrorItem item = new ErrorItemImpl(id, parsedFile, errorCode, line, col, lookupErrorMessage(errorCode), inCaller);
        item.setComment(errorMessage);
        return item;
    }

    /**
   * Load a set of error codes from a classpath resource with the given resource
   * name.  Any error codes in this resource will subsequently be available for
   * use within ErrorItems.  Note that conflicting error codes will not
   * overwrite those already loaded - in other words, if you attempt to define a
   * new message for error code 1, this will not be accepted.
   * <p/>
   * Loaded resources should have the form of a standard java properties file,
   * where the key is the numeric value of the error code and the value is the
   * string representing the error message associated with this error code.
   * <p/>
   * Note that this uses the class loader for this factory class to identify and
   * load resources, and as such only resources which are either available from
   * this classloader or one of it's parents can be accessed.
   *
   * @param resourceName the name of the resource containing error codes.
   */
    public void loadErrorCodeExtensions(String resourceName) {
        try {
            Enumeration<URL> resources = loader.getResources(resourceName);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                Properties props = new Properties();
                props.load(url.openStream());
                for (Object key : props.keySet()) {
                    int code = Integer.parseInt(key.toString());
                    String message = props.get(key).toString();
                    if (!errorCodes.containsKey(code)) {
                        errorCodes.put(code, message);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Unable to access classpath resource to load " + "core error codes, these will be ignored");
        }
    }

    /**
   * Retrieve the error message that relates to the given code.
   *
   * @param errorCode the code to lookup the message for
   * @return the related error message
   * @throws IllegalArgumentException if the code could not be found
   */
    public String lookupErrorMessage(int errorCode) throws IllegalArgumentException {
        if (!errorCodes.containsKey(errorCode)) {
            throw new IllegalArgumentException("The error code " + errorCode + " is not recognised");
        } else {
            return errorCodes.get(errorCode);
        }
    }
}

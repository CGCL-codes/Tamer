package edu.caltech.sbw;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import grace.log.StackTrace;

/**
 * The {@link ModuleImpl} class is used to define the implementation of a
 * module.  Its constructor defines various basic aspects such as the module's
 * name, and the {@link #addService} method described below provides the
 * ability to define services associated with the module.
 * <p>
 * <h3>Module Attributes</h3>
 * In addition to services, all module definitions have five pieces of
 * information associated with them: (1) a unique name, (2) a display name,
 * (3) a management type, (4) a class to use as the main class that will be
 * invoked to start up the module, and (5) a help or documentation string.
 * There are several versions of the constructor for {@link ModuleImpl},
 * providing increasingly more control over the definition of the module.  The
 * simpler forms derive some of their information dynamically from the Java
 * stack trace at the time the method is called.
 *
 * <h3>Module Management Types</h3>
 * Depending on the kind of services a module implements and the style of use
 * intended, it may be more suitable to have multiple copies (instances) of a
 * module running, or to have only one instance running.  Modules that do not
 * keep state information are often best implemented as having only one copy
 * running in an SBW system.  For example, a module that performs data
 * conversion operations could run as a unique instance in an SBW system,
 * avoiding the resource usage that would result if a fresh copy were started
 * every time that a client module requested its services.
 * <p>
 * SBW allows a module developer to designate the type of management that
 * SBW should assume for a module.  The type is indicated by a flag to the
 * constructors for the {@link ModuleImpl} class.
 *
 * <h3>Using <code>ModuleImpl</code></h3>
 * The following is an example of the <code>main</code> routine of a
 * very simple module that implements a single service, "Trig".  This
 * service is defined by the class <code>Trig</code> (not shown here).
 * <pre>
 * package edu.caltech.trigonometry;
 * import edu.caltech.sbw;
 *
 * class TrigApplication
 * {
 *   public static void main(String[] args)
 *   {
 *     try
 *     {
 *       ModuleImpl moduleImp = new ModuleImpl("Trigonometry");
 *
 *       moduleImp.addService("Trig", "sin and cosine functions",
 *                            "trigonometry", Trig.class);
 *       moduleImp.run(args);
 *     }
 *     catch (SBWException e)
 *     {
 *       e.handleWithException();
 *     }
 *   }
 * }</pre>
 * The creation of a {@link ModuleImpl} object is the first step in the
 * definition of the module.  The form of the constructor used in this
 * example is a simple one that requires only one argument: a
 * human-readable display name for the module.  (See below for other
 * variants of the constructor.) The constructor automatically assigns a
 * unique name to the module based on the package in which the definition
 * is placed (in this case, the unique name becomes
 * *``edu.caltech.trigonometry'').
 * <p>
 * The first argument of {@link #addService(String uniqueName, String
 * displayName, String category, Class implementationClass)} is a unique
 * name for the service (unique to the module); the second argument is a
 * short, human-readable display name for the service; the third argument
 * is the service category; and the last argument is the class that
 * implements the service.  The class definition is scanned by SBW for
 * public methods, and these become the methods offered by the service.
 * (There exist different variants of the {@link #addService(String
 * uniqueName, String displayName, String category, Class
 * implementationClass)} method; see below for a list.
 * <p>
 * The last step is starting up the module.  An SBW-enabled application can
 * run in three modes: without SBW, in SBW <em>registration</em> mode, and
 * in <em>module</em> mode.  The {@link ModuleImpl} class provides a
 * convenience method, {@link #run}, which takes care of switching between
 * the modes at run time.  The determination of which mode to use is made
 * on the basis of a flag passed on the command-line to the application
 * when it is started up.  Note the <code>args</code> array passed as an
 * argument to the {@link #run} method; this is the array of command-line
 * arguments that was handed to the application's <code>main</code>.  If
 * the application is started with the command-line flag
 * <code>-sbwregister</code>, the call to {@link ModuleImpl}'s {@link #run}
 * method registers the module and its services with the SBW Broker and
 * exits; if the application is started with <code>-sbwmodule</code>, it
 * connects to the Broker, notifies it that the module is providing
 * services, and returns, letting the module run until it shuts down or SBW
 * is disconnected.  If neither flag is given to the application, {@link
 * #run} does nothing.
 * <p>
 * @see Module
 * @see Service
 *
 * @author Michael Hucka
 * @author $Author: fbergmann $
 * @version $Revision: 1.3 $
 **/
public class ModuleImpl {

    /**
	 * A module management type that indicates only one module instance
	 * will be shared by all applications which access the module and
	 * the module will be shut down by SBW.
	 **/
    public static final int UNIQUE = 0;

    /**
	 * A module management type that indicates a new module instance will
	 * be created every time a module is accessed through {@link
	 * edu.caltech.sbw.SBW#getModuleInstance} and the module will decide
	 * for itself when to shutdown.
	 **/
    public static final int SELF_MANAGED = 1;

    /**
	 * Defines a new module.  The <code>uniqueName</code> is a unique name
	 * that identifies this module in SBW.  The <code>displayName</code> is
	 * a humanly-readable name for the module meant to be used in menus and
	 * such.  The <code>type</code> code indicates whether the module
	 * should be considered {@link #SELF_MANAGED} or {@link #UNIQUE}.
	 * The class object passed in
	 * <code>moduleMainClass</code> is used to determine which class
	 * contains the module's <code>main()</code> method and is used to
	 * construct a command line for starting up the module.  The argument
	 * <code>helpString</code> can be used to provide a string that
	 * summarizes the purpose of the module; this help string is
	 * retrievable by other modules through the SBW Broker.
	 * <p>
	 * If the given <code>uniqueName</code> is already known to SBW, the
	 * information associated with that module is overwritten by the
	 * information supplied in the new call.
	 * <p>
	 * The command for starting up the module using the given
	 * <code>moduleMainClass</code> has the following generic form:
	 * <blockquote>
	 *     <i>path-to-java</i> <kbd>-classpath</kbd> <i>class-path</i> <i>moduleMainClass</i> <kbd>-sbwmodule</kbd>
	 * </blockquote>
	 * The classpath is determined from the path to the SBW home directory,
	 * and the path to java is the path to the <code>java</code> executable
	 * that was used to start this module.  To set an explicit command line
	 * for starting up the module, use the {@link #setCommandLine} method.
	 * <p>
	 * @param uniqueName a unique name for the module
	 * @param displayName version of the module name intended for human consumption
	 * @param type how the module is managed: {@link #SELF_MANAGED} or
	 * {@link #UNIQUE}
	 * @param moduleMainClass the class containing the module's <code>main()</code>
	 * @param helpString a string describing the purpose of this module and
	 * offering some information about its intended usage
	 *
	 * @throws SBWModuleDefinitionException if any of the arguments are
	 * invalid, perhaps because they are null or otherwise unacceptable.
	 **/
    public ModuleImpl(String uniqueName, String displayName, int type, Class moduleMainClass, String helpString) throws SBWModuleDefinitionException {
        initModuleImpl(uniqueName, displayName, type, moduleMainClass, helpString);
    }

    /**
	 * Defines a new module.  The <code>uniqueName</code> is a unique name
	 * that identifies this module in SBW.  The <code>displayName</code> is
	 * a humanly-readable name for the module meant to be used in menus and
	 * such.  The <code>type</code> code indicates whether the module
	 * should be considered {@link #SELF_MANAGED} or {@link #UNIQUE}.
	 * The class object passed in
	 * <code>moduleMainClass</code> is used to determine which class
	 * contains the module's <code>main()</code> method and is used to
	 * construct a command line for starting up the module.
	 * <p>
	 * This is a shorter version of the constructor {@link
	 * #ModuleImpl(String uniqueName, String displayName, int
	 * type, Class moduleMainClass, String helpString)} that does not
	 * take a help string; in the present case, the help string is left empty.
	 * <p>
	 * If the given <code>uniqueName</code> is already known to SBW, the
	 * information associated with that module is overwritten by the
	 * information supplied in the new call.
	 * <p>
	 * The command for starting up the module using the given
	 * <code>moduleMainClass</code> has the following generic form:
	 * <blockquote>
	 *     <i>path-to-java</i> <kbd>-classpath</kbd> <i>class-path</i> <i>moduleMainClass</i> <kbd>-sbwmodule</kbd>
	 * </blockquote>
	 * The classpath is determined from the path to the SBW home directory,
	 * and the path to java is the path to the <code>java</code> executable
	 * that was used to start this module.  To set an explicit command line
	 * for starting up the module, use the {@link #setCommandLine} method.
	 * <p>
	 * @param uniqueName a unique name for the module
	 * @param displayName version of the module name intended for human consumption
	 * @param type how the module is managed: {@link #SELF_MANAGED} or
	 * {@link #UNIQUE}
	 * @param moduleMainClass the class containing the module's <code>main()</code>
	 *
	 * @throws SBWModuleDefinitionException if any of the arguments are
	 * invalid, perhaps because they are null or otherwise unacceptable.
	 **/
    public ModuleImpl(String uniqueName, String displayName, int type, Class moduleMainClass) throws SBWModuleDefinitionException {
        initModuleImpl(uniqueName, displayName, type, moduleMainClass, "");
    }

    /**
	 * Defines a new module.  This is a simplified version of {@link
	 * #ModuleImpl(String uniqueName, String displayName, int
	 * type, Class moduleMainClass, String helpString)} that derives some
	 * of its arguments from the calling class.
	 * <p>
	 * The <code>uniqueName</code> is a unique name
	 * that identifies this module in SBW.  The <code>displayName</code>
	 * is a humanly-readable name for the module meant to be used in menus
	 * and such.  The <code>type</code> code indicates whether the module
	 * should be considered {@link #SELF_MANAGED} or {@link #UNIQUE}.
	 * The class object
	 * is determined automatically from the class in which the constructor
	 * is called.  This is used to
	 * construct a command line for starting up the module.
	 * <p>
	 * If the given <code>uniqueName</code> is already known to SBW, the
	 * information associated with that module is overwritten by the
	 * information supplied in the new call.
	 * <p>
	 * @param uniqueName a unique name for the module
	 * @param displayName version of the module name intended for human consumption
	 * @param type how the module is managed: {@link #SELF_MANAGED} or
	 * {@link #UNIQUE}
	 * @param moduleMainClass the class containing the module's <code>main()</code>
	 *
	 * @throws SBWModuleDefinitionException if any of the arguments are
	 * invalid, perhaps because they are null or otherwise unacceptable.
	 **/
    public ModuleImpl(String uniqueName, String displayName, int type) throws SBWModuleDefinitionException {
        Class moduleClass = moduleClass();
        initModuleImpl(uniqueName, displayName, type, moduleClass, "");
    }

    /**
	 * Defines a new module.  This is a simplified version of {@link
	 * #ModuleImpl(String uniqueName, String displayName, int
	 * type, Class moduleMainClass, String helpString)} that derives most
	 * of its arguments from the calling class.  It is intended for quick
	 * implementations of modules that are not intended for widespread
	 * distribution.
	 * <p>
	 * The single argument, <code>displayName</code>, is a humanly-readable
	 * name for the module meant to be used in menus and such.  The
	 * module's unique name is derived from the class object invoking this
	 * method (this is obtained by looking through the calling stack).  The
	 * module's management type is set to {@link #SELF_MANAGED}.  The
	 * class that contains the module's <code>main</code> method is assumed
	 * to be the calling class.  The help string argument of {@link
	 * #ModuleImpl(String uniqueName, String displayName, int
	 * type, Class moduleMainClass, String helpString)} is left empty.
	 * <p>
	 * If the given <code>uniqueName</code> is already known to SBW, the
	 * information associated with that module is overwritten by the
	 * information supplied in the new call.
	 * <p>
	 * @see #ModuleImpl(String uniqueName, String displayName, int
	 * type, Class moduleMainClass)
	 * <p>
	 * @param displayName version of the module name intended for human consumption
	 * @throws SBWModuleDefinitionException if any of the arguments are
	 * invalid, perhaps because they are null or otherwise unacceptable.
	 **/
    public ModuleImpl(String displayName) throws SBWModuleDefinitionException {
        String moduleName = modulePackageName();
        Class moduleClass = moduleClass();
        initModuleImpl(moduleName, displayName, SELF_MANAGED, moduleClass, "");
    }

    /**
	 * Adds a service to the list of services provided by this module.
	 * Normally called from a module in register
	 * mode.  The <code>uniqueName</code> is a unique string that
	 * identifies the service; the <code>displayName</code> is a
	 * human-readable name, for such uses as displaying in a menu;
	 * <code>category</code> is the category into which the service
	 * belongs; and <code>implementationClass</code> is the class of the
	 * object that implements the service.  The argument
	 * <code>helpString</code> can be used to provide a string that
	 * summarizes the purpose of the service; this help string is
	 * retrievable by other modules through the SBW Broker.
	 * <p>
	 * If the service is already defined on this module, it is
	 * redefined.  Equality is determined on the basis of the
	 * <code>uniqueName</code>.
	 * <p>
	 * @param uniqueName a unique name for the service
	 * @param displayName version of the service name intended for human
	 * consumption
	 * @param category the category into which to place the service
	 * @param implementationClass the class of the object that implements
	 * the service.
	 * @param helpString a string describing the purpose of this service and
	 * perhaps offering some information about its intended usage
	 * @param methodHelpStrings a hashtable mapping method names to their
	 * help strings
	 *
	 * @throws SBWIncorrectCategorySyntaxException if the syntax of the
	 * category name is invalid
	 * @throws SBWModuleDefinitionException if any of the arguments are
	 * invalid, for example by being null or otherwise unacceptable.
	 **/
    public void addService(String uniqueName, String displayName, String category, Class implementationClass, String helpString, Hashtable methodHelpStrings) throws SBWIncorrectCategorySyntaxException, SBWModuleDefinitionException {
        if (implementationClass == null) {
            throw new SBWModuleDefinitionException("Null class supplied for service implementation", "The service implementation class must be non-null.");
        }
        try {
            addService(uniqueName, displayName, category, implementationClass.newInstance(), helpString, methodHelpStrings);
        } catch (InstantiationException e) {
            String msg = "Unable to instantiate Class object for service '" + uniqueName + "'";
            SBWLog.exception(msg, e);
            throw new SBWModuleDefinitionException(msg, "The class may be abstract, an interface, an array class," + " a primitive type, or void, or the instantiation may" + " have failed for some other reason.");
        } catch (IllegalAccessException e) {
            String msg = "Class or initializer for service '" + uniqueName + "' is not accessible";
            SBWLog.exception(msg, e);
            throw new SBWModuleDefinitionException(msg, "The class or initializer is not available.");
        } catch (ExceptionInInitializerError e) {
            String msg = "Failure during class initialization for service '" + uniqueName + "'";
            SBWLog.exception(msg, e);
            throw new SBWModuleDefinitionException(msg, "The initialization provoked by this method failed.");
        } catch (SecurityException e) {
            String msg = "No permission to create new instance of class" + " for service '" + uniqueName + "'";
            SBWLog.exception(msg, e);
            throw new SBWModuleDefinitionException(msg, "Security exception.");
        }
    }

    /**
	 * Adds a service to the list of services provided by this module.
	 * Normally called from a module in register
	 * mode.  The <code>uniqueName</code> is a unique string that
	 * identifies the service; the <code>displayName</code> is a
	 * human-readable name, for such uses as displaying in a menu;
	 * <code>category</code> is the category into which the service
	 * belongs; and <code>implementationObject</code> is an instance of
	 * the object that implements the service.  The argument
	 * <code>helpString</code> can be used to provide a string that
	 * summarizes the purpose of the service; this help string is
	 * retrievable by other modules through the SBW Broker.
	 * <p>
	 * If the service is already defined on this module, it is
	 * redefined.  Equality is determined on the basis of the
	 * <code>uniqueName</code>.
	 * <p>
	 * @param uniqueName a unique name for the service
	 * @param displayName version of the service name intended for human
	 * consumption
	 * @param category the category into which to place the service
	 * @param implementationObject an instance of the object that implements
	 * the service.
	 * @param helpString a string describing the purpose of this service and
	 * perhaps offering some information about its intended usage
	 * @param methodHelpStrings a hashtable mapping method names to their
	 * help strings
	 *
	 * @throws SBWIncorrectCategorySyntaxException if the syntax of the
	 * category name is invalid
	 * @throws SBWModuleDefinitionException if any of the arguments are
	 * invalid, for example by being null or otherwise unacceptable.
	 **/
    public void addService(String uniqueName, String displayName, String category, Object implementationObject, String helpString, Hashtable methodHelpStrings) throws SBWIncorrectCategorySyntaxException, SBWModuleDefinitionException {
        SBWLog.trace("Defining service on module '" + moduleName + "' named '" + uniqueName + "', display name '" + displayName + "', category '" + category + "'");
        if (StringUtil.empty(uniqueName)) {
            throw new SBWModuleDefinitionException("Null or empty service name", "Service names cannot be null or empty strings.");
        }
        if (!SignatureScanner.conformsToSName(uniqueName)) {
            throw new SBWModuleDefinitionException("Service identification name does not conform to allowable syntax", "Service identification names must begin with either a" + " letter or an underscore character, followed by one" + " or more letters, digits or underscore characters, " + " and contain at least one character or digit.");
        }
        if (StringUtil.empty(displayName)) {
            throw new SBWModuleDefinitionException("Null or empty service display name", "Service display names cannot be null or empty strings.");
        }
        if (category == null) {
            throw new SBWIncorrectCategorySyntaxException("Null category given", "Category names cannot be null.");
        }
        if (implementationObject == null) {
            throw new SBWModuleDefinitionException("Null object supplied for service implementation", "The service implementation object must be non-null.");
        }
        receiver.addService(uniqueName, displayName, category, implementationObject, helpString, methodHelpStrings);
    }

    /**
	 * Adds a service to the list of services provided by this module.
	 * Normally called from a module in register
	 * mode.  The <code>uniqueName</code> is a unique string that
	 * identifies the service; the <code>displayName</code> is a
	 * human-readable name, for such uses as displaying in a menu;
	 * <code>category</code> is the category into which the service
	 * belongs; and <code>implementationClass</code> is the class of the
	 * object that implements the service.  The argument
	 * <code>helpString</code> can be used to provide a string that
	 * summarizes the purpose of the service; this help string is
	 * retrievable by other modules through the SBW Broker.
	 * <p>
	 * If the service is already defined on this module, it is
	 * redefined.  Equality is determined on the basis of the
	 * <code>uniqueName</code>.
	 * <p>
	 * @param uniqueName a unique name for the service
	 * @param displayName version of the service name intended for human
	 * consumption
	 * @param category the category into which to place the service
	 * @param implementationClass the class of the object that implements
	 * the service.
	 * @param helpString a string describing the purpose of this service and
	 * perhaps offering some information about its intended usage
	 *
	 * @throws SBWIncorrectCategorySyntaxException if the syntax of the
	 * category name is invalid
	 * @throws SBWModuleDefinitionException if any of the arguments are
	 * invalid, for example by being null or otherwise unacceptable.
	 **/
    public void addService(String uniqueName, String displayName, String category, Class implementationClass, String helpString) throws SBWIncorrectCategorySyntaxException, SBWModuleDefinitionException {
        addService(uniqueName, displayName, category, implementationClass, "", savedMethodHelpStrings);
    }

    /**
	 * Adds a service to the list of services provided by this module.
	 * Normally called from a module in register
	 * mode.  The <code>uniqueName</code> is a unique string that
	 * identifies the service; the <code>displayName</code> is a
	 * human-readable name, for such uses as displaying in a menu;
	 * <code>category</code> is the category into which the service
	 * belongs; and <code>implementationObject</code> is an instance of
	 * the object that implements the service.  The argument
	 * <code>helpString</code> can be used to provide a string that
	 * summarizes the purpose of the service; this help string is
	 * retrievable by other modules through the SBW Broker.
	 * <p>
	 * If the service is already defined on this module, it is
	 * redefined.  Equality is determined on the basis of the
	 * <code>uniqueName</code>.
	 * <p>
	 * @param uniqueName a unique name for the service
	 * @param displayName version of the service name intended for human
	 * consumption
	 * @param category the category into which to place the service
	 * @param implementationObject an instance of the object that implements
	 * the service.
	 * @param helpString a string describing the purpose of this service and
	 * perhaps offering some information about its intended usage
	 *
	 * @throws SBWIncorrectCategorySyntaxException if the syntax of the
	 * category name is invalid
	 * @throws SBWModuleDefinitionException if any of the arguments are
	 * invalid, for example by being null or otherwise unacceptable.
	 **/
    public void addService(String uniqueName, String displayName, String category, Object implementationObject, String helpString) throws SBWIncorrectCategorySyntaxException, SBWModuleDefinitionException {
        addService(uniqueName, displayName, category, implementationObject, "", savedMethodHelpStrings);
    }

    /**
	 * Adds a service to the list of services provided by this module.
	 * Normally called from a module in register
	 * mode.  The <code>uniqueName</code> is a unique string that
	 * identifies the service; the <code>displayName</code> is a
	 * human-readable name, for such uses as displaying in a menu;
	 * <code>category</code> is the category into which the service
	 * belongs; and <code>implementationClass</code> is the class of the
	 * object that implements the service.
	 * <p>
	 * In this version of the method, the help string that is
	 * accepted by {@link #addService(String uniqueName, String displayName,
	 * String category, Class implementationClass,
	 * String helpString)} is set to the empty string.
	 * <p>
	 * If the service is already defined on this module, it is
	 * redefined.  Equality is determined on the basis of the
	 * <code>uniqueName</code>.
	 * <p>
	 * @param uniqueName a unique name for the service
	 * @param displayName version of the service name intended for human
	 * consumption
	 * @param category the category into which to place the service
	 * @param implementationClass the class of the object that implements
	 * the service.
	 *
	 * @throws SBWIncorrectCategorySyntaxException if the syntax of the
	 * category name is invalid
	 * @throws SBWModuleDefinitionException if any of the arguments are
	 * invalid, for example by being null or otherwise unacceptable.
	 **/
    public void addService(String uniqueName, String displayName, String category, Class implementationClass) throws SBWIncorrectCategorySyntaxException, SBWModuleDefinitionException {
        addService(uniqueName, displayName, category, implementationClass, "", savedMethodHelpStrings);
    }

    /**
	 * Adds a service to the list of services provided by this module.
	 * Normally called from a module in register
	 * mode.  The <code>uniqueName</code> is a unique string that
	 * identifies the service; the <code>displayName</code> is a
	 * human-readable name, for such uses as displaying in a menu;
	 * <code>category</code> is the category into which the service
	 * belongs; and <code>implementationObject</code> is an instance of
	 * the object that implements the service.
	 * <p>
	 * In this version of the method, the help string that is
	 * accepted by {@link #addService(String uniqueName, String displayName,
	 * String category, Class implementationClass,
	 * String helpString)} is set to the empty string.
	 * <p>
	 * If the service is already defined on this module, it is
	 * redefined.  Equality is determined on the basis of the
	 * <code>uniqueName</code>.
	 * <p>
	 * @param uniqueName a unique name for the service
	 * @param displayName version of the service name intended for human
	 * consumption
	 * @param category the category into which to place the service
	 * @param implementationObject an instance of the object that implements
	 * the service.
	 *
	 * @throws SBWIncorrectCategorySyntaxException if the syntax of the
	 * category name is invalid
	 * @throws SBWModuleDefinitionException if any of the arguments are
	 * invalid, for example by being null or otherwise unacceptable.
	 **/
    public void addService(String uniqueName, String displayName, String category, Object implementationObject) throws SBWIncorrectCategorySyntaxException, SBWModuleDefinitionException {
        addService(uniqueName, displayName, category, implementationObject, "", savedMethodHelpStrings);
    }

    /**
	 * Adds a service to the list of services provided by this module.
	 * Normally called from a module in register mode.  This is a variant
	 * method in which the unique name and display name are set to the same
	 * string, the given <code>uniqueName</code>.  Parameter
	 * <code>category</code> is the category into which the service
	 * belongs, and <code>implementationObject</code> is an instance of the
	 * object that implements the service.
	 * <p>
	 * In this version of the method, the help string that is
	 * accepted by {@link #addService(String uniqueName, String displayName,
	 * String category, Object implementationObject,
	 * String helpString)} is set to the empty string.
	 * <p>
	 * If the service is already defined on this module, it is
	 * redefined.  Equality is determined on the basis of the
	 * <code>uniqueName</code>.
	 * <p>
	 * @param uniqueName a unique name for the service; this is also used
	 * for the display name
	 * @param category the category into which to place the service
	 * @param implementationObject an instance of the object that implements
	 * the service.
	 *
	 * @throws SBWIncorrectCategorySyntaxException if the syntax of the
	 * category name is invalid
	 * @throws SBWModuleDefinitionException if any of the arguments are
	 * invalid, for example by being null or otherwise unacceptable.
	 **/
    public void addService(String uniqueName, String category, Object implementationObject) throws SBWIncorrectCategorySyntaxException, SBWModuleDefinitionException {
        addService(uniqueName, uniqueName, category, implementationObject, "", savedMethodHelpStrings);
    }

    /**
	 * Adds a service to the list of services provided by this module.
	 * Normally called from a module in register mode.  This is a variant
	 * method in which the unique name and display name are set to the same
	 * string, the given <code>uniqueName</code>.  Parameter
	 * <code>category</code> is the category into which the service
	 * belongs, and <code>implementationClass</code> is the class of object
	 * that implements the service.
	 * <p>
	 * In this version of the method, the help string that is
	 * accepted by {@link #addService(String uniqueName, String displayName,
	 * String category, Class implementationClass,
	 * String helpString)} is set to the empty string.
	 * <p>
	 * If the service is already defined on this module, it is
	 * redefined.  Equality is determined on the basis of the
	 * <code>uniqueName</code>.
	 * <p>
	 * @param uniqueName a unique name for the service; this is also used
	 * for the display name
	 * @param category the category into which to place the service
	 * @param implementationClass the class of object that implements
	 * the service.
	 *
	 * @throws SBWIncorrectCategorySyntaxException if the syntax of the
	 * category name is invalid
	 * @throws SBWModuleDefinitionException if any of the arguments are
	 * invalid, for example by being null or otherwise unacceptable.
	 **/
    public void addService(String uniqueName, String category, Class implementationClass) throws SBWIncorrectCategorySyntaxException, SBWModuleDefinitionException {
        addService(uniqueName, uniqueName, category, implementationClass, "", savedMethodHelpStrings);
    }

    /**
	 * Connects to the SBW Broker running on the local computer,
	 * sends the registration information
	 * for this module and its defined services, then returns.
	 * <p>
	 * Note that simply creating a {@link #ModuleImpl} object with one of the
	 * constructors does not actually define the module to the SBW Broker.
	 * It is not until either of the methods {@link #registerModule} or
	 * {@link #enableModuleServices} are called that SBW contacts the Broker.
	 * <p>
	 * @throws SBWCommunicationException if there is a problem communicating
	 * with the SBW Broker
	 * @throws SBWModuleDefinitionException if the SBW Broker determines there
	 * is something wrong with the module definition.
	 **/
    public void registerModule() throws SBWModuleDefinitionException, SBWBrokerStartException, SBWCommunicationException {
        SBWLowLevelInternal rpc = SBW.getInternalAPI();
        rpc.connect(moduleName, "localhost");
        BrokerInterface broker = SBW.getBrokerService();
        try {
            broker.registerModule(moduleName, moduleDisplayName, moduleType, cmdLine, helpString);
            receiver.registerServicesWithBroker(broker);
        } catch (SBWModuleDefinitionException e) {
            SBWLog.trace("Module definition exception", e);
            throw e;
        } catch (SBWException e) {
            throw new SBWCommunicationException("Unable to register module with Broker", "");
        }
    }

    /**
	 * Whereas the {@link #registerModule} method registers the module with
	 * the SBW Broker and returns, the {@link #enableModuleServices}
	 * notifies the Broker that the module is now able to receive calls.
	 * <p>
	 * @throws SBWCommunicationException if there is a problem communicating
	 * with the SBW Broker
	 **/
    public void enableModuleServices() throws SBWCommunicationException, SBWBrokerStartException {
        SBWLowLevelInternal rpc = SBW.getInternalAPI();
        rpc.registerReceiver(receiver);
        rpc.connect(moduleName, host);
    }

    /**
	 * Sets a specific command line for starting up this module.  The
	 * default command for starting up the module is based on the main
	 * class given to the constructor method {@link #ModuleImpl(String
	 * uniqueName, String displayName, int type, Class moduleMainClass)}.
	 * The default has the following generic form:
	 * <blockquote>
	 *     <i>path-to-java</i> <kbd>-classpath</kbd> <i>class-path</i> <i>moduleMainClass</i> <kbd>-sbwmodule</kbd>
	 * </blockquote>
	 * The classpath is determined from the path to the SBW home directory,
	 * and the path to java is the path to the <code>java</code> executable
	 * that was used to start this module.
	 * <p>
	 * The {@link #setCommandLine} method gives complete control over
	 * the command line that should be used to start up this module.  The
	 * command supplied must include the path to the java
	 * executable and the <kbd>-sbwmodule</kbd> argument.
	 * <p>
	 * Note that no verification is performed on the validity of the given
	 * command line.  SBW and client modules will not learn whether the
	 * given command line correctly starts up the module until the next
	 * time that SBW attempts to run it.
	 * <p>
	 * @param cmdline the explicit command line to use to start up this module
	 **/
    public void setCommandLine(String cmdline) {
        this.cmdLine = cmdline;
    }

    /**
	 * Returns the command line set for this module implementation.
	 * <p>
	 * @return a string, the command line for this module
	 * @see #setCommandLine
	 **/
    public String getCommandLine() {
        return cmdLine;
    }

    public void setHost(String sHost) {
        this.host = sHost;
    }

    public String getHost() {
        return this.host;
    }

    /**
	 * Convenience function for performing the tasks required for handling
	 * the <code>-sbwregister</code> and <code>-sbwmodule</code> options
	 * to a module.  This method must be passed the array of command-line
	 * arguments passed to the application.  This then checks for the
	 * presence of <code>-sbwregister</code> and <code>-sbwmodule</code>,
	 * and acts as follows:
	 * <ul>
	 * <li>If the flag <code>-sbwregister</code> is found first in the array of
	 * command-line arguments given to the program, this method invokes the
	 * {@link #registerModule} method followed by Java's
	 * {@link java.lang.System#exit} call.
	 * <p>
	 * <li>If the flag <code>-sbwmodule</code> is found first in the array of
	 * command-line arguments, this method invokes
	 * {@link #enableModuleServices} and returns.
	 * </ul>
	 * <p>
	 * The calling routine should perform whatever tasks it would after
	 * enabling module services.  Most modules will not need to perform
	 * anything else and should simply return.
	 * <p>
	 * The exceptions thrown by this method are simply propagated from the
	 * calls to {@link #registerModule} and {@link #enableModuleServices}.
	 * <p>
	 * @param args the array of strings passed to the <code>main</code>
	 * method of the application
	 * @throws SBWCommunicationException if there is a problem communicating
	 * with the SBW Broker
	 * @throws SBWModuleDefinitionException if the SBW Broker determines there
	 * is something wrong with the module definition.
	 **/
    public void run(String[] args) throws SBWModuleDefinitionException, SBWCommunicationException, SBWBrokerStartException {
        if (Sys.OSIsMac() || Sys.OSIsUnix()) {
            System.setProperty("java.net.preferIPv4Stack", "true");
            System.setProperty("sbw.broker.allow-remote-modules", "true");
        }
        for (int i = 0; i < args.length; i++) {
            if ("-sbwregister".equals(args[i])) {
                SBWLog.trace("Registering module");
                registerModule();
                SBWLog.trace("Done.  Exiting.");
                System.exit(0);
            } else if ("-sbwmodule".equals(args[i])) {
                SBWLog.trace("Running as module");
                enableModuleServices();
                break;
            }
        }
    }

    public static void setMethodHelp(String methodName, String helpString) {
        if (savedMethodHelpStrings == null) {
            savedMethodHelpStrings = new Hashtable();
        }
        savedMethodHelpStrings.put(methodName, helpString);
    }

    /**
	 * Helper method.
	 **/
    private void initModuleImpl(String uniqueName, String displayName, int type, Class moduleMainClass, String helpString) throws SBWModuleDefinitionException {
        if (StringUtil.empty(uniqueName)) {
            throw new SBWModuleDefinitionException("Null or empty module name", "Module names cannot be null or empty strings.");
        }
        if (uniqueName.indexOf(':') > 0) {
            throw new SBWModuleDefinitionException("The colon ':' character is not allowed in module names", "Module names cannot have a ':' character in them.  In" + " SBW, the colon character separates host names from module" + " names in contexts where remote modules may be specified." + " Colon characters are disallowed from module names to" + " prevent ambiguity in those cases.");
        }
        if (StringUtil.empty(displayName)) {
            throw new SBWModuleDefinitionException("Null or empty module display name", "Module display names cannot be null or empty strings.");
        }
        if (type != UNIQUE && type != SELF_MANAGED) {
            throw new SBWModuleDefinitionException("Invalid module type specified", "Module type must be one of ModuleImpl.SELF_MANAGED, " + "ModuleImpl.SBW_MANAGED or ModuleImpl.UNIQUE");
        }
        if (moduleMainClass == null) {
            throw new SBWModuleDefinitionException("Null class supplied for module main class", "The module main class must be non-null.");
        }
        this.moduleName = uniqueName;
        this.moduleDisplayName = displayName;
        this.moduleType = type;
        this.moduleMainClass = moduleMainClass;
        this.cmdLine = defaultCommandLine(moduleMainClass);
        this.helpString = helpString;
        if (this.savedMethodHelpStrings != null) {
            this.savedMethodHelpStrings.clear();
        } else {
            this.savedMethodHelpStrings = new Hashtable();
        }
        receiver = new ObjectOrientedReceiver(uniqueName);
    }

    /**
	 * Determines the package name of the object that has created the present
	 * instance of {@link #ModuleImpl}, and returns it as a string.
	 * <p>
	 * @return a string representing the package name
	 **/
    private String modulePackageName() {
        StackTrace trace = new StackTrace(2 + Config.getStackDepthOffset());
        String className = trace.getClassname();
        if (className.indexOf('.') > 0) {
            return className.substring(0, className.lastIndexOf('.'));
        } else {
            return className;
        }
    }

    /**
	 * Determines the class of the object that has created the present
	 * instance of {@link #ModuleImpl}, and returns it as a {@link
	 * java.lang.Class} object.
	 * <p>
	 * @return a <code>Class</code> object
	 **/
    private Class moduleClass() throws SBWModuleDefinitionException {
        StackTrace trace = new StackTrace(2 + Config.getStackDepthOffset());
        String className = trace.getClassname();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            SBWLog.exception("Cannot recreate class " + className, e);
            throw new SBWModuleDefinitionException("Class " + className + " is inaccessible", "Should be able to create an instance of class " + className + ", but cannot");
        }
    }

    /**
	 * Constructs a command to be used to start up a module.  The command
	 * is intended to be executed using {@link #execJavaCommand(String
	 * displayName, String cmd)}, which will use this string to construct
	 * the actual command that is exec'ed.
	 *
	 * @param mainClass the class containing the main() method
	 * @return a string containing the command line
	 **/
    private static String defaultCommandLine(Class mainClass) {
        String moduleCmd;
        String options = Config.getModuleOptions();
        moduleCmd = Sys.getJavaExecutable();
        if (options != null) {
            moduleCmd += " ";
            moduleCmd += options;
        }
        String cp = System.getProperty("java.class.path");
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(cp, File.pathSeparator);
        if (Sys.OSIsWindows()) moduleCmd += " -Dsbw.user=\"%SBW_USER%\""; else moduleCmd += " -Dsbw.user=\"$SBW_USER$\"";
        moduleCmd += " -classpath \"";
        while (tokenizer.hasMoreTokens()) {
            File cpfile = new File(tokenizer.nextToken());
            if (!cpfile.exists()) {
                SBWLog.error("Class path value '" + cpfile + "' does not exist");
            } else if (!cpfile.canRead()) {
                SBWLog.error("Class path value '" + cpfile + "' is not readable");
            }
            if (cpfile.canRead() && (cpfile.isDirectory() || cpfile.isFile())) {
                try {
                    moduleCmd += cpfile.getCanonicalPath();
                } catch (IOException e) {
                    moduleCmd += cpfile.getPath();
                }
                if (tokenizer.hasMoreTokens()) {
                    moduleCmd += File.pathSeparatorChar;
                }
            } else {
                SBWLog.error("Class path value '" + cpfile + "'is not a file or directory");
            }
        }
        moduleCmd += "\" ";
        moduleCmd += mainClass.getName();
        moduleCmd += " -sbwmodule";
        return moduleCmd;
    }

    private static Hashtable savedMethodHelpStrings;

    private String moduleName;

    private String moduleDisplayName;

    private String cmdLine;

    private String helpString;

    private String host = "localhost";

    private int moduleType;

    private Class moduleMainClass;

    private ObjectOrientedReceiver receiver;

    static {
        Config.recordClassVersion(ModuleImpl.class, "$Id: ModuleImpl.java,v 1.3 2007/07/24 23:08:23 fbergmann Exp $");
    }
}

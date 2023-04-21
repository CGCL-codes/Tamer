package net.sf.etl.parsers.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import net.sf.etl.parsers.AbstractTreeParser;
import net.sf.etl.parsers.ObjectName;
import net.sf.etl.parsers.ParserException;
import net.sf.etl.parsers.TermParser;
import net.sf.etl.parsers.TermToken;

/**
 * This class provide facilities for mapping class names.
 * 
 * @author const
 * 
 * @param <BaseObjectType>
 *            this is a base type for returned objects
 * @param <FeatureType>
 *            this is a type for feature metatype used by objects
 * @param <MetaObjectType>
 *            this is a type for meta object type
 * @param <HolderType>
 *            this is a holder type for collection properties
 */
public abstract class AbstractReflectionParser<BaseObjectType, FeatureType, MetaObjectType, HolderType> extends AbstractTreeParser<BaseObjectType, FeatureType, MetaObjectType, HolderType> {

    /** a logger */
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(AbstractReflectionParser.class.getName());

    /** A class loader that should be used to load classes */
    protected final ClassLoader classLoader;

    /** the active token collectors */
    protected final ArrayList<TokenCollector> collectors = new ArrayList<TokenCollector>();

    /** A map from namespace to java package */
    protected final HashMap<String, String> namespaceMapping = new HashMap<String, String>();

    /** A map from namespace to object to java class */
    protected final HashMap<String, HashMap<String, Class<? extends Object>>> objectMapping = new HashMap<String, HashMap<String, Class<? extends Object>>>();

    /**
	 * A constructor
	 * 
	 * @param parser
	 *            a term parser to use
	 * @param classLoader
	 *            a class loader for the parser
	 */
    public AbstractReflectionParser(TermParser parser, ClassLoader classLoader) {
        super(parser);
        if (classLoader == null) {
            classLoader = getClassLoader();
        }
        this.classLoader = classLoader;
    }

    /**
	 * This method tries to detect class loader that should be used by this
	 * instance in case when class loader is not provided by creator of the
	 * parser.
	 * 
	 * The method checks contextClassLoader of the thread, and if is still not
	 * found, uses class loader of parser class. Note if the class is
	 * subclassed, a classloader of the subclass will be used.
	 * 
	 * @return a class loader
	 */
    protected ClassLoader getClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (final Exception ex) {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "There is a security problem with loading classLoader", ex);
            }
        }
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }
        return classLoader;
    }

    /**
	 * Add mapping from namespace to java package
	 * 
	 * @param namespace
	 *            a namespace
	 * @param javaPackage
	 *            a java package
	 */
    public void mapNamespaceToPackage(String namespace, String javaPackage) {
        namespaceMapping.put(namespace, javaPackage);
    }

    /**
	 * Map name to the class
	 * 
	 * @param namespace
	 *            object namespace
	 * @param name
	 *            object name in parser
	 * @param beanClass
	 *            class of java bean
	 */
    public void mapNameToClass(String namespace, String name, Class<? extends Object> beanClass) {
        HashMap<String, Class<? extends Object>> nameToClass = objectMapping.get(namespace);
        if (nameToClass == null) {
            nameToClass = new HashMap<String, Class<? extends Object>>();
            objectMapping.put(namespace, nameToClass);
        }
        nameToClass.put(name, beanClass);
    }

    /**
	 * Get object class
	 * 
	 * @param name
	 *            a name of class
	 * @return class for the object name
	 */
    protected Class<? extends Object> getObjectClass(ObjectName name) {
        final HashMap<String, Class<? extends Object>> nameToObject = objectMapping.get(name.namespace());
        if (nameToObject != null) {
            final Class<? extends Object> rc = nameToObject.get(name.name());
            if (rc != null) {
                return rc;
            }
        }
        final String packageName = namespaceMapping.get(name.namespace());
        if (packageName != null) {
            final String className = packageName + "." + name.name();
            Class<? extends Object> rc;
            try {
                if (classLoader != null) {
                    rc = classLoader.loadClass(className);
                } else {
                    rc = Class.forName(className);
                }
                return rc;
            } catch (final ClassNotFoundException ex) {
                if (log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, "Class has not been found for name: " + name);
                }
            }
        }
        throw new ParserException("Class not found for object name " + name);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean advanceParser() {
        if (!collectors.isEmpty()) {
            TermToken t = parser.current();
            for (TokenCollector c : collectors) {
                c.collect(t);
            }
        }
        return super.advanceParser();
    }

    /** {@inheritDoc} */
    @Override
    protected void objectEnded(BaseObjectType object) {
        if (object instanceof TokenCollector) {
            TokenCollector r = collectors.remove(collectors.size() - 1);
            assert r == object;
        }
        super.objectEnded(object);
    }

    /** {@inheritDoc} */
    @Override
    protected void objectStarted(BaseObjectType object) {
        if (object instanceof TokenCollector) {
            collectors.add((TokenCollector) object);
        }
        super.objectStarted(object);
    }
}

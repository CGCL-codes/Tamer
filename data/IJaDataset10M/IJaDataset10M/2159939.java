package org.apache.jdo.impl.model.jdo.caching;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.jdo.model.java.JavaModel;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.impl.model.jdo.JDOModelImplDynamic;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.util.StringHelper;

/**
 * A JDOModel instance bundles a number of JDOClass instances used by an 
 * application. It provides factory methods to create and retrieve JDOClass 
 * instances. A fully qualified class name must be unique within a JDOModel 
 * instance. The model supports multiple classes having the same fully qualified 
 * name by different JDOModel instances.
 * <p>
 * The caching JDOModel implementation caches any caclulated value to
 * avoid re-calculating it if it is requested again. It is intended to
 * be used in an environment where JDO metadata does NOT change
 * (e.g. at runtime).
 *
 * @author Michael Bouschen
 * @since 1.1
 * @version 2.0
 */
public class JDOModelImplCaching extends JDOModelImplDynamic {

    /**
     * This is a mapping from short names to JDOClass instances. Key is the
     * JDOClass short name, value is the corresponding JDOClass instance.  
     */
    private Map jdoClassesForShortNames = new HashMap();

    /** 
     * This is a mapping from ObjectId classes to its JDOClass instances.
     * Key is the type representation of the ObjectId class, value is the 
     * corresponding JDOClass instance. Note, in the case of inheritance
     * the top most persistence-capable class is stored.
     */
    private Map jdoClassesForObjectIdClasses = new HashMap();

    /** 
     * Set of fully qualified names of classes known to be 
     * non persistence-capable. 
     */
    private Set nonPCClasses = new HashSet();

    /** 
     * Constructor. 
     * JDOModel instances are created using the JDOModelFactory only.
     */
    protected JDOModelImplCaching(JavaModel javaModel, boolean loadXMLMetadataDefault) {
        super(javaModel, loadXMLMetadataDefault);
    }

    /**
     * The method returns the JDOClass instance for the specified short name
     * (see {@link JDOClass#getShortName()}) or <code>null</code> if it cannot
     * find a JDOClass instance with the specified short name. 
     * <p>
     * The method searches the list of JDOClasses currently managed by this
     * JDOModel instance. It does not attempt to load any metadata if it
     * cannot find a JDOClass instance with the specified short name. The
     * metadata for a JDOClass returned by this method must have been loaded
     * before by any of the methods
     * {@link #createJDOClass(String className)},
     * {@link #createJDOClass(String className, boolean loadXMLMetadataDefault)},
     * {@link #getJDOClass(String className)}, or
     * {@link #getJDOClass(String className, boolean loadXMLMetadataDefault)}.
     * @param shortName the short name of the JDOClass instance to be returned
     * @return a JDOClass instance for the specified short name 
     * or <code>null</code> if not present
     */
    public synchronized JDOClass getJDOClassForShortName(String shortName) {
        if (StringHelper.isEmpty(shortName)) return null;
        JDOClass jdoClass = (JDOClass) jdoClassesForShortNames.get(shortName);
        if (jdoClass == null) {
            jdoClass = super.getJDOClassForShortName(shortName);
            if (jdoClass != null) {
                jdoClassesForShortNames.put(shortName, jdoClass);
            }
        }
        return jdoClass;
    }

    /**
     * This method returns the JDOClass instance that defines the specified type
     * as its objectId class. In the case of an inheritance hierarchy it returns 
     * the top most persistence-capable class of the hierarchy (see 
     * {@link JDOClass#getPersistenceCapableSuperclass}).
     * @param objectIdClass the type representation of the ObjectId class
     * @return the JDOClass defining the specified class as ObjectId class
     */
    public JDOClass getJDOClassForObjectIdClass(JavaType objectIdClass) {
        if (objectIdClass == null) return null;
        synchronized (jdoClassesForObjectIdClasses) {
            JDOClass jdoClass = (JDOClass) jdoClassesForObjectIdClasses.get(objectIdClass);
            if (jdoClass == null) {
                jdoClass = super.getJDOClassForObjectIdClass(objectIdClass);
                if (jdoClass != null) {
                    jdoClassesForObjectIdClasses.put(objectIdClass, jdoClass);
                }
            }
            return jdoClass;
        }
    }

    /** Returns a new instance of the JDOClass implementation class. */
    protected JDOClass newJDOClassInstance(String name) {
        return new JDOClassImplCaching(name);
    }

    /**
     * Checks whether the type with the specified name does NOT denote a
     * persistence-capable class.
     * @param typeName name of the type to be checked
     * @return <code>true</code> if types is a name of a primitive type; 
     * <code>false</code> otherwise
     */
    protected boolean isKnownNonPC(String typeName) {
        return super.isKnownNonPC(typeName) || nonPCClasses.contains(typeName);
    }

    /** 
     * Hook called when a class is known to be non persistence
     * capable.
     * @param className the name of the non-pc class
     */
    protected void knownNonPC(String className) {
        nonPCClasses.add(className);
    }
}

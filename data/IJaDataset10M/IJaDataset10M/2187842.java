package org.springunit.framework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.springunit.framework.junit4.SpringUnit4Test;
import org.springunit.framework.junit4.SpringUnit4TransactionalTest;

/**
 * Manages the contexts of a SpringUnit test.
 * Stores an ordered list of the classes that comprise
 * the class hierarchy for the test (<code>testClasses</code>).
 * The first element in the list is the class of the test,
 * the last element is the descendent of the descendent
 * of AbstractDependencyInjectionSpringContextTests
 * or AbstractTransactionalSpringContextTests
 * that is an ancestor of the test class.<br/>
 * Using the list of classes in the hierarchy,
 * creates and provides a corresponding list of
 * resources that describe configuration data used
 * to populate the corresponding contexts.<br/>
 * Given a name for a data value, a name of a test
 * and the test object itself, traverses the
 * hierarchy of contexts to return the requested
 * data value, if it exists.<br/>
 * 
 * @author <a href="mailto:ted@velkoff.com">Ted Velkoff</a>
 *
 */
public class HierarchicalSpringUnitContext<S extends AbstractDependencyInjectionSpringContextTests> {

    /**
	 * Given the class of the current test (<code>testClass</code>),
	 * construct an ordered list of classes, where the first class is
	 * <code>testClass</code>, the second class is the superclass of
	 * <code>testClass</code> (if any), the third class is the superclass
	 * of the second (if any), etc., and the last class is
	 * the immediate descendent of the immediate descendent of
	 * AbstractDependencyInjectionSpringContextTests
	 * or AbstractTransactionalSpringContextTests.
	 * @param testClass Class of test whose class hierarchy is to be constructed.
	 */
    protected HierarchicalSpringUnitContext(Class<? extends S> testClass) {
        this.resourceLoader = new DefaultResourceLoader();
        this.testClasses = new ArrayList<Class<? super S>>();
        buildTestClasses(this.testClasses, (Class<? super S>) testClass);
    }

    /**
	 * Given the list of test classes in the hierarchy,
	 * return the list of resource names that satisfy the naming convention
	 * "<i>Classname</i>.xml".<br/>
	 * Caches the list of locations so this need only be computed once.
	 * @return Array of string filenames
	 */
    protected String[] getConfigLocations() {
        if (this.configLocations == null) {
            this.configLocations = createDataFileNames(this.testClasses);
        }
        return this.configLocations;
    }

    /**
	 * Search for object identified by <code>key</code>
	 * in the hierarchy of classes descending from
	 * the descendent of AbstractDependencyInjectionSpringContextTests
	 * or AbstractTransactionalSpringContextTests.
	 * This hierarchy is expressed as a list (<code>testClasses</code>)
	 * with lowest first and highest last.
	 * Iterates through this list,
	 * calling <code>getObject(key, getName(), c)</code>
	 * until a non-null result is returned or
	 * every member of the list has been visited.
	 * @param key Identifier of data value to find
	 * @param fName Name of test
	 * @param test SpringUnit test whose context will be searched
	 * @return Object of type T if found or null
	 * @throws Exception if errors occur when using reflection
	 * to access the SpringUnitContext for any
	 * class in the list
	 */
    protected <T extends Object> T getObject(String key, String fName, Object test) throws Exception {
        for (Class<? super S> c : this.testClasses) {
            T obj = (T) getObject(key, fName, c, test);
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }

    /**
	 * Using reflection to obtain the SpringUnitContext associated
	 * with class <code>c</code>, return the result of calling
	 * <code>getObject</code> on that context for
	 * identifier <code>key</code> and test method
	 * <code>fName</code>.
	 * @param key Identifier of data value
	 * @param fName Name of test in whose scope to begin search
	 * @param c Class whose SpringUnitContext to be searched
	 * @param test SpringUnit test whose context will be searched
	 * @return Object of type T if found, or null
	 * @throws Exception if errors occur when using reflection
	 * to access the SpringUnitContext for class <code>c</code>
	 */
    private <T extends Object> T getObject(String key, String fName, Class<? super S> c, Object test) throws Exception {
        assert c != null : "c != null";
        String methodName = "get" + c.getSimpleName();
        Method method = c.getMethod(methodName, new Class[0]);
        SpringUnitContext<T> dataContext = (SpringUnitContext<T>) method.invoke(test, new Object[0]);
        return dataContext.getObject(key, fName);
    }

    /**
	 * Generate an array of resource names, given a list of <code>classes</code>.
	 * For each class <code>c</code> in <code>classes</code>,
	 * form a resource name by converting its fully qualified name
	 * to a path and appending the file extension ".xml".
	 * For backward compatibility, resource names derived from
	 * the simple class name are generated when a resource
	 * identified by the fully qualified name cannot be found.
	 * By convention, each class descending from a SpringUnit test has
	 * an associated file named <code><i>Classname</i>.xml</code>. 
	 * @param classes List of classes that are ancestors of the
	 * test class and proper descendents of a SpringUnit test
	 * @return Array of string resource names
	 */
    private String[] createDataFileNames(List<Class<? super S>> classes) {
        assert classes != null : "classes != null";
        String[] result = new String[classes.size()];
        int i = 0;
        for (Class<? super S> c : classes) {
            result[i++] = createDataFileName(c);
        }
        return result;
    }

    /**
	 * Derive the resource name of the XML bean configuration file
	 * from the name of the class <code>c</code>.<br/>
	 * @param c Class whose bean configuration resource name
	 * will be derived
	 * @return Name of resource containing the corresponding
	 * XML bean configuration file
	 */
    private String createDataFileName(Class c) {
        assert c != null : "c != null";
        StringBuilder result = new StringBuilder();
        result.append("classpath:");
        String name = c.getName();
        int index = 0;
        int dot = name.indexOf('.', index);
        while (dot > -1) {
            result.append(name.substring(index, dot));
            result.append('/');
            index = dot + 1;
            dot = name.indexOf('.', index);
        }
        result.append(name.substring(index));
        result.append(".xml");
        return simpleOrFullyQualifiedName(result.toString());
    }

    /**
	 * Return <code>resourceName</code> if its corresponding resource exists.
	 * Otherwise, derive a name for the resource if it were held in the
	 * root directory of the classpath and return that name.
	 * <br/>
	 * This provides backward-compatibility with earlier versions of
	 * the framework, which required that the XML data files be
	 * kept in the root directory.
	 * @param resourceName Name of the resource to locate
	 * @return resourceName if resource exists, otherwise
	 * a derived name of a resource found in the root of the classpath
	 */
    private String simpleOrFullyQualifiedName(String resourceName) {
        Resource resource = this.resourceLoader.getResource(resourceName);
        if (resource.exists()) {
            return resourceName;
        }
        StringBuilder result = new StringBuilder();
        result.append("classpath:");
        String name = resourceName.substring("classpath:".length());
        int slash = name.lastIndexOf('/');
        if (slash < 0) {
            result.append(name);
        } else {
            result.append(name.substring(slash + 1));
        }
        return result.toString();
    }

    /**
	 * Build the list of ancestors of class <code>c</code>
	 * that are proper descendents of the immediate
	 * descendent of AbstractDependencyInjectionSpringContextTests.<br/>
	 * @param c Class to be added to list of <code>classes</code>
	 * unless <code>c.getSuperClass()</code> is
	 * <code>org.springframework.test.AbstractDependencyInjectionSpringContextTests</code> or
	 * <code>org.springframework.test.AbstractTransactionalSpringContextTests</code>,
	 * in which case the recursion stops.
	 * @param classes List that accumulates the ordered list of
	 * ancestors of <code>c</code>
	 */
    private void buildTestClasses(List<Class<? super S>> classes, Class<? super S> c) {
        assert c != null : "c != null";
        assert classes != null : "classes != null";
        Class<? super S> superclass = c.getSuperclass();
        if (!AbstractDependencyInjectionSpringContextTests.class.equals(superclass) && !AbstractTransactionalSpringContextTests.class.equals(superclass) && !SpringUnit4Test.class.equals(c) && !SpringUnit4TransactionalTest.class.equals(c)) {
            classes.add(c);
            buildTestClasses(classes, superclass);
        }
    }

    /**
	 * List of classes that comprise the class hierarchy
	 * of the test.  The class of the test is first
	 * in the list.<br/>
	 */
    private List<Class<? super S>> testClasses;

    /**
	 * List of names of resources that hold test data.
	 * This caches the list of locations so it need only
	 * be generated once.
	 */
    private String[] configLocations;

    /**
	 * ResourceLoader is used to check for existence of data
	 * configuration files that correspond to the classes of the
	 * test hierarchy.  This check provides backward compatibility
	 * for data files stored in the root directory of the classpath.
	 */
    private ResourceLoader resourceLoader;
}

package tigerunit.runner;

import tigerunit.framework.Test;
import tigerunit.framework.TestSuite;
import tigerunit.util.ClassFinder;
import tigerunit.util.Log;
import java.util.Collection;
import java.util.LinkedList;
import java.io.InputStream;

/**
 * Load tests using the legacy JUnit hierarchy: If the test class has
 * a static suite() method, that method is used to get the test suite. If the
 * class has a static SUITE field, that field is used. Otherwise, a TestSuite
 * is created from the class, meaning that each public instance method within
 * the class that begins with "test" is treated as a test case. For the most
 * part, this class delegates to the static create* methods in
 * {@link tigerunit.framework.TestSuite}.
 */
public class HierarchyTestLoader extends AbstractTestLoader {

    public Collection<Test> loadTestsFromClasses(Collection<Class> classes) throws Exception {
        if ((getIncludedCategories() != null && !getIncludedCategories().isEmpty()) || (getExcludedCategories() != null && !getExcludedCategories().isEmpty())) {
            Log.info("Categories are ignored for HierarchyTestLoader");
        }
        Collection<Test> tests = new LinkedList<Test>();
        for (Class c : classes) {
            tests.add(TestSuite.createTestSuite(c));
        }
        return tests;
    }

    protected Collection<Test> loadTestsFromConfiguration(InputStream config, ClassFinder finder) throws Exception {
        throw new UnsupportedOperationException();
    }
}

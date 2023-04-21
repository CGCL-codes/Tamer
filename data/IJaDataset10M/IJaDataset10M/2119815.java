package gnu.beanfactory.admin;

/**
 *  JUnit Test Suite for the gnu.beanfactory package
 **/
public class PackageTestSuite extends junit.framework.TestSuite {

    public PackageTestSuite() {
        addTestSuite(NamespaceNavigatorTestCase.class);
    }
}

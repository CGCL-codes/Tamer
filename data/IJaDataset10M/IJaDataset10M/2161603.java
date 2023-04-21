package test.org.one.stone.soup.server.beanserver;

/**
 * @author nikcross
 *
 */
public class TestBeanFactory {

    /**
	 *
	 */
    private TestBeanFactory() {
        super();
    }

    private static TestBeanFactory testBeanFactory;

    public static TestBeanFactory getInstance() {
        if (testBeanFactory == null) {
            testBeanFactory = new TestBeanFactory();
        }
        return testBeanFactory;
    }

    public static TestBeanB getAddress() {
        TestBeanA beanA = BeanServerTest.createTestBeanA();
        TestBeanB bean = beanA.getAddress();
        return bean;
    }

    public static TestBeanA getProfile() {
        TestBeanA bean = BeanServerTest.createTestBeanA();
        return bean;
    }

    public static TestBeanA getProfile(TestBeanA a) {
        TestBeanA bean = BeanServerTest.createTestBeanA();
        return bean;
    }
}

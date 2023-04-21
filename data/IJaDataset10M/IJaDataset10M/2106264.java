package org.settings4j.helper.spring;

import junit.framework.TestCase;
import org.settings4j.contentresolver.ClasspathContentResolver;
import org.settings4j.objectresolver.SpringConfigObjectResolver;

/**
 * TestSuite for {@link Settings4jFactoryBean}.
 * 
 * @author brabenetz
 *
 */
public class Settings4jFactoryBeanTest extends TestCase {

    /**
     * Test simple UseCase and read a String Object. 
     * <p>
     * See /src/test/resources/org/settings4j/helper/spring/Settings4jFactoryBeanHappyPath
     */
    public void testHappyPath() {
        System.setProperty("Spring.HappyPathTest", "Hallo World");
        final Object result = getObjectFromSpringConfig("org/settings4j/helper/spring/Settings4jFactoryBeanHappyPath");
        assertEquals("Hallo World", result);
    }

    /**
     * Test complex UseCase with "defaultObject". 
     * <p>
     * See /src/test/resources/org/settings4j/helper/spring/Settings4jFactoryBeanHappyPathComplex
     */
    public void testHappyPathComplex() {
        Object result;
        DummySessionFactory sessionFactory;
        result = getObjectFromSpringConfig("org/settings4j/helper/spring/Settings4jFactoryBeanHappyPathComplex");
        assertEquals(DummySessionFactory.class.getName(), result.getClass().getName());
        sessionFactory = (DummySessionFactory) result;
        assertEquals("test Property Value 1", sessionFactory.getHibernateProperties().get("testProperty1"));
        assertEquals("test Property Value 2", sessionFactory.getHibernateProperties().get("testProperty2"));
        assertNull(sessionFactory.getHibernateProperties().get("testProperty3"));
        System.setProperty("Spring.HappyPathComplexTest", "classpath:org/settings4j/helper/spring/CustomHibernate.properties");
        result = getObjectFromSpringConfig("org/settings4j/helper/spring/Settings4jFactoryBeanHappyPathComplex");
        assertEquals(DummySessionFactory.class.getName(), result.getClass().getName());
        sessionFactory = (DummySessionFactory) result;
        assertEquals("test Property Value 1 Custom", sessionFactory.getHibernateProperties().get("testProperty1"));
        assertEquals("test Property Value 2", sessionFactory.getHibernateProperties().get("testProperty2"));
        assertEquals("test Property Value 3 Custom", sessionFactory.getHibernateProperties().get("testProperty3"));
    }

    private Object getObjectFromSpringConfig(final String key) {
        final SpringConfigObjectResolver springConfigObjectResolver = new SpringConfigObjectResolver();
        final Object result = springConfigObjectResolver.getObject(key, new ClasspathContentResolver());
        return result;
    }
}

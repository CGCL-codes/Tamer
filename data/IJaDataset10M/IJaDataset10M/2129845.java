package sto.orz.dao;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * Base class for running Dao tests.
 * @author mraible
 */
public abstract class BaseDaoTestCase extends AbstractTransactionalDataSourceSpringContextTests {

    protected final Log log = LogFactory.getLog(getClass());

    protected ResourceBundle rb;

    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] { "classpath*:/**/dao/applicationContext-*.xml", "classpath*:META-INF/spring/applicationContext-*.xml" };
    }

    public BaseDaoTestCase() {
        String className = this.getClass().getName();
        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
        }
    }

    /**
     * Utility method to populate a javabean-style object with values
     * from a Properties file
     * @param obj
     * @return Object populated object
     * @throws Exception
     */
    protected Object populate(Object obj) throws Exception {
        Map map = new HashMap();
        for (Enumeration keys = rb.getKeys(); keys.hasMoreElements(); ) {
            String key = (String) keys.nextElement();
            map.put(key, rb.getString(key));
        }
        BeanUtils.copyProperties(obj, map);
        return obj;
    }

    protected void flushSession() {
        SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
        sessionFactory.getCurrentSession().flush();
    }
}

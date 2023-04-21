package edu.ftn.ais.service;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.ftn.ais.util.ConvertUtil;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public abstract class BaseManagerTestCase extends AbstractTransactionalDataSourceSpringContextTests {

    protected final Log log = LogFactory.getLog(getClass());

    protected static ResourceBundle rb = null;

    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] { "/applicationContext-resources.xml", "classpath:/applicationContext-dao.xml", "/applicationContext-service.xml", "classpath*:/**/applicationContext.xml" };
    }

    public BaseManagerTestCase() {
        String className = this.getClass().getName();
        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
        }
    }

    /**
     * Utility method to populate a javabean-style object with values
     * from a Properties file
     *
     * @param obj the model object to populate
     * @return Object populated object
     * @throws Exception if BeanUtils fails to copy properly
     */
    protected Object populate(Object obj) throws Exception {
        Map map = ConvertUtil.convertBundleToMap(rb);
        BeanUtils.copyProperties(obj, map);
        return obj;
    }
}

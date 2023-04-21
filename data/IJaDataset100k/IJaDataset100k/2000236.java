package org.appfuse.service;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.util.ConvertUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@ContextConfiguration(locations = { "classpath:/applicationContext-resources.xml", "classpath:/applicationContext-dao.xml", "classpath:/applicationContext-service.xml", "classpath*:/**/applicationContext.xml" })
public abstract class BaseManagerTestCase extends AbstractTransactionalJUnit4SpringContextTests {

    /**
     * A simple logger
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * The resourceBundle
     */
    protected ResourceBundle rb;

    /**
     * Default constructor will set the ResourceBundle if needed.
     */
    public BaseManagerTestCase() {
        String className = this.getClass().getName();
        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
        }
    }

    /**
     * Utility method to populate an object with values from a properties file
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

package com.zjnan.app.service.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.zjnan.app.util.ConvertUtil;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Mockery;
import org.junit.runner.RunWith;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@RunWith(JMock.class)
public abstract class BaseManagerMockTestCase {

    protected final Log log = LogFactory.getLog(getClass());

    protected ResourceBundle rb;

    protected Mockery context = new JUnit4Mockery();

    public BaseManagerMockTestCase() {
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

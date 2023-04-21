package com.liferay.portal.spring.context;

import com.liferay.portal.bean.BeanLocatorImpl;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import java.io.FileNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * <a href="TunnelApplicationContext.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TunnelApplicationContext extends XmlWebApplicationContext {

    public void setParent(ApplicationContext parent) {
        if (parent == null) {
            BeanLocatorImpl beanLocatorImpl = (BeanLocatorImpl) PortalBeanLocatorUtil.getBeanLocator();
            parent = beanLocatorImpl.getApplicationContext();
        }
        super.setParent(parent);
    }

    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) {
        String[] configLocations = getConfigLocations();
        if (configLocations == null) {
            return;
        }
        for (String configLocation : configLocations) {
            try {
                reader.loadBeanDefinitions(configLocation);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof FileNotFoundException) {
                    if (_log.isWarnEnabled()) {
                        _log.warn(cause.getMessage());
                    }
                } else {
                    _log.error(e, e);
                }
            }
        }
    }

    private static Log _log = LogFactory.getLog(TunnelApplicationContext.class);
}

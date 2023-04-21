package org.gbif.portal.web.download;

import java.lang.reflect.Method;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;

/**
 * Retrieves the value from a nested bean.
 *  
 * @author dmartin
 */
public class NestedField extends Field {

    protected String nestedBeanName;

    /**
	 * @see org.gbif.portal.web.download.Field#getRenderValue(javax.servlet.http.HttpServletRequest, org.springframework.context.MessageSource, java.util.Locale, java.lang.Object)
	 */
    @Override
    public String getRenderValue(HttpServletRequest request, MessageSource messageSource, Locale locale, Object bean) {
        try {
            String beanName = StringUtils.capitalize(nestedBeanName);
            Method getter = bean.getClass().getMethod("get" + beanName, (Class[]) null);
            Object nestedBean = getter.invoke(bean, (Object[]) null);
            String propertyValue = BeanUtils.getProperty(nestedBean, fieldName);
            return getFieldValue(messageSource, locale, propertyValue);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
	 * @return the nestedBeanName
	 */
    public String getNestedBeanName() {
        return nestedBeanName;
    }

    /**
	 * @param nestedBeanName the nestedBeanName to set
	 */
    public void setNestedBeanName(String nestedBeanName) {
        this.nestedBeanName = nestedBeanName;
    }
}

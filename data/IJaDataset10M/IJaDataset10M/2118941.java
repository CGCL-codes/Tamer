package com.liferay.util.spring.jndi;

import com.liferay.portal.kernel.jndi.PortalJNDIUtil;
import javax.sql.DataSource;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * <a href="PortalDataSourceFactoryBean.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PortalDataSourceFactoryBean extends AbstractFactoryBean {

    public Class getObjectType() {
        return DataSource.class;
    }

    protected Object createInstance() throws Exception {
        return PortalJNDIUtil.getDataSource();
    }
}

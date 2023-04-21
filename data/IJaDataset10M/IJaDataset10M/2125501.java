package org.teaframework.services.jdbc;

import javax.sql.DataSource;
import org.teaframework.exception.DataSourceConfigurationException;
import org.teaframework.exception.ServiceLocatorException;
import org.teaframework.services.ejb.ServiceLocator;
import org.apache.commons.lang.StringUtils;

/**
 * Database datasource serivce implementation
 * 
 * @author <a href="mailto:founder_chen@yahoo.com.cn">Peter Cheng </a>
 * @version $Revision: 1.5 $ $Date: 2006/02/15 08:45:45 $
 * @version Revision: 1.0
 */
public class DataSourceServiceImpl implements DataSourceService {

    private ServiceLocator serviceLocator;

    /**
     * @param serviceLocator
     */
    public DataSourceServiceImpl(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    /**
     * @see org.teaframework.services.jdbc.DataSourceService#getDataSource(java.lang.String)
     */
    public DataSource getDataSource(String dataSourceName) throws DataSourceConfigurationException, ServiceLocatorException {
        if (StringUtils.isEmpty(dataSourceName)) {
            return DataSourceFactory.getDataSource();
        } else {
            return serviceLocator.getDataSource(dataSourceName);
        }
    }
}

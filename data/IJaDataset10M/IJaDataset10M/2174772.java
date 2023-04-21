package org.plazmaforge.framework.platform.structure.impl.spring;

import org.plazmaforge.framework.platform.structure.FrameworkContext;
import org.springframework.context.ApplicationContext;

public class SpringContext implements FrameworkContext {

    /** Location List **/
    public static final String DEFAULT_LOCATION_LIST_PROPERTY = "application-context-list.xml";

    /** Location * */
    public static final String DEFAULT_LOCATION_PROPERTY = "application-context.xml";

    /** Data Source * */
    public static final String DEFAULT_DATA_SOURCE_PROPERTY = "dataSource";

    /** Session Factory * */
    public static final String DEFAULT_SESSION_FACTORY_PROPERTY = "sessionFactory";

    /** Property Configurer * */
    public static final String DEFAULT_PROPERTY_CONFIGURER_PROPERTY = "propertyConfigurer";

    /** Transaction Manager * */
    public static final String DEFAULT_TRANSACTION_MANAGER_PROPERTY = "transactionManager";

    private String locationListProperty;

    private String locationProperty;

    private String dataSourceProperty;

    private String sessionFactoryProperty;

    private String propertyConfigurerProperty;

    private String transactionManagerProperty;

    /** Spring Application Context * */
    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public SpringContext() {
        locationListProperty = DEFAULT_LOCATION_LIST_PROPERTY;
        locationProperty = DEFAULT_LOCATION_PROPERTY;
        dataSourceProperty = DEFAULT_DATA_SOURCE_PROPERTY;
        sessionFactoryProperty = DEFAULT_SESSION_FACTORY_PROPERTY;
        propertyConfigurerProperty = DEFAULT_PROPERTY_CONFIGURER_PROPERTY;
        transactionManagerProperty = DEFAULT_TRANSACTION_MANAGER_PROPERTY;
    }

    public String getLocationListProperty() {
        return locationListProperty;
    }

    public void setLocationListProperty(String locationListProperty) {
        this.locationListProperty = locationListProperty;
    }

    public String getLocationProperty() {
        return locationProperty;
    }

    public void setLocationProperty(String locationProperty) {
        this.locationProperty = locationProperty;
    }

    public String getDataSourceProperty() {
        return dataSourceProperty;
    }

    public void setDataSourceProperty(String dataSourceProperty) {
        this.dataSourceProperty = dataSourceProperty;
    }

    public String getPropertyConfigurerProperty() {
        return propertyConfigurerProperty;
    }

    public void setPropertyConfigurerProperty(String propertyConfigurerProperty) {
        this.propertyConfigurerProperty = propertyConfigurerProperty;
    }

    public String getSessionFactoryProperty() {
        return sessionFactoryProperty;
    }

    public void setSessionFactoryProperty(String sessionFactoryProperty) {
        this.sessionFactoryProperty = sessionFactoryProperty;
    }

    public String getTransactionManagerProperty() {
        return transactionManagerProperty;
    }

    public void setTransactionManagerProperty(String transactionManagerProperty) {
        this.transactionManagerProperty = transactionManagerProperty;
    }
}

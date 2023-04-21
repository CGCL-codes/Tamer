package org.in4ama.datasourcemanager.hibernate;

import java.util.List;
import org.hibernate.Session;
import org.in4ama.datasourcemanager.AbstractDataSource;
import org.in4ama.datasourcemanager.DataContext;
import org.in4ama.datasourcemanager.DataSet;
import org.in4ama.datasourcemanager.DataSetTemplate.Builder;
import org.in4ama.datasourcemanager.bean.BeanClassLoader;
import org.in4ama.datasourcemanager.cfg.DataSetConfiguration;
import org.in4ama.datasourcemanager.cfg.DataSourceConfiguration;
import org.in4ama.datasourcemanager.cfg.Property;
import org.in4ama.datasourcemanager.exception.DataSourceException;

/** Data source implementation for Hibernate managed POJOs */
public class HibernateDataSource extends AbstractDataSource {

    public static final String TYPE_NAME = "ds.hibernate";

    public static final String CLASSPATH = "classpath";

    public static final String HQL = "HQL query";

    private final BeanClassLoader beanClassLoader = new BeanClassLoader();

    /** Creates a new instance of the HibernateDataSource class. */
    public HibernateDataSource() throws DataSourceException {
    }

    /**
	 * Creates and returns a template configuration of this data source, 
	 * 
	 * @return DataSourceConfiguration object.
	 */
    @Override
    protected DataSourceConfiguration createConfigurationTemplate() {
        return null;
    }

    /**
	 * Creates and returns a template data set configuration consisting of a
	 * "HQL" property.
	 * 
	 * @return DataSetConfigurationTemplate
	 */
    @Override
    protected DataSetConfiguration createDataSetConfigurationTemplate() throws DataSourceException {
        DataSetConfiguration configuration = new DataSetConfiguration(name, "template");
        configuration.addProperty(new Property(HQL));
        return configuration;
    }

    /** Returns the hibernate data set builder object. */
    @Override
    public Builder getDataSetTemplateBuilder() {
        return HibernateDataSetTemplate.getBuilder();
    }

    /**
	 * Creates a new hibernate data set object from the HQL query.
	 */
    public DataSet createDataSet(DataSetConfiguration dataSetConfiguration, DataContext dataContext) throws DataSourceException {
        String dataSetName = dataSetConfiguration.getName();
        String hql = (String) dataSetConfiguration.getProperty(HQL).getValue();
        HibernateDataSet dataSet = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            List<?> bean = session.createQuery(hql).list();
            dataSet = new HibernateDataSet(dataSetName, this, bean, dataContext, session);
        } catch (Exception ex) {
            if (dataSet != null) dataSet.close();
            String msg = "Unable to crate the '" + dataSetName + "' hibernate data set from the query '" + hql + "'.";
            throw new DataSourceException(msg, ex);
        }
        return dataSet;
    }

    /** Makes the required jar files available in the current JVM instance */
    public void initialise() throws DataSourceException {
        String classPath = getClassPath();
        if (classPath != null) {
            try {
                beanClassLoader.addJars(classPath.split(":"));
            } catch (Exception ex) {
                String msg = "Unable to initialize the Hibernate Data Source.";
                throw new DataSourceException(msg, ex);
            }
        }
    }

    /**
	 * Gets the name of this datasource's type.
	 * 
	 * @return the type's name.
	 */
    @Override
    public String getType() {
        return TYPE_NAME;
    }

    public String getClassPath() {
        return (String) properties.get(CLASSPATH);
    }

    public void setClassPath(String classPath) {
        properties.put(CLASSPATH, classPath);
    }
}

package org.ikasan.framework.configuration.dao;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ikasan.framework.configuration.model.Configuration;
import org.ikasan.framework.configuration.model.ConfigurationParameter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Implementation of the ConfigurationDao interface providing
 * the Hibernate persistence for configuration instances.
 * 
 * @author Ikasan Development Team
 */
public class ConfigurationHibernateImpl extends HibernateDaoSupport implements ConfigurationDao {

    public Configuration findById(String configurationId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Configuration.class);
        criteria.add(Restrictions.eq("configurationId", configurationId));
        List<Configuration> configuration = getHibernateTemplate().findByCriteria(criteria);
        if (configuration == null || configuration.size() == 0) {
            return null;
        }
        return configuration.get(0);
    }

    public void save(Configuration configuration) {
        if ("".equals(configuration.getDescription())) {
            configuration.setDescription(null);
        }
        for (ConfigurationParameter configurationParameter : configuration.getConfigurationParameters()) {
            if ("".equals(configurationParameter.getValue())) {
                configurationParameter.setValue(null);
            }
            if ("".equals(configurationParameter.getDescription())) {
                configurationParameter.setDescription(null);
            }
        }
        getHibernateTemplate().saveOrUpdate(configuration);
    }

    public void delete(Configuration configuration) {
        getHibernateTemplate().delete(configuration);
    }
}

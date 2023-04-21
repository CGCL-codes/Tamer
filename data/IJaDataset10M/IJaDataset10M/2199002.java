package com.tredart.yahoo.dataimport.parsers;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author abhi
 * @author george
 *
 */
public class YahooPriceDataDao extends HibernateDaoSupport {

    private static final Logger LOGGER = Logger.getLogger(YahooPriceDataDao.class);

    public void saveOrUpdate(YahooPriceData entity) {
        getHibernateTemplate().saveOrUpdate(entity);
    }

    public YahooPriceData load(final long id) {
        return (YahooPriceData) getHibernateTemplate().get(YahooPriceData.class, id);
    }

    public void saveAll(final List<YahooPriceData> priceDatas) {
        HibernateTemplate template = getHibernateTemplate();
        for (YahooPriceData data : priceDatas) {
            try {
                template.saveOrUpdate(data);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}

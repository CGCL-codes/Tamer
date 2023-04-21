package com.acv.dao.common.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.orm.ObjectRetrievalFailureException;
import com.acv.dao.common.BaseDaoHibernate;
import com.acv.dao.common.ProvinceDao;
import com.acv.dao.common.model.Province;

/**
 * The Class ProvinceDaoHibernate.
 */
@SuppressWarnings("unchecked")
public class ProvinceDaoHibernate extends BaseDaoHibernate implements ProvinceDao {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(ProvinceDaoHibernate.class);

    public List<Province> getProvinces() {
        List<Province> result = new ArrayList<Province>();
        List<Long> ids = getACVHibernateTemplate().find("Select id from Province");
        for (Long id : ids) {
            result.add((Province) getACVHibernateTemplate().get(Province.class, id));
        }
        return result;
    }

    public Province getProvince(Long id) {
        Province province = (Province) getACVHibernateTemplate().get(Province.class, id);
        if (province != null) return province;
        ObjectRetrievalFailureException e = new ObjectRetrievalFailureException(Province.class, id);
        log.warn("Province not found", e);
        throw e;
    }

    public List<Province> getProvinceByCountryId(Long countryId) {
        List<Province> result = new ArrayList<Province>();
        List<Long> ids = getACVHibernateTemplate().find("select id from Province where country.id = ?", countryId);
        for (Long id : ids) {
            result.add((Province) getACVHibernateTemplate().get(Province.class, id));
        }
        return result;
    }

    public void saveProvince(Province province) {
        getACVHibernateTemplate().saveOrUpdate(province);
    }

    public Object getObject(Class clazz, Serializable id) {
        return null;
    }

    public List getObjects(Class clazz) {
        return null;
    }

    public void removeObject(Class clazz, Serializable id) {
    }

    public void saveObject(Object o) {
    }
}

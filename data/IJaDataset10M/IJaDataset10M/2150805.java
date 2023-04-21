package com.orange.erp.dao;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import com.kn.core.dao.BaseDao;
import com.orange.erp.bean.KhuVucBean;

public class KhuVucDao extends BaseDao {

    public List<KhuVucBean> getKhuVucs() {
        DetachedCriteria criteria = DetachedCriteria.forClass(KhuVucBean.class);
        List<KhuVucBean> khuVucs = this.getHibernateTemplate().findByCriteria(criteria);
        return khuVucs;
    }

    public KhuVucBean getKhuVuc(Long id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(KhuVucBean.class);
        criteria.add(Property.forName("id").eq(id));
        List<KhuVucBean> khuVucs = this.getHibernateTemplate().findByCriteria(criteria);
        if (khuVucs != null && khuVucs.size() > 0) {
            return khuVucs.get(0);
        }
        return null;
    }

    public KhuVucBean getKhuVuc(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(KhuVucBean.class);
        criteria.add(Property.forName("code").eq(code));
        List<KhuVucBean> khuVucs = this.getHibernateTemplate().findByCriteria(criteria);
        if (khuVucs != null && khuVucs.size() > 0) {
            return khuVucs.get(0);
        }
        return null;
    }
}

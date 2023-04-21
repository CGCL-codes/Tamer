package net.sf.brightside.mobilestock.service.hibernate.generic;

import java.util.List;
import net.sf.brightside.mobilestock.service.api.generic.IGetByType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class GetByTypeImpl extends HibernateDaoSupport implements IGetByType {

    private Session getManager() {
        return getSessionFactory().getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public <Type> List<Type> getByType(Class<Type> type) {
        if (type != null) {
            Session session = getManager();
            Criteria criteria = session.createCriteria(type);
            List<Type> resultsList = criteria.list();
            return resultsList;
        }
        return null;
    }
}

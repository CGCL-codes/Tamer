package net.sf.brightside.beautyshop.service.login;

import net.sf.brightside.beautyshop.metamodel.beans.CustomerBean;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class LoginImpl extends HibernateDaoSupport implements Login {

    private String name;

    @Override
    @Transactional
    public Object execute() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CustomerBean.class);
        criteria.add(Restrictions.like("name", name));
        return criteria.uniqueResult();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}

package com.jshop.dao.impl;

import java.sql.SQLException;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import com.jshop.dao.RoleFunctionMDao;
import com.jshop.dao.RoleMDao;
import com.jshop.entity.BaseHibernateDAO;
import com.jshop.entity.RoleFunctionM;
import com.jshop.entity.RoleM;
import com.jshop.entity.TableT;

/**
 * A data access object (DAO) providing persistence and search support for
 * RoleFunctionM entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jshop.entity.RoleFunctionM
 * @author MyEclipse Persistence Tools
 */
@Repository("roleFunctionMDao")
public class RoleFunctionMDaoImpl extends HibernateDaoSupport implements RoleFunctionMDao {

    private static final Logger log = LoggerFactory.getLogger(RoleFunctionMDaoImpl.class);

    public void addRoleFunctionM(RoleFunctionM rfm) {
        log.debug("save RoleFunctionM");
        try {
            this.getHibernateTemplate().save(rfm);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public List<RoleFunctionM> findAllRoleFunctionMByroleid(String roleid) {
        log.debug("findAllRoleFunctionMByroleid");
        try {
            String queryString = "from RoleFunctionM as rfm where rfm.roleid=:roleid";
            List<RoleFunctionM> list = this.getHibernateTemplate().findByNamedParam(queryString, "roleid", roleid);
            return list;
        } catch (RuntimeException re) {
            log.error("findAllRoleFunctionMByroleid error", re);
            throw re;
        }
    }

    public int delRoleFunctionM(final String roleid) {
        log.debug("delRoleFunctionM");
        try {
            final String queryString = "delete from RoleFunctionM as rfm where rfm.roleid=:roleid";
            this.getHibernateTemplate().execute(new HibernateCallback() {

                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    Query query = session.createQuery(queryString);
                    int i = 0;
                    query.setParameter("roleid", roleid);
                    i = query.executeUpdate();
                    return i;
                }
            });
        } catch (RuntimeException re) {
            log.error("delFunctionM failed", re);
            throw re;
        }
        return 0;
    }
}

package com.ncs.flex.server.dao;

import com.ncs.flex.to.RoleTO;
import com.ncs.flex.to.SubjectTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository("subjectDAO")
public class SubjectDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(SubjectDAO.class);

    public static final String SUBJECT_ID = "subjectId";

    public static final String PROJ_ID = "projId";

    public static final String PASSWORD = "password";

    public static final String USERNAME = "username";

    public static final String ENABLED = "enabled";

    public static final String LOGIN_NAME = "loginName";

    public static final String LOGIN_MECHANISM = "loginMechanism";

    public static final String RECALL_QUESTION = "recallQuestion";

    public static final String RECALL_ANS = "recallAns";

    public static final String ATTEMPTS_MADE = "attemptsMade";

    public static final String CREATED_BY = "createdBy";

    public static final String UPDATED_BY = "updatedBy";

    public static final String DOMAIN_NAME = "domainName";

    protected void initDao() {
    }

    public void save(SubjectTO transientInstance) {
        log.debug("saving SubjectTO instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void update(SubjectTO subjectTO) {
        log.debug("updating SubjectTO instance");
        try {
            getHibernateTemplate().update(subjectTO);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }

    public void delete(SubjectTO persistentInstance) {
        log.debug("deleting SubjectTO instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public SubjectTO findById(java.lang.Integer id) {
        log.debug("getting SubjectTO instance with id: " + id);
        try {
            SubjectTO instance = (SubjectTO) getHibernateTemplate().get("SubjectTO", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<SubjectTO> findByExample(SubjectTO instance) {
        log.debug("finding SubjectTO instance by example");
        try {
            List<SubjectTO> results = (List<SubjectTO>) getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List<RoleTO> queryRoleByProjId(String projectId) {
        String hql = "from RoleTO as r where r.projectId =?";
        return getHibernateTemplate().find(hql, projectId);
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding SubjectTO instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from SubjectTO as model where model." + propertyName + "= ?";
            log.info("========list .size():" + getHibernateTemplate().find(queryString, value).size());
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public SubjectTO findBySubjectId(Object subjectId) {
        return (SubjectTO) findByProperty(SUBJECT_ID, subjectId).get(0);
    }

    public SubjectTO findByUsername(Object username) {
        return (SubjectTO) findByProperty(USERNAME, username).get(0);
    }

    public List checkHasUsername(Object username) {
        return findByProperty(USERNAME, username);
    }

    public List<SubjectTO> findByProjId(Object projId) {
        return findByProperty(PROJ_ID, projId);
    }

    public List<SubjectTO> findByPassword(Object password) {
        return findByProperty(PASSWORD, password);
    }

    public List<SubjectTO> findByEnabled(Object enabled) {
        return findByProperty(ENABLED, enabled);
    }

    public List<SubjectTO> findByLoginName(Object loginName) {
        return findByProperty(LOGIN_NAME, loginName);
    }

    public List<SubjectTO> findByLoginMechanism(Object loginMechanism) {
        return findByProperty(LOGIN_MECHANISM, loginMechanism);
    }

    public List<SubjectTO> findByRecallQuestion(Object recallQuestion) {
        return findByProperty(RECALL_QUESTION, recallQuestion);
    }

    public List<SubjectTO> findByRecallAns(Object recallAns) {
        return findByProperty(RECALL_ANS, recallAns);
    }

    public List<SubjectTO> findByAttemptsMade(Object attemptsMade) {
        return findByProperty(ATTEMPTS_MADE, attemptsMade);
    }

    public List<SubjectTO> findByCreatedBy(Object createdBy) {
        return findByProperty(CREATED_BY, createdBy);
    }

    public List<SubjectTO> findByUpdatedBy(Object updatedBy) {
        return findByProperty(UPDATED_BY, updatedBy);
    }

    public List<SubjectTO> findByDomainName(Object domainName) {
        return findByProperty(DOMAIN_NAME, domainName);
    }

    public List findAll() {
        log.debug("finding all SubjectTO instances");
        try {
            String queryString = "from SubjectTO";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public SubjectTO merge(SubjectTO detachedInstance) {
        log.debug("merging SubjectTO instance");
        try {
            SubjectTO result = (SubjectTO) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(SubjectTO instance) {
        log.debug("attaching dirty SubjectTO instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(SubjectTO instance) {
        log.debug("attaching clean SubjectTO instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static SubjectDAO getFromApplicationContext(ApplicationContext ctx) {
        return (SubjectDAO) ctx.getBean("SubjectTODAO");
    }
}

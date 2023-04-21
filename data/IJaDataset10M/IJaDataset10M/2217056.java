package edu.purdue.rcac.cesm.persistence.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.Management;
import org.jboss.ejb3.annotation.Service;
import edu.purdue.rcac.cesm.persistence.model.CESMCaseConfig;
import edu.purdue.rcac.cesm.persistence.model.CESMCaseRuntime;

/**
 * This is service bean implementation of interface CaseManager. Depends on
 * persistence unit "ccsm4ws-pu".
 * 
 * @author Han Zhang
 * 
 */
@Management(CaseManager.class)
@Service(objectName = CaseManagerMBean.OBJECT_NAME)
public class CaseManagerMBean implements CaseManager {

    private static final Logger log = Logger.getLogger(CaseManager.class.getName());

    @PersistenceContext(unitName = "cesm-pu")
    private static EntityManager em;

    public void create() throws Exception {
        log.debug("create()");
    }

    public void destroy() {
        log.debug("destroy()");
    }

    public void start() throws Exception {
        log.info("Service started.");
    }

    public void stop() {
        log.info("Service stopped.");
    }

    public boolean exists(String owner, String casename) {
        CESMCaseConfig caseConfig = this.findByOwnerAndName(owner, casename);
        if (caseConfig != null) log.info("EXIST: " + caseConfig.toShort()); else log.info(String.format("NOT EXIST: name=%s, owner=%s", casename, owner));
        return caseConfig != null;
    }

    public void save(CESMCaseConfig caseConfig) {
        em.persist(caseConfig);
        log.info("SAVED: " + caseConfig);
    }

    public void save(CESMCaseRuntime caseRuntime) {
        em.persist(caseRuntime);
        log.info("SAVED: " + caseRuntime);
    }

    public void merge(CESMCaseConfig caseConfig) {
        em.merge(caseConfig);
        log.info("MERGED: " + caseConfig);
    }

    public void merge(CESMCaseRuntime caseRuntime) {
        if (!em.contains(caseRuntime)) {
            em.merge(caseRuntime);
            log.info("MERGED: " + caseRuntime);
        }
    }

    public void remove(CESMCaseConfig caseConfig) {
        em.remove(caseConfig);
        log.info("DELETED: " + caseConfig);
    }

    public void remove(CESMCaseRuntime caseRuntime) {
        em.remove(caseRuntime);
        log.info("DELETED: " + caseRuntime);
    }

    public void refresh(CESMCaseConfig caseConfig) {
        em.refresh(caseConfig);
        log.info("UPDATED: " + caseConfig);
    }

    public CESMCaseConfig findById(Long id) {
        log.debug("findCaseById(id)");
        Query q = em.createNamedQuery(CESMCaseConfig.FIND_BY_ID).setParameter("id", id);
        try {
            return (CESMCaseConfig) q.getSingleResult();
        } catch (Exception e) {
            log.warn(String.format("findCaseById(%d) returns null", id));
            return null;
        }
    }

    public CESMCaseConfig findByOwnerAndName(String owner, String casename) {
        log.debug("findCaseByOwnerAndName(owner, casename)");
        Query q = em.createNamedQuery(CESMCaseConfig.FIND_BY_OWNER_N_NAME).setParameter("owner", owner).setParameter("name", casename);
        try {
            return (CESMCaseConfig) q.getSingleResult();
        } catch (NoResultException e) {
            log.warn(String.format("findCaseByOwnerAndName(\"%s\", \"%s\") returns null", owner, casename));
            return null;
        }
    }

    public List<CESMCaseConfig> findByOwner(String owner) {
        log.debug("findCaseNamesByOwner(owner)");
        Query q = em.createNamedQuery(CESMCaseConfig.FIND_BY_OWNER).setParameter("owner", owner);
        try {
            return (List<CESMCaseConfig>) q.getResultList();
        } catch (Exception e) {
            log.warn(String.format("findCaseNamesByOwner(\"%s\") returns null", owner));
            return null;
        }
    }

    public CESMCaseRuntime findByPbsid(String pbsid) {
        log.debug("findCaseByPbsid(pbsid)");
        Query q = em.createNamedQuery(CESMCaseRuntime.FIND_BY_PBSID).setParameter("pbsid", pbsid);
        try {
            return (CESMCaseRuntime) q.getSingleResult();
        } catch (NoResultException e) {
            log.warn(String.format("findCaseByPbsid(\"%s\") returns null", pbsid));
            return null;
        }
    }

    public void deleteCaseByOwnerAndName(String owner, String name) {
        CESMCaseConfig caseConfig = this.findByOwnerAndName(owner, name);
        if (caseConfig != null) {
            em.remove(caseConfig);
        }
    }

    public List<CESMCaseRuntime> listAllUnfinishedRuntimes() {
        Query q = em.createNamedQuery(CESMCaseRuntime.FIND_ALL_UNFINISHED);
        try {
            return (List<CESMCaseRuntime>) q.getResultList();
        } catch (Exception e) {
            log.warn("listAllUnfinishedRuntimes() returns null");
            return null;
        }
    }

    public List<CESMCaseRuntime> findByCaseConfigId(long cid) {
        Query q = em.createNamedQuery(CESMCaseRuntime.FIND_BY_CASE_CONFIG_ID).setParameter("cid", cid);
        try {
            return (List<CESMCaseRuntime>) q.getResultList();
        } catch (Exception e) {
            log.warn("findByCaseConfigId() returns null");
            return null;
        }
    }

    public List<CESMCaseConfig> listAllRunningCases() {
        Query q = em.createNamedQuery(CESMCaseConfig.FIND_ALL_RUNNING);
        try {
            return (List<CESMCaseConfig>) q.getResultList();
        } catch (Exception e) {
            log.warn("listAllRunningCases() returns null");
            return null;
        }
    }
}

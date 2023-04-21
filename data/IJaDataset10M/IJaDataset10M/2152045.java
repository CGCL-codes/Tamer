package org.ivoa.util.runner;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.ivoa.bean.SingletonSupport;
import org.ivoa.jpa.JPAFactory;

/**
 * Job Persistence
 * @author laurent bourges (voparis)
 */
public final class JobJPAManager extends SingletonSupport {

    /** Job PU */
    private static final String JOB_PU = "JobDB-PU";

    /** singleton instance (java 5 memory model) */
    private static volatile JobJPAManager instance = null;

    /** JPAFactory instance */
    private JPAFactory jf = null;

    /**
   * Forbidden constructor
   */
    private JobJPAManager() {
    }

    /**
   * Return the ThreadLocalUtils singleton instance
   *
   * @return ThreadLocalUtils singleton instance
   * @throws IllegalStateException if a problem occured
   */
    public static final JobJPAManager getInstance() {
        if (instance == null) {
            instance = prepareInstance(new JobJPAManager());
        }
        return instance;
    }

    /**
   * Concrete implementations of the SingletonSupport's initialize() method :<br/>
   * Callback to initialize this SingletonSupport instance
   *
   * @see SingletonSupport#initialize()
   *
   * @throws IllegalStateException if a problem occured
   */
    @Override
    protected void initialize() throws IllegalStateException {
        if (logB.isInfoEnabled()) {
            logB.info("JobJPAManager.init ...");
        }
        this.jf = getJPAFactory();
    }

    /**
   * TODO : Method Description
   *
   * @return value TODO : Value Description
   */
    private JPAFactory getJPAFactory() {
        if (log.isInfoEnabled()) {
            log.info("JobJPAManager : get JPAFactory : " + JOB_PU + " ...");
        }
        final JPAFactory f = JPAFactory.getInstance(JOB_PU);
        if (log.isInfoEnabled()) {
            log.info("JobJPAManager : get JPAFactory : " + JOB_PU + " : OK");
        }
        return f;
    }

    /**
   * Concrete implementations of the SingletonSupport's clearStaticReferences() method :<br/>
   * Callback to clean up the possible static references used by this SingletonSupport instance
   * iso clear static references
   *
   * @see SingletonSupport#clearStaticReferences()
   */
    @Override
    protected void clearStaticReferences() {
        if (instance != null) {
            instance = null;
        }
    }

    /**
   * Concrete implementations of the SingletonSupport's clear() method :<br/>
   * Callback to clean up this SingletonSupport instance iso clear instance fields
   *
   * @see SingletonSupport#clear()
   */
    @Override
    protected void clear() {
    }

    private EntityManager getEntityManager() {
        return jf.getEm();
    }

    /**
   * Persist (aka flush) the run context (and their children) to the database.<br/>
   *
   * @param ctx run context to store in the database
   */
    public void persist(final RunContext ctx) {
        EntityManager em = null;
        RunContext updCtx = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            if (log.isInfoEnabled()) {
                log.info("JobJPAManager.persist : " + ctx.shortString());
            }
            if (ctx.getId() == null || ctx.getId().longValue() < 0l) {
                em.persist(ctx);
            } else {
                updCtx = em.merge(ctx);
            }
            if (log.isInfoEnabled()) {
                log.info("JobJPAManager.persist : committing TX : " + ctx.shortString());
            }
            em.getTransaction().commit();
            if (log.isInfoEnabled()) {
                log.info("JobJPAManager.persist : TX commited : " + ctx.shortString());
            }
        } catch (final RuntimeException re) {
            log.error("JobJPAManager.persist : runtime failure : ", re);
            if (em != null && em.getTransaction().isActive()) {
                log.warn("JobJPAManager.persist : rollbacking TX : " + ctx.shortString());
                em.getTransaction().rollback();
                log.warn("JobJPAManager.persist : TX rollbacked : " + ctx.shortString());
            }
            throw re;
        } finally {
            close(em);
        }
        if (updCtx != null) {
            ctx.setJpaVersion(updCtx.getJpaVersion());
            if (ctx instanceof RootContext) {
                final RootContext rctx = (RootContext) ctx;
                final RootContext rupdCtx = (RootContext) updCtx;
                final List<RunContext> listCtx = rctx.getChildContexts();
                final List<RunContext> listUpdCtx = rupdCtx.getChildContexts();
                RunContext c1, c2;
                for (int i = 0, size = listCtx.size(); i < size; i++) {
                    c1 = listCtx.get(i);
                    c2 = listUpdCtx.get(i);
                    if (c1.getId() == null || c1.getId().longValue() < 0l) {
                        c1.setId(c2.getId());
                    }
                    c1.setJpaVersion(c2.getJpaVersion());
                }
            }
        }
    }

    /**
   * Retrieve an existing run context in the database (or in cache)
   * @param id job id
   * @return run context
   */
    public RunContext get(final Long id) {
        return refresh(id, false);
    }

    /**
   * Retrieve a run context from the database
   * @param id job id
   * @param forceRefresh true indicates to load data surely from the database
   * @return run context
   */
    public RunContext refresh(final Long id, final boolean forceRefresh) {
        if (log.isInfoEnabled()) {
            log.info("JobJPAManager.refresh : enter : " + id);
        }
        RunContext ctx = null;
        EntityManager em = null;
        try {
            em = jf.getEm();
            final Query q = em.createNamedQuery("RunContext.findById").setParameter("id", id);
            if (forceRefresh) {
                q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            }
            ctx = (RunContext) q.getSingleResult();
        } catch (final RuntimeException re) {
            log.error("JobJPAManager.refresh : runtime failure : ", re);
            throw re;
        } finally {
            close(em);
        }
        if (log.isInfoEnabled()) {
            log.info("JobJPAManager.refresh : exit : " + ctx.shortString());
        }
        return ctx;
    }

    /**
   * Retrieve the run contexts for a given application name from the database
   * @param name application name
   * @param forceRefresh true indicates to load data surely from the database
   * @return list of run context
   */
    public List<RootContext> findContexts(final String name, final boolean forceRefresh) {
        if (log.isInfoEnabled()) {
            log.info("JobJPAManager.findContexts : enter : " + name);
        }
        final List<RootContext> ctxInterrupted = findContexts(name, RunState.STATE_INTERRUPTED, forceRefresh);
        final List<RootContext> ctxPending = findContexts(name, RunState.STATE_PENDING, forceRefresh);
        final List<RootContext> results = new ArrayList<RootContext>(ctxInterrupted);
        results.addAll(ctxPending);
        if (log.isInfoEnabled()) {
            log.info("JobJPAManager.findContexts : exit : " + results);
        }
        return results;
    }

    /**
   * Retrieve the run contexts for a given application name from the database
   * @param name application name
   * @param state matching state
   * @param forceRefresh true indicates to load data surely from the database
   * @return list of run context
   */
    @SuppressWarnings("unchecked")
    private List<RootContext> findContexts(final String name, final RunState state, boolean forceRefresh) {
        List<RootContext> ctxList = null;
        EntityManager em = null;
        try {
            em = jf.getEm();
            final Query q = em.createNamedQuery("RootContext.findPendingByName").setParameter("state", state).setParameter("name", name);
            if (forceRefresh) {
                q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            }
            ctxList = (List<RootContext>) q.getResultList();
        } catch (final RuntimeException re) {
            log.error("JobJPAManager.findContexts : runtime failure : ", re);
            throw re;
        } finally {
            close(em);
        }
        return ctxList;
    }

    /**
   * Retrieve the run contexts for a given application name from the database
   * @param name application name
   * @param state matching state
   * @param forceRefresh true indicates to load data surely from the database
   * @return list of run context
   */
    @SuppressWarnings("unchecked")
    public List<RootContext> findContextsForOwner(final String owner, boolean forceRefresh) {
        List<RootContext> ctxList = null;
        EntityManager em = null;
        try {
            em = jf.getEm();
            final Query q = em.createNamedQuery("RootContext.findAllByOwner").setParameter("owner", owner);
            if (forceRefresh) {
                q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            }
            ctxList = (List<RootContext>) q.getResultList();
        } catch (final RuntimeException re) {
            log.error("JobJPAManager.findContextsForOwner : runtime failure : ", re);
            throw re;
        } finally {
            close(em);
        }
        return ctxList;
    }

    /**
   * Close the given entity manager
   *
   * @param em entity manager
   */
    public final void close(final EntityManager em) {
        if (em != null) {
            em.close();
        }
    }
}

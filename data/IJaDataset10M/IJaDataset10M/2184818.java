package edu.ucdavis.genomics.metabolomics.binbase.logging.server;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import edu.ucdavis.genomics.metabolomics.binbase.filter.IPRequest;

/**
 * actual implementations
 * 
 * @author wohlgemuth
 */
@Stateless
public class LoggingServiceBean implements LoggingService {

    @PersistenceContext
    EntityManager manager;

    private Logger logger = Logger.getLogger(getClass());

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public List<String> getAddresses() {
        Query query = manager.createQuery("select b.hostAddress from " + LoggingEntityBean.class.getName() + " b group by b.hostAddress");
        List<String> beans = query.getResultList();
        return beans;
    }

    @SuppressWarnings("unchecked")
    public List<String> getAddresses(Date date) {
        Query query = manager.createQuery("select b.hostAddress from " + LoggingEntityBean.class.getName() + " b where b.time = ?1  group by b.hostAddress");
        query.setParameter(1, date);
        List<String> beans = query.getResultList();
        return beans;
    }

    @SuppressWarnings("unchecked")
    public List<String> getAddresses(Date begin, Date end) {
        logger.info("searching for address by range: " + begin + " - " + end);
        Query query = manager.createQuery("select b.hostAddress from " + LoggingEntityBean.class.getName() + " b where b.time between ?1 and ?2  group by b.hostAddress");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        List<String> beans = query.getResultList();
        return beans;
    }

    @SuppressWarnings("unchecked")
    public List<Date> getDates(String hostname) {
        Query query = manager.createQuery("select b.time from " + LoggingEntityBean.class.getName() + " b where hostName = ?1  group by b.time");
        query.setParameter(1, hostname);
        List<Date> beans = query.getResultList();
        return beans;
    }

    @SuppressWarnings("unchecked")
    public List<String> getHostnames() {
        Query query = manager.createQuery("select b.hostName from " + LoggingEntityBean.class.getName() + " b group by b.hostName");
        List<String> beans = query.getResultList();
        return beans;
    }

    @SuppressWarnings("unchecked")
    public List<String> getHostnames(Date date) {
        Query query = manager.createQuery("select b.hostName from " + LoggingEntityBean.class.getName() + " b where b.time= ?1 group by b.hostName");
        query.setParameter(1, date);
        List<String> beans = query.getResultList();
        return beans;
    }

    @SuppressWarnings("unchecked")
    public List<String> getHostnames(Date begin, Date end) {
        Query query = manager.createQuery("select b.hostName from " + LoggingEntityBean.class.getName() + " b where b.time between ?1 and ?2 group by b.hostName");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        List<String> beans = query.getResultList();
        return beans;
    }

    @SuppressWarnings("unchecked")
    public List<IPRequest> getRequest(String hostname) {
        Query query = manager.createQuery("from " + LoggingEntityBean.class.getName() + " b where b.hostName = ?1");
        query.setParameter(1, hostname);
        List<LoggingEntityBean> beans = query.getResultList();
        return toRequest(beans);
    }

    @SuppressWarnings("unchecked")
    public List<IPRequest> getRequest(String hostname, Date date) {
        Query query = manager.createQuery("from " + LoggingEntityBean.class.getName() + " b where b.time = ?1 and b.hostName = ?2");
        query.setParameter(1, date);
        query.setParameter(2, hostname);
        List<LoggingEntityBean> beans = query.getResultList();
        return toRequest(beans);
    }

    @SuppressWarnings("unchecked")
    public List<IPRequest> getRequest(String hostname, Date begin, Date end) {
        Query query = manager.createQuery("from " + LoggingEntityBean.class.getName() + " b where b.time between ?1 and ?2 and b.hostName = ?3");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        query.setParameter(3, hostname);
        List<LoggingEntityBean> beans = query.getResultList();
        return toRequest(beans);
    }

    @SuppressWarnings("unchecked")
    public List<IPRequest> getRequest(Date date) {
        Query query = manager.createQuery("from " + LoggingEntityBean.class.getName() + " b where b.time = ?1");
        query.setParameter(1, date);
        List<LoggingEntityBean> beans = query.getResultList();
        return toRequest(beans);
    }

    @SuppressWarnings("unchecked")
    public List<IPRequest> getRequest(Date begin, Date end) {
        Query query = manager.createQuery("from " + LoggingEntityBean.class.getName() + " b where b.time between ?1 and ?2");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        List<LoggingEntityBean> beans = query.getResultList();
        return toRequest(beans);
    }

    /**
	 * converts our beans to requests
	 * 
	 * @param bean
	 * @return
	 */
    protected List<IPRequest> toRequest(List<LoggingEntityBean> bean) {
        List<IPRequest> result = new Vector<IPRequest>(bean.size());
        for (LoggingEntityBean b : bean) {
            result.add(b.toRequest());
        }
        return result;
    }

    public void registerEvent(IPRequest request) {
        logger.debug("storing: " + request);
        EntityTransaction trans = manager.getTransaction();
        trans.begin();
        manager.persist(new LoggingEntityBean(request));
        manager.flush();
        trans.commit();
    }

    @SuppressWarnings("unchecked")
    public void removeEvent(Date begin, Date end) {
        EntityTransaction trans = manager.getTransaction();
        trans.begin();
        Query query = manager.createQuery("from " + LoggingEntityBean.class.getName() + " b where b.time between ?1 and ?2");
        query.setParameter(1, begin);
        query.setParameter(2, end);
        List<LoggingEntityBean> beans = query.getResultList();
        for (LoggingEntityBean b : beans) {
            manager.remove(b);
        }
        manager.flush();
        trans.commit();
    }
}

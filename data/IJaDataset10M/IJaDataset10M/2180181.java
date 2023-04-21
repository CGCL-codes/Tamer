package org.signserver.ejb;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import org.signserver.server.clusterclassloader.BaseClusterClassLoaderDataService;
import org.signserver.server.clusterclassloader.IClusterClassLoaderDataBean;

/**
 * Contains help method to make queries to the
 * resource repository in database. 
 */
public class ClusterClassLoaderDataService extends BaseClusterClassLoaderDataService {

    public transient Logger log = Logger.getLogger(this.getClass());

    public ClusterClassLoaderDataService(EntityManager em, String moduleName) {
        super(em, moduleName);
    }

    public ClusterClassLoaderDataService(EntityManager em, String moduleName, int version) {
        super(em, moduleName, version);
    }

    public ClusterClassLoaderDataService(EntityManager em, String moduleName, String part, int version) {
        super(em, moduleName, part, version);
    }

    public IClusterClassLoaderDataBean findByResourceName(String resourceName) {
        try {
            return (IClusterClassLoaderDataBean) em.createNamedQuery("ClusterClassLoaderDataBean.findByResourceName").setParameter(1, resourceName).setParameter(2, moduleName).setParameter(3, part).setParameter(4, version).getSingleResult();
        } catch (javax.persistence.NoResultException e) {
        }
        return null;
    }

    public int findLatestVersionOfResource(String resourceName) {
        try {
            return (Integer) em.createNamedQuery("ClusterClassLoaderDataBean.findLatestVersionOfResource").setParameter(1, resourceName).getSingleResult();
        } catch (javax.persistence.NoResultException e) {
        }
        return 0;
    }

    public int findLatestVersionOfModule(String moduleName) {
        try {
            return (Integer) em.createNamedQuery("ClusterClassLoaderDataBean.findLatestVersionOfModule").setParameter(1, moduleName).getSingleResult();
        } catch (javax.persistence.NoResultException e) {
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection<IClusterClassLoaderDataBean> findResources() {
        try {
            return em.createNamedQuery("ClusterClassLoaderDataBean.findResources").setParameter(1, moduleName).setParameter(2, part).setParameter(3, version).getResultList();
        } catch (javax.persistence.NoResultException e) {
        }
        return new ArrayList<IClusterClassLoaderDataBean>();
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection<IClusterClassLoaderDataBean> findAllResourcesInModule() {
        try {
            return em.createNamedQuery("ClusterClassLoaderDataBean.findAllResourcesInModule").setParameter(1, moduleName).setParameter(2, version).getResultList();
        } catch (javax.persistence.NoResultException e) {
        }
        return new ArrayList<IClusterClassLoaderDataBean>();
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection<IClusterClassLoaderDataBean> findImplementorsInModule(String interfaceName) {
        try {
            return em.createNamedQuery("ClusterClassLoaderDataBean.findImplementorsInModule").setParameter(1, interfaceName).setParameter(2, moduleName).setParameter(3, part).setParameter(4, version).getResultList();
        } catch (javax.persistence.NoResultException e) {
        }
        return new ArrayList<IClusterClassLoaderDataBean>();
    }
}

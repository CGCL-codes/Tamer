package org.riverock.webmill.portal.dao;

import java.util.List;
import org.riverock.interfaces.portal.dao.PortalVirtualHostDao;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 14:16:08
 */
public class PortalVirtualHostDaoImpl implements PortalVirtualHostDao {

    private AuthSession authSession = null;

    private ClassLoader classLoader = null;

    PortalVirtualHostDaoImpl(AuthSession authSession, ClassLoader classLoader) {
        this.authSession = authSession;
        this.classLoader = classLoader;
    }

    public List<VirtualHost> getVirtualHostsFullList() {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalVirtualHostDao().getVirtualHostsFullList();
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public List<VirtualHost> getVirtualHosts(Long siteId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalVirtualHostDao().getVirtualHosts(siteId);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public Long createVirtualHost(VirtualHost virtualHost) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalVirtualHostDao().createVirtualHost(virtualHost);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }
}

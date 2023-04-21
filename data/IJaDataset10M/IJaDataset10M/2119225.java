package net.jetrix.monitor.dao;

import java.util.List;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import net.jetrix.monitor.ServerInfo;

/**
 * @author Emmanuel Bourg
 * @version $Revision: 828 $, $Date: 2010-02-23 19:36:21 -0500 (Tue, 23 Feb 2010) $
 */
public class ServerInfoDao extends HibernateDaoSupport {

    public ServerInfo getServer(long id) {
        return (ServerInfo) getSession().get(ServerInfo.class, id);
    }

    public ServerInfo getServer(String hostname) {
        Query query = getSession().createQuery("FROM ServerInfo WHERE hostname = :hostname");
        query.setParameter("hostname", hostname);
        return (ServerInfo) query.uniqueResult();
    }

    public List<ServerInfo> getServers() {
        return getSession().createQuery("FROM ServerInfo ORDER BY stats.playerCount DESC, hostname").list();
    }

    /**
     * Returns the latest servers added.
     */
    public List<ServerInfo> getLatestServers(int n) {
        Query query = getSession().createQuery("FROM ServerInfo WHERE lastOnline != null ORDER BY dateAdded DESC");
        query.setMaxResults(n);
        return query.list();
    }

    public void save(ServerInfo server) {
        getSession().saveOrUpdate(server);
    }

    public void remove(long id) {
        Object server = getSession().load(ServerInfo.class, id);
        getSession().delete(server);
    }

    /**
     * Check if the hostname or IP address of the specified server already exist in the database.
     *
     * @param server
     */
    public boolean exists(ServerInfo server) {
        Query query = getSession().createQuery("FROM ServerInfo WHERE IP = :ip OR hostname = :name OR :ip IN elements(aliases)");
        query.setParameter("ip", server.getIP());
        query.setParameter("name", server.getHostname());
        List result = query.list();
        return !result.isEmpty();
    }
}

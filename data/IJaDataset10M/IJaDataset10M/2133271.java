package jk.spider.core.storage.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jk.spider.core.storage.ProxyDAO;
import jk.spider.model.ProxyInfo;
import org.apache.log4j.Logger;

public class ProxyDAOImpl implements ProxyDAO {

    protected static final Logger log = Logger.getLogger(ProxyDAOImpl.class);

    protected static final Set<String> pxSet = new HashSet<String>();

    protected DAOHelp daoHelp;

    public ProxyDAOImpl(DAOHelp daoHelp) {
        this.daoHelp = daoHelp;
    }

    public boolean create(ProxyInfo proxy) {
        String sql = "insert into spider_proxy (pxIp, pxPort, pxMark) values (" + daoHelp.format(proxy.getPxIp()) + ", " + daoHelp.format(proxy.getPxPort()) + ", " + daoHelp.format(proxy.getPxMark()) + ")";
        if (!isHavingProxy(proxy.getPxIp())) {
            if (daoHelp.execSql(sql) > 0) return true;
        }
        return false;
    }

    public List<ProxyInfo> getProxy() {
        List<ProxyInfo> pList = new ArrayList<ProxyInfo>();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select pxId, pxIp, pxPort, pxMark from spider_proxy where pxMark = 1 order by pxId";
        try {
            stmt = daoHelp.getConnection().createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ProxyInfo proxy = createProxyInfo(rs);
                setMark(proxy.getPxId(), 2);
                pList.add(proxy);
            }
            daoHelp.safeClose(rs);
            daoHelp.safeClose(stmt);
        } catch (SQLException e) {
            log.error("SQLException -> ", e);
            daoHelp.safeClose(rs);
            daoHelp.safeClose(stmt);
        } finally {
            daoHelp.safeClose(rs);
            daoHelp.safeClose(stmt);
        }
        return pList;
    }

    public synchronized List<ProxyInfo> getValidateProxy() {
        List<ProxyInfo> pList = new ArrayList<ProxyInfo>();
        ResultSet rs = null;
        Statement stmt = null;
        String sql = "select top 100 pxId, pxIp, pxPort, pxMark from spider_proxy where pxMark = 0 order by pxId";
        try {
            stmt = daoHelp.getConnection().createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ProxyInfo proxy = createProxyInfo(rs);
                setMark(proxy.getPxId(), 3);
                pList.add(proxy);
            }
            daoHelp.safeClose(rs);
            daoHelp.safeClose(stmt);
        } catch (SQLException e) {
            log.error("SQLException -> ", e);
            daoHelp.safeClose(rs);
            daoHelp.safeClose(stmt);
        } finally {
            daoHelp.safeClose(rs);
            daoHelp.safeClose(stmt);
        }
        return pList;
    }

    public void setMark(int pxId, int pxMark) {
        String sql = "update spider_proxy set pxMark = " + daoHelp.format(pxMark) + " where pxId = " + pxId;
        daoHelp.execSql(sql);
    }

    protected ProxyInfo createProxyInfo(ResultSet rs) throws SQLException {
        ProxyInfo proxy = new ProxyInfo();
        proxy.setPxId(rs.getInt(1));
        proxy.setPxIp(rs.getString(2));
        proxy.setPxPort(rs.getInt(3));
        proxy.setPxMark(rs.getInt(4));
        return proxy;
    }

    protected synchronized boolean isHavingProxy(String pxIp) {
        if (pxSet.contains(pxIp)) return true;
        pxSet.add(pxIp);
        return false;
    }
}

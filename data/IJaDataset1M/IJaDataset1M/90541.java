package org.ourgrid.discoveryservice;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ourgrid.common.statistics.beans.ds.DS_PeerStatusChange;
import org.ourgrid.common.statistics.beans.status.PeerStatus;
import org.ourgrid.common.statistics.util.hibernate.HibernateUtil;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;

/**
 * @author alan
 *
 */
public class PeerStatusChangeUtil {

    public static void peerIsUp(DeploymentID peerID) {
        registerPeerStatusChange(peerID, PeerStatus.UP, System.currentTimeMillis());
    }

    public static void peerIsDown(DeploymentID peerID) {
        registerPeerStatusChange(peerID, PeerStatus.DOWN, System.currentTimeMillis());
    }

    @SuppressWarnings("unchecked")
    public static List<DS_PeerStatusChange> getPeerStatusChangesHistory(long since) {
        HibernateUtil.beginTransaction();
        List<DS_PeerStatusChange> allPeerStatusChange = null;
        try {
            allPeerStatusChange = new ArrayList<DS_PeerStatusChange>();
            Criteria criteria = HibernateUtil.getSession().createCriteria(DS_PeerStatusChange.class).add(Restrictions.ge("timeOfChange", since)).addOrder(Order.asc("timeOfChange"));
            allPeerStatusChange.addAll(criteria.list());
            HibernateUtil.commitTransaction();
        } catch (HibernateException e) {
            HibernateUtil.rollbackAndCloseSession();
        }
        return allPeerStatusChange;
    }

    public static void registerPeerStatusChange(DeploymentID peerID, PeerStatus status, long timeOfChange) {
        Session session = HibernateUtil.getSession();
        HibernateUtil.beginTransaction();
        try {
            DS_PeerStatusChange change = new DS_PeerStatusChange();
            change.setCurrentStatus(status);
            change.setLastModified(timeOfChange);
            change.setPeerAddress(peerID.getUserName() + "@" + peerID.getServerName());
            change.setTimeOfChange(timeOfChange);
            session.saveOrUpdate(change);
            HibernateUtil.commitTransaction();
        } catch (HibernateException e) {
            HibernateUtil.rollbackAndCloseSession();
        }
    }

    public static void killAllActivePeers(long timeOfChange) {
        Session session = HibernateUtil.getSession();
        HibernateUtil.beginTransaction();
        try {
            List<DS_PeerStatusChange> changes = session.getNamedQuery("getLastStatusChangesToUp").list();
            for (DS_PeerStatusChange peerStatusChange : changes) {
                DS_PeerStatusChange change = new DS_PeerStatusChange();
                change.setCurrentStatus(PeerStatus.DOWN);
                change.setLastModified(timeOfChange);
                change.setPeerAddress(peerStatusChange.getPeerAddress());
                change.setTimeOfChange(timeOfChange);
                session.save(change);
            }
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            HibernateUtil.rollbackAndCloseSession();
        }
    }
}

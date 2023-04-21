package org.ourgrid.common.statistics.control;

import java.util.Map;
import org.ourgrid.common.interfaces.to.GridProcessAccounting;
import org.ourgrid.common.statistics.beans.peer.Peer;
import org.ourgrid.common.statistics.util.hibernate.HibernateUtil;
import org.ourgrid.peer.dao.AccountingDAO;
import org.ourgrid.peer.dao.statistics.PeerDAO;
import org.ourgrid.peer.to.PeerBalance;
import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;

public class AccountingControl extends EntityControl {

    private static AccountingControl instance = null;

    public static AccountingControl getInstance() {
        if (instance == null) {
            instance = new AccountingControl();
        }
        return instance;
    }

    protected AccountingControl() {
    }

    public void setRemotePeerBalance(String localPeerDN, String remotePeerDN, PeerBalance balance, AccountingDAO dao, PeerDAO peerDAO) {
        logger.enter();
        HibernateUtil.beginTransaction();
        try {
            Peer localPeer = peerDAO.getPeerBySubjectDN(localPeerDN);
            Peer remotePeer = peerDAO.getPeerBySubjectDN(remotePeerDN);
            dao.setRemotePeerBalance(localPeer, remotePeer, balance, remotePeerDN);
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            HibernateUtil.rollbackAndCloseSession();
        }
        logger.leave();
    }

    public PeerBalance getRemotePeerBalance(String localPeerDN, String remotePeerDN, AccountingDAO dao, PeerDAO peerDAO) {
        logger.enter();
        HibernateUtil.beginTransaction();
        PeerBalance balance = null;
        try {
            Peer localPeer = peerDAO.getPeerBySubjectDN(localPeerDN);
            Peer remotePeer = peerDAO.getPeerBySubjectDN(remotePeerDN);
            balance = dao.getRemotePeerBalance(localPeer, remotePeer);
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            HibernateUtil.rollbackAndCloseSession();
        }
        logger.leave();
        return balance;
    }

    public Map<String, PeerBalance> getBalances(String peerDNData, AccountingDAO dao, PeerDAO peerDAO) {
        logger.enter();
        HibernateUtil.beginTransaction();
        Map<String, PeerBalance> balances = null;
        try {
            Peer localPeer = peerDAO.getPeerBySubjectDN(peerDNData);
            balances = dao.getBalances(localPeer);
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            HibernateUtil.rollbackAndCloseSession();
        }
        logger.leave();
        return balances;
    }

    public void addReplicaAccounting(GridProcessAccounting replicaAccounting, String providerCertificateDN, ServiceManager serviceManager) {
        logger.enter();
        HibernateUtil.beginTransaction();
        try {
            PeerDAO peerDAO = serviceManager.getDAO(PeerDAO.class);
            Peer peer = peerDAO.getPeerBySubjectDN(providerCertificateDN);
            serviceManager.getDAO(AccountingDAO.class).addReplicaAccounting(replicaAccounting, peer);
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            HibernateUtil.rollbackAndCloseSession();
        }
        logger.leave();
    }
}

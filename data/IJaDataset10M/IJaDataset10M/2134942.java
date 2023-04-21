package com.bizsensors.gourangi.menu.util;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.bizsensors.gourangi.db.DashboardTree;
import com.bizsensors.gourangi.db.HibernateUtil;
import com.bizsensors.gourangi.db.Menu;
import com.bizsensors.gourangi.db.MenuTree;
import com.jenkov.prizetags.tree.impl.Tree;
import com.jenkov.prizetags.tree.impl.TreeNode;
import com.jenkov.prizetags.tree.itf.ITree;
import com.jenkov.prizetags.tree.itf.ITreeNode;

public class DashboardTreeUtil {

    Logger log = Logger.getLogger(this.getClass());

    SessionFactory sf = HibernateUtil.getSessionFactory();

    Session session = sf.openSession();

    public ITree getTree() {
        ITree tree = new Tree();
        ITreeNode root = new TreeNode("0", "Dashboards", "FOLDER");
        Transaction tr = session.beginTransaction();
        try {
            root = populateTree(root, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        } finally {
            tr.commit();
            session.disconnect();
        }
        tree.setRoot(root);
        return tree;
    }

    public ITreeNode populateTree(ITreeNode iTreeNode, int parentID) {
        Query query = session.createQuery("from DashboardTree as dt where dt.parentId=:rootID");
        query.setLong("rootID", parentID);
        log.debug("QUERY : " + query.toString());
        List list = query.list();
        DashboardTree[] dashboardTrees = new DashboardTree[list.size()];
        for (int x = 0; x < list.size(); x++) {
            Object object = list.get(x);
            DashboardTree dashboardTree = new DashboardTree();
            dashboardTree = (DashboardTree) object;
            ITreeNode node = new TreeNode(dashboardTree.getId().toString(), dashboardTree.getName(), dashboardTree.getType());
            iTreeNode.addChild(node);
            populateTree(node, dashboardTree.getId());
        }
        return iTreeNode;
    }

    public static void main(String[] args) {
        DashboardTreeUtil mutil = new DashboardTreeUtil();
        ITree tree = mutil.getTree();
        System.out.println("Menu Size = " + tree.toString());
    }
}

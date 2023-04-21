package org.parosproxy.paros.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.view.SiteMapListener;

/**
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class SiteMapPanel extends JPanel {

    private static final long serialVersionUID = -7183676073224775626L;

    private static Log log = LogFactory.getLog(SiteMapPanel.class);

    private JScrollPane jScrollPane = null;

    private JTree treeSite = null;

    private TreePath rootTreePath = null;

    private View view = null;

    private List<SiteMapListener> listenners = new ArrayList<SiteMapListener>();

    /**
	 * This is the default constructor
	 */
    public SiteMapPanel() {
        super();
        initialize();
    }

    private View getView() {
        if (view == null) {
            view = View.getSingleton();
        }
        return view;
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setLayout(new CardLayout());
        this.setSize(300, 200);
        this.add(getJScrollPane(), getJScrollPane().getName());
        expandRoot();
    }

    /**
	 * This method initializes jScrollPane
	 * 
	 * @return JScrollPane
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getTreeSite());
            jScrollPane.setPreferredSize(new Dimension(200, 400));
            jScrollPane.setName("jScrollPane");
        }
        return jScrollPane;
    }

    /**
	 * This method initializes treeSite
	 * 
	 * @return JTree
	 */
    public JTree getTreeSite() {
        if (treeSite == null) {
            treeSite = new JTree();
            treeSite.setShowsRootHandles(true);
            treeSite.setName("treeSite");
            treeSite.setToggleClickCount(1);
            treeSite.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
                        TreePath tp = treeSite.getPathForLocation(e.getPoint().x, e.getPoint().y);
                        if (tp != null) {
                            boolean select = true;
                            if (treeSite.getSelectionPaths() != null) {
                                for (TreePath t : treeSite.getSelectionPaths()) {
                                    if (t.equals(tp)) {
                                        select = false;
                                        break;
                                    }
                                }
                            }
                            if (select) {
                                treeSite.getSelectionModel().setSelectionPath(tp);
                            }
                        }
                        View.getSingleton().getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
            treeSite.addTreeSelectionListener(new TreeSelectionListener() {

                public void valueChanged(TreeSelectionEvent e) {
                    HttpMessage msg = null;
                    SiteNode node = (SiteNode) treeSite.getLastSelectedPathComponent();
                    if (node == null) {
                        return;
                    }
                    if (!node.isRoot()) {
                        try {
                            msg = node.getHistoryReference().getHttpMessage();
                        } catch (Exception e1) {
                            log.warn(e1.getMessage(), e1);
                            return;
                        }
                        HttpPanel reqPanel = getView().getRequestPanel();
                        HttpPanel resPanel = getView().getResponsePanel();
                        reqPanel.setMessage(msg, true);
                        resPanel.setMessage(msg, false);
                        for (SiteMapListener listener : listenners) {
                            listener.nodeSelected(node);
                        }
                    }
                }
            });
        }
        return treeSite;
    }

    public void expandRoot() {
        TreeNode root = (TreeNode) treeSite.getModel().getRoot();
        if (rootTreePath == null || root != rootTreePath.getPathComponent(0)) {
            rootTreePath = new TreePath(root);
        }
        if (EventQueue.isDispatchThread()) {
            getTreeSite().expandPath(rootTreePath);
            return;
        }
        try {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    getTreeSite().expandPath(rootTreePath);
                }
            });
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    public void addSiteMapListenner(SiteMapListener listenner) {
        this.listenners.add(listenner);
    }
}

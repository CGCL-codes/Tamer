package uk.co.zenly.jllama.gui.tree;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import uk.co.zenly.jllama.data.Collector;
import uk.co.zenly.jllama.data.CollectorTemplate;
import uk.co.zenly.jllama.data.Device;
import uk.co.zenly.jllama.gui.AppInternalFrame;
import uk.co.zenly.jllama.gui.ApplicationWindow;
import uk.co.zenly.jllama.sql.CollectorDAO;
import uk.co.zenly.jllama.sql.CollectorTemplateDAO;
import uk.co.zenly.jllama.sql.DeviceDAO;

/**
 * Tree of all our devices and Collectors
 * 
 * @author dougg
 *
 */
@SuppressWarnings("serial")
public class DeviceTree extends AppInternalFrame implements TreeSelectionListener, InternalFrameListener {

    static Logger logger = Logger.getLogger(ApplicationWindow.class.getName());

    JComponent desktop;

    static int xSize = 700;

    static int ySize = 500;

    JTree tree;

    JSplitPane splitPane;

    JPanel treePanel;

    public DeviceTree(JComponent desktop) {
        super();
        this.desktop = desktop;
        this.treePanel = treePanel();
        setTitle(title());
        this.add(treePanel);
        this.addInternalFrameListener(this);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    public String title() {
        return "Device Tree";
    }

    public void refresh() {
        logger.debug("Refreshing device tree");
        this.remove(treePanel);
        this.treePanel = treePanel();
        this.add(treePanel);
    }

    JPanel treePanel() {
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(1, 0));
        JScrollPane treeView = new JScrollPane(tree());
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(treeView);
        splitPane.setRightComponent(helpPane());
        Dimension minimumSize = new Dimension(360, 100);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(250);
        splitPane.setPreferredSize(new Dimension(700, 500));
        jp.add(splitPane);
        return jp;
    }

    JComponent helpPane() {
        JEditorPane jep = new JEditorPane();
        JScrollPane sp = new JScrollPane(jep);
        return sp;
    }

    JTree tree() {
        SortedTreeNode top = new SortedTreeNode("Devices");
        createNodes(top);
        this.tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
        return tree;
    }

    private void createNodes(SortedTreeNode top) {
        SortedTreeNode category = null;
        SortedTreeNode subCategory = null;
        CollectorTemplateTreeNodeObject cttno = null;
        List<Device> devices = DeviceDAO.getDevices();
        List<CollectorTemplate> collectorTemplates = CollectorTemplateDAO.getCollectorTemplates();
        List<Collector> collectors = CollectorDAO.getCollectors();
        for (Device d : devices) {
            category = new SortedTreeNode(d);
            top.add(category);
            for (Collector c : collectors) {
                if (c.getDeviceId() == d.getId()) {
                    for (CollectorTemplate ct : collectorTemplates) {
                        if (c.getCollectorTemplateId() == ct.getId()) {
                            cttno = new CollectorTemplateTreeNodeObject(c);
                            subCategory = new SortedTreeNode(cttno);
                            category.add(subCategory);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) return;
        Object nodeInfo = node.getUserObject();
        if (Device.class.isInstance(nodeInfo)) {
            logger.debug("Selected a Device Object in the tree");
            Device d = (Device) nodeInfo;
            DevicePanel dPane = new DevicePanel(d);
            splitPane.setRightComponent(new JScrollPane(dPane));
        } else if (CollectorTemplateTreeNodeObject.class.isInstance(nodeInfo)) {
            logger.debug("Selected a Collector in the tree");
            CollectorTemplateTreeNodeObject cttno = (CollectorTemplateTreeNodeObject) nodeInfo;
            CollectorPanel cp = new CollectorPanel(cttno.getCollector());
            splitPane.setRightComponent(new JScrollPane(cp));
        }
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent arg0) {
        logger.debug("Activated the frame");
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent arg0) {
        logger.debug("Closed the frame");
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent arg0) {
        logger.debug("Closing the frame");
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent arg0) {
        logger.debug("Deactivated the frame");
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent arg0) {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent arg0) {
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent arg0) {
    }
}

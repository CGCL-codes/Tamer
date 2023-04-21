package au.gov.qld.dnr.dss.v1.ui.pref;

import au.gov.qld.dnr.dss.v1.framework.Framework;
import au.gov.qld.dnr.dss.v1.framework.interfaces.ResourceManager;
import org.swzoo.log2.core.*;
import au.gov.qld.dnr.dss.v1.ui.pref.interfaces.PreferenceContainer;
import au.gov.qld.dnr.dss.v1.ui.pref.content.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * The preference component.
 */
public class PreferenceComponent implements PreferenceContainer {

    /** Preference user interface component. */
    JComponent _prefUI = null;

    /** Preference providers. */
    Vector _prefContainers;

    /**
     * Constructor for ranking component.
     */
    private PreferenceComponent() {
        _prefContainers = new Vector();
    }

    /**
     * Initialise this component based on the provided properties.
     *
     * @param pref a list of preferences.
     */
    public void init(Properties pref) {
        LogTools.trace(logger, 25, "PreferenceComponent.init().");
        for (int i = 0; i < _prefContainers.size(); i++) {
            ((PreferenceContainer) _prefContainers.elementAt(i)).init(pref);
        }
    }

    /**
     * Store the prefences.
     *
     * @param pref a properties object to store the preferences into.
     */
    public void store(Properties pref) {
        LogTools.trace(logger, 25, "PreferenceComponent.store().");
        for (int i = 0; i < _prefContainers.size(); i++) {
            ((PreferenceContainer) _prefContainers.elementAt(i)).store(pref);
        }
    }

    /**
     * Get the main user interface component.
     *
     * @return the component.
     */
    public JComponent getUIComponent() {
        return _prefUI;
    }

    /**
     * Set the preference UI component.
     *
     * @param ui the preference UI component.
     */
    protected void setUI(JComponent ui) {
        _prefUI = ui;
    }

    /**
     * A a preference panel to our list of known preference panels.
     */
    void addProviderPanel(PreferenceContainer provider) {
    }

    /**
     * Build a preference component.
     */
    public static PreferenceComponent createInstance() {
        JPanel main;
        final JPanel contentPanel;
        JScrollPane treescroller;
        JScrollPane contentscroller;
        JTree jtree;
        jtree = new JTree();
        jtree.setScrollsOnExpand(true);
        jtree.setShowsRootHandles(false);
        jtree.setRootVisible(false);
        TreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
        selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jtree.setSelectionModel(selectionModel);
        jtree.setCellRenderer(new PreferenceTreeCellRenderer());
        DefaultMutableTreeNode initialSelection;
        {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            {
                DefaultMutableTreeNode report = new DefaultMutableTreeNode(new ReportPreferences());
                {
                    DefaultMutableTreeNode analysis = new DefaultMutableTreeNode(new ReportAnalysisPreferences());
                    DefaultMutableTreeNode ranking = new DefaultMutableTreeNode(new ReportRankingPreferences());
                    DefaultMutableTreeNode result = new DefaultMutableTreeNode(new ReportResultPreferences());
                    report.add(analysis);
                    report.add(ranking);
                    report.add(result);
                }
                root.add(report);
                initialSelection = report;
            }
            jtree.setModel(new DefaultTreeModel(root, false));
        }
        expandTree(jtree);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        jtree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent ev) {
                LogTools.trace(logger, 25, "PreferenceComponent.TreeSelectionListener.valueChanged()");
                LogTools.trace(logger, 25, "PreferenceComponent.TreeSelectionListener.valueChanged() - Removing any content panel components.");
                contentPanel.removeAll();
                TreePath path = ev.getNewLeadSelectionPath();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                JComponent comp = (JComponent) node.getUserObject();
                if (comp != null) {
                    LogTools.trace(logger, 25, "PreferenceComponent.TreeSelectionListener.valueChanged() - Adding " + comp.getClass().getName() + " to the contentPanel");
                    contentPanel.add(comp, BorderLayout.CENTER);
                    comp.invalidate();
                } else {
                    LogTools.trace(logger, 25, "PreferenceComponent.TreeSelectionListener.valueChanged() - It seems the user object component is NULL");
                }
                contentPanel.validate();
                contentPanel.repaint();
            }
        });
        jtree.setSelectionPath(new TreePath(initialSelection.getPath()));
        contentscroller = new JScrollPane(contentPanel);
        treescroller = new JScrollPane(jtree);
        treescroller.setPreferredSize(new Dimension(150, 300));
        contentscroller.setPreferredSize(new Dimension(400, 300));
        main = new JPanel();
        main.setLayout(new BorderLayout());
        main.add(treescroller, BorderLayout.WEST);
        main.add(contentscroller, BorderLayout.CENTER);
        BevelBorder bevel = new BevelBorder(BevelBorder.LOWERED);
        EmptyBorder margin = new EmptyBorder(10, 10, 10, 10);
        CompoundBorder border = new CompoundBorder(margin, bevel);
        main.setBorder(border);
        PreferenceComponent pcomp = new PreferenceComponent();
        pcomp.setUI(main);
        return pcomp;
    }

    /**
     * Expand the entire tree.
     */
    static void expandTree(JTree jtree) {
        expand(new TreePath(jtree.getModel().getRoot()), jtree);
    }

    /**
     * Expand a tree path and all tree paths below.
     */
    static void expand(TreePath path, JTree jtree) {
        DefaultMutableTreeNode jnode = (DefaultMutableTreeNode) path.getLastPathComponent();
        jtree.expandPath(path);
        Enumeration children = jnode.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            TreePath childPath = new TreePath(child.getPath());
            expand(childPath, jtree);
        }
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource manager. */
    static ResourceManager resources = Framework.getGlobalManager().getResourceManager();
}

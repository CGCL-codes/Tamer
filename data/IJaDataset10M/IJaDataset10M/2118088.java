package org.opencdspowered.opencds.ui.config;

import org.opencdspowered.opencds.ui.main.MainFrame;
import org.opencdspowered.opencds.core.lang.DynamicLocalisation;
import org.opencdspowered.opencds.core.config.*;
import org.opencdspowered.opencds.ui.config.panels.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.event.*;

/**
 * The configuration dialog class.
 *
 * @author  Lars 'Levia' Wesselius
*/
public class ConfigurationDialog extends JDialog implements TreeSelectionListener, ActionListener {

    private MainFrame m_MainFrame;

    private JTree m_Tree;

    private JSplitPane m_SplitPane;

    private JButton m_Save;

    private JButton m_Ok;

    private JButton m_Cancel;

    LanguagePanel m_LangPanel = null;

    ThemePanel m_ThemePanel = null;

    LoggingPanel m_LoggingPanel = null;

    HTTPPanel m_HTTPPanel = null;

    FTPPanel m_FTPPanel = null;

    UpdaterPanel m_UpdaterPanel = null;

    ViewPanel m_ViewPanel = null;

    private java.util.List<SaveListener> m_Listeners = new java.util.ArrayList<SaveListener>();

    /**
     * The configuration dialog constructor.
    */
    public ConfigurationDialog(MainFrame mainFrame, boolean modal) {
        super(mainFrame.getFrame(), mainFrame.getLocalisation().getMessage("ConfigurationDialog.Caption"), modal);
        m_MainFrame = mainFrame;
        initialize();
    }

    /**
     * Initializes the dialog.
    */
    public void initialize() {
        DynamicLocalisation loc = m_MainFrame.getLocalisation();
        this.setSize(650, 550);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(ConfigurationDialog.class.getResource("/org/opencdspowered/opencds/ui/icons/tools-configuration.png")).getImage());
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        JPanel contentPane = new JPanel();
        contentPane.setOpaque(true);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 3, 5));
        this.setContentPane(contentPane);
        m_Tree = new JTree();
        JPanel rightPanel = new JPanel();
        m_SplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_Tree, rightPanel);
        m_SplitPane.setOneTouchExpandable(true);
        m_SplitPane.setDividerLocation(150);
        m_SplitPane.setAlignmentX(Component.RIGHT_ALIGNMENT);
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode generalNode = new DefaultMutableTreeNode(loc.getMessage("ConfigurationDialog.GeneralSection"));
        rootNode.add(generalNode);
        if (m_ViewPanel == null) {
            m_ViewPanel = new ViewPanel(m_MainFrame);
        }
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(m_ViewPanel);
        m_Listeners.add(m_ViewPanel);
        TreePath path = new TreePath(childNode.getPath());
        generalNode.add(childNode);
        if (m_LangPanel == null) {
            m_LangPanel = new LanguagePanel(m_MainFrame);
        }
        childNode = new DefaultMutableTreeNode(m_LangPanel);
        m_Listeners.add(m_LangPanel);
        generalNode.add(childNode);
        if (m_ThemePanel == null) {
            m_ThemePanel = new ThemePanel(m_MainFrame, this);
        }
        childNode = new DefaultMutableTreeNode(m_ThemePanel);
        m_Listeners.add(m_ThemePanel);
        generalNode.add(childNode);
        if (m_LoggingPanel == null) {
            m_LoggingPanel = new LoggingPanel(m_MainFrame);
        }
        childNode = new DefaultMutableTreeNode(m_LoggingPanel);
        m_Listeners.add(m_LoggingPanel);
        generalNode.add(childNode);
        DefaultMutableTreeNode connectionNode = new DefaultMutableTreeNode(loc.getMessage("ConfigurationDialog.ConnectionSection"));
        rootNode.add(connectionNode);
        if (m_HTTPPanel == null) {
            m_HTTPPanel = new HTTPPanel(m_MainFrame);
        }
        childNode = new DefaultMutableTreeNode(m_HTTPPanel);
        m_Listeners.add(m_HTTPPanel);
        connectionNode.add(childNode);
        if (m_FTPPanel == null) {
            m_FTPPanel = new FTPPanel(m_MainFrame);
        }
        childNode = new DefaultMutableTreeNode(m_FTPPanel);
        m_Listeners.add(m_FTPPanel);
        connectionNode.add(childNode);
        if (m_UpdaterPanel == null) {
            m_UpdaterPanel = new UpdaterPanel(m_MainFrame);
        }
        childNode = new DefaultMutableTreeNode(m_UpdaterPanel);
        m_Listeners.add(m_UpdaterPanel);
        connectionNode.add(childNode);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        m_Tree.setModel(model);
        for (int i = 0; i != m_Tree.getRowCount(); ++i) {
            m_Tree.expandRow(i);
        }
        m_Tree.setRootVisible(false);
        m_Tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        m_Tree.getSelectionModel().addTreeSelectionListener(this);
        m_Tree.setSelectionPath(path);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        m_Save = new JButton(loc.getMessage("ConfigurationDialog.Save"));
        m_Save.setActionCommand("Save");
        m_Save.setAlignmentX(Component.RIGHT_ALIGNMENT);
        m_Save.addActionListener(this);
        m_Ok = new JButton(loc.getMessage("General.Ok"));
        m_Ok.setActionCommand("Ok");
        m_Ok.setAlignmentX(Component.RIGHT_ALIGNMENT);
        m_Ok.addActionListener(this);
        m_Cancel = new JButton(loc.getMessage("Cancel"));
        m_Cancel.setActionCommand("Cancel");
        m_Cancel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        m_Cancel.addActionListener(this);
        buttonPanel.add(m_Save);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(m_Ok);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(m_Cancel);
        m_Tree.setMinimumSize(new Dimension(100, 500));
        rightPanel.setMinimumSize(new Dimension(200, 500));
        contentPane.add(m_SplitPane);
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPane.add(buttonPanel);
        this.setVisible(true);
    }

    /**
     * Fired when the selection has been changed.
    */
    public void valueChanged(TreeSelectionEvent event) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) m_Tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        if (node.isLeaf()) {
            Object nodeInfo = node.getUserObject();
            m_SplitPane.setRightComponent((JPanel) nodeInfo);
            m_SplitPane.setDividerLocation(150);
        } else {
            m_SplitPane.setRightComponent(new JPanel());
            m_SplitPane.setDividerLocation(150);
        }
    }

    /**
     * Fired when a button has been clicked.
    */
    public void actionPerformed(ActionEvent event) {
        String e = event.getActionCommand();
        if (e.equals("Save")) {
            fireSave();
            ConfigurationManager.getInstance().stopSession();
        } else if (e.equals("Cancel")) {
            ConfigurationManager.getInstance().cancel(true);
            this.setVisible(false);
        } else if (e.equals("Ok")) {
            fireSave();
            ConfigurationManager.getInstance().stopSession();
            this.setVisible(false);
        }
    }

    /**
     * Fire a save action.
    */
    private void fireSave() {
        for (int i = 0; i < m_Listeners.size(); i++) {
            SaveListener listener = (SaveListener) m_Listeners.get(i);
            listener.save();
        }
    }
}

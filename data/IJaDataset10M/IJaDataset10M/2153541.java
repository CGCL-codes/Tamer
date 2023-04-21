package com.teletalk.jadmin.gui.overview;

import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.NoSuchElementException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import com.teletalk.jadmin.gui.AdminMainPanel;
import com.teletalk.jadmin.gui.util.ConfirmButton;
import com.teletalk.jadmin.gui.util.ConfirmButtonListener;
import com.teletalk.jadmin.gui.util.GuiUtils;
import com.teletalk.jadmin.gui.util.ResourceHandler;
import com.teletalk.jadmin.proxy.JServerProxy;
import com.teletalk.jadmin.proxy.PropertyProxy;
import com.teletalk.jadmin.proxy.RemoteObjectProxy;
import com.teletalk.jadmin.proxy.SubComponentProxy;
import com.teletalk.jadmin.proxy.SubSystemProxy;
import com.teletalk.jadmin.proxy.VectorPropertyProxy;

/**
 *  This class is the container panel for the server component tree, as well as the server control button panel.
 * 
 * @author Tobias L�fstrand
 * 
 * @since Alpha
 */
public final class OverviewPanel extends JPanel implements ActionListener, ConfirmButtonListener, MouseListener {

    private static final long serialVersionUID = 1L;

    private final AdminMainPanel mainPanel;

    private final Frame mainFrame;

    private JServerProxy jServerProxy;

    private final JPanel waitPanel;

    private final JLabel waitLabel;

    private final ImageIcon waitIcon1;

    private final ImageIcon waitIcon2;

    private final ImageIcon waitIcon3;

    private final ImageIcon waitIcon4;

    AdminTree tree;

    private boolean initialized;

    private int waitIconSwichCount;

    private final JPanel controlPanel;

    private final ConfirmButton kill;

    private final ConfirmButton shutDown;

    private final ConfirmButton restart;

    final HashMap nodes;

    private JPopupMenu popupMenu;

    private JMenuItem engageM;

    private JMenuItem shutDownM;

    private JMenuItem reinitializeM;

    private RemoteObjectProxy popupMenuData = null;

    private PropertyDialog propertyDialog = null;

    private VectorPropertyDialog vectorPropertyDialog = null;

    /**
	 * Creates a new OverviewPanel.
	 */
    public OverviewPanel(final AdminMainPanel mainPanel) throws Exception {
        super();
        initialized = false;
        waitIconSwichCount = 0;
        this.mainPanel = mainPanel;
        this.mainFrame = mainPanel.getMainFrame();
        createPopupMenu();
        this.nodes = new HashMap();
        setLayout(new BorderLayout());
        waitPanel = new JPanel(new BorderLayout());
        JPanel waitInnerPanel2 = new JPanel();
        waitInnerPanel2.setLayout(new BoxLayout(waitInnerPanel2, BoxLayout.Y_AXIS));
        JPanel waitInnerPanel = new JPanel();
        waitInnerPanel.setLayout(new BoxLayout(waitInnerPanel, BoxLayout.X_AXIS));
        waitLabel = new JLabel("Please wait...");
        waitIcon1 = ResourceHandler.getImageIcon("/images/wait_icon1.gif");
        waitIcon2 = ResourceHandler.getImageIcon("/images/wait_icon2.gif");
        waitIcon3 = ResourceHandler.getImageIcon("/images/wait_icon3.gif");
        waitIcon4 = ResourceHandler.getImageIcon("/images/wait_icon4.gif");
        waitLabel.setIcon(waitIcon1);
        waitInnerPanel.add(Box.createGlue());
        waitInnerPanel.add(waitLabel);
        waitInnerPanel.add(Box.createGlue());
        waitInnerPanel2.add(Box.createGlue());
        waitInnerPanel2.add(waitInnerPanel);
        waitInnerPanel2.add(Box.createGlue());
        waitPanel.add(waitInnerPanel2, BorderLayout.CENTER);
        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        AudioClip warningSound = ResourceHandler.getAudioClip("/sounds/warning.wav");
        JPanel leftInnerPanel = new JPanel();
        leftInnerPanel.setLayout(new BoxLayout(leftInnerPanel, BoxLayout.Y_AXIS));
        leftInnerPanel.setAlignmentX(CENTER_ALIGNMENT);
        leftInnerPanel.setAlignmentY(CENTER_ALIGNMENT);
        kill = new ConfirmButton("/images/kill/", ".gif", "/images/confirm/", ".gif", 16);
        kill.getConfirmationButton().setEnableSound(warningSound);
        kill.setEnableTimeout(5 * 1000);
        kill.setAlignmentX(CENTER_ALIGNMENT);
        kill.addConfirmButtonListener(this);
        shutDown = new ConfirmButton("/images/shutdown/", ".gif", "/images/confirm/", ".gif", 16);
        shutDown.getConfirmationButton().setEnableSound(warningSound);
        shutDown.setEnableTimeout(5 * 1000);
        shutDown.setAlignmentX(CENTER_ALIGNMENT);
        shutDown.addConfirmButtonListener(this);
        restart = new ConfirmButton("/images/restart/", ".gif", "/images/confirm/", ".gif", 16);
        restart.getConfirmationButton().setEnableSound(warningSound);
        restart.setEnableTimeout(5 * 1000);
        restart.setAlignmentX(CENTER_ALIGNMENT);
        restart.addConfirmButtonListener(this);
        leftInnerPanel.add(Box.createVerticalStrut(5));
        leftInnerPanel.add(kill);
        leftInnerPanel.add(Box.createVerticalStrut(5));
        leftInnerPanel.add(shutDown);
        leftInnerPanel.add(Box.createVerticalStrut(5));
        leftInnerPanel.add(restart);
        leftInnerPanel.add(Box.createVerticalStrut(5));
        controlPanel.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
        controlPanel.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
        controlPanel.add(leftInnerPanel, BorderLayout.CENTER);
    }

    /**
	 * Initializes this OverviewPanel.
	 */
    public void initialize(final JServerProxy proxy) {
        if (initialized) destroy();
        add(waitPanel, BorderLayout.CENTER);
        this.jServerProxy = proxy;
        tree = new AdminTree(proxy, mainPanel);
        tree.addTreeMouseListener(this);
        mainPanel.setKeyActions(tree);
    }

    /**
	 * Called to indicate that a connection to a server has been established.
	 */
    public void connectedToServer() {
        remove(waitPanel);
        add(tree, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        initialized = true;
        controlPanel.validate();
        controlPanel.repaint();
        this.validate();
    }

    /**
	 * Switches the icon on the waitpanel.
	 */
    public void swichWaitIcon() {
        waitIconSwichCount++;
        int periodicity = 1;
        if (waitIconSwichCount % (8 * periodicity) == 6 * periodicity) {
            waitLabel.setIcon(waitIcon4);
        } else if (waitIconSwichCount % (8 * periodicity) == 4 * periodicity) {
            waitLabel.setIcon(waitIcon3);
        } else if (waitIconSwichCount % (8 * periodicity) == 2 * periodicity) {
            waitLabel.setIcon(waitIcon2);
        } else if (waitIconSwichCount % (8 * periodicity) == 0 * periodicity) {
            waitLabel.setIcon(waitIcon1);
        }
    }

    /**
	 * Creates the popup menu for the tree.
	 */
    private void createPopupMenu() {
        popupMenu = new JPopupMenu("Admin commands");
        ImageIcon redArrow = ResourceHandler.getImageIcon("/images/redarrow.gif");
        ImageIcon greenArrow = ResourceHandler.getImageIcon("/images/greenarrow.gif");
        ImageIcon redGreenArrow = ResourceHandler.getImageIcon("/images/red-green-arrow.gif");
        engageM = new JMenuItem("Engage", greenArrow);
        shutDownM = new JMenuItem("Shutdown", redArrow);
        reinitializeM = new JMenuItem("Reinitialize", redGreenArrow);
        engageM.setHorizontalTextPosition(JMenuItem.RIGHT);
        shutDownM.setHorizontalTextPosition(JMenuItem.RIGHT);
        reinitializeM.setHorizontalTextPosition(JMenuItem.RIGHT);
        engageM.addActionListener(this);
        shutDownM.addActionListener(this);
        reinitializeM.addActionListener(this);
        popupMenu.setLabel("Admin commands");
        popupMenu.add(engageM);
        popupMenu.add(shutDownM);
        popupMenu.add(reinitializeM);
        popupMenu.pack();
    }

    /**
	 * Sets the popup mode.
	 */
    private boolean setPopupMode(final Object nodeData) {
        boolean ok = false;
        popupMenu.setLabel("Admin commands");
        popupMenu.removeAll();
        if (nodeData instanceof SubComponentProxy) {
            popupMenu.add(engageM);
            popupMenu.add(shutDownM);
            popupMenu.add(reinitializeM);
            ok = true;
        }
        return ok;
    }

    /**
	 * Destroys this panel.
	 */
    public void destroy() {
        try {
            if (propertyDialog != null) {
                propertyDialog.dispose();
                propertyDialog = null;
            }
        } catch (Exception e) {
        }
        try {
            if (vectorPropertyDialog != null) {
                vectorPropertyDialog.dispose();
                vectorPropertyDialog = null;
            }
        } catch (Exception e) {
        }
        if (tree != null) {
            tree.clearTree();
            remove(tree);
        }
        if (controlPanel != null) remove(controlPanel);
        nodes.clear();
        tree = null;
        jServerProxy = null;
        initialized = false;
    }

    /**
	 * Called when a confim button has been pressed.
	 */
    public void confimButtonPressed(final ConfirmButton button) {
        if (button == kill) {
            jServerProxy.killServer();
        } else if (button == shutDown) {
            jServerProxy.shutDownServer();
        } else if (button == restart) {
            jServerProxy.restartServer();
        }
    }

    /**
	 * Action event handler method
	 */
    public void actionPerformed(final ActionEvent event) {
        if (popupMenuData == null) popupMenuData = tree.getSelectedNodeData();
        if (event.getSource() == engageM) {
            if (popupMenuData instanceof SubComponentProxy) ((SubComponentProxy) popupMenuData).engage();
        } else if (event.getSource() == shutDownM) {
            if (popupMenuData instanceof SubComponentProxy) ((SubComponentProxy) popupMenuData).shutDown();
        } else if (event.getSource() == reinitializeM) {
            if (popupMenuData instanceof SubComponentProxy) ((SubComponentProxy) popupMenuData).reinitialize();
        }
        popupMenuData = null;
    }

    /**
	 * Gets the next index on which an element can be added under the specified parent SubSystem element. 
	 */
    int getNextSubSystemProxyIndex(final SubSystemProxy parent) {
        DefaultMutableTreeNode parentNode;
        int sChildren = 0;
        int otherChildren = 0;
        if (parent == jServerProxy) parentNode = tree.getRootNode(); else parentNode = (DefaultMutableTreeNode) nodes.get(parent.getFullName());
        if (parentNode != null) {
            int children = parentNode.getChildCount();
            for (int i = 0; i < children; i++) {
                Object userObj = ((DefaultMutableTreeNode) parentNode.getChildAt(i)).getUserObject();
                if (userObj instanceof SubSystemProxy) {
                    sChildren++;
                } else {
                    otherChildren++;
                }
            }
        }
        return sChildren;
    }

    /**
	 * Gets the next index on which an element can be added under the specified parent SubComponent element. 
	 */
    int getNextSubComponentProxyIndex(SubComponentProxy parent) {
        DefaultMutableTreeNode parentNode;
        int sChildren = 0;
        if (parent == jServerProxy) parentNode = tree.getRootNode(); else parentNode = (DefaultMutableTreeNode) nodes.get(parent.getFullName());
        if (parentNode != null) {
            int children = parentNode.getChildCount();
            for (int i = 0; i < children; i++) {
                Object userObj = ((DefaultMutableTreeNode) parentNode.getChildAt(i)).getUserObject();
                if (userObj instanceof SubSystemProxy || userObj instanceof SubComponentProxy) {
                    sChildren++;
                }
            }
        }
        return sChildren;
    }

    /**
	 * Adds a SubSystem node to the tree.
	 */
    public void addSubSystemNode(final RemoteObjectProxy nodeData, final SubSystemProxy parentData) {
        Runnable addTask = new Runnable() {

            public void run() {
                synchronized ((OverviewPanel.this)) {
                    int index = getNextSubSystemProxyIndex(parentData);
                    doAddNode(nodeData, parentData, index);
                }
            }
        };
        SwingUtilities.invokeLater(addTask);
    }

    /**
	 * Adds a SubComponent node to the tree.
	 */
    public void addSubComponentNode(final RemoteObjectProxy nodeData, final SubComponentProxy parentData) {
        Runnable addTask = new Runnable() {

            public void run() {
                synchronized ((OverviewPanel.this)) {
                    int index = getNextSubComponentProxyIndex(parentData);
                    doAddNode(nodeData, parentData, index);
                }
            }
        };
        SwingUtilities.invokeLater(addTask);
    }

    /**
	 * Adds a node to the tree.
	 */
    public void addNode(final RemoteObjectProxy nodeData, final RemoteObjectProxy parentData) {
        addNode(nodeData, parentData, -1);
    }

    /**
	 * Adds a node to the tree.
	 */
    public void addNode(final RemoteObjectProxy nodeData, final RemoteObjectProxy parentData, final int index) {
        Runnable addTask = new Runnable() {

            public void run() {
                synchronized ((OverviewPanel.this)) {
                    doAddNode(nodeData, parentData, index);
                }
            }
        };
        SwingUtilities.invokeLater(addTask);
    }

    /**
	 * Internal method to add a node to the tree.
	 */
    void doAddNode(final RemoteObjectProxy nodeData, final RemoteObjectProxy parentData, final int index) {
        if (!this.initialized) swichWaitIcon();
        final DefaultMutableTreeNode parentNode;
        final boolean makeVisible;
        if (parentData == jServerProxy) {
            parentNode = tree.getRootNode();
            makeVisible = true;
        } else {
            parentNode = (DefaultMutableTreeNode) nodes.get(parentData.getFullName());
            makeVisible = false;
        }
        if (parentNode != null) {
            DefaultMutableTreeNode addedNode = (DefaultMutableTreeNode) nodes.get(nodeData.getFullName());
            if (addedNode == null) {
                if (index >= 0) addedNode = (DefaultMutableTreeNode) tree.addNode(nodeData, parentNode, index); else addedNode = (DefaultMutableTreeNode) tree.addNode(nodeData, parentNode);
                nodes.put(nodeData.getFullName(), addedNode);
            }
            if (makeVisible) {
                try {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentNode.getFirstChild();
                    if (node != null) tree.makeVisible(new TreePath(node.getPath()));
                    (OverviewPanel.this).revalidate();
                } catch (NoSuchElementException e) {
                }
            }
        }
    }

    /**
	 * Adds a node under the root node.
	 */
    public void addNode(final RemoteObjectProxy nodeData) {
        if (!this.initialized) swichWaitIcon();
        Runnable addTask = new Runnable() {

            public void run() {
                DefaultMutableTreeNode addedNode = (DefaultMutableTreeNode) tree.addNode(nodeData);
                nodes.put(nodeData.getFullName(), addedNode);
            }
        };
        SwingUtilities.invokeLater(addTask);
    }

    /**
	 * Adds a node at the specified index under the root node.
	 */
    public void addNode(final RemoteObjectProxy nodeData, final int index) {
        if (!this.initialized) swichWaitIcon();
        Runnable addTask = new Runnable() {

            public void run() {
                DefaultMutableTreeNode addedNode = (DefaultMutableTreeNode) tree.addNode(nodeData, index);
                nodes.put(nodeData.getFullName(), addedNode);
            }
        };
        SwingUtilities.invokeLater(addTask);
    }

    /**
	 * Removes the node with the specified full name.
	 */
    public void removeNode(final String nodeName) {
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.get(nodeName);
        if (node != null) {
            Runnable addTask = new Runnable() {

                public void run() {
                    tree.removeNode(node, (DefaultMutableTreeNode) node.getParent());
                    nodes.remove(nodeName);
                }
            };
            SwingUtilities.invokeLater(addTask);
        }
    }

    /**
    * Called to indicate that the node with the specified full name has changed.
    */
    public void nodeChanged(final String nodeName) {
        Runnable doNodeChangedTask = new Runnable() {

            public void run() {
                doNodeChanged(nodeName);
            }
        };
        SwingUtilities.invokeLater(doNodeChangedTask);
    }

    /**
	 * Called by the sequential node change handler to indicate that the node with the specified full name has changed.
	 */
    void doNodeChanged(final String nodeName) {
        DefaultMutableTreeNode node;
        node = (DefaultMutableTreeNode) nodes.get(nodeName);
        if (node != null) {
            tree.nodeChanged(node);
        }
    }

    /**
	 * Updates the tree.
	 */
    public void updateTree() {
        tree.update();
    }

    /**
	 * Mouse click event handler method.
	 */
    public void mouseClicked(MouseEvent evt) {
        try {
            if (evt.getModifiers() == MouseEvent.BUTTON1_MASK && evt.getClickCount() == 2) {
                RemoteObjectProxy data = tree.getNodeDataAt(evt.getX(), evt.getY());
                if (data != null && data instanceof PropertyProxy) {
                    final PropertyProxy pProxy = (PropertyProxy) data;
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.get(data.getFullName());
                    if (!(pProxy instanceof VectorPropertyProxy)) {
                        try {
                            try {
                                if (propertyDialog != null) {
                                    propertyDialog.dispose();
                                    propertyDialog = null;
                                }
                            } catch (Exception e) {
                            }
                            propertyDialog = new PropertyDialog(mainFrame, pProxy);
                            Dimension dlgSize = propertyDialog.getPreferredSize();
                            Dimension frmSize = mainFrame.getSize();
                            Point loc = mainFrame.getLocation();
                            propertyDialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
                            propertyDialog.setSize(dlgSize);
                            propertyDialog.setVisible(true);
                            propertyDialog = null;
                            if (initialized) tree.nodeChanged(node, data);
                        } catch (Exception e) {
                            GuiUtils.showErrorMessage("Unable to open property change dialog! (" + e + ")");
                        }
                    } else {
                        try {
                            try {
                                if (vectorPropertyDialog != null) {
                                    vectorPropertyDialog.dispose();
                                    vectorPropertyDialog = null;
                                }
                            } catch (Exception e) {
                            }
                            vectorPropertyDialog = new VectorPropertyDialog(mainFrame, (VectorPropertyProxy) pProxy);
                            Dimension dlgSize = new Dimension(550, 400);
                            Dimension frmSize = mainFrame.getSize();
                            Point loc = mainFrame.getLocation();
                            vectorPropertyDialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
                            vectorPropertyDialog.setSize(dlgSize);
                            vectorPropertyDialog.setVisible(true);
                            vectorPropertyDialog = null;
                            if (initialized) tree.nodeChanged(node, data);
                        } catch (Exception e) {
                            GuiUtils.showErrorMessage("Unable to open vectorproperty dialog! (" + e + ")");
                        }
                    }
                }
            } else if (evt.getModifiers() == MouseEvent.BUTTON3_MASK) {
                popupMenuData = tree.getNodeDataAt(evt.getX(), evt.getY());
                evt = SwingUtilities.convertMouseEvent(tree.getTree(), evt, tree);
                if ((popupMenuData != null) && setPopupMode(popupMenuData)) {
                    popupMenu.pack();
                    popupMenu.show(this, evt.getX(), evt.getY());
                }
            }
        } catch (Exception e) {
            if (mainPanel != null) mainPanel.writeToEventLog("Error in mouseevent - " + e);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}

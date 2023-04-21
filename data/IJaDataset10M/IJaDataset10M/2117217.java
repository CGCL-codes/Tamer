package com.arsenal.user.client.panels;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import com.arsenal.message.*;
import com.arsenal.session.*;
import com.arsenal.client.*;
import com.arsenal.user.*;
import com.arsenal.user.client.*;
import com.arsenal.client.observer.*;
import com.arsenal.user.message.*;
import com.arsenal.skin.SkinBean;
import com.arsenal.skin.SkinChangeObserver;

public class UserPanel extends JPanel implements LogoutObserver, LoginObserver, CleanupBeforeLogoutObserver, NewActiveUserObserver, RemoveActiveUserObserver, SkinChangeObserver {

    private final int ROOTNODECHOSEN = 1;

    private final int USERNODECHOSEN = 2;

    private Vector selectUserObservers = new Vector();

    private Vector unselectUserObservers = new Vector();

    private String panelText = "Users";

    private int selRow;

    private TreePath selPath = null;

    private JScrollPane buttonScrollPane = null;

    private ImageIcon userIcon = new ImageIcon("./skins/arsenal/user/user.jpg");

    private ImageIcon allUsersIcon = new ImageIcon("./skins/arsenal/user/users.jpg");

    private JPanel buttonPanel = new JPanel();

    private DefaultMutableTreeNode base = new DefaultMutableTreeNode("Users");

    private ArsenalUserTreeRenderer renderer = new ArsenalUserTreeRenderer();

    private JTree tree = new JTree(base);

    public DefaultMutableTreeNode getBaseNode() {
        return base;
    }

    private JScrollPane treeView = new JScrollPane(tree);

    private JSplitPane mainPanel = null;

    private String userChosen = null;

    private void setUserChosen(String userChosen) {
        this.userChosen = userChosen;
    }

    public String getUserChosen() {
        return this.userChosen;
    }

    private Hashtable userNodeList = new Hashtable();

    public Hashtable getUserNodes() {
        return userNodeList;
    }

    private static UserPanel instance = new UserPanel();

    public static UserPanel getInstance() {
        if (instance == null) {
            instance = new UserPanel();
        }
        return instance;
    }

    public UserPanel() {
        setTreeSize();
        assignButtonListeners();
        addButtonsToPanel();
        setButtonPanelSize();
        setScrollBarOnPanels();
        setMainPanelSizeAndAdd();
        registerNewActiveUserListener(this);
        registerRemoveActiveUserListener(this);
        registerLogoutListener(this);
        registerLoginListener(this);
        registerCleanupBeforeLogoutListener(this);
        registerCleanupBeforeLogoutListener(this);
        registerSkinChangeListener(this);
        addTreeMouseListener();
        mainPanel.setSize(new Dimension(170, 550));
        mainPanel.setPreferredSize(new Dimension(170, 650));
        mainPanel.setMinimumSize(new Dimension(170, 650));
    }

    public void setRendererForTree() {
        tree.setCellRenderer(renderer);
    }

    private void setTreeSize() {
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeView.setSize(new Dimension(175, 395));
        treeView.setPreferredSize(new Dimension(175, 395));
        treeView.setMinimumSize(new Dimension(175, 300));
        treeView.setMaximumSize(new Dimension(175, 430));
    }

    private void assignButtonListeners() {
    }

    private void addButtonsToPanel() {
    }

    private void setButtonPanelSize() {
        buttonPanel.setSize(new Dimension(170, 10));
        buttonPanel.setPreferredSize(new Dimension(170, 10));
        buttonPanel.setMinimumSize(new Dimension(170, 10));
    }

    private void setScrollBarOnPanels() {
        treeView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        treeView.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        buttonScrollPane = new JScrollPane(buttonPanel);
        buttonScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        buttonScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    private void setMainPanelSizeAndAdd() {
        mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, treeView, buttonScrollPane);
        mainPanel.setDividerLocation(400);
        mainPanel.setSize(new Dimension(170, 550));
        mainPanel.setPreferredSize(new Dimension(170, 650));
        mainPanel.setMinimumSize(new Dimension(170, 650));
        mainPanel.setDividerSize(5);
        add(mainPanel);
    }

    private void addTreeMouseListener() {
        MouseListener ml = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                selRow = tree.getRowForLocation(e.getX(), e.getY());
                selPath = tree.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    if (e.getClickCount() == 1) {
                        handleUI(selPath);
                    }
                }
            }
        };
        tree.addMouseListener(ml);
    }

    /***************************************************************************
   *
   * handle UI components and events
   *
   ***************************************************************************/
    public void handleUI(TreePath selPath) {
        switch(selPath.getPathCount()) {
            case ROOTNODECHOSEN:
                alertRootNodeChosen();
                break;
            case USERNODECHOSEN:
                setUserChosen(selPath.getPathComponent(selPath.getPathCount() - 1).toString());
                alertUserNodeChosen();
                break;
        }
    }

    public void addToButtonPanel(JButton button) {
        buttonPanel.add(button);
    }

    public DefaultMutableTreeNode getActiveUserNode(String username) {
        return (DefaultMutableTreeNode) getUserNodes().get(username);
    }

    /************************************************************************
   *
   * event handlers
   *
   ************************************************************************/
    public void doNewActiveUserAction(Object object) {
        UserBean bean = (UserBean) object;
        if ((bean == null) || (bean.getName() == null) || (userNodeList.get(bean.getName()) != null)) return;
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(bean.getName());
        base.add(node);
        userNodeList.put(bean.getName(), node);
        tree.updateUI();
    }

    public void registerNewActiveUserListener(NewActiveUserObserver newActiveUserObserver) {
        UserClientHandler.getInstance().registerNewActiveUserListener(newActiveUserObserver);
    }

    public void doRemoveActiveUserAction(Object object) {
        UserBean bean = (UserBean) object;
        if (userNodeList.get(bean.getName()) == null) return;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) userNodeList.get(bean.getName());
        base.remove(node);
        userNodeList.remove(bean.getName());
        tree.updateUI();
    }

    public void registerRemoveActiveUserListener(RemoveActiveUserObserver removeActiveUserObserver) {
        UserClientHandler.getInstance().registerRemoveActiveUserListener(removeActiveUserObserver);
    }

    public void doLogoutAction() {
        base.removeAllChildren();
        userNodeList.clear();
        setUserChosen(null);
        tree.updateUI();
    }

    public void doLoginAction() {
        setRendererForTree();
    }

    public void registerLoginListener(LoginObserver loginObserver) {
        UserClientHandler.getInstance().registerLoginListener(loginObserver);
    }

    public void registerLogoutListener(LogoutObserver logoutObserver) {
        UserClientHandler.getInstance().registerLogoutListener(logoutObserver);
    }

    public void registerUserSelectedListener(UserSelectedObserver userSelectedObserver) {
        if (userSelectedObserver != null) selectUserObservers.add(userSelectedObserver);
    }

    private void informUserSelectedEvent() {
        Enumeration e = selectUserObservers.elements();
        while (e.hasMoreElements()) {
            ((UserSelectedObserver) e.nextElement()).doUserSelectedAction(getUserChosen());
        }
    }

    public void registerUserUnselectedListener(UserUnselectedObserver userUnselectedObserver) {
        if (userUnselectedObserver != null) unselectUserObservers.add(userUnselectedObserver);
    }

    private void informUserUnselectedEvent() {
        Enumeration e = unselectUserObservers.elements();
        while (e.hasMoreElements()) {
            ((UserUnselectedObserver) e.nextElement()).doUserUnselectedAction();
        }
    }

    public void doCleanupBeforeLogoutAction() {
    }

    public void registerCleanupBeforeLogoutListener(CleanupBeforeLogoutObserver cleanupBeforeLogoutObserver) {
        Client.getInstance().registerCleanupBeforeLogoutObserver(cleanupBeforeLogoutObserver);
    }

    public void registerSkinChangeListener(SkinChangeObserver skinchangeObserver) {
        Client.getInstance().registerSkinChangeObserver(skinchangeObserver);
    }

    public void doSkinChangeAction(Object object) {
        SkinBean bean = (SkinBean) object;
        this.setBackground(bean.getBackgroundColor());
        buttonScrollPane.setBackground(bean.getBackgroundColor());
        mainPanel.setBackground(bean.getBackgroundColor());
        treeView.setBackground(bean.getBackgroundColor());
        buttonPanel.setBackground(bean.getBackgroundColor());
    }

    /****************************************************************************
   *
   * node-choosing actions
   *
   ****************************************************************************/
    private void alertRootNodeChosen() {
        informUserUnselectedEvent();
    }

    private void alertUserNodeChosen() {
        informUserSelectedEvent();
    }

    public void doRootNodeChosenAction() {
    }

    public void doUserNodeChosenAction() {
    }
}

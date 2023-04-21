package com.googlecode.maratische.google;

import static com.googlecode.maratische.google.MainController.getMainController;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;
import be.lechtitseb.google.reader.api.model.exception.GoogleReaderException;
import com.googlecode.maratische.google.gui.CommandLogFrame;
import com.googlecode.maratische.google.gui.FeedNode;
import com.googlecode.maratische.google.gui.FeedTreeCellRenderer;
import com.googlecode.maratische.google.gui.InfWindowCheckNewFeeds;
import com.googlecode.maratische.google.gui.InfWindowShowMessage;
import com.googlecode.maratische.google.gui.ItemListModel;
import com.googlecode.maratische.google.gui.LabelForm;
import com.googlecode.maratische.google.gui.LabeledFeedNode;
import com.googlecode.maratische.google.gui.LogoutButton;
import com.googlecode.maratische.google.gui.MainSplitPage;
import com.googlecode.maratische.google.gui.PopupListenerDisplayPane;
import com.googlecode.maratische.google.gui.SettingsFrame;
import com.googlecode.maratische.google.gui.SourceCodeFrame;
import com.googlecode.maratische.google.gui.SpamForm;
import com.googlecode.maratische.google.listeners.MainFrameListener;
import com.googlecode.maratische.google.listeners.SpinnerListener;
import com.googlecode.maratische.google.model.Commands;
import com.googlecode.maratische.google.model.Constants;
import com.googlecode.maratische.google.model.Credentials;
import com.googlecode.maratische.google.model.Feed;
import com.googlecode.maratische.google.model.Item;
import com.googlecode.maratische.google.model.Label;
import com.googlecode.maratische.google.model.Property;
import com.googlecode.maratische.google.model.Spam;

public class MainFrame extends JFrame implements MainFrameListener, SpinnerListener {

    static Logger logger = Logger.getLogger(MainFrame.class.getName());

    private JFrame fullScreenWindow = null;

    private KeyAdapter cursorKeyAdapter = null;

    private PopupListenerDisplayPane popupListener = null;

    public MainFrame(Exception e) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        showError(e);
    }

    public MainFrame() throws SQLException, BaseException {
        setTitle("rssReader 1.0-b20111103");
        setIconImage(new ImageIcon(getClass().getResource("/feed_disk.png")).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    getMainController().checkProperty(MainFrame.this.getSize(), MainFrame.this.getMainPaneDividerLocation(), MainFrame.this.getRightPaneDividerLocation());
                } catch (Exception e1) {
                    System.out.println(e1);
                }
                System.exit(0);
            }
        });
        Property propertyWidth = null;
        Property propertyHeight = null;
        propertyWidth = getDaoAction().getPropertyDao().getPropertyByType(Property.PROPERTY_FORM_WIDTH);
        propertyHeight = getDaoAction().getPropertyDao().getPropertyByType(Property.PROPERTY_FORM_HEIGHT);
        setSize(propertyWidth.getInteger1(), propertyHeight.getInteger1());
        getMainController().addObserverListener((MainFrameListener) this);
        getMainController().addObserverListener((SpinnerListener) this);
        Property propertymainDivided = null;
        propertymainDivided = getDaoAction().getPropertyDao().getPropertyByType(Property.PROPERTY_MAINPANE_DIVIDER);
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        Box boxToolBar = Box.createHorizontalBox();
        JButton btnStart = new JButton(getBundleMessage("button.start"));
        boxToolBar.add(btnStart);
        boxToolBar.add(Box.createHorizontalStrut(10));
        btnStart.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                startSync();
            }
        });
        offlineButton = new JButton("");
        offlineButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    getMainController().changeOfflineStatus();
                } catch (Exception e1) {
                    showError(e1);
                }
            }
        });
        updateOfflineButton();
        boxToolBar.add(offlineButton);
        boxToolBar.add(Box.createHorizontalStrut(10));
        labelSpinner = new JLabel();
        labelSpinner.setToolTipText(getBundleMessage("labelSpinner.tooltip"));
        labelSpinner.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                commandLogFrame = new CommandLogFrame();
            }
        });
        spinnerDisable();
        boxToolBar.add(labelSpinner);
        boxToolBar.add(Box.createHorizontalGlue());
        boxToolBar.add(Box.createHorizontalStrut(10));
        JButton spamButton = new JButton("Spam");
        spamButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (getMainController().getSelectedItem() != null) {
                        getMainController().showSpanFormForSelectedItem();
                    } else {
                        SettingsFrame settingsForm = new SettingsFrame(MainFrame.this, 1);
                        settingsForm.setVisible(true);
                    }
                } catch (Exception e1) {
                    showError("Ошибка показал Помощи", e1);
                }
            }
        });
        boxToolBar.add(spamButton);
        boxToolBar.add(Box.createHorizontalStrut(10));
        JButton helpButton = new JButton("Help");
        helpButton.setIcon(new ImageIcon(getClass().getResource("/help.png")));
        helpButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    getMainController().showHelp();
                } catch (Exception e1) {
                    showError("Ошибка показал Помощи", e1);
                }
            }
        });
        boxToolBar.add(helpButton);
        boxToolBar.add(Box.createHorizontalStrut(10));
        JButton configButton = new JButton(getBundleMessage("button.settings"));
        configButton.setIcon(new ImageIcon(getClass().getResource("/settings.png")));
        configButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    SettingsFrame settingsForm = new SettingsFrame(MainFrame.this);
                    settingsForm.setVisible(true);
                } catch (Exception e1) {
                    showError(getBundleMessage("settings.error.view"), e1);
                }
            }
        });
        boxToolBar.add(configButton);
        LogoutButton logoutButton = new LogoutButton(getBundleMessage("button.logout"));
        logoutButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    getMainController().logout();
                } catch (Exception e1) {
                    showError(getBundleMessage("logout.error"), e1);
                }
            }
        });
        getMainController().addObserverListener(logoutButton);
        boxToolBar.add(Box.createHorizontalStrut(10));
        boxToolBar.add(logoutButton);
        boxToolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(boxToolBar);
        this.add(toolBar, BorderLayout.NORTH);
        cursorKeyAdapter = getKeyAdapter();
        popupListener = new PopupListenerDisplayPane();
        JTree feedsPanel = getFeedsPanel();
        mainPane = new MainSplitPage(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(feedsPanel), getItemsPanel(cursorKeyAdapter, popupListener));
        mainPane.setDividerLocation(propertymainDivided.getInteger1().intValue());
        add(mainPane, BorderLayout.CENTER);
        updateFeedMenu();
        CommandProcessor.createCommandProcessor(getDaoAction());
        CommandProcessor.getCommandProcessor().setOfflineStatus(getMainController().getOfflineStatus());
        Vector<Component> order = new Vector<Component>(3);
        order.add(feedsPanel);
        order.add(itemsPanel.getCurrentFocusComponent());
        order.add(panelView.getCurrentFocusComponent());
        this.setFocusTraversalPolicy(new MyFocusTraversalPolicy(order));
        btnStart.requestFocus();
        setVisible(true);
        initFullScreen(cursorKeyAdapter, popupListener);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                getMainController().afterStartMainFrame();
            }
        });
    }

    class MyFocusTraversalPolicy extends FocusTraversalPolicy {

        Vector<Component> order;

        public MyFocusTraversalPolicy(Vector<Component> order) {
            this.order = new Vector<Component>(order.size());
            this.order.addAll(order);
        }

        public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
            int idx = (order.indexOf(aComponent) + 1) % order.size();
            return order.get(idx);
        }

        public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
            int idx = order.indexOf(aComponent) - 1;
            if (idx < 0) {
                idx = order.size() - 1;
            }
            return order.get(idx);
        }

        public Component getDefaultComponent(Container focusCycleRoot) {
            return order.get(0);
        }

        public Component getLastComponent(Container focusCycleRoot) {
            return order.lastElement();
        }

        public Component getFirstComponent(Container focusCycleRoot) {
            return order.get(0);
        }
    }

    private void initFullScreen(KeyAdapter cursorKeyAdapter, PopupListenerDisplayPane popupListener) {
        fullScreenWindow = new JFrame();
        fullScreenWindow.setName(this.getTitle());
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fullScreenWindow.setSize(env.getMaximumWindowBounds().getSize());
        fullScreenWindow.setUndecorated(true);
        fullScreenWindow.setLayout(new BorderLayout());
        final DisplayPane displayPane = getDisplayPane(cursorKeyAdapter, popupListener);
        fullScreenWindow.add(displayPane, BorderLayout.CENTER);
        fullScreenWindow.setVisible(false);
        fullScreenWindow.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        displayPane.requestFocus();
                    }
                });
            }
        });
        fullScreenWindow.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
            }

            /**
			 * при закрытие окна каким нибудь неродным способом, необходимо изменить статус переменной
			 */
            public void windowClosed(WindowEvent e) {
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice device = env.getDefaultScreenDevice();
                device.setFullScreenWindow(null);
                if (getMainController().isFullScreen) {
                    getMainController().doFullScreenMode();
                }
            }

            public void windowActivated(WindowEvent e) {
            }
        });
    }

    public ItemListModel itemList = new ItemListModel();

    private ItemsPanelJList itemsPanel;

    private JLabel labelSpinner;

    private JButton offlineButton;

    private MainSplitPage mainPane = null;

    private MainSplitPage rightPane = null;

    @SuppressWarnings("unused")
    private CommandLogFrame commandLogFrame = null;

    private MainFrame mainFrame;

    public Label openLabelForm(Item selectedItem) {
        Point position = null;
        Dimension size = null;
        if (fullScreenWindow != null && fullScreenWindow.isVisible()) {
            position = fullScreenWindow.getLocationOnScreen();
            size = fullScreenWindow.getSize();
        } else {
            position = itemsPanel.getLocationOnScreen();
            size = itemsPanel.getVisibleRect().getSize();
        }
        infWindowShowLabeled = new InfWindowShowMessage(position, size, getBundleMessage("label.confimDialogAdded"), 3);
        Label label = new Label();
        label.setType(Label.LABEL_TYPE_READ);
        return label;
    }

    public void openSpamForm() {
        SpamForm labelForm = new SpamForm(this.mainFrame);
        labelForm.setVisible(true);
    }

    InfWindowShowMessage infWindowShowLabeled = null;

    public boolean openNotLabelForm(Item selectedItem) {
        Point position = null;
        Dimension size = null;
        if (fullScreenWindow != null && fullScreenWindow.isVisible()) {
            position = fullScreenWindow.getLocationOnScreen();
            size = fullScreenWindow.getSize();
        } else {
            position = itemsPanel.getLocationOnScreen();
            size = itemsPanel.getVisibleRect().getSize();
        }
        infWindowShowLabeled = new InfWindowShowMessage(position, size, getBundleMessage("label.confimDialogRemoved"), 3);
        return true;
    }

    public Credentials loginForm(String errorMessage) {
        LoginForm loginForm = new LoginForm(this.mainFrame, errorMessage);
        loginForm.setVisible(true);
        Credentials credentials = loginForm.getCredentials();
        loginForm = null;
        return credentials;
    }

    SourceCodeFrame sourceCodeFrame = null;

    public void viewSourceCodeFrame(String title, String description) {
        if (sourceCodeFrame == null) {
            sourceCodeFrame = new SourceCodeFrame();
        }
        sourceCodeFrame.setTitle(title);
        sourceCodeFrame.setDescription(description);
        sourceCodeFrame.setVisible(true);
    }

    public void doExpandItems(boolean expand) {
        itemsPanel.doExpandItems(expand);
        try {
            getMainController().selectFeedUpdate();
        } catch (Exception e) {
            getMainController().showError("error on doExpandItems", e);
        }
    }

    public void doExpandWindow() {
        mainPane.doExpandWindow();
        rightPane.doExpandWindow();
    }

    public void doFullScreenMode() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                fullScreenWindow.setVisible(getMainController().isFullScreen);
                MainFrame.this.setVisible(!getMainController().isFullScreen);
                if (getMainController().isFullScreen) {
                    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    GraphicsDevice device = env.getDefaultScreenDevice();
                    device.setFullScreenWindow(fullScreenWindow);
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            fullScreenWindow.requestFocus();
                        }
                    });
                }
            }
        });
    }

    public void showError(Exception e) {
        showError("", e);
    }

    /**
	 * Показываем ошибку, в случае если это ошибка типа SQLException, то
	 * показывается еще и следующая ошибка, в которой может быть немало
	 * интересной информации
	 * 
	 * @param e
	 */
    public void showError(String text, Exception e) {
        StringBuffer errorMessage = new StringBuffer(text);
        if (e != null) {
            errorMessage.append((e.getMessage() != null ? e.getMessage() : e)).append(" \n");
            if (e instanceof SQLException) {
                SQLException sqlException = (SQLException) e;
                if (sqlException != null && sqlException.getNextException() != null) {
                    errorMessage.append(sqlException.getNextException().getMessage()).append(" \n");
                }
            }
            if (e instanceof GoogleReaderException) {
                if ("be.lechtitseb.google.reader.api.model.exception.GoogleReaderException: Expected 200 OK. Received 401 Unauthorized".equals(e.getMessage())) {
                    errorMessage.append("Click Logout and try to Login again \n");
                }
            }
            StackTraceElement[] elements = e.getStackTrace();
            if (elements != null) {
                for (StackTraceElement element : elements) {
                    logger.error(element);
                }
            }
        }
        JOptionPane.showMessageDialog(this, errorMessage);
    }

    public void spinnerEnable() {
        labelSpinner.setIcon(new ImageIcon(getClass().getResource("/spinner.gif")));
        changeSpinnerValue();
    }

    private void changeSpinnerValue() {
        try {
            int size = getDaoAction().getCommandDao().sizeOfCommands();
            labelSpinner.setText("" + (size > 0 ? size : ""));
        } catch (SQLException e) {
            showError(e);
        }
    }

    public void spinnerDisable() {
        labelSpinner.setIcon(new ImageIcon(getClass().getResource("/spinner_stop.jpg")));
        changeSpinnerValue();
    }

    public void updateOfflineButton() throws BaseException {
        if (getMainController().getOfflineStatus()) {
            this.offlineButton.setText(getBundleMessage("button.online"));
        } else {
            this.offlineButton.setText(getBundleMessage("button.offline"));
        }
    }

    public void updateFeedMenu() {
        logger.info("updateFeedMenu");
        try {
            updateFeedMenu(rootFeed);
        } catch (Exception e) {
            showError(e);
        }
        feedsTree.updateUI();
    }

    public void updateItemsPanel() {
        this.itemsPanel.updateUI();
    }

    /**
	 * @param parentFeed
	 * @throws SQLException
	 */
    private void updateFeedMenu(FeedNode parentFeed) throws SQLException, BaseException {
        List<Feed> feeds = new ArrayList<Feed>();
        if (parentFeed.equals(rootFeed)) {
            Feed starredFeed = new Feed();
            starredFeed.setId(Constants.RSS_READER_LABELED_FEED);
            starredFeed.setName(getBundleMessage("label.rootName") + " [*]");
            starredFeed.setOfflineSupport(true);
            starredFeed.setParentId(rootFeed.getId());
            starredFeed.setUnreadItems(getDaoAction().getItemDao().countAllLabeled());
            starredFeed.setFeedDescriptorId("labeled");
            feeds.add(starredFeed);
        }
        feeds.addAll(getDaoAction().getFeedDao().findByParentId(parentFeed.getId()));
        if (feeds != null && feeds.size() > 0) {
            for (Feed feed : feeds) {
                boolean isNew = false;
                FeedNode feedNode = checkExistFeedInMenu(parentFeed, feed.getId());
                if (feedNode == null) {
                    feedNode = new FeedNode(feed);
                    isNew = true;
                } else {
                    feedNode.setFeed(feed);
                }
                updateFeedMenu(feedNode);
                if (getMainController().getFeedMenuViewAllStatus()) {
                    parentFeed.add(feedNode);
                } else {
                    if (feedNode.getUnreadItems() > 0 && isNew) {
                        parentFeed.add(feedNode);
                    } else if (feedNode.getUnreadItems() == 0 && !isNew) {
                        parentFeed.remove(feedNode);
                    }
                }
                if (feed.getId() == Constants.RSS_READER_LABELED_FEED) {
                    List<Feed> labeledFeeds = getDaoAction().getFeedDao().listLabeledFeeds();
                    for (Feed labeledFeed : labeledFeeds) {
                        labeledFeed.setUnreadItems(getDaoAction().getItemDao().countItemsByFeedIdLabeled(labeledFeed.getId()));
                        if (labeledFeed.getUnreadItems() > 0) {
                            FeedNode feedNode2 = checkExistFeedInMenu(feedNode, labeledFeed.getId());
                            if (feedNode2 == null) {
                                feedNode2 = new LabeledFeedNode(labeledFeed);
                                feedNode.add(feedNode2);
                            } else {
                                feedNode2.setFeed(labeledFeed);
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private FeedNode checkExistFeedInMenu(FeedNode parentFeed, Integer feedNodeId) {
        if (parentFeed.getChildCount() > 0) {
            Enumeration<DefaultMutableTreeNode> childs = parentFeed.children();
            while (childs.hasMoreElements()) {
                FeedNode child = (FeedNode) childs.nextElement();
                if (child != null) {
                    if (child.getId().equals(feedNodeId)) {
                        return child;
                    }
                }
            }
        }
        return null;
    }

    private void startSync() {
        try {
            CommandProcessor.getCommandProcessor().createCommand(Commands.UpdateMainMenu);
            CommandProcessor.getCommandProcessor().createCommand(Commands.UpdateBroadcastFriendsItems);
            CommandProcessor.getCommandProcessor().createCommand(Commands.UPDATE_STARRED_ITEMS);
        } catch (SQLException e1) {
            showError(e1);
        } catch (BaseException e) {
            showError(e);
        }
    }

    /**
	 * заменяем содержимое ленты новостей
	 * 
	 * @param items
	 */
    public void setItemsToItemList(List<Item> items) {
        this.itemList.clearItems();
        if (items != null) {
            itemList.addAll(items);
        }
    }

    public void itemsPanelClearSelection() {
        itemsPanel.clearSelection();
    }

    abstract class FeedNodeSelectorInThread implements Runnable {

        FeedNode selectedFeedNode;

        public FeedNodeSelectorInThread(FeedNode selectedFeedNode) {
            this.selectedFeedNode = selectedFeedNode;
        }
    }

    abstract class ItemSelectorInThread implements Runnable {

        Item selectedItem;

        public ItemSelectorInThread(Item selectedItem) {
            this.selectedItem = selectedItem;
        }
    }

    private FeedNode rootFeed = new FeedNode();

    private JTree feedsTree = new JTree(rootFeed);

    /**
	 * get left Feeds tree
	 * 
	 * @return
	 */
    private JTree getFeedsPanel() {
        feedsTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                FeedNode selectedNode = (FeedNode) e.getPath().getLastPathComponent();
                SwingUtilities.invokeLater(new FeedNodeSelectorInThread(selectedNode) {

                    public void run() {
                        try {
                            getMainController().selectFeed(this.selectedFeedNode);
                        } catch (Exception e) {
                            showError(e);
                        }
                    }
                });
            }
        });
        feedsTree.addMouseListener(new FeedsPopupMenuWithListener());
        feedsTree.setCellRenderer(new FeedTreeCellRenderer());
        return feedsTree;
    }

    private DaoAction getDaoAction() {
        return DaoAction.getDaoAction();
    }

    public int getMainPaneDividerLocation() {
        return mainPane.getDividerLocation();
    }

    public int getRightPaneDividerLocation() {
        return rightPane.getDividerLocation();
    }

    class FeedsPopupMenuWithListener extends MouseAdapter {

        JPopupMenu feedsPopup = null;

        JMenuItem menuItemOnOffline = null;

        JMenuItem menuItemOffOffline = null;

        JMenuItem menuItemViewNew = null;

        JMenuItem menuItemViewAll = null;

        public FeedsPopupMenuWithListener() {
            feedsPopup = new JPopupMenu();
            menuItemOnOffline = new JMenuItem(getBundleMessage("feedtree.onOflineMode"));
            feedsPopup.add(menuItemOnOffline);
            menuItemOnOffline.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    getMainController().changeOfflineSupportForSelectedFeed(Boolean.TRUE);
                }
            });
            menuItemOffOffline = new JMenuItem(getBundleMessage("feedtree.offOflineMode"));
            feedsPopup.add(menuItemOffOffline);
            menuItemOffOffline.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    getMainController().changeOfflineSupportForSelectedFeed(Boolean.FALSE);
                }
            });
            menuItemViewNew = new JMenuItem(getBundleMessage("feedtree.show.new"));
            feedsPopup.add(menuItemViewNew);
            menuItemViewNew.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        getMainController().setFeedMenuViewAllStatus(false);
                    } catch (SQLException e1) {
                        showError("Ошибка смены режима показов лент", e1);
                    }
                    updateFeedMenu();
                }
            });
            menuItemViewAll = new JMenuItem(getBundleMessage("feedtree.show.all"));
            feedsPopup.add(menuItemViewAll);
            menuItemViewAll.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        getMainController().setFeedMenuViewAllStatus(true);
                    } catch (SQLException e1) {
                        showError("Ошибка смены режима показов лент", e1);
                    }
                    updateFeedMenu();
                }
            });
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                getMainController().disableInfWindow();
                Feed selectedFeed = getMainController().getSelectedFeed();
                if (selectedFeed == null || selectedFeed.isFolder() || selectedFeed.getId() < 0) {
                    menuItemOnOffline.setEnabled(false);
                    menuItemOffOffline.setEnabled(false);
                } else {
                    boolean offlineSupport = false;
                    if (selectedFeed.getOfflineSupport() != null) {
                        offlineSupport = selectedFeed.getOfflineSupport();
                    }
                    menuItemOnOffline.setEnabled(!offlineSupport);
                    menuItemOffOffline.setEnabled(offlineSupport);
                }
                try {
                    menuItemViewAll.setEnabled(!getMainController().getFeedMenuViewAllStatus());
                    menuItemViewNew.setEnabled(getMainController().getFeedMenuViewAllStatus());
                } catch (BaseException e1) {
                    showError(e1);
                }
                feedsPopup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private JComponent getItemsPanel(KeyAdapter cursorKeyAdapter, PopupListenerDisplayPane popupListener) throws BaseException {
        itemsPanel = new ItemsPanelJList(this.itemList, popupListener);
        itemsPanel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    try {
                        getMainController().selectItem(itemList.getItem(((JList) e.getSource()).getSelectedIndex()));
                        int[] indices = ((JList) e.getSource()).getSelectedIndices();
                        if (indices.length > 1) {
                            List<Item> items = new ArrayList<Item>();
                            for (int index : indices) {
                                items.add(itemList.getItem(index));
                            }
                            getMainController().selectItems(items);
                        } else {
                            getMainController().selectItems(null);
                        }
                    } catch (Exception e1) {
                        showError(e1);
                    }
                }
            }
        });
        itemsPanel.addKeyListenerForList(cursorKeyAdapter);
        getMainController().addObserverListener(itemsPanel);
        Property propertyRightDivided = getDaoAction().getPropertyDao().getPropertyByType(Property.PROPERTY_RIGHTPANE_DIVIDER);
        rightPane = new MainSplitPage(JSplitPane.VERTICAL_SPLIT, itemsPanel, getDisplayPane(cursorKeyAdapter, popupListener));
        rightPane.setDividerLocation(propertyRightDivided.getInteger1().intValue());
        rightPane.setOneTouchExpandable(true);
        return rightPane;
    }

    private KeyAdapter getKeyAdapter() {
        KeyAdapter cursorKeyAdapter = new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getModifiersEx() == 128) {
                    switch(e.getKeyCode()) {
                        case 85:
                            getMainController().openSourceCodeSelectItem();
                            break;
                        case 38:
                            getMainController().fontSizeUp();
                            break;
                        case 40:
                            getMainController().fontSizeDown();
                            break;
                        case 10:
                            try {
                                getMainController().openInBrowserSelectItem();
                            } catch (Exception e1) {
                                getMainController().showError("Ошибка при открытии браузера", e1);
                            }
                            break;
                    }
                } else {
                    switch(e.getKeyCode()) {
                        case KeyEvent.VK_SPACE:
                            try {
                                getMainController().openLabelFormForSelectedItem();
                            } catch (SQLException e1) {
                                showError("Ошибка при создании метки", e1);
                            } catch (BaseException e1) {
                                showError("Ошибка при создании метки", e1);
                            }
                            break;
                        case KeyEvent.VK_1:
                            getMainController().doExpandItems(true);
                            break;
                        case KeyEvent.VK_2:
                            getMainController().doExpandItems(false);
                            break;
                        case 83:
                            getMainController().showSpanFormForSelectedItem();
                            break;
                        case KeyEvent.VK_U:
                            getMainController().doExpandWindow();
                            break;
                        case KeyEvent.VK_F11:
                            getMainController().doFullScreenMode();
                            break;
                        case KeyEvent.VK_LEFT:
                            getMainController().itemsListSelectLeft();
                            break;
                        case KeyEvent.VK_RIGHT:
                            getMainController().itemsListSelectRight();
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        return cursorKeyAdapter;
    }

    DisplayPane panelView = null;

    private DisplayPane getDisplayPane(KeyListener cursorKeyAdapter, PopupListenerDisplayPane popupListener) {
        panelView = new DisplayPane(popupListener);
        panelView.addKeyListener(cursorKeyAdapter);
        getMainController().addObserverListener(panelView);
        return panelView;
    }

    public DisplayPane getDisplayPane() {
        return this.panelView;
    }

    public String getBundleMessage(String message) {
        return getMainController().getBundleMessage(message);
    }

    private static final long serialVersionUID = 1159098014226477647L;
}

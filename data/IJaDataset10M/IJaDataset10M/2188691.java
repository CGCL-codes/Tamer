package coopnetclient.frames.clientframe;

import coopnetclient.protocol.out.Protocol;
import coopnetclient.enums.ContactListElementTypes;
import coopnetclient.frames.clientframe.TabOrganizer;
import coopnetclient.utils.Settings;
import coopnetclient.frames.components.ContactListPopupMenu;
import coopnetclient.frames.components.mutablelist.DefaultListCellEditor;
import coopnetclient.frames.models.ContactListModel;
import coopnetclient.frames.renderers.ContactListRenderer;
import coopnetclient.utils.ContactListFileDropHandler;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class QuickPanel extends javax.swing.JPanel {

    public static ImageIcon ContactListIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("data/icons/quicktab/contacts.png"));

    public static ImageIcon FavouritesIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("data/icons/quicktab/favourites.png"));

    private static ContactListPopupMenu popup;

    ContactListModel model;

    /** Creates new form PlayerListPanel */
    public QuickPanel() {
        initComponents();
    }

    public QuickPanel(ContactListModel model) {
        this.model = model;
        initComponents();
        lst_contactList.setModel(model);
        lst_contactList.setCellRenderer(new ContactListRenderer(model));
        lst_contactList.setListCellEditor(new DefaultListCellEditor(new JTextField()));
        lst_contactList.setDragEnabled(true);
        lst_contactList.setDropMode(DropMode.USE_SELECTION);
        lst_contactList.setTransferHandler(new ContactListFileDropHandler());
        popup = new ContactListPopupMenu(lst_contactList);
        lst_contactList.setComponentPopupMenu(popup);
        refreshFavourites();
        lst_favouritesList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void setTabAlignment(boolean left) {
        if (left) {
            tp_quickPanel.setTabPlacement(JTabbedPane.LEFT);
        } else {
            tp_quickPanel.setTabPlacement(JTabbedPane.RIGHT);
        }
    }

    public void refreshFavourites() {
        final Vector<String> favs = Settings.getFavourites();
        lst_favouritesList.setModel(new javax.swing.AbstractListModel() {

            Vector<String> strings = favs;

            @Override
            public int getSize() {
                return strings.size();
            }

            @Override
            public Object getElementAt(int i) {
                return strings.get(i);
            }
        });
        lst_favouritesList.repaint();
    }

    private void initComponents() {
        tp_quickPanel = new javax.swing.JTabbedPane();
        scrl_contactlist = new javax.swing.JScrollPane();
        lst_contactList = new coopnetclient.frames.components.mutablelist.EditableJlist();
        pnl_favouritesList = new javax.swing.JPanel();
        scrl_favouritesList = new javax.swing.JScrollPane();
        lst_favouritesList = new javax.swing.JList();
        setFocusable(false);
        setPreferredSize(new java.awt.Dimension(200, 200));
        tp_quickPanel.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tp_quickPanel.setDoubleBuffered(true);
        tp_quickPanel.setFocusable(false);
        tp_quickPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        scrl_contactlist.setMinimumSize(new java.awt.Dimension(0, 0));
        lst_contactList.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        lst_contactList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lst_contactList.setFixedCellHeight(20);
        lst_contactList.setFocusable(false);
        lst_contactList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lst_contactListMouseClicked(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                lst_contactListMouseExited(evt);
            }
        });
        lst_contactList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lst_contactListMouseMoved(evt);
            }
        });
        scrl_contactlist.setViewportView(lst_contactList);
        tp_quickPanel.addTab("", ContactListIcon, scrl_contactlist);
        lst_favouritesList.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        lst_favouritesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lst_favouritesList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lst_favouritesListMouseClicked(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                lst_favouritesListMouseExited(evt);
            }
        });
        lst_favouritesList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lst_favouritesListMouseMoved(evt);
            }
        });
        scrl_favouritesList.setViewportView(lst_favouritesList);
        javax.swing.GroupLayout pnl_favouritesListLayout = new javax.swing.GroupLayout(pnl_favouritesList);
        pnl_favouritesList.setLayout(pnl_favouritesListLayout);
        pnl_favouritesListLayout.setHorizontalGroup(pnl_favouritesListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(scrl_favouritesList, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE));
        pnl_favouritesListLayout.setVerticalGroup(pnl_favouritesListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(scrl_favouritesList, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE));
        tp_quickPanel.addTab("", FavouritesIcon, pnl_favouritesList);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(tp_quickPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(tp_quickPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE));
    }

    private void lst_contactListMouseClicked(java.awt.event.MouseEvent evt) {
        if (!popup.isVisible()) {
            lst_contactList.setSelectedIndex(lst_contactList.locationToIndex(evt.getPoint()));
        }
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 1 && lst_contactList.getSelectedIndex() > -1) {
            if (popup.isClosing()) {
                return;
            }
            if (lst_contactList.getSelectedValue() == null) {
                return;
            }
            String selected = lst_contactList.getSelectedValue() + "";
            if (model.getGroupNames().contains(selected)) {
                model.toggleGroupClosedStatus(selected);
            }
        } else {
            if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2 && lst_contactList.getSelectedIndex() > -1) {
                if (lst_contactList.getSelectedValue() == null) {
                    return;
                }
                String selected = lst_contactList.getSelectedValue() + "";
                if (model.getStatus(selected) != ContactListElementTypes.OFFLINE && model.getStatus(selected) != ContactListElementTypes.GROUPNAME_OPEN && model.getStatus(selected) != ContactListElementTypes.GROUPNAME_CLOSED) {
                    TabOrganizer.openPrivateChatPanel(selected, true);
                }
            }
        }
    }

    private void lst_favouritesListMouseClicked(java.awt.event.MouseEvent evt) {
        if (!popup.isVisible()) {
            lst_favouritesList.setSelectedIndex(lst_favouritesList.locationToIndex(evt.getPoint()));
        }
        if (evt.getButton() == MouseEvent.BUTTON1) {
            Protocol.joinChannel(lst_favouritesList.getSelectedValue().toString());
        }
    }

    private void lst_contactListMouseMoved(java.awt.event.MouseEvent evt) {
        if (!popup.isVisible()) {
            int idx = lst_contactList.locationToIndex(evt.getPoint());
            Rectangle rec = lst_contactList.getCellBounds(idx, idx);
            if (rec == null) {
                return;
            }
            if (!rec.contains(evt.getPoint())) {
                lst_contactList.clearSelection();
                return;
            }
            if (idx == lst_contactList.getSelectedIndex()) {
                return;
            }
            String selected = lst_contactList.getModel().getElementAt(idx).toString();
            if (selected != null && selected.length() > 0) {
                lst_contactList.setSelectedIndex(idx);
            } else {
                lst_contactList.clearSelection();
            }
        }
    }

    private void lst_favouritesListMouseMoved(java.awt.event.MouseEvent evt) {
        if (!popup.isVisible()) {
            int idx = lst_favouritesList.locationToIndex(evt.getPoint());
            if (!lst_favouritesList.getCellBounds(idx, idx).contains(evt.getPoint())) {
                lst_favouritesList.clearSelection();
                return;
            }
            if (idx == lst_favouritesList.getSelectedIndex()) {
                return;
            }
            String selected = lst_favouritesList.getModel().getElementAt(idx).toString();
            if (selected != null && selected.length() > 0) {
                lst_favouritesList.setSelectedIndex(idx);
            } else {
                lst_favouritesList.clearSelection();
            }
        }
    }

    private void lst_contactListMouseExited(java.awt.event.MouseEvent evt) {
        if (!popup.isVisible()) {
            lst_contactList.clearSelection();
        }
    }

    private void lst_favouritesListMouseExited(java.awt.event.MouseEvent evt) {
        lst_favouritesList.clearSelection();
    }

    private coopnetclient.frames.components.mutablelist.EditableJlist lst_contactList;

    private javax.swing.JList lst_favouritesList;

    private javax.swing.JPanel pnl_favouritesList;

    private javax.swing.JScrollPane scrl_contactlist;

    private javax.swing.JScrollPane scrl_favouritesList;

    private javax.swing.JTabbedPane tp_quickPanel;
}

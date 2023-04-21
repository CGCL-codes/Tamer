package net.java.sip.communicator.gui.imp;

import javax.swing.*;
import java.awt.*;
import net.java.sip.communicator.common.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.io.File;

/**
 * <p>Title: SIP Communicator</p>
 * <p>Description: A SIP UA</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Network Research Team, Louis Pasteur University</p>
 * @author Emil Ivov
 * @version 1.0
 */
public class ContactListFrame extends JFrame implements MouseListener {

    private static final Console console = Console.getConsole(ContactListFrame.class);

    JPanel contentPane = new JPanel();

    BorderLayout borderLayout1 = new BorderLayout();

    ContactsTree contactsTree = null;

    JScrollPane treeScrollPane = new JScrollPane();

    BorderLayout borderLayout2 = new BorderLayout();

    JComboBox statusComboBox = new JComboBox();

    public MenuBar menuBar = new MenuBar();

    public ContactListPopupMenu popupMenu = new ContactListPopupMenu();

    private static final int DEFAULT_WIDTH = 150;

    private static final int DEFAULT_HEIGHT = 500;

    private ContactListActions contactListActions = new ContactListActions();

    public ContactListFrame() {
        super("Arsenal SIP Communicator");
        setIconImage(new ImageIcon("." + File.separator + "images" + File.separator + "arsenal" + File.separator + "ico.gif").getImage());
        try {
            jbInit();
            initComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        addWindowListener(new FrameSizeSaver());
        addMouseListener(this);
        treeScrollPane.addMouseListener(this);
        popupMenu.setContactListActions(contactListActions);
        contactListActions.setApplicationFrame(this);
        int x = -1;
        int y = -1;
        int width = DEFAULT_WIDTH;
        int height = DEFAULT_HEIGHT;
        try {
            x = Integer.parseInt(Utils.getProperty("net.java.sip.communicator.gui.imp.CONTACT_LIST_X"));
        } catch (NumberFormatException ex) {
            console.debug("Failed to parse CONTACT_LIST_X default value (" + x + ") will be used");
        }
        try {
            y = Integer.parseInt(Utils.getProperty("net.java.sip.communicator.gui.imp.CONTACT_LIST_Y"));
        } catch (NumberFormatException ex) {
            console.debug("Failed to parse CONTACT_LIST_Y default value (" + y + ") will be used");
        }
        try {
            width = Integer.parseInt(Utils.getProperty("net.java.sip.communicator.gui.imp.CONTACT_LIST_WIDTH"));
        } catch (NumberFormatException ex) {
            console.debug("Failed to parse CONTACT_LIST_WIDTH default value (" + width + ") will be used");
        }
        try {
            height = Integer.parseInt(Utils.getProperty("net.java.sip.communicator.gui.imp.CONTACT_LIST_HEIGHT"));
        } catch (NumberFormatException ex) {
            console.debug("Failed to parse CONTACT_LIST_HEIGHT default value (" + height + ") will be used");
        }
        setSize(width, height);
        if (x == -1) x = ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2;
        if (y == -1) y = ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2;
        setLocation(x, y);
        setIconImage(new ImageIcon(Utils.getResource("sip-communicator-16x16.jpg")).getImage());
    }

    private void expandContactsTree() {
        for (int i = 0; i < contactsTree.getRowCount(); i++) contactsTree.expandRow(i);
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        contentPane.setLayout(borderLayout2);
        this.setJMenuBar(menuBar);
        this.getContentPane().add(contentPane, BorderLayout.CENTER);
        contentPane.add(treeScrollPane, BorderLayout.CENTER);
        this.getContentPane().add(statusComboBox, BorderLayout.SOUTH);
    }

    public void setModel(ContactListModel model) {
        contactsTree = new ContactsTree();
        contactsTree.setModel(model);
        contactsTree.setCellRenderer(model);
        contactsTree.addMouseListener(this);
        treeScrollPane.setViewportView(contactsTree);
        treeScrollPane.updateUI();
        expandContactsTree();
    }

    /**
     * Set a set of strings representing a supported status set.
     * @param iter an iterator over strings representing a status set.
     */
    public void setStatusControllerModel(PresenceStatusControllerUIModel model) {
        statusComboBox.setModel(model);
        statusComboBox.setRenderer(model);
    }

    private class FrameSizeSaver extends WindowAdapter {

        public void windowClosing(WindowEvent evt) {
            Utils.setProperty("net.java.sip.communicator.gui.imp.CONTACT_LIST_X", String.valueOf(getX()));
            Utils.setProperty("net.java.sip.communicator.gui.imp.CONTACT_LIST_Y", String.valueOf(getY()));
            Utils.setProperty("net.java.sip.communicator.gui.imp.CONTACT_LIST_WIDTH", String.valueOf(getWidth()));
            Utils.setProperty("net.java.sip.communicator.gui.imp.CONTACT_LIST_HEIGHT", String.valueOf(getHeight()));
            PropertiesDepot.storeProperties();
        }
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a
     * component.
     *
     * @param e MouseEvent
     */
    public void mouseClicked(MouseEvent e) {
        maybeShowPopup(e);
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e MouseEvent
     */
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e MouseEvent
     */
    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e MouseEvent
     */
    public void mouseEntered(MouseEvent e) {
        maybeShowPopup(e);
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e MouseEvent
     */
    public void mouseExited(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            Component invoker = (Component) e.getSource();
            if (invoker instanceof ContactsTree) {
                ContactsTree tree = (ContactsTree) invoker;
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if (path != null && path.getPathCount() > 0) {
                    tree.setSelectionPath(path);
                }
            }
            popupMenu.show(invoker, e.getX(), e.getY());
        }
    }

    /**
     * Returns the path to the first currently selected node.
     * @return an array of objects representing the path to the first currently
     * selected node.
     */
    Object[] getSelectedPath() {
        return contactsTree.getSelectionPath().getPath();
    }

    /**
     * Returns a path containing all parents of the currently selected node
     * except the node itself.
     * @return an array of objects containing all parents of the currently
     * selected node except the node itself.
     */
    Object[] getSelectedParentPath() {
        return contactsTree.getSelectionPath().getParentPath().getPath();
    }

    /**
     * Transmits the given request to the contacts tree module.
     * @param request the request describing the contact that the user requested
     * to add.
     */
    void requestContactAddition(ContactAdditionRequest request) {
        ((ContactListModel) this.contactsTree.getModel()).requestContactAddition(request);
    }

    /**
     * Transmits the given request to the contacts tree module.
     * @param request the request describing the contact that the user requested
     * to remove.
     */
    void requestContactRemoval(ContactRemovalRequest request) {
        ((ContactListModel) this.contactsTree.getModel()).requestContactRemoval(request);
    }
}

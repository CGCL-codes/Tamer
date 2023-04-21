package net.sf.mailsomething.gui.mail;

import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import net.sf.mailsomething.gui.GridLayout2;
import net.sf.mailsomething.gui.GuiUser;
import net.sf.mailsomething.gui.TableSorter;
import net.sf.mailsomething.gui.drag.MailAddressTransfer;
import net.sf.mailsomething.gui.event.EscapeDialogAction;
import net.sf.mailsomething.mail.AddressList;
import net.sf.mailsomething.mail.AddressBook;
import net.sf.mailsomething.mail.MailAddress;
import net.sf.mailsomething.gui.i18n.PropertyManager;

/**
 * @author Administrator
 * @created 25-03-2003
 * 
 */
public class AddressListViewer extends JDialog implements DragGestureListener, DragSourceListener {

    private AddressBook book;

    private AddressNameComboModel model;

    private JComboBox addressCombo;

    private JTable table;

    private JButton okbutton, cancelbutton;

    private boolean cancelpressed = false;

    private JPanel content;

    private DragSource dragSource;

    private JButton[] buttons;

    public static int ADD_ADDRESS_VIEW = 1;

    public static int ADDRESS_CONFIG_VIEW = 0;

    private int viewType = 0;

    public AddressListViewer(AddressBook book) {
        this(book, PropertyManager.getString("address.viewer.label"));
    }

    public AddressListViewer(AddressBook book, String description) {
        this(GuiUser.getFrame(), book, description);
    }

    public AddressListViewer(AddressBook book, String description, int viewType) {
        this(GuiUser.getFrame(), book, description, viewType);
    }

    public AddressListViewer(Frame frame, AddressBook book, String description) {
        this(frame, book, description, ADDRESS_CONFIG_VIEW);
    }

    public AddressListViewer(Frame frame, AddressBook book, String description, int viewType) {
        super(frame, false);
        this.book = book;
        getContentPane().setLayout(new BorderLayout());
        model = new AddressNameComboModel(book);
        JComboBox addressCombo = new JComboBox(model);
        ComboActionListener comboActionListener = new ComboActionListener();
        addressCombo.addActionListener(comboActionListener);
        JPanel panel1 = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea(description);
        area.setEditable(false);
        area.setBackground(panel1.getBackground());
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("", Font.PLAIN, 11));
        panel1.add(area, BorderLayout.CENTER);
        panel1.add(addressCombo, BorderLayout.NORTH);
        table = new JTable();
        TableSorter sorter = new TableSorter(new TableModel(book.getDefaultAddressList()));
        table.setModel(sorter);
        table.setTableHeader(new NoHeader());
        table.setShowGrid(false);
        table.addMouseListener(new MouseListener());
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        sorter.sortByColumn(0);
        JScrollPane panel5 = new JScrollPane(table);
        panel5.getViewport().setBackground(Color.WHITE);
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel = new JPanel(new GridLayout2(5, 1, 5, 4));
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 30, 4));
        ActionListener listener = new ActionHandler();
        buildRightButtonPanels(viewType, buttonPanel, bottomPanel);
        addButtonListeners(listener);
        buttonPanel.setBorder(new EmptyBorder(20, 10, 10, 10));
        bottomPanel.setBorder(new EmptyBorder(10, 20, 0, 20));
        panel2.add(buttonPanel);
        content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel1.setBorder(new EmptyBorder(5, 0, 10, 150));
        content.add(panel1, BorderLayout.NORTH);
        content.add(panel5, BorderLayout.CENTER);
        content.add(panel2, BorderLayout.EAST);
        content.add(bottomPanel, BorderLayout.SOUTH);
        content.setPreferredSize(new Dimension(510, 330));
        getContentPane().add(content);
        EscapeDialogAction action = new EscapeDialogAction(table, this);
        EscapeDialogAction action1 = new EscapeDialogAction(content, this);
        dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(table, DnDConstants.ACTION_MOVE, this);
    }

    public void buildRightButtonPanels(int viewType, JPanel buttonPanel, JPanel bottomPanel) {
        String[] bottomlabels = { PropertyManager.getString("common.ok"), PropertyManager.getString("common.cancel") };
        int counter = 0;
        if (viewType == ADDRESS_CONFIG_VIEW) {
            String[] rightlabels = { PropertyManager.getString("address.viewer.configure"), PropertyManager.getString("address.viewer.new"), PropertyManager.getString("address.viewer.edit"), PropertyManager.getString("address.viewer.remove"), PropertyManager.getString("address.viewer.addto") };
            buttons = new JButton[rightlabels.length + bottomlabels.length];
            for (int i = 0; i < rightlabels.length; i++) {
                buttons[i] = new JButton(rightlabels[i]);
                buttonPanel.add(buttons[i]);
            }
            counter = rightlabels.length;
        } else {
            String[] rightlabels = { PropertyManager.getString("address.viewer.addAddress"), PropertyManager.getString("address.viewer.addAll"), PropertyManager.getString("address.viewer.clear") };
            buttons = new JButton[rightlabels.length + bottomlabels.length];
            for (int i = 0; i < rightlabels.length; i++) {
                buttons[i] = new JButton(rightlabels[i]);
                buttonPanel.add(buttons[i]);
            }
            counter = rightlabels.length;
        }
        for (int i = 0; i < bottomlabels.length; i++) {
            buttons[counter + i] = new JButton(bottomlabels[i]);
            bottomPanel.add(buttons[counter + i]);
        }
    }

    public void addButtonListeners(ActionListener listener) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(listener);
        }
    }

    class ComboActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String name = (String) model.getSelectedItem();
            AddressList alist = book.getAddressList(name);
            if (alist != null) table.setModel(new TableSorter(new TableModel(alist)));
        }
    }

    class ActionHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().trim().equals(PropertyManager.getString("address.viewer.edit"))) {
                showMailAddressDialog(table.getSelectedRow());
            } else if (e.getActionCommand().trim().equals(PropertyManager.getString("common.cancel"))) {
                cancelpressed = true;
                hide();
                dispose();
            } else if (e.getActionCommand().trim().equals(PropertyManager.getString("address.viewer.remove"))) {
                int row[] = table.getSelectedRows();
                String[] alias = new String[row.length];
                String[] email = new String[row.length];
                for (int j = 0; j < row.length; j++) {
                    alias[j] = (String) table.getModel().getValueAt(row[j], 0);
                    email[j] = (String) table.getModel().getValueAt(row[j], 1);
                }
                String selectedName = (String) model.getSelectedItem();
                for (int j = 0; j < row.length; j++) {
                    if (alias[j] == null || email[j] == null) continue;
                    MailAddress address = null;
                    if (selectedName.equals(AddressList.DEFAULT_NAME)) {
                        AddressList list = book.getDefaultAddressList();
                        address = list.getMatchingAddress(alias[j], email[j]);
                        if (address != null) {
                            int res = JOptionPane.showConfirmDialog(AddressListViewer.this, PropertyManager.getString("address.viewer.confirm.delete") + address.getAlias() + " <" + address.getEmail() + ">\n" + PropertyManager.getString("address.viewer.confirm.book"), PropertyManager.getString("address.viewer.confirm"), JOptionPane.YES_NO_OPTION);
                            if (res == JOptionPane.YES_OPTION) {
                                list.removeMailAddress(address);
                            }
                            if (!book.getBannedList().exists(address.getEmail())) book.getBannedList().addMailAddress(address);
                        }
                    } else {
                        AddressList alist = book.getAddressList(selectedName);
                        address = alist.getMatchingAddress(alias[j], email[j]);
                        if (address != null) {
                            int res = JOptionPane.showConfirmDialog(AddressListViewer.this, PropertyManager.getString("address.viewer.confirm.delete") + address.getAlias() + " <" + address.getEmail() + ">\n" + PropertyManager.getString("address.viewer.confirm.list") + " " + alist.getName(), PropertyManager.getString("address.viewer.confirm"), JOptionPane.YES_NO_OPTION);
                            if (res == JOptionPane.YES_OPTION) alist.removeMailAddress(address);
                        }
                    }
                }
                if (row.length > 0) {
                    TableModelEvent event = new TableModelEvent(table.getModel(), row[0], table.getModel().getRowCount());
                    ((TableSorter) table.getModel()).tableChanged(event);
                    ((TableSorter) table.getModel()).sortByColumn(0);
                }
            } else if (e.getActionCommand().trim().equals(PropertyManager.getString("address.viewer.new"))) {
                final MailAddress address = new MailAddress();
                String name = (String) model.getSelectedItem();
                final AddressList list = book.getAddressList(name);
                final AddressList main = book.getDefaultAddressList();
                PropertyDialog dialog = new PropertyDialog(AddressListViewer.this, address, book, PropertyManager.getString("address.property.new"), PropertyDialog.NEW_ADDRESS);
                dialog.addOkListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (main != null && !main.equals(list)) main.addMailAddress(address);
                        list.addMailAddress(address);
                        TableModelEvent f = new TableModelEvent(table.getModel());
                        ((TableSorter) table.getModel()).tableChanged(f);
                        ((TableSorter) table.getModel()).sortByColumn(0);
                    }
                });
                GuiUser.showDialog(dialog, null);
            } else if (e.getActionCommand().trim().equals(PropertyManager.getString("address.viewer.addto"))) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(AddressListViewer.this, PropertyManager.getString("address.viewer.select"), PropertyManager.getString("address.viewer.select.title"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String alias = (String) table.getModel().getValueAt(row, 0);
                String email = (String) table.getModel().getValueAt(row, 1);
                AddressList list = book.getDefaultAddressList();
                MailAddress[] address = list.matchAlias(alias);
                for (int i = 0; i < address.length; i++) {
                    if (address[i].getEmail().equals(email)) {
                        PropertyDialog dialog = new PropertyDialog(AddressListViewer.this, address[i], book, PropertyManager.getString("address.property.addto"), PropertyDialog.CHANGE_LIST);
                        GuiUser.showDialog(dialog, null);
                        TableModelEvent f = new TableModelEvent(table.getModel());
                        ((TableSorter) table.getModel()).tableChanged(f);
                        ((TableSorter) table.getModel()).sortByColumn(0);
                    }
                }
            } else if (e.getActionCommand().trim().equals(PropertyManager.getString("address.viewer.configure"))) {
                AddressListConfig dialog = new AddressListConfig(GuiUser.getFrame(), model);
                GuiUser.showDialog(dialog, null);
            } else if (e.getActionCommand().trim().equals(PropertyManager.getString("common.ok"))) {
                hide();
                dispose();
            }
        }
    }

    /**
	 * Returns the contents of a selected AddressList
	 * @return MailAddress[]
	 */
    public MailAddress[] getContentsAddressList() {
        if (cancelpressed) return null;
        String name = (String) model.getSelectedItem();
        if (name == null) return null;
        AddressList list = book.getAddressList(name);
        MailAddress[] addresses = list.getAddresses();
        return addresses;
    }

    /**
	 * Returnss the selected MailAddresses
	 * @return MailAddress[]
	 */
    public MailAddress[] getSelectedAddresses() {
        if (cancelpressed) return null;
        if (table.getSelectedRow() == -1) return null;
        MailAddress[] addresses = new MailAddress[table.getSelectedRowCount()];
        String name = (String) model.getSelectedItem();
        AddressList list = book.getAddressList(name);
        int[] rows = table.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            String alias = (String) table.getModel().getValueAt(rows[i], 0);
            String email = (String) table.getModel().getValueAt(rows[i], 1);
            addresses[i] = list.getMatchingAddress(alias, email);
        }
        return addresses;
    }

    /**
	 * Returns the selected mailaddress. Returns null if cancel is
	 * pressed or ok is pressed and theres no selection.
	 * 
	 * 
	 */
    public MailAddress getSelectedAddress() {
        if (cancelpressed) return null;
        if (table.getSelectedRow() == -1) return null;
        String alias = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);
        String email = (String) table.getModel().getValueAt(table.getSelectedRow(), 1);
        String name = (String) model.getSelectedItem();
        AddressList list = book.getAddressList(name);
        MailAddress address = list.getMatchingAddress(alias, email);
        return address;
    }

    class TableModel extends AbstractTableModel {

        AddressList list;

        public TableModel(AddressList list) {
            this.list = list;
        }

        public int getRowCount() {
            return list.getAddresses().length;
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int row, int column) {
            if (column == 0) return list.getAddresses()[row].getAlias();
            if (column == 1) return list.getAddresses()[row].getEmail();
            return "";
        }

        public String getColumnName(int column) {
            if (column == 0) return "Alias";
            if (column == 1) return "Email-address";
            return "";
        }
    }

    class NoHeader extends JTableHeader {

        public Dimension getPreferredSize() {
            return new Dimension(0, 0);
        }

        public Dimension getSize() {
            return new Dimension(0, 0);
        }
    }

    public void addOkListener(ActionListener listener) {
        if (okbutton != null) okbutton.addActionListener(listener);
    }

    public void addCancelListener(ActionListener listener) {
        if (cancelbutton != null) cancelbutton.addActionListener(listener);
    }

    /**
	 * Method for setting another buttonpanel to the right. That will
	 * remove the default buttons. 
	 * 
	 * 
	 */
    public void setRightButtonPanel(JPanel panel) {
        content.add(panel, BorderLayout.EAST);
    }

    public void disableComboBox() {
        addressCombo.setEnabled(false);
    }

    public void allowMultipleSelection(boolean allow) {
        if (allow) table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); else table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
	 * Method showMailAddressDialog. Method to show the mailaddress dialog, 
	 * which is simply a dialog containing 2 textfields with the alias and the
	 * email of the selected mailaddress.
	 * 
	 * @param row
	 */
    public void showMailAddressDialog(int row) {
        if (row == -1) return;
        String alias = (String) table.getModel().getValueAt(row, 0);
        String email = (String) table.getModel().getValueAt(row, 1);
        AddressList list = book.getDefaultAddressList();
        MailAddress[] address = list.matchAlias(alias);
        for (int i = 0; i < address.length; i++) {
            if (address[i].getEmail().equals(email)) {
                PropertyDialog dialog = new PropertyDialog(AddressListViewer.this, address[i], book, PropertyManager.getString("address.property.edit"), PropertyDialog.CHANGE_ADDRESS);
                final int index = i;
                dialog.addOkListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        TableModelEvent f = new TableModelEvent(table.getModel(), index);
                        ((TableSorter) table.getModel()).tableChanged(f);
                        ((TableSorter) table.getModel()).sortByColumn(0);
                    }
                });
                GuiUser.showDialog(dialog, null);
                break;
            }
        }
    }

    class MouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                showMailAddressDialog(table.getSelectedRow());
            }
        }
    }

    public void dragGestureRecognized(DragGestureEvent event) {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int index = ((TableSorter) table.getModel()).getIndexAt(row);
        AddressList list = book.getDefaultAddressList();
        MailAddressTransfer transfer = new MailAddressTransfer(list.getAddresses()[index]);
        dragSource.startDrag(event, null, transfer, this);
        JRootPane pane = getRootPane();
    }

    public void dragDropEnd(DragSourceDropEvent e) {
    }

    public void dragEnter(DragSourceDragEvent e) {
    }

    public void dragExit(DragSourceEvent e) {
    }

    public void dragOver(DragSourceDragEvent e) {
    }

    public void dropActionChanged(DragSourceDragEvent e) {
    }
}

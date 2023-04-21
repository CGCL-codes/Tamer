package net.mjrz.fm.ui.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.mjrz.fm.actions.ActionResponse;
import net.mjrz.fm.entity.FManEntityManager;
import net.mjrz.fm.entity.beans.*;
import net.mjrz.fm.ui.FinanceManagerUI;
import net.mjrz.fm.ui.utils.ContactsTableModel;
import net.mjrz.fm.ui.utils.SortableTableModel;
import net.mjrz.fm.ui.utils.TableHeaderRenderer;
import net.mjrz.fm.ui.utils.TransactionTableModel;
import net.mjrz.fm.ui.utils.UIDefaults;
import net.mjrz.fm.utils.Messages;

/**
 * @author Mjrz contact@mjrz.net
 * 
 */
public class ViewContactsDialog extends JFrame {

    private static final long serialVersionUID = 4101591120488514875L;

    private User user;

    private JTable contactsTable;

    private ContactsTableModel tableModel;

    private FManEntityManager em;

    private Component parent;

    private TableRowSorter<TableModel> rowSorter;

    private JToolBar toolBar;

    private JLabel statusLbl, msgLbl;

    private static ViewContactsDialog instance = null;

    public static boolean isInited() {
        return instance != null;
    }

    public static synchronized ViewContactsDialog getInstance(Component parent, User user) {
        if (instance == null) {
            instance = new ViewContactsDialog(parent, user);
        }
        return instance;
    }

    public static synchronized void disposeInstance() {
        if (instance == null) return;
        instance.dispose();
        instance = null;
    }

    private ViewContactsDialog() {
    }

    private ViewContactsDialog(Component parent, User user) {
        super(UIDefaults.ADDRESSBOOK_MANAGER_TITLE);
        this.parent = parent;
        this.user = user;
        em = new FManEntityManager();
        initialize();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        toFront();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        loadContacts();
        toolBar = new JToolBar(Messages.getString("Contacts toolbar"), JToolBar.VERTICAL);
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));
        toolBar.setFloatable(false);
        loadButtons();
        contactsTable = new JTable(tableModel) {

            private static final long serialVersionUID = 1L;

            public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                if (isCellSelected(rowIndex, vColIndex)) {
                    c.setBackground(UIDefaults.DEFAULT_TABLE_ROW_SEL_COLOR);
                    return c;
                }
                if (rowIndex % 2 == 0) {
                    c.setBackground(new Color(234, 234, 234));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        contactsTable.setSelectionBackground(Color.BLUE);
        contactsTable.setSelectionForeground(Color.BLUE);
        contactsTable.setShowVerticalLines(false);
        contactsTable.setGridColor(new Color(154, 191, 192));
        contactsTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        contactsTable.getTableHeader().setDefaultRenderer(new TableHeaderRenderer(UIDefaults.DEFAULT_TABLE_HEADER_COLOR, Color.WHITE));
        contactsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsTable.getTableHeader().addMouseListener(new SortInfoListener());
        contactsTable.setRowHeight(30);
        contactsTable.addMouseListener(new MouseEventListener());
        setupRowSorter();
        contactsTable.setBorder(BorderFactory.createLineBorder(Color.black));
        JPanel center = new JPanel(new GridLayout(1, 1));
        center.add(new JScrollPane(contactsTable));
        center.setBorder(BorderFactory.createTitledBorder(Messages.getString("Contacts")));
        add(center, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
        JPanel south = new JPanel();
        south.setLayout(new GridLayout(1, 2));
        statusLbl = new JLabel(Messages.getString("Contacts: ") + tableModel.getRowCount(), JLabel.LEADING);
        msgLbl = new JLabel("", JLabel.TRAILING);
        south.add(statusLbl);
        south.add(msgLbl);
        add(south, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(parent.getWidth() - 100, parent.getHeight() - 100));
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("icons/icon_money.png"));
    }

    private void loadContacts() {
        tableModel = new ContactsTableModel();
        java.util.List contacts = getContactsList();
        if (contacts == null) return;
        int sz = contacts.size();
        for (int i = 0; i < sz; i++) {
            Contact c = (Contact) contacts.get(i);
            String[] row = { "" + c.getId(), c.getFullName(), c.getBusPhone(), c.getEmail() };
            tableModel.insertRow(i, row);
        }
    }

    private java.util.List getContactsList() {
        try {
            java.util.List contacts = em.getContacts(user.getUid());
            return contacts;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    class MouseEventListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int row = contactsTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    row = contactsTable.convertRowIndexToModel(row);
                } else {
                    return;
                }
                contactsTable.getSelectionModel().setSelectionInterval(row, row);
                String id = (String) tableModel.getContactId(row);
                if (id == null || id.length() == 0) {
                    return;
                }
                showAddContactDialog(Long.parseLong(id));
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }

    private void loadButtons() {
        JButton add = new JButton(Messages.getString("Add"), new ImageIcon("icons/new_contact.png"));
        add.setMnemonic(KeyEvent.VK_A);
        add.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showAddContactDialog();
            }
        });
        JButton edit = new JButton(Messages.getString("Edit"), new ImageIcon("icons/edit_contact.png"));
        edit.setMnemonic(KeyEvent.VK_E);
        edit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int sel = contactsTable.getSelectedRow();
                if (sel >= 0) {
                    sel = contactsTable.convertRowIndexToModel(sel);
                }
                String id = (String) tableModel.getContactId(sel);
                if (id == null || id.length() == 0) {
                    return;
                }
                showAddContactDialog(Long.parseLong(id));
            }
        });
        JButton delete = new JButton(Messages.getString("Delete"), new ImageIcon("icons/bin.png"));
        delete.setMnemonic(KeyEvent.VK_D);
        delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (showConfirmDialog("Are you sure?")) {
                    int[] selectedArr = contactsTable.getSelectedRows();
                    for (int i : selectedArr) {
                        int sel = contactsTable.convertRowIndexToModel(i);
                        if (sel >= 0) {
                            String id = (String) tableModel.getContactId(sel);
                            if (id == null || id.length() == 0) {
                                return;
                            }
                            deleteContact(id);
                        }
                    }
                }
            }
        });
        JButton ic = new JButton(Messages.getString("Import"), new ImageIcon("icons/import.png"));
        ic.setMnemonic(KeyEvent.VK_I);
        ic.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                CSVFilter filter = new CSVFilter();
                fc.addChoosableFileFilter(filter);
                int sel = fc.showOpenDialog(ViewContactsDialog.this);
                if (sel == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    if (filter.accept(f)) {
                        doImport(f);
                    } else JOptionPane.showMessageDialog(ViewContactsDialog.this, Messages.getString("Invalid file selection"), Messages.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton cancel = new JButton(Messages.getString("Close"), new ImageIcon("icons/cancel.png"));
        cancel.setMnemonic(KeyEvent.VK_C);
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        toolBar.add(add);
        toolBar.add(edit);
        toolBar.add(delete);
        toolBar.add(ic);
        toolBar.add(cancel);
    }

    private void reloadContacts() {
        tableModel.setRowCount(0);
        java.util.List results = getContactsList();
        tableModel = new ContactsTableModel();
        if (results != null) {
            int sz = results.size();
            for (int i = 0; i < sz; i++) {
                Contact c = (Contact) results.get(i);
                String[] row = { "" + c.getId(), c.getFullName(), c.getBusPhone(), c.getEmail() };
                tableModel.insertRow(i, row);
            }
            contactsTable.setModel(tableModel);
        }
        setupRowSorter();
    }

    public void addToView(Contact newContact, Contact oldContact) {
        String[] row = { "" + newContact.getId(), newContact.getFullName(), newContact.getBusPhone(), newContact.getEmail() };
        if (oldContact != null) {
            tableModel.updateRow(oldContact.getId(), row);
        } else {
            tableModel.insertRow(tableModel.getRowCount(), row);
            setupRowSorter();
            contactsTable.updateUI();
        }
    }

    private void setupRowSorter() {
        rowSorter = new TableRowSorter<TableModel>(contactsTable.getModel());
        rowSorter.setComparator(2, stringComparator);
        rowSorter.setComparator(1, stringComparator);
        rowSorter.setComparator(0, stringComparator);
        contactsTable.setRowSorter(rowSorter);
    }

    private transient Comparator<String> stringComparator = new Comparator<String>() {

        public int compare(String s1, String s2) {
            return s1.compareTo(s2);
        }
    };

    private void deleteContact(String id) {
        try {
            int r = em.deleteContact(Long.parseLong(id));
            if (r == 1) {
                reloadContacts();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this, Messages.getString("Unable to delete"), Messages.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean showConfirmDialog(String msg) {
        int n = JOptionPane.showConfirmDialog(this, msg);
        return n == JOptionPane.YES_OPTION;
    }

    private void showAddContactDialog() {
        AddContactDialog d = new AddContactDialog(this, user);
        d.pack();
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    private void showAddContactDialog(long contactid) {
        try {
            Contact c = em.getContact(contactid);
            AddContactDialog d = new AddContactDialog(this, user, c);
            d.pack();
            d.setLocationRelativeTo(this);
            d.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void doImport(final File f) {
        SwingWorker<int[], Void> worker = new SwingWorker<int[], Void>() {

            public int[] doInBackground() throws Exception {
                try {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    net.mjrz.fm.utils.ImportOutlookContacts ioc = new net.mjrz.fm.utils.ImportOutlookContacts();
                    int[] ret = ioc.importFile(user.getUid(), f);
                    msgLbl.setText("<html><b>" + Messages.getString("Imported ") + ret[1] + Messages.getString(" out of ") + ret[0] + "</b></html>");
                    return ret;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    JOptionPane.showMessageDialog(ViewContactsDialog.this, Messages.getString("Error importing file"), Messages.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return null;
                } finally {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }

            public void done() {
                reloadContacts();
            }
        };
        worker.execute();
    }

    public class CSVFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            int pos = f.getName().lastIndexOf('.');
            String ext = null;
            if (pos >= 0 && pos + 1 < f.getName().length()) ext = f.getName().substring(pos + 1);
            if (ext != null) {
                if (ext.equalsIgnoreCase("csv")) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        public String getDescription() {
            return "csv files";
        }
    }

    class SortInfoListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            int col = contactsTable.columnAtPoint(e.getPoint());
            int sortInfo = tableModel.getSortInfo(col);
            int newSortInfo = SortableTableModel.NONE;
            switch(sortInfo) {
                case TransactionTableModel.NONE:
                    newSortInfo = SortableTableModel.ASC;
                    break;
                case TransactionTableModel.ASC:
                    newSortInfo = SortableTableModel.DESC;
                    break;
                case TransactionTableModel.DESC:
                    newSortInfo = SortableTableModel.ASC;
                    break;
            }
            tableModel.setSortInfo(col, newSortInfo);
        }
    }
}

package gui;

import general.Ban;
import general.KeyRef;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import state.GameState;
import state.ProgramState;
import storage.DB;

/**
 * 
 * @author JPD
 * The tab with local bans in it
 */
@SuppressWarnings("serial")
public class LocalPanel extends JPanel {

    protected static int INFO_DIALOG = 0;

    protected static int BANS_DIALOG = 1;

    protected static int EDIT_DIALOG = 2;

    public LocalBansTable table;

    private DB database;

    public ProgramState programState = ProgramState.instance();

    private JScrollPane tableScrollPane = new JScrollPane();

    private JPanel toolbar = new JPanel();

    private JPanel selectionBox = new JPanel();

    private JCheckBox banCheck = new JCheckBox("Bans", true);

    public JCheckBox infoCheck = new JCheckBox("Infos", true);

    private JLabel selectionShowLabel = new JLabel("Check what you want to view");

    private String typeToDelete;

    private String nameToDelete;

    JButton newBan = new JButton("Add Ban");

    JButton newInfo = new JButton("Add Info");

    JButton toInfos = new JButton("To Info");

    JButton toBans = new JButton("To Ban");

    JButton delete = new JButton("Delete Ban(s)/Info(s)");

    JButton edit = new JButton("Edit Ban/Info");

    JDialog bansDialog;

    JPanel bansPane;

    JLabel name;

    JLabel reason;

    JComboBox comments;

    JTextField nameField;

    JTextField reasonField;

    JButton add;

    JButton editd;

    JButton cancel;

    JTable commentsTable;

    JPanel searchPanel = new JPanel();

    JLabel searchLabel = new JLabel("Search for a ban:  ");

    JTextField search = new JTextField(30);

    public LocalPanel() {
        database = DB.instance();
        table = new LocalBansTable();
        TableColumn column = null;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMaxWidth(40);
                column.setMinWidth(40);
            }
            if (i == 1) {
                column.setMaxWidth(115);
                column.setMinWidth(115);
            }
            if (i == 3) {
                column.setMaxWidth(75);
                column.setMinWidth(75);
            }
            if (i == 4) {
                column.setMaxWidth(75);
                column.setMinWidth(75);
            }
        }
        this.setLayout(new BorderLayout());
        selectionBox.setLayout(new BoxLayout(selectionBox, BoxLayout.LINE_AXIS));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));
        searchPanel.add(searchLabel);
        searchPanel.add(search);
        selectionBox.add(selectionShowLabel);
        selectionBox.add(banCheck);
        selectionBox.add(infoCheck);
        selectionBox.add(searchPanel);
        tableScrollPane.setViewportView(table);
        toolbarInitialization();
        addListeners();
        this.setOpaque(false);
        this.add(selectionBox, BorderLayout.PAGE_END);
        this.add(toolbar, BorderLayout.PAGE_START);
        this.add(tableScrollPane, BorderLayout.CENTER);
    }

    /**
	 * Adds listeners for all of the buttons in the tab
	 */
    private void addListeners() {
        infoCheck.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                filterUpdate();
            }
        });
        banCheck.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                filterUpdate();
            }
        });
        newBan.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createNewBanDialog(LocalPanel.BANS_DIALOG);
            }
        });
        newInfo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createNewBanDialog(LocalPanel.INFO_DIALOG);
            }
        });
        edit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRowCount() == 1) {
                    createNewBanDialog(LocalPanel.EDIT_DIALOG);
                } else {
                    JOptionPane.showMessageDialog(null, "You must select one ban/info to edit", "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        toInfos.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRowCount() > 0) {
                    if (table.tableModel.getValueAt(table.getSelectedRow(), 0).equals("Info")) {
                        JOptionPane.showMessageDialog(null, "Already an Info", "This is already any info!", JOptionPane.INFORMATION_MESSAGE, null);
                    } else {
                        String name = (String) table.tableModel.getValueAt(table.getSelectedRow(), 1);
                        try {
                            database.convert(database.getBanID(database.getPID(name), programState.getBnetName(), Ban.BAN), Ban.INFO);
                            table.setValueAt("Info", table.getSelectedRow(), 0);
                            JOptionPane.showMessageDialog(null, "The ban on " + name + " was successfully converted to an info!");
                        } catch (SQLException ef) {
                            JOptionPane.showMessageDialog(null, "The ban on " + name + " was unable to be converted to a info!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You must select a ban to convert");
                }
            }
        });
        toBans.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRowCount() > 0) {
                    if (table.tableModel.getValueAt(table.getSelectedRow(), 0).equals("Ban")) {
                        JOptionPane.showMessageDialog(null, "Already a ban", "This is already a ban", JOptionPane.INFORMATION_MESSAGE, null);
                    } else {
                        String name = (String) table.tableModel.getValueAt(table.getSelectedRow(), 1);
                        try {
                            database.convert(database.getBanID(database.getPID(name), programState.getBnetName(), Ban.INFO), Ban.BAN);
                            table.setValueAt("Ban", table.getSelectedRow(), 0);
                            JOptionPane.showMessageDialog(null, "The info on " + name + " was successfully converted to a ban!");
                        } catch (SQLException ef) {
                            JOptionPane.showMessageDialog(null, "The info on " + name + " was unable to be converted to a ban!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You must select an info to convert");
                }
            }
        });
        delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "You must first select an item to delete!", "", JOptionPane.INFORMATION_MESSAGE);
                }
                if (table.getSelectedRowCount() == 1) {
                    typeToDelete = (String) table.tableModel.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 0);
                    nameToDelete = (String) table.tableModel.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 1);
                    int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + typeToDelete + " on " + nameToDelete + "?", "Delete " + typeToDelete, JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        try {
                            database.removeFromBans(nameToDelete, typeToDelete);
                        } catch (SQLException e2) {
                            JOptionPane.showMessageDialog(null, "Unable to delete from database", "Error", JOptionPane.ERROR_MESSAGE);
                            e2.printStackTrace();
                        }
                        table.tableModel.removeRow(table.getSelectedRow());
                    }
                }
                if (table.getSelectedRowCount() > 1) {
                    int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ALL the selected bans/infos?", "Delete multiple bans/infos?", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        for (int i = 0; i < table.getSelectedRowCount(); i++) {
                            try {
                                database.removeFromBans((String) table.tableModel.getValueAt(table.convertRowIndexToModel(table.getSelectedRows()[i]), 1), (String) table.tableModel.getValueAt(table.convertRowIndexToModel(table.getSelectedRows()[i]), 0));
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }
                        for (int i = table.getSelectedRowCount() - 1; i >= 0; i--) {
                            table.tableModel.removeRow(table.getSelectedRows()[i]);
                        }
                    }
                }
            }
        });
        search.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                filterUpdate();
            }

            public void insertUpdate(DocumentEvent e) {
                filterUpdate();
            }

            public void removeUpdate(DocumentEvent e) {
                filterUpdate();
            }
        });
    }

    /**
	 * Intitializes the toolbar
	 */
    private void toolbarInitialization() {
        toolbar.add(newBan);
        toolbar.add(newInfo);
        toolbar.add(Box.createRigidArea(new Dimension(10, 0)));
        toolbar.add(edit);
        toolbar.add(Box.createRigidArea(new Dimension(10, 0)));
        toolbar.add(delete);
        toolbar.add(Box.createRigidArea(new Dimension(10, 0)));
        toolbar.add(toBans);
        toolbar.add(toInfos);
        newBan.setOpaque(false);
        newInfo.setOpaque(false);
        edit.setOpaque(false);
        delete.setOpaque(false);
        toBans.setOpaque(false);
        toInfos.setOpaque(false);
        toolbar.setOpaque(false);
    }

    /**
	 * Creates a ban dialog for adding or editing bans and infos
	 * @param type --type of the ban dialog (edit, info, ban)
	 */
    private void createNewBanDialog(final int type) {
        bansDialog = new JDialog() {

            public boolean isResizable() {
                return false;
            }
        };
        bansPane = (JPanel) bansDialog.getContentPane();
        name = new JLabel("Account: ");
        reason = new JLabel("Reason: ");
        comments = new JComboBox(general.Comment.comments.toArray());
        nameField = new JTextField(20);
        reasonField = new JTextField(20);
        AbstractDocument doc = (AbstractDocument) nameField.getDocument();
        doc.setDocumentFilter(new FixedSizeFilter(15));
        if (type == LocalPanel.BANS_DIALOG) add = new JButton("Add Ban"); else if (type == LocalPanel.INFO_DIALOG) add = new JButton("Add Info"); else if (type == LocalPanel.EDIT_DIALOG) add = new JButton("OK");
        cancel = new JButton("Cancel");
        if (type == LocalPanel.BANS_DIALOG) {
            bansDialog.setTitle("Add a Ban");
        } else if (type == LocalPanel.INFO_DIALOG) {
            bansDialog.setTitle("Add an Info");
        } else if (type == LocalPanel.EDIT_DIALOG) bansDialog.setTitle("Edit Ban/Info");
        if (type == LocalPanel.EDIT_DIALOG) {
            nameField.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
            reasonField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
        }
        JPanel buttonPanel = new JPanel();
        SpringLayout layout = new SpringLayout();
        bansPane.setLayout(layout);
        commentsTable = new JTable(general.Comment.getComments(), new String[] { "Keyword", "Template" });
        commentsTable.getColumnModel().getColumn(0).setPreferredWidth(65);
        commentsTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        commentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane commentsScrollPane = new JScrollPane(commentsTable);
        buttonPanel.add(add);
        buttonPanel.add(cancel);
        bansPane.add(name);
        bansPane.add(nameField);
        bansPane.add(reason);
        bansPane.add(reasonField);
        bansPane.add(commentsScrollPane);
        bansPane.add(buttonPanel);
        layout.putConstraint(SpringLayout.WEST, name, 10, SpringLayout.WEST, bansPane);
        layout.putConstraint(SpringLayout.NORTH, name, 13, SpringLayout.NORTH, bansPane);
        layout.putConstraint(SpringLayout.WEST, nameField, 5, SpringLayout.EAST, name);
        layout.putConstraint(SpringLayout.NORTH, nameField, 10, SpringLayout.NORTH, bansPane);
        layout.putConstraint(SpringLayout.EAST, nameField, 0, SpringLayout.EAST, commentsScrollPane);
        layout.putConstraint(SpringLayout.WEST, reason, 10, SpringLayout.WEST, bansPane);
        layout.putConstraint(SpringLayout.NORTH, reason, 8, SpringLayout.SOUTH, nameField);
        layout.putConstraint(SpringLayout.WEST, reasonField, 5, SpringLayout.EAST, reason);
        layout.putConstraint(SpringLayout.NORTH, reasonField, 5, SpringLayout.SOUTH, nameField);
        layout.putConstraint(SpringLayout.EAST, reasonField, 0, SpringLayout.EAST, commentsScrollPane);
        layout.putConstraint(SpringLayout.WEST, commentsScrollPane, 10, SpringLayout.WEST, bansPane);
        layout.putConstraint(SpringLayout.NORTH, commentsScrollPane, 10, SpringLayout.SOUTH, reasonField);
        layout.putConstraint(SpringLayout.SOUTH, commentsScrollPane, 110, SpringLayout.SOUTH, reasonField);
        layout.putConstraint(SpringLayout.EAST, commentsScrollPane, 400, SpringLayout.WEST, commentsScrollPane);
        layout.putConstraint(SpringLayout.EAST, buttonPanel, 0, SpringLayout.EAST, commentsScrollPane);
        layout.putConstraint(SpringLayout.NORTH, buttonPanel, 5, SpringLayout.SOUTH, commentsScrollPane);
        layout.putConstraint(SpringLayout.EAST, bansPane, 10, SpringLayout.EAST, nameField);
        layout.putConstraint(SpringLayout.SOUTH, bansPane, 10, SpringLayout.SOUTH, buttonPanel);
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                bansDialog.setVisible(false);
                bansDialog = null;
            }
        });
        add.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String name = null;
                String reason = null;
                String[] info = { Ban.typeToString(Ban.INFO), Ban.typeToString(Ban.BAN) };
                if (nameField.getText() == null || nameField.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "You must enter a name to ban.");
                    return;
                } else if (!Ban.isValidName(nameField.getText())) {
                    JOptionPane.showMessageDialog(null, "You entered an invalid battle.net name");
                    return;
                } else {
                    name = nameField.getText();
                }
                if (reasonField.getText() == null && type == LocalPanel.BANS_DIALOG) {
                    reason = "leaver";
                } else if (reasonField.getText() == null) {
                    JOptionPane.showMessageDialog(null, "You must enter a Comment on this user");
                } else {
                    reason = reasonField.getText();
                }
                if (type != LocalPanel.EDIT_DIALOG) {
                    try {
                        if (GameState.instance().gameStarted) {
                            reason = KeyRef.processText(reason);
                        }
                    } catch (NullPointerException e1) {
                    }
                    try {
                        if (!database.itemExistsLocalDB(name, type)) {
                            database.addBan(new Ban(name, programState.getBnetName(), reason, date(), date(), type));
                            table.addBanRow(info[type], name, reason, date(), date());
                            bansDialog.setVisible(false);
                            bansDialog = null;
                        } else {
                            JOptionPane.showMessageDialog(null, "There cannot be more than one ban on a single player", "Duplicate ban detected", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException e1) {
                        if (type == LocalPanel.BANS_DIALOG) {
                            JOptionPane.showMessageDialog(null, "Could not add the ban to the database");
                        } else {
                            JOptionPane.showMessageDialog(null, "Could not add the info to the database");
                        }
                    }
                } else {
                    try {
                        int banType;
                        if (table.getValueAt(table.getSelectedRow(), 0).toString().equals("Info")) banType = 0; else banType = 1;
                        try {
                            database.removeFromBans(table.getValueAt(table.getSelectedRow(), 1).toString(), table.getValueAt(table.getSelectedRow(), 0).toString());
                        } catch (SQLException e2) {
                            JOptionPane.showMessageDialog(null, "Unable to delete from database", "Error", JOptionPane.ERROR_MESSAGE);
                            e2.printStackTrace();
                        }
                        if (!database.itemExistsLocalDB(name, banType)) {
                            database.addBan(new Ban(name, programState.getBnetName(), reason, table.getValueAt(table.getSelectedRow(), 3).toString(), date(), banType));
                            bansDialog.setVisible(false);
                            bansDialog = null;
                            table.addBanRow(info[banType], name, reason, table.getValueAt(table.getSelectedRow(), 3).toString(), date());
                            table.tableModel.removeRow(table.getSelectedRow());
                        } else {
                            JOptionPane.showMessageDialog(null, "There cannot be more than one ban on a single player", "Duplicate ban detected", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException e1) {
                        if (type == LocalPanel.BANS_DIALOG) {
                            JOptionPane.showMessageDialog(null, "Could not add the ban to the database");
                        } else {
                            JOptionPane.showMessageDialog(null, "Could not add the info to the database");
                        }
                    }
                }
            }
        });
        SelectionListener listener = new SelectionListener();
        commentsTable.getSelectionModel().addListSelectionListener(listener);
        bansDialog.getRootPane().setDefaultButton(add);
        bansDialog.pack();
        bansDialog.setModal(true);
        bansDialog.setLocationRelativeTo(null);
        bansDialog.setVisible(true);
    }

    /**
	 * Get the current date
	 * @return the Current date
	 */
    public static String date() {
        final String DATE_FORMAT_NOW = "MM-dd-yyyy";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    /**
	 * For use only with the search Document Listener
	 */
    private void filterUpdate() {
        if (search.getText() != null) {
            if (banCheck.isSelected() && infoCheck.isSelected()) {
                RowFilter<Object, Object> andOrFilter = new RowFilter<Object, Object>() {

                    public boolean include(Entry<? extends Object, ? extends Object> entry) {
                        if (entry.getStringValue(0).equalsIgnoreCase(Ban.typeToString(Ban.BAN)) || entry.getStringValue(0).equalsIgnoreCase(Ban.typeToString(Ban.INFO))) {
                            for (int x = 0; x < entry.getValueCount() - 1; x++) {
                                if (entry.getStringValue(x).toLowerCase().contains(search.getText().toLowerCase())) {
                                    return true;
                                }
                            }
                            return false;
                        } else {
                            return false;
                        }
                    }
                };
                table.sorter.setRowFilter(andOrFilter);
            } else if (!banCheck.isSelected() && infoCheck.isSelected()) {
                ArrayList<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(2);
                filters.add(RowFilter.regexFilter("Info", 0));
                filters.add(RowFilter.regexFilter(search.getText()));
                RowFilter<Object, Object> filta = RowFilter.andFilter(filters);
                table.sorter.setRowFilter(filta);
            } else if (banCheck.isSelected() && !infoCheck.isSelected()) {
                ArrayList<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(2);
                filters.add(RowFilter.regexFilter("Ban", 0));
                filters.add(RowFilter.regexFilter(search.getText()));
                RowFilter<Object, Object> filta = RowFilter.andFilter(filters);
                table.sorter.setRowFilter(filta);
            } else if (!banCheck.isSelected() && !infoCheck.isSelected()) {
                ArrayList<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(2);
                filters.add(RowFilter.regexFilter("d4ecxj34346"));
                filters.add(RowFilter.regexFilter(search.getText()));
                RowFilter<Object, Object> filta = RowFilter.andFilter(filters);
                table.sorter.setRowFilter(filta);
            }
        } else {
            if (banCheck.isSelected() && infoCheck.isSelected()) {
                ArrayList<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(2);
                filters.add(RowFilter.regexFilter("Ban", 0));
                filters.add(RowFilter.regexFilter("Info", 0));
                RowFilter<Object, Object> fooBarFilter = RowFilter.orFilter(filters);
                table.sorter.setRowFilter(fooBarFilter);
            } else if (!banCheck.isSelected() && infoCheck.isSelected()) {
                table.sorter.setRowFilter(RowFilter.regexFilter("Info", 0));
            } else if (banCheck.isSelected() && !infoCheck.isSelected()) {
                table.sorter.setRowFilter(RowFilter.regexFilter("Ban", 0));
            } else if (!banCheck.isSelected() && !infoCheck.isSelected()) {
                table.sorter.setRowFilter(RowFilter.regexFilter("d4ecxj34346"));
            }
        }
    }

    /**
	 *  Class that allows us to limit the size of a document, which will be used to limit the size of the name to ban textField
	 * @author JPD
	 *
	 */
    class FixedSizeFilter extends DocumentFilter {

        int maxSize;

        /**
		 * limit is the maximum number of characters allowed.
		 * @param limit --an integer limit on the number of characters
		 */
        public FixedSizeFilter(int limit) {
            maxSize = limit;
        }

        /**
		 * This method is called when characters are inserted into the document
		 */
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
            replace(fb, offset, 0, str, attr);
        }

        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
            int newLength = fb.getDocument().getLength() - length + str.length();
            if (newLength <= maxSize) {
                fb.replace(offset, length, str, attrs);
            } else {
                throw new BadLocationException("New characters exceeds max size of document", offset);
            }
        }
    }

    /**
	 * Refreshes all the Tables
	 */
    public void refreshTables() {
        table.refreshTables();
    }

    /**
	 * Overrides the SelectionListener
	 */
    public class SelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            reasonField.setText((String) commentsTable.getValueAt(commentsTable.convertRowIndexToModel(commentsTable.getSelectedRow()), 1));
        }
    }

    /**
     * Deletes a row from the table given a player's name
     * @param player --the name of the player
     * @param type --the type of ban (or info)
     */
    public void deleteRowbyPlayer(final String player, final String type) {
        Runnable r = new Runnable() {

            public void run() {
                int index = table.findPlayer(player, type);
                if (index >= 0) table.tableModel.removeRow(index);
            }
        };
        Thread go = new Thread(r);
        go.start();
    }
}

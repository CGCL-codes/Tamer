package modrcon;

import java.awt.event.*;
import java.awt.*;
import javax.swing.table.*;
import javax.swing.*;
import java.util.ArrayList;

/**
 *
 * @author Pyrite
 */
public class Q3CommandManager extends JDialog implements ActionListener, MouseListener {

    private MainWindow parent;

    private JScrollPane jsp;

    private JTable commandTable;

    private JLabel iconKeyAssignments;

    private JLabel iconAddRow;

    private JLabel iconEditCell;

    private JLabel iconDeleteRow;

    private JButton btnSave;

    private JButton btnClose;

    public Q3CommandManager(MainWindow owner) {
        super(owner);
        this.parent = owner;
        this.setTitle("1up ModRcon - Manage Commands");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setModal(true);
        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());
        JPanel row2 = new JPanel();
        row2.setLayout(new BorderLayout());
        row2.setBorder(BorderFactory.createTitledBorder("Commands List"));
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BorderLayout());
        iconKeyAssignments = new JLabel("Key Assignments");
        iconKeyAssignments.setIcon(new ImageIcon(getClass().getResource("/modrcon/resources/about.png")));
        JPanel tableIconPanel = new JPanel();
        iconAddRow = new JLabel();
        iconAddRow.setIcon(new ImageIcon(getClass().getResource("/modrcon/resources/add.png")));
        iconAddRow.addMouseListener(this);
        iconEditCell = new JLabel();
        iconEditCell.setIcon(new ImageIcon(getClass().getResource("/modrcon/resources/files_edit.png")));
        iconEditCell.addMouseListener(this);
        iconDeleteRow = new JLabel();
        iconDeleteRow.setIcon(new ImageIcon(getClass().getResource("/modrcon/resources/del.png")));
        iconDeleteRow.addMouseListener(this);
        tableIconPanel.add(iconAddRow);
        tableIconPanel.add(iconEditCell);
        tableIconPanel.add(iconDeleteRow);
        iconPanel.add(iconKeyAssignments, BorderLayout.WEST);
        iconPanel.add(tableIconPanel, BorderLayout.EAST);
        DefaultTableModel dtm = new DefaultTableModel();
        commandTable = new JTable(dtm);
        dtm.addColumn("Command");
        dtm.addColumn("Description");
        dtm.addColumn("Example");
        commandTable.setPreferredScrollableViewportSize(new Dimension(350, 200));
        commandTable.setGridColor(Color.LIGHT_GRAY);
        jsp = new JScrollPane(commandTable);
        row2.add(jsp, BorderLayout.CENTER);
        row2.add(iconPanel, BorderLayout.SOUTH);
        JPanel row3 = new JPanel();
        row3.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        btnSave = new JButton("Save");
        btnSave.addActionListener(this);
        btnClose = new JButton("Close");
        btnClose.addActionListener(this);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnClose);
        row3.add(new JLabel(" Note: Changes are only saved to the database after clicking the Save button."), BorderLayout.WEST);
        row3.add(buttonPanel, BorderLayout.EAST);
        cp.add(new LogoPanel(LogoPanel.LOGO_CENTER), BorderLayout.NORTH);
        cp.add(row2, BorderLayout.CENTER);
        cp.add(row3, BorderLayout.SOUTH);
        this.pack();
        this.readFile();
        Point aboutBoxLocation = new Point();
        double aboutBoxX = owner.getLocation().getX() + ((owner.getWidth() / 2) - (this.getWidth() / 2));
        double aboutBoxY = owner.getLocation().getY() + ((owner.getHeight() / 2) - (this.getHeight() / 2));
        aboutBoxLocation.setLocation(aboutBoxX, aboutBoxY);
        this.setLocation(aboutBoxLocation);
        this.setVisible(true);
    }

    private void readFile() {
        DefaultTableModel dm = (DefaultTableModel) this.commandTable.getModel();
        dm.getDataVector().removeAllElements();
        Q3CommandDatabase db = new Q3CommandDatabase();
        ArrayList commands = db.getCommandList();
        for (Object o : commands) {
            Q3Command cmd = (Q3Command) o;
            dm.addRow(cmd.toArray());
        }
        ColumnResizer.adjustColumnPreferredWidths(this.commandTable);
    }

    private void saveTable() {
        ArrayList commands = new ArrayList();
        TableModel tm = this.commandTable.getModel();
        int numRows = tm.getRowCount();
        for (int i = 0; i < numRows; i++) {
            String col1 = (String) tm.getValueAt(i, 0);
            String col2 = (String) tm.getValueAt(i, 1);
            String col3 = (String) tm.getValueAt(i, 2);
            Q3Command c = new Q3Command(col1, col2, col3);
            commands.add(c);
        }
        Q3CommandDatabase db = new Q3CommandDatabase();
        db.setCommandList(commands);
        db.saveDatabase();
    }

    /**
     * Checks all table cells to verify that they are non-empty.
     *
     * @return True if all cells are populated, otherwise returns false.
     */
    private boolean validateFields() {
        boolean flag = true;
        TableModel tm = this.commandTable.getModel();
        int numRows = tm.getRowCount();
        int numCols = tm.getColumnCount();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                String cell = (String) tm.getValueAt(i, j);
                if (cell.equals("")) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    public void actionPerformed(ActionEvent event) {
        AbstractButton pressedButton = (AbstractButton) event.getSource();
        if (pressedButton == btnSave) {
            if (this.validateFields()) {
                this.saveTable();
                this.parent.getControlPanel().refreshCommandCombo();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "<html>All fields are required, and cannot be empty.<br>If you are still in editing mode on a particular cell,<br>hit tab or click outside of the cell, then try to save again.</html>");
            }
        } else if (pressedButton == btnClose) {
            this.dispose();
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == iconAddRow) {
            DefaultTableModel tm = (DefaultTableModel) this.commandTable.getModel();
            tm.addRow(new Q3Command("", "", "").toArray());
        } else if (e.getSource() == iconDeleteRow) {
            DefaultTableModel tm = (DefaultTableModel) this.commandTable.getModel();
            int row = this.commandTable.getSelectedRow();
            if (row != -1) {
                tm.removeRow(row);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void mouseReleased(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}

package editor;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DialogLibrary extends JDialog {

    private static final long serialVersionUID = 1L;

    private Editor window;

    private final JTable table;

    private MyTableModel dtm;

    private Object[][] datas;

    private String[] title = { "Name", "Use" };

    private JButton addButton;

    private final JButton removeButton;

    public DialogLibrary(Editor w) {
        window = w;
        this.setTitle("External library");
        this.setResizable(false);
        this.setModal(true);
        this.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        table = new JTable();
        dtm = new MyTableModel(datas, title);
        table.setModel(dtm);
        table.setForeground(Color.blue);
        table.setFont(new Font("MS Gothic", Font.BOLD, 10));
        refreshTable();
        infoPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        infoPanel.setPreferredSize(new Dimension(300, 150));
        panel.add(infoPanel, BorderLayout.NORTH);
        removeButton = new JButton("Remove");
        removeButton.setEnabled(false);
        removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });
        panel.add(removeButton, BorderLayout.WEST);
        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                addActionPerformed(evt);
            }
        });
        table.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                if (table.getSelectedRow() >= 0) {
                    removeButton.setEnabled(true);
                    table.setRowSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
                }
            }
        });
        panel.add(addButton, BorderLayout.EAST);
        this.add(panel, BorderLayout.CENTER);
        this.pack();
        Point loc = new Point(window.frame.getLocation().x + window.frame.getSize().width / 2 - this.getSize().width / 2, window.frame.getLocation().y + window.frame.getSize().height / 2 - this.getSize().height / 2);
        this.setLocation(loc);
        this.setVisible(true);
    }

    private void addActionPerformed(ActionEvent evt) {
        String name = "", use = "", call = "", code = "";
        JFrame frame = new JFrame("Open library (*.hl)");
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            chooser.getCurrentDirectory();
            File file = chooser.getSelectedFile();
            Scanner s = null;
            try {
                s = new Scanner(file);
                name = s.nextLine();
                use = s.nextLine();
                call = s.nextLine();
                while (s.hasNext()) {
                    code += s.nextLine() + "\n";
                }
            } catch (FileNotFoundException ex) {
                System.out.println("[Error] Cannot load the selected library");
            }
        }
        boolean inside = false;
        for (Library l : window.libs) {
            if (l.name.compareTo(name) == 0) {
                inside = true;
                break;
            }
        }
        if (!inside) {
            if (call.startsWith("#")) {
                call = call.replace("#", "");
                window.libs.add(new Library(name, use, call, code, true));
            } else {
                window.libs.add(new Library(name, use, call, code));
            }
        }
        refreshTable();
    }

    private void removeActionPerformed(ActionEvent evt) {
        String name = (String) datas[table.getSelectedRow()][0];
        Iterator<Library> i = window.libs.iterator();
        while (i.hasNext()) {
            Library r = i.next();
            if (r.name.equals(name)) {
                window.libs.remove(r);
                if (window.libs.size() == 0) removeButton.setEnabled(false);
                refreshTable();
                return;
            }
        }
    }

    public class MyTableModel extends DefaultTableModel {

        private static final long serialVersionUID = 1L;

        public MyTableModel(Object[][] data, Object[] names) {
            super(data, names);
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }

    /**
     * Refresh the library's table
     * 
     */
    private void refreshTable() {
        int k = 0;
        Iterator<Library> i = window.libs.iterator();
        datas = new Object[window.libs.size()][2];
        while (i.hasNext()) {
            Library l = i.next();
            datas[k][0] = l.name;
            datas[k][1] = l.use;
            k++;
        }
        DefaultTableModel newDtm = new MyTableModel(datas, title);
        table.setModel(newDtm);
    }
}

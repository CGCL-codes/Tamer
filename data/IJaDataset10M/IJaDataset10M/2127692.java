package textparser.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class RegexSelectPopupWindow extends JFrame {

    private static final long serialVersionUID = 8985286733666175345L;

    private JFrame frame;

    private Vector<Object> head_jtable;

    private DefaultTableModel jtableModel;

    private JTable jtable;

    private static int delete_me = 0;

    RegexSelectPopupWindow(Component comp) {
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setTitle("Select Regex");
        this.setUndecorated(true);
        frame = this;
        Point p = comp.getLocationOnScreen();
        this.setBounds(p.x, p.y, 250, 180);
        add(getHeader(), BorderLayout.NORTH);
        add(getMainContentPanel(), BorderLayout.CENTER);
        this.setVisible(true);
        addWindowFocusListener(new WindowFocusListener() {

            public void windowGainedFocus(WindowEvent e) {
            }

            public void windowLostFocus(WindowEvent e) {
                frame.requestFocus();
            }
        });
    }

    private Component getHeader() {
        JButton jbAdd = new JButton("add");
        jbAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Vector<String> newRow = new Vector<String>();
                newRow.add("AaValue" + delete_me++);
                newRow.add("BbValue");
                jtableModel.addRow(newRow);
            }
        });
        JButton jbEdit = new JButton("edit");
        jbEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
            }
        });
        JButton jbDelete = new JButton("delete");
        jbDelete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                int rowIndex = jtable.getSelectedRow();
                if (rowIndex >= 0) {
                    Vector modelRows = jtableModel.getDataVector();
                    System.out.println(modelRows.get(rowIndex));
                    modelRows.remove(rowIndex);
                    jtableModel.setDataVector(modelRows, head_jtable);
                }
            }
        });
        JButton jbX = new JButton("close");
        jbX.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                frame.setVisible(false);
            }
        });
        JPanel jpnlHeader = new JPanel(new GridLayout(1, 4));
        jpnlHeader.add(jbX);
        jpnlHeader.add(jbAdd);
        jpnlHeader.add(jbEdit);
        jpnlHeader.add(jbDelete);
        jpnlHeader.setPreferredSize(new Dimension(250, 24));
        return jpnlHeader;
    }

    private Component getMainContentPanel() {
        head_jtable = new Vector<Object>();
        head_jtable.add("Name");
        head_jtable.add("Regex");
        Vector<Object> data_jtable = new Vector<Object>();
        jtableModel = new DefaultTableModel(data_jtable, head_jtable);
        jtable = new JTable(jtableModel);
        jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtable.setDragEnabled(false);
        jtable.getTableHeader().setReorderingAllowed(false);
        JScrollPane sp_jtable = new JScrollPane(jtable);
        return sp_jtable;
    }
}

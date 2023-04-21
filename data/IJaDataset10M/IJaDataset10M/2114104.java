package edu.nyu.funcassociate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

public class Table extends JPanel implements ActionListener {

    private boolean DEBUG = false;

    private JTable table;

    JTextArea infoarea;

    JTextArea warningarea;

    public Table(final String[] columnNames, final Object[][] data, String[] infonames, String[] addinformation, String warnings, int columns) {
        super(new GridLayout(1, 1));
        JTabbedPane tabbedPane = new JTabbedPane();
        table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(600, 300));
        if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    printDebugData(table);
                }
            });
        }
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        infoarea = new JTextArea();
        JScrollPane textscrollPane = new JScrollPane(infoarea);
        warningarea = new JTextArea();
        JScrollPane warnscrollPane = new JScrollPane(warningarea);
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        URL printURL = Table.class.getResource("/edu/nyu/funcimages/print-icon-1.png");
        ImageIcon printIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(printURL));
        JButton printButton = new JButton(printIcon);
        printButton.setToolTipText("Print");
        printButton.setActionCommand("Print");
        printButton.addActionListener(this);
        toolbar.add(printButton);
        URL saveURL = Table.class.getResource("/edu/nyu/funcimages/save-1.png");
        ImageIcon saveIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(saveURL));
        JButton saveButton = new JButton(saveIcon);
        saveButton.setToolTipText("Save Result");
        saveButton.setActionCommand("Save Result");
        saveButton.addActionListener(this);
        toolbar.add(saveButton);
        add(toolbar, BorderLayout.NORTH);
        InitColumnSize(table, columns);
        for (int i = 0; i < columns - 2; i++) {
            SetUpColumn(table, table.getColumnModel().getColumn(i), i);
        }
        ;
        tabbedPane.add("ResultTable", scrollPane);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.add("RequestInfo", textscrollPane);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        tabbedPane.add("Warnings", warnscrollPane);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        add(tabbedPane);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < infonames.length; i++) {
            sb.append(infonames[i] + ":" + addinformation[i] + "\n");
        }
        infoarea.append(sb.toString());
        infoarea.setCaretPosition(infoarea.getText().length());
        StringBuffer warn_sb = new StringBuffer();
        warn_sb.append(warnings);
        warningarea.append(warn_sb.toString());
        warningarea.setCaretPosition(warningarea.getText().length());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "Print") {
            print();
        } else if (e.getActionCommand() == "Save Result") {
            save();
        }
    }

    private void print() {
        MessageFormat header = new MessageFormat("Page {0,number,integer}");
        try {
            table.print(JTable.PrintMode.FIT_WIDTH, header, null);
        } catch (java.awt.print.PrinterException e1) {
            System.err.format("Cannot print %s%n", e1.getMessage());
        }
    }

    private void save() {
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new GeneListFileFilter());
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                FileWriter fw = new FileWriter(f);
                BufferedWriter out = new BufferedWriter(fw);
                for (int c = 0; c < table.getColumnCount(); c++) {
                    out.write(table.getColumnName(c));
                    out.write("     ");
                }
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        out.write(table.getValueAt(i, j).toString());
                        out.write("        ");
                    }
                    out.newLine();
                }
                out.newLine();
                String s = infoarea.getText().toString();
                String[] tokens = s.split("\\n");
                for (int m = 0; m < tokens.length; m++) {
                    out.write(tokens[m]);
                    out.newLine();
                }
                String w_s = warningarea.getText().toString();
                out.write(w_s);
                out.newLine();
                out.close();
                StringBuffer sb = new StringBuffer();
                sb.append("Your file has been saved!!");
                String msg = sb.toString();
                int messageType = JOptionPane.INFORMATION_MESSAGE;
                Icon icon = null;
                String title = "Save File";
                JOptionPane.showMessageDialog(null, msg, title, messageType, icon);
            } catch (IOException ioe) {
                System.out.println("Exception Caught : " + ioe.getMessage());
            }
        }
    }

    private void InitColumnSize(JTable table, int columns) {
        TableColumn column = null;
        if (columns == 7) {
            for (int i = 0; i < columns; i++) {
                column = table.getColumnModel().getColumn(i);
                if (i < 2) {
                    column.setPreferredWidth(50);
                } else if (i >= 2 & i < 4) {
                    column.setPreferredWidth(200);
                } else if (i == 4) {
                    column.setPreferredWidth(50);
                } else if (i == 5) {
                    column.setPreferredWidth(150);
                } else {
                    column.setPreferredWidth(300);
                }
            }
        } else {
            for (int i = 0; i < columns; i++) {
                column = table.getColumnModel().getColumn(i);
                if (i <= 2) {
                    column.setPreferredWidth(50);
                } else if (i > 2 & i <= 4) {
                    column.setPreferredWidth(200);
                } else if (i == 5) {
                    column.setPreferredWidth(50);
                } else if (i == 6) {
                    column.setPreferredWidth(150);
                } else {
                    column.setPreferredWidth(300);
                }
            }
        }
    }

    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();
        System.out.println("Value of data: ");
        for (int i = 0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j = 0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    public void SetUpColumn(JTable table, TableColumn Column, int i) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        if (i == 7) {
            switch(i) {
                case 0:
                    renderer.setToolTipText("Number of entities in query having the row's attribute");
                    Column.setCellRenderer(renderer);
                    break;
                case 1:
                    renderer.setToolTipText("Total number of entities having the row's attribute");
                    Column.setCellRenderer(renderer);
                    break;
                case 2:
                    renderer.setToolTipText("Logarithm(base 10) of the odds ratio");
                    Column.setCellRenderer(renderer);
                    break;
                case 3:
                    renderer.setToolTipText("P-value");
                    Column.setCellRenderer(renderer);
                    break;
                case 4:
                    renderer.setToolTipText("Resampling-adjusted P-value");
                    Column.setCellRenderer(renderer);
                    break;
                default:
                    break;
            }
        } else {
            switch(i) {
                case 0:
                    renderer.setToolTipText("Number of entities in query having the row's attribute");
                    Column.setCellRenderer(renderer);
                    break;
                case 1:
                    renderer.setToolTipText("Size of most surprising subquery for the row's attribute");
                    Column.setCellRenderer(renderer);
                    break;
                case 2:
                    renderer.setToolTipText("Total number of entities having the row's attribute");
                    Column.setCellRenderer(renderer);
                    break;
                case 3:
                    renderer.setToolTipText("Logarithm(base 10) of the odds ratio");
                    Column.setCellRenderer(renderer);
                    break;
                case 4:
                    renderer.setToolTipText("P-value");
                    Column.setCellRenderer(renderer);
                    break;
                case 5:
                    renderer.setToolTipText("Resampling-adjusted P-value");
                    Column.setCellRenderer(renderer);
                    break;
                default:
                    break;
            }
        }
    }
}

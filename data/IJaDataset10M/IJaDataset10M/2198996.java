package org.datanucleus.dataquality;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.MaskFormatter;

public class DisplayFileTable extends JPanel implements ActionListener {

    private ReportTable _rt;

    private JRadioButton rb1, rb2;

    private JFormattedTextField tf, rn;

    private int actionType;

    private JDialog d_f, d_m;

    private String _fileN = "";

    private Hashtable<String, Integer> _ht;

    private Vector[] vector1;

    private JComboBox[] table1, col1;

    private JLabel[] l1;

    private JRadioButton[] radio1;

    private JTextField[] tf1;

    private boolean init = false;

    private int ADDITION = 0;

    private int DELETION = 1;

    private String[] queryString;

    private Vector<String> table_s, column_s;

    private Vector<String> unique_table_s;

    private Vector<Object> copy_v;

    private Vector<Integer> delete_v = null;

    private Vector<Object[]> delrow_v = null;

    private JFrame frame;

    public DisplayFileTable(ReportTable rt) {
        _rt = rt;
        showGUI();
    }

    public DisplayFileTable(ReportTable rt, String fileN) {
        _rt = rt;
        _fileN = fileN;
        showGUI();
    }

    private void showGUI() {
        if (_rt == null) return;
        frame = new JFrame("File Table Display:" + _fileN);
        frame.getContentPane().add(_rt);
        frame.setLocation(100, 100);
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        JMenu option_m = new JMenu("Options");
        option_m.setMnemonic('O');
        menubar.add(option_m);
        JMenu column_m = new JMenu("Column");
        column_m.setMnemonic('C');
        menubar.add(column_m);
        JMenu analytics_m = new JMenu("Analytics");
        analytics_m.setMnemonic('A');
        menubar.add(analytics_m);
        JMenuItem lineC_m = new JMenuItem("Line Chart");
        lineC_m.addActionListener(this);
        lineC_m.setActionCommand("linechart");
        analytics_m.add(lineC_m);
        JMenuItem barC_m = new JMenuItem("Bar Chart");
        barC_m.addActionListener(this);
        barC_m.setActionCommand("barchart");
        analytics_m.add(barC_m);
        JMenuItem hbarC_m = new JMenuItem("Horizontal Bar Chart");
        hbarC_m.addActionListener(this);
        hbarC_m.setActionCommand("hbarchart");
        analytics_m.add(hbarC_m);
        JMenuItem pieC_m = new JMenuItem("Pie Chart");
        pieC_m.addActionListener(this);
        pieC_m.setActionCommand("piechart");
        analytics_m.add(pieC_m);
        JMenuItem addC_m = new JMenuItem("Add Column");
        addC_m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_MASK));
        addC_m.addActionListener(this);
        addC_m.setActionCommand("addcolumn");
        column_m.add(addC_m);
        JMenuItem hideC_m = new JMenuItem("Remove Column");
        hideC_m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_MASK));
        hideC_m.addActionListener(this);
        hideC_m.setActionCommand("hidecolumn");
        column_m.add(hideC_m);
        column_m.addSeparator();
        JMenuItem copyC_m = new JMenuItem("Copy Column");
        copyC_m.addActionListener(this);
        copyC_m.setActionCommand("copycolumn");
        column_m.add(copyC_m);
        JMenuItem pasteC_m = new JMenuItem("Paste Column");
        pasteC_m.addActionListener(this);
        pasteC_m.setActionCommand("pastecolumn");
        column_m.add(pasteC_m);
        JMenuItem renameC_m = new JMenuItem("Rename Column");
        renameC_m.addActionListener(this);
        renameC_m.setActionCommand("renamecolumn");
        column_m.add(renameC_m);
        JMenuItem populateC_m = new JMenuItem("Populate Column");
        populateC_m.addActionListener(this);
        populateC_m.setActionCommand("populatecolumn");
        column_m.add(populateC_m);
        column_m.addSeparator();
        JMenuItem searC_m = new JMenuItem("Search & Replace");
        searC_m.addActionListener(this);
        searC_m.setActionCommand("seareplace");
        column_m.add(searC_m);
        JMenuItem replaceC_m = new JMenuItem("Replace Null");
        replaceC_m.addActionListener(this);
        replaceC_m.setActionCommand("replace");
        column_m.add(replaceC_m);
        column_m.addSeparator();
        JMenu caseFormatC_m = new JMenu("Case Format");
        column_m.add(caseFormatC_m);
        JMenuItem upperC_m = new JMenuItem("UPPER CASE");
        upperC_m.addActionListener(this);
        upperC_m.setActionCommand("uppercase");
        caseFormatC_m.add(upperC_m);
        JMenuItem lowerC_m = new JMenuItem("lower case");
        lowerC_m.addActionListener(this);
        lowerC_m.setActionCommand("lowercase");
        caseFormatC_m.add(lowerC_m);
        JMenuItem titleC_m = new JMenuItem("Title Case");
        titleC_m.addActionListener(this);
        titleC_m.setActionCommand("titlecase");
        caseFormatC_m.add(titleC_m);
        JMenuItem sentenceC_m = new JMenuItem("Sentence case");
        sentenceC_m.addActionListener(this);
        sentenceC_m.setActionCommand("sentencecase");
        caseFormatC_m.add(sentenceC_m);
        JMenuItem addR_m = new JMenuItem("Insert Rows");
        addR_m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.ALT_MASK));
        addR_m.addActionListener(this);
        addR_m.setActionCommand("addrow");
        option_m.add(addR_m);
        JMenuItem removeR_m = new JMenuItem("Delete Rows");
        removeR_m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK));
        removeR_m.addActionListener(this);
        removeR_m.setActionCommand("deleterow");
        option_m.add(removeR_m);
        option_m.addSeparator();
        JMenuItem transR_m = new JMenuItem("Transpose Rows");
        transR_m.addActionListener(this);
        transR_m.setActionCommand("transrow");
        option_m.add(transR_m);
        option_m.addSeparator();
        JMenuItem join_m = new JMenuItem("Load Joinable File");
        join_m.addActionListener(this);
        join_m.setActionCommand("joinfile");
        option_m.add(join_m);
        JMenuItem loadR_m = new JMenuItem("Load File into Rows");
        loadR_m.addActionListener(this);
        loadR_m.setActionCommand("filerow");
        option_m.add(loadR_m);
        JMenuItem loadC_m = new JMenuItem("Load File into Columns");
        loadC_m.addActionListener(this);
        loadC_m.setActionCommand("filecol");
        option_m.add(loadC_m);
        option_m.addSeparator();
        JMenuItem formatC_m = new JMenuItem("Format");
        formatC_m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.ALT_MASK));
        formatC_m.addActionListener(this);
        formatC_m.setActionCommand("format");
        option_m.add(formatC_m);
        option_m.addSeparator();
        JMenuItem analyseC_m = new JMenuItem("Analyse");
        analyseC_m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.ALT_MASK));
        analyseC_m.addActionListener(this);
        analyseC_m.setActionCommand("analyse");
        option_m.add(analyseC_m);
        JMenuItem analyseS_m = new JMenuItem("Analyse Selected");
        analyseS_m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK));
        analyseS_m.addActionListener(this);
        analyseS_m.setActionCommand("analyseselected");
        option_m.add(analyseS_m);
        option_m.addSeparator();
        JMenuItem createC_m = new JMenuItem("Create Condition");
        createC_m.addActionListener(this);
        createC_m.setActionCommand("createcond");
        option_m.add(createC_m);
        JMenuItem undoC_m = new JMenuItem("Undo Condition");
        undoC_m.addActionListener(this);
        undoC_m.setActionCommand("undocond");
        option_m.add(undoC_m);
        option_m.addSeparator();
        JMenuItem similarC_m = new JMenuItem("Similarity Check");
        similarC_m.addActionListener(this);
        similarC_m.setActionCommand("simcheck");
        option_m.add(similarC_m);
        option_m.addSeparator();
        JMenu discreetC_m = new JMenu("Discreet Range Check");
        option_m.add(discreetC_m);
        JMenuItem matchdiscreetC_m = new JMenuItem("Match");
        matchdiscreetC_m.addActionListener(this);
        matchdiscreetC_m.setActionCommand("matchdiscreetrange");
        discreetC_m.add(matchdiscreetC_m);
        JMenuItem nomatchdiscreetC_m = new JMenuItem("No Match");
        nomatchdiscreetC_m.addActionListener(this);
        nomatchdiscreetC_m.setActionCommand("nomatchdiscreetrange");
        discreetC_m.add(nomatchdiscreetC_m);
        option_m.addSeparator();
        JMenuItem loadDB_m = new JMenuItem("Load to DB");
        loadDB_m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_MASK));
        loadDB_m.addActionListener(this);
        loadDB_m.setActionCommand("todb");
        option_m.add(loadDB_m);
        JMenuItem syncDB_m = new JMenuItem("Synch From DB");
        syncDB_m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_MASK));
        syncDB_m.addActionListener(this);
        syncDB_m.setActionCommand("fromdb");
        option_m.add(syncDB_m);
        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        try {
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (command.equals("barchart")) {
                AnalyticsListener al = new AnalyticsListener(_rt, 1);
                return;
            }
            if (command.equals("piechart")) {
                AnalyticsListener al = new AnalyticsListener(_rt, 2);
                return;
            }
            if (command.equals("hbarchart")) {
                AnalyticsListener al = new AnalyticsListener(_rt, 3);
                return;
            }
            if (command.equals("linechart")) {
                AnalyticsListener al = new AnalyticsListener(_rt, 4);
                return;
            }
            if (command.equals("undocond")) {
                if (delete_v == null || delrow_v == null) {
                    JOptionPane.showMessageDialog(null, "Condition is not Set");
                    return;
                }
                for (int i = (delete_v.size() - 1); i >= 0; i--) {
                    _rt.addRows(delete_v.get(i), 1);
                    _rt.pasteRow(delete_v.get(i), delrow_v.get(i));
                }
                delrow_v.clear();
                delrow_v = null;
                delete_v.clear();
                delete_v = null;
            }
            if (command.equals("createcond")) {
                Vector[] vector1 = new Vector[2];
                vector1[0] = new Vector();
                vector1[1] = new Vector();
                for (int i = 0; i < _rt.table.getColumnCount(); i++) {
                    vector1[0].add(_rt.table.getColumnName(i));
                    vector1[1].add(_rt.table.getColumnClass(i).getName());
                }
                FileQueryDialog fqd = new FileQueryDialog(2, _fileN, vector1);
                fqd.setReportTable(_rt);
                fqd.setLocation(175, 100);
                fqd.setTitle(_fileN + " Query Dialog");
                fqd.setModal(true);
                fqd.pack();
                fqd.setVisible(true);
                int j = fqd.response;
                if (j == 1) {
                    _rt = fqd._rt;
                    if (delete_v == null) delete_v = new Vector<Integer>();
                    if (delrow_v == null) delrow_v = new Vector<Object[]>();
                    for (int i = 0; i < fqd.delete_v.size() && i < fqd.delrow_v.size(); i++) {
                        delete_v.add(fqd.delete_v.get(i));
                        delrow_v.add(fqd.delrow_v.get(i));
                    }
                    revalidate();
                    repaint();
                }
                return;
            }
            if (command.compareTo("addcolumn") == 0) {
                String input = (String) JOptionPane.showInputDialog(null, "Column to Add", "Column Add Dialog", JOptionPane.PLAIN_MESSAGE, null, null, "Column_" + (_rt.table.getColumnCount() + 1));
                if (input == null || input.equals("")) return;
                _rt.addColumn(input);
                return;
            }
            if (command.equals("hidecolumn")) {
                int index = selectedColIndex(_rt);
                if (index < 0) return;
                _rt.hideColumn(index);
                return;
            }
            if (command.equals("renamecolumn")) {
                int index = selectedColIndex(_rt);
                if (index < 0) return;
                String name = JOptionPane.showInputDialog("Column Name??", "New_Name");
                if (name == null || "".equals(name)) return;
                _rt.table.getColumnModel().getColumn(index).setHeaderValue(name);
                return;
            }
            if (command.equals("copycolumn")) {
                int index = selectedColIndex(_rt);
                if (index < 0) return;
                int row_c = _rt.table.getRowCount();
                copy_v = new Vector<Object>();
                for (int i = 0; i < row_c; i++) {
                    copy_v.addElement(_rt.table.getValueAt(i, index));
                }
                JOptionPane.showMessageDialog(null, copy_v.size() + " objects copied", "Information Message", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (command.equals("pastecolumn")) {
                int index = selectedColIndex(_rt);
                if (index < 0) return;
                if (copy_v == null || copy_v.size() == 0) {
                    JOptionPane.showMessageDialog(null, "Nothing to Paste \n Copy a Column First", "Information Message", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                int row_c = _rt.table.getRowCount();
                int vec_c = copy_v.size();
                for (int i = 0; (i < row_c) && (i < vec_c); i++) {
                    _rt.table.setValueAt(copy_v.get(i), i, index);
                }
                return;
            }
            if (command.equals("populatecolumn")) {
                int index = selectedColIndex(_rt);
                if (index < 0) return;
                int row_c = _rt.table.getRowCount();
                String[] popOption = new String[] { "Auto Incremenatal", "Expression Builder", "Random Generation" };
                String input = (String) JOptionPane.showInputDialog(null, "Choose population Type", "Select population Type", JOptionPane.INFORMATION_MESSAGE, null, popOption, popOption[0]);
                if (input == null || "".equals(input)) return;
                if ("Auto Incremenatal".equals(input)) {
                    String start = "";
                    int i = 0;
                    while ("".equals(start)) {
                        start = JOptionPane.showInputDialog("Choose Starting Number", 1);
                        if (start == null) return;
                        try {
                            i = Integer.parseInt(start);
                        } catch (NumberFormatException ne) {
                            start = "";
                            continue;
                        }
                    }
                    for (int j = 0; j < row_c; j++) {
                        _rt.table.setValueAt(i++, j, index);
                    }
                } else if ("Expression Builder".equals(input)) {
                    ExpressionBuilder eb = new ExpressionBuilder(_rt, index);
                } else {
                    RandomColGen rcg = new RandomColGen(_rt, index);
                }
                return;
            }
            if (command.equals("replace")) {
                int index = selectedColIndex(_rt);
                if (index < 0) return;
                String input = JOptionPane.showInputDialog("Replace Null with: \n For Date Object Format is dd-MM-yyyy");
                if (input == null || "".equals(input)) return;
                Object replace = null;
                Class cclass = _rt.table.getColumnClass(index);
                try {
                    if (cclass.getName().toUpperCase().contains("DOUBLE")) {
                        replace = Double.parseDouble(input);
                    } else if (cclass.getName().toUpperCase().contains("DATE")) {
                        replace = new SimpleDateFormat("dd-MM-yyyy").parse(input);
                    } else {
                        replace = new String(input);
                    }
                } catch (Exception exp) {
                    ConsoleFrame.addText("\n WANING: Could not Parse Input String:");
                }
                int row_c = _rt.table.getRowCount();
                _rt.cancelSorting();
                _rt.table.clearSelection();
                _rt.table.setColumnSelectionInterval(index, index);
                for (int i = 0; i < row_c; i++) {
                    Object obj = _rt.table.getValueAt(i, index);
                    if (obj == null || "".equals(obj.toString()) || obj.toString().compareToIgnoreCase("null") == 0) {
                        _rt.table.setValueAt(replace, i, index);
                        _rt.table.addRowSelectionInterval(i, i);
                    }
                }
                return;
            }
            if (command.equals("seareplace")) {
                int index = selectedColIndex(_rt);
                if (index < 0) return;
                Hashtable filterHash = null;
                JOptionPane.showMessageDialog(null, "Choose a file which has Key Value format like \n \"searchReplace.txt\"", "Information Message", JOptionPane.INFORMATION_MESSAGE);
                try {
                    File f = QualityListener.chooseFile("Select Search & Replace File");
                    if (f == null) return;
                    ConsoleFrame.addText("\n Selected File:" + f.toString());
                    filterHash = ParamParser.parseFile(f.toString());
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null, ioe.getMessage(), "IO Exception Dialog", JOptionPane.ERROR_MESSAGE);
                    ConsoleFrame.addText("\n ERROR: IO exception happened");
                }
                int row_c = _rt.table.getRowCount();
                _rt.cancelSorting();
                _rt.table.clearSelection();
                _rt.table.setColumnSelectionInterval(index, index);
                for (int i = 0; i < row_c; i++) {
                    Object obj = _rt.table.getValueAt(i, index);
                    if (obj == null) continue;
                    Enumeration en = filterHash.keys();
                    while (en.hasMoreElements()) {
                        String key = en.nextElement().toString();
                        String value = obj.toString().trim().replaceAll("\\s+", " ");
                        String valueTok[] = value.split(" ");
                        boolean matchFound = false;
                        for (int j = 0; j < valueTok.length; j++) {
                            try {
                                if (Pattern.matches(key, valueTok[j]) == true) {
                                    String newvalue = (String) filterHash.get(key);
                                    valueTok[j] = newvalue;
                                    matchFound = true;
                                    continue;
                                }
                            } catch (PatternSyntaxException pe) {
                                ConsoleFrame.addText("\n Pattern Compile Exception:" + pe.getMessage());
                                break;
                            }
                        }
                        if (matchFound == true) {
                            String newValue = "";
                            for (int j = 0; j < valueTok.length; j++) {
                                if (newValue.equals("") == false) newValue += " ";
                                newValue += valueTok[j];
                            }
                            _rt.table.setValueAt(newValue, i, index);
                            _rt.table.addRowSelectionInterval(i, i);
                        }
                    }
                }
                return;
            }
            if (command.equals("uppercase")) {
                caseFormat(1);
                return;
            }
            if (command.equals("lowercase")) {
                caseFormat(2);
                return;
            }
            if (command.equals("titlecase")) {
                caseFormat(3);
                return;
            }
            if (command.equals("sentencecase")) {
                caseFormat(4);
                return;
            }
            if (command.equals("format")) {
                int index = selectedColIndex(_rt);
                if (index < 0) return;
                int row_c = _rt.table.getRowCount();
                _rt.cancelSorting();
                FormatPattern fp = new FormatPattern(_rt.table.getColumnName(index));
                int response = fp.createDialog();
                if (response == 0) return;
                String type = fp.getType();
                Object[] pattern = fp.inputPatterns();
                if (pattern.length == 0) return;
                _rt.table.getColumnModel().getColumn(index).setCellRenderer(new MyCellRenderer(pattern[0].toString()));
                Object v = null;
                if (type.equals("Number")) {
                    for (int i = 0; i < row_c; i++) {
                        Object o = _rt.getValueAt(i, index);
                        if (o != null) v = parseNumber(o, pattern); else v = (Double) null;
                        _rt.setTableValueAt(v, i, index);
                    }
                    return;
                } else if (type.equals("Date")) {
                    for (int i = 0; i < row_c; i++) {
                        Object o = _rt.getValueAt(i, index);
                        if (o != null) v = parseDate(o, pattern); else v = (Date) null;
                        _rt.setTableValueAt(v, i, index);
                    }
                    return;
                } else {
                    try {
                        _rt.table.getColumnModel().getColumn(index).setCellEditor(new DefaultCellEditor(new JFormattedTextField(new MaskFormatter(pattern[0].toString()))));
                        for (int i = 0; i < row_c; i++) {
                            String o = _rt.getTextValueAt(i, index);
                            if (o != null) v = parseString(o, pattern); else v = (String) null;
                            if (v == null) _rt.setTableValueAt(v, i, index); else {
                                StringBuffer t;
                                if (type.equals("Phone")) t = phoneFormat(v.toString(), pattern[0].toString()); else t = toFormat(v.toString(), pattern[0].toString());
                                _rt.setTableValueAt(t.toString(), i, index);
                            }
                        }
                    } catch (Exception e_parse) {
                        ConsoleFrame.addText("\n ERROR:Parsing Exception happened");
                        JOptionPane.showMessageDialog(null, "Parsing Exception:" + e_parse.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
                    }
                }
                return;
            }
            if (command.equals("addrow")) {
                actionType = ADDITION;
                inputDialog();
                return;
            }
            if (command.equals("ok")) {
                d_f.dispose();
                ok_action(actionType);
                return;
            }
            if (command.equals("cancel")) {
                d_f.dispose();
                return;
            }
            if (command.equals("mcancel")) {
                init = false;
                d_m.dispose();
                return;
            }
            if (command.equals("deleterow")) {
                actionType = DELETION;
                inputDialog();
                return;
            }
            if (command.equals("analyse")) {
                int index = selectedColIndex(_rt);
                if (index < 0) return;
                Object[] colObj = getColObject(index);
                FileAnalysisPanel fp = new FileAnalysisPanel(colObj);
                fp.createAndShowGUI();
                return;
            }
            if (command.equals("analyseselected")) {
                Object[] colObj = getSelectedColObject();
                if (colObj == null) return;
                FileAnalysisPanel fp = new FileAnalysisPanel(colObj);
                fp.createAndShowGUI();
                return;
            }
            if (command.equals("simcheck")) {
                _rt.cancelSorting();
                SimilarityCheck sim = new SimilarityCheck(_rt);
                return;
            }
            if (command.equals("todb")) {
                mapDialog(true);
                return;
            }
            if (command.equals("fromdb")) {
                mapDialog(false);
                return;
            }
            if (command.equals("load")) {
                String[] query = getQString(true);
                if (query == null || query.length == 0) return;
                loadQuery(query);
                return;
            }
            if (command.equals("synch")) {
                String[] query = getQString(false);
                if (query == null || query.length == 0) return;
                synchQuery(query);
                return;
            }
            if (command.equals("filerow") || command.equals("filecol")) {
                ImportFile impF = new ImportFile(false);
                ReportTable rtable = impF.getTable();
                if (rtable == null) return;
                int colC = rtable.table.getColumnCount();
                int colCE = _rt.table.getColumnCount();
                if (command.equals("filerow")) {
                    if (colC != colCE) {
                        JOptionPane.showMessageDialog(null, "Column Count not Matching " + colC + " Columns and " + colCE + " Columns \n Will adjust Accordingly", "Error Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                int rowC = rtable.table.getRowCount();
                int rowCE = _rt.table.getRowCount();
                if (command.equals("filerow")) {
                    _rt.addRows(rowCE, rowC);
                    for (int i = 0; i < colC - colCE; i++) _rt.addColumn(rtable.table.getColumnName(colCE + i));
                    for (int i = 0; i < rowC; i++) {
                        Object[] obj = rtable.copyRow(i);
                        for (int j = 0; j < colC; j++) {
                            _rt.table.setValueAt(obj[j], rowCE + i, j);
                        }
                    }
                } else {
                    if (rowC > rowCE) _rt.addRows(rowCE, rowC - rowCE);
                    for (int i = 0; i < colC; i++) {
                        _rt.addColumn(rtable.table.getColumnName(i));
                    }
                    for (int i = 0; i < rowC; i++) {
                        Object[] obj = rtable.copyRow(i);
                        for (int j = 0; j < obj.length; j++) {
                            _rt.table.setValueAt(obj[j], i, colCE + j);
                        }
                    }
                }
                return;
            }
            if (command.equals("joinfile")) {
                ImportFile impF = new ImportFile(false);
                ReportTable rtable = impF.getTable();
                if (rtable == null) return;
                int lindex = selectedColIndex(_rt);
                if (lindex < 0) return;
                int rindex = selectedColIndex(rtable);
                if (rindex < 0) return;
                _rt = joinTables(_rt, lindex, rtable, rindex, 0);
                return;
            }
            if (command.equals("transrow")) {
                _rt.transposeTable();
                return;
            }
        } finally {
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private String[] getQString(boolean isLoad) {
        String[] query = null;
        table_s = new Vector<String>();
        column_s = new Vector<String>();
        Hashtable<String, Vector<String>> ht = new Hashtable<String, Vector<String>>();
        _ht = new Hashtable<String, Integer>();
        Vector<String> vc = null;
        String table, column;
        for (int i = 0; i < tf1.length; i++) {
            if (radio1[i].isSelected() == true) {
                table = table1[i].getSelectedItem().toString();
                column = col1[i].getSelectedItem().toString();
                _ht.put(table + column, i);
                table_s.add(table);
                column_s.add(column);
                vc = ht.get(table);
                if (vc == null) {
                    vc = new Vector<String>();
                    vc.add(0, column);
                    ht.put(table, vc);
                } else if (vc.indexOf(column) == -1) {
                    vc.add(vc.size(), column);
                    ht.put(table, vc);
                } else {
                    JOptionPane.showMessageDialog(null, "Duplicate Mapping: " + (i + 1) + " Row", "Error Message", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }
        }
        init = false;
        QueryBuilder qb = new QueryBuilder(Jdbc_conn123.getHValue("Database_DSN"), Jdbc_conn123.getDBType());
        if (isLoad) {
            unique_table_s = new Vector<String>();
            query = qb.get_mapping_query(ht, unique_table_s);
        } else {
            Vector<String> s_vc = qb.get_synch_mapping_query(table_s, column_s);
            query = new String[s_vc.size()];
            for (int a = 0; a < s_vc.size(); a++) query[a] = s_vc.get(a);
        }
        return query;
    }

    public Object[] getColObject(int colIndex) {
        int row_c = _rt.table.getRowCount();
        Object[] colObj = new Object[row_c];
        for (int i = 0; i < row_c; i++) colObj[i] = _rt.getValueAt(i, colIndex);
        return colObj;
    }

    public Object[] getSelectedColObject() {
        int c_s = _rt.table.getSelectedColumn();
        if (c_s < 0) return null;
        int r_c = _rt.table.getSelectedRowCount();
        if (r_c <= 0) return null;
        Object[] colObj = new Object[r_c];
        int[] rowS = _rt.table.getSelectedRows();
        for (int i = 0; i < rowS.length; i++) colObj[i] = _rt.getValueAt(rowS[i], c_s);
        return colObj;
    }

    private JDialog inputDialog() {
        ButtonGroup bg = new ButtonGroup();
        rb1 = new JRadioButton("Last Row");
        rb2 = new JRadioButton("From/At Row Index");
        bg.add(rb1);
        bg.add(rb2);
        rb1.setSelected(true);
        tf = new JFormattedTextField(NumberFormat.getIntegerInstance());
        tf.setValue(new Integer(0));
        JLabel row_n = new JLabel("# of Rows", JLabel.TRAILING);
        rn = new JFormattedTextField(NumberFormat.getIntegerInstance());
        rn.setValue(new Integer(1));
        JButton ok = new JButton("Ok");
        ok.setActionCommand("ok");
        ok.addActionListener(this);
        ok.addKeyListener(new KeyBoardListener());
        JButton cancel = new JButton("Cancel");
        cancel.setActionCommand("cancel");
        cancel.addActionListener(this);
        cancel.addKeyListener(new KeyBoardListener());
        JPanel dp = new JPanel();
        dp.setLayout(new GridLayout(4, 2));
        dp.add(rb1);
        dp.add(new JLabel());
        dp.add(rb2);
        dp.add(tf);
        dp.add(row_n);
        dp.add(rn);
        dp.add(ok);
        dp.add(cancel);
        d_f = new JDialog();
        d_f.setModal(true);
        d_f.setTitle("Option Dialog");
        d_f.setLocation(250, 250);
        d_f.getContentPane().add(dp);
        d_f.pack();
        d_f.setVisible(true);
        return d_f;
    }

    private JDialog mapDialog(boolean toDb) {
        init = false;
        TableItemListener tl = new TableItemListener();
        ColumnItemListener cl = new ColumnItemListener();
        int colC = _rt.table.getColumnCount();
        Vector vector = Jdbc_conn123.getTable();
        vector1 = new Vector[2];
        table1 = new JComboBox[colC];
        col1 = new JComboBox[colC];
        l1 = new JLabel[colC];
        radio1 = new JRadioButton[colC];
        tf1 = new JTextField[colC];
        JLabel[] condF = new JLabel[colC];
        JLabel[] showL = new JLabel[colC];
        queryString = new String[colC];
        vector1 = VariableQuery.populateTable(5, 0, 1, vector1);
        JPanel jp = new JPanel();
        SpringLayout layout = new SpringLayout();
        jp.setLayout(layout);
        ImageIcon imageicon = new ImageIcon("./Filter.gif", "Query");
        int imageLS = imageicon.getImageLoadStatus();
        for (int i = 0; i < colC; i++) {
            tf1[i] = new JTextField(8);
            tf1[i].setText(_rt.table.getColumnName(i));
            tf1[i].setEditable(false);
            tf1[i].setToolTipText(_rt.table.getColumnClass(i).getName());
            jp.add(tf1[i]);
            if (toDb) radio1[i] = new JRadioButton("Map to"); else radio1[i] = new JRadioButton("Synch From");
            if (toDb) radio1[i].setSelected(true); else radio1[i].setSelected(false);
            jp.add(radio1[i]);
            table1[i] = new JComboBox();
            table1[i].addItemListener(tl);
            for (int j = 0; j < vector.size(); j++) {
                String item = (String) vector.get(j);
                table1[i].addItem(item);
            }
            jp.add(table1[i]);
            col1[i] = new JComboBox();
            col1[i].addItemListener(cl);
            for (int j = 0; j < vector1[0].size(); j++) {
                String item = (String) vector1[0].get(j);
                col1[i].addItem(item);
            }
            jp.add(col1[i]);
            int va = ((Integer) (vector1[1].get(0))).intValue();
            l1[i] = new JLabel(SqlType.getTypeName(va));
            l1[i].setToolTipText("Data Type");
            jp.add(l1[i]);
            if (imageLS == MediaTracker.ABORTED || imageLS == MediaTracker.ERRORED) condF[i] = new JLabel("<html><body><a href=\"\">Query</A></body></html>", 0); else condF[i] = new JLabel(imageicon, JLabel.CENTER);
            condF[i].setToolTipText("Click to Add Conditions");
            if (toDb) condF[i].setVisible(false);
            condF[i].addMouseListener(new LinkMouseListener(i));
            jp.add(condF[i]);
            showL[i] = new JLabel("<html><body><a href=\"\">Show Condition</A></body></html>", 0);
            showL[i].setToolTipText("Click to show condition");
            if (toDb) showL[i].setVisible(false);
            showL[i].addMouseListener(new LinkMouseListener(i));
            jp.add(showL[i]);
        }
        SpringUtilities.makeCompactGrid(jp, colC, 7, 3, 3, 3, 3);
        JScrollPane jscrollpane1 = new JScrollPane(jp);
        if (colC * 35 > 400) jscrollpane1.setPreferredSize(new Dimension(575, 400)); else jscrollpane1.setPreferredSize(new Dimension(575, colC * 35));
        JPanel bp = new JPanel();
        JButton ok;
        JLabel smap = new JLabel("<html><body><a href=\"\">Save Mapping</A></body></html>", 0);
        smap.setToolTipText("Click to save Mapping");
        smap.addMouseListener(new LinkMouseListener(0));
        JLabel lmap = new JLabel("<html><body><a href=\"\">Load Mapping</A></body></html>", 0);
        lmap.setToolTipText("Click to load from Mapping File");
        lmap.addMouseListener(new LinkMouseListener(0));
        bp.add(smap);
        bp.add(lmap);
        if (toDb) {
            ok = new JButton("Load");
            ok.setActionCommand("load");
        } else {
            ok = new JButton("Synch");
            ok.setActionCommand("synch");
        }
        ok.addActionListener(this);
        ok.addKeyListener(new KeyBoardListener());
        bp.add(ok);
        JButton cancel = new JButton("Cancel");
        cancel.setActionCommand("mcancel");
        cancel.addActionListener(this);
        cancel.addKeyListener(new KeyBoardListener());
        bp.add(cancel);
        JPanel jp_p = new JPanel(new BorderLayout());
        jp_p.add(jscrollpane1, BorderLayout.CENTER);
        jp_p.add(bp, BorderLayout.PAGE_END);
        d_m = new JDialog();
        d_m.setModal(true);
        d_m.setTitle("Map Dialog");
        d_m.setLocation(250, 250);
        d_m.getContentPane().add(jp_p);
        init = true;
        d_m.pack();
        d_m.setVisible(true);
        return d_m;
    }

    private void ok_action(int actionType) {
        int actionR = ((Number) rn.getValue()).intValue();
        if (actionR < 1) actionR = 1;
        if (rb1.isSelected() == true) {
            if (actionType == ADDITION) _rt.addRows(_rt.table.getRowCount(), actionR); else {
                if (actionR >= _rt.table.getRowCount()) _rt.removeRows(0, _rt.table.getRowCount()); else _rt.removeRows(_rt.table.getRowCount() - actionR, actionR);
            }
        } else {
            int rowC = _rt.table.getRowCount();
            int insertR = ((Number) tf.getValue()).intValue();
            if (insertR >= rowC) insertR = rowC - 1;
            if (insertR < 0) insertR = 0;
            if (actionType == ADDITION) _rt.addRows(insertR, actionR); else {
                if ((insertR + actionR) >= rowC) _rt.removeRows(insertR, _rt.table.getRowCount() - insertR); else _rt.removeRows(insertR, actionR);
            }
        }
    }

    public static Double parseNumber(Object value, Object[] pattern) {
        Double d = null;
        if (value instanceof Double) return d = (Double) value;
        DecimalFormat format = new DecimalFormat();
        for (int i = 0; i < pattern.length; i++) {
            try {
                format.applyPattern(pattern[i].toString());
                d = new Double(format.parse(value.toString()).doubleValue());
                if (d != null) break;
            } catch (Exception e) {
                continue;
            }
        }
        return d;
    }

    public static Date parseDate(Object value, Object[] pattern) {
        Date d = null;
        if (value instanceof Date) return d = (Date) value;
        SimpleDateFormat format = new SimpleDateFormat();
        for (int i = 0; i < pattern.length; i++) {
            try {
                format.applyPattern(pattern[i].toString());
                d = format.parse(value.toString(), new ParsePosition(0));
                if (d != null) break;
            } catch (Exception e) {
                continue;
            }
        }
        return d;
    }

    public static Object parseString(String value, Object[] pattern) {
        MaskFormatter format = new MaskFormatter();
        format.setValueContainsLiteralCharacters(false);
        Object d = null;
        for (int i = 0; i < pattern.length; i++) {
            try {
                format.setMask(pattern[i].toString());
                d = format.stringToValue(value);
                if (d != null) break;
            } catch (Exception e) {
                continue;
            }
        }
        return d;
    }

    private class MyCellRenderer extends DefaultTableCellRenderer {

        private String _format;

        public MyCellRenderer(String format) {
            _format = format;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            StringBuffer text = null;
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Number) {
                DecimalFormat f = new DecimalFormat(_format);
                text = f.format(value, new StringBuffer(), new FieldPosition(0));
                ((JLabel) c).setHorizontalAlignment(JLabel.TRAILING);
                c.setForeground(Color.RED.darker());
            }
            if (value instanceof Date) {
                SimpleDateFormat f = new SimpleDateFormat(_format);
                text = f.format(value, new StringBuffer(), new FieldPosition(0));
                ((JLabel) c).setHorizontalAlignment(JLabel.LEADING);
                c.setForeground(Color.MAGENTA.darker());
            }
            if (value instanceof String) {
                text = new StringBuffer(value.toString());
                ((JLabel) c).setHorizontalAlignment(JLabel.LEADING);
                c.setForeground(Color.BLUE.darker());
            }
            if (text != null) ((JLabel) c).setText(text.toString());
            return c;
        }
    }

    private int selectedColIndex(ReportTable rt) {
        int colC = rt.table.getColumnCount();
        Object[] colN = new Object[colC];
        for (int i = 0; i < colC; i++) colN[i] = (i + 1) + "," + rt.table.getColumnName(i);
        String input = (String) JOptionPane.showInputDialog(null, "Select the Column ", "Column Selection Dialog", JOptionPane.PLAIN_MESSAGE, null, colN, colN[0]);
        if (input == null || input.equals("")) return -1;
        String col[] = input.split(",", 2);
        int index = Integer.valueOf(col[0]).intValue();
        return index - 1;
    }

    private StringBuffer toFormat(String value, String mask) {
        if (value == null) return null;
        StringBuffer output = new StringBuffer();
        int maskIndex = 0;
        int i = 0;
        while ((maskIndex < mask.length()) && (i < value.length())) {
            char c_m = mask.charAt(maskIndex);
            if (c_m == '#' && Character.isDigit(value.charAt(i))) output.append(value.charAt(i)); else if (c_m == 'U' && Character.isLetter(value.charAt(i))) output.append(Character.toUpperCase(value.charAt(i))); else if (c_m == 'L' && Character.isLetter(value.charAt(i))) output.append(Character.toLowerCase(value.charAt(i))); else if (c_m == '?' && Character.isLetter(value.charAt(i))) output.append(value.charAt(i)); else if (c_m == 'A' && (Character.isLetter(value.charAt(i)) || Character.isDigit(value.charAt(i)))) output.append(value.charAt(i)); else if (c_m == '*') output.append(value.charAt(i)); else if (c_m == 'H' && (Character.isDigit(value.charAt(i)) || value.charAt(i) == 'a' || value.charAt(i) == 'A' || value.charAt(i) == 'b' || value.charAt(i) == 'B' || value.charAt(i) == 'c' || value.charAt(i) == 'C' || value.charAt(i) == 'd' || value.charAt(i) == 'D' || value.charAt(i) == 'e' || value.charAt(i) == 'E' || value.charAt(i) == 'f' || value.charAt(i) == 'F')) output.append(value.charAt(i)); else if (c_m == '\'' && (value.length() > (i + 1))) {
                i++;
                maskIndex++;
                output.append(value.charAt(i));
            } else {
                output.append(mask.charAt(maskIndex));
                i--;
            }
            maskIndex++;
            i++;
        }
        return output;
    }

    private StringBuffer phoneFormat(String value, String mask) {
        if (value == null) return null;
        StringBuffer output = new StringBuffer();
        int maskIndex = mask.length() - 1;
        int i = value.length() - 1;
        while ((maskIndex >= 0) && (i >= 0)) {
            char c_m = mask.charAt(maskIndex);
            if (c_m == '#' && Character.isDigit(value.charAt(i))) output.append(value.charAt(i)); else if (c_m == 'U' && Character.isLetter(value.charAt(i))) output.append(Character.toUpperCase(value.charAt(i))); else if (c_m == 'L' && Character.isLetter(value.charAt(i))) output.append(Character.toLowerCase(value.charAt(i))); else if (c_m == '?' && Character.isLetter(value.charAt(i))) output.append(value.charAt(i)); else if (c_m == 'A' && (Character.isLetter(value.charAt(i)) || Character.isDigit(value.charAt(i)))) output.append(value.charAt(i)); else if (c_m == '*') output.append(value.charAt(i)); else if (c_m == 'H' && (Character.isDigit(value.charAt(i)) || value.charAt(i) == 'a' || value.charAt(i) == 'A' || value.charAt(i) == 'b' || value.charAt(i) == 'B' || value.charAt(i) == 'c' || value.charAt(i) == 'C' || value.charAt(i) == 'd' || value.charAt(i) == 'D' || value.charAt(i) == 'e' || value.charAt(i) == 'E' || value.charAt(i) == 'f' || value.charAt(i) == 'F')) output.append(value.charAt(i)); else {
                output.append(mask.charAt(maskIndex));
                i++;
            }
            maskIndex--;
            i--;
        }
        if (maskIndex == -1) return output.reverse();
        while (maskIndex >= 0) {
            char c_m = mask.charAt(maskIndex);
            if (c_m == '#' || c_m == 'U' || c_m == 'L' || c_m == '?' || c_m == 'A' || c_m == '*' || c_m == 'H') output.append('0'); else output.append(c_m);
            maskIndex--;
        }
        return output.reverse();
    }

    private class TableItemListener implements ItemListener {

        private int index = 0;

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED && init == true) {
                for (index = 0; index < table1.length; index++) {
                    if (e.getSource().equals(table1[index])) {
                        int s_index = table1[index].getSelectedIndex();
                        vector1 = VariableQuery.populateTable(5, s_index, s_index + 1, vector1);
                        int va = ((Integer) (vector1[1].get(0))).intValue();
                        col1[index].removeAllItems();
                        for (int i = 0; i < vector1[0].size(); i++) {
                            String item = (String) vector1[0].get(i);
                            col1[index].addItem(item);
                        }
                        l1[index].setText(SqlType.getTypeName(va));
                        queryString[index] = "";
                    }
                }
            }
        }
    }

    private class ColumnItemListener implements ItemListener {

        private int index = 0;

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED && init == true) {
                for (index = 0; index < col1.length; index++) {
                    if (e.getSource().equals(col1[index])) {
                        int s_index = table1[index].getSelectedIndex();
                        vector1 = VariableQuery.populateTable(5, s_index, s_index + 1, vector1);
                        int va = ((Integer) (vector1[1].get(col1[index].getSelectedIndex()))).intValue();
                        l1[index].setText(SqlType.getTypeName(va));
                    }
                }
            }
        }
    }

    private void loadQuery(final String[] query) {
        final int count = _rt.table.getRowCount();
        _rt.cancelSorting();
        if (d_m != null) d_m.dispose();
        Thread[] tid = new Thread[query.length];
        for (int qindex = 0; qindex < query.length; qindex++) {
            final int cIndex = qindex;
            tid[cIndex] = new Thread(new Runnable() {

                public void run() {
                    int fcount = 0;
                    String tbl[] = null;
                    String col[] = null;
                    try {
                        RowsetTable rs = new RowsetTable(query[cIndex], 0);
                        tbl = rs.getTableName();
                        col = rs.getColName();
                        rs.moveToFirst();
                        for (int c = 0; c < count; c++) {
                            Object[] obj = new Object[col.length];
                            for (int i = 0; i < col.length; i++) {
                                Integer index = _ht.get(unique_table_s.get(cIndex) + col[i]);
                                if (index == null) continue;
                                obj[i] = _rt.table.getValueAt(c, index.intValue());
                            }
                            try {
                                rs.insertRow(obj);
                            } catch (SQLException sql_e) {
                                ConsoleFrame.addText("\n Row Id:" + (c + 1) + " Error-" + sql_e.getMessage() + " For Table: " + unique_table_s.get(cIndex));
                                fcount++;
                                continue;
                            }
                        }
                        rs.close();
                    } catch (SQLException e) {
                        ConsoleFrame.addText("\n Error-" + e.getMessage() + " For Table: " + unique_table_s.get(cIndex));
                    }
                    ConsoleFrame.addText("\n " + (count - fcount) + " of Total " + count + " Rows Inserted Successfully in table :" + unique_table_s.get(cIndex));
                }
            });
            tid[cIndex].start();
        }
        for (int i = 0; i < query.length; i++) {
            try {
                tid[i].join();
            } catch (Exception e) {
                ConsoleFrame.addText("\n Thread Error:" + e.getMessage());
            }
        }
    }

    private void synchQuery(final String[] query) {
        final int count = _rt.table.getRowCount();
        _rt.cancelSorting();
        if (d_m != null) d_m.dispose();
        final Object[][] stored = new Object[query.length][count];
        final int[] cI = new int[query.length];
        Thread[] tid = new Thread[query.length];
        for (int qindex = 0; qindex < query.length; qindex++) {
            final int cIndex = qindex;
            tid[cIndex] = new Thread(new Runnable() {

                public void run() {
                    String tbl = table_s.get(cIndex);
                    String col = column_s.get(cIndex);
                    String newQuery = query[cIndex];
                    Integer tab_index = _ht.get(tbl + col);
                    if (tab_index == null) return;
                    if (!(queryString[tab_index] == null || "".equals(queryString[tab_index]))) newQuery = newQuery + " WHERE " + queryString[tab_index];
                    cI[cIndex] = tab_index;
                    try {
                        RowsetTable rs = new RowsetTable(newQuery, count);
                        for (int c = 0; c < count; c++) stored[cIndex][c] = rs.getObject(c + 1, 1);
                        rs.close();
                    } catch (SQLException e) {
                        ConsoleFrame.addText("\n Error-" + e.getMessage() + " For Table: " + tbl);
                    }
                }
            });
            tid[cIndex].start();
        }
        for (int i = 0; i < query.length; i++) {
            try {
                tid[i].join();
            } catch (Exception e) {
                ConsoleFrame.addText("\n Thread Error:" + e.getMessage());
            }
        }
        for (int c = 0; c < count; c++) for (int j = 0; j < query.length; j++) _rt.table.setValueAt(stored[j][c], c, cI[j]);
    }

    private class LinkMouseListener extends MouseAdapter {

        int _index;

        public void mouseClicked(MouseEvent mouseevent) {
            try {
                mouseevent.getComponent().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
                String s1 = ((JLabel) mouseevent.getSource()).getText();
                if (s1 != null && s1.equals("<html><body><a href=\"\">Show Condition</A></body></html>")) {
                    String qry_msg = queryString[_index];
                    if (qry_msg == null || "".equals(qry_msg)) qry_msg = "Condition Not Set";
                    JOptionPane.showMessageDialog(null, qry_msg, "Query Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else if (s1 != null && s1.equals("<html><body><a href=\"\">Save Mapping</A></body></html>")) {
                    String[] query = getQString(true);
                    if (query == null) return;
                    ReportTable smTable = new ReportTable(new String[] { "File", "Field", "Table", "Column", "Data_Type" });
                    int colC = _rt.table.getColumnCount();
                    for (int i = 0; i < colC; i++) {
                        if (radio1[i].isSelected() == true) {
                            String[] row = new String[] { _fileN, tf1[i].getText(), table1[i].getSelectedItem().toString(), col1[i].getSelectedItem().toString(), l1[i].getText() };
                            smTable.addFillRow(row);
                        }
                    }
                    smTable.saveAsXml();
                } else if (s1 != null && s1.equals("<html><body><a href=\"\">Load Mapping</A></body></html>")) {
                    try {
                        File f = QualityListener.chooseFile("Select Mapping File");
                        if (f == null || f.getName().toLowerCase().endsWith(".xml") == false) return;
                        final XmlReader xmlReader = new XmlReader();
                        ReportTable lmTable = xmlReader.read(f);
                        if (lmTable == null) return;
                        if (_fileN.equals(lmTable.getValueAt(0, 0)) == false) {
                            int n = JOptionPane.showConfirmDialog(null, "File Name not Matching. \n Do you wish to Continue ?", "File Not Matching", JOptionPane.YES_NO_OPTION);
                            if (n == JOptionPane.NO_OPTION) return;
                        }
                        int colC = _rt.table.getColumnCount();
                        Vector<String> colName = new Vector<String>();
                        for (int i = 0; i < colC; i++) {
                            radio1[i].setSelected(false);
                            colName.add(_rt.table.getColumnName(i));
                        }
                        int lrowC = lmTable.table.getRowCount();
                        for (int i = 0; i < lrowC; i++) {
                            int index = colName.indexOf(lmTable.getValueAt(i, 1));
                            if (index < 0 || index > colC) {
                                ConsoleFrame.addText("\n Index out of range:" + index);
                                return;
                            }
                            radio1[index].setSelected(true);
                            String tableN = lmTable.getValueAt(i, 2).toString();
                            String colN = lmTable.getValueAt(i, 3).toString();
                            int tableI = table1[index].getItemCount();
                            for (int j = 0; j < tableI; j++) {
                                if (tableN.equals(table1[index].getItemAt(j))) table1[index].setSelectedIndex(j);
                            }
                            int colI = col1[index].getItemCount();
                            for (int j = 0; j < colI; j++) {
                                if (colN.equals(col1[index].getItemAt(j))) col1[index].setSelectedIndex(j);
                            }
                        }
                    } catch (Exception e) {
                        ConsoleFrame.addText("\n Exception: Load Mapping File Exception");
                        ConsoleFrame.addText("\n " + e.getMessage());
                    }
                } else {
                    Vector vector = Jdbc_conn123.getTable();
                    int i = vector.indexOf(table1[_index].getSelectedItem().toString());
                    Vector avector[] = null;
                    avector = VariableQuery.populateTable(5, i, i + 1, avector);
                    QueryDialog querydialog = new QueryDialog(2, table1[_index].getSelectedItem().toString(), avector);
                    querydialog.setColumn(col1[_index].getSelectedItem().toString());
                    querydialog.setLocation(175, 100);
                    querydialog.setTitle(" DataQuality Query Setup ");
                    querydialog.setModal(true);
                    querydialog.pack();
                    querydialog.setVisible(true);
                    int j = querydialog.response;
                    if (j == 1) {
                        queryString[_index] = querydialog.cond;
                    }
                    return;
                }
            } finally {
                mouseevent.getComponent().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        }

        public void mouseEntered(MouseEvent mouseevent) {
            mouseevent.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        private LinkMouseListener(int index) {
            _index = index;
        }
    }

    public static ReportTable joinTables(ReportTable leftT, int indexL, ReportTable rightT, int indexR, int joinType) {
        leftT.cancelSorting();
        Vector<Object> lvc = new Vector<Object>();
        Vector<Object> rvc = new Vector<Object>();
        int lrow_c = leftT.table.getRowCount();
        int rrow_c = rightT.table.getRowCount();
        for (int i = 0; i < lrow_c; i++) {
            lvc.addElement(leftT.table.getValueAt(i, indexL));
        }
        for (int i = 0; i < rrow_c; i++) {
            rvc.addElement(rightT.table.getValueAt(i, indexR));
        }
        int rcolc = rightT.table.getColumnCount();
        int lcolc = leftT.table.getColumnCount();
        for (int i = 0; (i < rcolc); i++) {
            if (i == indexR) continue;
            leftT.addColumn(rightT.table.getColumnName(i));
        }
        switch(joinType) {
            case 0:
                for (int i = 0; i < lrow_c; i++) {
                    int i_find = rvc.indexOf(lvc.get(i));
                    if (i_find != -1) {
                        int curC = lcolc;
                        for (int j = 0; (j < rcolc); j++) {
                            if (j == indexR) continue;
                            leftT.table.setValueAt(rightT.table.getValueAt(i_find, j), i, curC++);
                        }
                    }
                }
                break;
        }
        return leftT;
    }

    private void caseFormat(int caseType) {
        int index = selectedColIndex(_rt);
        if (index < 0) return;
        String name = _rt.table.getColumnClass(index).getName();
        if (name.toUpperCase().contains("STRING") == false) return;
        int rowC = _rt.table.getRowCount();
        char defChar = '.';
        if (caseType == 4) {
            Locale defLoc = Locale.getDefault();
            ConsoleFrame.addText("\n Default Locale is :" + defLoc);
            if (defLoc.equals(Locale.US) || defLoc.equals(Locale.UK) || defLoc.equals(Locale.CANADA)) {
            } else {
                String response = JOptionPane.showInputDialog(null, "Please enter the end of Line Character ?", "Language End Line Input", JOptionPane.QUESTION_MESSAGE);
                if (response == null || "".equals(response)) {
                } else defChar = response.charAt(0);
            }
        }
        for (int i = 0; i < rowC; i++) {
            if (_rt.table.getValueAt(i, index) == null) continue;
            String prevC = _rt.table.getValueAt(i, index).toString();
            switch(caseType) {
                case 1:
                    prevC = CaseFormatUtil.toUpperCase(prevC);
                    break;
                case 2:
                    prevC = CaseFormatUtil.toLowerCase(prevC);
                    break;
                case 3:
                    prevC = CaseFormatUtil.toTitleCase(prevC);
                    break;
                case 4:
                    prevC = CaseFormatUtil.toSentenceCase(prevC, defChar);
                    break;
                default:
            }
            _rt.table.setValueAt(prevC, i, index);
        }
    }
}

package imogenart.cp;

import java.awt.BorderLayout;
import java.awt.CheckboxGroup;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

public class ShowTable extends JPanel implements java.awt.event.ActionListener, ComponentListener, WindowListener, Serializable {

    private boolean DEBUG = false;

    boolean printok = false;

    private void p(String a) {
        if (printok) System.out.println("ShowTable::" + a);
    }

    private static JFrame frame;

    private static FileMenu menubar = new FileMenu();

    public static CPtabs parent;

    JScrollPane scrollPane;

    private JPanel bottom = new JPanel();

    private CPGcolors CPGcolors;

    private JTable table;

    private String path;

    private JTextArea log;

    /** this String vector contains the table contents and is created by MyTableModel */
    HifStackString v = new HifStackString();

    private JButton figButton;

    private JButton defaultButton;

    private JTextField fontField;

    private JTextField diamField;

    private JTextField gapField;

    private JTextField htField;

    private JTextField wdField;

    private JTextField colField;

    private JComboBox fontchoice = new JComboBox();

    private JComboBox fontchoice2 = new JComboBox();

    private String[] fontdescription;

    private String foundata = "";

    private JPanel foroptions = new JPanel();

    private JPanel blank = new JPanel();

    private JCheckBox check4vertlabels;

    private JCheckBox check4vertlabels2;

    private JCheckBox checkyellowbox;

    private JCheckBox checkcrossbox;

    private JCheckBox checkGap4row2;

    private JCheckBox checkGap4row1;

    private static String FONTCHOICE = "";

    private static String FONTCHOICE2 = "";

    private static boolean ADDVERTLABELS = true;

    private static boolean ADD2ndVERTLABELS = true;

    private static boolean YELLOWBOX = true;

    private static boolean gaps4Row2 = false;

    private static boolean gaps4Row1 = false;

    private boolean CROSS = false;

    private String colourFileName;

    private static int defaultColSep = 30;

    private static int defaultGap = 3;

    private static int defaultHi = 30;

    private static int defaultWdV = (defaultGap * 3);

    private static int defaultDiam = 25;

    private static int defaultFon = 10;

    private static String defaultFonttype = "SansSerif";

    private static int ColSep = 40;

    private static int Gap = 16;

    private static int Hi = 40;

    private static int WdV = Gap;

    private static int Diam = 35;

    private static int Fon = 12;

    static String gowindowfile = ".cpg/showtable";

    private static Point oldlocation;

    private static Dimension oldscreensize;

    private static boolean prefasktoclose = true;

    private static String currecpath = "~";

    private static boolean ShowTableusedefaults = false;

    private Options options;

    private GridBagLayout gb = new GridBagLayout();

    private GridBagConstraints constraints = new GridBagConstraints();

    private Font smallerfont;

    private boolean disableFig = false;

    public JFrame frame() {
        return frame;
    }

    Hashtable splitpanes = new Hashtable();

    JSplitPane secondsplit;

    JSplitPane split1;

    public ShowTable(String path, JTextArea parentlog, String ColourFile, boolean ShowTableusedefaults, CPGcolors cols, CPtabs mainw) {
        super();
        parent = mainw;
        parent.addwindow();
        setLayout(new BorderLayout());
        smallerfont = new JButton().getFont();
        smallerfont = new Font(smallerfont.getFamily(), smallerfont.getSize() - 2, smallerfont.getStyle());
        this.ShowTableusedefaults = ShowTableusedefaults;
        log = parentlog;
        CPGcolors = cols;
        colourFileName = ColourFile;
        log("Preparing colors from file " + ColourFile + "\n");
        table = new JTable(new MyTableModel(path));
        table.setPreferredScrollableViewportSize(new Dimension(100, 300));
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        fontField = new JTextField(5);
        diamField = new JTextField(5);
        gapField = new JTextField(5);
        htField = new JTextField(5);
        wdField = new JTextField(5);
        colField = new JTextField(5);
        String sFon = Integer.toString(Fon);
        String sDiam = Integer.toString(Diam);
        String sWdV = Integer.toString(WdV);
        String sHi = Integer.toString(Hi);
        String sGap = Integer.toString(Gap);
        String sColSep = Integer.toString(Hi);
        fontField.setText(sFon);
        diamField.setText(sDiam);
        gapField.setText(sWdV);
        htField.setText(sHi);
        wdField.setText(sGap);
        colField.setText(sColSep);
        JLabel fontLabel = new JLabel(" Font Size (7-15 pt) ");
        JLabel diamLabel = new JLabel(" Pie Diameter (5-78 px) ");
        JLabel gapLabel = new JLabel(" Group Padding (2-80 px) ");
        JLabel wdLabel = new JLabel(" Subgroup Padding (0-40 px) ");
        JLabel htLabel = new JLabel(" Row Height (10-80 px) ");
        JLabel colLabel = new JLabel(" Column Gap (0-400) px) ");
        JPanel sizePanel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout g = new GridBagLayout();
        g.setConstraints(sizePanel, c);
        sizePanel.setLayout(g);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.4;
        sizePanel.add(fontLabel, c);
        c.gridy++;
        sizePanel.add(htLabel, c);
        c.gridy++;
        sizePanel.add(diamLabel, c);
        c.gridy++;
        sizePanel.add(wdLabel, c);
        c.gridy++;
        sizePanel.add(gapLabel, c);
        c.gridy++;
        sizePanel.add(colLabel, c);
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 0.1;
        sizePanel.add(fontField, c);
        c.gridy++;
        sizePanel.add(htField, c);
        c.gridy++;
        sizePanel.add(diamField, c);
        c.gridy++;
        sizePanel.add(wdField, c);
        c.gridy++;
        sizePanel.add(gapField, c);
        c.gridy++;
        sizePanel.add(colField, c);
        c.gridy = 0;
        check4vertlabels = new JCheckBox("Show Top Group Label", true);
        check4vertlabels2 = new JCheckBox("Show Secondary Label", true);
        checkyellowbox = new JCheckBox("Mark Group Borders", false);
        checkcrossbox = new JCheckBox("Transpose", false);
        checkGap4row2 = new JCheckBox("Gap for second headers", false);
        checkGap4row1 = new JCheckBox("No gaps", false);
        checkGap4row2.addActionListener(this);
        checkGap4row1.addActionListener(this);
        CheckboxGroup group = new CheckboxGroup();
        CheckboxGroup group1 = new CheckboxGroup();
        JPanel groupPan = new JPanel();
        groupPan.setLayout(new BoxLayout(groupPan, BoxLayout.Y_AXIS));
        groupPan.add(checkGap4row2, group, 0);
        groupPan.add(checkGap4row1, group, 1);
        JPanel groupPan1 = new JPanel();
        groupPan1.setLayout(new BoxLayout(groupPan1, BoxLayout.Y_AXIS));
        groupPan1.add(checkyellowbox, group1, 0);
        groupPan1.add(checkcrossbox, group1, 1);
        c.gridx = 2;
        c.weightx = 0.5;
        sizePanel.add(check4vertlabels, c);
        c.gridy++;
        sizePanel.add(check4vertlabels2, c);
        c.gridy++;
        sizePanel.add(groupPan1, c);
        c.gridy++;
        sizePanel.add(groupPan, c);
        c.gridy++;
        TextCanvas tc = new TextCanvas(CPGcolors.getColorArray());
        fontdescription = tc.getFontDescription();
        for (int i = 0; i < 3; i++) {
            fontchoice.addItem(fontdescription[i]);
        }
        for (int i = 0; i < 3; i++) {
            fontchoice2.addItem(fontdescription[i]);
        }
        JPanel check1 = new JPanel();
        check1.add(fontchoice);
        check1.add(new JLabel("Main font"));
        JPanel check2 = new JPanel();
        check2.add(fontchoice2);
        check2.add(new JLabel("Pie labels"));
        sizePanel.add(check1, c);
        c.gridy++;
        sizePanel.add(check2, c);
        defaultButton = new JButton("Default options");
        defaultButton.addActionListener(this);
        figButton = new JButton("Figure");
        figButton.addActionListener(this);
        options = new Options(sizePanel, sizePanel.getPreferredSize().width, defaultButton);
        options.hideOptions();
        foroptions.add(options);
        JPanel figPan = new JPanel();
        bottom.setLayout(new BorderLayout());
        bottom.add(figButton, BorderLayout.CENTER);
        secondsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, foroptions, bottom);
        split1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, secondsplit);
        split1.setEnabled(false);
        secondsplit.setEnabled(false);
        this.add(split1);
        splitpanes.put(0, split1);
        splitpanes.put(1, secondsplit);
        foroptions.addComponentListener(this);
        if (disableFig) {
            figButton.setEnabled(false);
        }
    }

    public void addNotify() {
        super.addNotify();
        if (foundata.equals("")) {
            figButton.requestFocus();
        } else {
            super.getRootPane().setDefaultButton(figButton);
        }
    }

    private void log(String message) {
        p(message);
        log.append(message);
        log.setCaretPosition(log.getSelectionEnd());
    }

    public void actionPerformed(java.awt.event.ActionEvent ignore) {
        if (ignore.equals((String) "Print")) {
            MessageFormat header = new MessageFormat("Page {0,number,integer}");
            try {
                table.print(JTable.PrintMode.FIT_WIDTH, header, null);
            } catch (java.awt.print.PrinterException e) {
                System.err.format("Cannot print %s%n", e.getMessage());
            }
        } else if (ignore.getSource() == defaultButton) {
            ShowTableusedefaults = true;
            Gap = defaultGap;
            Diam = defaultDiam;
            Hi = defaultHi;
            WdV = defaultWdV;
            Fon = defaultFon;
            ColSep = (Hi);
            ADDVERTLABELS = true;
            ADD2ndVERTLABELS = true;
            FONTCHOICE = defaultFonttype;
            FONTCHOICE2 = defaultFonttype;
            CROSS = false;
            store();
            check4vertlabels.setSelected(ADDVERTLABELS);
            check4vertlabels2.setSelected(ADD2ndVERTLABELS);
            checkyellowbox.setSelected(YELLOWBOX);
            checkcrossbox.setSelected(CROSS);
            checkGap4row2.setSelected(false);
            checkGap4row1.setSelected(false);
            fontchoice.setSelectedItem(FONTCHOICE);
            fontchoice2.setSelectedItem(FONTCHOICE2);
            String sFon = Integer.toString(Fon);
            String sDiam = Integer.toString(Diam);
            String sWdV = Integer.toString(WdV);
            String sHi = Integer.toString(Hi);
            String sGap = Integer.toString(Gap);
            String sColSep = Integer.toString(ColSep);
            fontField.setText(sFon);
            diamField.setText(sDiam);
            gapField.setText(sWdV);
            htField.setText(sHi);
            wdField.setText(sGap);
            colField.setText(sColSep);
        } else if (ignore.getSource() == checkGap4row2) {
            boolean x = checkGap4row2.isSelected();
            p("Got " + x + " for check Gap 2");
            if (x) checkGap4row1.setSelected(false);
        } else if (ignore.getSource() == checkGap4row1) {
            boolean x = checkGap4row1.isSelected();
            p("Got " + x + " for check Gap 1");
            if (x) checkGap4row2.setSelected(false);
        } else if (ignore.getSource() == figButton) {
            ShowTableusedefaults = false;
            String sGap = gapField.getText();
            if (!sGap.equals("")) Gap = new Integer(sGap);
            String sDiam = diamField.getText();
            if (!sDiam.equals("")) Diam = new Integer(sDiam);
            String sHi = htField.getText();
            if (!sHi.equals("")) {
                Hi = new Integer(sHi);
            }
            String sWd = wdField.getText();
            if (!sWd.equals("")) {
                WdV = new Integer(sWd);
            }
            String sFon = fontField.getText();
            if (!sFon.equals("")) {
                Fon = new Integer(sFon);
            }
            if (Fon > 15) {
                Fon = 15;
                log("Font cannot be larger than " + Fon + "\n");
                fontField.setText(Integer.toString(Fon));
            }
            if (Fon < 7) {
                Fon = 7;
                log("Font cannot be smaller than " + Fon + "\n");
                fontField.setText(Integer.toString(Fon));
            }
            if (Hi > 80) {
                Hi = 80;
                log("Height of pie squares cannot be larger than " + Hi + "\n");
                htField.setText(Integer.toString(Hi));
            }
            if (Hi < 10) {
                Hi = 10;
                log("Height of pie squares cannot be smaller than " + Hi + "\n");
                htField.setText(Integer.toString(Hi));
            }
            if (WdV > Hi / 2) {
                WdV = Hi / 2;
                log("Gap between pie squares cannot be larger than half height (" + (Hi / 2) + ")\n");
                wdField.setText(Integer.toString(WdV));
            }
            if (WdV < 1) {
                WdV = 1;
                log("Gap between pie squares cannot be less than " + WdV + "\n");
                wdField.setText(Integer.toString(WdV));
            }
            if (Gap < 1) {
                Gap = 1;
                log("Gap between groups of pies cannot be less than " + Gap + "\n");
                gapField.setText(Integer.toString(Gap));
            }
            if (Gap > (Hi * 4)) {
                Gap = (Hi * 4);
                log("Gap between groups of pies cannot be more than 4Xheight (" + Gap + ")\n");
                gapField.setText(Integer.toString(Gap));
            }
            if (Diam < 5) {
                Diam = 5;
                log("Diameter of pies cannot be less than " + Diam + "\n");
                diamField.setText(Integer.toString(Diam));
            }
            if (Diam > (Hi - 2)) {
                Diam = Hi - 2;
                log("Diameter of pies cannot exceed height of pie squares-2 (" + (Hi - 2) + ")\n");
                diamField.setText(Integer.toString(Diam));
            }
            if (ColSep < 1) {
                ColSep = Hi;
                log("Gap between pie columns cannot be less than " + (Hi) + "\n");
                colField.setText(Integer.toString(ColSep));
            }
            if (ColSep > (Hi * 5)) {
                ColSep = Hi * 5;
                log("Gap between pie columns cannot exceed 5 x height of pie squares (" + (Hi * 5) + ")\n");
                colField.setText(Integer.toString(ColSep));
            }
            ADDVERTLABELS = check4vertlabels.isSelected();
            ADD2ndVERTLABELS = check4vertlabels2.isSelected();
            YELLOWBOX = checkyellowbox.isSelected();
            CROSS = checkcrossbox.isSelected();
            gaps4Row2 = checkGap4row2.isSelected();
            gaps4Row1 = checkGap4row1.isSelected();
            FONTCHOICE = (String) fontchoice.getSelectedItem();
            FONTCHOICE2 = (String) fontchoice2.getSelectedItem();
            ColSep = Integer.parseInt(colField.getText());
            store();
            new printit(v, path, log, table, Gap, Hi, Diam, Fon, WdV, ADDVERTLABELS, ADD2ndVERTLABELS, YELLOWBOX, CROSS, gaps4Row2, gaps4Row1, ColSep, FONTCHOICE, FONTCHOICE2, CPGcolors);
        }
    }

    /** Inner Class MyTableModel to extract and parse data
	     * If the data appears IN THE table then parsing was successful - 
	     * errors have to come out in the logging.
	     * @author lime
	     *
	     */
    public class MyTableModel extends AbstractTableModel {

        private String[] columnNames;

        private Object[][] data;

        public MyTableModel(String path) {
            log("Opening data file: " + path + " to put into Table\n");
            int HeaderLineNo = 4;
            int DataLineStart = 0;
            boolean DataLineStartSet = false;
            String HeaderLine = "";
            String delim = ",";
            try {
                FileIO fio = new FileIO();
                if (!fio.openReadFile(path)) {
                    log("Can't open data file" + path + "\n");
                } else {
                    log("Reading data file " + path + "\n");
                    int count = 0;
                    String t = fio.readDataLine();
                    int countline = 1;
                    delim = "\t";
                    log("Default delimiter is '" + delim + "'\n");
                    while (t != null) {
                        if (t.contains(",") && delim.equals("\t")) {
                            delim = ",";
                            log("Delimiter is now '" + delim + "'\n");
                        }
                        if (delim.equals(",")) t.replaceAll("\\s", "");
                        if (t.contains(delim)) {
                            if (!DataLineStartSet) {
                                v.push(t);
                                log("Header line (ignoring first two columns): " + t + "\n");
                            }
                        } else {
                            log("ERROR: No delimiter found (, or <tab>) - not using line: " + t + "\n");
                        }
                        if (countline > 3 && (t.contains("+") || t.contains("-"))) {
                            foundata = "+";
                            if (!DataLineStartSet) {
                                DataLineStart = countline - 1;
                                DataLineStartSet = true;
                                log("CPG thinks you are using - and + data. [If this is not correct please report as a bug with your input file ]\n");
                            } else {
                                v.push(t);
                            }
                        } else if (countline > 3 && (t.contains("0") || t.contains("1"))) {
                            foundata = "1";
                            if (!DataLineStartSet) {
                                DataLineStart = countline - 1;
                                DataLineStartSet = true;
                                log("CPG thinks you are using 0 and 1 as data rather than - and +. [If this is not correct please report as a bug with your input file ]\n");
                                log("Not using line: " + t + "\n");
                            } else {
                                v.push(t);
                            }
                        }
                        count++;
                        if (count == (HeaderLineNo - 1)) {
                            HeaderLine = t;
                            log("Headers (pie labels left) on line " + HeaderLineNo + ": " + HeaderLine + "\n");
                        }
                        t = fio.readDataLine();
                        countline++;
                    }
                    if (fio.closeReadFile()) {
                        log("Data file closed\n");
                    } else {
                        log("Can't close data file, sorry\n");
                    }
                    if (foundata.equals("")) {
                        log("STOP !!! I can't get your data type: did you use 0,1 or -,+ to indicate pie fillings?");
                        log("Not using " + t + "\n");
                        figButton.setEnabled(false);
                    }
                }
            } catch (Exception e) {
                log("Can't open file " + path + " due to: " + e);
            }
            int NoDataRows = v.size() - DataLineStart;
            log("CPG has " + v.size() + " rows of which " + NoDataRows + " are potentially data (pies)");
            columnNames = HeaderLine.split(delim);
            data = new String[NoDataRows][columnNames.length];
            int commacount = columnNames.length;
            if (commacount == 0) {
                log("NO delimiters (" + delim + ") - IS IT A CSV or tabbed text FILE - can't make table\n");
                delim = "\t";
                log("Note that if the data does not appear in the cells of the table it has not been properly parsed: please review this log for details.\n");
                figButton.setEnabled(false);
            } else {
                log("There are " + commacount + " delimiters per line\n");
                int CountRow = 0;
                for (int k = DataLineStart; k < v.size(); k++) {
                    String row;
                    row = v.get(k);
                    if (delim.equals(",")) row.replaceAll("\\s", "");
                    if (row.contains("-") || row.contains("+")) {
                        String[] thisrow = row.split(delim);
                        data[CountRow] = thisrow;
                        CountRow++;
                        log("Adding row " + CountRow + ": " + row + "' to table\n");
                    } else if (row.contains("0") || row.contains("1")) {
                        row.replace('1', '+');
                        row.replace('0', '-');
                        String[] thisrow = row.split(delim);
                        data[CountRow] = thisrow;
                        CountRow++;
                        log("Adding row " + CountRow + ": " + row + "' to table\n");
                    } else {
                        log(CountRow + " is not a data row: won't appear in table: " + row + "\n");
                    }
                }
                log("Size of dataset: " + data.length + " rows \n");
                String foundata_opp = "-";
                if (foundata.equals("1")) foundata_opp = "0";
                log("Testing third row: should contain main left labels: " + v.get(2) + "\n");
                String totest = v.get(2).substring(v.get(2).indexOf(delim));
                totest = totest.substring(totest.indexOf(delim));
                p("test " + totest);
                if (totest.contains(foundata) || totest.contains(foundata_opp)) {
                    String message = ("Row 3 looks like a data line: you must have at least 3 header lines\nSTOPPING\nPLEASE CORRECT your data file\n\n");
                    log(message);
                    System.out.println(message);
                    disableFig = true;
                }
            }
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                p("Setting value at " + row + "," + col + " to " + value + " (an instance of " + value.getClass() + ")");
            }
            data[row][col] = value;
            fireTableCellUpdated(row, col);
            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();
            for (int i = 0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j = 0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

    static Dimension optsize = new Dimension(527, 514);

    /**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event-dispatching thread.
	     */
    public static void createAndShowGUI(String path, JTextArea parentlog, String ColourFile, boolean ShowTableusedefaults, CPGcolors cols) {
        if (!ShowTableusedefaults) {
            try {
                load();
            } catch (Exception e) {
                System.out.println("Can't restore location or previous settings because " + e);
            }
        }
        frame = new JFrame("Your file... table");
        String filename = path;
        if (filename.indexOf("/") > -1) filename = filename.substring((filename.lastIndexOf("/") + 1));
        frame.setTitle(filename);
        ShowTable newContentPane = new ShowTable(path, parentlog, ColourFile, ShowTableusedefaults, cols, parent);
        newContentPane.setOpaque(true);
        setLocations(frame);
        frame.setSize(optsize);
        frame.setContentPane(newContentPane);
        frame.setJMenuBar(menubar);
        menubar.addShowTable(newContentPane);
        parent.centerit(frame);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(newContentPane);
    }

    public void doClose() {
        p("In doClose for ShowTable");
        this.frame.setVisible(false);
        parent.removewindow();
    }

    private static void setLocations(JFrame superframe) {
        if (oldlocation != null && oldscreensize != null) {
            superframe.setLocation(oldlocation);
            superframe.setSize(oldscreensize);
        }
    }

    private class Options extends JPanel {

        JPanel buttons = new JPanel();

        JButton optionalButt;

        JPanel options;

        JLabel showhide = new JLabel();

        Dimension minsize = new Dimension(5, 5);

        int optWidth;

        public Options(JPanel checkmadeabove, int width, JButton defaults) {
            JPanel optionpanel = new JPanel();
            optionpanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(), new EmptyBorder(10, 10, 10, 10)));
            optionpanel.setBorder(BorderFactory.createTitledBorder("Figure options"));
            JPanel figurepart = new JPanel();
            figurepart.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(), new EmptyBorder(10, 10, 10, 10)));
            figurepart.setBorder(BorderFactory.createTitledBorder("Plot"));
            optWidth = width;
            gb.setConstraints(this, constraints);
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weighty = 0.2;
            setLayout(gb);
            options = checkmadeabove;
            JButton image = new JButton(new ImageIcon(".cpg/Cpgsm.png"));
            image.setFocusable(false);
            JLabel optionalButt = new JLabel("Hide Options");
            optionpanel.add(image);
            optionpanel.add(optionalButt);
            optionpanel.add(defaults);
            optionpanel.setMinimumSize(new Dimension(400, 25));
            buttons.add(optionpanel);
            gb.setConstraints(buttons, constraints);
            this.add(buttons, constraints);
            image.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    putUpOptions();
                }
            });
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.weighty = 0.8;
            Component[] ca = this.getComponents();
            for (int i = 0; i < ca.length; i++) {
                ca[i].setMinimumSize(minsize);
                ca[i].setFont(smallerfont);
            }
            this.add(options, constraints);
            doshowoptions();
        }

        boolean isUp = false;

        public void hideOptions() {
            isUp = true;
        }

        public void putUpOptions() {
            p("putUpOptions isUp " + isUp);
            if (!isUp) {
                p("remove blank");
                this.remove(blank);
                constraints.gridx = 0;
                constraints.gridy = 1;
                constraints.weighty = 0.8;
                this.add(options, constraints);
                p("Added options");
                isUp = true;
            } else if (isUp) {
                this.remove(options);
                constraints.gridx = 0;
                constraints.gridy = 1;
                constraints.weighty = 0.8;
                this.add(blank, constraints);
                isUp = false;
            }
            doshowoptions();
        }
    }

    public void doshowoptions() {
        if (options != null) {
            Component[] comps = options.getComponents();
            for (int i = 0; i < comps.length; i++) {
                p("repaint " + comps[i]);
                comps[i].repaint();
            }
            options.repaint();
            if (!options.isUp) {
                split1.setDividerLocation(this.getSize().height - 140);
            } else {
                split1.setDividerLocation(this.getSize().height - 348);
            }
        } else p("options NULL");
    }

    void store() {
        File f = new File(gowindowfile);
        if (!f.isFile()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Can't create " + gowindowfile + " because " + e);
            }
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
            Vector goObjects = new Vector();
            goObjects.add(oldscreensize);
            goObjects.add(oldlocation);
            goObjects.add(prefasktoclose);
            goObjects.add(currecpath);
            goObjects.add(Gap);
            goObjects.add(Hi);
            goObjects.add(WdV);
            goObjects.add(Diam);
            goObjects.add(Fon);
            goObjects.add(FONTCHOICE);
            goObjects.add(FONTCHOICE2);
            goObjects.add(ADDVERTLABELS);
            goObjects.add(ADD2ndVERTLABELS);
            goObjects.add(ADD2ndVERTLABELS);
            goObjects.add(gaps4Row2);
            goObjects.add(gaps4Row1);
            goObjects.add(ColSep);
            out.writeObject(goObjects);
            out.close();
        } catch (IOException e) {
            System.out.println("Could not store your program parameters " + e);
        }
    }

    static void load() throws IOException, ClassNotFoundException {
        File f = new File(gowindowfile);
        if (!f.isFile()) return;
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(gowindowfile));
        Vector goObjects = (Vector) in.readObject();
        if (goObjects.size() > 0) oldscreensize = (Dimension) goObjects.elementAt(0);
        if (goObjects.size() > 1) oldlocation = (Point) goObjects.elementAt(1);
        if (goObjects.size() > 2) prefasktoclose = (Boolean) goObjects.elementAt(2);
        if (goObjects.size() > 3) currecpath = (String) goObjects.elementAt(3);
        if (goObjects.size() > 4) WdV = (Integer) goObjects.elementAt(4);
        if (goObjects.size() > 5) Hi = (Integer) goObjects.elementAt(5);
        if (goObjects.size() > 6) Gap = (Integer) goObjects.elementAt(6);
        if (goObjects.size() > 7) Diam = (Integer) goObjects.elementAt(7);
        if (goObjects.size() > 8) Fon = (Integer) goObjects.elementAt(8);
        if (goObjects.size() > 9) FONTCHOICE = (String) goObjects.elementAt(9);
        if (goObjects.size() > 10) FONTCHOICE2 = (String) goObjects.elementAt(10);
        if (goObjects.size() > 11) ADDVERTLABELS = (Boolean) goObjects.elementAt(11);
        if (goObjects.size() > 12) ADD2ndVERTLABELS = (Boolean) goObjects.elementAt(12);
        if (goObjects.size() > 13) gaps4Row2 = (Boolean) goObjects.elementAt(13);
        if (goObjects.size() > 14) gaps4Row1 = (Boolean) goObjects.elementAt(14);
        if (goObjects.size() > 15) ColSep = (Integer) goObjects.elementAt(15);
    }

    public void componentMoved(ComponentEvent ce) {
        oldlocation = new Point(this.getLocation());
    }

    public void componentHidden(ComponentEvent ce) {
    }

    public void componentResized(ComponentEvent ce) {
        oldscreensize = new Dimension(this.getSize());
        p("RESize to " + this.getSize());
        secondsplit.setDividerLocation((secondsplit.getSize().height - 42));
    }

    public void componentShown(ComponentEvent ce) {
        try {
            load();
        } catch (Exception e) {
            System.out.println("Can't restore location or previous settings because " + e);
            e.printStackTrace();
        }
    }

    public void windowClosing(WindowEvent w) {
        p("ShowTable Window closing  - storing");
        parent.removewindow();
        store();
    }

    public void windowClosed(WindowEvent w) {
        p("ShowTable windowClosed ");
    }

    public void windowOpened(WindowEvent w) {
        p("ShowTable windowOpened ");
    }

    public void windowActivated(WindowEvent w) {
        p("ShowTable windowActivated try grab menubar");
    }

    public void windowDeactivated(WindowEvent w) {
        p("ShowTable windowDeactivated, store location.size");
        store();
    }

    public void windowDeiconified(WindowEvent w) {
    }

    public void windowIconified(WindowEvent w) {
    }
}

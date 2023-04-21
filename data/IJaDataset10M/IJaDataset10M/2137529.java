package fiswidgets.fisutils;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import fiswidgets.fisutils.*;
import fiswidgets.fisutils.Utils;
import fiswidgets.fisgui.*;
import java.io.*;
import java.lang.*;
import javax.swing.*;
import java.awt.image.*;

public class InteractiveGraph extends FisFrame implements ActionListener, AdjustmentListener {

    public GraphCanvas gc;

    private Vector graphList = new Vector();

    private JScrollBar slider = new JScrollBar(Scrollbar.HORIZONTAL, 0, 1, 0, 1);

    private JCheckBoxMenuItem holdon;

    private int currentGraph = 0;

    private JFrame popup;

    private JLabel label;

    private JButton ok;

    private FisTextField file;

    public static final int CLOSED = 4;

    public static final int YES = 5;

    public static final int NO = 6;

    public static final int CANCEL = 7;

    public final String file_delim = "\t ";

    public static final int CLEARALL_BUTTON = 1;

    public static final int CLEARLAST_BUTTON = 2;

    public static final int PRINT_BUTTON = 4;

    public static final int TITLE_BUTTON = 8;

    public static final int QUIT_BUTTON = 16;

    public static final int SPREAD_BUTTON = 32;

    public static void main(String[] args) throws GraphCanvas.GraphException {
        InteractiveGraph fg;
        fg = new InteractiveGraph(GraphCanvas.LANDSCAPE);
        fg.setStandAlone(true);
        fg.actionPerformed(new ActionEvent(fg, 0, "adddatafile"));
    }

    public InteractiveGraph() {
        super();
        try {
            FisProperties.loadFisProperties(FisProperties.FISPROPERTIES_SYSTEM_OR_USER);
        } catch (Exception ex) {
        }
        setTitle("InteractiveGraph");
        setStandAlone(false);
        BorderLayout bl = new BorderLayout();
        getContentPane().setLayout(bl);
        bl.setVgap(4);
        addWindowListener(this);
        setJMenuBar(initJMenu());
        slider.addAdjustmentListener(this);
        gc = new GraphCanvas(GraphCanvas.LANDSCAPE);
        gc.setBackground(Color.lightGray);
        gc.setTitle("1.");
        graphList.addElement(gc);
        getContentPane().add("Center", gc);
        getContentPane().add("South", slider);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        validate();
        pack();
        repaint();
        setVisible(true);
        actionPerformed(new ActionEvent(this, 0, "adddatafile"));
    }

    public InteractiveGraph(int shape) {
        super();
        try {
            FisProperties.loadFisProperties(FisProperties.FISPROPERTIES_SYSTEM_OR_USER);
        } catch (Exception ex) {
        }
        setTitle("InteractiveGraph");
        setStandAlone(false);
        BorderLayout bl = new BorderLayout();
        getContentPane().setLayout(bl);
        bl.setVgap(4);
        addWindowListener(this);
        setJMenuBar(initJMenu());
        slider.addAdjustmentListener(this);
        gc = new GraphCanvas(shape);
        gc.setBackground(Color.lightGray);
        gc.setTitle("1.");
        graphList.addElement(gc);
        getContentPane().add("Center", gc);
        getContentPane().add("South", slider);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        validate();
        pack();
        repaint();
        setVisible(true);
    }

    public InteractiveGraph(int shape, String[] files) {
        super();
        try {
            FisProperties.loadFisProperties(FisProperties.FISPROPERTIES_SYSTEM_OR_USER);
        } catch (Exception ex) {
        }
        setTitle("InteractiveGraph");
        setStandAlone(false);
        BorderLayout bl = new BorderLayout();
        getContentPane().setLayout(bl);
        bl.setVgap(4);
        addWindowListener(this);
        setJMenuBar(initJMenu());
        slider.addAdjustmentListener(this);
        gc = new GraphCanvas(shape);
        gc.setBackground(Color.lightGray);
        gc.setTitle("1.");
        graphList.addElement(gc);
        getContentPane().add("Center", gc);
        getContentPane().add("South", slider);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        validate();
        pack();
        setVisible(true);
        for (int k = 0; k < files.length; k++) {
            if (!(new File(files[k])).canRead()) {
                Dialogs.ShowErrorDialog(this, "Cannot read file " + files[k]);
                break;
            }
            Vector input = GFileParser(files[k]);
            if (input == null) return;
            float[] x_axis;
            for (int i = 0; i < input.size(); i++) {
                x_axis = new float[((float[]) input.elementAt(i)).length];
                for (int j = 0; j < x_axis.length; j++) x_axis[j] = j;
                try {
                    ((GraphCanvas) graphList.elementAt(currentGraph)).addCurve(x_axis, (float[]) input.elementAt(i));
                } catch (GraphCanvas.GraphException ex) {
                }
            }
            if (currentGraph + 1 == slider.getMaximum()) {
                GraphCanvas temp = new GraphCanvas(GraphCanvas.LANDSCAPE);
                temp.setBackground(Color.lightGray);
                graphList.insertElementAt(temp, currentGraph + 1);
                slider.setMaximum(slider.getMaximum() + 1);
            }
            String temp = gc.getTitle();
            if (temp.substring(temp.indexOf(".") + 1).trim().equals("")) gc.setTitle((currentGraph + 1) + ". " + files[k]);
        }
        repaint();
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        currentGraph = e.getValue();
        changeGraph();
    }

    public void actionPerformed(ActionEvent e) {
        PrintJob pj;
        Graphics g;
        Properties prop;
        Point p;
        String dest;
        if (e.getActionCommand().equals("headfile")) {
            if (!file.isAFile()) return;
            TextView display = new TextView(file.getText());
            display.setStandAlone(false);
            display.display();
        } else if (e.getActionCommand().equals("clearall")) {
            gc.clearAll();
        } else if (e.getActionCommand().equals("clearlast")) {
            gc.clearLast();
        } else if (e.getActionCommand().equals("copy")) {
            GraphCanvas temp = new GraphCanvas(GraphCanvas.LANDSCAPE);
            temp.setBackground(Color.lightGray);
            float[][] f = ((GraphCanvas) graphList.elementAt(currentGraph)).getCurves();
            for (int i = 0; i < f.length; i++) {
                int len = f[i].length;
                float y[] = new float[len];
                for (int j = 0; j < len; j++) y[j] = j;
                try {
                    temp.addCurve(y, f[i]);
                } catch (GraphCanvas.GraphException ex) {
                }
            }
            graphList.insertElementAt(temp, currentGraph + 1);
            currentGraph++;
            slider.setMaximum(slider.getMaximum() + 1);
            slider.setValue(currentGraph);
            changeGraph();
        } else if (e.getActionCommand().equals("delete")) {
            if (currentGraph == graphList.size() - 1) {
                Dialogs.ShowErrorDialog(gc, "You cannot delete the last graph.");
                return;
            }
            graphList.removeElementAt(currentGraph);
            slider.setMaximum(slider.getMaximum() - 1);
            changeGraph();
        } else if (e.getActionCommand().equals("adddatacols")) {
            FisPanel dialog = new FisPanel();
            file = new FisTextField("File", "", dialog);
            FisFileBrowser chooser = new FisFileBrowser(dialog);
            chooser.attachTo(file);
            dialog.newLine();
            FisIntField cols = new FisIntField("Cols", dialog);
            cols.setToolTipText("0 based numbering is used");
            cols.setQuarterWidth();
            FisButton browse = new FisButton("view file", dialog);
            browse.setActionCommand("headfile");
            browse.addActionListener(this);
            dialog.newLine();
            FisIntField skip = new FisIntField("Skip lines", dialog);
            skip.setToolTipText("Skip the first n lines of the file");
            skip.setQuarterWidth();
            if (ShowTextPaneYesNoDialog(dialog, dialog.getComponentPanel(), "Add Data Columns") == YES) {
                if (!file.isAFile()) return;
                File stuff = new File(file.getText());
                int[] selectedCols = new int[1];
                selectedCols[0] = cols.getTextAsInt();
                if (!holdon.getState()) gc.clearAll();
                Vector input = GFileParser(stuff.getPath());
                float[] x_axis;
                float[] y_axis;
                int i;
                for (int k = 0; k < selectedCols.length; k++) {
                    i = selectedCols[k];
                    if (i > input.size() - 1) continue;
                    if (skip.getTextAsInt() >= ((float[]) input.elementAt(i)).length) continue;
                    y_axis = new float[((float[]) input.elementAt(i)).length - skip.getTextAsInt()];
                    for (int pz = 0; pz < y_axis.length; pz++) y_axis[pz] = ((float[]) input.elementAt(i))[pz + skip.getTextAsInt()];
                    x_axis = new float[((float[]) input.elementAt(i)).length - skip.getTextAsInt()];
                    for (int j = 0; j < x_axis.length; j++) x_axis[j] = j;
                    try {
                        ((GraphCanvas) graphList.elementAt(currentGraph)).addCurve(x_axis, y_axis);
                    } catch (GraphCanvas.GraphException ex) {
                    }
                }
                if (currentGraph + 1 == slider.getMaximum()) {
                    GraphCanvas temp = new GraphCanvas(GraphCanvas.LANDSCAPE);
                    temp.setBackground(Color.lightGray);
                    graphList.insertElementAt(temp, currentGraph + 1);
                    slider.setMaximum(slider.getMaximum() + 1);
                }
                repaint();
            }
        } else if (e.getActionCommand().equals("adddatafile")) {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            if (chooser.showOpenDialog(gc) != JFileChooser.APPROVE_OPTION) return;
            if (!holdon.getState()) gc.clearAll();
            Vector input = GFileParser(chooser.getSelectedFile().getPath());
            if (input == null) return;
            float[] x_axis;
            for (int i = 0; i < input.size(); i++) {
                x_axis = new float[((float[]) input.elementAt(i)).length];
                for (int j = 0; j < x_axis.length; j++) x_axis[j] = j;
                try {
                    ((GraphCanvas) graphList.elementAt(currentGraph)).addCurve(x_axis, (float[]) input.elementAt(i));
                } catch (GraphCanvas.GraphException ex) {
                }
            }
            if (currentGraph + 1 == slider.getMaximum()) {
                GraphCanvas temp = new GraphCanvas(GraphCanvas.LANDSCAPE);
                temp.setBackground(Color.lightGray);
                graphList.insertElementAt(temp, currentGraph + 1);
                slider.setMaximum(slider.getMaximum() + 1);
            }
            String temp = gc.getTitle();
            if (temp.substring(temp.indexOf(".") + 1).trim().equals("")) gc.setTitle((currentGraph + 1) + ". " + chooser.getSelectedFile().getName());
            repaint();
        } else if (e.getActionCommand().equals("namecanvas")) {
            FisPanel dialog = new FisPanel();
            FisTextField title = new FisTextField("Title", "", dialog);
            if (ShowTextPaneYesNoDialog(dialog, dialog.getComponentPanel(), "Title") == YES) gc.setTitle((currentGraph + 1) + ". " + title.getText());
            repaint();
        } else if (e.getActionCommand().equals("labels")) {
            FisPanel dialog = new FisPanel();
            FisTextField xLabel, yLabel;
            xLabel = new FisTextField("Label x-axis", "", dialog);
            dialog.newLine();
            yLabel = new FisTextField("Label y-axis", "", dialog);
            if (ShowTextPaneYesNoDialog(dialog, dialog.getComponentPanel(), "Label axis") == YES) {
                gc.setXlabel(xLabel.getText());
                gc.setYlabel(yLabel.getText());
                repaint();
            }
        } else if (e.getActionCommand().equals("viewspread")) {
            FisTableView ftv = new FisTableView();
            float[][] f = gc.getCurves();
            if (f.length == 0) {
                Dialogs.ShowErrorDialog(gc, "No curves plotted.");
                return;
            }
            try {
                ftv.setTitle(this.getTitle());
                ftv.createTableWith(f);
            } catch (Exception ex) {
                Dialogs.ShowErrorDialog(gc, "Unable to create table: " + ex.getMessage());
                return;
            }
        } else if (e.getActionCommand().equals("savespread")) {
            FisTableView ftv = new FisTableView();
            float[][] f = gc.getCurves();
            if (f.length == 0) {
                Dialogs.ShowErrorDialog(gc, "No curves plotted.");
                return;
            }
            try {
                ftv.setTitle(this.getTitle());
                ftv.createTableWith(f);
                ftv.doSave();
            } catch (Exception ex) {
                Dialogs.ShowErrorDialog(gc, "Unable to create table: " + ex.getMessage());
                return;
            }
        } else if (e.getActionCommand().equals("goto")) {
            FisPanel dialog = new FisPanel();
            FisIntField number = new FisIntField("Graph Number", dialog);
            if (ShowTextPaneYesNoDialog(dialog, dialog.getComponentPanel(), "Goto") == YES) {
                int n = number.getTextAsInt();
                if ((n < 1) || (n > slider.getMaximum())) return;
                currentGraph = n - 1;
                slider.setValue(n - 1);
                changeGraph();
            }
        } else if (e.getActionCommand().equals("setupaxis")) {
            FisPanel dialog = new FisPanel();
            FisCheckBox xlog = new FisCheckBox("Logarithmic X Axis", gc.getBoolOpt(GraphCanvas.XLOGSCALE), dialog);
            xlog.setEnabled(false);
            FisCheckBox ylog = new FisCheckBox("Logarithmic Y Axis", gc.getBoolOpt(GraphCanvas.YLOGSCALE), dialog);
            ylog.setEnabled(false);
            dialog.newLine();
            FisCheckBox zero = new FisCheckBox("Draw Y axis at 0", gc.getBoolOpt(GraphCanvas.ZEROAXIS), dialog);
            if (ShowTextPaneYesNoDialog(dialog, dialog.getComponentPanel(), "Setup Axis") == YES) {
                gc.setBoolOpt(GraphCanvas.XLOGSCALE, xlog.isChecked());
                gc.setBoolOpt(GraphCanvas.YLOGSCALE, ylog.isChecked());
                gc.setBoolOpt(GraphCanvas.ZEROAXIS, zero.isChecked());
            }
        } else if (e.getActionCommand().equals("print")) {
            prop = new Properties();
            prop.put("awt.print.destination", "file");
            prop.put("awt.print.printer", System.getProperty("PRINT_COM"));
            prop.put("awt.print.options", "-P" + System.getProperty("PRINTER"));
            pj = this.getToolkit().getPrintJob(this, System.getProperty("user.name"), prop);
            if (pj != null) {
                g = pj.getGraphics();
                this.printAll(g);
                g.dispose();
                pj.end();
            }
        } else if (e.getActionCommand().equals("printscreen")) {
            FisPanel dialog = new FisPanel();
            FisPanel pane = new FisPanel();
            dialog.addFisPanel(pane, 2, 1);
            FisTextField fileName = new FisTextField("Output file", "", pane);
            FisFileBrowser browse = new FisFileBrowser(pane);
            browse.attachTo(fileName);
            dialog.newLine();
            FisLabel label = new FisLabel("Quality", dialog);
            JSlider bar = new JSlider(1, 100);
            FisComponent comp = new FisComponent(bar, dialog);
            if (ShowTextPaneYesNoDialog(dialog, dialog.getComponentPanel(), "Printscreen") != YES) return;
            String filename = fileName.getText();
            if (!(filename.endsWith(".jpg") || filename.endsWith(".jpeg"))) filename += ".jpg";
            FileOutputStream file;
            try {
                file = new FileOutputStream(filename);
            } catch (IOException ex) {
                Dialogs.ShowErrorDialog(gc, "Filename caused error:" + ex.getMessage());
                return;
            }
            Image screen = gc.createImage(gc.getSize().width, gc.getSize().height);
            gc.paint(screen.getGraphics());
            try {
                Utils.writeJpegImage(screen, bar.getValue(), file);
            } catch (IOException ex) {
                Dialogs.ShowErrorDialog(this, "Error saving Jpeg file " + filename);
            }
        } else if (e.getActionCommand().equals("bgcolor")) {
            FisPanel dialog = new FisPanel();
            FisRadioButtonGroup group = new FisRadioButtonGroup();
            FisRadioButton white = new FisRadioButton("White", dialog);
            group.add(white);
            if (gc.getBackground() == Color.white) white.setSelected(true);
            FisRadioButton gray = new FisRadioButton("Gray", dialog);
            group.add(gray);
            if (gc.getBackground() == Color.lightGray) gray.setSelected(true);
            FisRadioButton black = new FisRadioButton("Black", dialog);
            group.add(black);
            if (gc.getBackground() == Color.black) black.setSelected(true);
            if (ShowTextPaneYesNoDialog(dialog, dialog.getComponentPanel(), "Change background color") == YES) {
                if (white.isSelected()) {
                    gc.setBackground(Color.white);
                    gc.setForeground(Color.black);
                } else if (black.isSelected()) {
                    gc.setBackground(Color.black);
                    gc.setForeground(Color.white);
                } else if (gray.isSelected()) {
                    gc.setBackground(Color.lightGray);
                    gc.setForeground(Color.black);
                }
            }
        } else if (e.getActionCommand().equals("stddev")) {
            FisPanel dialog = new FisPanel();
            FisLabel label = new FisLabel("# of deviations", dialog);
            FisRadioButtonGroup group = new FisRadioButtonGroup();
            FisRadioButton zero = new FisRadioButton("0(mean)", dialog);
            group.add(zero);
            zero.setSelected(true);
            FisRadioButton one = new FisRadioButton("1", dialog);
            group.add(one);
            FisRadioButton two = new FisRadioButton("2", dialog);
            group.add(two);
            FisRadioButton three = new FisRadioButton("3", dialog);
            group.add(three);
            FisRadioButton four = new FisRadioButton("4", dialog);
            group.add(four);
            FisRadioButton five = new FisRadioButton("5", dialog);
            group.add(five);
            if (ShowTextPaneYesNoDialog(dialog, dialog.getComponentPanel(), "Add Standard Deviations") == YES) {
                if (zero.isSelected()) {
                    addStdDevs(gc, 0);
                } else if (one.isSelected()) {
                    addStdDevs(gc, 1);
                } else if (two.isSelected()) {
                    addStdDevs(gc, 2);
                } else if (three.isSelected()) {
                    addStdDevs(gc, 3);
                } else if (four.isSelected()) {
                    addStdDevs(gc, 4);
                } else if (five.isSelected()) {
                    addStdDevs(gc, 5);
                }
            }
        } else if (e.getActionCommand().equals("quit")) {
            dispose();
        } else if (e.getActionCommand().equals("help")) {
            try {
                String browser = System.getProperty("BROWSER", null);
                String htmlfile = "InteractiveGraph.html";
                if (browser == null) throw new FisHelpDocPathPropertyNotDefinedException("The BROWSER property was not found");
                if (!(new File(browser)).exists()) throw new FisHelpDocPathPropertyNotDefinedException(browser + " was not found");
                String test = System.getProperty("FISDOC_PATH", null);
                if (test == null) throw new FisHelpDocPathPropertyNotDefinedException("The FISDOC_PATH property was not found " + "so the help cannot be activated.");
                if (test.startsWith("http")) htmlfile = test + "/" + htmlfile; else if ((new File(test + File.separator + htmlfile)).exists()) htmlfile = test + File.separator + htmlfile; else throw new FisHelpDocPathPropertyNotDefinedException(htmlfile + " was not found");
                Runtime rt = Runtime.getRuntime();
                Process pr = null;
                pr = rt.exec(browser + " " + htmlfile);
            } catch (FisHelpDocPathPropertyNotDefinedException ex) {
                Dialogs.ShowErrorDialog(gc, ex.getMessage());
            } catch (IOException ex) {
                Dialogs.ShowErrorDialog(gc, "Could not open the help file.");
            }
        }
    }

    private void changeGraph() {
        remove(gc);
        gc = (GraphCanvas) graphList.elementAt(currentGraph);
        String temp = gc.getTitle();
        temp = (currentGraph + 1) + "." + temp.substring(temp.indexOf(".") + 1);
        gc.setTitle(temp);
        getContentPane().add("Center", gc);
        validate();
        repaint();
    }

    private JMenuBar initJMenu() {
        JMenuBar JMenu = new JMenuBar();
        JMenu fileJMenu = new JMenu("File");
        JMenu editJMenu = new JMenu("Edit");
        JMenu viewJMenu = new JMenu("View");
        JMenu helpJMenu = new JMenu("Help");
        JMenuItem temp = new JMenuItem("Copy");
        temp.setActionCommand("copy");
        temp.addActionListener(this);
        editJMenu.add(temp);
        temp = new JMenuItem("Delete");
        temp.setActionCommand("delete");
        temp.addActionListener(this);
        editJMenu.add(temp);
        editJMenu.addSeparator();
        temp = new JMenuItem("Title...");
        temp.setActionCommand("namecanvas");
        temp.addActionListener(this);
        editJMenu.add(temp);
        temp = new JMenuItem("Labels...");
        temp.setActionCommand("labels");
        temp.addActionListener(this);
        editJMenu.add(temp);
        editJMenu.addSeparator();
        temp = new JMenuItem("Setup axis...");
        temp.setActionCommand("setupaxis");
        temp.addActionListener(this);
        editJMenu.add(temp);
        holdon = new JCheckBoxMenuItem("Hold on", true);
        editJMenu.add(holdon);
        editJMenu.addSeparator();
        temp = new JMenuItem("Clear all");
        temp.setActionCommand("clearall");
        temp.addActionListener(this);
        editJMenu.add(temp);
        temp = new JMenuItem("Clear last");
        temp.setActionCommand("clearlast");
        temp.addActionListener(this);
        editJMenu.add(temp);
        temp = new JMenuItem("Add data");
        temp.setActionCommand("adddatafile");
        temp.addActionListener(this);
        fileJMenu.add(temp);
        temp = new JMenuItem("Add data col");
        temp.setActionCommand("adddatacols");
        temp.addActionListener(this);
        fileJMenu.add(temp);
        fileJMenu.addSeparator();
        temp = new JMenuItem("Print...");
        temp.setActionCommand("print");
        temp.addActionListener(this);
        fileJMenu.add(temp);
        temp = new JMenuItem("Make jpeg");
        temp.setActionCommand("printscreen");
        temp.addActionListener(this);
        fileJMenu.add(temp);
        fileJMenu.addSeparator();
        temp = new JMenuItem("Quit");
        temp.setActionCommand("quit");
        temp.addActionListener(this);
        fileJMenu.add(temp);
        temp = new JMenuItem("View spreadsheet");
        temp.setActionCommand("viewspread");
        temp.addActionListener(this);
        viewJMenu.add(temp);
        temp = new JMenuItem("Save spreadsheet");
        temp.setActionCommand("savespread");
        temp.addActionListener(this);
        viewJMenu.add(temp);
        viewJMenu.addSeparator();
        temp = new JMenuItem("Goto");
        temp.setActionCommand("goto");
        temp.addActionListener(this);
        viewJMenu.add(temp);
        temp = new JMenuItem("Background Color...");
        temp.setActionCommand("bgcolor");
        temp.addActionListener(this);
        viewJMenu.add(temp);
        temp = new JMenuItem("Std Dev");
        temp.setActionCommand("stddev");
        temp.addActionListener(this);
        viewJMenu.add(temp);
        temp = new JMenuItem("Help");
        temp.setActionCommand("help");
        temp.addActionListener(this);
        helpJMenu.add(temp);
        JMenu.add(fileJMenu);
        JMenu.add(editJMenu);
        JMenu.add(viewJMenu);
        JMenu.add(helpJMenu);
        return JMenu;
    }

    public Vector GFileParser(String input) {
        Vector numberList = new Vector();
        Vector numbers;
        float[] floaters;
        Float[] Floaters;
        StringTokenizer tok;
        String line;
        Vector strings = new Vector();
        int rows = 0;
        BufferedReader lr;
        int columns;
        int c;
        try {
            columns = Utils.ColumnCount(input, file_delim);
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        } catch (Exception ex) {
            Dialogs.ShowErrorDialog(this, "Error reading file " + input + "\nFile may be empty or unreadable");
            return null;
        }
        try {
            lr = new BufferedReader(new FileReader(input));
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        } catch (Exception ex) {
            Dialogs.ShowErrorDialog(this, "Error reading file " + input + "\nFile may be empty or unreadable");
            return null;
        }
        while (true) {
            c = 0;
            try {
                line = lr.readLine();
            } catch (IOException ex) {
                return null;
            }
            if (line == null) break;
            if (line.trim().length() == 0) continue;
            tok = new StringTokenizer(line, file_delim);
            while (tok.hasMoreElements()) {
                c++;
                strings.addElement(tok.nextToken());
            }
            for (; c < columns; c++) strings.addElement("\0");
            rows++;
        }
        try {
            lr.close();
        } catch (IOException ex) {
        }
        Float temp;
        for (int i = 0; i < columns; i++) {
            numbers = new Vector();
            try {
                for (int j = 0; j < rows; j++) {
                    if (strings.elementAt(j * columns + i) != "\0") {
                        temp = new Float(strings.elementAt(j * columns + i).toString());
                        numbers.addElement(temp);
                    }
                }
            } catch (NumberFormatException ex) {
                Dialogs.ShowErrorDialog(this, "File " + input + " does not seem to be a valid graph file");
                return null;
            }
            Floaters = new Float[numbers.size()];
            floaters = new float[numbers.size()];
            numbers.copyInto(Floaters);
            for (int k = 0; k < Floaters.length; k++) floaters[k] = Floaters[k].floatValue();
            numberList.addElement(floaters);
        }
        return numberList;
    }

    public static int ShowTextPaneYesNoDialog(Component parent, Object textpane, String title) {
        int selection = JOptionPane.showConfirmDialog(parent, textpane, title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        switch(selection) {
            case JOptionPane.CLOSED_OPTION:
                return CLOSED;
            case JOptionPane.YES_OPTION:
                return YES;
            case JOptionPane.NO_OPTION:
                return NO;
        }
        return CLOSED;
    }

    public static int getStringDialog(Component parent, String label, Float input, String title) {
        FisPanel dialog = new FisPanel();
        FisTextField field = new FisTextField(label, "", dialog);
        if (title == null) title = label;
        int selection = JOptionPane.showConfirmDialog(parent, dialog, title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        switch(selection) {
            case JOptionPane.CLOSED_OPTION:
                return CLOSED;
            case JOptionPane.YES_OPTION:
                return YES;
            case JOptionPane.NO_OPTION:
                return NO;
        }
        return CLOSED;
    }

    public static int getFloatDialog(Component parent, String label, float input, String title) {
        FisPanel dialog = new FisPanel();
        FisFloatField field = new FisFloatField(label, dialog);
        if (title == null) title = label;
        int selection = JOptionPane.showConfirmDialog(parent, dialog, title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        switch(selection) {
            case JOptionPane.CLOSED_OPTION:
                return CLOSED;
            case JOptionPane.YES_OPTION:
                input = field.getTextAsFloat();
                return YES;
            case JOptionPane.NO_OPTION:
                return NO;
        }
        return CLOSED;
    }

    public static int getIntDialog(Component parent, String label, int input, String title) {
        FisPanel dialog = new FisPanel();
        FisIntField field = new FisIntField(label, dialog);
        if (title == null) title = label;
        int selection = JOptionPane.showConfirmDialog(parent, dialog, title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        switch(selection) {
            case JOptionPane.CLOSED_OPTION:
                return CLOSED;
            case JOptionPane.YES_OPTION:
                input = field.getTextAsInt();
                return YES;
            case JOptionPane.NO_OPTION:
                return NO;
        }
        return CLOSED;
    }

    /**
     * Sets the title of the title bar of InteractiveGraph.
     * Note that this does not set the title of an individual graph inside of the InteractiveGraph frame. 
     * Please use the setTitle method in GraphCanvas to set this explicitly.
     */
    public void title(String title) {
        this.setTitle(title);
    }

    public void disableButtons(int mask) {
        if ((mask & CLEARALL_BUTTON) != 0) getJMenuBar().getMenu(1).getItem(9).setEnabled(false);
        if ((mask & CLEARLAST_BUTTON) != 0) getJMenuBar().getMenu(1).getItem(10).setEnabled(false);
        if ((mask & PRINT_BUTTON) != 0) getJMenuBar().getMenu(0).getItem(4).setEnabled(false);
        if ((mask & TITLE_BUTTON) != 0) getJMenuBar().getMenu(1).getItem(4).setEnabled(false);
        if ((mask & QUIT_BUTTON) != 0) getJMenuBar().getMenu(0).getItem(6).setEnabled(false);
        if ((mask & SPREAD_BUTTON) != 0) {
            getJMenuBar().getMenu(2).getItem(0).setEnabled(false);
            getJMenuBar().getMenu(2).getItem(1).setEnabled(false);
        }
    }

    /**
     * if set to true then all editing option JMenus will be disabled
     */
    public void setViewOnly(boolean val) {
        if (val) {
            getJMenuBar().getMenu(1).setEnabled(false);
            getJMenuBar().getMenu(0).getItem(0).setEnabled(false);
            getJMenuBar().getMenu(0).getItem(1).setEnabled(false);
            getJMenuBar().getMenu(2).getItem(3).setEnabled(false);
        } else {
            getJMenuBar().getMenu(1).setEnabled(true);
            getJMenuBar().getMenu(0).getItem(0).setEnabled(true);
            getJMenuBar().getMenu(0).getItem(1).setEnabled(true);
            getJMenuBar().getMenu(2).getItem(3).setEnabled(true);
        }
    }

    /**
     * Create a Jpeg Image from graph
     */
    public void makeJpeg(String filename, int quality) throws IOException {
        if (!(filename.endsWith(".jpg") || filename.endsWith(".jpeg"))) filename += ".jpg";
        FileOutputStream file;
        try {
            file = new FileOutputStream(filename);
        } catch (IOException ex) {
            Dialogs.ShowErrorDialog(gc, "Filename caused error:" + ex.getMessage());
            return;
        }
        Image screen = createImage(gc.getSize().width, gc.getSize().height);
        gc.repaint();
        gc.paint(screen.getGraphics());
        Utils.writeJpegImage(screen, quality, file);
    }

    public float calcMean(float[] values) {
        double mean = 0;
        if (values == null) return 0;
        for (int i = 0; i < values.length; i++) {
            mean += values[i];
        }
        mean /= values.length;
        return (float) mean;
    }

    public float calcStdDev(float[] values, float mean) {
        double var = 0;
        if (values == null) return 0;
        if (values.length == 1) return 0;
        for (int i = 0; i < values.length; i++) {
            var += (values[i] - mean) * (values[i] - mean);
        }
        var /= (values.length - 1);
        return (float) Math.sqrt(var);
    }

    private void addStdDevs(GraphCanvas gcanvas, int n) {
        float[][] dataset = gcanvas.getCurves();
        float mean, stddev;
        String data = "";
        if (dataset == null) return;
        if (dataset.length == 0) return;
        for (int i = 0; i < dataset.length; i++) {
            data += "Curve #" + i + " containing " + dataset[i].length + " datapoints:\n";
            mean = calcMean(dataset[i]);
            data += "  Mean: " + mean + "\n";
            stddev = calcStdDev(dataset[i], mean);
            data += "  Standard Deviation: " + stddev;
            if (i != dataset.length - 1) data += "\n\n";
            try {
                gcanvas.addBlackLine(mean);
                for (int j = 1; j <= n; j++) {
                    gcanvas.addBlackLine(mean + (stddev * j));
                    gcanvas.addBlackLine(mean - (stddev * j));
                }
            } catch (GraphCanvas.GraphException ex) {
                Dialogs.ShowErrorDialog(gcanvas, "Error adding line" + ex.getMessage());
                return;
            }
        }
        JFrame window = new JFrame();
        window.setTitle("Standard Deviation Data");
        JTextArea text = new JTextArea(data);
        text.setEditable(false);
        window.getContentPane().add(text);
        window.pack();
        window.show();
    }
}

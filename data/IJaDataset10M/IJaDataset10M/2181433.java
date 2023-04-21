package org.tigr.microarray.mev.file;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.tigr.microarray.mev.FloatSlideData;
import org.tigr.microarray.mev.ISlideData;
import org.tigr.microarray.mev.SlideData;
import org.tigr.microarray.mev.SlideDataElement;
import org.tigr.microarray.mev.TMEV;

public class Mas5FileLoader extends ExpressionFileLoader {

    private GBA gba;

    private boolean stop = false;

    private Mas5FileLoaderPanel sflp;

    private int affyDataType = TMEV.DATA_TYPE_AFFY;

    public Mas5FileLoader(SuperExpressionFileLoader superLoader) {
        super(superLoader);
        gba = new GBA();
        sflp = new Mas5FileLoaderPanel();
    }

    public Vector loadExpressionFiles() throws IOException {
        return loadMas5ExpressionFile(new File(this.sflp.pathTextField.getText()), this.sflp.refTextField.getText());
    }

    public ISlideData loadExpressionFile(File f) {
        return null;
    }

    public void setTMEVDataType() {
        TMEV.setDataType(TMEV.DATA_TYPE_AFFY);
    }

    public int getAffyDataType() {
        return this.affyDataType;
    }

    public Vector loadMas5ExpressionFile(File f, String callfile) throws IOException {
        final int preSpotRows = this.sflp.getXRow() + 1;
        final int preExperimentColumns = this.sflp.getXColumn();
        this.setTMEVDataType();
        int numLines = this.getCountOfLines(f);
        int spotCount = numLines - preSpotRows;
        if (spotCount <= 0) {
            JOptionPane.showMessageDialog(superLoader.getFrame(), "There is no spot data available.", "TDMS Load Error", JOptionPane.INFORMATION_MESSAGE);
        }
        int[] rows = new int[] { 0, 1, 0 };
        int[] columns = new int[] { 0, 1, 0 };
        String value;
        float cy3, cy5;
        String[] moreFields = new String[preExperimentColumns];
        final int rColumns = 1;
        final int rRows = spotCount;
        ISlideData[] slideDataArray = null;
        SlideDataElement sde;
        BufferedReader reader = new BufferedReader(new FileReader(f));
        StringSplitter ss = new StringSplitter((char) 0x09);
        String currentLine;
        int counter, row, column;
        counter = 0;
        row = column = 1;
        this.setFilesCount(1);
        this.setRemain(1);
        this.setFilesProgress(0);
        this.setLinesCount(numLines);
        this.setFileProgress(0);
        while ((currentLine = reader.readLine()) != null) {
            if (stop) {
                return null;
            }
            ss.init(currentLine);
            if (counter == 0) {
                int experimentCount = ss.countTokens() + 2 - preExperimentColumns;
                slideDataArray = new ISlideData[experimentCount];
                slideDataArray[0] = new SlideData(rRows, rColumns);
                slideDataArray[0].setSlideFileName(f.getPath());
                for (int i = 1; i < slideDataArray.length; i++) {
                    slideDataArray[i] = new FloatSlideData(slideDataArray[0].getSlideMetaData(), spotCount);
                    slideDataArray[i].setSlideFileName(f.getPath());
                }
                String[] fieldNames = new String[preExperimentColumns + 1];
                fieldNames[0] = "ChipID";
                for (int i = 1; i < preExperimentColumns; i++) {
                    fieldNames[i] = ss.nextToken();
                }
                fieldNames[preExperimentColumns] = callfile;
                slideDataArray[0].getSlideMetaData().appendFieldNames(fieldNames);
                for (int i = 0; i < experimentCount; i++) {
                    slideDataArray[i].setSlideDataName(ss.nextToken());
                }
            } else if (counter >= preSpotRows) {
                rows[0] = rows[2] = row;
                columns[0] = columns[2] = column;
                if (column == rColumns) {
                    column = 1;
                    row++;
                } else {
                    column++;
                }
                for (int i = 0; i < preExperimentColumns; i++) {
                    moreFields[i] = ss.nextToken();
                }
                sde = new SlideDataElement(String.valueOf(row + 1), rows, columns, new float[2], moreFields);
                slideDataArray[0].addSlideDataElement(sde);
                for (int i = 0; i < slideDataArray.length; i++) {
                    cy3 = 1f;
                    try {
                        value = ss.nextToken();
                        cy5 = Float.parseFloat(value);
                    } catch (Exception e) {
                        cy3 = 0;
                        cy5 = Float.NaN;
                    }
                    slideDataArray[i].setIntensities(counter - preSpotRows, cy3, cy5);
                }
            } else {
                for (int i = 0; i < preExperimentColumns - 1; i++) {
                    ss.nextToken();
                }
                String key = ss.nextToken();
                for (int j = 0; j < slideDataArray.length; j++) {
                    slideDataArray[j].addNewSampleLabel(key, ss.nextToken());
                }
            }
            this.setFileProgress(counter);
            counter++;
        }
        reader.close();
        Vector data = new Vector(slideDataArray.length);
        for (int i = 0; i < slideDataArray.length; i++) data.add(slideDataArray[i]);
        this.setFilesProgress(1);
        return data;
    }

    public FileFilter getFileFilter() {
        FileFilter mevFileFilter = new FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                if (f.getName().endsWith(".txt")) return true; else return false;
            }

            public String getDescription() {
                return "Mas5 Files(*.txt)";
            }
        };
        return mevFileFilter;
    }

    public boolean checkLoadEnable() {
        int tableRow = sflp.getXRow() + 1;
        int tableColumn = sflp.getXColumn();
        if (tableColumn < 0) return false;
        TableModel model = sflp.getTable().getModel();
        String fieldSummary = "";
        for (int i = 0; i < tableColumn; i++) {
            fieldSummary += model.getColumnName(i) + (i + 1 == tableColumn ? "" : ", ");
        }
        sflp.setFieldsText(fieldSummary);
        if (tableRow >= 1 && tableColumn >= 0) {
            setLoadEnabled(true);
            return true;
        } else {
            setLoadEnabled(false);
            return false;
        }
    }

    public boolean validateFile(File targetFile) {
        return true;
    }

    public JPanel getFileLoaderPanel() {
        return sflp;
    }

    public void loadCallFile(File targetFile) {
        sflp.setCallFileName(targetFile.getAbsolutePath());
    }

    public void processMas5File(File targetFile) {
        Vector columnHeaders = new Vector();
        Vector dataVector = new Vector();
        Vector rowVector = null;
        BufferedReader reader = null;
        String currentLine = null;
        if (!validateFile(targetFile)) return;
        sflp.setDataFileName(targetFile.getAbsolutePath());
        DefaultTableModel model = new DefaultTableModel() {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        try {
            reader = new BufferedReader(new FileReader(targetFile), 1024 * 128);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        try {
            StringSplitter ss = new StringSplitter('\t');
            currentLine = reader.readLine();
            ss.init(currentLine);
            columnHeaders.add("ChipID\t");
            for (int i = 0; i < ss.countTokens() + 1; i++) {
                columnHeaders.add(ss.nextToken());
            }
            model.setColumnIdentifiers(columnHeaders);
            int cnt = 0;
            while ((currentLine = reader.readLine()) != null && cnt < 100) {
                cnt++;
                ss.init(currentLine);
                rowVector = new Vector();
                for (int i = 0; i < ss.countTokens() + 1; i++) {
                    try {
                        rowVector.add(ss.nextToken());
                    } catch (java.util.NoSuchElementException nsee) {
                        rowVector.add(" ");
                    }
                }
                dataVector.add(rowVector);
                model.addRow(rowVector);
            }
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        sflp.setTableModel(model);
    }

    public String getFilePath() {
        return this.sflp.pathTextField.getText();
    }

    public void openDataPath() {
        this.sflp.openDataPath();
    }

    private class Mas5FileLoaderPanel extends JPanel {

        FileTreePane fileTreePane;

        JTextField pathTextField;

        JPanel pathPanel;

        JTable expressionTable;

        JLabel instructionsLabel;

        JScrollPane tableScrollPane;

        JPanel tablePanel;

        JPanel mas5ListPanel;

        JList mas5AvailableList;

        JScrollPane mas5AvailableScrollPane;

        JPanel refListPanel;

        JList refAvailableList;

        JScrollPane refAvailableScrollPane;

        JTextField refTextField;

        JPanel refPanel;

        JTextField annoTextField;

        JPanel annoPanel;

        JPanel fileLoaderPanel;

        JSplitPane splitPane;

        private int xRow = -1;

        private int xColumn = -1;

        public Mas5FileLoaderPanel() {
            setLayout(new GridBagLayout());
            fileTreePane = new FileTreePane(SuperExpressionFileLoader.DATA_PATH);
            fileTreePane.addFileTreePaneListener(new FileTreePaneEventHandler());
            fileTreePane.setPreferredSize(new java.awt.Dimension(200, 50));
            pathTextField = new JTextField();
            pathTextField.setEditable(false);
            pathTextField.setBorder(new TitledBorder(new EtchedBorder(), "Selected Path"));
            pathTextField.setForeground(Color.black);
            pathTextField.setFont(new Font("monospaced", Font.BOLD, 12));
            pathPanel = new JPanel();
            pathPanel.setLayout(new GridBagLayout());
            pathPanel.setBorder(new TitledBorder(new EtchedBorder(), getFileFilter().getDescription()));
            gba.add(pathPanel, pathTextField, 0, 0, 2, 1, 1, 0, GBA.H, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            mas5AvailableList = new JList(new DefaultListModel());
            mas5AvailableList.setCellRenderer(new ListRenderer());
            mas5AvailableList.addListSelectionListener(new ListListener());
            mas5AvailableScrollPane = new JScrollPane(mas5AvailableList);
            mas5ListPanel = new JPanel();
            mas5ListPanel.setLayout(new GridBagLayout());
            mas5ListPanel.setBorder(new TitledBorder(new EtchedBorder(), "Data File Available"));
            gba.add(mas5ListPanel, mas5AvailableScrollPane, 0, 0, 1, 1, 1, 1, GBA.B, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            expressionTable = new JTable();
            expressionTable.setCellSelectionEnabled(true);
            expressionTable.setColumnSelectionAllowed(false);
            expressionTable.setRowSelectionAllowed(false);
            expressionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            expressionTable.getTableHeader().setReorderingAllowed(false);
            expressionTable.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent event) {
                    xRow = expressionTable.rowAtPoint(event.getPoint());
                    xColumn = expressionTable.columnAtPoint(event.getPoint());
                    checkLoadEnable();
                }
            });
            tableScrollPane = new JScrollPane(expressionTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            instructionsLabel = new JLabel();
            instructionsLabel.setForeground(java.awt.Color.red);
            String instructions = "<html>Click the upper-leftmost expression value. Click the <b>Load</b> button to finish.</html>";
            instructionsLabel.setText(instructions);
            tablePanel = new JPanel();
            tablePanel.setLayout(new GridBagLayout());
            tablePanel.setBorder(new TitledBorder(new EtchedBorder(), "Expression Table"));
            gba.add(tablePanel, tableScrollPane, 0, 0, 1, 2, 1, 1, GBA.B, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            gba.add(tablePanel, instructionsLabel, 0, 2, 1, 1, 1, 0, GBA.H, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            refAvailableList = new JList(new DefaultListModel());
            refAvailableList.setCellRenderer(new ListRenderer());
            refAvailableList.addListSelectionListener(new ListListener());
            refAvailableScrollPane = new JScrollPane(refAvailableList);
            refListPanel = new JPanel();
            refListPanel.setLayout(new GridBagLayout());
            refListPanel.setBorder(new TitledBorder(new EtchedBorder(), "Call File Available"));
            gba.add(refListPanel, refAvailableScrollPane, 0, 0, 1, 1, 1, 1, GBA.B, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            refTextField = new JTextField();
            refTextField.setEditable(false);
            refTextField.setBorder(new TitledBorder(new EtchedBorder(), "Selected Call File"));
            refTextField.setForeground(Color.black);
            refTextField.setFont(new Font("monospaced", Font.BOLD, 12));
            refPanel = new JPanel();
            refPanel.setLayout(new GridBagLayout());
            refPanel.setBorder(new TitledBorder(new EtchedBorder(), "Call File"));
            gba.add(refPanel, refTextField, 0, 0, 2, 1, 1, 0, GBA.H, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            annoTextField = new JTextField();
            annoTextField.setEditable(false);
            annoTextField.setForeground(Color.black);
            annoTextField.setFont(new Font("serif", Font.BOLD, 12));
            annoPanel = new JPanel();
            annoPanel.setLayout(new GridBagLayout());
            annoPanel.setBorder(new TitledBorder(new EtchedBorder(), "Annotation Fields"));
            gba.add(annoPanel, annoTextField, 0, 0, 2, 1, 1, 0, GBA.H, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            fileLoaderPanel = new JPanel();
            fileLoaderPanel.setLayout(new GridBagLayout());
            gba.add(fileLoaderPanel, mas5ListPanel, 0, 0, 1, 5, 1, 1, GBA.B, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            gba.add(fileLoaderPanel, refListPanel, 0, 6, 1, 4, 1, 1, GBA.B, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            gba.add(fileLoaderPanel, pathPanel, 2, 0, 1, 1, 3, 0, GBA.H, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            gba.add(fileLoaderPanel, tablePanel, 2, 1, 1, 6, 3, 1, GBA.B, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            gba.add(fileLoaderPanel, annoPanel, 2, 7, 1, 1, 3, 0, GBA.H, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            gba.add(fileLoaderPanel, refPanel, 2, 8, 1, 1, 3, 0, GBA.H, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
            splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileTreePane, fileLoaderPanel);
            splitPane.setPreferredSize(new java.awt.Dimension(600, 600));
            splitPane.setDividerLocation(200);
            gba.add(this, splitPane, 0, 0, 1, 1, 1, 1, GBA.B, GBA.C, new Insets(5, 5, 5, 5), 0, 0);
        }

        public void openDataPath() {
            fileTreePane.openDataPath();
        }

        public JTable getTable() {
            return expressionTable;
        }

        public int getXColumn() {
            return xColumn;
        }

        public int getXRow() {
            return xRow;
        }

        public void selectMas5File() {
            JFileChooser jfc = new JFileChooser(SuperExpressionFileLoader.DATA_PATH);
            jfc.setFileFilter(getFileFilter());
            int activityCode = jfc.showDialog(this, "Select");
            if (activityCode == JFileChooser.APPROVE_OPTION) {
                File target = jfc.getSelectedFile();
                processMas5File(target);
            }
        }

        public void setDataFileName(String fileName) {
            pathTextField.setText(fileName);
        }

        public void setCallFileName(String fileName) {
            refTextField.setText(fileName);
        }

        public void setTableModel(TableModel model) {
            expressionTable.setModel(model);
            int numCols = expressionTable.getColumnCount();
            for (int i = 0; i < numCols; i++) {
                expressionTable.getColumnModel().getColumn(i).setMinWidth(75);
            }
        }

        public void setFieldsText(String fieldsText) {
            annoTextField.setText(fieldsText);
        }

        private class ListRenderer extends DefaultListCellRenderer {

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                File file = (File) value;
                setText(file.getName());
                return this;
            }
        }

        private class ListListener implements javax.swing.event.ListSelectionListener {

            public void valueChanged(ListSelectionEvent lse) {
                File file;
                Object source = lse.getSource();
                if (source == mas5AvailableList) {
                    file = (File) (mas5AvailableList.getSelectedValue());
                    if (file == null || !(file.exists())) return;
                    processMas5File(file);
                    return;
                } else file = (File) (refAvailableList.getSelectedValue());
                loadCallFile(file);
            }
        }

        private class FileTreePaneEventHandler implements FileTreePaneListener {

            public void nodeSelected(FileTreePaneEvent event) {
                String filePath = (String) event.getValue("Path");
                Vector fileNames = (Vector) event.getValue("Filenames");
                if (fileNames.size() < 1) return;
                FileFilter Mas5FileFilter = getFileFilter();
                ((DefaultListModel) (mas5AvailableList.getModel())).clear();
                ((DefaultListModel) (refAvailableList.getModel())).clear();
                for (int i = 0; i < fileNames.size(); i++) {
                    File targetFile = new File((String) fileNames.elementAt(i));
                    if (Mas5FileFilter.accept(targetFile)) {
                        ((DefaultListModel) (mas5AvailableList.getModel())).addElement(new File((String) fileNames.elementAt(i)));
                    }
                    if (Mas5FileFilter.accept(targetFile)) {
                        ((DefaultListModel) (refAvailableList.getModel())).addElement(new File((String) fileNames.elementAt(i)));
                    }
                }
            }

            public void nodeCollapsed(FileTreePaneEvent event) {
            }

            public void nodeExpanded(FileTreePaneEvent event) {
            }
        }
    }
}

package gov.nasa.gsfc.visbard.gui.resourcemanip;

import gov.nasa.gsfc.visbard.model.VisbardMain;
import gov.nasa.gsfc.visbard.model.threadtask.soap.SoapTasks;
import gov.nasa.gsfc.visbard.model.threadtask.soap.SscWebTask;
import gov.nasa.gsfc.visbard.repository.resource.RDFWriter;
import gov.nasa.gsfc.visbard.util.BrowserLauncher;
import gov.nasa.gsfc.visbard.util.VisbardException;
import gsfc.nssdc.cdf.Attribute;
import gsfc.nssdc.cdf.CDF;
import gsfc.nssdc.cdf.CDFConstants;
import gsfc.nssdc.cdf.CDFException;
import gsfc.nssdc.cdf.Variable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.gjt.fredgc.unit.ParseException;
import org.gjt.fredgc.unit.TokenMgrError;
import org.gjt.fredgc.unit.Unit;

public class AutoGenRDFPanel extends JPanel {

    private JDialog fOwner, fOwner2, fCombineScalarsDialog;

    private JButton fPopulateWebServButton, fContinueButton, fCancelButton;

    private JButton fCancelButton2, fBackButton, fFinishButton;

    private JTable fFileIDTable, fVarSelectionTable, fDataImportParamsTable;

    private JComboBox fTimeComboBox, fLocationComboBox, fCoordSysComboBox, fWebServicesComboBox;

    private JPanel fStep2Panel;

    private File fFile, fValidRDF;

    private Vector fAttributes, fVariables;

    private ArrayList<Integer> fIDList;

    private ArrayList<Integer> fVarsToImport;

    private boolean fIsMhdTimeSource;

    private static int MAX_CDF_CHARS = 256;

    public static int MAX_CDF_VARS = 128;

    private static String CCMC_MHD_MODEL_STRING = "MHD Model (CCMC)";

    private int fLogicalSourceIndex, fTimeIndex, fStatus;

    private static final org.apache.log4j.Category sLogger = org.apache.log4j.Category.getInstance(AutoGenRDFPanel.class.getName());

    private class AttribSelectTableModel extends AbstractTableModel {

        private String[] fColumnNames;

        private Object[][] fData;

        public AttribSelectTableModel(String[] columnNames, Object[][] data) {
            fColumnNames = columnNames;
            fData = data;
        }

        public int getColumnCount() {
            return fColumnNames.length;
        }

        public int getRowCount() {
            return fData.length;
        }

        public String getColumnName(int col) {
            return fColumnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return fData[row][col];
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
            fData[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }

    private class ImportParamTableModel extends AbstractTableModel {

        private String[] fColumnNames;

        private Object[][] fData;

        public ImportParamTableModel(String[] columnNames, Object[][] data) {
            fColumnNames = columnNames;
            fData = data;
        }

        public int getColumnCount() {
            return fColumnNames.length;
        }

        public int getRowCount() {
            return fData.length;
        }

        public String getColumnName(int col) {
            return fColumnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return fData[row][col];
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
            fData[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }

    private class VBComboBoxRenderer extends JComboBox implements TableCellRenderer {

        public VBComboBoxRenderer(String[] items) {
            super(items);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            if (value.toString().equals("unknown")) {
                setBackground(new java.awt.Color(255, 0, 0));
                setToolTipText("Value needs to be valid in order to proceed");
            } else setToolTipText("");
            setSelectedItem(value);
            return this;
        }
    }

    private class VBTableCellRenderer extends JLabel implements TableCellRenderer {

        public VBTableCellRenderer() {
            this.setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            if (value != null) {
                setText(value.toString());
                if ((value.toString().equals("unknown")) || (value.toString().equals(""))) {
                    setBackground(new java.awt.Color(255, 0, 0));
                    setToolTipText("Value needs to be valid in order to proceed");
                } else if (column == 1) setToolTipText(value.toString()); else if ((table != null) && (table.getName() != null) && (table.getName().equals("DataImportParamSelection"))) {
                    if (column == 4) {
                        try {
                            Unit.newUnit(value.toString());
                            setToolTipText("");
                        } catch (ParseException pe) {
                            setBackground(new java.awt.Color(255, 0, 0));
                            setToolTipText("Invalid unit: " + pe.getMessage() + "  See link below for proper specification.");
                        } catch (TokenMgrError tme) {
                            setBackground(new java.awt.Color(255, 0, 0));
                            setToolTipText("Invalid character in unit: " + tme.getMessage() + ".  See link below for proper specification.");
                        }
                    } else if ((column == 5) && (value.toString().equals("Time"))) {
                        setToolTipText("This value must be set to \"Time\"");
                    } else setToolTipText("");
                } else setToolTipText("");
            } else {
                setText("");
                setToolTipText("");
            }
            return this;
        }
    }

    private class VBComboBoxEditor extends DefaultCellEditor {

        public VBComboBoxEditor(String[] items) {
            super(new JComboBox(items));
        }

        public JComboBox getComboBox() {
            return (JComboBox) this.getComponent();
        }
    }

    public AutoGenRDFPanel(JDialog owner, File file) throws VisbardException {
        fOwner = owner;
        fFile = file;
        fValidRDF = null;
        fStatus = AutoGenRDFDialog.TO_STEP_ONE;
        fIsMhdTimeSource = false;
        this.setOpaque(true);
        this.getFileInfo();
        this.initGUI();
        this.addListeners();
    }

    private void getFileInfo() throws VisbardException {
        sLogger.debug("Retriving file info for: " + fFile.getName());
        if (!fFile.getName().endsWith(".cdf")) throw new VisbardException("Automatic RDF generation is currently available for CDF files only");
        try {
            CDF cdf = null;
            cdf = CDF.open(fFile.getAbsolutePath(), CDFConstants.READONLYon);
            fAttributes = cdf.getAttributes();
            fVariables = cdf.getVariables();
            cdf.close();
        } catch (CDFException exc) {
            throw new VisbardException(exc.getMessage());
        }
    }

    private void initGUI() throws VisbardException {
        this.setLayout(new GridBagLayout());
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        JPanel panelFileIDLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        panelFileIDLabel.add(new JLabel("Attributes to use for Identification :   "));
        panelFileIDLabel.setMaximumSize(new Dimension(500, 25));
        leftPanel.add(panelFileIDLabel);
        String[] columnNames = { "Attrib Name", "Attrib Value", "Use for ID?" };
        Object fileIDData[][] = new Object[fAttributes.size()][MAX_CDF_CHARS];
        fLogicalSourceIndex = -1;
        for (int i = 0; i < fAttributes.size(); i++) {
            sLogger.debug("Retrieving attribute data for: " + fAttributes.get(i).toString());
            fileIDData[i][0] = fAttributes.get(i).toString();
            if (fileIDData[i][0].toString().toLowerCase().equals("logical_source")) fLogicalSourceIndex = i;
            try {
                fileIDData[i][1] = ((Attribute) fAttributes.get(i)).getEntry(0).getData().toString();
            } catch (CDFException cdfex) {
                sLogger.warn(cdfex.getMessage());
            }
            fileIDData[i][2] = new Boolean(((fileIDData[i][0].equals("TITLE")) || (fileIDData[i][0].equals("Descriptor")) || (fileIDData[i][0].equals("model_name")) || (fileIDData[i][0].equals("model_type")) || (fileIDData[i][0].equals("output_type"))));
        }
        fFileIDTable = new JTable(new AttribSelectTableModel(columnNames, fileIDData));
        JScrollPane tableScrollPane = new JScrollPane(fFileIDTable);
        fFileIDTable.setDefaultRenderer(String.class, new VBTableCellRenderer());
        fFileIDTable.setPreferredScrollableViewportSize(new Dimension(280, 140));
        fFileIDTable.setMaximumSize(new Dimension(400, 800));
        fFileIDTable.getColumnModel().getColumn(2).setPreferredWidth(45);
        fFileIDTable.getTableHeader().setReorderingAllowed(false);
        fFileIDTable.setRowSelectionAllowed(false);
        fFileIDTable.setColumnSelectionAllowed(false);
        leftPanel.add(tableScrollPane);
        JPanel panelTimeLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        panelTimeLabel.add(new JLabel("Time source:  (\"Epoch\" recommended)"));
        panelTimeLabel.setMaximumSize(new Dimension(500, 25));
        leftPanel.add(panelTimeLabel);
        ItemListener timeItemListener = new ItemListener() {

            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getItem().toString().equals(CCMC_MHD_MODEL_STRING)) {
                    fIsMhdTimeSource = (itemEvent.getStateChange() == ItemEvent.SELECTED);
                } else {
                    for (int i = 0; i < fVariables.size(); i++) if (itemEvent.getItem().toString().equals(fVarSelectionTable.getValueAt(i, 0))) fVarSelectionTable.setValueAt(new Boolean(itemEvent.getStateChange() == ItemEvent.SELECTED), i, 2);
                }
            }
        };
        ItemListener locationItemListener = new ItemListener() {

            public void itemStateChanged(ItemEvent itemEvent) {
                for (int i = 0; i < fVariables.size(); i++) {
                    if (itemEvent.getItem().toString().equals(CCMC_MHD_MODEL_STRING)) {
                        if (fVarSelectionTable.getValueAt(i, 0).toString().equals("x") || fVarSelectionTable.getValueAt(i, 0).toString().equals("y") || fVarSelectionTable.getValueAt(i, 0).toString().equals("z")) fVarSelectionTable.setValueAt(new Boolean(itemEvent.getStateChange() == ItemEvent.SELECTED), i, 2);
                    } else if (itemEvent.getItem().toString().equals(fVarSelectionTable.getValueAt(i, 0))) fVarSelectionTable.setValueAt(new Boolean(itemEvent.getStateChange() == ItemEvent.SELECTED), i, 2);
                }
            }
        };
        fTimeComboBox = new JComboBox();
        fTimeComboBox.setPreferredSize(new Dimension(180, 25));
        fTimeComboBox.setMaximumSize(new Dimension(180, 25));
        fTimeComboBox.addItem("Not Selected");
        fTimeComboBox.addItem(CCMC_MHD_MODEL_STRING);
        fTimeComboBox.addItemListener(timeItemListener);
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        timePanel.setMaximumSize(new Dimension(500, 80));
        timePanel.add(fTimeComboBox);
        leftPanel.add(timePanel);
        JPanel panelLocationLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        panelLocationLabel.add(new JLabel("Location and Coordinate System:  (optional)"));
        panelLocationLabel.setMaximumSize(new Dimension(500, 25));
        leftPanel.add(panelLocationLabel);
        fLocationComboBox = new JComboBox();
        fLocationComboBox.setPreferredSize(new Dimension(180, 25));
        fLocationComboBox.addItem("Not Selected");
        fLocationComboBox.addItem(CCMC_MHD_MODEL_STRING);
        fLocationComboBox.addItemListener(locationItemListener);
        fCoordSysComboBox = new JComboBox();
        fCoordSysComboBox.setPreferredSize(new Dimension(50, 25));
        fCoordSysComboBox.addItem("GSE");
        fCoordSysComboBox.setEnabled(false);
        JPanel locPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        locPanel.setMaximumSize(new Dimension(500, 80));
        locPanel.add(fLocationComboBox);
        locPanel.add(fCoordSysComboBox);
        leftPanel.add(locPanel);
        JPanel panelWSLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        panelWSLabel.add(new JLabel("SSCWeb Web Services Name:  (optional)"));
        panelWSLabel.setMaximumSize(new Dimension(500, 25));
        leftPanel.add(panelWSLabel);
        fWebServicesComboBox = new JComboBox();
        fWebServicesComboBox.setPreferredSize(new Dimension(125, 25));
        fPopulateWebServButton = new JButton("Populate List");
        JPanel webServPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        webServPanel.add(fWebServicesComboBox);
        webServPanel.add(fPopulateWebServButton);
        webServPanel.setMaximumSize(new Dimension(500, 80));
        leftPanel.add(webServPanel);
        JPanel panelDataSelection = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        panelDataSelection.add(new JLabel("Variables to Import :   "));
        panelDataSelection.setMaximumSize(new Dimension(600, 25));
        rightPanel.add(panelDataSelection);
        String dataColumnNames[] = { "Variable Name", "Description", "Import?" };
        Object dataSelectionData[][] = new Object[fVariables.size()][MAX_CDF_CHARS];
        for (int i = 0; i < fVariables.size(); i++) {
            sLogger.debug("Retrieving \"FIELDNAM\" and \"DICT_KEY\" data for: " + fVariables.get(i).toString());
            dataSelectionData[i][0] = fVariables.get(i).toString();
            try {
                dataSelectionData[i][1] = ((Variable) fVariables.get(i)).getEntryData("FIELDNAM").toString();
            } catch (CDFException cdfex) {
                try {
                    dataSelectionData[i][1] = ((Variable) fVariables.get(i)).getEntryData("description").toString();
                } catch (CDFException cdfex2) {
                    dataSelectionData[i][1] = "<Not Available>";
                }
            }
            dataSelectionData[i][2] = new Boolean(false);
            fTimeComboBox.addItem(dataSelectionData[i][0]);
            fLocationComboBox.addItem(dataSelectionData[i][0]);
            try {
                String dictKey = ((Variable) fVariables.get(i)).getEntryData("DICT_KEY").toString();
                dataSelectionData[i][1] = new String(dataSelectionData[i][1] + " (" + dictKey + ")");
            } catch (CDFException cdfex) {
            }
        }
        fVarSelectionTable = new JTable(new AttribSelectTableModel(dataColumnNames, dataSelectionData));
        JScrollPane tableScrollPane2 = new JScrollPane(fVarSelectionTable);
        fVarSelectionTable.setDefaultRenderer(String.class, new VBTableCellRenderer());
        fVarSelectionTable.setPreferredScrollableViewportSize(new Dimension(375, 300));
        fVarSelectionTable.getColumnModel().getColumn(2).setPreferredWidth(15);
        fVarSelectionTable.getTableHeader().setReorderingAllowed(false);
        fVarSelectionTable.setRowSelectionAllowed(false);
        fVarSelectionTable.setColumnSelectionAllowed(false);
        rightPanel.add(tableScrollPane2);
        fCancelButton = new JButton("Cancel");
        fContinueButton = new JButton("Continue");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 15));
        buttonPanel.add(fCancelButton);
        buttonPanel.add(fContinueButton);
        buttonPanel.setMaximumSize(new Dimension(500, 80));
        rightPanel.add(buttonPanel);
        fTimeComboBox.setSelectedItem("Not Selected");
        fTimeComboBox.setSelectedItem("Epoch");
        fLocationComboBox.setSelectedItem("Not Selected");
        this.add(leftPanel, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        this.add(rightPanel, new GridBagConstraints(1, GridBagConstraints.RELATIVE, 1, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    }

    private void initGUI2() {
        fStep2Panel.setLayout(new GridBagLayout());
        fStep2Panel.add(new JLabel("Choose Data Size, Type, Units, and Name to use within ViSBARD:"), new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        String dataColumnNames[] = { "CDF Var. Name", "CDF Var. Info", "Data Size", "Data Type", "Units", "ViSBARD Var. Name" };
        Object dataSelectionData[][] = new Object[fVarsToImport.size()][MAX_CDF_CHARS];
        if (fIsMhdTimeSource) System.out.println("MHD Time source selected");
        for (int i = 0; i < fVarsToImport.size(); i++) {
            Variable var = (Variable) fVariables.get(((Integer) fVarsToImport.get(i)).intValue());
            try {
                sLogger.debug("To import: " + var.toString());
                dataSelectionData[i][0] = var.toString();
                dataSelectionData[i][1] = var.getEntryData("FIELDNAM").toString();
            } catch (CDFException cdfex) {
                sLogger.warn("FIELDNAM: " + cdfex.getMessage());
                try {
                    dataSelectionData[i][1] = var.getEntryData("description").toString();
                } catch (CDFException cdfex2) {
                    dataSelectionData[i][1] = "<Not Available>";
                }
            }
            try {
                String dictKey = ((Variable) fVariables.get(((Integer) fVarsToImport.get(i)).intValue())).getEntryData("DICT_KEY").toString();
                dataSelectionData[i][1] = new String(dataSelectionData[i][1] + " (" + dictKey + ")");
            } catch (CDFException cdfex) {
            }
            long dataSize = var.getDataType();
            if ((dataSize == CDF.CDF_INT1) || (dataSize == CDF.CDF_INT2) || (dataSize == CDF.CDF_INT4)) dataSelectionData[i][2] = "int"; else if ((dataSize == CDF.CDF_REAL4) || (dataSize == CDF.CDF_FLOAT)) dataSelectionData[i][2] = "float"; else if ((dataSize == CDF.CDF_REAL8) || (dataSize == CDF.CDF_DOUBLE) || (dataSize == CDF.CDF_EPOCH)) dataSelectionData[i][2] = "double"; else dataSelectionData[i][2] = "unknown";
            if (var.getNumDims() > 1) dataSelectionData[i][3] = "unknown"; else if (var.getNumDims() == 1) dataSelectionData[i][3] = "vector"; else dataSelectionData[i][3] = "scalar";
            try {
                String units = ((Variable) fVariables.get(((Integer) fVarsToImport.get(i)).intValue())).getEntryData("UNITS").toString();
                if ((units != null) || (units != "")) dataSelectionData[i][4] = units; else dataSelectionData[i][4] = "unknown";
            } catch (CDFException cdfex) {
                try {
                    String units = ((Variable) fVariables.get(((Integer) fVarsToImport.get(i)).intValue())).getEntryData("units").toString();
                    if ((units != null) || (units != "")) dataSelectionData[i][4] = units; else dataSelectionData[i][4] = "unknown";
                } catch (CDFException cdfex2) {
                    sLogger.warn("UNITS: " + cdfex.getMessage());
                    dataSelectionData[i][4] = "unknown";
                }
            }
            if (dataSelectionData[i][0].toString().equals(fTimeComboBox.getSelectedItem().toString())) dataSelectionData[i][5] = "Time"; else if (((String) dataSelectionData[i][1]).toLowerCase().indexOf("position") != -1) {
                dataSelectionData[i][5] = "Location";
            } else {
                try {
                    dataSelectionData[i][5] = var.getEntryData("FIELDNAM").toString();
                } catch (CDFException cdfex) {
                    dataSelectionData[i][5] = "unknown";
                }
            }
        }
        fDataImportParamsTable = new JTable(new ImportParamTableModel(dataColumnNames, dataSelectionData));
        fDataImportParamsTable.setName("DataImportParamSelection");
        TableColumn sizeCol = fDataImportParamsTable.getColumnModel().getColumn(2);
        sizeCol.setCellEditor(new VBComboBoxEditor(ResourceView.sDataSizeVals));
        sizeCol.setCellRenderer(new VBComboBoxRenderer(ResourceView.sDataSizeVals));
        TableColumn typeCol = fDataImportParamsTable.getColumnModel().getColumn(3);
        final VBComboBoxEditor dataTypeComboBoxEditor = new VBComboBoxEditor(ResourceView.sDataTypeVals);
        typeCol.setCellEditor(dataTypeComboBoxEditor);
        TableColumn unitsCol = fDataImportParamsTable.getColumnModel().getColumn(4);
        final VBComboBoxEditor unitsComboBoxEditor = new VBComboBoxEditor(ResourceView.sUnitVals);
        unitsComboBoxEditor.getComboBox().setEditable(true);
        unitsCol.setCellEditor(unitsComboBoxEditor);
        TableColumn displayCol = fDataImportParamsTable.getColumnModel().getColumn(5);
        final VBComboBoxEditor displayComboBoxEditor = new VBComboBoxEditor(ResourceView.sDisplayNames);
        displayComboBoxEditor.getComboBox().setEditable(true);
        displayCol.setCellEditor(displayComboBoxEditor);
        JScrollPane tableScrollPane3 = new JScrollPane(fDataImportParamsTable);
        fDataImportParamsTable.setDefaultRenderer(String.class, new VBTableCellRenderer());
        fDataImportParamsTable.setPreferredScrollableViewportSize(new Dimension(575, 300));
        fDataImportParamsTable.setRowSelectionAllowed(false);
        fDataImportParamsTable.setColumnSelectionAllowed(false);
        fDataImportParamsTable.getTableHeader().setReorderingAllowed(false);
        fDataImportParamsTable.setRowHeight(20);
        fStep2Panel.add(tableScrollPane3, new GridBagConstraints(GridBagConstraints.RELATIVE, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        JButton combineScalarsButton = new JButton("Combine 3 scalar components into a vector variable...");
        combineScalarsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fCombineScalarsDialog = new JDialog();
                fCombineScalarsDialog.setTitle("Combine Scalars into a Vector");
                fCombineScalarsDialog.setModal(true);
                Box combineScalarsPanel = new Box(BoxLayout.Y_AXIS);
                JPanel xComponentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
                JPanel yComponentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
                JPanel zComponentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
                JPanel varNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
                JPanel unitsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
                xComponentPanel.add(new JLabel("X component: "));
                yComponentPanel.add(new JLabel("Y component: "));
                zComponentPanel.add(new JLabel("Z component: "));
                varNamePanel.add(new JLabel("ViSBARD variable name: "));
                unitsPanel.add(new JLabel("Units: "));
                int numVarsToImport = fDataImportParamsTable.getModel().getRowCount();
                final JComboBox xComponentBox = new JComboBox();
                final JComboBox yComponentBox = new JComboBox();
                final JComboBox zComponentBox = new JComboBox();
                xComponentBox.setPreferredSize(new Dimension(180, 25));
                yComponentBox.setPreferredSize(new Dimension(180, 25));
                zComponentBox.setPreferredSize(new Dimension(180, 25));
                xComponentBox.setMaximumSize(new Dimension(180, 25));
                yComponentBox.setMaximumSize(new Dimension(180, 25));
                zComponentBox.setMaximumSize(new Dimension(180, 25));
                final VBComboBoxEditor varNameBox = new VBComboBoxEditor(ResourceView.sDisplayNames);
                final VBComboBoxEditor unitsBox = new VBComboBoxEditor(ResourceView.sUnitVals);
                varNameBox.getComboBox().setEditable(true);
                unitsBox.getComboBox().setEditable(true);
                for (int i = 0; i < numVarsToImport; i++) {
                    xComponentBox.addItem(fDataImportParamsTable.getModel().getValueAt(i, 0).toString());
                    yComponentBox.addItem(fDataImportParamsTable.getModel().getValueAt(i, 0).toString());
                    zComponentBox.addItem(fDataImportParamsTable.getModel().getValueAt(i, 0).toString());
                }
                xComponentPanel.add(xComponentBox);
                yComponentPanel.add(yComponentBox);
                zComponentPanel.add(zComponentBox);
                varNamePanel.add(varNameBox.getComboBox());
                unitsPanel.add(unitsBox.getComboBox());
                combineScalarsPanel.add(xComponentPanel);
                combineScalarsPanel.add(yComponentPanel);
                combineScalarsPanel.add(zComponentPanel);
                combineScalarsPanel.add(varNamePanel);
                combineScalarsPanel.add(unitsPanel);
                JButton cancelCombineButton = new JButton("Cancel");
                JButton combineButton = new JButton("Combine");
                JPanel combineButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 15));
                combineButtonPanel.add(cancelCombineButton);
                combineButtonPanel.add(combineButton);
                combineScalarsPanel.add(combineButtonPanel);
                cancelCombineButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        fCombineScalarsDialog.dispose();
                    }
                });
                combineButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (varNameBox.getComboBox().getSelectedItem().toString().equals("")) {
                            JOptionPane errorPane = new JOptionPane("ViSBARD variable name must be specified", JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                            JDialog errorDialog = errorPane.createDialog(fCombineScalarsDialog, "Variable Name Empty");
                            errorDialog.show();
                            errorDialog.dispose();
                            return;
                        }
                        int nVars = fDataImportParamsTable.getModel().getRowCount();
                        String xComponent = xComponentBox.getSelectedItem().toString();
                        String yComponent = yComponentBox.getSelectedItem().toString();
                        String zComponent = zComponentBox.getSelectedItem().toString();
                        boolean allScalars = true;
                        boolean allDataSizesConsistent = true;
                        String lastDataSize = new String("not_initialized");
                        for (int i = 0; i < nVars; i++) {
                            if (fDataImportParamsTable.getModel().getValueAt(i, 0).toString().equals(xComponent) || fDataImportParamsTable.getModel().getValueAt(i, 0).toString().equals(yComponent) || fDataImportParamsTable.getModel().getValueAt(i, 0).toString().equals(zComponent)) {
                                if (!fDataImportParamsTable.getModel().getValueAt(i, 3).toString().equals("scalar")) allScalars = false;
                                if (lastDataSize.equals("not_initialized")) lastDataSize = fDataImportParamsTable.getModel().getValueAt(i, 2).toString(); else if (!lastDataSize.equals(fDataImportParamsTable.getModel().getValueAt(i, 2).toString())) allDataSizesConsistent = false;
                            }
                        }
                        if (!allScalars) {
                            JOptionPane errorPane = new JOptionPane("All selected variables must be scalars", JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                            JDialog errorDialog = errorPane.createDialog(fCombineScalarsDialog, "Error");
                            errorDialog.show();
                            errorDialog.dispose();
                            return;
                        }
                        if (!allDataSizesConsistent) {
                            JOptionPane errorPane = new JOptionPane("All selected variables must be of the same data type", JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                            JDialog errorDialog = errorPane.createDialog(fCombineScalarsDialog, "Error");
                            errorDialog.show();
                            errorDialog.dispose();
                            return;
                        }
                        String varName = varNameBox.getComboBox().getSelectedItem().toString();
                        for (int i = 0; i < nVars; i++) {
                            if (fDataImportParamsTable.getModel().getValueAt(i, 0).toString().equals(xComponent)) {
                                fDataImportParamsTable.getModel().setValueAt(unitsBox.getComboBox().getSelectedItem().toString(), i, 4);
                                fDataImportParamsTable.getModel().setValueAt(varName + "__X", i, 5);
                            }
                            if (fDataImportParamsTable.getModel().getValueAt(i, 0).toString().equals(yComponent)) {
                                fDataImportParamsTable.getModel().setValueAt(unitsBox.getComboBox().getSelectedItem().toString(), i, 4);
                                fDataImportParamsTable.getModel().setValueAt(varName + "__Y", i, 5);
                            }
                            if (fDataImportParamsTable.getModel().getValueAt(i, 0).toString().equals(zComponent)) {
                                fDataImportParamsTable.getModel().setValueAt(unitsBox.getComboBox().getSelectedItem().toString(), i, 4);
                                fDataImportParamsTable.getModel().setValueAt(varName + "__Z", i, 5);
                            }
                        }
                        fCombineScalarsDialog.dispose();
                    }
                });
                fCombineScalarsDialog.getContentPane().add(combineScalarsPanel);
                fCombineScalarsDialog.setSize(new Dimension(400, 300));
                fCombineScalarsDialog.setResizable(true);
                fCombineScalarsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                fCombineScalarsDialog.setLocation(fOwner2.getLocation());
                fCombineScalarsDialog.setModal(true);
                fCombineScalarsDialog.show();
            }
        });
        JLabel unitsLabel1 = new JLabel("For listing of units specification, visit:");
        JButton unitsButton = new JButton("http://unit.sourceforge.net/unitlist.html");
        unitsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    BrowserLauncher.openURL("http://unit.sourceforge.net/unitlist.html");
                } catch (IOException ioe) {
                    sLogger.error("Error while opening URL: " + ioe.getMessage());
                }
            }
        });
        JPanel unitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        unitPanel.add(unitsLabel1);
        unitPanel.add(unitsButton);
        fStep2Panel.add(combineScalarsButton, new GridBagConstraints(GridBagConstraints.RELATIVE, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        fStep2Panel.add(unitPanel, new GridBagConstraints(GridBagConstraints.RELATIVE, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        fCancelButton2 = new JButton("Cancel");
        fBackButton = new JButton("Back");
        fFinishButton = new JButton("Finish");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 15));
        buttonPanel.add(fCancelButton2);
        buttonPanel.add(fBackButton);
        buttonPanel.add(fFinishButton);
        fStep2Panel.add(buttonPanel, new GridBagConstraints(GridBagConstraints.RELATIVE, 4, 1, 1, 0, 0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    }

    private void addListeners() {
        fCancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fStatus = AutoGenRDFDialog.TO_END;
                fOwner.dispose();
            }
        });
        fPopulateWebServButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SscWebTask task = new SscWebTask(SoapTasks.GET_SSC_SATS_AVAIL);
                task.go();
                if (!task.isSuccess()) {
                    sLogger.error("Error getting available SSC satellites.  Aborting.\n");
                }
                String[][] missionGroups = task.getAvailableSatellites();
                for (int i = 0; i < missionGroups.length; i++) fWebServicesComboBox.addItem(missionGroups[i][0]);
                fWebServicesComboBox.addItem("Not Selected");
                fWebServicesComboBox.setSelectedItem("Not Selected");
            }
        });
        fContinueButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fIDList = new ArrayList<Integer>();
                for (int i = 0; i < fFileIDTable.getRowCount(); i++) {
                    if (((Boolean) fFileIDTable.getValueAt(i, 2)).booleanValue()) fIDList.add(new Integer(i));
                }
                if (fIDList.size() < 1) {
                    JOptionPane errorPane = new JOptionPane("At least one Attribute needs to be " + "selected for identification purposes", JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    JDialog errorDialog = errorPane.createDialog(fOwner, "No ID Attributes selected");
                    errorDialog.show();
                    errorDialog.dispose();
                    return;
                }
                boolean importTimeSource = false;
                String errMsg = new String("The variable selected for \"Time source\"" + " must be slated to be imported");
                fVarsToImport = new ArrayList<Integer>();
                if (fTimeComboBox.getSelectedItem().toString().equals("Not Selected")) errMsg = "You must select a variable as your \"Time source\""; else for (int i = 0; i < fVariables.size(); i++) {
                    if (((Boolean) fVarSelectionTable.getValueAt(i, 2)).booleanValue()) {
                        fVarsToImport.add(new Integer(i));
                        if (fTimeComboBox.getSelectedItem().toString().equals(fVarSelectionTable.getValueAt(i, 0))) {
                            importTimeSource = true;
                            fTimeIndex = i;
                        }
                    }
                }
                if (!importTimeSource && !fIsMhdTimeSource) {
                    JOptionPane errorPane = new JOptionPane(errMsg, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    JDialog errorDialog = errorPane.createDialog(fOwner, "Time variable needs to be imported");
                    errorDialog.show();
                    errorDialog.dispose();
                    return;
                }
                fOwner2 = new JDialog();
                fOwner2.setTitle(AutoGenRDFDialog.sTitle2 + "  --  " + fFile.getName());
                fOwner2.setModal(true);
                fStep2Panel = new JPanel();
                initGUI2();
                addListeners2();
                fOwner2.getContentPane().add(fStep2Panel);
                fOwner2.setSize(fOwner.getWidth(), fOwner.getHeight());
                fOwner2.setResizable(true);
                fOwner2.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                fOwner2.setLocation(fOwner.getLocation());
                WindowAdapter wa = new WindowAdapter() {

                    public void windowClosing(WindowEvent e) {
                        sLogger.debug("AutoGenRDF Window closing");
                        setStatus(AutoGenRDFDialog.TO_END);
                    }
                };
                fOwner2.addWindowListener(wa);
                fOwner2.setModal(true);
                fStatus = AutoGenRDFDialog.TO_STEP_TWO;
                fOwner.hide();
            }
        });
    }

    protected void show2() {
        fOwner2.show();
    }

    protected int getStatus() {
        return fStatus;
    }

    protected void setStatus(int status) {
        fStatus = status;
    }

    private void addListeners2() {
        fCancelButton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fStatus = AutoGenRDFDialog.TO_END;
                fOwner2.dispose();
            }
        });
        fBackButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fStatus = AutoGenRDFDialog.TO_STEP_ONE;
                fOwner2.dispose();
            }
        });
        fFinishButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fDataImportParamsTable.clearSelection();
                fDataImportParamsTable.editCellAt(0, 0);
                ArrayList invalidItems = new ArrayList();
                boolean timeFound = false;
                for (int i = 0; i < fDataImportParamsTable.getRowCount(); i++) {
                    if (fDataImportParamsTable.getValueAt(i, 2).toString().equals("unknown")) invalidItems.add("Invalid Data Size for \"" + fDataImportParamsTable.getValueAt(i, 0).toString() + "\"");
                    if (fDataImportParamsTable.getValueAt(i, 3).toString().equals("unknown")) invalidItems.add("Invalid Data Type for \"" + fDataImportParamsTable.getValueAt(i, 0).toString() + "\"");
                    try {
                        Unit.newUnit(fDataImportParamsTable.getValueAt(i, 4).toString());
                        if ((fDataImportParamsTable.getValueAt(i, 4).toString().equals("")) || (fDataImportParamsTable.getValueAt(i, 4).toString().equals("unknown"))) invalidItems.add("Invalid Units for \"" + fDataImportParamsTable.getValueAt(i, 0).toString() + "\"");
                    } catch (ParseException pe) {
                        invalidItems.add("Could not parse units for \"" + fDataImportParamsTable.getValueAt(i, 0).toString() + "\" - " + pe.getMessage());
                    } catch (TokenMgrError tme) {
                        invalidItems.add("Could not parse units for \"" + fDataImportParamsTable.getValueAt(i, 0).toString() + "\" - " + tme.getMessage());
                    }
                    if ((fDataImportParamsTable.getValueAt(i, 5).toString().equals("")) || (fDataImportParamsTable.getValueAt(i, 5).toString().equals("unknown"))) invalidItems.add("Invalid Display Name for \"" + fDataImportParamsTable.getValueAt(i, 0).toString() + "\""); else if (fDataImportParamsTable.getValueAt(i, 5).toString().equals("Time")) timeFound = true;
                }
                if (!timeFound && !fIsMhdTimeSource) invalidItems.add("\"Time\" must be listed as one of the Display Names");
                if (invalidItems.size() > 0) {
                    String errMsg = new String("The following item(s) are invalid and must be corrected before generating a new RDF:\n");
                    for (int i = 0; i < invalidItems.size(); i++) {
                        errMsg += "  * " + invalidItems.get(i).toString() + "\n";
                    }
                    sLogger.error(errMsg);
                    JOptionPane errorPane = new JOptionPane(errMsg, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    JDialog errorDialog = errorPane.createDialog(fOwner2, "Error during validation of import parameters");
                    errorDialog.show();
                    errorDialog.dispose();
                    return;
                }
                try {
                    generateNewRDF();
                } catch (VisbardException ve) {
                    ve.showErrorMessage();
                    return;
                }
                fStatus = AutoGenRDFDialog.TO_END;
                fOwner.dispose();
                fOwner2.dispose();
                try {
                    JOptionPane infoPane = new JOptionPane("New Resource Description File (RDF) successfully created in file:\n  " + fValidRDF.getCanonicalPath(), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    JDialog infoDialog = infoPane.createDialog(fOwner, "RDF Successfully Created");
                    infoDialog.show();
                    infoDialog.dispose();
                } catch (IOException ioe) {
                    sLogger.error("Error showing full path of new RDF: " + ioe.getMessage());
                }
            }
        });
    }

    private void generateNewRDF() throws VisbardException {
        String missionName = new String();
        String instrumentName = new String();
        String resourceName = new String("");
        String fileBase = new String();
        String webServName = new String();
        if ((fWebServicesComboBox.getSelectedItem() != null) && (!fWebServicesComboBox.getSelectedItem().toString().equals("")) && (!fWebServicesComboBox.getSelectedItem().toString().equals("Not Selected"))) missionName = fWebServicesComboBox.getSelectedItem().toString().replaceAll("[[^a-z]&&[^A-Z]]", "") + "_"; else missionName = null;
        int descriptorIndex = -1;
        for (int i = 0; i < fFileIDTable.getRowCount(); i++) if (fFileIDTable.getValueAt(i, 0).toString().equals("Descriptor")) descriptorIndex = i;
        if (descriptorIndex != -1) instrumentName = fFileIDTable.getValueAt(descriptorIndex, 1).toString().substring(0, 3).replaceAll("[[^a-z]&&[^A-Z]]", ""); else instrumentName = fFileIDTable.getValueAt(((Integer) fIDList.get(0)).intValue(), 1).toString().substring(0, 3).replaceAll("[[^a-z]&&[^A-Z]]", "");
        String filename = new String(VisbardMain.getSettingsDir().getAbsolutePath() + File.separator + "rdf" + File.separator + "CDF_AutoGen_");
        if ((missionName != null) && (!missionName.equals("")) && (!missionName.equals("Not Selected"))) filename += missionName.replaceAll("[[^a-z]&&[^A-Z]]", "") + "_"; else filename += "NA_";
        if (instrumentName != null) filename += instrumentName + "_"; else filename += "NA_";
        filename += ".xml";
        for (int i = 0; i < fIDList.size(); i++) {
            resourceName += fFileIDTable.getValueAt(((Integer) fIDList.get(i)).intValue(), 1).toString() + "  ";
        }
        String idKeys[][] = new String[fIDList.size()][MAX_CDF_VARS];
        for (int i = 0; i < fIDList.size(); i++) {
            idKeys[i][0] = fFileIDTable.getValueAt(((Integer) fIDList.get(i)).intValue(), 0).toString();
            idKeys[i][1] = fFileIDTable.getValueAt(((Integer) fIDList.get(i)).intValue(), 1).toString();
        }
        if (fLogicalSourceIndex != -1) fileBase = fFileIDTable.getValueAt(fLogicalSourceIndex, 1).toString(); else fileBase = null;
        if ((fWebServicesComboBox.getSelectedItem() != null) && (!fWebServicesComboBox.getSelectedItem().toString().equals("")) && (!fWebServicesComboBox.getSelectedItem().toString().equals("Not Selected"))) webServName = fWebServicesComboBox.getSelectedItem().toString(); else webServName = null;
        String varMap[][] = new String[fDataImportParamsTable.getRowCount()][MAX_CDF_VARS];
        for (int i = 0; i < fDataImportParamsTable.getRowCount(); i++) {
            varMap[i][0] = fDataImportParamsTable.getValueAt(i, 0).toString();
            varMap[i][1] = fDataImportParamsTable.getValueAt(i, 5).toString();
            varMap[i][2] = fDataImportParamsTable.getValueAt(i, 2).toString();
            varMap[i][3] = fDataImportParamsTable.getValueAt(i, 3).toString();
            varMap[i][4] = fDataImportParamsTable.getValueAt(i, 4).toString();
        }
        fValidRDF = RDFWriter.generateRDF(filename, false, resourceName, idKeys, fileBase, webServName, varMap);
        if (fValidRDF == null) throw new VisbardException("Output RDF is null");
        return;
    }

    File getGeneratedRDF() {
        return fValidRDF;
    }
}

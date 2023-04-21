package de.ibis.permoto.gui.solver.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import org.apache.log4j.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import de.ibis.permoto.gui.img.ImageLoader;
import de.ibis.permoto.gui.modelling.util.editors.ButtonCellEditor;
import de.ibis.permoto.gui.modelling.util.editors.ImagedComboBoxCellEditorFactory;
import de.ibis.permoto.gui.solver.PerMoToSolverGUI;
import de.ibis.permoto.gui.textual.table.DisabledCellRenderer;
import de.ibis.permoto.gui.textual.table.ExactCellEditor;
import de.ibis.permoto.model.basic.scenario.Station;
import de.ibis.permoto.model.basic.types.StationTypeType;
import de.ibis.permoto.model.definitions.IClassSection;
import de.ibis.permoto.model.definitions.IStationSection;
import de.ibis.permoto.model.definitions.impl.PerMoToBusinessCase;
import de.ibis.permoto.solver.sim.mgt.definitions.ISimulationDefinition;
import de.ibis.permoto.util.Constants;

/**
 * Created by IntelliJ IDEA. User: orsotronIII Date: 26-lug-2005 Time: 16.08.15
 * Modified by Bertoli Marco 29/09/2005, 7-oct-2005 9-jan-2006 -->
 * ComboBoxCellEditor
 */
public class MeasurePanel extends JPanel implements Constants {

    private static final long serialVersionUID = 8647167676212587802L;

    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(MeasurePanel.class);

    /** Interfaces for model class data exchange. */
    protected IClassSection bcClassSection;

    /** Interfaces for model station data exchange. */
    protected IStationSection bcStationSection;

    /** Interfaces for simulation data exchange. */
    protected ISimulationDefinition simData;

    /** Warning panel, used for warning the user of missing input. */
    protected WarningScrollTable warningPanel;

    /** Label containing description of this panel's purpose. */
    private JLabel descrLabel = new JLabel(Constants.MEASURES_DESCRIPTION);

    /** Table containing measure data. */
    protected MeasureTable measureTable;

    /** Button for measure addition. */
    private JButton addMeasureButton;

    private static String WORKFLOW = "Workflow";

    private static String ITEM = "Item";

    private static String SERVICE = "Service";

    private static String DELAYITEM = "Delay Item";

    /** Types of measures selectable. */
    protected String[] measureTypes = new String[] { ISimulationDefinition.MEASURE_QL, ISimulationDefinition.MEASURE_QT, ISimulationDefinition.MEASURE_RP, ISimulationDefinition.MEASURE_U, ISimulationDefinition.MEASURE_X, ISimulationDefinition.MEASURE_CLASS_RP };

    /** ClassIDs of the business case */
    protected List<String> classIDs;

    /** Measure selection ComboBox. */
    protected JComboBox measureSelection = new JComboBox(measureTypes);

    /** Class Selection ComboBox */
    protected JComboBox classSelection;

    /** Level Selection ComboBox WORKFLOW, ITEM, SERVICE */
    protected JComboBox levelSelection;

    /** Editors and renderers for station data in the table. */
    protected ImagedComboBoxCellEditorFactory stationsCombos;

    /** Editors and renderers for class data in the table. */
    protected ImagedComboBoxCellEditorFactory classesCombos;

    private PerMoToSolverGUI wizard;

    private JTextField classNameField;

    /** Deletes a measure from list. */
    protected AbstractAction deleteMeasure = new AbstractAction("") {

        private static final long serialVersionUID = 6723473914228434205L;

        {
            putValue(Action.SHORT_DESCRIPTION, "Deletes this measure");
            putValue(Action.SMALL_ICON, ImageLoader.loadImage("Delete"));
        }

        public void actionPerformed(ActionEvent e) {
            int index = measureTable.getSelectedRow();
            if (index >= 0 && index < measureTable.getRowCount()) {
                deleteMeasure(index);
            }
        }
    };

    /** Deletes a measure from list. */
    protected AbstractAction deleteAllMeasures = new AbstractAction("Remove All") {

        private static final long serialVersionUID = 6723473914228434205L;

        {
            putValue(Action.SHORT_DESCRIPTION, "Deletes all measures");
            putValue(Action.SMALL_ICON, ImageLoader.loadImage("Delete"));
        }

        public void actionPerformed(ActionEvent e) {
            int count = measureTable.getRowCount() - 1;
            while (count >= 0) {
                deleteMeasure(count);
                count--;
            }
        }
    };

    /** Addition of a class one by one. */
    protected AbstractAction addMeasure = new AbstractAction("Add Measure") {

        private static final long serialVersionUID = 1020179998359644986L;

        {
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.ALT_MASK));
            putValue(Action.SHORT_DESCRIPTION, "Adds a new measure with selected performance index");
        }

        public void actionPerformed(ActionEvent e) {
            String actualClassID = (String) classSelection.getSelectedItem();
            String level = (String) levelSelection.getSelectedItem();
            addMeasure(actualClassID, level);
            SimulationSolverBasePanel.getInstance().appendToConsole(actualClassID + " - Measures added");
        }
    };

    protected AbstractAction updateClassNameTextField = new AbstractAction() {

        private static final long serialVersionUID = -3825967505831965186L;

        public void actionPerformed(ActionEvent e) {
            String classID = (String) ((JComboBox) e.getSource()).getSelectedItem();
            String className = " ";
            if (ALL_CLASSES.equals(classID)) {
                className = " ";
            } else {
                className = bcClassSection.getClassName(classID);
            }
            classNameField.setText(className);
        }
    };

    /**
	 * Constructor.
	 * @param classes - ClassDefinition
	 * @param stations - StationDefinition
	 * @param simParams - ISimulationDefinition
	 */
    public MeasurePanel(IClassSection classes, IStationSection stations, ISimulationDefinition simParams, PerMoToSolverGUI wizard) {
        this.wizard = wizard;
        stationsCombos = new ImagedComboBoxCellEditorFactory(stations);
        classesCombos = new ImagedComboBoxCellEditorFactory(classes);
        classesCombos.setAllowsNull(true);
        setData(classes, stations, simParams);
        initComponents();
    }

    /**
	 * Initiate the components of this instance.
	 */
    private void initComponents() {
        this.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.setLayout(new BorderLayout(5, 5));
        String[] classIdArray = new String[classIDs.size() + 1];
        classIdArray[0] = ALL_CLASSES;
        int i = 1;
        for (String classID : classIDs) {
            classIdArray[i] = classID;
            i++;
        }
        Border classBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Class Selection");
        JPanel classComboAndText = new JPanel(new BorderLayout());
        classSelection = new JComboBox(classIdArray);
        classSelection.addActionListener(this.updateClassNameTextField);
        classComboAndText.add(classSelection, BorderLayout.NORTH);
        classNameField = new JTextField();
        classNameField.setEditable(false);
        classComboAndText.add(classNameField, BorderLayout.CENTER);
        classComboAndText.setBorder(classBorder);
        Border levelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Level Selection");
        JPanel levelOfMeasure = new JPanel(new BorderLayout());
        String[] levels = new String[4];
        levels[0] = WORKFLOW;
        levels[1] = ITEM;
        levels[2] = SERVICE;
        levels[3] = DELAYITEM;
        levelSelection = new JComboBox(levels);
        levelOfMeasure.add(levelSelection, BorderLayout.NORTH);
        levelOfMeasure.setBorder(levelBorder);
        JPanel buttonPanel = new JPanel(new BorderLayout());
        this.addMeasureButton = new JButton(this.addMeasure);
        JButton removeAllMeasures = new JButton(deleteAllMeasures);
        buttonPanel.add(addMeasureButton, BorderLayout.CENTER);
        buttonPanel.add(removeAllMeasures, BorderLayout.EAST);
        Border measureBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Measure Selection");
        JPanel measureSelectionPanel = new JPanel(new BorderLayout());
        measureSelectionPanel.add(measureSelection, BorderLayout.NORTH);
        measureSelectionPanel.setBorder(measureBorder);
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        rightPanel.add(measureSelectionPanel, BorderLayout.EAST);
        rightPanel.add(classComboAndText, BorderLayout.CENTER);
        rightPanel.add(levelOfMeasure, BorderLayout.WEST);
        this.measureTable = new MeasureTable();
        JPanel headPanel = new JPanel(new BorderLayout(5, 5));
        headPanel.add(this.descrLabel, BorderLayout.CENTER);
        headPanel.add(rightPanel, BorderLayout.EAST);
        this.addMeasureButton.setMaximumSize(Constants.DIM_BUTTON_M);
        this.add(headPanel, BorderLayout.NORTH);
        this.warningPanel = new WarningScrollTable(this.measureTable, Constants.WARNING_CLASS_STATION);
        this.warningPanel.addCheckVector(this.bcClassSection.getClassIDs());
        this.warningPanel.addCheckVector(this.bcStationSection.getAllStationIDsNoSourceNoSinkNoDelay());
        this.add(this.warningPanel, BorderLayout.CENTER);
        if (this.wizard.getPredictionBusinessCase() != null) {
            this.addMeasureButton.setEnabled(false);
            removeAllMeasures.setEnabled(false);
            this.measureSelection.setEnabled(false);
            this.classSelection.setEnabled(false);
            this.levelSelection.setEnabled(false);
            this.measureTable.setEnabled(false);
        }
    }

    /**
	 * Updates data contained in this panel's components.
	 * @param classes - ClassDefinition
	 * @param stations - StationDefintion
	 * @param simParams - SimultionDefinition
	 */
    public void setData(IClassSection classes, IStationSection stations, ISimulationDefinition simParams) {
        this.bcClassSection = classes;
        this.bcStationSection = stations;
        this.simData = simParams;
        this.stationsCombos.setData(stations);
        this.classesCombos.setData(classes);
        classIDs = bcClassSection.getClassIDs();
        this.refreshComponents();
    }

    /**
	 * Called by the Wizard when the panel becomes active.
	 */
    public void gotFocus() {
        this.stationsCombos.clearCache();
        this.classesCombos.clearCache();
        this.refreshComponents();
    }

    /**
	 * Repaint this instance.
	 */
    public void repaint() {
        this.refreshComponents();
        super.repaint();
    }

    /**
	 * Refresh the components of this instance.
	 */
    private void refreshComponents() {
        if (this.measureTable != null) {
            this.measureTable.tableChanged(new TableModelEvent(this.measureTable.getModel()));
        }
        if (this.warningPanel != null) {
            this.warningPanel.clearCheckVectors();
            this.warningPanel.addCheckVector(bcClassSection.getClassIDs());
            this.warningPanel.addCheckVector(bcStationSection.getAllStationIDsNoSourceNoSinkNoDelay());
        }
    }

    /**
	 * Adds measures of a single class to the simulation model. Note: This
	 * method adds measures only for a single defined classID!
	 * @param classID the class to add the measures on
	 * @param level the level to add measures on
	 */
    public void addMeasuresOfSingleClass(String classID, String level, String measureType) {
        boolean classFound = false;
        PerMoToBusinessCase bc = PerMoToBusinessCase.getInstance();
        List<de.ibis.permoto.model.basic.scenario.Class> classList = bc.getAllClasses();
        for (de.ibis.permoto.model.basic.scenario.Class currClass : classList) {
            if (currClass.getClassID().equals(classID)) {
                classFound = true;
            }
        }
        if (!classFound) {
            logger.error("Cannot add a measure on class " + classID + ". The classID was not found in the current BusinessCase. ");
        } else {
            List<String> stationIdsOfClass = null;
            stationIdsOfClass = bcClassSection.getStationIdsOfClass(classID);
            if (level.equals(WORKFLOW)) {
                measureSelection.setSelectedIndex(5);
                measureSelection.setEditable(false);
                for (String currStationID : stationIdsOfClass) {
                    Station currStation = bcStationSection.getStationByID(currStationID);
                    if (currStation.getStationType().equals(StationTypeType.SINK)) {
                        this.simData.addMeasure(measureType, currStationID, classID);
                    }
                }
            } else if (level.equals(DELAYITEM)) {
                measureSelection.setEditable(true);
                for (String currStationID : stationIdsOfClass) {
                    Station currStation = bcStationSection.getStationByID(currStationID);
                    if (currStation.getStationType().equals(StationTypeType.DELAY)) {
                        this.simData.addMeasure(measureType, currStationID, classID);
                    }
                }
            } else if (level.equals(ITEM)) {
                measureSelection.setEditable(true);
                for (String currStationID : stationIdsOfClass) {
                    Station currStation = bcStationSection.getStationByID(currStationID);
                    if (currStation.getStationType().equals(StationTypeType.SERVER)) {
                        if (currStation.getStationName().contains(ITEM)) {
                            this.simData.addMeasure(measureType, currStationID, classID);
                        }
                    }
                }
            } else if (level.equals(SERVICE)) {
                measureSelection.setEditable(true);
                for (String currStationID : stationIdsOfClass) {
                    Station currStation = bcStationSection.getStationByID(currStationID);
                    if (currStation.getStationType().equals(StationTypeType.SERVER)) {
                        if (currStation.getStationName().contains(SERVICE)) {
                            this.simData.addMeasure(measureType, currStationID, classID);
                        }
                    }
                }
            }
        }
    }

    /**
	 * Adds measures to the simulation model.
	 * @param classString the GUI String for the class
	 * @param levelString the GUI String for the level
	 */
    public void addMeasure(String classString, String levelString) {
        String measureTypeString = (String) this.measureSelection.getSelectedItem();
        if (classString.equals(ALL_CLASSES)) {
            for (String currClassId : bcClassSection.getClassIDs()) {
                addMeasuresOfSingleClass(currClassId, levelString, measureTypeString);
            }
        } else {
            addMeasuresOfSingleClass(classString, levelString, measureTypeString);
        }
        this.measureTable.tableChanged(new TableModelEvent(this.measureTable.getModel()));
    }

    /**
	 * Deletes a measure from the simulation model.
	 */
    private void deleteMeasure(int index) {
        this.simData.removeMeasure(this.simData.getMeasureKeys().get(index));
        this.measureTable.tableChanged(new TableModelEvent(this.measureTable.getModel()));
    }

    /**
	 * Returns the name of this panel.
	 * @return String
	 */
    public String getName() {
        return "Performance Indices";
    }

    /**
	 * Called by the Wizard before when switching to another panel.
	 */
    public void lostFocus() {
        TableCellEditor editor = measureTable.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
    }

    /**
	 * Measure table.
	 */
    protected class MeasureTable extends JTable {

        private static final long serialVersionUID = 4027035732589182956L;

        /** Button for deleting measurement indices. */
        private JButton deleteButton = new JButton(deleteMeasure);

        /**
		 * Constructor.
		 */
        public MeasureTable() {
            setModel(new MeasureTableModel());
            setRowHeight(Constants.ROW_HEIGHT);
            sizeColumns();
            setDefaultEditor(Double.class, new ExactCellEditor());
            setDefaultRenderer(Object.class, new DisabledCellRenderer());
        }

        /**
		 * Sets the size of the columns.
		 */
        private void sizeColumns() {
            int[] columnWidths = ((MeasureTableModel) getModel()).columnWidths;
            for (int i = 0; i < columnWidths.length; i++) {
                int prefWidth = columnWidths[i];
                if (i == columnWidths.length - 1) {
                    getColumnModel().getColumn(i).setMaxWidth(getRowHeight());
                } else {
                    getColumnModel().getColumn(i).setPreferredWidth(prefWidth);
                }
            }
        }

        /**
		 * Returns the TableCellEditor of this table.
		 * @return TableCellEditor
		 */
        public TableCellEditor getCellEditor(int row, int column) {
            if (column == 5) {
                return new ButtonCellEditor(deleteButton);
            } else if (column == 2) {
                return stationsCombos.getEditor(bcStationSection.getAllStationIDsNoSourceNoSinkNoDelay());
            } else if (column == 1) {
                return classesCombos.getEditor(bcClassSection.getClassIDs());
            } else {
                return super.getCellEditor(row, column);
            }
        }

        /**
		 * Returns the TableCellRenderer of this table.
		 * @return TableCellRenderer
		 */
        public TableCellRenderer getCellRenderer(int row, int column) {
            if (column == 5) {
                return new ButtonCellEditor(deleteButton);
            } else if (column == 2 && !simData.isGlobalMeasure(simData.getMeasureKeys().get(row))) {
                return stationsCombos.getRenderer();
            } else if (column == 1) {
                return classesCombos.getRenderer();
            } else {
                return super.getCellRenderer(row, column);
            }
        }
    }

    /**
	 * Table model for the measurement table.
	 */
    protected class MeasureTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 597222930811535856L;

        /** Column names. */
        private String[] columnNames = new String[] { "Performance Index", "Class", "Station", "Confidence Interval (0-1)", "Max Relative Error (0-1)", "" };

        /** Class types of the columns. */
        @SuppressWarnings("unchecked")
        private java.lang.Class[] columnClasses = new java.lang.Class[] { String.class, String.class, String.class, Double.class, Double.class, Object.class };

        /** The width of the columns. */
        private int[] columnWidths = new int[] { 120, 80, 80, 80, 80, 20 };

        /**
		 * Returns the number of rows.
		 * @return int - the number of rows.
		 */
        public int getRowCount() {
            return simData.getMeasureKeys().size();
        }

        /**
		 * Returns the number of columns.
		 * @return int - the number of columns.
		 */
        public int getColumnCount() {
            return columnNames.length;
        }

        /**
		 * Returns the name of a column.
		 * @param columnIndex - int, the column index.
		 * @return String - the column name.
		 */
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        /**
		 * Returns the class of corresponding column.
		 * @param columnIndex - int, the column index.
		 * @return Class - the column class type.
		 */
        public java.lang.Class<?> getColumnClass(int columnIndex) {
            return columnClasses[columnIndex];
        }

        /**
		 * Checks if a given cell is editable.
		 * @param rowIndex - int
		 * @param columnIndex - int
		 * @return <code>true</code> if a cell is editable, <code>false</code>
		 *         otherwise.
		 */
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return false;
            }
            if (columnIndex == 2 && simData.isGlobalMeasure(simData.getMeasureKeys().get(rowIndex))) {
                return false;
            }
            return true;
        }

        /**
		 * Returns the value of a given postion.
		 * @param rowIndex - int
		 * @param columnIndex - int
		 * @return Object
		 */
        public Object getValueAt(int rowIndex, int columnIndex) {
            Long key = simData.getMeasureKeys().get(rowIndex);
            if (columnIndex == 0) {
                return simData.getMeasureType(key);
            } else if (columnIndex == 1) {
                return simData.getMeasureClass(key);
            } else if (columnIndex == 2) {
                return simData.getMeasureStation(key);
            } else if (columnIndex == 3) {
                return simData.getMeasureAlpha(key);
            } else if (columnIndex == 4) {
                return simData.getMeasurePrecision(key);
            }
            return null;
        }

        /**
		 * Sets a value of a given cell.
		 * @param aValue - Object, to be set.
		 * @param rowIndex - int
		 * @param columnIndex - int
		 */
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Long key = simData.getMeasureKeys().get(rowIndex);
            if (columnIndex == 0) {
                logger.info("Measure type: " + aValue.toString());
                simData.setMeasureType((String) aValue, key);
            } else if (columnIndex == 1) {
                simData.setMeasureClass((String) aValue, key);
            } else if (columnIndex == 2) {
                logger.info("Measure station: " + aValue.toString() + " set for measure " + key.toString());
                simData.setMeasureStation((String) aValue, key);
            } else if (columnIndex == 3) {
                try {
                    String doubleVal = (String) aValue;
                    simData.setMeasureAlpha(Double.valueOf(doubleVal), key);
                } catch (NumberFormatException e) {
                    logger.error("Bad number format of alpha, " + "while setting the measurements." + e.getMessage());
                }
            } else if (columnIndex == 4) {
                try {
                    String doubleVal = (String) aValue;
                    simData.setMeasurePrecision(Double.valueOf(doubleVal), key);
                } catch (NumberFormatException e) {
                    logger.error("Bad number format of precision, " + "while setting the measurements." + e.getMessage());
                }
            }
        }
    }
}

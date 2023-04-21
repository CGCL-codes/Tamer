package vademecum.preparation.nonLinearTransformer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import vademecum.data.IDataGrid;
import vademecum.data.IColumn;
import vademecum.math.statistics.Univariate;

/**
 * The TableModel for the main table in {@link LinearTransformer}
 * @author Torben Rühl
 *
 */
public class NonLinearTransformerTableModel extends AbstractTableModel implements ActionListener {

    /**
	 * Logger instance
	 */
    private static Log log = LogFactory.getLog(NonLinearTransformerTableModel.class);

    private IDataGrid grid;

    private Vector<String> tableColumnNames;

    private Vector<NonLinearTransformerTableModelColumnData> tableColumns;

    private Vector<Class<INonLinearTransformModel>> availableTransformations;

    private Properties nonLinearTransformerProperties;

    private NonLinearTransformer nonLinTrans;

    private NumberFormat nf;

    protected static final String APPLY_PREFERENCE = "apply_preference";

    protected static final String APPLY_ALLSET_PREFERENCE = "apply_all_setpreference";

    /**
	 * The initialisation of the table
	 * @param grid
	 * @param availableTransformations
	 */
    public NonLinearTransformerTableModel(IDataGrid grid, Vector<Class<INonLinearTransformModel>> availableTransformations, Properties props, NonLinearTransformer nonLinTrans) {
        nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(3);
        this.grid = grid;
        this.nonLinTrans = nonLinTrans;
        this.availableTransformations = availableTransformations;
        String columnName;
        double meanOld, minOld, maxOld, stdDevOld, meanNew, minNew, maxNew, stdDevNew, skewnessOld, skewnessNew;
        initTableColumnNames();
        nonLinearTransformerProperties = props;
        if (props.getProperty(NonLinearTransformer.TRANSFORM_PROPERTY_NAME) == null) {
            String transformString = new String();
            for (IColumn column : grid.getColumns()) {
                if (column.getType() == Double.class) {
                    transformString += "," + availableTransformations.get(0).getSimpleName();
                }
            }
            transformString = transformString.substring(1);
            props.setProperty(NonLinearTransformer.TRANSFORM_PROPERTY_NAME, transformString);
            log.debug(props.getProperty(NonLinearTransformer.TRANSFORM_PROPERTY_NAME));
        }
        tableColumns = new Vector<NonLinearTransformerTableModelColumnData>();
        StringTokenizer tokenizer = new StringTokenizer(props.getProperty(NonLinearTransformer.TRANSFORM_PROPERTY_NAME), ",");
        int index = 0;
        for (IColumn column : grid.getColumns()) {
            if (column.getType() == Double.class) {
                columnName = column.getLabel();
                Vector<Object> inputVector = new Vector<Object>(grid.getNumRows());
                for (int row = 0; row < grid.getNumRows(); row++) {
                    inputVector.add(grid.getPoint(row, index));
                }
                meanOld = Univariate.getMean(inputVector);
                minOld = Univariate.getMin(inputVector);
                maxOld = Univariate.getMax(inputVector);
                stdDevOld = Math.sqrt(Univariate.getVariance(inputVector));
                skewnessOld = Univariate.getRelativeSkewness(inputVector);
                String currentTransformation = tokenizer.nextToken();
                Class<INonLinearTransformModel> currentTransformationModel = null;
                for (Class<INonLinearTransformModel> model : availableTransformations) {
                    if (model.getSimpleName().compareTo(currentTransformation) == 0) {
                        currentTransformationModel = model;
                        log.debug("Setting row " + index + " to " + model.getSimpleName());
                    }
                }
                INonLinearTransformModel instance = null;
                try {
                    instance = currentTransformationModel.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                instance.setDataRow(inputVector);
                String properties;
                if ((properties = (String) this.nonLinearTransformerProperties.get("preference_" + index)) != null) {
                    StringTokenizer token = new StringTokenizer(properties, ";");
                    if (token.nextToken().equals(currentTransformationModel.getSimpleName())) {
                        Vector<NonLinearTransformerPreference> savedPreferences = new Vector<NonLinearTransformerPreference>();
                        while (token.hasMoreTokens()) {
                            StringTokenizer preferenceToken = new StringTokenizer(token.nextToken(), "?");
                            String key = preferenceToken.nextToken();
                            Double value = Double.valueOf(preferenceToken.nextToken());
                            Boolean readOnly = Boolean.parseBoolean(preferenceToken.nextToken());
                            savedPreferences.add(new NonLinearTransformerPreference(key, value, readOnly));
                        }
                        instance.setPreferences(savedPreferences);
                    } else {
                        instance.setDefaultPreferences();
                    }
                } else {
                    instance.setDefaultPreferences();
                }
                meanNew = instance.newMean(inputVector);
                minNew = instance.newMin(inputVector);
                maxNew = instance.newMax(inputVector);
                stdDevNew = instance.newStdDev(inputVector);
                skewnessNew = instance.newSkewness(inputVector);
                tableColumns.add(new NonLinearTransformerTableModelColumnData(columnName, index, meanOld, minOld, maxOld, stdDevOld, meanNew, minNew, maxNew, stdDevNew, skewnessOld, skewnessNew, instance));
            }
            index++;
        }
    }

    private void initTableColumnNames() {
        this.tableColumnNames = new Vector<String>();
        this.tableColumnNames.add("nr");
        this.tableColumnNames.add("name");
        this.tableColumnNames.add("transformation");
        this.tableColumnNames.add("preference");
        this.tableColumnNames.add("old mean");
        this.tableColumnNames.add("old min");
        this.tableColumnNames.add("old max");
        this.tableColumnNames.add("old stddev");
        this.tableColumnNames.add("old skewness");
        this.tableColumnNames.add("new mean");
        this.tableColumnNames.add("new min");
        this.tableColumnNames.add("new max");
        this.tableColumnNames.add("new stddev");
        this.tableColumnNames.add("new skewness");
    }

    @Override
    public int getColumnCount() {
        return this.tableColumnNames.size();
    }

    @Override
    public int getRowCount() {
        return tableColumns.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return tableColumnNames.get(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return tableColumns.get(rowIndex).getGridColumn();
            case 1:
                return tableColumns.get(rowIndex).getColumnName();
            case 2:
                return tableColumns.get(rowIndex).getCurrentTransformation();
            case 3:
                return tableColumns.get(rowIndex).getCurrentTransformation();
            case 4:
                return nf.format(tableColumns.get(rowIndex).getMeanOld());
            case 5:
                return nf.format(tableColumns.get(rowIndex).getMinOld());
            case 6:
                return nf.format(tableColumns.get(rowIndex).getMaxOld());
            case 7:
                return nf.format(tableColumns.get(rowIndex).getStdDevOld());
            case 8:
                return nf.format(tableColumns.get(rowIndex).getSkewnessOld());
            case 9:
                return nf.format(tableColumns.get(rowIndex).getMeanNew());
            case 10:
                return nf.format(tableColumns.get(rowIndex).getMinNew());
            case 11:
                return nf.format(tableColumns.get(rowIndex).getMaxNew());
            case 12:
                return nf.format(tableColumns.get(rowIndex).getStdDevNew());
            case 13:
                return nf.format(tableColumns.get(rowIndex).getSkewnessNew());
            default:
                return new String("no value assigned");
        }
    }

    public boolean isCellEditable(int row, int col) {
        return col == 2 || col == 3;
    }

    public void setValueAt(Object value, int row, int col) {
        if (col == 2) {
            Class<INonLinearTransformModel> currentModel = (Class<INonLinearTransformModel>) value;
            INonLinearTransformModel instance = null;
            try {
                instance = currentModel.newInstance();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            Vector<Object> inputVector = new Vector<Object>(this.grid.getNumRows());
            for (int i = 0; i < grid.getNumRows(); i++) {
                inputVector.add(grid.getPoint(i, this.tableColumns.get(row).getGridColumn()));
            }
            instance.setDataRow(inputVector);
            String properties;
            if ((properties = (String) this.nonLinearTransformerProperties.get("preference_" + row)) != null) {
                StringTokenizer token = new StringTokenizer(properties, ";");
                if (token.nextToken().equals(currentModel.getSimpleName())) {
                    Vector<NonLinearTransformerPreference> savedPreferences = new Vector<NonLinearTransformerPreference>();
                    while (token.hasMoreTokens()) {
                        StringTokenizer preferenceToken = new StringTokenizer(token.nextToken(), "?");
                        String key = preferenceToken.nextToken();
                        Double temp = Double.valueOf(preferenceToken.nextToken());
                        Boolean readOnly = Boolean.parseBoolean(preferenceToken.nextToken());
                        savedPreferences.add(new NonLinearTransformerPreference(key, temp, readOnly));
                    }
                    instance.setPreferences(savedPreferences);
                } else {
                    instance.setDefaultPreferences();
                }
            } else {
                instance.setDefaultPreferences();
            }
            this.tableColumns.get(row).setCurrentTransformation(instance);
        }
        if (col == 3) {
            INonLinearTransformModel currentTransformation = (INonLinearTransformModel) value;
            String preferenceStringKey = "preference_" + row + "";
            String preferenceString = this.tableColumns.get(row).getCurrentTransformation().getClass().getSimpleName() + ";";
            Vector<NonLinearTransformerPreference> preferences = currentTransformation.getPreferences();
            for (int i = 0; i < preferences.size(); i++) {
                preferenceString += preferences.get(i).key + "?" + preferences.get(i).value + "?" + preferences.get(i).readOnly + ";";
            }
            this.nonLinearTransformerProperties.remove((String) preferenceStringKey);
            this.nonLinearTransformerProperties.setProperty(preferenceStringKey, preferenceString);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        log.debug("Action in LinearTransformerTableModel: " + e.getActionCommand());
        if (e.getActionCommand() == NonLinearTransformer.ACTION_APPLY_BUTTON) {
            log.debug("Apply button pressed");
            String transformInfo = new String();
            for (NonLinearTransformerTableModelColumnData data : this.tableColumns) transformInfo += "," + data.getCurrentTransformation().getClass().getSimpleName();
            nonLinearTransformerProperties.setProperty(NonLinearTransformer.TRANSFORM_PROPERTY_NAME, transformInfo.substring(1));
            log.debug("Set transformation properties to: " + nonLinearTransformerProperties.getProperty(NonLinearTransformer.TRANSFORM_PROPERTY_NAME));
            this.nonLinTrans.actionPerformed(e);
        }
        if (e.getActionCommand() == NonLinearTransformer.ACTION_RESET_BUTTON) {
            log.debug("Reset button pressed");
            StringTokenizer tokenizer = new StringTokenizer(nonLinearTransformerProperties.getProperty(NonLinearTransformer.TRANSFORM_PROPERTY_NAME), ",");
            log.debug("Resetting to " + nonLinearTransformerProperties.getProperty(NonLinearTransformer.TRANSFORM_PROPERTY_NAME));
            int index = 0;
            INonLinearTransformModel currentModel = null;
            while (tokenizer.hasMoreTokens()) {
                String currentTransformation = tokenizer.nextToken();
                log.debug("looking for " + currentTransformation);
                for (Class<INonLinearTransformModel> model : this.availableTransformations) {
                    if (model.getSimpleName().compareTo(currentTransformation) == 0) {
                        try {
                            currentModel = model.newInstance();
                        } catch (InstantiationException e1) {
                            e1.printStackTrace();
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                        currentModel.setDefaultPreferences();
                        this.tableColumns.get(index).setCurrentTransformation(currentModel);
                        log.debug("Setting row " + index + " to " + model.getSimpleName());
                    }
                }
                index++;
            }
            this.recalcTableData();
            this.fireTableDataChanged();
        }
        if (e.getActionCommand() == NonLinearTransformer.ACTION_DEFAULT_BUTTON) {
            log.debug("Default button pressed");
            INonLinearTransformModel currentModel = null;
            for (int i = 0; i < this.tableColumns.size(); i++) {
                try {
                    currentModel = this.availableTransformations.firstElement().newInstance();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
                currentModel.setDefaultPreferences();
                this.tableColumns.get(i).setCurrentTransformation(currentModel);
            }
            this.recalcTableData();
            this.fireTableDataChanged();
        }
        if (e.getActionCommand() == NonLinearTransformer.ACTION_COMBOBOX) {
            log.debug("ComboBox Changes");
            this.recalcTableData();
            this.fireTableDataChanged();
        }
        if (e.getActionCommand() == NonLinearTransformer.ACTION_ALLSET_BUTTON) {
            log.debug("Acton: Set Button for set All");
            Class<INonLinearTransformModel> model = (Class<INonLinearTransformModel>) this.nonLinTrans.getAllSetComboBox().getSelectedItem();
            Vector<NonLinearTransformerPreference> savedPreferences = null;
            String properties;
            if ((properties = (String) this.nonLinearTransformerProperties.get("preference_all")) != null) {
                StringTokenizer token = new StringTokenizer(properties, ";");
                if (token.nextToken().equals(model.getSimpleName())) {
                    savedPreferences = new Vector<NonLinearTransformerPreference>();
                    while (token.hasMoreTokens()) {
                        StringTokenizer preferenceToken = new StringTokenizer(token.nextToken(), "?");
                        String key = preferenceToken.nextToken();
                        Double value = Double.valueOf(preferenceToken.nextToken());
                        Boolean readOnly = Boolean.parseBoolean(preferenceToken.nextToken());
                        savedPreferences.add(new NonLinearTransformerPreference(key, value, readOnly));
                    }
                }
            }
            INonLinearTransformModel newModel = null;
            for (NonLinearTransformerTableModelColumnData data : this.tableColumns) {
                Vector<NonLinearTransformerPreference> tempPreference = new Vector<NonLinearTransformerPreference>();
                for (int i = 0; i < savedPreferences.size(); i++) {
                    tempPreference.add(new NonLinearTransformerPreference(savedPreferences.get(0).key, savedPreferences.get(0).value, savedPreferences.get(0).readOnly));
                }
                try {
                    newModel = model.newInstance();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
                newModel.setDataRow(data.getCurrentTransformation().getDataRow());
                if (savedPreferences != null) {
                    newModel.setPreferences(tempPreference);
                    newModel.initPreferenceData();
                    newModel.updatePreferences(null);
                } else {
                    newModel.setDefaultPreferences();
                }
                Vector<NonLinearTransformerPreference> preferences = newModel.getPreferences();
                data.setCurrentTransformation(newModel);
                String preferenceStringKey = "preference_" + data.getGridColumn() + "";
                String preferenceString = model.getSimpleName() + ";";
                for (int i = 0; i < preferences.size(); i++) {
                    preferenceString += preferences.get(i).key + "?" + preferences.get(i).value + "?" + preferences.get(i).readOnly + ";";
                }
                this.nonLinearTransformerProperties.remove((String) preferenceStringKey);
                this.nonLinearTransformerProperties.setProperty(preferenceStringKey, preferenceString);
            }
            this.recalcTableData();
            this.fireTableDataChanged();
        }
        if (e.getActionCommand() == NonLinearTransformerTableModel.APPLY_PREFERENCE) {
            log.debug("Action: Apply in the Preference Dialog");
            this.recalcTableData();
            this.fireTableDataChanged();
        }
        if (e.getActionCommand() == NonLinearTransformer.ACTION_ALLSET_PREFERENCE_BUTTON) {
            log.debug("Action: Open the Preference Dialog in allSet Mode");
            Class<INonLinearTransformModel> model = (Class<INonLinearTransformModel>) this.nonLinTrans.getAllSetComboBox().getSelectedItem();
            INonLinearTransformModel currentModel = null;
            try {
                currentModel = model.newInstance();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            currentModel.setDefaultPreferences(true);
            PreferenceDialog preferenceDialog = new PreferenceDialog(currentModel, this, true);
            preferenceDialog.pack();
            preferenceDialog.setVisible(true);
        }
        if (e.getActionCommand() == NonLinearTransformerTableModel.APPLY_ALLSET_PREFERENCE) {
            log.debug("Preference Dialog Apply for allSet");
            PreferenceDialog preferenceDialog = (PreferenceDialog) e.getSource();
            Vector<NonLinearTransformerPreference> preferences = preferenceDialog.preferences_new;
            preferenceDialog.setVisible(false);
            String preferenceStringKey = "preference_all";
            String preferenceString = preferenceDialog.model.getClass().getSimpleName() + ";";
            for (int i = 0; i < preferences.size(); i++) {
                preferenceString += preferences.get(i).key + "?" + preferences.get(i).value + "?" + preferences.get(i).readOnly + ";";
            }
            log.debug("Key: " + preferenceStringKey + " Preference String: " + preferenceString);
            this.nonLinearTransformerProperties.remove((String) preferenceStringKey);
            this.nonLinearTransformerProperties.setProperty(preferenceStringKey, preferenceString);
        }
    }

    private void recalcTableData() {
        log.debug("Recalculating the table data...");
        INonLinearTransformModel instance = null;
        for (int index = 0; index < this.tableColumns.size(); index++) {
            instance = this.tableColumns.get(index).getCurrentTransformation();
            log.debug("Current model Colum " + index + ": " + instance.toString());
            Vector<Object> inputVector = new Vector<Object>(this.grid.getNumRows());
            for (int j = 0; j < this.grid.getNumRows(); j++) inputVector.add(grid.getPoint(j, this.tableColumns.get(index).getGridColumn()));
            this.tableColumns.get(index).setMeanNew(instance.newMean(inputVector));
            this.tableColumns.get(index).setMinNew(instance.newMin(inputVector));
            this.tableColumns.get(index).setMaxNew(instance.newMax(inputVector));
            this.tableColumns.get(index).setStdDevNew(instance.newStdDev(inputVector));
            this.tableColumns.get(index).setSkewnessNew(instance.newSkewness(inputVector));
        }
    }

    /**
	 * @return the tableColumns
	 */
    public Vector<NonLinearTransformerTableModelColumnData> getTableColumns() {
        return tableColumns;
    }
}

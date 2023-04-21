package KFramework30.Widgets;

import KFramework30.Communication.dbTransactionClientClass;
import KFramework30.Base.*;
import KFramework30.Base.KConfigurationClass;
import KFramework30.Base.KExceptionClass;
import KFramework30.Base.KLogClass;
import KFramework30.Base.KMetaUtilsClass;
import java.util.*;
import javax.swing.*;

public class KDropDownFillerClass extends java.lang.Object implements KCustomWidgetIntegrationIntrerface {

    private static final int MAX_DATA_WIDTH_OF_COMBOBOX = 60;

    private KConfigurationClass configuration;

    private KLogClass log;

    public JComboBox dropDownBox;

    private dbTransactionClientClass dbTransactionClient;

    private String SQL;

    private String keyField;

    private List keyValueList;

    private int maxDataSize;

    private String name;

    private List wholeTable;

    private boolean enabled = true;

    /** Creates new dropDownFillerClass */
    public KDropDownFillerClass(KConfigurationClass configurationParam, KLogClass logParam, String SQLstring, String keyFieldParam, JComboBox dropDownParam, String nameParam) throws KExceptionClass {
        super();
        configuration = configurationParam;
        log = logParam;
        dropDownBox = dropDownParam;
        maxDataSize = MAX_DATA_WIDTH_OF_COMBOBOX;
        SQL = SQLstring;
        keyField = keyFieldParam;
        keyValueList = new ArrayList();
        name = nameParam;
        wholeTable = new ArrayList();
        dbTransactionClient = new dbTransactionClientClass(configuration, log);
        dbTransactionClient.prepare(SQL);
        log.log(this, "Constructed successfully.");
    }

    /** Creates new dropDownFillerClass */
    public KDropDownFillerClass(KConfigurationClass configurationParam, KLogClass logParam, String SQLstring, String keyFieldParam, JComboBox dropDownParam) throws KExceptionClass {
        this(configurationParam, logParam, SQLstring, keyFieldParam, dropDownParam, "");
    }

    @Override
    public void disable() {
        dropDownBox.setEnabled(false);
        enabled = false;
    }

    @Override
    public void enable() {
        dropDownBox.setEnabled(true);
        enabled = true;
    }

    public void addItem(String key, String value) {
        keyValueList.add(key);
        dropDownBox.addItem(value);
    }

    public void removeItemByID(String keyParam) throws KExceptionClass {
        Iterator IDs = keyValueList.iterator();
        int index = 0;
        while (IDs.hasNext()) {
            if (((String) IDs.next()).equals(keyParam)) {
                keyValueList.remove(keyParam);
                dropDownBox.remove(index);
                return;
            }
            index++;
        }
        throw new KExceptionClass("Could not remove id from list " + name + " value " + keyParam + " is not in the list. ", null);
    }

    /**
            NAME
            Filler name used by serializableObject interface
    */
    @Override
    public String getName() {
        return (name);
    }

    /**
            BINDING
            Parameters can be reassigned at any moment 
            and may be reset only partially				
    */
    public void bind(String parameterName, Object parameterValue) throws KExceptionClass {
        dbTransactionClient.dynamicBind(parameterName, parameterValue);
    }

    /**   Set maximum data width in combo box */
    public void setMaxDataWidth(int dataWidth) {
        maxDataSize = dataWidth;
    }

    /**   Execute the query and get the results.  */
    public void load() throws KExceptionClass {
        log.log(this, "Loading combo " + name);
        dropDownBox.removeAllItems();
        dbTransactionClient.executeQuery(0, dbTransactionClientClass.DEFAULT_MAXROWS);
        if (!keyField.isEmpty() && !dbTransactionClient.columnNamesList.contains(keyField)) {
            throw new KExceptionClass("*** SQL Query error **** \n" + "Key field, [" + keyField + "] not found in db Query! for drop " + name, null);
        }
        boolean noData = true;
        while (dbTransactionClient.fetch()) {
            noData = false;
            Iterator columnNames = dbTransactionClient.columnNamesList.iterator();
            String rowLabel = "";
            Vector record = new Vector<String>();
            String logMessage = "";
            int columnIndex = 0;
            while (columnNames.hasNext()) {
                String column_name = (String) columnNames.next();
                if (!keyField.isEmpty() && column_name.equals(keyField)) {
                    keyValueList.add(dbTransactionClient.getProperty(column_name));
                }
                if (columnIndex == 1) {
                    rowLabel += dbTransactionClient.getProperty(column_name);
                }
                record.add(dbTransactionClient.getProperty(column_name));
                logMessage += dbTransactionClient.getProperty(column_name) + "|";
                columnIndex++;
            }
            if (rowLabel.length() > maxDataSize) rowLabel = rowLabel.substring(0, maxDataSize);
            dropDownBox.addItem(rowLabel);
            wholeTable.add(record);
            log.log(this, "Loading combo {" + logMessage + "}");
        }
        if (noData) throw new KExceptionClass("***Could not setup combobox ***\n" + "No data for [" + name + "]  ", null);
        if (!enabled) {
            dropDownBox.setEnabled(false);
        } else {
            dropDownBox.setEnabled(true);
        }
    }

    /**
     * Calling function - public List getAllRequested(), 
     * will get list of rows as Vector containing entire SQL request result set. 
     * The list index lines up with combo box index. 
     * The Vector index lines up with SELECT columns order.
    */
    public List getAllItems() {
        return wholeTable;
    }

    /**   Return the key field value of current selected item.  */
    public long SelectedItem() throws KExceptionClass {
        int index = dropDownBox.getSelectedIndex();
        if (index == -1) {
            throw new KExceptionClass("*** Combo Box error **** \n" + "No valid selected item in the model list!", null);
        }
        return KMetaUtilsClass.getIntegralNumericValueFromString((String) keyValueList.get(index));
    }

    /**   Return the key field value of current selected item.  */
    public String SelectedItemLabel() throws KExceptionClass {
        return ((String) dropDownBox.getSelectedItem());
    }

    /**   Set selected item via key value.  */
    public void setSelectedItem(long id) throws KExceptionClass {
        if (keyValueList.size() == 0) {
            throw new KExceptionClass("*** Combo Box error **** \n" + "Can not set selected value for drop down " + name + "\n" + "Drop down list is empty.", null);
        }
        String key = KMetaUtilsClass.toDecimalString(id);
        Iterator keyValues = keyValueList.iterator();
        int index = 0;
        while (keyValues.hasNext()) {
            String nextKey = (String) keyValues.next();
            if (key.equals(nextKey)) break;
            index++;
        }
        if (index >= keyValueList.size()) {
            throw new KExceptionClass("*** Combo Box error **** \n" + "Invalid [" + key + "] value for drop down " + name, null);
        }
        dropDownBox.setSelectedIndex(index);
    }

    @Override
    public void setValue(String newValue) throws KExceptionClass {
        setSelectedItem(KMetaUtilsClass.getIntegralNumericValueFromString(newValue));
    }

    @Override
    public String getValue() throws KExceptionClass {
        return "" + SelectedItem();
    }
}

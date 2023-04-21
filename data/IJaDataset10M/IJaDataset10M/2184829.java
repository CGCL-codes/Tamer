package net.mystrobe.client.dynamic.config;

import java.util.List;
import java.util.Map;
import net.mystrobe.client.IDataBean;

/**
 * Dynamic form/table configuration interface.<br/>
 * 
 * Contains all required information to dynamically
 *  display data object form / tables. <br/>
 *  
 * Default configurations can be changed and make fields not visible,
 *  required/not required, change display label, form input type etc.      
 * 
 * 
 * @author TVH Group NV
 */
public interface IDynamicFormConfig<T extends IDataBean> {

    public static final String RESOURCE_KEY_DEFAULT_PREFIX = "??";

    public static final String RESOURCE_KEY_DEFAULT_SUFFIX = "??";

    /**
	 * Retrieve map of properties for a a given column(<tt>columnName</tt>).
	 * 
	 * @param columnName Column for which to retrieve configuration.   
	 * @return Map of configuration properties values.  
	 */
    public Map<IDynamicFormFieldConfig.Property, Object> getColumnProperties(String columnName);

    /**
	 * Set a configuration property for a certain column.
	 * 
	 * @param columnName Column which configuration will be set/changed.
	 * @param property Configuration property to be set/changed.
	 * @param value Value of the configuration property.
	 */
    public void setColumnProperty(String columnName, IDynamicFormFieldConfig.Property property, Object value);

    /**
	 * 
	 * Set a configuration property for a certain column identified by <tt>columnId</tt>. 
	 * 
	 * @param columnId Column Id which configuration will be set/changed.
	 * @param property Configuration property to be set/changed.
	 * @param value Value of the configuration property.
	 */
    public void setColumnProperty(Object columnId, IDynamicFormFieldConfig.Property property, Object value);

    /**
	 * Configuration property used when displaying forms.</br> 
	 * 
	 * Value should be sent according to ones needs, when set to 
	 *  2 then 2 form input fields will be displayed on a row and so on.   
	 * 
	 * @return Count of form input fields to be displayed on a single row.  
	 */
    public int getNumberOfFieldsOnRow();

    /**
	 * Set number of input fields displayed in a row. 
	 * 
	 * @param columns Form fields in a single display row.
	 */
    public void setNumberOfFieldsOnRow(int columns);

    /**
	 * For input fields are to be viewed in a certain order.
	 * 
	 * @return True if form input fields are sorted for display.
	 */
    public boolean hasSortableInputFields();

    /**
	 * Get list of visible columns.<br/>
	 * 
	 * Used by both form/table display components.
	 * 
	 * @return List of data object visible columns.
	 */
    public List<String> getVisibleColumnNames();

    /**
	 * Flag used by the UI components when displaying column labels.<br/>
	 * 
	 * When true system looks up for i18n resources with key matching
	 *  the column label otherwise label is displayed as it is set by the {@link IDynamicFormFieldConfig.Property.Label} property.
	 *  
	 * @return True when column labels use localization keys for display.
	 */
    public boolean getLocalizableFormLabels();

    /**
	 * Change data object display columns sortable property
	 * 
	 * @param sortable Sort columns for display flag.
	 */
    public void setSortable(boolean sortable);
}

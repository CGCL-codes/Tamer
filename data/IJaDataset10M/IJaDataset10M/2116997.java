package javango.forms;

import java.util.List;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;
import javango.forms.fields.BoundField;
import javango.forms.fields.Field;

public interface Form extends Iterable<BoundField> {

    /**
	 * Returns true if this is a bound form,  bound forms have been associated with input from a user.
	 * @return
	 */
    boolean isBound();

    /**
	 * Bind the form to the input values from the user.
	 * @param m
	 */
    Form bind(Map<String, String[]> m);

    /**
	 * Bind the form to the input values from the user.
	 * @param m
	 */
    Form bind(Map<String, String[]> m, Map<String, FileItem> fileMap);

    /**
	 * Return true if this form is valid.
	 * @return
	 */
    boolean isValid();

    /**
	 * Returns a Map of field errors
	 * TODO DDN Should each field allow multiple errors?  ie return Map<String, Collection>
	 * @return
	 */
    Map<String, String> getErrors();

    /**
	 * Cleans the data in this form into a new bean of the specified class.
	 * TODO DDN - Should initial contain all available properties in the objectClass,  or just those that coorespond to fields in the form. 
	 * @param objectClass
	 * @return
	 */
    <T> T clean(Class<T> objectClass);

    /**
	 * Cleans the data in this form into the provided bean.
	 * @param bean
	 * @return
	 */
    <T> T clean(T bean);

    /**
	 * Returns the map of cleaned data for this form,  the values of the map depend on the types of fields in the form.
	 * ie an IntegerField will place Integers in the map while a BooleanField will place Boolean.TRUE/FALSE
	 * 
	 * isValid must be called prior to calling getCleanedData(),  prior to isValid being called cleandedData will return null,  you have been warned.
	 * if isValid returns false this methods return value is undefined and may be null or may contain partially validated data
	 *  
	 * @return
	 */
    Map<String, Object> getCleanedData();

    /**
	 * Returns the cleaned value for the given field,  same as
	 * getCleanedData().get(field.getName());
	 * 
	 * The same rules apply in regards to calling isValid as exist for getCleandData()
	 * @param field
	 * @return
	 */
    <T> T getCleanedValue(Field<T> field);

    /**
	 * Returns the map of input data
	 * @return
	 */
    Map<String, String[]> getData();

    /**
	 * Returns the map of fields
	 * 
	 * @return
	 */
    Map<String, Field<?>> getFields();

    /**
	 * Set the forms initial data based on values from the Map.
	 * 
	 * @param initial
	 * @return
	 */
    Form setInitial(Map<String, Object> initial);

    /**
	 * Set the forms initial data using getters/setters from the provided bean. 
	 * @param initial
	 * @return
	 */
    Form setInitial(Object initial);

    Map<String, Object> getInitial();

    /**
	 * Sets the form's prefix
	 * 
	 * @param prefix
	 * @return
	 */
    Form setPrefix(String prefix);

    String getPrefix();

    /**
	 * Takes either a format string (id_%s) or null to disable ids.
	 * 
	 * @param id
	 * @return
	 */
    Form setId(String id);

    String getId();

    /**
	 * Returns a bound field for the requested field.
	 * 
	 * @param field
	 * @return
	 */
    BoundField get(String field);

    /**
	 * Returns a list of error messages that are not associated with any single field.
	 * 
	 * @return
	 */
    List<String> getNonFieldErrors();

    /**
	 * Sets the entire form's readonly status,  if read only is true fields should be displayed using their readonly widget.
	 * @param readOnly
	 * @return
	 */
    Form setReadOnly(boolean readOnly);

    boolean isReadOnly();

    /**
	 * @param fieldName
	 */
    String addPrefix(String fieldName);

    /**
	 * Returns this form represented as an html table.
	 * 
	 * @return
	 */
    String asTable();

    /**
	 * Returns a string representation of this form's hidden fields
	 * @return
	 */
    String getHiddenFieldsHtml();

    /**
	 * css class that should be added to a field's container (ie row) if the field has an error,  null for none
	 * @return
	 */
    String getErrorCssClass();

    /**
	 * css class that should be added to a field's container (ie row) if the field is required,  null for none
	 * @return
	 */
    String getRequiredCssClass();
}

package org.jkoha.library.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * @jdo.persistence-capable
 * @sql.table
 *      table-name="SystemPreference"
 * @author T. Kia Ntoni
 * 
 * 10 oct. 2005 
 * SystemPreference @version
 */
public class SystemPreference {

    public static String OPTION_DELIMITER = "|";

    /**
     * @jdo.field
     *      primary-key="true"
     * @sql.field
     *      column-name="variable"
     */
    private String variable;

    /**
     * @jdo.field
     *      primary-key="false"
     * @sql.field
     *      column-name="value"
     */
    private String value;

    /**
     * @jdo.field
     *      primary-key="false"
     * @sql.field
     *      column-name="options"
     */
    private String options;

    /**
     * @jdo.field
     *      primary-key="false"
     * @sql.field
     *      column-name="explanation"
     */
    private String explanation;

    /**
     * @jdo.field
     *      primary-key="fase"
     * @sql.field
     *      column-name="type"
     */
    private String type;

    /**
     * 
     */
    public SystemPreference() {
    }

    /**
     * @return Returns the explanation.
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * @param explanation The explanation to set.
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    /**
     * @return Returns the options.
     */
    public String getOptions() {
        return options;
    }

    /**
     * 
     * @return
     */
    public Collection<String> getOptionsItems() {
        ArrayList<String> result = new ArrayList<String>();
        if (StringUtils.isNotBlank(options)) {
            StringTokenizer tokenizer = new StringTokenizer(options, OPTION_DELIMITER);
            while (tokenizer.hasMoreTokens()) {
                result.add(tokenizer.nextToken());
            }
        }
        return result;
    }

    public void setOptionsItems(Collection<String> items) {
        options = "";
        for (String string : items) {
            options += string;
            options += OPTION_DELIMITER;
        }
        options = StringUtils.isNotBlank(options) && options.endsWith(OPTION_DELIMITER) ? options.substring(0, options.length() - 1) : options;
    }

    /**
     * 
     * @param item
     */
    public boolean addOptionsItem(String item) {
        Collection<String> currentItems = getOptionsItems();
        if (StringUtils.isNotBlank(item) && !currentItems.contains(item)) {
            currentItems.add(item);
            setOptionsItems(currentItems);
            return true;
        }
        return false;
    }

    public boolean addOptionsItems(Collection<String> items) {
        boolean result = false;
        Collection<String> currentItems = getOptionsItems();
        for (String item : items) {
            if (StringUtils.isNotBlank(item) && !currentItems.contains(item)) {
                currentItems.add(item);
                result = true;
            }
        }
        if (result) {
            setOptionsItems(currentItems);
        }
        return result;
    }

    public boolean removeOptionsItem(String item) {
        Collection<String> currentItems = getOptionsItems();
        if (currentItems.remove(item)) {
            setOptionsItems(currentItems);
            return true;
        }
        return false;
    }

    public boolean removeOptionsItems(Collection<String> items) {
        Collection<String> currentItems = getOptionsItems();
        if (currentItems.removeAll(items)) {
            setOptionsItems(currentItems);
            return true;
        }
        return false;
    }

    /**
     * @param options The options to set.
     */
    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return Returns the variable.
     */
    public String getVariable() {
        return variable;
    }

    /**
     * @param variable The variable to set.
     */
    public void setVariable(String variable) {
        this.variable = variable;
    }
}

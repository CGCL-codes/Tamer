package net.sourceforge.processdash.data.util;

import net.sourceforge.processdash.data.DataContext;
import net.sourceforge.processdash.data.SaveableData;
import net.sourceforge.processdash.data.SimpleData;
import net.sourceforge.processdash.data.repository.DataRepository;

public class InheritedValue {

    protected String prefix;

    protected String dataName;

    protected SaveableData value;

    private InheritedValue(String prefix, String dataName, SaveableData value) {
        this.prefix = prefix;
        this.dataName = dataName;
        this.value = value;
    }

    /**
     * Return the prefix where the value was finally found.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Return the data name that we were searching for.
     */
    public String getDataName() {
        return dataName;
    }

    /**
     * Return the full data name of the value that was finally found.
     */
    public String getFullDataName() {
        return DataRepository.createDataName(prefix, dataName);
    }

    /**
     * Return the value that was found. If no inherited value was found, this
     * will return null.
     */
    public SaveableData getValue() {
        return value;
    }

    /**
     * Return the value that was found, as a simple value. If no inherited value
     * was found, this will return null.
     */
    public SimpleData getSimpleValue() {
        return (value == null ? null : value.getSimpleValue());
    }

    /**
     * Look for a hierarchically inherited value in the given DataContext.
     * 
     * This method first constructs a data name by appending the given prefix
     * and name. Then it checks to see if the data context has a non-null value
     * for that name. If so, an InheritableValue object is returned containing
     * the value found. Otherwise, this method chops the final name segment off
     * of the prefix and tries again. It walks up the path hierarchy in this
     * manner and returns the first match found. Even if no match can be found,
     * an InheritableValue object is still returned; its getValue method will
     * return null.
     * 
     * Note that the search algorithm above is looking for non-null
     * {@link SaveableValue} objects. If the value found is a calculation, that
     * calculation might still evaluate to null.
     * 
     * @param data
     *                the data context
     * @param prefix
     *                the prefix to start the search
     * @param name
     *                the name of a data element to look up
     * @return return an {@link InheritedValue} object capturing the inherited
     *         value that was or was not found
     */
    public static InheritedValue get(DataContext data, String prefix, String name) {
        String dataName = prefix + "/" + name;
        SaveableData result = data.getValue(dataName);
        int pos;
        while (result == null && prefix.length() > 0) {
            pos = prefix.lastIndexOf('/');
            if (pos == -1) prefix = ""; else prefix = prefix.substring(0, pos);
            dataName = prefix + "/" + name;
            result = data.getValue(dataName);
        }
        return new InheritedValue(prefix, name, result);
    }
}

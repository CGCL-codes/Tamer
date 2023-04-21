package nl.alterra.openmi.sdk.extensions;

import java.util.Collection;

/**
 * Collection of IArgumentEx objects.
 */
public interface IArguments extends Collection<IArgumentEx> {

    /**
     * Returns the Value for the first found IArgument in the collection
     * that has the specified key.
     *
     * @param key
     * @return String Value, empty if key does not exist
     */
    public String getValueForKey(String key);

    /**
     * Changes the value of all arguments with the specified key and that
     * are not ReadOnly to the specified string.
     *
     * @param key
     * @param value
     */
    public void setValueForKey(String key, String value);

    /**
     * Returns true if the specified key exists in the collection.
     *
     * @param key
     * @return boolean, true if key exists
     */
    public boolean containsKey(String key);

    /**
     * Gets the index for a specified IArgumentEx in the collection.
     *
     * @param elem
     * @return int Index
     */
    public int indexOf(IArgumentEx elem);

    /**
     * Gets the IArgumentEx for the specified index.
     *
     * @return IArgumentEx The indexth argument
     */
    public IArgumentEx get(int index);
}

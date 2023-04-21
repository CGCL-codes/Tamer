package megamek.common.options;

import java.util.Enumeration;

/**
 * Interface that represents the options container
 */
public interface IOptions {

    /**
     * Returns the <code>Enumeration</code> of the option groups in this
     * options container.
     * 
     * @return <code>Enumeration</code> of the <code>IOptionGroup</code>
     */
    public abstract Enumeration<IOptionGroup> getGroups();

    /**
     * Returns the <code>Enumeration</code> of the options in this options
     * container. The order of options is not specified.
     * 
     * @return <code>Enumeration</code> of the <code>IOption</code>
     */
    public abstract Enumeration<IOption> getOptions();

    /**
     * Returns the option by name or <code>null</code> if there is no such
     * option
     * 
     * @param name option name
     * @return the option or <code>null</code> if there is no such option
     */
    public abstract IOption getOption(String name);

    /**
     * Returns the UI specific data to allow the user to set the option
     * 
     * @param name option name
     * @return UI specific data
     * @see IOptionInfo
     */
    public abstract IOptionInfo getOptionInfo(String name);

    /**
     * Returns the value of the desired option as the <code>boolean</code>
     * 
     * @param name option name
     * @return the value of the desired option as the <code>boolean</code>
     */
    public abstract boolean booleanOption(String name);

    /**
     * Returns the value of the desired option as the <code>int</code>
     * 
     * @param name option name
     * @return the value of the desired option as the <code>int</code>
     */
    public abstract int intOption(String name);

    /**
     * Returns the value of the desired option as the <code>float</code>
     * 
     * @param name option name
     * @return the value of the desired option as the <code>float</code>
     */
    public abstract float floatOption(String name);

    /**
     * Returns the value of the desired option as the <code>String</code>
     * 
     * @param name option name
     * @return the value of the desired option as the <code>String</code>
     */
    public abstract String stringOption(String name);
}

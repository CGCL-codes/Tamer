package bioweka.core.properties;

import java.util.StringTokenizer;
import bioweka.core.BioWekaUtils;

/**
 * Property for handling multiple strings, i.e. a string array.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.3 $
 */
public final class StringArrayProperty extends AbstractSingleOptionProperty {

    /**
     * The unique class identifier.
     */
    private static final long serialVersionUID = 3906644198295156025L;

    /**
     * The option flag to sets the strings on command line.
     */
    private String optionFlag = null;

    /**
     * The description of the strings property.
     */
    private String tipText = null;

    /**
     * The description of the string format.
     */
    private String optionSynopsis = null;

    /**
     * The string to separate the individual strings.
     */
    private String separator = null;

    /**
     * The string values.
     */
    private String[] strings = null;

    /**
     * The default strings.
     */
    private String[] defaultStrings = null;

    /**
     * Initializes the string array property.
     * @param name the name of the property
     * @param optionFlag the flag add a string on command line
     * @param tipText the description of the string array property
     * @param optionSynopsis the format of the strings
     * @param separator the string to seperate the individual strings
     * @param defaultStrings the default and initial strings
     * @throws NullPointerException if one of the parameters is 
     * <code>null</code>.
     */
    public StringArrayProperty(String name, String optionFlag, String tipText, String optionSynopsis, String separator, String[] defaultStrings) throws NullPointerException {
        super(name);
        if (optionFlag == null) {
            throw new NullPointerException("optionFlag is null.");
        }
        this.optionFlag = optionFlag;
        if (tipText == null) {
            throw new NullPointerException("tipText is null.");
        }
        this.tipText = tipText;
        if (optionSynopsis == null) {
            throw new NullPointerException("optionSynopsis is null.");
        }
        this.optionSynopsis = optionSynopsis;
        if (separator == null) {
            throw new NullPointerException("separator is null.");
        }
        this.separator = separator;
        if (defaultStrings == null) {
            throw new NullPointerException("defaultStrings is null.");
        }
        this.defaultStrings = (String[]) defaultStrings.clone();
        this.strings = (String[]) defaultStrings.clone();
    }

    /**
     * Returns the array of strings.
     * @return a string array
     */
    public String[] strings() {
        return (String[]) strings.clone();
    }

    /**
     * Gets the strings.
     * @return strings seperated by the specified char
     */
    public String getStrings() {
        return BioWekaUtils.join(strings, separator);
    }

    /**
     * Sets the strings
     * @param strings strings seperated by the specified char
     * @throws Exception if <code>strings</code> is invalid. 
     */
    public void setStrings(String strings) throws Exception {
        if (strings == null) {
            throw new NullPointerException("strings is null.");
        }
        StringTokenizer stringTokenizer = new StringTokenizer(strings, separator);
        int i = 0;
        String[] newStrings = new String[stringTokenizer.countTokens()];
        while (stringTokenizer.hasMoreTokens()) {
            newStrings[i] = stringTokenizer.nextToken();
            i++;
        }
        change(newStrings);
        this.strings = newStrings;
    }

    /**
     * {@inheritDoc}
     */
    protected String optionFlag() {
        return optionFlag;
    }

    /**
     * {@inheritDoc}
     */
    protected String optionSynopsis() {
        return optionSynopsis + separator + optionSynopsis + separator + "...";
    }

    /**
     * {@inheritDoc}
     */
    public String tipText() {
        return tipText;
    }

    /**
     * {@inheritDoc}
     */
    public Object value() {
        return (String[]) strings.clone();
    }

    /**
     * {@inheritDoc}
     */
    protected String defaultOptionValue() {
        return BioWekaUtils.join(defaultStrings, separator);
    }

    /**
     * {@inheritDoc}
     */
    protected String getOptionValue() {
        String optionValue = getStrings();
        if (defaultOptionValue().equals(optionValue)) {
            optionValue = null;
        }
        return optionValue;
    }

    /**
     * {@inheritDoc}
     */
    protected void setOptionValue(String value) throws Exception {
        setStrings(value);
    }

    /**
     * {@inheritDoc}
     */
    public Object clone() {
        StringArrayProperty clone = (StringArrayProperty) super.clone();
        clone.strings = (String[]) strings.clone();
        clone.defaultStrings = (String[]) defaultStrings.clone();
        return clone;
    }
}

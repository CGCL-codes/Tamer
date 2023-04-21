package bioweka.core.properties;

import bioweka.core.BioWekaUtils;
import weka.core.FastVector;
import weka.core.Option;
import weka.core.Utils;

/**
 * Abstract base class for {@link Property} implementations that supports 
 * command line options. Such a propert has a single option flag and a single
 * option argument, i.e. value. 
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.5 $
 */
public abstract class AbstractSingleOptionProperty extends AbstractOptionProperty {

    /**
     * Initializes the property with its name.
     * @param name the name for the property
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     */
    public AbstractSingleOptionProperty(String name) throws NullPointerException {
        super(name);
    }

    /**
     * Gets the default property value as an option value.
     * @return the default property value as it should be used on command line,
     * or <code>null</code> if the option must be set on command line, i.e. it
     * is a required option.
     */
    protected abstract String defaultOptionValue();

    /**
     * Gets the property value as an option value.
     * @return the property value as it should be used on command line, or 
     * <code>null</code> if the property is not set or set to the default value.
     */
    protected abstract String getOptionValue();

    /**
     * Sets the property value as an option value if the corresponding
     * option flag was set on command line.
     * @param value the command line argument
     * @throws Exception if <code>value</code> is invalid.
     */
    protected abstract void setOptionValue(String value) throws Exception;

    /**
     * {@inheritDoc}
     */
    public final void listOptions(FastVector options) throws NullPointerException {
        if (options == null) {
            throw new NullPointerException("options is null.");
        }
        String defaultValue = defaultOptionValue();
        if (defaultValue == null) {
            defaultValue = "<REQUIRED>";
        }
        options.addElement(new Option(BioWekaUtils.formatDescription(tipText(), defaultValue), optionFlag(), 1, "-" + optionFlag() + " " + optionSynopsis()));
    }

    /**
     * {@inheritDoc}
     */
    public final void getOptions(FastVector options) {
        if (options == null) {
            throw new NullPointerException("options is null.");
        }
        String value = getOptionValue();
        if (value != null) {
            options.addElement("-" + optionFlag());
            options.addElement(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    public final void setOptions(String[] options) throws Exception {
        if (options == null) {
            throw new NullPointerException("options is null.");
        }
        String value = Utils.getOption(optionFlag(), options);
        if (!"".equals(value)) {
            setOptionValue(value);
        }
    }
}

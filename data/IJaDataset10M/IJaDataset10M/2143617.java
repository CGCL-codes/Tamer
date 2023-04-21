package bioweka.core.properties.indices;

import bioweka.core.BioWekaUtils;
import bioweka.core.SimpleOptionHandler;
import bioweka.core.properties.AbstractVetoableProperty;
import weka.core.FastVector;
import weka.core.Option;
import weka.core.Range;
import weka.core.Utils;

/**
 * Abstract base property for indices ranges.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractRangeProperty extends AbstractVetoableProperty implements SimpleOptionHandler {

    /**
     * The description of how to handle indices ranges.
     */
    public static final String RANGE_OPTION_SYNOPSIS = "[all|first-last|1,2,3|...]";

    /**
     * The default value for the invert property.
     */
    public static final boolean INVERT_DEFAULT_VALUE = false;

    /**
	 * The actual range
	 */
    private Range range = null;

    /**
     * The default range value as string description
     */
    private String defaultRanges = null;

    /**
     * Initializes the range property with its name and the default range.
     * @param name the name for the property
     * @param defaultRanges the initial range value
     * @throws NullPointerException if <code>name</code> or 
     * <code>defaultRanges</code> is <code>null</code>. 
     */
    public AbstractRangeProperty(String name, String defaultRanges) throws NullPointerException {
        super(name);
        if (defaultRanges == null) {
            throw new NullPointerException("defaultRanges is null.");
        }
        this.defaultRanges = defaultRanges;
        range = new Range(defaultRanges);
    }

    /**
     * Returns the option flag to set the range property on command line.
     * @return option flag without the leading "-"
     */
    protected abstract String rangeOptionFlag();

    /**
     * Returns the option flag to set the invert property on command line.
     * @return option flag without the leading "-"
     */
    protected abstract String invertOptionFlag();

    /**
     * Returns the description of the range property.
     * @return a human readable text
     */
    protected abstract String rangeTipText();

    /**
     * Returns the description of the invert property.
     * @return a human readable text
     */
    protected abstract String invertTipText();

    /**
     * Gets the range.
     * @param upperLimit the maximum range possible
     * @return the numeric range
     */
    public final int[] selection(int upperLimit) {
        range.setUpper(upperLimit);
        return range.getSelection();
    }

    /**
     * Returns the range;
     * @return the range;
     */
    public final Range range() {
        return range;
    }

    /**
     * Gets the range.
     * @return the string range
     */
    public final String getRanges() {
        return range.getRanges();
    }

    /**
     * Sets the range.
	 * @param ranges the string range
	 * @throws Exception if <code>range</code> is invalid.
	 */
    public final void setRanges(String ranges) throws Exception {
        if (ranges == null) {
            throw new NullPointerException("ranges is null.");
        }
        Range newRange = new Range(ranges);
        change(newRange);
        this.range = newRange;
    }

    /**
     * Sets the range.
     * @param indices the selected indices
     * @throws Exception if <code>indices</code> is invalid.
     */
    public final void setRanges(int[] indices) throws Exception {
        setRanges(Range.indicesToRangeList(indices));
    }

    /**
     * Indicates if the range should be inverted
     * @return <code>true</code> if the range should be inverted,
     * <code>false</code> otherwise 
     */
    public final boolean getInvert() {
        return range.getInvert();
    }

    /**
     * Specifies if the range should be inverted.
     * @param invert <code>true</code> if the range should be inverted,
     * <code>false</code> otherwise 
     * @throws Exception if <code>invert</code> is invalid.
     */
    public final void setInvert(boolean invert) throws Exception {
        Range newRange = new Range(getRanges());
        newRange.setInvert(invert);
        change(newRange);
        range = newRange;
    }

    /**
     * {@inheritDoc}
     */
    public Object value() {
        return range;
    }

    /**
     * {@inheritDoc}
     */
    public Object clone() {
        AbstractRangeProperty clone = (AbstractRangeProperty) super.clone();
        clone.range = new Range(getRanges());
        return clone;
    }

    /**
     * {@inheritDoc}
     */
    public final void listOptions(FastVector options) throws NullPointerException {
        if (options == null) {
            throw new NullPointerException("options is null.");
        }
        options.addElement(new Option(BioWekaUtils.formatDescription(rangeTipText(), defaultRanges), rangeOptionFlag(), 1, "-" + rangeOptionFlag() + " " + RANGE_OPTION_SYNOPSIS));
        options.addElement(new Option(BioWekaUtils.formatDescription(invertTipText(), Boolean.toString(INVERT_DEFAULT_VALUE)), rangeOptionFlag(), 1, "-" + invertOptionFlag()));
    }

    /**
     * {@inheritDoc}
     */
    public final void getOptions(FastVector options) {
        if (options == null) {
            throw new NullPointerException("options is null.");
        }
        if (!defaultRanges.equals(range.getRanges())) {
            options.addElement("-" + rangeOptionFlag());
            options.addElement(range.getRanges());
        }
        if (range.getInvert() != INVERT_DEFAULT_VALUE) {
            options.addElement("-" + invertOptionFlag());
        }
    }

    /**
     * {@inheritDoc}
     */
    public final void setOptions(String[] options) throws Exception {
        if (options == null) {
            throw new NullPointerException("options is null.");
        }
        String ranges = Utils.getOption(rangeOptionFlag(), options);
        if (!"".equals(ranges)) {
            setRanges(ranges);
        }
        if (Utils.getFlag(invertOptionFlag(), options)) {
            setInvert(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String tipText() {
        return rangeTipText();
    }
}

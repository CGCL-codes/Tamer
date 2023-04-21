package bioweka.normalizers;

import bioweka.core.BioWekaUtils;
import weka.core.FastVector;
import weka.core.Option;
import weka.core.Utils;

/**
 * Normalizer for scaling values into a new numeric range. After normalization
 * all values lie between {@link #getNewMin()} and {@link #getNewMax()}. For 
 * scaling the minimum and maximum values of the registered values are used.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.2 $
 */
public final class MinMaxNormalizer extends AbstractSimpleNormalizer {

    /**
     * The unique class identifier.
     */
    private static final long serialVersionUID = 3258126977134573875L;

    /**
     * The default value for the new maximum property.
     */
    public static final double NEW_MAX_DEFAULT_VALUE = 1.0;

    /**
     * The option flag to set the new maximum property on command line.
     */
    public static final String NEW_MAX_OPTION_FLAG = "M";

    /**
     * The description of the new maximum property.
     */
    public static final String NEW_MAX_TIP_TEXT = "Specifies the upper bound.";

    /**
     * The name of the new maximum property.
     */
    public static final String NEW_MAX_PROPERTY_NAME = "newMax";

    /**
     * The default value for the new minimum property.
     */
    public static final double NEW_MIN_DEFAULT_VALUE = 0.0;

    /**
     * The option flag to set the new minimum property on command line.
     */
    public static final String NEW_MIN_OPTION_FLAG = "m";

    /**
     * The description of the new minimum property.
     */
    public static final String NEW_MIN_TIP_TEXT = "Specifies the lower bound.";

    /**
     * The name of the new minimum property.
     */
    public static final String NEW_MIN_PROPERTY_NAME = "newMin";

    /**
     * The description of the min-max-scaling normalizer component.
     */
    public static final String MIN_MAX_NORMALIZER_GLOBAL_INFO = "Normalizer for scaling values into a new numeric range. After " + "normalization all values lie between the new minimum and the new " + "maximum property.";

    /**
     * The maximum value after scaling/normalization.
     */
    private double newMax = 1.0;

    /**
     * The minimum value after scaling/normalization.
     */
    private double newMin = 0.0;

    /**
     * The minimum value of all registered values. 
     */
    private double min = Double.NaN;

    /**
     * The maximum value of all registered values. 
     */
    private double max = Double.NaN;

    /**
     * The scaling factor to calculate the new minimum and maximum values. 
     */
    private double factor = Double.NaN;

    /**
     * Initializes the min-max-scaling normalizer. 
     */
    public MinMaxNormalizer() {
        super();
    }

    /**
     * Gets the minimum value after scaling/normalization.
     * @return the minimum value of all normalized values
     */
    public double getNewMin() {
        return newMin;
    }

    /**
     * Sets the minimum value after scaling/normalization.
     * @param newMin the minimum value of all normalized values
     * @throws Exception if <code>newMin</code> is invalid.
     */
    public void setNewMin(double newMin) throws Exception {
        getDebugger().config(NEW_MIN_PROPERTY_NAME, new Double(newMin));
        this.newMin = newMin;
    }

    /**
     * The description of the new minimum property.
     * @return a human readable text
     */
    public String newMinTipText() {
        return NEW_MIN_TIP_TEXT;
    }

    /**
     * Gets the maximum value after scaling/normalization.
     * @return the maximum value of all normalized values
     */
    public double getNewMax() {
        return newMax;
    }

    /**
     * Sets the maximum value after scaling/normalization.
     * @param newMax the maximum value of all normalized values
     * @throws Exception if <code>newMax</code> is invalid.
     */
    public void setNewMax(double newMax) throws Exception {
        getDebugger().config(NEW_MAX_PROPERTY_NAME, new Double(newMax));
        this.newMax = newMax;
    }

    /**
     * Returns the description of the new maximum property.
     * @return a human readable text.
     */
    public String newMaxTipText() {
        return NEW_MAX_TIP_TEXT;
    }

    /**
     * {@inheritDoc}
     */
    public String globalInfo() {
        return MIN_MAX_NORMALIZER_GLOBAL_INFO;
    }

    /**
     * {@inheritDoc}
     */
    public void getOptions(FastVector options) throws NullPointerException {
        super.getOptions(options);
        if (newMin != NEW_MIN_DEFAULT_VALUE) {
            options.addElement("-" + NEW_MIN_OPTION_FLAG);
            options.addElement(Double.toString(newMin));
        }
        if (newMax != NEW_MAX_DEFAULT_VALUE) {
            options.addElement("-" + NEW_MAX_OPTION_FLAG);
            options.addElement(Double.toString(newMax));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listOptions(FastVector options) throws NullPointerException {
        super.listOptions(options);
        options.addElement(new Option(BioWekaUtils.formatDescription(NEW_MIN_TIP_TEXT, Double.toString(NEW_MIN_DEFAULT_VALUE)), NEW_MIN_OPTION_FLAG, 1, "-" + NEW_MIN_OPTION_FLAG + " <float>"));
        options.addElement(new Option(BioWekaUtils.formatDescription(NEW_MAX_TIP_TEXT, Double.toString(NEW_MAX_DEFAULT_VALUE)), NEW_MAX_OPTION_FLAG, 1, "-" + NEW_MAX_OPTION_FLAG + " <float>"));
    }

    /**
     * {@inheritDoc}
     */
    public void setOptions(String[] options) throws Exception {
        super.setOptions(options);
        String newMin = Utils.getOption(NEW_MIN_OPTION_FLAG, options);
        if (!"".equals(newMin)) {
            setNewMin(Double.parseDouble(newMin));
        }
        String newMax = Utils.getOption(NEW_MAX_OPTION_FLAG, options);
        if (!"".equals(newMax)) {
            setNewMax(Double.parseDouble(newMax));
        }
    }

    /**
     * {@inheritDoc}
     */
    public double doNormalize(double value) throws NullPointerException, Exception {
        if (!Double.isNaN(value) && !Double.isNaN(factor)) {
            value = (value - min) * factor + newMin;
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public void doInitialize() {
        min = Double.NaN;
        max = Double.NaN;
        factor = Double.NaN;
    }

    /**
     * {@inheritDoc}
     */
    public void doRegister(double value) {
        if (Double.isNaN(min)) {
            min = value;
            max = value;
        } else {
            if (value > max) {
                max = value;
            } else if (value < min) {
                min = value;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void doFinalize() {
        if (max != min) {
            factor = (newMax - newMin) / (max - min);
        }
    }
}

package uk.ac.ed.ph.jqtiplus.node.item.interaction;

import uk.ac.ed.ph.jqtiplus.control.AssessmentItemController;
import uk.ac.ed.ph.jqtiplus.node.item.response.declaration.ResponseDeclaration;
import org.qtitools.qti.attribute.enumerate.OrientationAttribute;
import org.qtitools.qti.attribute.value.BooleanAttribute;
import org.qtitools.qti.attribute.value.FloatAttribute;
import org.qtitools.qti.attribute.value.IntegerAttribute;
import org.qtitools.qti.node.XmlObject;
import org.qtitools.qti.validation.ValidationError;
import org.qtitools.qti.validation.ValidationResult;
import org.qtitools.qti.value.FloatValue;
import org.qtitools.qti.value.IntegerValue;
import org.qtitools.qti.value.Orientation;
import org.qtitools.qti.value.Value;

/**
 * 
 * The slider interaction presents the candidate with A control for selecting A numerical 
 * value between A lower and upper bound. It must be bound to A response variable with single
 * cardinality with A base-type of either integer or float.
 * 
 * Attribute : lowerBound [1]: float
 * If the associated response variable is of type integer then the lowerBound must be rounded 
 * down to the greatest integer less than or equal to the value given.
 * 
 * Attribute : upperBound [1]: float
 * If the associated response variable is of type integer then the upperBound must be rounded 
 * up to the least integer greater than or equal to the value given.
 * 
 * Attribute : step [0..1]: integer
 * The steps that the control moves in. For example, if the lowerBound and upperBound are [0,10] 
 * and step is 2 then the response would be constrained to the set of values {0,2,4,6,8,10}. If 
 * bound to an integer response the default step is 1, otherwise the slider is assumed to 
 * operate on an approximately continuous scale.
 * 
 * Attribute : stepLabel [0..1]: boolean = false
 * By default, sliders are labeled only at their ends. The stepLabel attribute controls whether 
 * or not each step on the slider should also be labeled. It is unlikely that delivery engines 
 * will be able to guarantee to label steps so this attribute should be treated only as request.
 * 
 * Attribute : orientation [0..1]: orientation
 * The orientation attribute provides A hint to rendering systems that the slider is being used 
 * to indicate the value of A quantity with an inherent vertical or horizontal interpretation. 
 * For example, an interaction that is used to indicate the value of height might set the 
 * orientation to vertical to indicate that rendering it horizontally could spuriously increase 
 * the difficulty of the item.
 * 
 * Attribute : reverse [0..1]: boolean
 * The reverse attribute provides A hint to rendering systems that the slider is being used to
 * indicate the value of A quantity for which the normal sense of the upper and lower bounds is
 * reversed. For example, an interaction that is used to indicate A depth below sea level might 
 * specify both A vertical orientation and set reverse.
 * 
 * Note that A slider interaction does not have A default or initial position except where 
 * specified by A default value for the associated response variable. The currently selected value,
 * if any, must be clearly indicated to the candidate.
 *
 * @author Jonathon Hare
 */
public class SliderInteraction extends BlockInteraction {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static String CLASS_TAG = "sliderInteraction";

    /** Name of lowerBound attribute in xml schema. */
    public static String ATTR_LOWER_BOUND_NAME = "lowerBound";

    /** Name of upperBound attribute in xml schema. */
    public static String ATTR_UPPER_BOUND_NAME = "upperBound";

    /** Name of step attribute in xml schema. */
    public static String ATTR_STEP_NAME = "step";

    /** Name of stepLabel attribute in xml schema. */
    public static String ATTR_STEP_LABEL_NAME = "stepLabel";

    /** Name of orientation attribute in xml schema. */
    public static String ATTR_ORIENTATION_NAME = "orientation";

    /** Name of reverse attribute in xml schema. */
    public static String ATTR_REVERSE_NAME = "reverse";

    /**
     * Construct new interaction.
     *  
     * @param parent Parent node
     */
    public SliderInteraction(XmlObject parent) {
        super(parent);
        getAttributes().add(new FloatAttribute(this, ATTR_LOWER_BOUND_NAME));
        getAttributes().add(new FloatAttribute(this, ATTR_UPPER_BOUND_NAME));
        getAttributes().add(new IntegerAttribute(this, ATTR_STEP_NAME, null, null, false));
        getAttributes().add(new BooleanAttribute(this, ATTR_STEP_LABEL_NAME, null, null, false));
        getAttributes().add(new OrientationAttribute(this, ATTR_ORIENTATION_NAME, null, null, false));
        getAttributes().add(new BooleanAttribute(this, ATTR_REVERSE_NAME, null, null, false));
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }

    /**
     * Sets new value of lowerBound attribute.
     *
     * @param lowerBound new value of lowerBound attribute
     * @see #getLowerBound
     */
    public void setLowerBound(Double lowerBound) {
        getAttributes().getFloatAttribute(ATTR_LOWER_BOUND_NAME).setValue(lowerBound);
    }

    /**
     * Gets value of lowerBound attribute.
     *
     * @return value of lowerBound attribute
     * @see #setLowerBound
     */
    public Double getLowerBound() {
        return getAttributes().getFloatAttribute(ATTR_LOWER_BOUND_NAME).getValue();
    }

    /**
     * Sets new value of upperBound attribute.
     *
     * @param upperBound new value of upperBound attribute
     * @see #getUpperBound
     */
    public void setUpperBound(Double upperBound) {
        getAttributes().getFloatAttribute(ATTR_UPPER_BOUND_NAME).setValue(upperBound);
    }

    /**
     * Gets value of upperBound attribute.
     *
     * @return value of upperBound attribute
     * @see #setUpperBound
     */
    public Double getUpperBound() {
        return getAttributes().getFloatAttribute(ATTR_UPPER_BOUND_NAME).getValue();
    }

    /**
     * Sets new value of step attribute.
     *
     * @param step new value of step attribute
     * @see #getStep
     */
    public void setStep(Integer step) {
        getAttributes().getIntegerAttribute(ATTR_STEP_NAME).setValue(step);
    }

    /**
     * Gets value of step attribute.
     *
     * @return value of step attribute
     * @see #setStep
     */
    public Integer getStep() {
        return getAttributes().getIntegerAttribute(ATTR_STEP_NAME).getValue();
    }

    /**
     * Sets new value of stepLabel attribute.
     *
     * @param stepLabel new value of stepLabel attribute
     * @see #getStepLabel
     */
    public void setStepLabel(Boolean stepLabel) {
        getAttributes().getBooleanAttribute(ATTR_STEP_LABEL_NAME).setValue(stepLabel);
    }

    /**
     * Gets value of stepLabel attribute.
     *
     * @return value of stepLabel attribute
     * @see #setStepLabel
     */
    public Boolean getStepLabel() {
        return getAttributes().getBooleanAttribute(ATTR_STEP_LABEL_NAME).getValue();
    }

    /**
     * Sets new value of orientation attribute.
     *
     * @param orientation new value of orientation attribute
     * @see #getOrientation
     */
    public void setOrientation(Orientation orientation) {
        getAttributes().getOrientationAttribute(ATTR_ORIENTATION_NAME).setValue(orientation);
    }

    /**
     * Gets value of orientation attribute.
     *
     * @return value of orientation attribute
     * @see #setOrientation
     */
    public Orientation getOrientation() {
        return getAttributes().getOrientationAttribute(ATTR_ORIENTATION_NAME).getValue();
    }

    /**
     * Sets new value of reverse attribute.
     *
     * @param reverse new value of reverse attribute
     * @see #getReverse
     */
    public void setReverse(Boolean reverse) {
        getAttributes().getBooleanAttribute(ATTR_REVERSE_NAME).setValue(reverse);
    }

    /**
     * Gets value of reverse attribute.
     *
     * @return value of reverse attribute
     * @see #setReverse
     */
    public Boolean getReverse() {
        return getAttributes().getBooleanAttribute(ATTR_REVERSE_NAME).getValue();
    }

    @Override
    public ValidationResult validate() {
        ValidationResult result = super.validate();
        if (getResponseIdentifier() != null) {
            ResponseDeclaration declaration = getParentItem().getResponseDeclaration(getResponseIdentifier());
            if (declaration != null && declaration.getCardinality() != null && !declaration.getCardinality().isSingle()) result.add(new ValidationError(this, "Response variable must have single cardinality"));
            if (declaration != null && declaration.getBaseType() != null && !declaration.getBaseType().isNumeric()) result.add(new ValidationError(this, "Response variable must have numeric base type"));
        }
        return result;
    }

    @Override
    public boolean validateResponse(AssessmentItemController itemController, Value responseValue) {
        if (responseValue.isNull()) {
            return false;
        }
        if (getResponseDeclaration().getBaseType().isFloat()) {
            double doubleValue = ((FloatValue) responseValue).doubleValue();
            if (doubleValue < getLowerBound() || doubleValue > getUpperBound()) {
                return false;
            }
        } else {
            int intValue = ((IntegerValue) responseValue).intValue();
            int lowerBound = (int) Math.floor(getLowerBound());
            int upperBound = (int) Math.ceil(getUpperBound());
            if (intValue < lowerBound || intValue > upperBound) {
                return false;
            }
        }
        return true;
    }
}

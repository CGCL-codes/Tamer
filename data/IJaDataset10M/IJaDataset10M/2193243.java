package uk.ac.ed.ph.jqtiplus.node.expression.operator;

import uk.ac.ed.ph.jqtiplus.attribute.value.LongAttribute;
import uk.ac.ed.ph.jqtiplus.node.expression.ExpressionParent;

/**
 * Extends random expression - supports seed attribute.
 * <p>
 * This expression should be used only for special purposes when you need repeatability (not for real assessments).
 * 
 * @author Jiri Kajaba
 */
public class RandomEx extends Random {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "randomEx";

    /** Name of seed attribute in xml schema. */
    public static final String ATTR_SEED_NAME = "seed";

    /** Default value of seed attribute. */
    public static final Long ATTR_SEED_DEFAULT_VALUE = null;

    /**
     * Constructs expression.
     *
     * @param parent parent of this expression
     */
    public RandomEx(ExpressionParent parent) {
        super(parent);
        getAttributes().add(new LongAttribute(this, ATTR_SEED_NAME, ATTR_SEED_DEFAULT_VALUE));
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }

    @Override
    public Long getSeedAttributeValue() {
        return getAttributes().getLongAttribute(ATTR_SEED_NAME).getValue();
    }

    /**
     * Sets new value of seed attribute.
     *
     * @param seed new value of seed attribute
     * @see #getSeedAttributeValue
     */
    public void setSeedAttributeValue(Long seed) {
        getAttributes().getLongAttribute(ATTR_SEED_NAME).setValue(seed);
    }
}

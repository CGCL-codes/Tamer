package uk.ac.ed.ph.jqtiplus.node.item.interaction;

import uk.ac.ed.ph.jqtiplus.control.AssessmentItemController;
import uk.ac.ed.ph.jqtiplus.node.item.interaction.choice.SimpleAssociableChoice;
import uk.ac.ed.ph.jqtiplus.node.item.interaction.choice.SimpleAssociableChoiceContainer;
import uk.ac.ed.ph.jqtiplus.node.item.response.declaration.ResponseDeclaration;
import org.qtitools.qti.attribute.value.BooleanAttribute;
import org.qtitools.qti.attribute.value.IntegerAttribute;
import org.qtitools.qti.group.item.interaction.choice.SimpleAssociableChoiceGroup;
import org.qtitools.qti.node.XmlNode;
import org.qtitools.qti.node.XmlObject;
import org.qtitools.qti.validation.ValidationError;
import org.qtitools.qti.validation.ValidationResult;
import org.qtitools.qti.value.ListValue;
import org.qtitools.qti.value.PairValue;
import org.qtitools.qti.value.SingleValue;
import org.qtitools.qti.value.Value;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * An associate interaction is a blockInteraction that presents candidates 
 * with a number of choices and allows them to create associations between them.
 * 
 * The associateInteraction must be bound to a response variable with base-type 
 * pair and either single or multiple cardinality.
 * 
 * Attribute : shuffle [1]: boolean = false
 * If the shuffle attribute is true then the delivery engine must randomize the order
 * in which the choices are presented subject to the fixed attribute of the choice.
 * 
 * Attribute : maxAssociations [1]: integer = 1
 * The maximum number of associations that the candidate is allowed to make. If 
 * maxAssociations is 0 then there is no restriction. If maxAssociations is greater 
 * than 1 (or 0) then the interaction must be bound to a response with multiple cardinality.
 * 
 * Attribute : minAssociations [0..1]: integer = 0
 * The minimum number of associations that the candidate is required to make to form a valid 
 * response. If minAssociations is 0 then the candidate is not required to make any associations. 
 * minAssociations must be less than or equal to the limit imposed by maxAssociations.
 * 
 * Contains : simpleAssociableChoice [1..*]
 * An ordered set of choices.
 *
 * @author Jonathon Hare
 */
public class AssociateInteraction extends BlockInteraction implements SimpleAssociableChoiceContainer, Shuffleable {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static String CLASS_TAG = "associateInteraction";

    /** Name of shuffle attribute in xml schema. */
    public static String ATTR_SHUFFLE_NAME = "shuffle";

    /** Default value of shuffle attribute. */
    public static boolean ATTR_SHUFFLE_DEFAULT_VALUE = false;

    /** Name of maxAssociations attribute in xml schema. */
    public static String ATTR_MAX_ASSOCIATIONS_NAME = "maxAssociations";

    /** Default value of maxAssociations attribute. */
    public static int ATTR_MAX_ASSOCIATIONS_DEFAULT_VALUE = 1;

    /** Name of minAssociations attribute in xml schema. */
    public static String ATTR_MIN_ASSOCIATIONS_NAME = "minAssociations";

    /** Default value of minAssociations attribute. */
    public static int ATTR_MIN_ASSOCIATIONS_DEFAULT_VALUE = 0;

    private List<String> shuffledChoiceOrder;

    /**
     * Construct new interaction.
     *  
     * @param parent Parent node
     */
    public AssociateInteraction(XmlObject parent) {
        super(parent);
        getAttributes().add(new BooleanAttribute(this, ATTR_SHUFFLE_NAME, ATTR_SHUFFLE_DEFAULT_VALUE, ATTR_SHUFFLE_DEFAULT_VALUE, true));
        getAttributes().add(new IntegerAttribute(this, ATTR_MAX_ASSOCIATIONS_NAME, ATTR_MAX_ASSOCIATIONS_DEFAULT_VALUE, ATTR_MAX_ASSOCIATIONS_DEFAULT_VALUE, true));
        getAttributes().add(new IntegerAttribute(this, ATTR_MIN_ASSOCIATIONS_NAME, ATTR_MIN_ASSOCIATIONS_DEFAULT_VALUE, ATTR_MIN_ASSOCIATIONS_DEFAULT_VALUE, false));
        getNodeGroups().add(new SimpleAssociableChoiceGroup(this, 1));
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }

    /**
     * Gets an unmodifiable list of the child elements. Use the other
     * methods on AssociateInteraction to add children to the correct group.
     */
    @Override
    public List<? extends XmlNode> getChildren() {
        List<XmlNode> children = new ArrayList<XmlNode>();
        children.addAll(super.getChildren());
        children.addAll(getNodeGroups().getSimpleAssociableChoiceGroup().getSimpleAssociableChoices());
        return Collections.unmodifiableList(children);
    }

    /**
     * Sets new value of shuffle attribute.
     *
     * @param shuffle new value of shuffle attribute
     * @see #getShuffle
     */
    public void setShuffle(Boolean shuffle) {
        getAttributes().getBooleanAttribute(ATTR_SHUFFLE_NAME).setValue(shuffle);
    }

    /**
     * Gets value of shuffle attribute.
     *
     * @return value of shuffle attribute
     * @see #setShuffle
     */
    public Boolean getShuffle() {
        return getAttributes().getBooleanAttribute(ATTR_SHUFFLE_NAME).getValue();
    }

    /**
     * Sets new value of maxAssociations attribute.
     *
     * @param maxAssociations new value of maxAssociations attribute
     * @see #getMaxAssociations
     */
    public void setMaxAssociations(Integer maxAssociations) {
        getAttributes().getIntegerAttribute(ATTR_MAX_ASSOCIATIONS_NAME).setValue(maxAssociations);
    }

    /**
     * Gets value of maxAssociations attribute.
     *
     * @return value of maxAssociations attribute
     * @see #setMaxAssociations
     */
    public Integer getMaxAssociations() {
        return getAttributes().getIntegerAttribute(ATTR_MAX_ASSOCIATIONS_NAME).getValue();
    }

    /**
     * Sets new value of minAssociations attribute.
     *
     * @param minAssociations new value of minAssociations attribute
     * @see #getMinAssociations
     */
    public void setMinAssociations(Integer minAssociations) {
        getAttributes().getIntegerAttribute(ATTR_MIN_ASSOCIATIONS_NAME).setValue(minAssociations);
    }

    /**
     * Gets value of minAssociations attribute.
     *
     * @return value of minAssociations attribute
     * @see #setMinAssociations
     */
    public Integer getMinAssociations() {
        return getAttributes().getIntegerAttribute(ATTR_MIN_ASSOCIATIONS_NAME).getValue();
    }

    /**
     * Gets simpleAssociableChoice children.
     *
     * @return simpleAssociableChoice children
     */
    public List<SimpleAssociableChoice> getSimpleAssociableChoices() {
        return getNodeGroups().getSimpleAssociableChoiceGroup().getSimpleAssociableChoices();
    }

    /**
     * Gets simpleAssociableChoice child with given identifier or null.
     *
     * @param identifier given identifier
     * @return simpleAssociableChoice with given identifier or null
     */
    public SimpleAssociableChoice getSimpleAssociableChoice(String identifier) {
        for (SimpleAssociableChoice choice : getSimpleAssociableChoices()) if (choice.getIdentifier() != null && choice.getIdentifier().equals(identifier)) return choice;
        return null;
    }

    @Override
    public ValidationResult validate() {
        ValidationResult result = super.validate();
        if (getMinAssociations() > getMaxAssociations()) result.add(new ValidationError(this, "Minimum number of associations must be less than or equal to maximum number of associations"));
        if (getResponseIdentifier() != null) {
            ResponseDeclaration declaration = getResponseDeclaration();
            if (declaration != null) {
                if (declaration.getBaseType() != null && !declaration.getBaseType().isPair()) result.add(new ValidationError(this, "Response variable must have pair base type"));
                if (getMaxAssociations() != 1 && declaration.getCardinality() != null && !declaration.getCardinality().isMultiple()) {
                    result.add(new ValidationError(this, "Response variable must have multiple cardinality when maxAssociations is not 1"));
                } else if (declaration.getCardinality() != null && !(declaration.getCardinality().isSingle() || declaration.getCardinality().isMultiple())) {
                    result.add(new ValidationError(this, "Response variable must have single or multiple cardinality"));
                }
            }
        }
        return result;
    }

    @Override
    public void initialize(AssessmentItemController itemController) {
        super.initialize(itemController);
        itemController.shuffleInteractionChoiceOrder(this, getSimpleAssociableChoices());
    }

    public List<String> getShuffledChoiceOrder() {
        return shuffledChoiceOrder;
    }

    @Override
    public boolean validateResponse(AssessmentItemController itemController, Value responseValue) {
        List<PairValue> responseAssociations = new ArrayList<PairValue>();
        if (responseValue.isNull()) {
        } else if (responseValue.getCardinality().isList()) {
            for (SingleValue association : (ListValue) responseValue) {
                responseAssociations.add((PairValue) association);
            }
        } else {
            responseAssociations.add((PairValue) responseValue);
        }
        int maxAssociations = getMaxAssociations();
        int minAssociations = getMinAssociations();
        if (responseAssociations.size() < minAssociations) {
            return false;
        }
        if (maxAssociations != 0 && responseAssociations.size() > maxAssociations) {
            return false;
        }
        List<SimpleAssociableChoice> choices = getSimpleAssociableChoices();
        Map<String, Integer> responseChoiceCounts = new HashMap<String, Integer>();
        for (SimpleAssociableChoice choice : choices) {
            responseChoiceCounts.put(choice.getIdentifier(), Integer.valueOf(0));
        }
        for (PairValue responseAssociation : responseAssociations) {
            String sourceIdentifier = responseAssociation.sourceValue();
            String destIdentifier = responseAssociation.destValue();
            Integer sourceCount = responseChoiceCounts.get(sourceIdentifier);
            Integer destCount = responseChoiceCounts.get(destIdentifier);
            if (sourceCount == null || destCount == null) {
                return false;
            }
            responseChoiceCounts.put(sourceIdentifier, sourceCount + 1);
            responseChoiceCounts.put(destIdentifier, destCount + 1);
        }
        for (SimpleAssociableChoice choice : choices) {
            Integer responseChoiceCount = responseChoiceCounts.get(choice.getIdentifier());
            if (!validateChoice(choice, responseChoiceCount)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateChoice(SimpleAssociableChoice choice, int responseCountCount) {
        int matchMin = choice.getMatchMin();
        int matchMax = choice.getMatchMax();
        if (responseCountCount < matchMin) {
            return false;
        } else if (matchMax != 0 && responseCountCount > matchMax) {
            return false;
        }
        return true;
    }
}

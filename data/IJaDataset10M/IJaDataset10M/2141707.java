package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.compoundrelationship.validation;

import uk.co.ordnancesurvey.rabbitparser.exception.RabbitRuntimeException;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.compoundrelationship.ParsedSubsumptionPhrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.CompoundObjectType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedCompoundObject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedConceptObject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedObject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedObjectAndList;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedObjectList;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.ObjectType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.objectmodifier.IParsedNumber;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.objectmodifier.IParsedObjectPrefix;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.objectmodifier.ObjectPrefixType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.validation.BaseParsedPartValidationRule;

/**
 * Validates the {@link IParsedCompoundObject} of a
 * {@link ParsedSubsumptionPhrase}. Number restrictions and the like can't be
 * used in combination with a concept subsumption.
 * 
 * @author rdenaux
 * 
 */
public class SubsumptionPhraseSuperObjectVR extends BaseParsedPartValidationRule<ParsedSubsumptionPhrase> {

    private static final long serialVersionUID = 3852047263604681031L;

    public SubsumptionPhraseSuperObjectVR(ParsedSubsumptionPhrase aSubsumptionPhrase) {
        super();
        setPartToValidate(aSubsumptionPhrase);
    }

    public void validate() {
        ParsedSubsumptionPhrase toVal = getPartToValidate();
        IParsedCompoundObject compObj = toVal.getSuperCompoundObject();
        CompoundObjectType type = CompoundObjectType.getType(compObj);
        switch(type) {
            case Object:
                validateObject((IParsedObject) compObj);
                break;
            case ObjectAndList:
                validateAndList((IParsedObjectAndList) compObj);
                break;
            case ObjectList:
                validateList((IParsedObjectList) compObj);
                break;
            default:
                throw new RabbitRuntimeException("unsupported type " + type);
        }
    }

    private void validateList(IParsedObjectList aObjList) {
        IParsedNumber num = aObjList.getNumberOfMoreOf();
        if (num != null) {
            addError("Cannot use a combination of 'is a kind of' and a list of concepts with a number restriction '" + num.getAsString() + " or more of'");
        }
    }

    private void validateAndList(IParsedObjectAndList compObj) {
    }

    private void validateObject(IParsedObject compObj) {
        IParsedObject actualObj = ObjectType.getActualObject(compObj);
        ObjectType type = ObjectType.getType(actualObj);
        switch(type) {
            case ConceptObject:
                validateConceptObject((IParsedConceptObject) actualObj);
                break;
            case Instance:
                addError("Cannot use an individual in combination with 'is a kind of', " + "because an individual is one of a kind: " + actualObj.getAsString());
                break;
            default:
                throw new RabbitRuntimeException("unsupported type " + type);
        }
    }

    private void validateConceptObject(IParsedConceptObject conceptObj) {
        IParsedObjectPrefix prefix = conceptObj.getObjectPrefix();
        if (prefix != null) {
            ObjectPrefixType type = ObjectPrefixType.getType(prefix);
            switch(type) {
                case ARTICLE:
                    break;
                case NUMBER_RESTRICTION:
                    addError("Cannot use a number restriction in combination with 'is a kind of': " + prefix.getAsString());
                    break;
                default:
                    throw new RabbitRuntimeException("unsupported type " + type);
            }
        }
    }
}

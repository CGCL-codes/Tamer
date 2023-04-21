package uk.co.ordnancesurvey.rabbitparser.parsedsentences.corerelationsdeclaration;

import uk.co.ordnancesurvey.rabbitparser.DeclarativeSentenceType;
import uk.co.ordnancesurvey.rabbitparser.IRabbitParsedResult;
import uk.co.ordnancesurvey.rabbitparser.RabbitFeatureType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.ParsedRelationCollection;
import uk.co.ordnancesurvey.rabbitparser.parsedsentences.BaseParsedSentence;

/**
 * Represent a parsed sentence of type CORE_RELATIONS_DECLARATION
 * 
 * @author rdenaux
 * 
 */
public class CoreRelationsDeclarationParsedSentence extends BaseParsedSentence {

    public CoreRelationsDeclarationParsedSentence(IRabbitParsedResult rabbitParsedResult) {
        super(rabbitParsedResult);
    }

    @Override
    protected void validateSentence() {
    }

    public DeclarativeSentenceType getSentenceType() {
        return DeclarativeSentenceType.CORE_RELATIONS_DECLARATION;
    }

    /**
	 * @return the relationCollection
	 */
    public final ParsedRelationCollection getRelationCollection() {
        return (ParsedRelationCollection) getParsedPart(RabbitFeatureType.RELATION_COLLECTION);
    }

    @Override
    protected void doSetParsedPart(RabbitFeatureType featureType) {
        super.doSetParsedPart(featureType);
        switch(featureType) {
            case RELATION_COLLECTION:
                getRelationCollection().setEntityCollectionBeingDefined(true);
                break;
            default:
        }
    }
}

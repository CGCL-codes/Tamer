package uk.co.ordnancesurvey.rabbitparser.parsedsentences.coreconceptsdeclaration;

import uk.co.ordnancesurvey.rabbitparser.DeclarativeSentenceType;
import uk.co.ordnancesurvey.rabbitparser.IRabbitParsedResult;
import uk.co.ordnancesurvey.rabbitparser.RabbitFeatureType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.ParsedConceptCollection;
import uk.co.ordnancesurvey.rabbitparser.parsedsentences.BaseParsedSentence;

/**
 * Represents a parsed sentence of type CORE_CONCEPT_DECLARATION
 * 
 * @author rdenaux
 * 
 */
public class CoreConceptDeclarationParsedSentence extends BaseParsedSentence {

    public CoreConceptDeclarationParsedSentence(IRabbitParsedResult rabbitParsedResult) {
        super(rabbitParsedResult);
    }

    @Override
    protected void validateSentence() {
    }

    public DeclarativeSentenceType getSentenceType() {
        return DeclarativeSentenceType.CORE_CONCEPTS_DECLARATION;
    }

    /**
	 * @return the conceptCollection
	 */
    public final ParsedConceptCollection getConceptCollection() {
        return (ParsedConceptCollection) getParsedPart(RabbitFeatureType.CONCEPT_COLLECTION);
    }

    @Override
    protected void doSetParsedPart(RabbitFeatureType featureType) {
        super.doSetParsedPart(featureType);
        switch(featureType) {
            case CONCEPT_COLLECTION:
                getConceptCollection().setEntityCollectionBeingDefined(true);
                break;
            default:
        }
    }
}

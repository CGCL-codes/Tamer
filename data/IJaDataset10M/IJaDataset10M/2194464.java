package uk.co.ordnancesurvey.rabbitparser.parsedsentences.nonexclusiveor;

import uk.co.ordnancesurvey.rabbitparser.DeclarativeSentenceType;
import uk.co.ordnancesurvey.rabbitparser.IRabbitParsedResult;
import uk.co.ordnancesurvey.rabbitparser.RabbitFeatureType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.ParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.ParsedConceptCollection;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.ParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentences.BaseParsedSentence;

/**
 * Parsed sentence matching the NON_EXCLUSIVE_OR pattern.
 * 
 * @author rdenaux
 * 
 */
public class NonExclusiveOrParsedSentence extends BaseParsedSentence {

    public NonExclusiveOrParsedSentence(IRabbitParsedResult rabbitParsedResult) {
        super(rabbitParsedResult);
    }

    @Override
    protected void validateSentence() {
    }

    public DeclarativeSentenceType getSentenceType() {
        return DeclarativeSentenceType.NON_EXCLUSIVE_OR;
    }

    /**
	 * @return the c1
	 */
    protected final ParsedConcept getC1() {
        return (ParsedConcept) getParsedPart(RabbitFeatureType.C1);
    }

    /**
	 * @return the relation
	 */
    public final ParsedRelation getRelation() {
        return (ParsedRelation) getParsedPart(RabbitFeatureType.RELATION);
    }

    /**
	 * @return the conceptCollection
	 */
    public final ParsedConceptCollection getConceptCollection() {
        return (ParsedConceptCollection) getParsedPart(RabbitFeatureType.CONCEPT_COLLECTION);
    }
}

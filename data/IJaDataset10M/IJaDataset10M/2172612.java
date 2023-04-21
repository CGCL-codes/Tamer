package uk.co.ordnancesurvey.rabbitparser.gate.jape.conceptassertionfinder;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship.IParsedCompoundRelationship;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.conceptassertion.ParsedProbableAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedConceptAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.subject.IParsedSubject;

/**
 * Handles the finding of a probable assertion pattern
 * 
 * @author rdenaux
 * 
 */
public class JapeCB_ProbableAssertion extends BaseConceptAssertionCB {

    @Override
    protected IParsedConceptAssertion retrieveFoundPart() {
        IParsedSubject sub = getRequiredPart("subject", IParsedSubject.class);
        IParsedCompoundRelationship cr = getRequiredPart("compoundRelationship", IParsedCompoundRelationship.class);
        return new ParsedProbableAssertion(sub, cr, getSingleKeyphraseAsList());
    }
}

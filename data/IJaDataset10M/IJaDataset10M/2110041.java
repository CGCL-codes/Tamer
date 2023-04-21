package uk.co.ordnancesurvey.rabbitparser.gate.jape.conceptassertionfinder;

import uk.co.ordnancesurvey.rabbitparser.gate.annotation.RbtAnnotation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.complexobject.IParsedComplexRelationshipPhrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.conceptassertion.ParsedAllValueAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedConceptAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.subject.IParsedSubject;

/**
 * Handles the finding of a All values assertion pattern.
 * 
 * @author rdenaux
 * 
 */
public class JapeCB_AllValuesAssertion extends BaseConceptAssertionCB {

    @Override
    protected IParsedConceptAssertion retrieveFoundPart() {
        RbtAnnotation rbtAnn = getRequiredRbtAnn("subject");
        IParsedSubject sub = rbtAnn.getSubject();
        rbtAnn = getRequiredRbtAnn("complexRelationshipPhrase");
        IParsedComplexRelationshipPhrase crp = rbtAnn.getComplexRelationshipPhrase();
        return new ParsedAllValueAssertion(sub, crp, getParsedKeyphrases());
    }
}

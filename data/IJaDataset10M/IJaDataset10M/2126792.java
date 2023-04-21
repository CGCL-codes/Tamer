package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.complexobject.IParsedComplexRelationshipPhrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedConceptAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.subject.IParsedSubject;

/**
 * Defines all values that are possible for the concept in the
 * {@link IParsedSubject}.
 * 
 * @author rdenaux
 * 
 */
public interface IParsedAllValueAssertion extends IParsedConceptAssertion {

    IParsedSubject getSubject();

    IParsedComplexRelationshipPhrase getComplexRelationshipPhrase();
}

package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.instanceassertion;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship.IParsedCompoundRelationship;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedInstance;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedInstanceAssertion;

/**
 * States an assertion about an instance.
 * 
 * @author rdenaux
 * 
 */
public interface IParsedPositiveInstanceAssertion extends IParsedInstanceAssertion {

    /**
     * Returns the {@link IParsedInstance} of which an assertion is made.
     * 
     * @return
     */
    IParsedInstance getInstance();

    /**
     * Returns the {@link IParsedCompoundRelationship} containing the assertion
     * that is being made.
     * 
     * @return
     */
    IParsedCompoundRelationship getCompoundRelationship();
}

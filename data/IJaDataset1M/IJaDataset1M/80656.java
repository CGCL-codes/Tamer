package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedPart;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedCompoundObject;

/**
 * Represents a 'is a kind of <concept>' pattern
 * 
 * @author rdenaux
 * 
 */
public interface IParsedSubsumptionPhrase extends IParsedPart, IParsedDefinitionPhrase {

    /**
     * Returns the contained keyphrase
     * 
     * @return
     */
    IParsedKeyphrase getKeyphrase();

    /**
     * Returns the super compound object defined by the subsumption phrase. This
     * is the object that is a parent class of the subject of this
     * {@link IParsedSubsumptionPhrase}.
     * 
     * @return
     */
    IParsedCompoundObject getSuperCompoundObject();
}

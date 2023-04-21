package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelation;

/**
 * Interface for sentences which define of modify a relationship.
 * 
 * @author rdenaux
 * 
 */
public interface IParsedRelationshipModifier extends IParsedSentenceBody {

    /**
     * Returns the {@link IParsedRelation} modified by this
     * {@link IParsedRelationshipModifier}.
     * 
     * @return
     */
    IParsedRelation getModifiedRelation();
}

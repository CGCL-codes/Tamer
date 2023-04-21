package uk.co.ordnancesurvey.rabbitparser.canonicalname;

import java.net.URL;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedEntity;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedInstance;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelation;

/**
 * Provides methods for getting the canonical name of parsed entities.
 * 
 * @author rdenaux
 * 
 */
public interface CanonicalNameProvider {

    /**
	 * Convenience method in case you don't care whether aParsedEntity is a
	 * concept, relation or instance.
	 * 
	 * @param aParsedEntity
	 * @return
	 */
    String getCanonicalName(IParsedEntity aParsedEntity);

    /**
	 * Returns the unique id (as a URL) of the canonization process applied to
	 * aParsedEntity to derive the canonical name.
	 * 
	 * @param aParsedEntity
	 * @return
	 */
    URL getCanonizerId(IParsedEntity aParsedEntity);

    URL getInstanceCanonizerId();

    URL getConceptCanonizerId();

    URL getRelationCanonizerId();

    /**
	 * Returns the canonical name of aConcept
	 * 
	 * The canonical name is a unique name generated based on the parsed tokens
	 * that aConcept contains. It is used as a means of identifying, creating,
	 * matching and retrieving aConcept in the ontology.
	 * 
	 * @param aConcept
	 * @return
	 */
    String getCanonicalName(IParsedConcept aConcept);

    /**
	 * Returns the canonical name of aRelation.
	 * 
	 * The canonical name is a unique name generated based on the parsed tokens
	 * that aConcept contains. It is used as a means of identifying, creating,
	 * matching and retrieving aRelation in the ontology.
	 * 
	 * @param aRelation
	 * @return
	 */
    String getCanonicalName(IParsedRelation aRelation);

    /**
	 * Returns the canonical name of aInstance
	 * 
	 * The canonical name is a unique name generated based on the parsed tokens
	 * that aConcept contains. It is used as a means of identifying, creating,
	 * matching and retrieving aInstance in the ontology.
	 * 
	 * 
	 * @param aInstance
	 * @return
	 */
    String getCanonicalName(IParsedInstance aInstance);
}

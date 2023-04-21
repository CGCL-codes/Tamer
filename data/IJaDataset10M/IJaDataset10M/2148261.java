package uk.co.ordnancesurvey.rabbitparser;

import java.io.Serializable;
import uk.co.ordnancesurvey.rabbitparser.canonicalname.CanonicalNameProvider;
import uk.co.ordnancesurvey.rabbitparser.result.imports.IRabbitImportManager;

/**
 * Defines the methods a RabbitParser client must provide. The
 * {@link IRabbitParserClient} provides context during parsing and conversion of
 * a parsed result. This context can be a backend ontology and/or a set of
 * previously parsed rabbit sentences (but not converted into an ontology yet).
 * 
 * @author rdenaux
 * 
 */
public interface IRabbitParserClient<ClassType, IndividualType, RelationshipType, OntologyType> extends RabbitToOWLEntityMapper<ClassType, IndividualType, RelationshipType>, CanonicalNameProvider, Serializable {

    /**
	 * Returns the import loader for rabbit documents. This import loader
	 * 
	 * @return
	 */
    IRabbitImportManager getRabbitImportManager();

    /**
	 * Returns the target ontology. This is the ontology that should be modified
	 * when a parsed result is applied to an OWL Ontology.
	 * 
	 * @return
	 */
    OntologyType getTargetOntology();

    /**
	 * Adds a listener to this
	 * 
	 * @param aListener
	 */
    void addListener(IRabbitParserClientListener aListener);

    /**
	 * Removes aListener from the list of listeners of this IRabbitParserClient.
	 * 
	 * @param aListener
	 */
    void removeListener(IRabbitParserClientListener aListener);

    /**
	 * Dispose any resources used up by this {@link IRabbitParserClient}.
	 */
    void dispose();
}

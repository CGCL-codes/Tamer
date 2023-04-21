package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.parserclient;

import uk.co.ordnancesurvey.rabbitparser.IRabbitParserClient;

/**
 * This dummy implementation of the IRabbitParserClient always returns a string
 * for all canonicalNames
 * 
 * @author rdenaux
 * 
 */
public class TestAlwaysFindEntitiesParserClient implements IRabbitParserClient {

    private static final String CONCEPT_ID = "testConceptId";

    private static final String INDIVIDUAL_ID = "testIndividualId";

    private static final String RELATION_ID = "testRelationId";

    @Override
    public String searchConceptId(String canonicalName) {
        return CONCEPT_ID;
    }

    @Override
    public String searchIndividualId(String canoniclName) {
        return INDIVIDUAL_ID;
    }

    @Override
    public String searchRelationId(String canonicalName) {
        return RELATION_ID;
    }
}

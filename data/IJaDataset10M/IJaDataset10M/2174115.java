package uk.co.ordnancesurvey.rabbitparser.testkit.fixture;

import uk.co.ordnancesurvey.rabbitparser.exception.RabbitRuntimeException;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.importsbasic.IParsedLabel;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.importsbasic.IParsedURL;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.importsbasic.IParsedUrlRef;
import uk.co.ordnancesurvey.rabbitparser.testkit.RabbitParserTestObjectFactory;

/**
 * Provides fixtures for {@link IParsedUrlRef}s
 * 
 * @author rdenaux
 * 
 */
public class ParsedRefFixture {

    public enum Ids {

        Buildings
    }

    private RabbitParserTestObjectFactory factory = new RabbitParserTestObjectFactory();

    public ParsedRefFixture() {
    }

    public IParsedUrlRef getFixture(Ids aIds) {
        IParsedUrlRef result = null;
        switch(aIds) {
            case Buildings:
                IParsedLabel label = factory.createTestLabel("Buildings");
                IParsedURL url = factory.createTestParsedURL("http://www.comp.leeds.ac.uk/confluence/testOntologies/BuildingsAndPlaces.rbt");
                result = factory.createTestUrlRef(label, url);
                break;
            default:
                throw new RabbitRuntimeException("Illegal id " + aIds);
        }
        return result;
    }
}

package data.riksdagen.se;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Unmarshaller;
import data.riksdagen.se.votering.Votering;

/**
 * The Class UnmarshallBallotXmlTest.
 */
public class UnmarshallBallotXmlTest extends AbstractRiksdagenXmlTest<Votering> {

    /** The unmarshaller. */
    @Autowired
    @Qualifier("riksdagenBallotMarshaller")
    private Unmarshaller unmarshaller;

    /**
	 * Instantiates a new unmarshall ballot xml test.
	 */
    public UnmarshallBallotXmlTest() {
        super();
    }

    /**
	 * Unmarshall ballot xml test.
	 * 
	 * @throws Exception
	 *             the exception
	 */
    @Test
    public void unmarshallBallotXmlTest() throws Exception {
        Assert.assertNotNull(this.unmarshallXml(unmarshaller, "src/main/resources/votering.xml"));
    }
}

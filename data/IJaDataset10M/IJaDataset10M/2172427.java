package mil.army.usace.ehlschlaeger.digitalpopulations.test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.xml.bind.JAXBException;
import mil.army.usace.ehlschlaeger.digitalpopulations.PumsHousehold;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.DataPreparer;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.filerelationship.FileRelationship;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class DataPreparerTest {

    /**
     * Test our funky probabilistic rounding.
     * 
     * @throws IOException loading project files
     * @throws JAXBException parsing project files
     * @throws SAXException validating XML
     * 
     * @see DataPreparer.parseInt
     */
    @Test
    public void testParseInt() throws IOException, JAXBException, SAXException {
        File file = new File("test-files/miniproj/relationship.dprxml");
        FileRelationship rel = FileRelationship.loadFile(file);
        DataPreparer dp = new DataPreparer(rel, file.getParentFile());
        dp.setRandomSource(new Random(123456));
        List<PumsHousehold> hohs = dp.loadPumsHouseholds();
        PumsHousehold testHoh = hohs.get(1);
        int[] goodAttribs = new int[] { 0, 2, -5, 4, 4, 5, 4, 5, 5, -999999, 2, 1, 2, -5, -4, -4, -4, -5, -5, -5, -4, -5, -5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        PumsHousehold goodHoh = new PumsHousehold(testHoh.getSchema(), goodAttribs, "2");
        Assert.assertEquals(goodHoh, testHoh);
    }
}

package group;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander Schindler
 */
public class GetMembers_Test {

    public GetMembers_Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test_validate_group_getMembers() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            File schemaLocation = new File("tes.xsd");
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=group.getmembers&api_key=b25b959554ed76058ac220b7b2e0a026&group=mnml");
            InputStream inputStream = url.openStream();
            Source source = new StreamSource(inputStream);
            validator.validate(source);
        } catch (IOException ex) {
            Logger.getLogger(GetMembers_Test.class.getName()).log(Level.SEVERE, null, ex);
            assertFalse("File not found", true);
        } catch (SAXException ex) {
            Logger.getLogger(GetMembers_Test.class.getName()).log(Level.SEVERE, null, ex);
            assertFalse("Schema did not validate", true);
        }
        assertTrue(true);
    }
}

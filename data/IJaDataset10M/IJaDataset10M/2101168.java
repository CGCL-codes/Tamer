package verinec.util;

import java.io.StringReader;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import verinec.VerinecException;

/** Testcase for the Schema Validator
 * @author Dominik Jungo, David Buchmann
 * @version $Revision: 47 $
 */
public class SchemaValidatorTest extends SchemaValidatorBase {

    private SAXBuilder builder;

    /** Initialize builder and validator
     * @throws Exception if validator can not be built.
     */
    public void setUp() throws Exception {
        super.setUp();
        builder = new SAXBuilder();
    }

    private String validxml = "<nodes " + VerinecNamespaces.schemaLocationAttrString + " xmlns='" + VerinecNamespaces.SCHEMA_NODE + "' />";

    private String invalidxml = "<brzff " + VerinecNamespaces.schemaLocationAttrString + " xmlns='" + VerinecNamespaces.SCHEMA_NODE + "' />";

    /**
     * Test the behaviour of the Valiadator with a "static" test case
     * @throws VerinecException If unexpected exception is thrown 
     */
    public void testValidate() throws VerinecException {
        Document d = null;
        try {
            d = builder.build(new StringReader(validxml));
        } catch (Throwable t) {
            fail("Could not create jdom: " + t.toString());
        }
        if (!validator.validate(d)) {
            fail("Validate failed " + validator.getLastError().toString());
        }
    }

    /** 
     * Test behaviour of validator with invalid xml.
     * @throws VerinecException If unexcepted event occurs.
     */
    public void testInvalid() throws VerinecException {
        Document d = null;
        try {
            d = builder.build(new StringReader(invalidxml));
        } catch (Throwable t) {
            fail("Could not create jdom: " + t.toString());
        }
        if (validator.validate(d)) {
            fail("Validated invalid document.");
        }
    }

    /** Test behaviour with null instead of document.
     * 
     */
    public void testNull() {
        try {
            validator.validate((Document) null);
            fail("No exception thrown if document to validate is null");
        } catch (VerinecException e) {
        }
    }
}

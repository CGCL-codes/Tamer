package fedora.server.test;

import junit.framework.TestCase;
import fedora.server.validation.DOValidatorXMLSchema;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * <p><b>Title:</b> ValidateXMLSchemaTest.java</p>
 * <p><b>Description:</b></p>
 *
 * @author payette@cs.cornell.edu
 * @version $Id: ValidateXMLSchemaTest.java 3966 2005-04-21 13:33:01Z rlw $
 */
public class ValidateXMLSchemaTest extends TestCase {

    protected String inFile = null;

    protected String inXMLSchemaFile = null;

    protected String tempdir = null;

    protected void setUp() {
        tempdir = "TestValidation";
        inFile = "TestValidation/foxml-bdef.xml";
        inXMLSchemaFile = "src/xsd/foxml1-0.xsd";
        FileInputStream in = null;
        try {
            in = new FileInputStream(new File(inFile));
        } catch (IOException ioe) {
            System.out.println("Error on XML file inputstream: " + ioe.getMessage());
            ioe.printStackTrace();
        }
        try {
            DOValidatorXMLSchema dov = new DOValidatorXMLSchema(inXMLSchemaFile);
            dov.validate(in);
        } catch (Exception e) {
            System.out.println("Error: (" + e.getClass().getName() + "):" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void testFoo() {
    }
}

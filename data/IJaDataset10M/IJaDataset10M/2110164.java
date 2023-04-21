package org.qedeq.kernel.xml.schema;

import java.io.File;
import org.qedeq.base.test.QedeqTestCase;

/**
 * This class tests the XML schema files under <code>org.qedeq.kernel.xml.schema</code>.
 *
 * @version $Revision: 1.1 $
 * @author    Michael Meyling
 */
public class SchemaTest extends QedeqTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test XSLT transformation.
     *
     * @throws  Exception   Something bad happened.
     */
    public void testXml() throws Exception {
        try {
            org.apache.xalan.xslt.Process.main(new String[] { "-IN", getFile("qedeq.xml").toString(), "-XSL", getFile("qedeq.xsl").toString(), "-OUT", (new File(getOutdir(), "qedeq.html")).toString(), "-HTML" });
        } catch (RuntimeException t) {
            t.printStackTrace();
            throw t;
        } catch (Error t) {
            t.printStackTrace();
            throw t;
        }
    }
}

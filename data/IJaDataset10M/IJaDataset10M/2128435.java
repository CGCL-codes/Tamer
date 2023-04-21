package com.misyshealthcare.connect.ihe;

import junit.framework.TestCase;
import com.misyshealthcare.connect.base.*;
import com.misyshealthcare.connect.base.codemapping.CodeMappingManager;
import com.misyshealthcare.connect.ihe.configuration.ConfigurationLoader;
import com.misyshealthcare.connect.ihe.configuration.IheConfigurationException;
import com.misyshealthcare.connect.util.LibraryConfig.ILogContext;
import com.misyshealthcare.connect.util.OID;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.net.URL;
import java.io.File;

/**
 * Test XDS Document Consumer
 *
 * @author Wenzhi Li
 * @version 2.0, Jan 2, 2007
 */
public class XdsDocumentConsumerTest extends TestCase {

    public void setUp() throws Exception {
        ConfigurationLoader loader = ConfigurationLoader.getInstance();
        loader = ConfigurationLoader.getInstance();
        try {
            URL url = XdsDocumentConsumerTest.class.getResource("/config/connectathon/IheActors.xml");
            File file = new File(url.toURI());
            loader.loadConfiguration(file, false, "2.16.840.1.113883.3.65.2", new OidMock(), null, null, CodeMappingManager.getInstance(), new TestLogContext());
            Collection actors = new ArrayList<String>();
            actors.add("xds_q_nist");
            loader.resetConfiguration(actors, "c:\\testlog.xml");
        } catch (IheConfigurationException e) {
            e.printStackTrace();
            fail("Cannot load the actor property");
        }
    }

    public void testQueryFormatCode() {
        DocumentQuery query = new DocumentQuery();
        PatientID pid = new PatientID();
        pid.addId("1.3.6.1.4.1.21367.2005.3.7&ISO", "7f6c3557474f4ba");
        query.setPatientId(pid);
        List<Document> docs = DocumentBroker.getInstance().findDocuments(query);
        assertTrue(docs.size() >= 1);
    }

    public class OidMock implements OID.OidSource {

        public synchronized String generateId() {
            return Long.toString(System.currentTimeMillis());
        }
    }
}

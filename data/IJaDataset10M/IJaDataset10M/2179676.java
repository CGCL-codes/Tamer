package at.ac.univie.mminf.oai2lod;

import at.ac.univie.mminf.oai2lod.oai.OAIHarvestingJobConfiguration;
import junit.framework.TestCase;

public class OAI2LODServerTest extends TestCase {

    public OAI2LODServerTest(String msg) throws Exception {
        super(msg);
    }

    public void testServerConfig() throws Exception {
        String configFile = "test-config1.n3";
        OAI2LODServer server = OAI2LODServer.instance();
        server.setConfigFile(configFile);
        assertEquals("http://localhost:2020/", server.getBaseURI());
        assertEquals(2020, server.getPort());
        assertEquals("Example OAI2LOD Server", server.getServerName());
        OAIHarvestingJobConfiguration oai_config = server.getOAIConfiguration();
        assertEquals("http://www.infomotions.com/gallery/oai/index.pl", oai_config.getServerURL());
        assertEquals("oai_dc", oai_config.getMetadataPrefix());
        assertEquals("xsl/oai_dc2rdf_xml.xsl", oai_config.getStyleSheet());
        assertEquals(100, oai_config.getMaxRecords());
    }
}

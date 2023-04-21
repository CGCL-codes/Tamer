package mgmt;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.tridentproject.repository.fedora.mgmt.*;

public class testMAPImpl {

    private RepositoryClient rc;

    private RepositoryFactory rf;

    private String strHost;

    private String strPort;

    private String strUrl;

    private String strUser;

    private String strPass;

    private MAP pmap;

    private static Logger log = Logger.getLogger(testMAPImpl.class);

    @Before
    public void init() {
        Properties props = System.getProperties();
        strHost = props.getProperty("host", "");
        strPort = props.getProperty("port", "");
        strUrl = props.getProperty("url", "");
        strUser = props.getProperty("user", "");
        strPass = props.getProperty("pass", "");
        PropertyConfigurator.configure(props.getProperty("log4j.configuration"));
        log.debug("log4j init complete");
        try {
            rc = new RepositoryClient(strHost, strPort, strUrl, strUser, strPass);
            rf = new RepositoryFactory();
            pmap = rf.newMAP(rc, "duke-map:test");
        } catch (FedoraAPIException e) {
            log.debug(e.getMessage());
        }
    }

    @Test
    public void TestMetadataFormDefinition() {
        MetadataFormDefinition mfd = null;
        try {
            mfd = pmap.getMetadataFormDefinition();
        } catch (Exception e) {
            Assert.fail("failure during adding component: " + e.getMessage());
        }
        Assert.assertNotNull(mfd);
    }

    @Test(expected = FedoraAPIException.class)
    public void TestGetNonExistentMAP() throws FedoraAPIException {
        MAP bogusMAP = rf.getMAP(rc, "foo");
    }

    @After
    public void cleanup() {
        try {
            if (pmap != null) pmap.purge();
        } catch (Exception e) {
        }
    }
}

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

public class testItemImpl {

    private RepositoryClient rc;

    private String strHost;

    private String strPort;

    private String strUrl;

    private String strUser;

    private String strPass;

    private Item pitem;

    private static Logger log = Logger.getLogger(testItemImpl.class);

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
            pitem = new ItemImpl(rc);
            pitem.createDataObject();
            log.debug("persistent pid: " + pitem.getPid());
        } catch (FedoraAPIException e) {
        }
    }

    @Test
    public void TestCreateDataObject() {
        String pid = null;
        try {
            Item dobj = new ItemImpl(rc);
            dobj.createDataObject();
            pid = dobj.getPid();
            log.debug(pid);
        } catch (FedoraAPIException e) {
            Assert.fail("failure during data object creation: " + e.getMessage());
        }
        Assert.assertNotNull(pid);
    }

    @Test
    public void TestSetStatus() {
        String newStatus = "Yahoo";
        String returnedStatus = null;
        try {
            pitem.setStatus(newStatus);
            returnedStatus = pitem.getStatus();
        } catch (FedoraAPIException e) {
            Assert.fail("unable to either get or set status: " + e.getMessage());
        }
        Assert.assertEquals(newStatus, returnedStatus);
    }

    @Test
    public void TestDeleteItem() {
        String strStatus = null;
        try {
            pitem.delete();
            strStatus = pitem.getStatus();
        } catch (FedoraAPIException e) {
            Assert.fail("unable to either delete the item or get the item's status: " + e.getMessage());
        }
        Assert.assertEquals(strStatus, "Deleted");
    }

    @Test
    public void TestHasDMDService() {
        Document testDoc = null;
        DMD dmd = null;
        boolean test = false;
        try {
            log.debug("about to add dmd to " + pitem.getPid());
            InputStream is = new FileInputStream("build-test/duke_1_dukecore.xml");
            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);
            dmd = new DukeCore(doc);
            log.debug("DMD: " + dmd.toString());
            pitem.setDMD(dmd);
            test = pitem.hasDMDService();
        } catch (Exception e) {
            org.junit.Assert.fail("Exception: " + e.getMessage());
        }
        Assert.assertTrue(!test);
    }

    @Test
    public void TestEditGroups() {
        List<String> lEditGroups = new ArrayList<String>();
        lEditGroups.add("spec_coll");
        List<String> testEditGroups = null;
        try {
            pitem.setEditGroups(lEditGroups);
            testEditGroups = pitem.getEditGroups();
        } catch (FedoraAPIException e) {
            org.junit.Assert.fail("Exception: " + e.getMessage());
        }
        String strEditGroup = testEditGroups.get(0);
        Assert.assertTrue(strEditGroup != null && strEditGroup.equals("spec_coll"));
    }

    @After
    public void PurgePitem() {
        try {
            pitem.purge();
        } catch (Exception e) {
        }
    }
}

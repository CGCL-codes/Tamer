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

public class testDukeCore {

    private static Logger log = Logger.getLogger(testDukeCore.class);

    @Test
    public void TestDukeCoreConstructor() {
        DMD dmd = new DukeCore();
        Assert.assertNotNull(dmd);
    }

    @Test
    public void TestCreateDMD() {
        DMD dmd = new DukeCore();
        try {
            dmd.createDMD();
        } catch (FedoraAPIException e) {
            Assert.fail("error creating DMD: " + e.getMessage());
        }
        Document doc = dmd.getDMD();
        Assert.assertNotNull(doc);
    }

    @Test
    public void TestValidation() {
        SAXReader reader = new SAXReader();
        InputStream is;
        Document doc;
        DMD dmd = null;
        try {
            is = new FileInputStream("build-test/duke_1_dukecore.xml");
            doc = reader.read(is);
            dmd = new DukeCore(doc);
        } catch (Exception e) {
            org.junit.Assert.fail("Exception: " + e.getMessage());
        }
        ValidationRule rule = new ValidationRule("/duke:core/dc:bogus", "You got bit by the bogus error", "bogus");
        List rules = new ArrayList();
        rules.add(rule);
        List errors = dmd.validate(rules);
        org.junit.Assert.assertTrue(((ValidationRule) errors.get(0)).getFieldGroup().equals("bogus"));
    }

    @Test
    public void TestValidationValid() {
        SAXReader reader = new SAXReader();
        InputStream is;
        Document doc;
        DMD dmd = null;
        try {
            is = new FileInputStream("build-test/duke_1_dukecore.xml");
            doc = reader.read(is);
            dmd = new DukeCore(doc);
        } catch (Exception e) {
            org.junit.Assert.fail("Exception: " + e.getMessage());
        }
        ValidationRule rule = new ValidationRule("/duke:core/dc:title", "Title field is required", "title");
        List rules = new ArrayList();
        rules.add(rule);
        List errors = dmd.validate(rules);
        org.junit.Assert.assertTrue(errors.isEmpty());
    }
}

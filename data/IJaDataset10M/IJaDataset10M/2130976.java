package zcatalog.fs;

import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.xml.bind.Marshaller;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import zcatalog.db.ZCatObject;
import zcatalog.ui.IconSize;
import zcatalog.xml.jaxb.*;
import zcatalog.xml.*;
import zcatalog.*;

/**
 *
 * @author lex
 */
public class ZCatFolderTest {

    ZCatFolder zcatFolder;

    FSFolder folder;

    public ZCatFolderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        folder = new FSFolder();
        folder.setName("Directory");
        folder.setFileCount(new FileCount());
        folder.setModTime(MiscUtils.toXMLGregorianCal(System.currentTimeMillis()));
        folder.setDu(1024);
        zcatFolder = new ZCatFolder(folder, new ZCatObject[0]);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of hasContainer method, of class ZCatFolder.
     */
    @Test
    public void testHasContainer() {
        System.out.println("hasContainer");
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContainersCount method, of class ZCatFolder.
     */
    @Test
    public void testGetContainersCount() {
        System.out.println("getContainersCount");
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXMLSchema method, of class ZCatFolder.
     */
    @Test
    public void testGetXMLSchema() {
        System.out.println("getXMLSchema");
        assertEquals(XMLResources.load("FSFolder.xsd"), zcatFolder.getXMLSchema());
    }

    /**
     * Test of prepareForStorage method, of class ZCatFolder.
     */
    @Test
    public void testPrepareForStorage() {
        System.out.println("prepareForStorage");
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChildrenCount method, of class ZCatFolder.
     */
    @Test
    public void testGetChildrenCount() {
        System.out.println("getChildrenCount");
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChild method, of class ZCatFolder.
     */
    @Test
    public void testGetChild() {
        System.out.println("getChild");
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIndexOfChild method, of class ZCatFolder.
     */
    @Test
    public void testGetIndexOfChild() {
        System.out.println("getIndexOfChild");
        fail("The test case is a prototype.");
    }

    /**
     * Test of putFoldersFirst method, of class ZCatFolder.
     */
    @Test
    public void testPutFoldersFirst() {
        System.out.println("putFoldersFirst");
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIcon method, of class ZCatFolder.
     */
    @Test
    public void testGetIcon() {
        System.out.println("getIcon");
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMarshaller method, of class ZCatFolder.
     */
    @Test
    public void testGetMarshaller() throws Exception {
        System.out.println("getMarshaller");
        fail("The test case is a prototype.");
    }

    /**
     * Test of getJAXBObject method, of class ZCatFolder.
     */
    @Test
    public void testGetJAXBObject() {
        System.out.println("getJAXBObject");
        fail("The test case is a prototype.");
    }
}

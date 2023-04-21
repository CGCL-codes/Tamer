package org.exist.xmldb;

import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.exist.security.Account;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.exist.util.XMLFilenameFilter;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;
import static org.exist.xmldb.XmldbLocalTests.*;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

public class ResourceTest {

    private static final String TEST_COLLECTION = "testResource";

    @Test
    public void readNonExistingResource() throws XMLDBException {
        Collection testCollection = DatabaseManager.getCollection(ROOT_URI + "/" + TEST_COLLECTION);
        assertNotNull(testCollection);
        Resource nonExistent = testCollection.getResource("12345.xml");
        assertNull(nonExistent);
    }

    @Test
    public void readResource() throws XMLDBException {
        Collection testCollection = DatabaseManager.getCollection(ROOT_URI + "/" + TEST_COLLECTION);
        assertNotNull(testCollection);
        String[] resources = testCollection.listResources();
        assertEquals(resources.length, testCollection.getResourceCount());
        System.out.println("reading " + resources[0]);
        XMLResource doc = (XMLResource) testCollection.getResource(resources[0]);
        assertNotNull(doc);
        System.out.println("testing XMLResource.getContentAsSAX()");
        StringWriter sout = new StringWriter();
        OutputFormat format = new OutputFormat("xml", "ISO-8859-1", true);
        format.setLineWidth(60);
        XMLSerializer xmlout = new XMLSerializer(sout, format);
        doc.getContentAsSAX(xmlout);
        System.out.println("----------------------------------------");
        System.out.println(sout.toString());
        System.out.println("----------------------------------------");
    }

    @Test
    public void readDOM() throws XMLDBException {
        Collection testCollection = DatabaseManager.getCollection(ROOT_URI + "/" + TEST_COLLECTION);
        assertNotNull(testCollection);
        XMLResource doc = (XMLResource) testCollection.getResource("r_and_j.xml");
        assertNotNull(doc);
        Node n = doc.getContentAsDOM();
        Element elem = null;
        if (n instanceof Element) {
            elem = (Element) n;
        } else if (n instanceof Document) {
            elem = ((Document) n).getDocumentElement();
        }
        assertNotNull(elem);
        assertEquals(elem.getNodeName(), "PLAY");
        System.out.println("Root element: " + elem.getNodeName());
        NodeList children = elem.getChildNodes();
        Node node;
        for (int i = 0; i < children.getLength(); i++) {
            node = children.item(i);
            System.out.println("Child: " + node.getNodeName());
            assertNotNull(node);
            node = node.getFirstChild();
            while (node != null) {
                System.out.println("child: " + node.getNodeName());
                node = node.getNextSibling();
            }
        }
    }

    @Test
    public void setContentAsSAX() throws SAXException, ParserConfigurationException, XMLDBException, IOException {
        Collection testCollection = DatabaseManager.getCollection(ROOT_URI + "/" + TEST_COLLECTION);
        assertNotNull(testCollection);
        XMLResource doc = (XMLResource) testCollection.createResource("test.xml", "XMLResource");
        String xml = "<test><title>Title</title>" + "<para>Paragraph1</para>" + "<para>Paragraph2</para>" + "</test>";
        ContentHandler handler = doc.setContentAsSAX();
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        saxFactory.setNamespaceAware(true);
        saxFactory.setValidating(false);
        SAXParser sax = saxFactory.newSAXParser();
        XMLReader reader = sax.getXMLReader();
        reader.setContentHandler(handler);
        reader.parse(new InputSource(new StringReader(xml)));
        testCollection.storeResource(doc);
    }

    @Test
    public void setContentAsDOM() throws XMLDBException, ParserConfigurationException, SAXException, IOException {
        Collection testCollection = DatabaseManager.getCollection(ROOT_URI + "/" + TEST_COLLECTION);
        assertNotNull(testCollection);
        XMLResource doc = (XMLResource) testCollection.createResource("dom.xml", "XMLResource");
        String xml = "<test><title>Title</title>" + "<para>Paragraph1</para>" + "<para>Paragraph2</para>" + "</test>";
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = docFactory.newDocumentBuilder();
        Document dom = builder.parse(new InputSource(new StringReader(xml)));
        doc.setContentAsDOM(dom.getDocumentElement());
        testCollection.storeResource(doc);
    }

    @Test
    public void setContentAsSource_Reader() throws XMLDBException, SAXException, IOException, XpathException {
        Collection testCollection = DatabaseManager.getCollection(ROOT_URI + "/" + TEST_COLLECTION);
        assertNotNull(testCollection);
        XMLResource doc = (XMLResource) testCollection.createResource("source.xml", "XMLResource");
        final String xml = "<test><title>Title1</title>" + "<para>Paragraph3</para>" + "<para>Paragraph4</para>" + "</test>";
        doc.setContent(new InputSource(new StringReader(xml)));
        testCollection.storeResource(doc);
        XMLResource newDoc = (XMLResource) testCollection.getResource("source.xml");
        String newDocXml = (String) newDoc.getContent();
        assertXpathEvaluatesTo("Title1", "/test/title/text()", newDocXml);
        assertXpathEvaluatesTo("2", "count(/test/para)", newDocXml);
        assertXpathEvaluatesTo("Paragraph3", "/test/para[1]/text()", newDocXml);
        assertXpathEvaluatesTo("Paragraph4", "/test/para[2]/text()", newDocXml);
    }

    @Test
    public void queryRemoveResource() throws XMLDBException {
        Resource resource = null;
        Collection testCollection = DatabaseManager.getCollection(ROOT_URI + "/" + TEST_COLLECTION);
        assertNotNull(testCollection);
        String resourceName = "QueryTestPerson.xml";
        String id = "test." + System.currentTimeMillis();
        String content = "<?xml version='1.0'?><person id=\"" + id + "\"><name>Jason</name></person>";
        resource = testCollection.createResource(resourceName, "XMLResource");
        resource.setContent(content);
        testCollection.storeResource(resource);
        XPathQueryService service = (XPathQueryService) testCollection.getService("XPathQueryService", "1.0");
        ResourceSet rs = service.query("/person[@id='" + id + "']");
        for (ResourceIterator iterator = rs.getIterator(); iterator.hasMoreResources(); ) {
            Resource r = iterator.nextResource();
            System.err.println("Resource id=" + r.getId() + " xml=" + r.getContent());
            testCollection.removeResource(r);
            resource = null;
        }
    }

    @Test
    public void addRemove() throws XMLDBException {
        final String resourceID = "addremove.xml";
        XMLResource created = addResource(resourceID, xmlForTest());
        assertNotNull(created);
        XMLResource located = resourceForId(resourceID);
        assertNotNull(located);
        removeDocument(resourceID);
        XMLResource locatedAfterRemove = resourceForId(resourceID);
        assertNull(locatedAfterRemove);
    }

    @Test
    public void addRemoveAddWithIds() throws XMLDBException {
        final String resourceID = "removeWithIds;1.xml";
        addResource(resourceID, "<foo1 xml:id='f'/>");
        removeDocument(resourceID);
        addResource(resourceID, "<foo xml:id='f'/>");
    }

    private void removeDocument(String id) throws XMLDBException {
        XMLResource resource = resourceForId(id);
        if (null != resource) {
            Collection collection = null;
            try {
                collection = DatabaseManager.getCollection(ROOT_URI + "/" + TEST_COLLECTION);
                collection.removeResource(resource);
            } finally {
                closeCollection(collection);
            }
        }
    }

    private XMLResource addResource(String id, String content) throws XMLDBException {
        Collection collection = null;
        XMLResource result = null;
        try {
            collection = DatabaseManager.getCollection(ROOT_URI + "/" + TEST_COLLECTION);
            result = (XMLResource) collection.createResource(id, XMLResource.RESOURCE_TYPE);
            result.setContent(content);
            collection.storeResource(result);
        } finally {
            closeCollection(collection);
        }
        return result;
    }

    private XMLResource resourceForId(String id) throws XMLDBException {
        Collection collection = null;
        XMLResource result = null;
        try {
            collection = DatabaseManager.getCollection(ROOT_URI + "/" + TEST_COLLECTION);
            result = (XMLResource) collection.getResource(id);
        } finally {
            closeCollection(collection);
        }
        return result;
    }

    private void closeCollection(Collection collection) throws XMLDBException {
        if (null != collection) {
            collection.close();
        }
    }

    private String xmlForTest() {
        return "<test><title>Title</title>" + "<para>Paragraph1</para>" + "<para>Paragraph2</para>" + "</test>";
    }

    @BeforeClass
    public static void startDatabase() {
        try {
            Class<?> cl = Class.forName(DRIVER);
            Database database = (Database) cl.newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @AfterClass
    public static void stopDatabase() {
        try {
            Collection dbCol = DatabaseManager.getCollection(ROOT_URI, ADMIN_UID, ADMIN_PWD);
            DatabaseInstanceManager mgr = (DatabaseInstanceManager) dbCol.getService("DatabaseInstanceManager", "1.0");
            mgr.shutdown();
        } catch (XMLDBException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Before
    public void setUp() throws XMLDBException {
        Collection root = DatabaseManager.getCollection(ROOT_URI, ADMIN_UID, ADMIN_PWD);
        CollectionManagementService cms = (CollectionManagementService) root.getService("CollectionManagementService", "1.0");
        Collection testCollection = cms.createCollection(TEST_COLLECTION);
        UserManagementService ums = (UserManagementService) testCollection.getService("UserManagementService", "1.0");
        Account guest = ums.getAccount(GUEST_UID);
        ums.chown(guest, guest.getPrimaryGroup());
        ums.chmod("rwxr-xr-x");
        Collection testCollectionAsGuest = DatabaseManager.getCollection(ROOT_URI + "/" + TEST_COLLECTION);
        File files[] = getShakespeareSamplesDirectory().listFiles(new XMLFilenameFilter());
        for (File file : files) {
            XMLResource res = (XMLResource) testCollectionAsGuest.createResource(file.getName(), "XMLResource");
            res.setContent(file);
            testCollectionAsGuest.storeResource(res);
        }
    }

    @After
    public void tearDown() throws XMLDBException {
        Collection root = DatabaseManager.getCollection(ROOT_URI, ADMIN_UID, ADMIN_PWD);
        CollectionManagementService cms = (CollectionManagementService) root.getService("CollectionManagementService", "1.0");
        cms.removeCollection(TEST_COLLECTION);
    }
}

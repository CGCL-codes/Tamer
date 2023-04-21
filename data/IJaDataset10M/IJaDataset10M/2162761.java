package org.exist.storage.serializers;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import org.exist.Database;
import org.exist.EXistException;
import org.exist.Indexer;
import org.exist.TestUtils;
import org.exist.collections.Collection;
import org.exist.collections.IndexInfo;
import org.exist.dom.DefaultDocumentSet;
import org.exist.dom.DocumentImpl;
import org.exist.dom.DocumentSet;
import org.exist.dom.MutableDocumentSet;
import org.exist.storage.BrokerPool;
import org.exist.storage.DBBroker;
import org.exist.storage.txn.TransactionManager;
import org.exist.storage.txn.Txn;
import org.exist.test.TestConstants;
import org.exist.util.Configuration;
import org.exist.util.ConfigurationHelper;
import org.exist.util.serializer.SAXSerializer;
import org.exist.util.serializer.SerializerPool;
import org.exist.xmldb.XmldbURI;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author dmitriy
 * 
 */
public class DTDtests {

    static String xmlString = "<?xml version=\"1.0\"?>" + "  <!DOCTYPE address" + "  [" + "     <!ELEMENT address (buildingnumber, street, city, state, zip)>" + "     <!ATTLIST address xmlns CDATA #IMPLIED>" + "     <!ELEMENT buildingnumber (#PCDATA)>" + "     <!ELEMENT street (#PCDATA)>" + "     <!ELEMENT city (#PCDATA)>" + "     <!ELEMENT state (#PCDATA)>" + "     <!ELEMENT zip (#PCDATA)>" + "  ]>" + "  <address>" + "    <buildingnumber> 29 </buildingnumber>" + "    <street> South Street</street>" + "    <city>Vancouver</city>" + "    <state>BC</state>" + "    <zip>V6V 4U7</zip>" + "  </address>";

    static String xmlString2 = "<!DOCTYPE dmodule [ \n" + "<!ENTITY ICN-S1000DBIKE-AAA-DA23000-0-U8025-00532-A-04-1 SYSTEM \"DOCATO_ABSTRACT_ID_3055\" NDATA cgm>\n" + "<!NOTATION cgm PUBLIC \"-//USA-DOD//NOTATION Computer Graphics Metafile//EN\">]>\n" + "<test/>";

    @Test
    public void serializer() throws ParserConfigurationException, SAXException, IOException {
        store("testDTD.xml", xmlString);
        Database db = null;
        DBBroker broker = null;
        try {
            db = BrokerPool.getInstance();
            broker = db.get(null);
            DocumentImpl doc = root.getDocument(broker, XmldbURI.create("testDTD.xml"));
            SAXSerializer sax = null;
            Serializer serializer = broker.getSerializer();
            serializer.reset();
            try {
                sax = (SAXSerializer) SerializerPool.getInstance().borrowObject(SAXSerializer.class);
                Properties outputProps = new Properties();
                StringWriter writer = new StringWriter();
                sax.setOutput(writer, outputProps);
                serializer.setSAXHandlers(sax, sax);
                serializer.toSAX(doc);
                assertEquals("<!DOCTYPE address [<!ELEMENT address (buildingnumber,street,city,state,zip)><!ATTLIST address xmlns CDATA #IMPLIED><!ELEMENT buildingnumber (#PCDATA)><!ELEMENT street (#PCDATA)><!ELEMENT city (#PCDATA)><!ELEMENT state (#PCDATA)><!ELEMENT zip (#PCDATA)>]><address><buildingnumber> 29 </buildingnumber><street> South Street</street><city>Vancouver</city><state>BC</state><zip>V6V 4U7</zip></address>", writer.toString());
            } finally {
                if (sax != null) {
                    SerializerPool.getInstance().returnObject(sax);
                }
            }
        } catch (EXistException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (db != null) db.release(broker);
        }
        db = null;
        broker = null;
        stopDB(false);
        startDB();
        setup();
        try {
            db = BrokerPool.getInstance();
            broker = db.get(null);
            DocumentImpl doc = root.getDocument(broker, XmldbURI.create("testDTD.xml"));
            SAXSerializer sax = null;
            Serializer serializer = broker.getSerializer();
            serializer.reset();
            try {
                sax = (SAXSerializer) SerializerPool.getInstance().borrowObject(SAXSerializer.class);
                Properties outputProps = new Properties();
                StringWriter writer = new StringWriter();
                sax.setOutput(writer, outputProps);
                serializer.setSAXHandlers(sax, sax);
                serializer.toSAX(doc);
                assertEquals("<!DOCTYPE address [<!ELEMENT address (buildingnumber,street,city,state,zip)><!ATTLIST address xmlns CDATA #IMPLIED><!ELEMENT buildingnumber (#PCDATA)><!ELEMENT street (#PCDATA)><!ELEMENT city (#PCDATA)><!ELEMENT state (#PCDATA)><!ELEMENT zip (#PCDATA)>]><address><buildingnumber> 29 </buildingnumber><street> South Street</street><city>Vancouver</city><state>BC</state><zip>V6V 4U7</zip></address>", writer.toString());
            } finally {
                if (sax != null) {
                    SerializerPool.getInstance().returnObject(sax);
                }
            }
        } catch (EXistException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (db != null) db.release(broker);
        }
    }

    private DocumentSet store(String docName, String data) {
        DBBroker broker = null;
        TransactionManager transact = null;
        Txn transaction = null;
        MutableDocumentSet docs = new DefaultDocumentSet();
        try {
            broker = pool.get(pool.getSecurityManager().getSystemSubject());
            assertNotNull(broker);
            transact = pool.getTransactionManager();
            assertNotNull(transact);
            transaction = transact.beginTransaction();
            assertNotNull(transaction);
            IndexInfo info = root.validateXMLResource(transaction, broker, XmldbURI.create(docName), data);
            assertNotNull(info);
            root.store(transaction, broker, info, data, false);
            docs.add(info.getDocument());
            transact.commit(transaction);
        } catch (Exception e) {
            if (transact != null) transact.abort(transaction);
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            pool.release(broker);
        }
        return docs;
    }

    public void stopDB(boolean clean) {
        if (clean) TestUtils.cleanupDB();
        BrokerPool.stopAll(false);
        pool = null;
        root = null;
    }

    private static BrokerPool pool;

    private static Collection root;

    @Before
    public void setup() {
        DBBroker broker = null;
        TransactionManager transact = null;
        Txn transaction = null;
        try {
            broker = pool.get(pool.getSecurityManager().getSystemSubject());
            assertNotNull(broker);
            transact = pool.getTransactionManager();
            assertNotNull(transact);
            transaction = transact.beginTransaction();
            assertNotNull(transaction);
            root = broker.getOrCreateCollection(transaction, TestConstants.TEST_COLLECTION_URI);
            assertNotNull(root);
            broker.saveCollection(transaction, root);
            transact.commit(transaction);
        } catch (Exception e) {
            if (transact != null) transact.abort(transaction);
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (pool != null) pool.release(broker);
        }
    }

    @After
    public void cleanup() {
        BrokerPool pool = null;
        DBBroker broker = null;
        TransactionManager transact = null;
        Txn transaction = null;
        try {
            pool = BrokerPool.getInstance();
            assertNotNull(pool);
            broker = pool.get(pool.getSecurityManager().getSystemSubject());
            assertNotNull(broker);
            transact = pool.getTransactionManager();
            assertNotNull(transact);
            transaction = transact.beginTransaction();
            assertNotNull(transaction);
            if (root != null) {
                assertNotNull(root);
                broker.removeCollection(transaction, root);
            }
            transact.commit(transaction);
        } catch (Exception e) {
            if (transact != null) transact.abort(transaction);
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (pool != null) pool.release(broker);
        }
    }

    @BeforeClass
    public static void startDB() {
        try {
            File confFile = ConfigurationHelper.lookup("conf.xml");
            Configuration config = new Configuration(confFile.getAbsolutePath());
            config.setProperty(Indexer.PROPERTY_SUPPRESS_WHITESPACE, "none");
            config.setProperty(Indexer.PRESERVE_WS_MIXED_CONTENT_ATTRIBUTE, Boolean.TRUE);
            BrokerPool.configure(1, 5, config);
            pool = BrokerPool.getInstance();
            assertNotNull(pool);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @AfterClass
    public static void stopDB() {
        TestUtils.cleanupDB();
        BrokerPool.stopAll(false);
        pool = null;
        root = null;
    }
}

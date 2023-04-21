package org.dcm4chee.docstore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.activation.DataHandler;
import org.dcm4chee.docstore.spi.DocumentStorage;
import org.dcm4chee.docstore.test.DocStoreTestBase;
import org.dcm4chee.docstore.test.TestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentStorageListenerTest extends DocStoreTestBase {

    private static TestDocumentStorageListener listener;

    private DataHandler dummyDH = new DataHandler(TestUtil.DUMMY_PLAIN_DATA_SOURCE);

    private static Logger log = LoggerFactory.getLogger(TestDocumentStorageListener.class);

    public DocumentStorageListenerTest() throws IOException {
        super();
        initDocStore();
        initListener();
    }

    private void initListener() {
        if (listener == null) {
            listener = new TestDocumentStorageListener();
            docStore.addStorageListener(listener);
        }
    }

    protected void setUp() {
        listener.init();
    }

    protected void tearDown() throws Exception {
    }

    public void testDocumentStored() throws IOException {
        String uid1 = "test.event.stored.1.1";
        docStore.storeDocument(uid1, dummyDH);
        List l = listener.events[TestDocumentStorageListener.STORED];
        assertEquals("Wrong number of recorded STORED events!", 1, l.size());
        String uid2 = "test.event.stored.1.2";
        docStore.storeDocument(uid2, dummyDH);
        assertEquals("Wrong number of recorded STORED events!", 2, l.size());
    }

    public void testDocumentCreated() throws IOException {
        String uid1 = "test.event.created.1.1";
        docStore.createDocument(uid1, TestUtil.MIME_TEXT_PLAIN);
        List l = listener.events[TestDocumentStorageListener.CREATED];
        assertEquals("Wrong number of recorded CREATED events!", 1, l.size());
        String uid2 = "test.event.created.1.2";
        docStore.createDocument(uid2, TestUtil.MIME_TEXT_PLAIN);
        assertEquals("Wrong number of recorded CREATED events!", 2, l.size());
    }

    public void testDocumentDeleted() throws IOException {
        String uid1 = "test.event.deleted.1.1";
        String uid2 = "test.event.deleted.1.2";
        TestUtil.createDummyDocument(docStore, uid1, TestUtil.MIME_TEXT_PLAIN);
        TestUtil.createDummyDocument(docStore, uid2, TestUtil.MIME_TEXT_PLAIN);
        List l = listener.events[TestDocumentStorageListener.DELETED];
        assertEquals("Wrong number of recorded DELETED events!", 0, l.size());
        assertTrue("deleteDocument must return true for valid deletion!", docStore.deleteDocument(uid1));
        assertEquals("Wrong number of recorded DELETED events!", 1, l.size());
        assertTrue("deleteDocument must return true for valid deletion!", docStore.deleteDocument(uid2));
        assertEquals("Wrong number of recorded DELETED events!", 2, l.size());
        if (docStore.selectDocStorageFromPoolOrDomain(null).hasFeature(Feature.MULTI_MIME)) {
            listener.init();
            assertEquals("Internal test error! reset of event list failed!", 0, l.size());
            TestUtil.createDummyDocument(docStore, uid1, TestUtil.MIME_TEXT_PLAIN);
            TestUtil.createDummyDocument(docStore, uid1, TestUtil.MIME_TEXT_HTML);
            TestUtil.createDummyDocument(docStore, uid1, TestUtil.MIME_TEXT_XML);
            assertTrue("deleteDocument must return true for valid deletion!", docStore.deleteDocument(uid1));
            assertEquals("Wrong number of recorded DELETED events for multi Mime support!", 3, l.size());
        } else {
            log.info("********** SKIP Multimime Delete Notification Test! storage doesn't support MULTI_MIME! *******************");
        }
    }

    public void testDocumentRetrieved() throws IOException {
        String uid1 = "test.event.retrieved.1.1";
        String uid2 = "test.event.retrieved.1.2";
        TestUtil.createDummyDocument(docStore, uid1, TestUtil.MIME_TEXT_PLAIN);
        TestUtil.createDummyDocument(docStore, uid2, TestUtil.MIME_TEXT_PLAIN);
        List l = listener.events[TestDocumentStorageListener.RETRIEVED];
        assertEquals("Wrong number of recorded RETRIEVED events!", 0, l.size());
        docStore.getDocument(uid1, null);
        assertEquals("Wrong number of recorded RETRIEVED events!", 1, l.size());
        docStore.getDocument(uid2, null);
        assertEquals("Wrong number of recorded RETRIEVED events!", 2, l.size());
        docStore.getDocument(uid2, null);
        assertEquals("Wrong number of recorded RETRIEVED events!", 3, l.size());
        docStore.getAvailability(uid2);
        assertEquals("Wrong number of recorded RETRIEVED events after getAvailability!", 3, l.size());
    }

    public void testDocumentCommitted() throws IOException {
    }

    public void testStorageAvailabilityChanged() {
    }

    private void toggleAvailability(DocumentStorage st) {
        Availability avail = st.getStorageAvailability();
        TestUtil.setStorageAvailabilty(!avail.equals(Availability.ONLINE));
    }

    private final class TestDocumentStorageListener implements DocumentStorageListener {

        static final int CREATED = 0;

        static final int STORED = 1;

        static final int DELETED = 2;

        static final int COMMITTED = 3;

        static final int RETRIEVED = 4;

        static final int AVAILABILITY = 5;

        static final int MAX_IDX = 6;

        final String[] EVENT_APPREVIATION = new String[] { "CR", "S", "D", "CO", "R", "A" };

        List[] events;

        public TestDocumentStorageListener() {
            init();
        }

        protected void init() {
            if (events == null) {
                events = new ArrayList[MAX_IDX];
                for (int i = 0; i < MAX_IDX; i++) {
                    events[i] = new ArrayList();
                }
            } else {
                for (List l : events) {
                    l.clear();
                }
            }
        }

        public void documentCommitted(BaseDocument doc) {
            events[COMMITTED].add(doc);
        }

        public void documentCreated(BaseDocument doc) {
            events[CREATED].add(doc);
        }

        public void documentDeleted(BaseDocument doc) {
            log.info("Notification DELETED:" + doc);
            events[DELETED].add(doc);
        }

        public void documentRetrieved(BaseDocument doc) {
            events[RETRIEVED].add(doc);
        }

        public void documentStored(BaseDocument doc) {
            events[STORED].add(doc);
        }

        public void storageAvailabilityChanged(DocumentStorage docStore, Availability oldAvail, Availability newAvail) {
            events[AVAILABILITY].add(new Object[] { docStore, oldAvail, newAvail });
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("TestDocumentStorageListener (");
            for (int i = 0; i < MAX_IDX; i++) {
                sb.append(EVENT_APPREVIATION[i]).append(':').append(events[i].size()).append(' ');
            }
            sb.append(')');
            return sb.toString();
        }
    }
}

package it.pronetics.madstore.repository.tasks;

import it.pronetics.madstore.common.AtomConstants;
import it.pronetics.madstore.repository.CollectionRepository;
import it.pronetics.madstore.repository.EntryRepository;
import it.pronetics.madstore.repository.test.util.Utils;
import java.util.List;
import org.custommonkey.xmlunit.XMLUnit;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.w3c.dom.Element;

public class CleanRepositoryHistoryTaskTest extends AbstractDependencyInjectionSpringContextTests {

    private static final String FOO_COLLECTION_XML = "fooCollection.xml";

    private static final String ENTRY1_XML = "entry1.xml";

    private static final String ENTRY2_XML = "entry2.xml";

    private static final String ENTRY3_XML = "entry3.xml";

    private static final String ENTRY4_XML = "entry4.xml";

    private static final String ENTRY5_XML = "entry5.xml";

    private CollectionRepository collectionRepository;

    private EntryRepository entryRepository;

    private CleanRepositoryHistoryTask cleanHistoryTask;

    static {
        XMLUnit.setIgnoreWhitespace(true);
    }

    public CleanRepositoryHistoryTaskTest() {
        setAutowireMode(AUTOWIRE_BY_TYPE);
    }

    @Override
    protected void onTearDown() throws Exception {
        List<Element> collections = collectionRepository.readCollections();
        for (Element collection : collections) {
            String collectionKey = collection.getAttribute(AtomConstants.ATOM_KEY);
            List<Element> elements = entryRepository.readEntries(collectionKey);
            for (Element element : elements) {
                String entryKey = element.getAttribute(AtomConstants.ATOM_KEY);
                entryRepository.delete(collectionKey, entryKey);
            }
            collectionRepository.delete(collectionKey);
        }
    }

    public void testPutAndReadEntriesAfterCleanRepositoryHistoryTaskExecution() throws Exception {
        Element sourceCollectionElement = Utils.getDoc(FOO_COLLECTION_XML).getDocumentElement();
        Element entryElement1 = Utils.getDoc(ENTRY1_XML).getDocumentElement();
        Element entryElement2 = Utils.getDoc(ENTRY2_XML).getDocumentElement();
        Element entryElement3 = Utils.getDoc(ENTRY3_XML).getDocumentElement();
        Element entryElement4 = Utils.getDoc(ENTRY4_XML).getDocumentElement();
        Element entryElement5 = Utils.getDoc(ENTRY5_XML).getDocumentElement();
        String collectionKey = collectionRepository.putIfAbsent(sourceCollectionElement);
        assertNotNull(collectionKey);
        String entryKey1 = entryRepository.put(collectionKey, entryElement1);
        assertNotNull(entryKey1);
        String entryKey2 = entryRepository.put(collectionKey, entryElement2);
        assertNotNull(entryKey2);
        String entryKey3 = entryRepository.put(collectionKey, entryElement3);
        assertNotNull(entryKey3);
        String entryKey4 = entryRepository.put(collectionKey, entryElement4);
        assertNotNull(entryKey4);
        String entryKey5 = entryRepository.put(collectionKey, entryElement5);
        assertNotNull(entryKey5);
        cleanHistoryTask.clean();
        Thread.sleep(10000);
        List<Element> elements = entryRepository.readEntries(collectionKey);
        assertEquals(3, elements.size());
        assertEquals("entry1", elements.get(0).getAttribute(AtomConstants.ATOM_KEY));
        assertEquals("entry2", elements.get(1).getAttribute(AtomConstants.ATOM_KEY));
        assertEquals("entry3", elements.get(2).getAttribute(AtomConstants.ATOM_KEY));
        entryRepository.delete(collectionKey, entryKey1);
        entryRepository.delete(collectionKey, entryKey2);
        entryRepository.delete(collectionKey, entryKey3);
        assertEquals(0, entryRepository.readEntries(collectionKey).size());
    }

    public void setCollectionRepository(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public void setEntryRepository(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public void setCleanHistoryTask(CleanRepositoryHistoryTask cleanHistoryTask) {
        this.cleanHistoryTask = cleanHistoryTask;
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "classpath:repositoryApplicationContext.xml" };
    }
}

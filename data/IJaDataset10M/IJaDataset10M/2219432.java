package net.sf.joafip.service.bug.java.util;

import java.util.Iterator;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestException;
import net.sf.joafip.entity.EnumFilePersistenceCloseAction;
import net.sf.joafip.java.util.PLinkedList;
import net.sf.joafip.service.FilePersistenceClassNotFoundException;
import net.sf.joafip.service.FilePersistenceDataCorruptedException;
import net.sf.joafip.service.FilePersistenceException;
import net.sf.joafip.service.FilePersistenceInvalidClassException;
import net.sf.joafip.service.FilePersistenceNotSerializableException;
import net.sf.joafip.service.FilePersistenceTooBigForSerializationException;
import net.sf.joafip.service.bug.AbstractFileCheckPersistenceForTest;

@NotStorableClass
@StorableAccess
public class TestPLinkedListIterator extends AbstractFileCheckPersistenceForTest {

    public TestPLinkedListIterator() throws TestException {
        super();
    }

    public TestPLinkedListIterator(final String name) throws TestException {
        super(name);
    }

    public void testAddRemove1() throws FilePersistenceException, FilePersistenceClassNotFoundException, FilePersistenceInvalidClassException, FilePersistenceDataCorruptedException, FilePersistenceNotSerializableException, FilePersistenceTooBigForSerializationException {
        PLinkedList<String> linkedList = new PLinkedList<String>();
        linkedList.add("a");
        linkedList.add("b");
        linkedList.add("c");
        linkedList.add("d");
        linkedList.add("e");
        linkedList.add("f");
        session.open();
        session.setObject("list", linkedList);
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        linkedList = getObject();
        linkedList.remove("a");
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        linkedList = getObject();
        linkedList.remove("b");
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        linkedList = getObject();
        linkedList.remove("e");
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        linkedList = getObject();
        linkedList.remove("f");
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        linkedList = getObject();
        final Iterator<String> iterator = linkedList.iterator();
        assertTrue("must have first element", iterator.hasNext());
        assertEquals("'c' expected", "c", iterator.next());
        assertTrue("must have second element", iterator.hasNext());
        assertEquals("'d' expected", "d", iterator.next());
        assertFalse("must not have more element", iterator.hasNext());
        session.close(EnumFilePersistenceCloseAction.SAVE);
    }

    public void testAddRemove2() throws FilePersistenceException, FilePersistenceClassNotFoundException, FilePersistenceInvalidClassException, FilePersistenceDataCorruptedException, FilePersistenceNotSerializableException, FilePersistenceTooBigForSerializationException {
        PLinkedList<String> linkedList = new PLinkedList<String>();
        final PLinkedList<String> memLinkedList = new PLinkedList<String>();
        session.open();
        session.setObject("list", linkedList);
        final int max = 160;
        final int intervalBegin = 40;
        final int intervalEnd = 120;
        final int intervalLength = intervalEnd - intervalBegin + 1;
        int count;
        for (count = 0; count < max; count++) {
            final String keyValue = "" + count;
            linkedList.add(keyValue);
            memLinkedList.add(keyValue);
        }
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        linkedList = getObject();
        for (count = 0; count < max; count++) {
            if (count < intervalBegin || count > intervalEnd) {
                assertNotNull("must exist", linkedList.remove("" + count));
                assertNotNull("must exist", memLinkedList.remove("" + count));
                if (count % 60 == 1) {
                    session.close(EnumFilePersistenceCloseAction.SAVE);
                    session.open();
                    linkedList = getObject();
                }
            }
        }
        assertEquals("bad list size", intervalLength, linkedList.size());
        assertEquals("bad mem list size", intervalLength, memLinkedList.size());
        count = 0;
        final Iterator<String> keySetIterator = linkedList.iterator();
        while (keySetIterator.hasNext()) {
            assertEquals("bad value", "" + (count + intervalBegin), keySetIterator.next());
            count++;
        }
        assertEquals("bad count", intervalLength, count);
        session.close(EnumFilePersistenceCloseAction.SAVE);
    }

    @SuppressWarnings("unchecked")
    private PLinkedList<String> getObject() throws FilePersistenceException {
        return (PLinkedList<String>) session.getObject("list");
    }
}

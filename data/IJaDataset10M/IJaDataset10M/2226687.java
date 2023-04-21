package net.sf.joafip.simplestore;

import net.sf.joafip.AbstractDeleteFileTestCase;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestException;
import net.sf.joafip.service.FilePersistenceClassNotFoundException;
import net.sf.joafip.service.FilePersistenceDataCorruptedException;
import net.sf.joafip.service.FilePersistenceException;
import net.sf.joafip.service.FilePersistenceInvalidClassException;
import net.sf.joafip.service.FilePersistenceNotSerializableException;
import net.sf.joafip.service.FilePersistenceTooBigForSerializationException;

@NotStorableClass
@StorableAccess
public class TestSimpleStore extends AbstractDeleteFileTestCase {

    private static final String HELLO_WORLD = "hello world";

    public TestSimpleStore() throws TestException {
        super();
    }

    public TestSimpleStore(final String name) throws TestException {
        super(name);
    }

    public void testReadWrite() throws FilePersistenceException, FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, FilePersistenceTooBigForSerializationException {
        final SimpleStore simpleStore = new SimpleStore();
        simpleStore.open();
        simpleStore.writeObject("myKey", HELLO_WORLD);
        final String read = (String) simpleStore.readObject("myKey");
        assertEquals("must read that was wrote", HELLO_WORLD, read);
        simpleStore.close();
    }
}

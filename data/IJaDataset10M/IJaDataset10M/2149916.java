package net.sf.joafip.service.rel400;

import java.util.Iterator;
import net.sf.joafip.AbstractDeleteFileTestCase;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestException;
import net.sf.joafip.entity.EnumFilePersistenceCloseAction;
import net.sf.joafip.java.util.support.tree.TreeSupport;
import net.sf.joafip.service.FilePersistenceBuilder;
import net.sf.joafip.service.FilePersistenceClassNotFoundException;
import net.sf.joafip.service.FilePersistenceDataCorruptedException;
import net.sf.joafip.service.FilePersistenceException;
import net.sf.joafip.service.FilePersistenceInvalidClassException;
import net.sf.joafip.service.FilePersistenceNotSerializableException;
import net.sf.joafip.service.FilePersistenceTooBigForSerializationException;
import net.sf.joafip.service.IDataAccessSession;
import net.sf.joafip.service.IFilePersistence;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@StorableAccess
public class TestTreeSupportIterator extends AbstractDeleteFileTestCase {

    private static final String ITERATOR = "iterator";

    private static final String TREE = "tree";

    public static final String ID_KEY_NAME = "idKey";

    public static final String TYPE_KEY_NAME = "typeKey";

    private IFilePersistence filePersistence;

    public TestTreeSupportIterator() throws TestException {
        super();
    }

    public TestTreeSupportIterator(final String name) throws TestException {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        final FilePersistenceBuilder builder = new FilePersistenceBuilder();
        builder.setPathName("z:/runtime");
        builder.setRemoveFiles(true);
        builder.setProxyMode(true);
        builder.setAutoSaveEnabled(false);
        builder.setMaintenedInMemory(false);
        filePersistence = builder.build();
    }

    @Override
    public void tearDown() throws Exception {
        try {
            filePersistence.close();
        } catch (Exception e) {
        }
        super.tearDown();
    }

    @SuppressWarnings("unchecked")
    public void test() throws FilePersistenceException, FilePersistenceClassNotFoundException, FilePersistenceInvalidClassException, FilePersistenceDataCorruptedException, FilePersistenceNotSerializableException, FilePersistenceTooBigForSerializationException {
        final IDataAccessSession session = filePersistence.createDataAccessSession();
        session.open();
        TreeSupport<String> tree = new TreeSupport<String>(null, true, false);
        session.setObject(TREE, tree);
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        tree = (TreeSupport<String>) session.getObject(TREE);
        tree.add(ID_KEY_NAME);
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        tree = (TreeSupport<String>) session.getObject(TREE);
        tree.add(TYPE_KEY_NAME);
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        tree = (TreeSupport<String>) session.getObject(TREE);
        Iterator<String> iterator = tree.iterator();
        session.setObject(ITERATOR, iterator);
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        iterator = (Iterator<String>) session.getObject(ITERATOR);
        assertTrue("must has first element", iterator.hasNext());
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        iterator = (Iterator<String>) session.getObject(ITERATOR);
        String next = iterator.next();
        assertEquals("bad first key", ID_KEY_NAME, next);
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        iterator = (Iterator<String>) session.getObject(ITERATOR);
        assertTrue("must has second element", iterator.hasNext());
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        iterator = (Iterator<String>) session.getObject(ITERATOR);
        next = iterator.next();
        assertEquals("bad second key", TYPE_KEY_NAME, next);
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        iterator = (Iterator<String>) session.getObject(ITERATOR);
        assertFalse("must not has more element", iterator.hasNext());
        session.close(EnumFilePersistenceCloseAction.SAVE);
    }
}

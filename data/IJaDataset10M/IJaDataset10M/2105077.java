package net.sf.joafip.store.service;

import net.sf.joafip.AbstractDeleteFileTestCase;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestException;
import net.sf.joafip.store.service.objectfortest.BobContainer;

@NotStorableClass
@StorableAccess
public class TestCustomObjectIONoG extends AbstractDeleteFileTestCase {

    private StoreForTest store;

    public TestCustomObjectIONoG() throws TestException {
        super();
    }

    public TestCustomObjectIONoG(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        store = new StoreForTest(1, null, path, false);
        store.openAndNewAccessSession(true);
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            store.close();
        } catch (final Throwable throwable) {
        }
        store = null;
        super.tearDown();
    }

    public void testCustomIO() throws StoreException, StoreInvalidClassException, StoreNotSerializableException, StoreClassNotFoundException, StoreDataCorruptedException, StoreTooBigForSerializationException {
        store.setObjectIOForClass(ForTestClass.class, ForTestObjectInput.class, ForTestObjectOutput.class);
        BobContainer bobContainer = new BobContainer();
        ForTestClass forTest = new ForTestClass();
        forTest.setCharValue('A');
        bobContainer.setObject1(forTest);
        store.setRoot(bobContainer);
        store.save(true, false);
        bobContainer = (BobContainer) store.readAndGetRoot();
        forTest = (ForTestClass) bobContainer.getObject1();
        assertEquals("bad value", 'A', forTest.getCharValue());
    }
}

package net.sf.joafip.store.service.heaprecordable;

import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.TestException;
import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.kvstore.service.HeapException;
import net.sf.joafip.redblacktree.service.RBTException;
import net.sf.joafip.store.entity.heaprecordable.AbstractHeapRecordable;
import net.sf.joafip.store.service.AbstractDataManagerTestCase;
import net.sf.joafip.store.service.StoreException;
import net.sf.joafip.store.service.binary.HelperBinaryConversion;

@NotStorableClass
@DoNotTransform
public class TestHeapRecordableManager extends AbstractDataManagerTestCase {

    @NotStorableClass
    private static class Recordable extends AbstractHeapRecordable {

        private byte value;

        public Recordable(final HeapRecordableManager heapRecordableManager, final HelperBinaryConversion helperBinaryConversion) {
            super(heapRecordableManager, helperBinaryConversion);
        }

        public Recordable(final HeapRecordableManager heapRecordableManager, final byte value, final HelperBinaryConversion helperBinaryConversion) {
            super(heapRecordableManager, helperBinaryConversion);
            this.value = value;
        }

        public int byteSize() {
            return 10;
        }

        public byte getValue() {
            return value;
        }

        public void marshall(byte[] binary) {
            if (binary.length != 10) {
                throw new IllegalArgumentException("binary must have length of 10");
            }
            binary[0] = value;
        }

        public void unmarshall(final byte[] binary) {
            if (binary.length != 10) {
                throw new IllegalArgumentException("binary must have length of 10");
            }
            value = binary[0];
        }

        @Override
        public void setStateHaveChanged() throws HeapRecordableException {
            super.setStateHaveChanged();
        }
    }

    public TestHeapRecordableManager() throws TestException {
        super();
    }

    public TestHeapRecordableManager(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void test() throws HeapException, StoreException, HeapRecordableException, RBTException {
        final HelperBinaryConversion helperBinaryConversion = new HelperBinaryConversion();
        final HeapRecordableManager recordableManager = new HeapRecordableManager(dataManager);
        Recordable recordable0 = new Recordable(recordableManager, (byte) 0, helperBinaryConversion);
        final DataRecordIdentifier id0 = new DataRecordIdentifier(0);
        recordable0.setDataRecordIdentifier(id0, true);
        assertFalse("read must failed", recordableManager.readAndSetState(recordable0));
        recordableManager.attach(recordable0);
        recordable0.setStateHaveChanged();
        assertEquals("must alloc #1", new DataRecordIdentifier(1), allocHeapFileRecord(50));
        assertEquals("must alloc #2", new DataRecordIdentifier(2), allocHeapFileRecord(50));
        Recordable recordable3 = new Recordable(recordableManager, (byte) 3, helperBinaryConversion);
        recordableManager.attach(recordable3);
        recordable3.setStateHaveChanged();
        recordableManager.save();
        final DataRecordIdentifier id3 = new DataRecordIdentifier(3);
        assertEquals("must be write in #3", id3, recordable3.getDataRecordIdentifier());
        recordable0 = new Recordable(recordableManager, helperBinaryConversion);
        recordable0.setDataRecordIdentifier(id0, true);
        assertTrue("read must succeed", recordableManager.readAndSetState(recordable0));
        assertEquals("recordable0 value must be 0", (byte) 0, recordable0.getValue());
        recordable3 = new Recordable(recordableManager, helperBinaryConversion);
        recordable3.setDataRecordIdentifier(id3, true);
        assertTrue("read must succeed", recordableManager.readAndSetState(recordable3));
        assertEquals("recordable3 value must be 3", (byte) 3, recordable3.getValue());
    }

    private DataRecordIdentifier allocHeapFileRecord(final int size) throws HeapException {
        final DataRecordIdentifier identifier = dataManager.getNewDataRecordIdentifier();
        final byte[] data = new byte[size];
        dataManager.writeDataRecord(identifier, data);
        return identifier;
    }
}

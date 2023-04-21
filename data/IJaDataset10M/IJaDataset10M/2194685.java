package net.sf.joafip.heapfile.record.entity;

import java.io.File;
import java.io.IOException;
import net.sf.joafip.AbstractDeleteFileTestCase;
import net.sf.joafip.TestConstant;
import net.sf.joafip.file.entity.AbstractFileStorable;
import net.sf.joafip.file.service.FileForStorable;
import net.sf.joafip.heapfile.service.HeapException;

public class TestAbstractFileStorable extends AbstractDeleteFileTestCase {

    private static final String FILE_NAME = TestConstant.RUNTIME_DIR + File.separator + "test.dat";

    private static final String DATA1 = "abcdefghijkl";

    private static final String DATA2 = "0123456789abcdefghijkl";

    private class Storable extends AbstractFileStorable {

        public byte data[];

        public int dataSize;

        public Storable(final long positionInFile, final int length) {
            super(positionInFile, length);
        }

        @Override
        protected void unmarshallImpl() throws HeapException {
            data = readBytes(dataSize);
            readAndCheckCrc32();
            _log.info("writed crc32 " + crc32 + " pos=" + positionInFile);
        }

        @Override
        protected void marshallImpl() throws HeapException {
            writeBytes(data);
            writeInteger(crc32);
            _log.info("writed crc32 " + crc32 + " pos=" + positionInFile);
        }

        @Override
        public boolean equals(final Object obj) {
            boolean equals;
            if (obj == null) {
                equals = false;
            } else if (obj instanceof Storable) {
                final Storable storable = (Storable) obj;
                equals = positionInFile == storable.positionInFile;
                if (equals) {
                    equals = data.length == storable.data.length;
                }
                for (int index = 0; equals && index < data.length; index++) {
                    equals = data[index] == storable.data[index];
                }
            } else {
                equals = false;
            }
            return equals;
        }
    }

    ;

    private Storable storableIn1;

    private Storable storableOut1;

    private Storable storableIn2;

    private Storable storableOut2;

    private FileForStorable fileForStorable;

    protected void setUp() throws Exception {
        super.setUp();
        storableIn1 = new Storable(0, DATA1.length() + 4);
        storableOut1 = new Storable(0, DATA1.length() + 4);
        storableOut1.setValueIsChanged();
        storableIn2 = new Storable(DATA1.length() + 4, DATA2.length() + 4);
        storableOut2 = new Storable(DATA1.length() + 4, DATA2.length() + 4);
        storableOut2.setValueIsChanged();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInOut1() throws HeapException {
        new File(FILE_NAME).delete();
        fileForStorable = new FileForStorable(FILE_NAME);
        outIn();
    }

    public void testInOut2() throws HeapException {
        new File(FILE_NAME).delete();
        fileForStorable = new FileForStorable(FILE_NAME);
        output();
        fileForStorable = new FileForStorable(FILE_NAME);
        input();
    }

    private void outIn() throws HeapException {
        output();
        input();
        assertEquals("in1 must be equals to out1", storableIn1, storableOut1);
        assertEquals("in2 must be equals to out2", storableIn2, storableOut2);
    }

    /**
	 * @throws HeapException
	 * @throws FileCorruptedException
	 * @throws IOException
	 */
    private void input() throws HeapException {
        storableIn1.dataSize = DATA1.length();
        storableIn2.dataSize = DATA2.length();
        fileForStorable.open();
        fileForStorable.readStorable(storableIn2);
        fileForStorable.readStorable(storableIn1);
        fileForStorable.close();
    }

    /**
	 * @throws HeapException
	 * @throws FileCorruptedException
	 * @throws IOException
	 */
    private void output() throws HeapException {
        storableOut1.data = DATA1.getBytes();
        storableOut1.dataSize = DATA1.length();
        fileForStorable.open();
        fileForStorable.writeStorable(storableOut1);
        fileForStorable.close();
        storableOut2.data = DATA2.getBytes();
        storableOut2.dataSize = DATA2.length();
        fileForStorable.open();
        fileForStorable.writeStorable(storableOut2);
        fileForStorable.close();
    }
}

package test.dicom4j.data;

import org.dicom4j.data.CommandSet;
import org.dicom4j.data.elements.UnsignedLong;
import org.dicom4j.data.elements.UnsignedShort;
import org.dicom4j.dicom.DicomTags;
import test.dicom4j.Dicom4jTestCase;

public class CommandSetTest extends Dicom4jTestCase {

    public CommandSetTest(String name) {
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

    public void testToString() {
        CommandSet lObject1 = new CommandSet();
        lObject1.addElement(new UnsignedLong(DicomTags.CommandGroupLength));
        lObject1.addElement(new UnsignedShort(DicomTags.CommandField));
        lObject1.addElement(new UnsignedShort(DicomTags.DataSetType));
    }
}

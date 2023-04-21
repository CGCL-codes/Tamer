package test.dicom4j.io;

import java.io.File;
import junit.framework.TestCase;
import org.dicom4j.data.DataSet;
import org.dicom4j.data.FileMetaInformation;
import org.dicom4j.data.elements.SequenceOfItems;
import org.dicom4j.dicom.DicomTags;
import org.dicom4j.io.file.DicomFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.dicom4j.TestDicom4J;

/**
 * 
 * 
 * @since 0.0.3
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public class TestDicomFileReader extends TestCase {

    private static final Logger fLogger = LoggerFactory.getLogger(TestDicomFileReader.class);

    private static File locateFile(String name) {
        return new File("./src/test/resources/" + name);
    }

    public TestDicomFileReader(String name) {
        super(name);
        TestDicom4J.start();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testReadExplicitVRLE() throws Exception {
        DicomFileReader lReader = new DicomFileReader(locateFile("dicomdir1"));
        DataSet lData = new DataSet();
        FileMetaInformation lFMI = new FileMetaInformation();
        lReader.readFile(lFMI, lData);
        SequenceOfItems lSEQ = (SequenceOfItems) lData.getElement(DicomTags.DirectoryRecordSeq);
        assertNotNull(lSEQ);
        assertEquals(1203, lSEQ.count());
    }

    public final void testReadImplicitVRLE() throws Exception {
        DicomFileReader lReader = new DicomFileReader(locateFile("view400.dcm"));
        DataSet lData = new DataSet();
        FileMetaInformation lFMI = new FileMetaInformation();
        lReader.readFile(lFMI, lData);
        assertEquals(5, lFMI.count());
        assertEquals(37, lData.count());
    }

    public final void testReadRawImplicitVRLE() throws Exception {
        DicomFileReader lReader = new DicomFileReader(locateFile("OT-PAL-8-face"));
        DataSet lData = new DataSet();
        FileMetaInformation lFMI = new FileMetaInformation();
        lReader.readFile(lFMI, lData);
        assertEquals(33, lData.count());
    }
}

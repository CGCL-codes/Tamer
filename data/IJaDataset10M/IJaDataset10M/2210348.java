package test.dicom4j.data;

import org.dicom4j.data.DataElements;
import org.dicom4j.data.elements.ApplicationEntity;
import org.dicom4j.data.elements.DataElement;
import org.dicom4j.data.elements.LongText;
import org.dicom4j.data.elements.Time;
import org.dicom4j.data.elements.UnsignedLong;
import org.dicom4j.dicom.DicomTags;
import test.dicom4j.Dicom4jTestCase;

public class TestDataElement extends Dicom4jTestCase {

    public TestDataElement() {
        super("DataElementTest");
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testStringDataElements() {
        ApplicationEntity lAE = new ApplicationEntity(DicomTags.ScheduledStationAET);
        lAE.setValue("TEST1");
        lAE.addValue("TEST2");
        assertEquals("TEST1", lAE.getSingleStringValue(""));
        assertEquals("TEST1\\TEST2", lAE.getDelimitedStringValues(""));
    }

    public void testClear() throws Exception {
        ApplicationEntity lAE = new ApplicationEntity(DicomTags.ScheduledStationAET);
        lAE.setValue("Test");
        clearAndChek(lAE);
        Time ltime = DataElements.newTime();
        ltime.setValue("TEST");
        clearAndChek(ltime);
        LongText lLongText = DataElements.newAdditionalPatientHistory();
        lLongText.setValue("TEST");
        clearAndChek(lLongText);
        UnsignedLong lUnsignedLong = DataElements.newCommandGroupLength();
        lUnsignedLong.setValue(10);
        clearAndChek(lUnsignedLong);
    }

    protected void clearAndChek(DataElement aDataElement) throws Exception {
        assertTrue(aDataElement.getValueMultiplicity() != 0);
        aDataElement.clear();
        assertTrue(aDataElement.getValueMultiplicity() == 0);
    }
}

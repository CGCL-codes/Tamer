package ca.etsmtl.latis.pixelmedimpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import ca.etsmtl.latis.dicom.DicomException;
import ca.etsmtl.latis.dicom.IDicomAbstractFactory;
import ca.etsmtl.latis.dicom.IDicomElement;
import ca.etsmtl.latis.dicom.IDicomFile;
import ca.etsmtl.latis.dicom.IDicomSet;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.dicom.ValueRepresentation;

/**
 * Tests the PixelMed Abstract Factory implementation.
 *  
 * @version     Version 0.0 Tue, Oct 17th, 2006
 * @author      Alain Lemay
 */
public class DicomAbstractFactoryPixelMedImplTest {

    private static final int JUNIT_TEST_VALUE_TAG_GROUP_NUMBER_PATIENT_NAME = 0x0010;

    private static final int JUNIT_TEST_VALUE_TAG_ELEMENT_NUMBER_PATIENT_NAME = 0x0010;

    private static final String JUNIT_TEST_VALUE_VALUE_REPRESENTATION_PERSON_NAME = "PN";

    private static final String JUNIT_TEST_VALUE_PATIENT_NAME_VALUE = "JOHN DOE";

    private static final String JUNIT_TEST_VALUE_VALID_DICOM_FILENAME = "./dcm_samples/abdominal.dcm";

    private static final int ARRAY_OF_DICOM_ELEMENTS_LENGTH = 4;

    private static final IDicomElement[] ARRAY_OF_DICOM_ELEMENTS = new IDicomElement[ARRAY_OF_DICOM_ELEMENTS_LENGTH];

    /** Reference a DICOM Abstract Factory. */
    private IDicomAbstractFactory dicomAbstractFactory = DicomAbstractFactoryPixelMedImpl.getInstance();

    /**
	 * Constructs an instance of the {@code DicomAbstractFactoryPixelMedImplTest} class.
	 * 
	 * @throws DicomException if an error occurs during the creation of
	 *                        the DICOM elements.
	 */
    public DicomAbstractFactoryPixelMedImplTest() throws DicomException {
        ARRAY_OF_DICOM_ELEMENTS[0] = dicomAbstractFactory.createDicomElement(TagFromName.MediaStorageSOPClassUID.getGroup(), TagFromName.MediaStorageSOPClassUID.getElement(), new String(ValueRepresentation.UI), "1.2.840.10008.5.1.4.1.1.4");
        ARRAY_OF_DICOM_ELEMENTS[1] = dicomAbstractFactory.createDicomElement(TagFromName.MediaStorageSOPInstanceUID.getGroup(), TagFromName.MediaStorageSOPInstanceUID.getElement(), new String(ValueRepresentation.UI), "1.3.46.670589.11.30.9.1062531302827752870602.13.1.1.1.0.0.1");
        ARRAY_OF_DICOM_ELEMENTS[2] = dicomAbstractFactory.createDicomElement(TagFromName.PatientName.getGroup(), TagFromName.PatientName.getElement(), new String(ValueRepresentation.PN), "GEORGE ORWELL");
        ARRAY_OF_DICOM_ELEMENTS[3] = dicomAbstractFactory.createDicomElement(TagFromName.PatientBirthDate.getGroup(), TagFromName.PatientBirthDate.getElement(), new String(ValueRepresentation.DA), "19030625");
    }

    /**
	 * Tests if the {@link DicomAbstractFactoryPixelMedImpl#getInstance()}
	 * method always return a non-null reference.
	 */
    @Test
    public void testGetInstanceReturnNonNullInstance() {
        assertNotNull(DicomAbstractFactoryPixelMedImpl.getInstance());
    }

    /**
	 * Tests if the {@link DicomAbstractFactoryPixelMedImpl#getInstance()}
	 * method always return the same reference to the same object.
	 */
    @Test
    public void testGetInstanceReturnAlwaysTheSameInstance() {
        assertSame(DicomAbstractFactoryPixelMedImpl.getInstance(), DicomAbstractFactoryPixelMedImpl.getInstance());
    }

    /**
	 * Tests if the {@link DicomAbstractFactoryPixelMedImpl#createDicomElement(int, int, String, Object)}
	 * method returns a new {@link IDicomElement} successfully.
	 * 
	 * @throws DicomException this method is not supposed to throw any
	 *                        exception. 
	 */
    @Test
    public void testCreateDicomElementIntIntStringObjectWithValidValues() throws DicomException {
        IDicomElement dicomElement = dicomAbstractFactory.createDicomElement(JUNIT_TEST_VALUE_TAG_GROUP_NUMBER_PATIENT_NAME, JUNIT_TEST_VALUE_TAG_ELEMENT_NUMBER_PATIENT_NAME, JUNIT_TEST_VALUE_VALUE_REPRESENTATION_PERSON_NAME, JUNIT_TEST_VALUE_PATIENT_NAME_VALUE);
        assertTrue((dicomElement.getGroup() == JUNIT_TEST_VALUE_TAG_GROUP_NUMBER_PATIENT_NAME) && (dicomElement.getElement() == JUNIT_TEST_VALUE_TAG_ELEMENT_NUMBER_PATIENT_NAME) && dicomElement.getValueRepresentation().equals(JUNIT_TEST_VALUE_VALUE_REPRESENTATION_PERSON_NAME) && dicomElement.getValue().equals(JUNIT_TEST_VALUE_PATIENT_NAME_VALUE));
    }

    /**
	 * Tests if the {@link DicomAbstractFactoryPixelMedImpl#createDicomElement(int, int, String, Object)}
	 * method raise a {@link DicomException} when we specify an invalid
	 * value representation (VR).
	 * @throws DicomException 
	 * @throws DicomException 
	 */
    @Test(expected = DicomException.class)
    public void testCreateDicomElementIntIntStringObjectWithInvalidValueRepresentationThrowADicomException() throws DicomException {
        final String INVALID_VALUE_REPRESENTATION = "ZZ";
        dicomAbstractFactory.createDicomElement(JUNIT_TEST_VALUE_TAG_GROUP_NUMBER_PATIENT_NAME, JUNIT_TEST_VALUE_TAG_ELEMENT_NUMBER_PATIENT_NAME, INVALID_VALUE_REPRESENTATION, JUNIT_TEST_VALUE_PATIENT_NAME_VALUE);
    }

    /**
	 * Tests if the {@link DicomAbstractFactoryPixelMedImpl#createDicomElement(int, int, String, Object)}
	 * method raise a  {@link DicomException} when we specify a
	 * unsupported object type.
	 * @throws DicomException 
	 */
    @Test(expected = DicomException.class)
    public void testCreateDicomElementIntIntStringObjectWithUnsupportedObjectTypeThrowADicomException() throws DicomException {
        final Object SOME_OBJECT = new Integer(0);
        dicomAbstractFactory.createDicomElement(JUNIT_TEST_VALUE_TAG_GROUP_NUMBER_PATIENT_NAME, JUNIT_TEST_VALUE_TAG_ELEMENT_NUMBER_PATIENT_NAME, JUNIT_TEST_VALUE_VALUE_REPRESENTATION_PERSON_NAME, SOME_OBJECT);
    }

    /**
	 * Tests the {@link DicomAbstractFactoryPixelMedImpl#createDicomSet(List)}
	 * method with a list of DICOM elements.
	 * @throws DicomException 
	 */
    @Test
    public void testCreateDicomSetListWithAPartiallyFilledListOfDicomElements() throws DicomException {
        IDicomSet dicomSet = dicomAbstractFactory.createDicomSet(Arrays.asList(ARRAY_OF_DICOM_ELEMENTS));
        assertNotNull(dicomSet);
        assertEquals(ARRAY_OF_DICOM_ELEMENTS_LENGTH, dicomSet.getSize());
    }

    /**
	 * Tests the {@link DicomAbstractFactoryPixelMedImpl#createDicomSet(List)}
	 * method with an empty list of DICOM elements.
	 */
    @Test
    public void testCreateDicomSetListWithAnEmptyListOfDicomElements() {
        final List<IDicomElement> emptyListOfDicomElements = new ArrayList<IDicomElement>();
        IDicomSet dicomSet = dicomAbstractFactory.createDicomSet(emptyListOfDicomElements);
        assertNotNull(dicomSet);
        final int ZERO_LENGTH = 0;
        assertEquals(ZERO_LENGTH, dicomSet.getSize());
    }

    /**
	 * Tests the {@link DicomAbstractFactoryPixelMedImpl#createDicomSet(List)}
	 * method with a null reference to {@link java.util.List}.
	 */
    @Test(expected = NullPointerException.class)
    public void testCreateDicomSetListWithNullArgumentThrowsANullPointerException() {
        dicomAbstractFactory.createDicomSet((List<IDicomElement>) null);
    }

    /**
	 * Tests if the {@link DicomAbstractFactoryPixelMedImpl#createDicomFile(IDicomSet)}
	 * method returns a non-null reference to a DICOM file.
	 */
    @Test
    public void testCreateDicomFileIDicomSetWithAValidDicomSetReturnsANonNullDicomFileReference() {
        assertNotNull(dicomAbstractFactory.createDicomFile(dicomAbstractFactory.createDicomSet(Arrays.asList(ARRAY_OF_DICOM_ELEMENTS))));
    }

    /**
	 * Tests if the {@link DicomAbstractFactoryPixelMedImpl#createDicomFile(IDicomSet)}
	 * method returns a an instance of the {@link DicomFilePixelMedImpl}
	 * class.
	 */
    @Test
    public void testCreateDicomFileIDicomSetWithAValidDicomSetReturnsAnInstanceOfDicomFilePixelMedImplClass() {
        assertTrue(dicomAbstractFactory.createDicomFile(dicomAbstractFactory.createDicomSet(Arrays.asList(ARRAY_OF_DICOM_ELEMENTS))) instanceof DicomFilePixelMedImpl);
    }

    /**
	 * Tests if the {@link DicomAbstractFactoryPixelMedImpl#createDicomFile(IDicomSet)}
	 * method with a null reference to a {@link IDicomSet} passed as an
	 * argument throw a {@link NullPointerException} exception.
	 */
    @Test(expected = NullPointerException.class)
    public void testCreateDicomFileIDicomSetWithANullArgumentThrowsANullPointerException() {
        IDicomSet dicomSet = null;
        dicomAbstractFactory.createDicomFile(dicomSet);
    }

    /**
	 * Tests if the {@link DicomAbstractFactoryPixelMedImpl#createDicomFile(File)}
	 * method returns a non-null reference to a DICOM file.
	 * 
	 * @throws DicomException if an error occurs during the file
	 *                        reading. 
	 */
    @Test
    public void testCreateDicomFileFileWithAValidFilePassedAsArgumentReturnsAValidDicomFileReference() throws DicomException {
        assertNotNull(dicomAbstractFactory.createDicomFile(new File(JUNIT_TEST_VALUE_VALID_DICOM_FILENAME)));
    }

    /**
	 * Tests if the {@link DicomAbstractFactoryPixelMedImpl#createDicomFile(File)}
	 * method returns an instance of the {@link DicomFilePixelMedImpl} class.
	 * 
	 * @throws DicomException if an error occurs during the reading of
	 *                        the file.
	 */
    @Test
    public void testCreateDicomFileFileWithAValidFilePassedAsArgumentReturnsAnInstanceOfDicomFilePixelMedImplClass() throws DicomException {
        assertTrue(dicomAbstractFactory.createDicomFile(new File(JUNIT_TEST_VALUE_VALID_DICOM_FILENAME)) instanceof DicomFilePixelMedImpl);
    }

    /**
	 * Tests if the {@link DicomAbstractFactoryPixelMedImpl#createDicomFile(File)}
	 * method throws a {@link NullPointerException} if the reference to
	 * the {@link java.io.File} is equal to null.
	 * 
	 * @throws DicomException if an error occurs during the reading of
	 *                        the file.
	 */
    @Test(expected = NullPointerException.class)
    public void testCreateDicomFileFileWithNullReferenceToAFilePassedAsArgumentThrowsANullPointerException() throws DicomException {
        dicomAbstractFactory.createDicomFile((File) null);
    }

    /**
	 * Tests if the {@link DicomAbstractFactoryPixelMedImpl#createDicomFile(File)}
	 * method throws a {@link DicomException} caused by an {@link
	 * IOException} thrown by an invalid filename.
	 * @throws DicomException 
	 */
    @Test(expected = DicomException.class)
    public void testCreateDicomFileFileWithAnNonExistingFilePassedAsArgumentThrowsADicomExceptionCausedByAnIOException() throws DicomException {
        final String INVALID_FILENAME = "\\\\";
        dicomAbstractFactory.createDicomFile(new File(INVALID_FILENAME));
    }

    @Test
    public void testCreateDicomFileIDicomSetIDicomSetWithValidDicomSetAndValidMetaInfHeader() throws DicomException {
        final int ARRAY_OF_FILE_META_ELEMENTS_LENGTH = 4;
        final IDicomElement[] ARRAY_OF_FILE_META_ELEMENTS = new DicomElementPixelMedImpl[ARRAY_OF_FILE_META_ELEMENTS_LENGTH];
        ARRAY_OF_FILE_META_ELEMENTS[0] = dicomAbstractFactory.createDicomElement(TagFromName.MediaStorageSOPClassUID.getGroup(), TagFromName.MediaStorageSOPClassUID.getElement(), new String(ValueRepresentation.UI), "1.2.840.10008.5.1.4.1.1.4");
        ARRAY_OF_FILE_META_ELEMENTS[1] = dicomAbstractFactory.createDicomElement(TagFromName.MediaStorageSOPInstanceUID.getGroup(), TagFromName.MediaStorageSOPInstanceUID.getElement(), new String(ValueRepresentation.UI), "1.3.46.670589.11.30.9.1062531302827752870602.13.1.1.1.0.0.1");
        ARRAY_OF_FILE_META_ELEMENTS[2] = dicomAbstractFactory.createDicomElement(TagFromName.TransferSyntaxUID.getGroup(), TagFromName.TransferSyntaxUID.getElement(), new String(ValueRepresentation.UI), "1.2.840.10008.1.2");
        ARRAY_OF_FILE_META_ELEMENTS[3] = dicomAbstractFactory.createDicomElement(TagFromName.ImplementationClassUID.getGroup(), TagFromName.ImplementationClassUID.getElement(), new String(ValueRepresentation.UI), "Some value");
        IDicomSet dicomSet = dicomAbstractFactory.createDicomSet(Arrays.asList(ARRAY_OF_DICOM_ELEMENTS));
        IDicomSet metaInfHeader = dicomAbstractFactory.createDicomSet(Arrays.asList(ARRAY_OF_FILE_META_ELEMENTS));
        IDicomFile dicomFile = ((DicomAbstractFactoryPixelMedImpl) dicomAbstractFactory).createDicomFile(dicomSet, metaInfHeader);
        assertEquals(dicomSet, dicomFile.getDicomSet());
        assertEquals(metaInfHeader, dicomFile.getMetaInformationHeader());
    }
}

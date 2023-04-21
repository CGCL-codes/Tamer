package ca.etsmtl.latis.pixelmedimpl;

import java.io.File;
import java.util.Arrays;
import junit.framework.TestCase;
import ca.etsmtl.latis.dicom.DicomException;
import ca.etsmtl.latis.dicom.IDicomAbstractFactory;
import ca.etsmtl.latis.dicom.IDicomElement;
import ca.etsmtl.latis.dicom.IDicomSet;

/**
 * <p>
 * Tests the methods of the {@link DicomFilePixelMedImpl} class.
 * </p>
 * 
 * @version     Version 0.0 Sun, Oct 15th, 2006
 * @author      Alain Lemay
 */
public class DicomFilePixelMedImplTest extends TestCase {

    private IDicomAbstractFactory factory = DicomAbstractFactoryPixelMedImpl.getInstance();

    private IDicomSet getMetaInformationHeader() throws DicomException {
        final IDicomElement[] dicomElements = new IDicomElement[4];
        dicomElements[0] = factory.createDicomElement(0x0002, 0x0000, "UL", "212");
        dicomElements[1] = factory.createDicomElement(0x0002, 0x0002, "UI", "1.2.840.10008.5.1.4.1.1.4");
        dicomElements[2] = factory.createDicomElement(0x0002, 0x0003, "UI", "1.3.46.670589.11.30.9.1062531302827752870602.13.1.1.1.0.0.1");
        dicomElements[3] = factory.createDicomElement(0x0002, 0x0010, "UI", "1.2.840.10008.1.2");
        return factory.createDicomSet(Arrays.asList(dicomElements));
    }

    private IDicomSet getDicomDataSet() throws DicomException {
        final IDicomElement[] dicomElements = new IDicomElement[4];
        dicomElements[0] = factory.createDicomElement(0x0008, 0x0016, "UI", "1.2.840.10008.5.1.4.1.1.4");
        dicomElements[1] = factory.createDicomElement(0x0008, 0x0018, "UI", "1.3.46.670589.11.30.9.1062531302827752870602.13.1.1.1.0.0.1");
        dicomElements[2] = factory.createDicomElement(0x0010, 0x0010, "PN", "PATIENT NAME");
        dicomElements[3] = factory.createDicomElement(0x0010, 0x0030, "DA", "19700101");
        return factory.createDicomSet(Arrays.asList(dicomElements));
    }

    /**
	 * <p>
	 * Tests if the {@link DicomFilePixelMedImpl#DicomFilePixelMedImpl(
	 * IDicomSet, File)} constructor work appropriately.
	 * </p>
	 * @throws DicomException 
	 */
    public void testDicomFilePixelMedImplIDicomSetIDicomSetFileConstructsObjectSuccessfully() throws DicomException {
        assertNotNull(new DicomFilePixelMedImpl(getDicomDataSet(), getMetaInformationHeader(), null));
    }

    public void testDicomFilePixelMedImplGetDicomSetReturnsRightInstanceOfDicomSet() throws DicomException {
        IDicomSet dicomSet = getDicomDataSet();
        DicomFilePixelMedImpl dicomFile = new DicomFilePixelMedImpl(dicomSet, getMetaInformationHeader(), null);
        assertSame(dicomSet, dicomFile.getDicomSet());
    }

    public void testDicomFilePixelMedImplGetMetaInformationHeaderReturnsTheMetaInformationHeaderDicomSet() throws DicomException {
        IDicomSet metaInfHeader = getMetaInformationHeader();
        DicomFilePixelMedImpl dicomFile = new DicomFilePixelMedImpl(getDicomDataSet(), metaInfHeader, null);
        assertSame(metaInfHeader, dicomFile.getMetaInformationHeader());
    }
}

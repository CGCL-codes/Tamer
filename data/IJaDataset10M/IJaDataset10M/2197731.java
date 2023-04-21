package ca.etsmtl.latis.pixelmedimpl;

import org.apache.log4j.Logger;
import ca.etsmtl.latis.dicom.DicomException;
import ca.etsmtl.latis.dicom.IDicomUIDGenerator;
import com.pixelmed.dicom.UIDGenerator;

/**
 * This is the PixelMed implementation of the {@link
 * IDicomUIDGenerator} interface.
 *
 * @version Version 0.0 Wed, Aug 15<sup>th</sup>, 2007
 * @author  Alain Lemay
 */
public class DicomUIDGeneratorAdapterPixelMedImpl implements IDicomUIDGenerator {

    /** The Log4j logger for the objects of this class. */
    private static final Logger logger = Logger.getLogger(DicomUIDGeneratorAdapterPixelMedImpl.class);

    /** The PixelMed implementation of a UID generator. */
    private UIDGenerator generator = new UIDGenerator();

    private boolean another;

    /**
	 * Constructs a new instance of the {@code
	 * DicomUIDGeneratorAdapterPixelMedImpl} class.
	 *
	 * <p>
	 * Don't use that constructor directly, use the PixelMed
	 * implementation of the abstract factory {@link
	 * DicomAbstractFactoryPixelMedImpl} instead.
	 * </p>
	 */
    DicomUIDGeneratorAdapterPixelMedImpl() {
        logger.debug("Entering DicomUIDGeneratorAdapterPixelMedImpl().");
        logger.debug("Exiting DicomUIDGeneratorAdapterPixelMedImpl().");
    }

    public String generateUID() throws DicomException {
        logger.debug("Entering generateUID().");
        String uid = null;
        try {
            if (!another) {
                uid = generator.getNewUID();
                another = true;
            } else {
                uid = generator.getAnotherNewUID();
            }
        } catch (com.pixelmed.dicom.DicomException dex) {
            logger.error(dex, dex);
            throw new DicomException(dex);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting generateUID(); RV = " + (uid != null ? "\"" + uid + "\"" : null) + ".");
        }
        return uid;
    }
}

package ca.etsmtl.latis.server;

import jdcm.DicomElement;
import jdcm.DicomSet;
import org.apache.log4j.Logger;

/**
 * Wraps a {@code jdcm.DicomSet} class to offer more suitable services
 * than the {@code jdcm.DicomSet} itself. 
 * 
 * @version     Version 0.0 Tue May 23rd, 2006
 * @author      Alain Lemay
 */
public class DicomImageInfo {

    /** The logger of the {@code DicomImageInfo} class. */
    private static final Logger logger = Logger.getLogger(DicomImageInfo.class);

    /** Image information group. */
    public static final int GROUP_IMAGE_INFO = 0x0008;

    /** Patient information group. */
    public static final int GROUP_PATIENT_INFO = 0x0010;

    /** Instance information group 2. */
    public static final int GROUP_INSTANCE_INFO = 0x0020;

    /** SOP Instance UID attribute (0008,0018). */
    public static final int ELEMENT_SOP_INSTANCE_UID = 0x0018;

    /** Modality (0008,0060). */
    public static final int ELEMENT_MODALITY = 0x0060;

    /** Instance Number (0020,0013). */
    public static final int ELEMENT_INSTANCE_NUMBER = 0x0013;

    /** Series Number (0020, 0011). */
    public static final int ELEMENT_SERIES_NUMBER = 0x0011;

    /** Patient's Name (0010,0010). */
    public static final int ELEMENT_PATIENT_NAME = 0x0010;

    /** The wrapped data set ({@code DicomSet}). */
    private DicomSet dicomSet;

    /**
	 * Constructs a {@code DicomImageInfo} with an empty data set.
	 */
    public DicomImageInfo() {
        logger.debug("Entering DicomImageInfo().");
        dicomSet = new DicomSet();
        logger.debug("Exiting DicomImageIndo().");
    }

    /**
	 * Constructs a {@code DicomImageInfo} with the specified data set.
	 * 
	 * @param dicomSet an instance of the {@code DicomSet} containing the
	 *                 specified data set.
	 */
    public DicomImageInfo(DicomSet dicomSet) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering DicomImageInfo(DicomSet dicomSet).");
            logger.debug("dicomSet = [" + dicomSet.getClass().getName() + "@" + Integer.toHexString(dicomSet.hashCode()) + "].");
        }
        this.dicomSet = dicomSet;
        logger.debug("Exiting DicomImageInfo(DicomSet dicomSet).");
    }

    /**
	 * Returns the referenced SOP Instance UID in file
	 * 
	 * <p> The SOP Instance UID Data Element Tag is denoted by (0008,0018). </p>
	 * 
	 * @return a {@link java.lang.String String} with the SOP Instance UID.
	 */
    public String getSOPInstanceUID() {
        logger.debug("Entering getSOPInstanceUID().");
        String SOPInstanceUID = getValueAsString(GROUP_IMAGE_INFO, ELEMENT_SOP_INSTANCE_UID);
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getSOPInstanceUID(); RV = " + (SOPInstanceUID != null ? "\"" + SOPInstanceUID + "\"" : null) + ".");
        }
        return SOPInstanceUID;
    }

    /**
	 * Returns the modality.
	 * 
	 * <p> The modality data element tag is denoted by (0008,0060). </p>
	 * 
	 * @return a {@link java.lang.String String} containing the modality.
	 */
    public String getModality() {
        logger.debug("Entering getModality().");
        String modality = getValueAsString(GROUP_IMAGE_INFO, ELEMENT_MODALITY);
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getModality(); RV = " + (modality != null ? "\"" + modality + "\"" : null) + ".");
        }
        return modality;
    }

    /**
	 * Returns the instance number.
	 * 
	 * <p> The instance number data element tag is denoted by (0020,0013). </p>
	 * 
	 * @return the instance number represented as a {@link java.lang.Integer Integer}
	 *         instance.
	 */
    public Integer getInstanceNumber() {
        logger.debug("Entering getInstanceNumber().");
        Integer instanceNumber = getValueAsInteger(GROUP_INSTANCE_INFO, ELEMENT_INSTANCE_NUMBER);
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getInstanceNumber(); RV = " + (instanceNumber != null ? "[" + instanceNumber + "]" : null) + ".");
        }
        return instanceNumber;
    }

    /**
	 * Returns the series number.
	 * 
	 * <p> The series number data element tag is denoted by (0020,0011). </p>
	 * 
	 * @return the series number represented as a {@link java.lang.Integer Integer}
	 *         instance. It returns null if it is undefined.
	 */
    public Integer getSeriesNumber() {
        logger.debug("Entering getSeriesNumber().");
        Integer seriesNumber = getValueAsInteger(GROUP_INSTANCE_INFO, ELEMENT_SERIES_NUMBER);
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getSeriesNumber(); RV = " + (seriesNumber != null ? "[" + seriesNumber + "]" : null) + ".");
        }
        return seriesNumber;
    }

    /**
	 * Gets the patient's name of the image.
	 *
	 * <p> The patient's name is denoted by (0010,0010) in the Dicom
	 * standard. </p>
	 * 
	 * @return the patient's name represented as a {@link java.lang.String String}.
	 */
    public String getPatientName() {
        logger.debug("Entering getPatientName().");
        String patientName = getValueAsString(GROUP_PATIENT_INFO, ELEMENT_PATIENT_NAME);
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getSeriesNumber(); RV = " + (patientName != null ? "\"" + patientName + "\"" : null) + ".");
        }
        return patientName;
    }

    /**
	 * Returns a value in a Dicom set represented as a {@link java.lang.String String}.
	 * This method has been developed to avoid code repetition.
	 * 
	 * @param  group an {@code} int representing the group number.
	 * @param  element an {@code} representing the element number within that group.
	 * @return the data element represented as a {@link java.lang.String String}.
	 */
    private String getValueAsString(int group, int element) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering getValueAsString(int group, int element).");
            logger.debug("group = [" + String.format("%04x", group) + "].");
            logger.debug("element = [" + String.format("%04x", element) + "].");
        }
        String valueAsString = null;
        DicomElement dicomElement = dicomSet.getElement(group, element);
        if (dicomElement != null) {
            valueAsString = dicomElement.getValueAsString();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getValueAsString(int group, int element); RV = " + (valueAsString != null ? "\"" + valueAsString + "\"" : null) + ".");
        }
        return valueAsString;
    }

    /**
	 * Returns a data element value as an {@link java.lang.Integer Integer}
	 * instance.
	 * 
	 * @param  group the group number.
	 * @param  element the element number within that group.
	 * @return an {@link java.lang.Integer Integer} representation of
	 *         the data element if it is defined, {@code null} if it
	 *         is undefined.
	 */
    private Integer getValueAsInteger(int group, int element) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering getValueAsInteger(int group, int element).");
            logger.debug("group = [" + String.format("%04x", group) + "].");
            logger.debug("element = [" + String.format("%04x", element) + "].");
        }
        Integer valueAsInteger = null;
        DicomElement dicomElement = dicomSet.getElement(group, element);
        if (dicomElement != null) {
            valueAsInteger = dicomElement.getValueAsInt();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getValueAsInteger(int group, int element); RV = " + (valueAsInteger != null ? "[" + valueAsInteger + "]" : null) + ".");
        }
        return valueAsInteger;
    }

    @Override
    public String toString() {
        logger.debug("Entering toString().");
        String toString = super.toString();
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting toString(); RV =\"" + toString + "\"");
        }
        return toString;
    }
}

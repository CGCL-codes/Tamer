package ca.etsmtl.latis.dicom;

import java.io.File;
import org.apache.log4j.Logger;

/**
 * Gathers all the common behaviors of the classes implementing the
 * {@link IDicomFile} interface.
 * 
 * @version     Version 0.0 Sun, Oct 15th, 2006
 * @author      Alain Lemay
 */
public abstract class AbstractDicomFile implements IDicomFile {

    /** The Log4j logger for this class. */
    private static final Logger logger = Logger.getLogger(AbstractDicomFile.class);

    /** The io file under the DICOM file. */
    private File file;

    /** The meta information header of the DICOM file. */
    private IDicomSet metaInformationHeader;

    /** The set of DICOM elements in the file. */
    private IDicomSet dicomSet;

    /**
	 * Constructs an instance of the {@code AbstractDicomFile} class
	 * with the specified meta information header.
	 * 
	 * @param dicomSet the data set in the DICOM file.
	 * @param metaInformationHeader the set of meta information header in the DICOM file.
	 * @param file the underlying DICOM physical file.
	 */
    public AbstractDicomFile(IDicomSet dicomSet, IDicomSet metaInformationHeader, File file) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering AbstractDicomFile(IDicomSet dicomSet, IDicomSet metaInformationHeader, File file).");
            logger.debug("dicomSet = " + (dicomSet != null ? "[" + dicomSet.getClass().getName() + "@" + Integer.toHexString(dicomSet.hashCode()) + "]" : null) + ".");
            logger.debug("metaInformationHeader = " + (metaInformationHeader != null ? "[" + metaInformationHeader.getClass().getName() + "@" + Integer.toHexString(metaInformationHeader.hashCode()) + "]" : null) + ".");
            logger.debug("file = " + (file != null ? "[" + file + "]" : null) + ".");
        }
        this.dicomSet = dicomSet;
        this.metaInformationHeader = metaInformationHeader;
        this.file = file;
        logger.debug("Exiting AbstractDicomFile(IDicomSet dicomSet, IDicomSet metaInformationHeader, File file).");
    }

    /**
	 * Constructs an instance of the {@code AbstractDicomFile} class
	 * without specifing the file meta data.
	 * 
	 * @see AbstractDicomFile#AbstractDicomFile(IDicomSet, IDicomSet, File)
	 * @param dicomSet the data within the DICOM file.
	 * @param file the DICOM file.
	 */
    public AbstractDicomFile(IDicomSet dicomSet, File file) {
        this(dicomSet, null, file);
        if (logger.isDebugEnabled()) {
            logger.debug("Entering AbstractDicomFile(IDicomSet dicomSet, File file).");
            logger.debug("dicomSet = " + (dicomSet != null ? "\"" + dicomSet.getClass().getName() + "@" + Integer.toHexString(dicomSet.hashCode()) + "\"" : null) + ".");
            logger.debug("file = " + (file != null ? "[" + file + "]" : null) + ".");
        }
        this.dicomSet = dicomSet;
        this.file = file;
        logger.debug("Exiting AbstractDicomFile(IDicomSet dicomSet, File file).");
    }

    /**
	 * @inheritDoc
	 */
    public IDicomSet getDicomSet() {
        logger.debug("Entering getDicomSet().");
        IDicomSet dicomSet = this.dicomSet;
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getDicomSet(); RV = " + (dicomSet != null ? "[" + dicomSet.getClass().getName() + "@" + Integer.toHexString(dicomSet.hashCode()) + "]" : null) + ".");
        }
        return dicomSet;
    }

    /**
	 * {@inheritDoc}
	 */
    public IDicomSet getMetaInformationHeader() {
        logger.debug("Entering getMetaInformationHeader().");
        IDicomSet metaInformationHeader = this.metaInformationHeader;
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getMetaInformationHeader(); RV = " + (metaInformationHeader != null ? "[" + metaInformationHeader.getClass().getName() + "@" + Integer.toHexString(metaInformationHeader.hashCode()) + "]" : null) + ".");
        }
        return metaInformationHeader;
    }

    /**
	 * <p>
	 * Gets the {@link File} object attached to this DICOM file.
	 * </p>
	 * 
	 * <p>
	 * Note: I've implemented this method to remove the <em>&quot;The
	 * field {@link AbstractDicomFile#file AbstractDicomFile.file} is
	 * never read locally&quot;</em> warning message.
	 * </p>
	 * 
	 * @return the underlying file as an instance of the {@link File}
	 *         class.
	 */
    public File getFile() {
        logger.debug("Entering getFile().");
        File file = this.file;
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getFile(); RV = " + (file != null ? "[" + file + "]" : null) + ".");
        }
        return file;
    }

    /**
	 * @inheritDoc
	 */
    public String getMediaStorageSOPClassUID() {
        logger.debug("Entering getMediaStorageSOPClassUID().");
        String mediaStorageSOPClassUID = null;
        IDicomElement dicomElement = metaInformationHeader.getDicomElement(0x0002, 0x0002);
        if (dicomElement != null) {
            mediaStorageSOPClassUID = (String) dicomElement.getValue();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getMediaStorageSOPClassUID(); RV = " + (mediaStorageSOPClassUID != null ? "\"" + mediaStorageSOPClassUID + "\"" : null) + ".");
        }
        return mediaStorageSOPClassUID;
    }

    /**
	 * @inheritDoc
	 */
    public String getMediaStorageSOPInstanceUID() {
        logger.debug("Entering getMediaStorageSOPInstanceUID().");
        String mediaStorageSOPInstanceUID = null;
        IDicomElement dicomElement = metaInformationHeader.getDicomElement(0x0002, 0x0003);
        if (dicomElement != null) {
            mediaStorageSOPInstanceUID = (String) dicomElement.getValue();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getMediaStorageSOPInstanceUID(); RV = " + (mediaStorageSOPInstanceUID != null ? "\"" + mediaStorageSOPInstanceUID + "\"" : null) + ".");
        }
        return mediaStorageSOPInstanceUID;
    }

    /**
	 * @inheritDoc
	 */
    public String getTransferSyntaxUID() {
        logger.debug("Entering getTransferSyntaxUID().");
        String transferSyntaxUID = null;
        IDicomElement dicomElement = metaInformationHeader.getDicomElement(0x0002, 0x0010);
        if (dicomElement != null) {
            transferSyntaxUID = (String) dicomElement.getValue();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getTransferSyntaxUID(); RV = " + (transferSyntaxUID != null ? "\"" + transferSyntaxUID + "\"" : null) + ".");
        }
        return transferSyntaxUID;
    }
}

package co.edu.unal.ungrid.image.dicom.core;

/**
 * <p>
 * A class to represent the characteristics of a DICOM Transfer Syntax, which
 * may be instantiated from a UID or from basic characteristics, as well as
 * static UID strings for known Transfer Syntaxes, and static methods for
 * extracting the characteristics of known Transfer Syntaxes.
 * </p>
 * 
 * 
 */
public class TransferSyntax {

    /***/
    public static final String ImplicitVRLittleEndian = "1.2.840.10008.1.2";

    /***/
    public static final String ExplicitVRLittleEndian = "1.2.840.10008.1.2.1";

    /***/
    public static final String ExplicitVRBigEndian = "1.2.840.10008.1.2.2";

    /***/
    public static final String Default = ImplicitVRLittleEndian;

    /***/
    public static final String DeflatedExplicitVRLittleEndian = "1.2.840.10008.1.2.1.99";

    /***/
    public static final String JPEGBaseline = "1.2.840.10008.1.2.4.50";

    /***/
    public static final String JPEGExtended = "1.2.840.10008.1.2.4.51";

    /***/
    public static final String JPEGLossless = "1.2.840.10008.1.2.4.57";

    /***/
    public static final String JPEGLosslessSV1 = "1.2.840.10008.1.2.4.70";

    /***/
    public static final String JPEGLS = "1.2.840.10008.1.2.4.80";

    /***/
    public static final String JPEGNLS = "1.2.840.10008.1.2.4.81";

    /***/
    public static final String JPEG2000Lossless = "1.2.840.10008.1.2.4.90";

    /***/
    public static final String JPEG2000 = "1.2.840.10008.1.2.4.91";

    /***/
    public static final String MPEG2MPML = "1.2.840.10008.1.2.4.100";

    /***/
    public static final String Bzip2ExplicitVRLittleEndian = "1.3.6.1.4.1.5962.300.1";

    /***/
    public static final String EncapsulatedRawLittleEndian = "1.3.6.1.4.1.5962.300.2";

    /**
	 * @uml.property name="transferSyntaxUID"
	 */
    private String transferSyntaxUID;

    /**
	 * @uml.property name="description"
	 */
    private String description;

    /**
	 * @uml.property name="bigEndian"
	 */
    private boolean bigEndian;

    /**
	 * @uml.property name="explicitVR"
	 */
    private boolean explicitVR;

    /**
	 * @uml.property name="encapsulatedPixelData"
	 */
    private boolean encapsulatedPixelData;

    /**
	 * @uml.property name="lossy"
	 */
    private boolean lossy;

    /**
	 * <p>
	 * Construct a Transfer Syntax using the specified UID, automatically
	 * determining its characteristics.
	 * </p>
	 * 
	 * @param uid
	 *            the UID to use to refer to this transfer syntax
	 */
    public TransferSyntax(String uid) {
        transferSyntaxUID = uid;
        description = "Unrecognized";
        bigEndian = false;
        explicitVR = true;
        encapsulatedPixelData = false;
        lossy = false;
        if (transferSyntaxUID.equals(ImplicitVRLittleEndian)) {
            description = "Implicit VR Little Endian";
            bigEndian = false;
            explicitVR = false;
            encapsulatedPixelData = false;
            lossy = false;
        } else if (transferSyntaxUID.equals(ExplicitVRLittleEndian)) {
            description = "Explicit VR Little Endian";
            bigEndian = false;
            explicitVR = true;
            encapsulatedPixelData = false;
            lossy = false;
        } else if (transferSyntaxUID.equals(ExplicitVRBigEndian)) {
            description = "Explicit VR Big Endian";
            bigEndian = true;
            explicitVR = true;
            encapsulatedPixelData = false;
            lossy = false;
        } else if (transferSyntaxUID.equals(EncapsulatedRawLittleEndian)) {
            description = "Encapsulated Raw Little Endian";
            bigEndian = false;
            explicitVR = true;
            encapsulatedPixelData = true;
            lossy = false;
        } else if (transferSyntaxUID.equals(JPEGBaseline)) {
            description = "JPEG Baseline";
            bigEndian = false;
            explicitVR = true;
            encapsulatedPixelData = true;
            lossy = true;
        } else if (transferSyntaxUID.equals(JPEGExtended)) {
            description = "JPEG Extended";
            bigEndian = false;
            explicitVR = true;
            encapsulatedPixelData = true;
            lossy = true;
        } else if (transferSyntaxUID.equals(JPEG2000Lossless)) {
            description = "JPEG 2000 Lossless Only";
            bigEndian = false;
            explicitVR = true;
            encapsulatedPixelData = true;
            lossy = false;
        } else if (transferSyntaxUID.equals(JPEG2000)) {
            description = "JPEG 2000";
            bigEndian = false;
            explicitVR = true;
            encapsulatedPixelData = true;
            lossy = true;
        } else if (transferSyntaxUID.equals(JPEGLossless)) {
            description = "JPEG Lossless";
            bigEndian = false;
            explicitVR = true;
            encapsulatedPixelData = true;
            lossy = false;
        } else if (transferSyntaxUID.equals(JPEGLosslessSV1)) {
            description = "JPEG Lossless SV1";
            bigEndian = false;
            explicitVR = true;
            encapsulatedPixelData = true;
            lossy = false;
        } else if (transferSyntaxUID.equals(JPEGLS)) {
            description = "JPEG-LS";
            bigEndian = false;
            explicitVR = true;
            encapsulatedPixelData = true;
            lossy = false;
        } else if (transferSyntaxUID.equals(JPEGNLS)) {
            description = "JPEG-LS Near-lossless";
            bigEndian = false;
            explicitVR = true;
            encapsulatedPixelData = true;
            lossy = true;
        } else if (transferSyntaxUID.equals(MPEG2MPML)) {
            description = "MPEG2 MPML";
            bigEndian = false;
            explicitVR = true;
            encapsulatedPixelData = true;
            lossy = true;
        }
    }

    /**
	 * <p>
	 * Construct a Transfer Syntax using the specified UID and characteristics.
	 * </p>
	 * 
	 * @param uid
	 *            the UID to use to refer to this transfer syntax
	 * @param desc
	 *            the description of this transfer syntax
	 * @param explicit
	 *            true if an explicit VR transfer syntax
	 * @param big
	 *            true if big-endian transfer syntax
	 * @param encapsulated
	 *            true if a pixel data encapsulated transfer syntax
	 */
    TransferSyntax(String uid, String desc, boolean explicit, boolean big, boolean encapsulated) {
        transferSyntaxUID = uid;
        description = desc;
        bigEndian = big;
        explicitVR = explicit;
        encapsulatedPixelData = encapsulated;
        lossy = false;
    }

    /**
	 * <p>
	 * Construct a Transfer Syntax using the specified UID and characteristics.
	 * </p>
	 * 
	 * @param uid
	 *            the UID to use to refer to this transfer syntax
	 * @param desc
	 *            the description of this transfer syntax
	 * @param explicit
	 *            true if an explicit VR transfer syntax
	 * @param big
	 *            true if big-endian transfer syntax
	 * @param encapsulated
	 *            true if a pixel data encapsulated transfer syntax
	 * @param lossy
	 *            true if lossy compression
	 */
    TransferSyntax(String uid, String desc, boolean explicit, boolean big, boolean encapsulated, boolean lossy) {
        transferSyntaxUID = uid;
        description = desc;
        bigEndian = big;
        explicitVR = explicit;
        encapsulatedPixelData = encapsulated;
        this.lossy = lossy;
    }

    /**
	 * <p>
	 * Get the UID of the Transfer Syntax.
	 * </p>
	 * 
	 * @return the UID of the Transfer Syntax
	 */
    public String getUID() {
        return transferSyntaxUID;
    }

    /**
	 * <p>
	 * Get a human-readable description of the Transfer Syntax.
	 * </p>
	 * 
	 * @return the description of the Transfer Syntax
	 * @uml.property name="description"
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * <p>
	 * Is the Transfer Syntax big endian ?
	 * </p>
	 * 
	 * @return true if big endian
	 */
    public boolean isBigEndian() {
        return bigEndian;
    }

    /**
	 * <p>
	 * Is the Transfer Syntax little endian ?
	 * </p>
	 * 
	 * @return true if little endian
	 */
    public boolean isLittleEndian() {
        return !bigEndian;
    }

    /**
	 * <p>
	 * Is the Transfer Syntax explicit VR ?
	 * </p>
	 * 
	 * @return true if explicit VR
	 */
    public boolean isExplicitVR() {
        return explicitVR;
    }

    /**
	 * <p>
	 * Is the Transfer Syntax implicit VR ?
	 * </p>
	 * 
	 * @return true if implicit VR
	 */
    public boolean isImplicitVR() {
        return !explicitVR;
    }

    /**
	 * <p>
	 * Does the Transfer Syntax encapsulate the pixel data ?
	 * </p>
	 * 
	 * @return true if encapsulate
	 */
    public boolean isEncapsulated() {
        return encapsulatedPixelData;
    }

    /**
	 * <p>
	 * Does the Transfer Syntax encode the pixel data without encapsulation?
	 * </p>
	 * 
	 * @return true if not encapsulated
	 */
    public boolean isNotEncapsulated() {
        return !encapsulatedPixelData;
    }

    /**
	 * <p>
	 * Is the Transfer Syntax potentially lossy ?
	 * </p>
	 * 
	 * @return true if lossy
	 */
    public boolean isLossy() {
        return lossy;
    }

    /**
	 * <p>
	 * Does the Transfer Syntax use deflate compression ?
	 * </p>
	 * 
	 * @return true if deflated
	 */
    public boolean isDeflated() {
        return transferSyntaxUID.equals(DeflatedExplicitVRLittleEndian);
    }

    /**
	 * <p>
	 * Does the Transfer Syntax use bzip2 compression ?
	 * </p>
	 * 
	 * @return true if bzip2
	 */
    public boolean isBzip2ed() {
        return transferSyntaxUID.equals(Bzip2ExplicitVRLittleEndian);
    }

    /**
	 * <p>
	 * Get the Transfer Syntax as a string.
	 * </p>
	 * 
	 * @return the UID of the Transfer Syntax
	 */
    public String toString() {
        return transferSyntaxUID;
    }

    /**
	 * <p>
	 * Is the Transfer Syntax with the specified UID explicit VR ?
	 * </p>
	 * 
	 * @param uid
	 * @return true if explicit VR
	 */
    public static boolean isExplicitVR(String uid) {
        return !isImplicitVR(uid);
    }

    /**
	 * <p>
	 * Is the Transfer Syntax with the specified UID implicit VR ?
	 * </p>
	 * 
	 * @param uid
	 * @return true if explicit VR
	 */
    public static boolean isImplicitVR(String uid) {
        return uid.equals(ImplicitVRLittleEndian);
    }

    /**
	 * <p>
	 * Is the Transfer Syntax with the specified UID big endian ?
	 * </p>
	 * 
	 * @param uid
	 * @return true if big endian
	 */
    public static boolean isBigEndian(String uid) {
        return uid.equals(ExplicitVRBigEndian);
    }

    /**
	 * <p>
	 * Is the Transfer Syntax with the specified UID little endian ?
	 * </p>
	 * 
	 * @param uid
	 * @return true if little endian
	 */
    public static boolean isLittleEndian(String uid) {
        return !isBigEndian(uid);
    }
}

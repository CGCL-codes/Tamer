package BluRayWhiteBoxLevel1.BluRayWhiteBoxLevel2BluRayModel.Classes;

/** 
 * <!-- begin-UML-doc -->
 * <p>The BLURAY driver for handling the media output processing and TIME.</p>
 * <!-- end-UML-doc -->
 * @author s4h
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class BluRayModel implements IBluRayModel {

    /** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private MediaFactory mediaFactory;

    /** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private ClockManager clockManager;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An attribute that points to a reference of MEDIA.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private Media media;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation that creates the TIMEMANAGER object.</p><p></p>
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public ClockManager createTimeManager() {
        clockManager = new ClockManager();
        return clockManager;
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation that creates a MEDIAFACTORY object.</p>
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public MediaFactory createMediaFactory() {
        mediaFactory = new MediaFactory();
        if (mediaFactory == null) {
            System.err.println("MediaFactory object not created in BluRayModel!");
        }
        return mediaFactory;
    }

    /** 
	 * (non-Javadoc)
	 * @see IBluRayModel#createMedia()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void createMedia() {
        if (mediaFactory == null) {
            System.err.println("MediaFactory reference in BluRayModel is null!");
            return;
        }
        Disc disc = mediaFactory.getDisc();
        if (disc == null) {
            System.err.println("Disc error in MediaFactory.");
            return;
        }
        Media Tmedia = mediaFactory.createMedia(disc, disc.getType());
        if (Tmedia == null) {
            System.err.println("Media creation error in MediaFactory.");
            return;
        }
        media = Tmedia;
    }

    /** 
	 * (non-Javadoc)
	 * @see IBluRayModel#setTime(double time)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void setTime(double time) {
        if (clockManager == null) {
            System.err.println("ClockManager not initalized in BluRayModel");
            return;
        } else {
            clockManager.updateTime(time);
        }
    }

    /** 
	 * (non-Javadoc)
	 * @see IBluRayModel#getTime()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public double getTime() {
        if (clockManager == null) {
            System.err.println("ClockManager not initalized in BluRayModel");
            return -1;
        } else if (clockManager.getClock() == null) {
            System.err.println("Clock not initialized.  Returning");
            return -1;
        } else {
            return clockManager.getTime();
        }
    }

    /** 
	 * (non-Javadoc)
	 * @see IBluRayModel#processCommand(String type)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void processCommand(String type) {
    }

    /** 
	 * (non-Javadoc)
	 * @see IBluRayModel#inputDisc()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void inputDisc() {
    }

    /** 
	 * (non-Javadoc)
	 * @see IBluRayModel#getAudio()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public AudioData getAudio() {
        if (media != null) {
            return media.getAudio();
        } else {
            System.err.println("Media not initialized in BluRayModel");
            return null;
        }
    }

    /** 
	 * (non-Javadoc)
	 * @see IBluRayModel#getVideo()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public VideoData getVideo() {
        if (media != null) {
            return media.getVideo();
        } else {
            System.err.println("Media not initialized in BluRayModel");
            return null;
        }
    }
}

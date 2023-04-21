package BluRayWhiteBoxLevel1.BluRayWhiteBoxLevel2BluRayModel.BluRayModelTester.Classes;

import static org.junit.Assert.*;
import org.junit.Test;
import BluRayWhiteBoxLevel1.BluRayWhiteBoxLevel2BluRayModel.Classes.BluRayModel;
import BluRayWhiteBoxLevel1.BluRayWhiteBoxLevel2BluRayModel.Classes.ClockManager;
import BluRayWhiteBoxLevel1.BluRayWhiteBoxLevel2BluRayModel.Classes.MediaFactory;

/** 
 * <!-- begin-UML-doc -->
 * <p>A class that tests the BluRayModel Class.</p>
 * <!-- end-UML-doc -->
 * @author s4h
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class BluRayModelTester {

    /** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private BluRayModel bluRay;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation used to test createMedia in BLURAYMODEL class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    @Test
    public void testCreateMedia() {
        bluRay = new BluRayModel();
        bluRay.createMedia();
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation used to test setTime in BLURAYMODEL class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    @Test
    public void testSetTime() {
        ClockManager clockManager;
        bluRay = new BluRayModel();
        clockManager = bluRay.createTimeManager();
        clockManager.createClock();
        bluRay.setTime(25.0);
        assertEquals(bluRay.getTime(), 25.0, 0.1);
        bluRay = new BluRayModel();
        clockManager = bluRay.createTimeManager();
        clockManager.createClock();
        bluRay.setTime(25.0);
        bluRay.setTime(-22.3);
        assertEquals(bluRay.getTime(), 25.0, 0.1);
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation used to test getTime in BLURAYMODEL class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    @Test
    public void testGetTime() {
        ClockManager clockManager;
        bluRay = new BluRayModel();
        clockManager = bluRay.createTimeManager();
        clockManager.createClock();
        bluRay.setTime(5.0);
        assertEquals(bluRay.getTime(), 5.0, 0.1);
        bluRay = new BluRayModel();
        clockManager = bluRay.createTimeManager();
        bluRay.setTime(5.0);
        assertEquals(bluRay.getTime(), -1.0, 0.1);
        bluRay = new BluRayModel();
        bluRay.setTime(5.0);
        assertEquals(bluRay.getTime(), -1.0, 0.1);
        bluRay = new BluRayModel();
        clockManager = bluRay.createTimeManager();
        clockManager.createClock();
        assertEquals(bluRay.getTime(), 0.0, 0.1);
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation used to test processCommand in BLURAYMODEL class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void testProcessCommand() {
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation used to test createTimeManager in BLURAYMODEL class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    @Test
    public void testCreateTimeManager() {
        bluRay = new BluRayModel();
        assertNotNull(bluRay.createTimeManager());
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation used to test createMediaFactory in BLURAYMODEL class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    @Test
    public void testCreateMediaFactory() {
        bluRay = new BluRayModel();
        assertNotNull(bluRay.createMediaFactory());
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation used to test getAudio in BLURAYMODEL class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    @Test
    public void testGetAudio() {
        MediaFactory mediaFactory;
        bluRay = new BluRayModel();
        mediaFactory = bluRay.createMediaFactory();
        mediaFactory.createDiscDrive();
        FakeDISC disc = new FakeDISC();
        mediaFactory.setDisc(disc);
        disc.CTOR();
        assertTrue(disc.hasAudio());
        assertNotNull(bluRay.getAudio());
        bluRay = new BluRayModel();
        mediaFactory = bluRay.createMediaFactory();
        mediaFactory.createDiscDrive();
        disc = new FakeDISC();
        mediaFactory.setDisc(disc);
        assertNull(bluRay.getAudio());
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation used to test getVideo in BLURAYMODEL class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void testGetVideo() {
        MediaFactory mediaFactory;
        bluRay = new BluRayModel();
        mediaFactory = bluRay.createMediaFactory();
        mediaFactory.createDiscDrive();
        FakeDISC disc = (FakeDISC) mediaFactory.getDisc();
        disc.CTOR();
        assertNotNull(bluRay.getVideo());
        bluRay = new BluRayModel();
        mediaFactory = bluRay.createMediaFactory();
        mediaFactory.createDiscDrive();
        disc = (FakeDISC) mediaFactory.getDisc();
        assertNull(bluRay.getVideo());
    }
}

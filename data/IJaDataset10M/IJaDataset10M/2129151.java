package GUI.GUIChairMaintenance;

import org.junit.After;
import org.junit.Before;
import junit.framework.TestCase;

public class ChairClassJUnit extends TestCase {

    private ChairClass chairClass;

    @Before
    public void setUp() {
        String chairId = "4";
        chairClass = new ChairClass(chairId);
    }

    @After
    public void tearDown() {
        chairClass = null;
    }

    public void testCameraButton_actionPerformed() {
        assertTrue(chairClass.testCameraButton_actionPerformed());
    }

    public void testContactAdminActionPerformed() {
        assertTrue(chairClass.testContactAdminActionPerformed());
    }
}

package net.sourceforge.insim4j.insim.cameracontrol;

import static org.junit.Assert.assertEquals;
import java.nio.ByteBuffer;
import net.sourceforge.insim4j.insim.cameracontrol.InSimSetCarCamera;
import net.sourceforge.insim4j.insim.enums.PacketType;
import net.sourceforge.insim4j.insim.enums.View;
import org.junit.Before;
import org.junit.Test;

public class InSimSetCarCameraTest {

    private InSimSetCarCamera fScc;

    private final int fViewPLID;

    private final View fInGameCam;

    public InSimSetCarCameraTest() {
        fViewPLID = 66;
        fInGameCam = View.VIEW_DRIVER;
    }

    @Before
    public void setUp() throws Exception {
        fScc = new InSimSetCarCamera(fViewPLID, fInGameCam);
    }

    @Test
    public void testGetViewPLID() {
        assertEquals(fViewPLID, fScc.getViewPLID());
    }

    @Test
    public void testGetInGameCam() {
        assertEquals(fInGameCam, fScc.getInGameCam());
    }

    @Test
    public void testCompile() {
        final ByteBuffer buf = fScc.compile();
        buf.rewind();
        assertEquals((byte) 8, buf.get());
        assertEquals(PacketType.ISP_SCC.getValue(), buf.get());
        assertEquals((byte) 0, buf.get());
        assertEquals((byte) 0, buf.get());
        assertEquals((byte) fViewPLID, buf.get());
        assertEquals((byte) fInGameCam.getValue(), buf.get());
    }
}

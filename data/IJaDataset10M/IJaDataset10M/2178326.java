package org.mobicents.media.server.impl.resource.mediaplayer.audio.gsm;

import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kulikov
 */
public class GsmTrackImplTest {

    private GsmTrackImpl track;

    public GsmTrackImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setPeriod method, of class GsmTrackImpl.
     */
    @Test
    public void testDuration() throws Exception {
        URL url = GsmTrackImplTest.class.getClassLoader().getResource("org/mobicents/media/server/impl/cnfannouncement.gsm");
        track = new GsmTrackImpl(url);
        assertEquals(6420, track.getDuration());
    }
}

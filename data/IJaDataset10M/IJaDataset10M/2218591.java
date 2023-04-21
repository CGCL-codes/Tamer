package net.sf.cplab.headtracker.wiimote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wiiremotej.AccelerationConstants;
import wiiremotej.WiiRemote;

/**
 * @author jtse
 *
 */
public class MockWiiRemote extends WiiRemote {

    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(MockWiiRemote.class);

    public MockWiiRemote() {
        super(new AccelerationConstants(0, 0, 0, 0, 0, 0));
    }
}

package org.echarts.test.sip;

import org.echarts.test.sip.SIPAgent;
import javax.sdp.Connection;
import javax.sdp.SdpFactory;
import javax.sdp.SdpParseException;
import javax.sdp.SessionDescription;
import org.hamcrest.Description;
import org.junit.matchers.*;

public class BlackHoleSDPMatcher extends TypeSafeMatcher<SIPAgent> {

    private static SdpFactory sdpFactory;

    static {
        sdpFactory = SdpFactory.getInstance();
    }

    @Override
    public boolean matchesSafely(SIPAgent agent) {
        boolean hasBlackHole = false;
        try {
            final SessionDescription sessionDescription = sdpFactory.createSessionDescription(agent.getLastRecvdSDPAsString());
            final Connection connection = sessionDescription.getConnection();
            if (connection != null) {
                final String connectionAddress = connection.getAddress();
                hasBlackHole = "0.0.0.0".equals(connectionAddress);
            }
            return hasBlackHole;
        } catch (SdpParseException e) {
            return false;
        }
    }

    public void describeTo(Description description) {
        description.appendText("SDP should have connection address of 0.0.0.0");
    }
}

package org.echarts.test.sip;

import org.hamcrest.Description;
import org.junit.matchers.*;

class ConnectedToMatcher extends TypeSafeMatcher<SIPAgent> {

    SIPAgent peerAgent1;

    SIPAgent peerAgent2;

    String descriptionText;

    public ConnectedToMatcher(SIPAgent agent) {
        this.peerAgent1 = agent;
    }

    @Override
    public boolean matchesSafely(SIPAgent agent) {
        peerAgent2 = agent;
        if (peerAgent1.getCurrentState().equals(CATState.CallEstablished) && peerAgent2.getCurrentState().equals(CATState.CallEstablished)) {
            String peer1RemoteSDP = peerAgent1.getLastRecvdSDPAsString();
            String peer2RemoteSDP = peerAgent2.getLastRecvdSDPAsString();
            String peer2IP = SDPUtils.getRTPIP(peer1RemoteSDP);
            int peer2Port = SDPUtils.getRTPPort(peer1RemoteSDP);
            String peer1IP = SDPUtils.getRTPIP(peer2RemoteSDP);
            int peer1Port = SDPUtils.getRTPPort(peer2RemoteSDP);
            if (peer1IP.equals(peerAgent1.getRTPIP()) && (peer1Port == peerAgent1.getRTPPort()) && peer2IP.equals(peerAgent2.getRTPIP()) && (peer2Port == peerAgent2.getRTPPort())) {
                return true;
            }
        }
        return false;
    }

    public void describeTo(Description description) {
        description.appendText("agent " + peerAgent1.getName() + " is connected with agent " + peerAgent2.getName());
    }
}

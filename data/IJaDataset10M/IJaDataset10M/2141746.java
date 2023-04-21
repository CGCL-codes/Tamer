package ch.iserver.ace.net.discovery;

import junit.framework.TestCase;
import org.easymock.MockControl;
import ch.iserver.ace.UserDetails;
import ch.iserver.ace.net.NetworkServiceCallback;
import ch.iserver.ace.net.core.Discovery;
import ch.iserver.ace.net.core.DiscoveryCallback;
import ch.iserver.ace.net.core.DiscoveryCallbackImpl;
import ch.iserver.ace.net.core.NetworkProperties;
import ch.iserver.ace.net.protocol.filter.LogFilter;
import ch.iserver.ace.net.protocol.filter.RequestFilter;
import com.apple.dnssd.TXTRecord;

public class UserRegistrationTest extends TestCase {

    private MockControl peerDiscoveryCtrl;

    private UserRegistration registration;

    private String username, userid;

    public void testUserRegistrationAndUserDetailsUpdate() throws Exception {
        final String USER = "testuser";
        final String USER_ID = "test-id_1";
        username = USER;
        userid = USER_ID;
        MockControl networkServiceCallbackCtrl = MockControl.createControl(NetworkServiceCallback.class);
        NetworkServiceCallback nsc = (NetworkServiceCallback) networkServiceCallbackCtrl.getMock();
        RequestFilter filter = new LogFilter(null, false);
        DiscoveryCallback callback = new DiscoveryCallbackImpl(nsc, null, filter);
        Discovery discovery = createDiscovery(callback);
        discovery.setUserDetails(new UserDetails(USER));
        discovery.setUserId(USER_ID);
        discovery.execute();
        peerDiscoveryCtrl.verify();
        Thread.sleep(3000);
        assertTrue(registration.isRegistered());
        TXTRecord rec = ((UserRegistrationImpl) registration).getTXTRecord();
        assertEquals(NetworkProperties.get(NetworkProperties.KEY_TXT_VERSION), TXTRecordProxy.get(TXTRecordProxy.TXT_VERSION, rec));
        assertEquals(NetworkProperties.get(NetworkProperties.KEY_PROTOCOL_VERSION), TXTRecordProxy.get(TXTRecordProxy.TXT_PROTOCOL_VERSION, rec));
        registration.stop();
        assertFalse(registration.isRegistered());
    }

    public Discovery createDiscovery(DiscoveryCallback callback) {
        registration = new UserRegistrationImpl();
        peerDiscoveryCtrl = MockControl.createControl(PeerDiscovery.class);
        PeerDiscovery discovery = (PeerDiscovery) peerDiscoveryCtrl.getMock();
        discovery.browse();
        peerDiscoveryCtrl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
        peerDiscoveryCtrl.replay();
        Bonjour b = new Bonjour(registration, discovery);
        return b;
    }
}

package net.sf.xspecs.oomatron.acceptance;

import net.sf.xspecs.oomatron.Protocol;
import net.sf.xspecs.oomatron.ProtocolState;
import net.sf.xspecs.oomatron.ProtocolTestCase;
import net.sf.xspecs.oomatron.Role;

public class TransitionCheckAcceptanceTest extends ProtocolTestCase {

    interface I {

        void f();
    }

    public void testDoesNotAllowTransitionsBetweenDifferentProtocols() {
        Protocol p1 = newProtocol("p1");
        ProtocolState s1 = p1.newState("s1");
        Protocol p2 = newProtocol("p2");
        ProtocolState s2 = p2.newState("s2");
        Role r = newRole(I.class, "r");
        try {
            s1.on(r).method("f").withNoArguments().becomes(s2);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
        }
    }
}

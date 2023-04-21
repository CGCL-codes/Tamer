package org.eledge.authentication;

import static org.eledge.Eledge.create;
import static org.eledge.Eledge.dataContext;
import static org.eledge.Eledge.delete;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.RequestContext;
import org.easymock.MockControl;
import org.eledge.AbstractCDOTest;
import org.eledge.EledgeEngine;
import org.eledge.EledgeVisit;
import org.eledge.authentication.SimpleAuthentication;
import org.eledge.domain.User;
import org.rz.utils.MDFiver;

/**
 * @author robertz
 * 
 */
public class SimpleAuthenticationTest extends AbstractCDOTest {

    EledgeVisit visit;

    User u;

    SimpleAuthentication auth;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        visit = new EledgeVisit();
        u = create(User.class);
        u.setUserId("teststud");
        u.setLogin("teststud");
        u.setFirstName("Test");
        u.setLastName("Student");
        u.setPassword("testpass");
        dataContext().commitChanges();
        auth = new SimpleAuthentication();
    }

    @Override
    public void tearDown() {
        delete(u);
        dataContext().commitChanges();
    }

    public void testLogin() {
        auth = new SimpleAuthentication();
        MockControl cycleCtrl = MockControl.createControl(IRequestCycle.class);
        MockControl engineCtl = MockControl.createControl(IEngine.class);
        IEngine engine = (IEngine) engineCtl.getMock();
        IRequestCycle cycle = (IRequestCycle) cycleCtrl.getMock();
        cycleCtrl.expectAndReturn(cycle.getEngine(), engine, MockControl.ZERO_OR_MORE);
        engineCtl.expectAndReturn(engine.getVisit(cycle), visit, MockControl.ZERO_OR_MORE);
        engineCtl.expectAndReturn(engine.getVisit(), visit, MockControl.ZERO_OR_MORE);
        cycleCtrl.replay();
        engineCtl.replay();
        EledgeEngine.setEngine(engine);
        try {
            assertFalse(auth.login("test", "test", cycle, false));
            assertTrue(auth.login(u.getLogin(), u.getPassword(), cycle, false));
            cycleCtrl.verify();
            engineCtl.verify();
            assertNotNull(visit.getUser());
            assertTrue(visit.getUser().isAuthentic());
            assertNotNull(visit.getUser().getObjectContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void testIsAuthenticated() {
        MockControl cycleCtrl = MockControl.createControl(IRequestCycle.class);
        MockControl engineCtl = MockControl.createControl(IEngine.class);
        IEngine engine = (IEngine) engineCtl.getMock();
        IRequestCycle cycle = (IRequestCycle) cycleCtrl.getMock();
        cycleCtrl.expectAndReturn(cycle.getEngine(), engine, MockControl.ZERO_OR_MORE);
        MockRequestContext ctxt;
        try {
            ctxt = new MockRequestContext(null, null, null);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
        cycleCtrl.expectAndReturn(cycle.getRequestContext(), ctxt, MockControl.ZERO_OR_MORE);
        engineCtl.expectAndReturn(engine.getVisit(cycle), visit, MockControl.ZERO_OR_MORE);
        engineCtl.expectAndReturn(engine.getVisit(), visit, MockControl.ZERO_OR_MORE);
        cycleCtrl.replay();
        engineCtl.replay();
        EledgeEngine.setEngine(engine);
        u.setAuthentic(true);
        visit.setUser(u);
        assertTrue(auth.isAuthenticated(cycle));
        visit.setUser(null);
        assertFalse(auth.isAuthenticated(cycle));
        ctxt.addCookie("EledgeSaltyCookie", MDFiver.toMD5Sum(u.getLogin() + u.getPassword()));
        assertTrue(auth.isAuthenticated(cycle));
        cycleCtrl.verify();
        engineCtl.verify();
    }

    public void testLogout() {
        visit.setUser(u);
        u.setAuthentic(true);
        MockControl cycleCtrl = MockControl.createControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cycleCtrl.getMock();
        MockControl engineCtl = MockControl.createControl(IEngine.class);
        IEngine engine = (IEngine) engineCtl.getMock();
        cycleCtrl.expectAndReturn(cycle.getEngine(), engine, MockControl.ZERO_OR_MORE);
        System.out.println(visit);
        engineCtl.expectAndReturn(engine.getVisit(), visit, MockControl.ZERO_OR_MORE);
        EledgeEngine.setEngine(engine);
        MockRequestContext ctxt;
        try {
            ctxt = new MockRequestContext(null, null, null);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
        cycleCtrl.expectAndReturn(cycle.getRequestContext(), ctxt, 2);
        ctxt.addCookie("EledgeSaltyCookie", MDFiver.toMD5Sum(u.getLogin() + u.getPassword()));
        cycleCtrl.replay();
        engineCtl.replay();
        auth.logout(cycle);
        assertFalse(visit.isAuthenticated());
        assertFalse(visit.getUser().isAuthentic());
        assertNull(ctxt.getCookie("EledgeSaltyCookie"));
    }

    public void testGetSupportsLongTermPersistence() {
        assertTrue(auth.getSupportsLongTermPersistence());
    }
}

class MockRequestContext extends RequestContext {

    private HashMap<String, Cookie> cookies = new HashMap<String, Cookie>();

    MockRequestContext(ApplicationServlet servlet, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super(servlet, request, response);
    }

    @Override
    public Cookie getCookie(String name) {
        return cookies.get(name);
    }

    @Override
    public void addCookie(String name, String value) {
        Cookie c = new Cookie(name, value);
        c.setMaxAge(50);
        cookies.put(name, c);
    }

    @Override
    public void addCookie(Cookie c) {
        if (c.getMaxAge() <= 0) {
            cookies.remove(c.getName());
        } else {
            cookies.put(c.getName(), c);
        }
    }
}

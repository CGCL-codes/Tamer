package org.t2framework.t2.contexts.impl;

import javax.servlet.http.HttpSession;
import junit.framework.TestCase;
import org.t2framework.commons.mock.MockHttpServletRequest;
import org.t2framework.commons.mock.MockHttpServletRequestImpl;
import org.t2framework.commons.mock.MockHttpServletResponse;
import org.t2framework.commons.mock.MockHttpServletResponseImpl;
import org.t2framework.commons.mock.MockServletContextImpl;
import org.t2framework.t2.contexts.impl.RequestImpl;
import org.t2framework.t2.contexts.impl.SessionImpl;

public class SessionImplTest extends TestCase {

    public void test1() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequestImpl(new MockServletContextImpl("path"), "reqpath");
        MockHttpServletResponse res = new MockHttpServletResponseImpl(req);
        RequestImpl request = new RequestImpl(req, res);
        SessionImpl session = new SessionImpl(request);
        session.setAttribute("a", "A");
        assertEquals("A", session.getAttribute("a"));
        HttpSession ses = req.getSession(true);
        assertEquals("A", ses.getAttribute("a"));
    }
}

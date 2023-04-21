package com.herestudio;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.easymock.MockControl;
import org.easymock.internal.Range;
import com.herestudio.util.IngoredMatcher;

public class MockExample {

    public static void main(String[] args) {
        MockControl controlRequest = MockControl.createControl(HttpServletRequest.class);
        MockControl controlResponse = MockControl.createControl(HttpServletResponse.class);
        MockControl controlSession = MockControl.createControl(HttpSession.class);
        org.easymock.internal.Range range = new Range(1, Integer.MAX_VALUE);
        IngoredMatcher ingoredMatcher = new IngoredMatcher();
        HttpServletRequest mockRequest = (HttpServletRequest) controlRequest.getMock();
        HttpServletResponse mockResponse = (HttpServletResponse) controlResponse.getMock();
        HttpSession mockSession = (HttpSession) controlSession.getMock();
        mockRequest.getParameter("test");
        controlRequest.setMatcher(ingoredMatcher);
        controlRequest.setReturnValue("test-var", range);
        mockRequest.getRemoteAddr();
        controlRequest.setReturnValue("localhost", range);
        mockRequest.getSession();
        controlRequest.setReturnValue(mockSession, range);
        mockRequest.getCookies();
        controlRequest.setReturnValue(new Cookie[] {}, range);
        mockSession.setAttribute(HahereConstant.USER_LOGIN_ID, "");
        controlSession.setMatcher(ingoredMatcher);
        controlSession.setVoidCallable(range);
        Cookie rv = new Cookie("tc", "c1");
        mockResponse.addCookie(rv);
        controlResponse.setMatcher(ingoredMatcher);
        controlResponse.setVoidCallable(range);
        controlRequest.replay();
        controlResponse.replay();
        controlSession.replay();
        System.out.println(mockRequest.getParameter("test"));
        System.out.println(mockRequest.getParameter("test2"));
        System.out.println(mockRequest.getCookies().length);
        mockRequest.getSession();
        mockSession.setAttribute(HahereConstant.USER_LOGIN_ID, "1");
        mockResponse.addCookie(rv);
        Cookie rv2 = new Cookie("tc", "c2");
        mockResponse.addCookie(rv2);
    }
}

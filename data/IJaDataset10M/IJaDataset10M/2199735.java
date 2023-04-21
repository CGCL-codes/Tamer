package org.perfmon4j.servlet;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.NDC;
import org.perfmon4j.util.Logger;
import org.perfmon4j.util.LoggerFactory;
import org.perfmon4j.util.MiscHelper;

public class PerfMonNDCFilter extends PerfMonFilter {

    private static final Logger logger = LoggerFactory.initLogger(PerfMonNDCFilter.class);

    public static final String PROPERTY_PUSH_URL_ON_NDC = "PUSH_URL_ON_NDC";

    public static final String PROPERTY_PUSH_CLIENT_INFO = "NDC_PUSH_CLIENT_INFO";

    public static final String PROPERTY_PUSH_COOKIES = "NDC_PUSH_COOKIES";

    public static final String PROPERTY_PUSH_SESSION_ATTRIBUTES = "NDC_PUSH_SESSION_ATTRIBUTES";

    private boolean pushURLOnNDC = false;

    private boolean pushClientInfo = false;

    private String[] pushCookies = null;

    private String[] pushSessionAttributes = null;

    public PerfMonNDCFilter() {
        super();
    }

    public PerfMonNDCFilter(boolean childOfValve) {
        super(childOfValve);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        pushURLOnNDC = Boolean.parseBoolean(getInitParameter(filterConfig, PROPERTY_PUSH_URL_ON_NDC, Boolean.FALSE.toString()));
        pushClientInfo = Boolean.parseBoolean(getInitParameter(filterConfig, PROPERTY_PUSH_CLIENT_INFO, Boolean.FALSE.toString()));
        pushCookies = MiscHelper.tokenizeCSVString(getInitParameter(filterConfig, PROPERTY_PUSH_COOKIES, null));
        pushSessionAttributes = MiscHelper.tokenizeCSVString(getInitParameter(filterConfig, PROPERTY_PUSH_SESSION_ATTRIBUTES, null));
    }

    private String getCookieValue(String name, Cookie cookies[]) {
        String result = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (name.equals(cookies[i].getName())) {
                    return cookies[i].getValue();
                }
            }
        }
        return result;
    }

    private String buildCookieString(Cookie cookies[]) {
        StringBuilder result = new StringBuilder();
        final boolean useWildCard = pushCookies.length == 1 && "*".equals(pushCookies[0]);
        if (useWildCard) {
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    if (i > 0) {
                        result.append(" ");
                    }
                    result.append(cookies[i].getName()).append(":").append(cookies[i].getValue());
                }
            }
        } else {
            for (int i = 0; i < pushCookies.length; i++) {
                if (i > 0) {
                    result.append(" ");
                }
                result.append(pushCookies[i]).append(":").append(getCookieValue(pushCookies[i], cookies));
            }
        }
        return result.toString();
    }

    private String buildSessionString(HttpSession session) {
        StringBuilder result = new StringBuilder();
        final boolean useWildCard = pushSessionAttributes.length == 1 && "*".equals(pushSessionAttributes[0]);
        if (useWildCard) {
            if (session != null) {
                Enumeration<String> e = session.getAttributeNames();
                int i = 0;
                while (e.hasMoreElements()) {
                    if (i++ > 0) {
                        result.append(" ");
                    }
                    String name = e.nextElement();
                    result.append(name).append(":").append(session.getAttribute(name));
                }
            }
        } else {
            for (int i = 0; i < pushSessionAttributes.length; i++) {
                if (i > 0) {
                    result.append(" ");
                }
                String attributeValue = null;
                if (session != null) {
                    attributeValue = (String) session.getAttribute(pushSessionAttributes[i]);
                }
                result.append(pushSessionAttributes[i]).append(":").append(attributeValue);
            }
        }
        return result.toString();
    }

    protected void doFilterHttpRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean pushedNDC = false;
        try {
            if (pushURLOnNDC || pushClientInfo || (pushCookies != null) || (pushSessionAttributes != null)) {
                StringBuilder myNDC = new StringBuilder();
                if (pushURLOnNDC) {
                    myNDC.append(buildRequestDescription(request));
                }
                if (pushClientInfo) {
                    if (myNDC.length() > 0) {
                        myNDC.append(" ");
                    }
                    myNDC.append(request.getRemoteAddr());
                    String xForwardedFor = request.getHeader("X-Forwarded-For");
                    if (xForwardedFor != null) {
                        myNDC.append("[").append(xForwardedFor).append("]");
                    }
                }
                if (pushCookies != null) {
                    Cookie cookies[] = request.getCookies();
                    String cookieString = buildCookieString(cookies);
                    if (cookieString.length() > 0) {
                        if (myNDC.length() > 0) {
                            myNDC.append(" ");
                        }
                        myNDC.append(cookieString);
                    }
                }
                if (pushSessionAttributes != null) {
                    HttpSession session = request.getSession(false);
                    String sessionString = buildSessionString(session);
                    if (sessionString.length() > 0) {
                        if (myNDC.length() > 0) {
                            myNDC.append(" ");
                        }
                        myNDC.append(sessionString);
                    }
                }
                if (myNDC.length() > 0) {
                    NDC.push(myNDC.toString());
                    pushedNDC = true;
                }
            }
            super.doFilterHttpRequest(request, response, chain);
        } finally {
            if (pushedNDC) {
                NDC.pop();
            }
        }
    }
}

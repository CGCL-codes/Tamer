package org.ironrhino.core.session;

import java.net.URLDecoder;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import org.ironrhino.core.security.util.RC4;

public class WrappedHttpServletRequest extends HttpServletRequestWrapper {

    private WrappedHttpSession session;

    public WrappedHttpServletRequest(HttpServletRequest request, WrappedHttpSession session) {
        super(request);
        this.session = session;
    }

    @Override
    public HttpSession getSession() {
        return session;
    }

    @Override
    public HttpSession getSession(boolean create) {
        return session;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return session.isRequestedSessionIdFromCookie();
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return session.isRequestedSessionIdFromURL();
    }

    @Override
    @Deprecated
    public boolean isRequestedSessionIdFromUrl() {
        return isRequestedSessionIdFromURL();
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return true;
    }

    @Override
    public String getRequestedSessionId() {
        return session.getId();
    }

    @Override
    public Locale getLocale() {
        Locale locale = session.getHttpSessionManager().getLocale((HttpServletRequest) this.getRequest());
        return locale;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return decryptIfNecessary(name, value);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String name = entry.getKey();
            String[] value = entry.getValue();
            for (int i = 0; i < value.length; i++) value[i] = decryptIfNecessary(name, value[i]);
        }
        return map;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] value = super.getParameterValues(name);
        for (int i = 0; i < value.length; i++) value[i] = decryptIfNecessary(name, value[i]);
        return value;
    }

    private String decryptIfNecessary(String name, String value) {
        if (value != null && name.toLowerCase().endsWith("password") && value.length() >= 20) {
            String key = session.getSessionTracker();
            if (key.length() < 25) return value;
            try {
                key = key.substring(15, 25);
                String str = URLDecoder.decode(RC4.decrypt(value, key), "UTF-8");
                if (str.endsWith(key)) value = str.substring(0, str.length() - key.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}

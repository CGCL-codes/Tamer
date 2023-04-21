package dryven.sapi.servlet_v25;

import javax.servlet.http.HttpSession;
import dryven.request.http.Session;

public class ServletSessionWrapper implements Session {

    private HttpSession _servletSession;

    public ServletSessionWrapper(HttpSession servletSession) {
        super();
        _servletSession = servletSession;
    }

    @Override
    public long getLastAccessedTime() {
        return _servletSession.getLastAccessedTime();
    }

    @Override
    public int getMaxInactiveInterval() {
        return _servletSession.getMaxInactiveInterval();
    }

    @Override
    public Object getValue(String name) {
        return _servletSession.getAttribute(name);
    }

    @Override
    public void removeValue(String name) {
        _servletSession.removeAttribute(name);
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        _servletSession.setMaxInactiveInterval(interval);
    }

    @Override
    public void setValue(String name, Object value) {
        _servletSession.setAttribute(name, value);
    }
}

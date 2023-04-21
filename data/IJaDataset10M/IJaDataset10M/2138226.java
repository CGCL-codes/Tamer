package org.simpleframework.http;

import java.util.List;
import org.simpleframework.util.net.ContentType;
import org.simpleframework.util.net.Cookie;
import org.simpleframework.util.net.Path;
import org.simpleframework.util.net.Query;

public class MockProxyRequest extends MockRequest {

    private RequestHeader header;

    private State state;

    public MockProxyRequest(RequestHeader header) {
        this.state = new ActiveState(header.getValues("Cookie"));
        this.header = header;
    }

    public void add(String name, String value) {
        header.add(name, value);
    }

    public int getContentLength() {
        return header.getContentLength();
    }

    public ContentType getContentType() {
        return header.getContentType();
    }

    public String getValue(String name) {
        return header.getValue(name);
    }

    public List<String> getValues(String name) {
        return header.getValues(name);
    }

    public void remove(String name) {
        header.remove(name);
    }

    public void set(String name, String value) {
        header.set(name, value);
    }

    public void set(String name, int value) {
        header.set(name, value);
    }

    public int getMajor() {
        return header.getMajor();
    }

    public String getMethod() {
        return header.getMethod();
    }

    public int getMinor() {
        return header.getMajor();
    }

    public Path getPath() {
        return header.getPath();
    }

    public Query getQuery() {
        return header.getQuery();
    }

    public String getTarget() {
        return header.getTarget();
    }

    public void setMajor(int major) {
        header.setMajor(major);
    }

    public void setMethod(String method) {
        header.setMethod(method);
    }

    public void setMinor(int minor) {
        header.setMinor(minor);
    }

    public void setTarget(String target) {
        header.setTarget(target);
    }

    public String getParameter(String name) {
        return header.getQuery().get(name);
    }

    public Cookie getCookie(String name) {
        return state.getCookie(name);
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}

package net.infopeers.restrant.engine.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import net.infopeers.restrant.engine.params.AbstractParams;
import net.infopeers.restrant.util.gae.GaeExtensionParamPolicy;

public class TestParams extends AbstractParams {

    private HashMap<String, String> ps = new HashMap<String, String>();

    private String method;

    public TestParams() {
        super(new GaeExtensionParamPolicy());
    }

    @Override
    public String getParameter(String key) {
        return ps.get(key);
    }

    @Override
    public String[] getParameters(String key) {
        return null;
    }

    @Override
    public Set<String> nameSet() {
        Set<String> names = getExtensionNames();
        for (String key : ps.keySet()) {
            names.add(key);
        }
        return names;
    }

    public void addParams(String key, String value) {
        ps.put(key, value);
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public boolean isStreamContent() {
        return false;
    }

    @Override
    public String getContentType() {
        return null;
    }
}

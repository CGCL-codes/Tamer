package org.apache.myfaces.trinidadbuild.test;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.Application;

public class MockELContext extends ELContext {

    public MockELContext(Application application) {
        _resolver = new MockELResolver(application.getVariableResolver(), application.getPropertyResolver());
    }

    public Object getContext(Class key) {
        return _contexts.get(key);
    }

    public ELResolver getELResolver() {
        return _resolver;
    }

    public FunctionMapper getFunctionMapper() {
        return null;
    }

    public Locale getLocale() {
        return _locale;
    }

    public VariableMapper getVariableMapper() {
        throw new UnsupportedOperationException();
    }

    public boolean isPropertyResolved() {
        return _propertyResolved;
    }

    public void putContext(Class key, Object contextObject) {
        _contexts.put(key, contextObject);
    }

    public void setLocale(Locale locale) {
        _locale = locale;
    }

    public void setPropertyResolved(boolean resolved) {
        _propertyResolved = resolved;
    }

    private boolean _propertyResolved;

    private Locale _locale;

    private final Map<Class<?>, Object> _contexts = new HashMap<Class<?>, Object>();

    private final ELResolver _resolver;
}

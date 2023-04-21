package scrobblerj;

import java.util.HashMap;

public abstract class Proxy {

    private boolean fecthed;

    private HashMap<String, Object> properties;

    public Proxy() {
        properties = new HashMap<String, Object>();
    }

    protected final void setFecthed(boolean fecthed) {
        this.fecthed = fecthed;
    }

    protected final Object getProperty(String name) {
        if (!fecthed) fetch();
        return properties.get(name);
    }

    protected final void setProperty(String name, Object object) {
        properties.put(name, object);
    }

    protected abstract void fetch();

    final boolean isFecthed() {
        return fecthed;
    }
}

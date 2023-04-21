package org.simpleframework.util.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CookieMap extends HashMap<String, Cookie> {

    public CookieMap() {
        super();
    }

    public CookieMap(int capacity) {
        super(capacity);
    }

    public Cookie get(Object name) {
        return super.get(name);
    }

    public Cookie remove(Object name) {
        return super.remove(name);
    }

    public List<Cookie> values() {
        return new ArrayList<Cookie>(super.values());
    }
}

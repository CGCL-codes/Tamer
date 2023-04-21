package uncertain.composite;

import java.util.Map;

/**
 * CaseInsensitveMap
 * @author Zhou Fan
 * 
 */
public class CaseInsensitiveMap extends CompositeMap {

    /**
     * 
     */
    public CaseInsensitiveMap() {
        super();
    }

    /**
     * @param _name
     */
    public CaseInsensitiveMap(String _name) {
        super(_name);
    }

    /**
     * @param _prefix
     * @param _uri
     * @param _name
     */
    public CaseInsensitiveMap(String _prefix, String _uri, String _name) {
        super(_prefix, _uri, _name);
    }

    /**
     * @param size
     */
    public CaseInsensitiveMap(int size) {
        super(size);
    }

    /**
     * @param size
     * @param load_factor
     */
    public CaseInsensitiveMap(int size, float load_factor) {
        super(size, load_factor);
    }

    /**
     * @param another
     */
    public CaseInsensitiveMap(CompositeMap another) {
        super(another);
    }

    /**
     * @param name
     * @param map
     */
    public CaseInsensitiveMap(String name, Map map) {
        super(name, map);
    }

    public Object put(Object key, Object value) {
        if (key != null) if (key instanceof String) {
            return super.put(((String) key).toLowerCase(), value);
        }
        return super.put(key, value);
    }

    public CompositeMap createChild(String prefix, String uri, String name) {
        CaseInsensitiveMap m = new CaseInsensitiveMap(prefix, uri, name);
        addChild(m);
        return m;
    }

    public Object get(Object key) {
        if (key != null) if (key instanceof String) {
            return super.get(((String) key).toLowerCase());
        }
        return super.get(key);
    }
}

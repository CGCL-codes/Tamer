package net.sf.brico.cmd.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.sf.brico.cmd.Response;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class DefaultResponse implements Response {

    private static final long serialVersionUID = 4739263583107229311L;

    private Map attributes;

    public DefaultResponse() {
        this.attributes = new HashMap();
    }

    public boolean contains(String name) {
        return this.attributes.containsKey(name);
    }

    public Object get(String name) {
        return this.attributes.get(name);
    }

    public Object get(String name, Object defaultValue) {
        Object value = this.attributes.get(name);
        return (value != null) ? value : defaultValue;
    }

    public Map getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public Object remove(String name) {
        return this.attributes.remove(name);
    }

    public void set(String name, Object value) {
        this.attributes.put(name, value);
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(attributes).toString();
    }
}

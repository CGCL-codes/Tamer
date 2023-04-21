package jmud.engine.attribute;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AttributeMap {

    /**
	 * A mapping of Attribute names to Attribute references
	 */
    private Map<String, Attribute> attributeMap = Collections.synchronizedMap(new HashMap<String, Attribute>());

    public boolean containsAttribute(String attrName) {
        return this.attributeMap.containsKey(attrName);
    }

    public boolean containsAttribute(Attribute a) {
        return this.attributeMap.containsValue(a);
    }

    public Attribute getAttribute(String attrName) {
        return this.attributeMap.get(attrName);
    }

    public Set<String> getAttributeNames() {
        return this.attributeMap.keySet();
    }

    public Set<Attribute> getAttributes() {
        return (Set<Attribute>) this.attributeMap.values();
    }

    public Attribute addAttribute(String name, Attribute a) {
        return this.attributeMap.put(name, a);
    }

    public Attribute remAttribute(String name) {
        return this.attributeMap.remove(name);
    }
}

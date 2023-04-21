package org.yaoqiang.bpmn.model.elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * XMLCollection
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public abstract class XMLCollection extends XMLElement {

    protected LinkedHashMap<String, XMLElement> elements = new LinkedHashMap<String, XMLElement>();

    public XMLCollection(XMLElement parent) {
        super(parent);
    }

    public XMLCollection(XMLElement parent, String name) {
        super(parent, name);
    }

    public abstract XMLElement generateNewElement();

    public abstract String getElementName();

    public void set(String name, String value) {
        XMLElement el = get(name);
        if (el != null) {
            el.setValue(value);
        } else {
            throw new RuntimeException("No such element!");
        }
    }

    public void add(XMLElement el) {
        if (el == null) {
            return;
        }
        if (el instanceof XMLComplexElement && ((XMLComplexElement) el).get("id") != null && !((XMLComplexElement) el).get("id").toValue().equals("")) {
            elements.put(((XMLComplexElement) el).get("id").toValue(), el);
        } else {
            int id = elements.size();
            while (contains(String.valueOf(id))) {
                id++;
            }
            elements.put(String.valueOf(id), el);
        }
        el.setParent(this);
    }

    public void addAll(List<XMLElement> els) {
        if (els != null && els.size() > 0) {
            for (XMLElement el : els) {
                add(el);
            }
        }
    }

    public XMLElement remove(String id) {
        return elements.remove(id);
    }

    public XMLElement getCollectionElement(String id) {
        for (XMLElement ce : elements.values()) {
            if (((XMLComplexElement) ce).get("id").toValue().equals(id)) {
                return ce;
            }
        }
        return null;
    }

    public int size() {
        return elements.size();
    }

    public void clear() {
        elements.clear();
    }

    public boolean isEmpty() {
        return elements.size() == 0;
    }

    public List<XMLElement> toElements() {
        return new ArrayList<XMLElement>(elements.values());
    }

    public List<XMLElement> getXMLElements() {
        List<XMLElement> els = new ArrayList<XMLElement>();
        for (XMLElement el : elements.values()) {
            if (!(el instanceof XMLAttribute)) {
                els.add(el);
            }
        }
        return els;
    }

    public String createId(String baseId) {
        int num = 1;
        String id = baseId + "_" + num;
        while (getCollectionElement(id) != null) {
            num++;
            id = baseId + "_" + num;
        }
        return id;
    }

    public XMLElement get(String name) {
        return elements.get(name);
    }

    public boolean contains(String id) {
        return elements.containsKey(id);
    }

    public Object clone() {
        XMLCollection d = (XMLCollection) super.clone();
        d.elements = new LinkedHashMap<String, XMLElement>();
        for (Map.Entry<String, XMLElement> entry : elements.entrySet()) {
            String key = new String(entry.getKey());
            XMLElement value = (XMLElement) entry.getValue().clone();
            value.setParent(d);
            d.elements.put(key, value);
        }
        return d;
    }
}

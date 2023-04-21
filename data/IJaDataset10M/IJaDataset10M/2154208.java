package uk.ac.cam.caret.minibix.taggy;

import java.util.*;
import org.xml.sax.*;

public class TaggySchema extends Packaged {

    private String name, namespace = "http://www.caret.cam.ac.uk/taggy";

    private Map<String, TaggyClass> classes = new HashMap<String, TaggyClass>();

    private Map<String, TaggyClass> class_cache = new HashMap<String, TaggyClass>();

    public String getName() {
        return name;
    }

    public void setName(String in) {
        name = in;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String in) {
        namespace = in;
    }

    public TaggyClass getTaggyClass(String type) {
        return classes.get(type);
    }

    public void addTaggyClass(TaggyClass in) {
        in.setParent(this);
        classes.put(in.getType(), in);
    }

    void remap() {
        Map<String, TaggyClass> out = new HashMap<String, TaggyClass>();
        for (TaggyClass klass : classes.values()) out.put(klass.getType(), klass);
        classes = out;
    }

    TaggySchema duplicate() {
        TaggySchema out = new TaggySchema();
        out.name = name;
        out.namespace = namespace;
        out.classes.putAll(classes);
        return out;
    }

    void addSubSchema(TaggySchema in) {
        class_cache = new HashMap<String, TaggyClass>();
        classes.putAll(in.classes);
    }

    public TaggySerializer createSerializer() {
        return new TaggySerializer(this);
    }

    void serializeInner(Object in, ContentHandler out, ObjectStack stack, TaggySerializer serial) throws Unserializable, SAXException {
        TaggyClass inner = getTaggyClassFor(in);
        if (inner == null) throw new Unserializable("No way to serialize return in " + in.getClass().getName() + " (was a " + in.getClass().getName() + ")", in);
        inner.serializeThis(in, out, stack, serial);
    }

    private TaggyClass getTaggyClassFor(Object in) {
        String name = in.getClass().getName();
        TaggyClass out = class_cache.get(name);
        if (out != null) return out;
        out = getTaggyClassForCacheMiss(in);
        class_cache.put(name, out);
        return out;
    }

    TaggyClass getTaggyClassForCacheMiss(Object in) {
        for (Class type : Utils.allClasses(in.getClass())) {
            TaggyClass out = classes.get(type.getName());
            if (out != null) return out;
        }
        return null;
    }
}

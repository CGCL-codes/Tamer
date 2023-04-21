package net.sf.sql2java.common.configuration.xml;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dge2
 */
public class ReferenceHelper {

    Map<String, Class> references = new HashMap<String, Class>();

    public void addReference(Class reference) {
        XmlTag tag = (XmlTag) reference.getAnnotation(XmlTag.class);
        references.put(tag.value(), reference);
    }

    public Map<String, Class> getReferences() {
        return references;
    }
}

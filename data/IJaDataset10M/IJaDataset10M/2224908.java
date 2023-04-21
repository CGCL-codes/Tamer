package org.wikiup.modules.lucene.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.document.Fieldable;
import org.wikiup.core.impl.iterable.GenericCastIterable;
import org.wikiup.core.inf.Attribute;
import org.wikiup.core.inf.Document;
import org.wikiup.core.util.ValueUtil;

public class LuceneDocument implements Document {

    private Map<String, LuceneAttribute> attributes = new HashMap<String, LuceneAttribute>();

    public LuceneDocument(org.apache.lucene.document.Document doc) {
        for (Fieldable fieldable : doc.getFields()) attributes.put(fieldable.name(), new LuceneAttribute(fieldable.name(), fieldable));
    }

    public Document getChild(String name) {
        return null;
    }

    public Document addChild(String name) {
        return null;
    }

    public Iterable<Document> getChildren(String name) {
        return null;
    }

    public Iterable<Document> getChildren() {
        return null;
    }

    public void removeNode(Document child) {
    }

    public Document getParentNode() {
        return null;
    }

    public Attribute getAttribute(String name) {
        return attributes.get(name);
    }

    public Attribute addAttribute(String name) {
        return null;
    }

    public void removeAttribute(Attribute attr) {
    }

    public Iterable<Attribute> getAttributes() {
        return new GenericCastIterable<Attribute>(attributes.values());
    }

    public String getName() {
        return null;
    }

    public void setName(String name) {
    }

    public Object getObject() {
        return null;
    }

    public void setObject(Object obj) {
    }

    private static class LuceneAttribute implements Attribute {

        private String name;

        private Fieldable fieldable;

        private LuceneAttribute(String name, Fieldable fieldable) {
            this.name = name;
            this.fieldable = fieldable;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getObject() {
            return fieldable.stringValue();
        }

        public void setObject(Object obj) {
        }

        @Override
        public String toString() {
            return ValueUtil.toString(getObject(), "");
        }
    }
}

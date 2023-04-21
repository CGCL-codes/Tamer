package org.perfmon4j;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class ObjectID {

    private final String className;

    private final Properties attributes;

    private String attributeString = null;

    private Integer hashCode = null;

    protected ObjectID(String className) {
        this(className, null);
    }

    protected ObjectID(String className, Properties attributes) {
        this.className = className;
        this.attributes = attributes;
    }

    public int hashCode() {
        if (hashCode == null) {
            hashCode = generateHashCode();
        }
        return hashCode.intValue();
    }

    protected String getAttributeString() {
        if (attributeString == null) {
            attributeString = buildAttributeString();
            if (attributeString == null) {
                attributeString = "";
            }
        }
        return attributeString;
    }

    protected String buildAttributeString() {
        String result = "";
        if (attributes != null && attributes.size() > 0) {
            Set sortedKeys = new TreeSet(attributes.keySet());
            Iterator itr = sortedKeys.iterator();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                result += "KEY:(" + key + ")VALUE:(" + attributes.getProperty(key) + ")";
            }
        }
        return result;
    }

    private Integer generateHashCode() {
        return new Integer(className.hashCode() + getAttributeString().hashCode());
    }

    public boolean equals(Object obj) {
        boolean result = (this == obj);
        if (!result) {
            if (this.getClass().isInstance(obj)) {
                ObjectID id = (ObjectID) obj;
                result = (hashCode() == id.hashCode()) && className.equals(id.className) && getAttributeString().equals(id.getAttributeString());
            }
        }
        return result;
    }

    public String getClassName() {
        return className;
    }

    public Properties getAttributes() {
        Properties result = new Properties();
        if (attributes != null) {
            result.putAll(attributes);
        }
        return result;
    }
}

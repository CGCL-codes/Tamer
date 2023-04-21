package org.vardb.util.mongo.resources.dao;

import org.vardb.util.mongo.resources.dao.CFamily.FamilyAttribute;
import com.mongodb.BasicDBObject;

@SuppressWarnings("serial")
public class CResource extends BasicDBObject {

    public enum ResourceAttribute {

        identifier
    }

    ;

    public CResource() {
    }

    public CResource(String identifier) {
        setIdentifier(identifier);
    }

    public void setIdentifier(String identifier) {
        setAttribute(ResourceAttribute.identifier, identifier);
    }

    public String getIdentifier() {
        return (String) getAttribute(ResourceAttribute.identifier);
    }

    public void setAttribute(String name, Object value) {
        super.put(name, value);
    }

    public Object getAttribute(String name) {
        return super.get(name);
    }

    public void setAttribute(Enum attribute, Object value) {
        put(attribute.name(), value);
    }

    public Object getAttribute(Enum attribute) {
        return get(attribute.name());
    }
}

package org.wikiup.modules.velocity.context;

import org.apache.velocity.context.Context;
import org.wikiup.core.exception.AttributeException;
import org.wikiup.database.orm.inf.EntityModel;

public class WikiupEntityVelocityContext implements Context {

    private EntityModel entity;

    public WikiupEntityVelocityContext(EntityModel entity) {
        this.entity = entity;
    }

    public boolean containsKey(Object object) {
        try {
            return entity.get(object.toString()) != null;
        } catch (Exception ex) {
            return false;
        }
    }

    public Object get(String s) {
        try {
            return entity.get(s);
        } catch (AttributeException ex) {
            return new DocumentContextCollection(entity.getRelatives(s, null));
        }
    }

    public Object[] getKeys() {
        return null;
    }

    public Object put(String s, Object obj) {
        try {
            entity.get(s).setObject(obj);
        } catch (Exception ex) {
        }
        return obj;
    }

    public Object remove(Object obj) {
        return null;
    }
}

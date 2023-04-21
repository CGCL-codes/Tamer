package net.godcode.olivenotes.entities;

import java.io.Serializable;
import java.util.Date;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.TransientObjectException;
import org.hibernate.type.Type;
import org.slf4j.Logger;

/**
 * EntityInterceptor is an extremely simple interceptor that automatically
 * time stamps entities being saved or updated, if they have properties
 * 'created' and 'modified', respectively.
 * 
 * @author Chris Lewis 19/gen/08 <chris@thegodcode.net>
 * @version $Id: EntityInterceptor.java 26 2008-01-19 22:50:09Z burningodzilla $
 */
public class EntityInterceptor extends EmptyInterceptor {

    private Logger log;

    private Session session;

    public EntityInterceptor(Session session, Logger log) {
        this.session = session;
        this.log = log;
    }

    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        boolean modified = false;
        Date dateModified = new Date();
        if (isTransient(entity)) {
            modified = modifyProperty("created", dateModified, state, propertyNames);
        }
        return modifyProperty("modified", dateModified, state, propertyNames) || modified;
    }

    public Boolean isTransient(Object entity) {
        try {
            return this.session.getIdentifier(entity) == null;
        } catch (TransientObjectException e) {
            return true;
        }
    }

    /**
	 * Modify a property in an entity state array.
	 * @param prop the property name to modify
	 * @param value the value to assign
	 * @param state the current entity state array
	 * @param propertyNames the current entity property array
	 * @return <code>true</code> if a modification was made, <code>false</code> if not
	 */
    protected boolean modifyProperty(String prop, Object value, Object[] state, String[] propertyNames) {
        boolean modified = false;
        for (int i = 0; i < propertyNames.length; i++) {
            if (propertyNames[i].equals(prop)) {
                if (state[i] != value) {
                    state[i] = value;
                    modified = true;
                }
            }
        }
        return modified;
    }
}

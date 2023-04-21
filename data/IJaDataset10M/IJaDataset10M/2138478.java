package org.objectstyle.cayenne.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.objectstyle.cayenne.PersistenceState;
import org.objectstyle.cayenne.Persistent;
import org.objectstyle.cayenne.map.EntityResolver;
import org.objectstyle.cayenne.map.ObjEntity;
import org.objectstyle.cayenne.map.ObjRelationship;
import org.objectstyle.cayenne.property.Property;
import org.objectstyle.cayenne.query.RelationshipQuery;

/**
 * An abstract superlcass of lazily faulted to-one and to-many relationships.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
public abstract class RelationshipFault {

    protected Persistent relationshipOwner;

    protected String relationshipName;

    protected RelationshipFault() {
    }

    public RelationshipFault(Persistent relationshipOwner, String relationshipName) {
        if (relationshipOwner == null) {
            throw new NullPointerException("'relationshipOwner' can't be null.");
        }
        if (relationshipName == null) {
            throw new NullPointerException("'relationshipName' can't be null.");
        }
        this.relationshipOwner = relationshipOwner;
        this.relationshipName = relationshipName;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public Persistent getRelationshipOwner() {
        return relationshipOwner;
    }

    protected boolean isTransientParent() {
        int state = relationshipOwner.getPersistenceState();
        return state == PersistenceState.NEW || state == PersistenceState.TRANSIENT;
    }

    protected boolean isUncommittedParent() {
        int state = relationshipOwner.getPersistenceState();
        return state == PersistenceState.MODIFIED || state == PersistenceState.DELETED;
    }

    /**
     * Executes a query that returns related objects. Subclasses would invoke this method
     * whenever they need to resolve a fault.
     */
    protected List resolveFromDB() {
        if (isTransientParent()) {
            return new ArrayList();
        }
        List resolved = relationshipOwner.getObjectContext().performQuery(new RelationshipQuery(relationshipOwner.getObjectId(), relationshipName, false));
        if (resolved.isEmpty()) {
            return resolved;
        }
        EntityResolver resolver = relationshipOwner.getObjectContext().getEntityResolver();
        ObjEntity sourceEntity = resolver.lookupObjEntity(relationshipOwner.getObjectId().getEntityName());
        ObjRelationship relationship = (ObjRelationship) sourceEntity.getRelationship(relationshipName);
        ObjRelationship reverse = relationship.getReverseRelationship();
        if (reverse != null && !reverse.isToMany()) {
            Property property = resolver.getClassDescriptor(reverse.getSourceEntity().getName()).getProperty(reverse.getName());
            Iterator it = resolved.iterator();
            while (it.hasNext()) {
                property.writePropertyDirectly(it.next(), null, relationshipOwner);
            }
        }
        return resolved;
    }
}

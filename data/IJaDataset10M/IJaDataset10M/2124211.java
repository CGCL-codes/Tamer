package org.wocommunity.wovng.utility;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;

/**
 * Methods added to extend the functionality of EOObject
 *
 * @author Copyright (c) 2001-2009  Global Village Consulting, Inc.  All rights reserved.
 * @version $Revision: 11$
 */
public class EOObject {

    /**
     * Static methods only.  You'll never need to instantiate this class.
     */
    private EOObject() {
        super();
    }

    /**
     * Returns the <code>EOEntity</code> associated with the object.
     *
     * @param theObject the object to be evaluated
     * @return the <code>EOEntity</code> associated with the object.
     */
    public static EOEntity entityForSelf(EOEnterpriseObject theObject) {
        return EOUtilities.entityNamed(theObject.editingContext(), theObject.entityName());
    }

    /**
     * Convenience method that returns the global ID of the object.
     *
     * @param theObject the object to be evaluated
     * @return the globalID of the object.
     */
    public static EOGlobalID globalID(EOEnterpriseObject theObject) {
        return theObject.editingContext().globalIDForObject(theObject);
    }

    /**
     * Returns the <code>EOAttribute</code> (from the object's associated <code>EOEntity</code>) which corresponds to the passed name.
     *
     * @param theObject the object to be evaluated
     * @param attributeName the attribute name of the <code>EOAttribute</code> to return
     * @return the <code>EOAttribute</code> (from the object's associated <code>EOEntity</code>) which corresponds to the passed name.
     */
    public static EOAttribute attributeWithName(EOEnterpriseObject theObject, String attributeName) {
        return EOObject.entityForSelf(theObject).attributeNamed(attributeName);
    }

    /**
     * Returns the <code>EORelationship</code> (from the object's associated <code>EOEntity</code>) which corresponds to the passed name.
     *
     * @param theObject the object to be evaluated
     * @param relationshipName the name of the relationship to be fetched
     * @return the <code>EORelationship</code> (from the object's associated <code>EOEntity</code>) which corresponds to the passed name.
     */
    public static EORelationship relationshipWithName(EOEnterpriseObject theObject, String relationshipName) {
        return EOObject.entityForSelf(theObject).relationshipNamed(relationshipName);
    }

    /**
     * Returns the <code>EOAttribute</code> or <code>EORelationship</code> (from the object's associated <code>EOEntity</code>) which corresponds to the passed name.
     *
     * @param theObject the object to be evaluated
     * @param propertyName the property name of the EOAttribute or EORelationship
     * @return the <code>EOAttribute</code> or <code>EORelationship</code> (from the object's associated <code>EOEntity</code>) which corresponds to the passed name.
     */
    public static EOProperty propertyWithName(EOEnterpriseObject theObject, String propertyName) {
        EOProperty theProperty = EOObject.entityForSelf(theObject).attributeNamed(propertyName);
        if (theProperty == null) {
            theProperty = EOObject.entityForSelf(theObject).relationshipNamed(propertyName);
        }
        return theProperty;
    }

    /**
     * Private method to support <code>keyPathHasEntity()</code> and <code>entityFromKeyPath()</code>
     *
     * @param theObject the object to be evaluated
     * @param aKeyPath the key path of the entity
     * @return the EOEntity associated with the object and key path
     */
    protected static EOEntity _findEntityFromKeyPath(Object theObject, String aKeyPath) {
        EOEntity entity = null;
        String objectKeyPath = StringAdditions.objectKeyPathFromKeyPath(aKeyPath);
        try {
            EOEnterpriseObject object = (EOEnterpriseObject) NSKeyValueCodingAdditions.Utility.valueForKeyPath(theObject, objectKeyPath);
            entity = entityForSelf(object);
        } catch (Exception e) {
            entity = null;
        }
        return entity;
    }

    /**
     * Returns true if <code>StringAdditions.objectKeyPathFromKeyPath(aKeyPath)</code> points to an object associated with an <code>EOEntity</code>.
     *
     * @param theObject the object to be evaluated
     * @param aKeyPath the key path of the entity
     * @return true if <code>StringAdditions.objectKeyPathFromKeyPath(aKeyPath)</code> points to an object associated with an <code>EOEntity</code>.
     */
    public static boolean keyPathHasEntity(Object theObject, String aKeyPath) {
        return _findEntityFromKeyPath(theObject, aKeyPath) != null;
    }

    /**
     * Returns the <code>EOEntity</code> instance associated with the object pointed to by <code>StringAdditions.objectKeyPathFromKeyPath(aKeyPath)</code>
     *
     * @param theObject the object to be evaluated
     * @param aKeyPath the key path of the entity
     * @return the <code>EOEntity</code> associated with the object and key path
     */
    public static EOEntity entityFromKeyPath(Object theObject, String aKeyPath) {
        return _findEntityFromKeyPath(theObject, aKeyPath);
    }

    /**
     * Private method to support <code>attributeFromKeyPath</code> and <code>isKeyPathToAttribute</code>
     *
     * @param theObject the object to be evaluated
     * @param aKeyPath the key path of the entity
     * @return the <code>EOAttribute</code> associated with the object and key path
     */
    protected static EOAttribute _findAttributeFromKeyPath(Object theObject, String aKeyPath) {
        EOEntity entity = entityFromKeyPath(theObject, aKeyPath);
        String anAttributeName = StringAdditions.propertyNameFromKeyPath(aKeyPath);
        return entity.attributeNamed(anAttributeName);
    }

    /**
     * Returns true if <code>_findAttributeFromKeyPath(theObject, aKeyPath)</code> points to an object associated with an EOAttribute.
     *
     * @param theObject the object to be evaluated
     * @param aKeyPath the key path of the attribute
     * @return true if the passed key path is a key path to the attribute
     */
    public static boolean isKeyPathToAttribute(Object theObject, String aKeyPath) {
        if (keyPathHasEntity(theObject, aKeyPath)) {
            return _findAttributeFromKeyPath(theObject, aKeyPath) != null;
        }
        return false;
    }

    /**
     * Given a keypath to an attribute of an object on a page, returns the corresponding <code>EOAttribute</code>.
     *
     * @param theObject the object to be evaluated
     * @param aKeyPath the key path of the attribute
     * @return the <code>EOAttribute</code> associated with the key path
     */
    public static EOAttribute attributeFromKeyPath(Object theObject, String aKeyPath) {
        return _findAttributeFromKeyPath(theObject, aKeyPath);
    }

    /**
     * Private method to support <code>relationshipFromKeyPath()</code>
     *
     * @param theObject the object to be evaluated
     * @param aKeyPath the key path of the relationship
     * @return the <code>EORelationship</code>
     */
    protected static EORelationship _findRelationshipFromKeyPath(Object theObject, String aKeyPath) {
        EOEntity entity = entityFromKeyPath(theObject, aKeyPath);
        String aRelationshipName = StringAdditions.propertyNameFromKeyPath(aKeyPath);
        return entity.relationshipNamed(aRelationshipName);
    }

    /**
     * Returns true if <code>objectKeyPathFromKeyPath(theObject, aKeyPath)</code> points to an object associated with an <code>EORelationship</code>.
     *
     * @param theObject the object to be evaluated
     * @param aKeyPath the key path of the relationship
     * @return true if <code>objectKeyPathFromKeyPath(theObject, aKeyPath)</code> points to an object associated with an <code>EORelationship</code>
     */
    public static boolean isKeyPathToRelationship(Object theObject, String aKeyPath) {
        if (keyPathHasEntity(theObject, aKeyPath)) {
            return _findRelationshipFromKeyPath(theObject, aKeyPath) != null;
        }
        return false;
    }

    /**
     * Given a keypath to a relationship of an object on a page, returns the corresponding <code>EORelationship</code>.
     *
     * @param theObject the object to be evaluated
     * @param aKeyPath the key path of the relationship
     * @return the <code>EORelationship</code> associated with the key path
     */
    public static EORelationship relationshipFromKeyPath(Object theObject, String aKeyPath) {
        return EOObject._findRelationshipFromKeyPath(theObject, aKeyPath);
    }

    /**
     * Forces this object to be re-read from the database; the snapshot and all editing contexts
     * holding this object are updated. Merging or overwriting of the changes is then handled by
     * the editing context delegate. This method takes no action if the object is pending insertion.
     *
     * @param anObject the object to be evaluated
     */
    public static void refreshObject(EOEnterpriseObject anObject) {
        EOEditingContext ec = anObject.editingContext();
        if (!ec.insertedObjects().containsObject(anObject)) {
            EOQualifier thisObject = EOUtilities.qualifierForEnterpriseObject(ec, anObject);
            EOFetchSpecification fetchSpec = new EOFetchSpecification(entityForSelf(anObject).name(), thisObject, NSArray.EmptyArray);
            fetchSpec.setRefreshesRefetchedObjects(true);
            anObject.editingContext().objectsWithFetchSpecification(fetchSpec);
        }
    }

    /**
     * Reverts any changes in this object's values back to the state last fetched.  This will not refetch the
     * data from the database.  This method takes no action if the object is pending insertion or deletion.
     *
     * @param anObject the object to be evaluated
     */
    public static void revertToSaved(EOEnterpriseObject anObject) {
        EOEditingContext ec = anObject.editingContext();
        if (!(ec.insertedObjects().containsObject(anObject) || ec.deletedObjects().containsObject(anObject))) {
            anObject.updateFromSnapshot(ec.committedSnapshotForObject(anObject));
        }
    }
}

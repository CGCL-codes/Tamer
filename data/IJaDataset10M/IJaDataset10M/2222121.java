package org.hip.kernel.bom;

import org.hip.kernel.bom.model.ObjectDef;

/**
 * The general domain object has the whole functionality of read
 * only domain objects (i.e. without insert and delete).
 *
 * @author	Benno Luthiger
 */
public interface GeneralDomainObject extends SemanticObject {

    /**
 * 	Used to accept a DomainObjectVisitor.
 * 
 * 	@param inVisitor org.hip.kernel.bom.DomainObjectVisitor
 */
    void accept(DomainObjectVisitor inVisitor);

    /**
 * 	This method returns the home for this domain object.
 *
 * 	@return org.hip.kernel.bom.GeneralDomainObjectHome
 */
    GeneralDomainObjectHome getHome();

    /**
 * This Method returns the class name of the home.
 * @return java.lang.String
 */
    String getHomeClassName();

    /**
 * This method returns the key of this DomainObject.
 * @return org.hip.kernel.bom.KeyObject
 */
    KeyObject getKey();

    /**
 * This method returns the key of this DomainObject.
 * The initial key object is returned if demanded or 
 * if this domain objects mode is changed, else the actual key is returned.
 *
 * @param inInitial boolean
 * @return org.hip.kernel.bom.KeyObject
 */
    KeyObject getKey(boolean inInitial);

    /**
 * 	Returns the ObjectDef for this object.
 * 
 * 	@return ObjectDef
 */
    ObjectDef getObjectDef();

    /**
 * 	Returns the object name.
 * 
 * 	@return java.lang.String
 */
    String getObjectName();

    /**
 * 	Returns true if any property has been changed.
 * 
 * 	@return boolean
 */
    boolean isChanged();

    /**
 * Releases this DomainObject.
 */
    void release();
}

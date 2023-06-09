package pcgen.cdom.base;

import java.util.Collection;
import pcgen.cdom.enumeration.AssociationKey;

/**
 * An AssociatedObject is an object which carries a set of Associations.
 * 
 * This is effectively a Map from an AssociationKey to a value, though the value
 * is "type safe" based on the Generic parameter on the AssocaitionKey.
 */
public interface AssociatedObject {

    /**
	 * Sets an Association (as defined by the given key) to the given value.
	 * Overwrites any previous value associated with the given AssociationKey.
	 * 
	 * @param <T>
	 *            The type of the AssociationKey and the Class of the object to
	 *            be associated with the given AssociationKey.
	 * @param key
	 *            The AssociationKey used to form the association with the given
	 *            value
	 * @param value
	 *            The value to be associated with the given AssociationKey
	 */
    public <T> void setAssociation(AssociationKey<T> key, T value);

    /**
	 * Returns the value associated with the given AssociationKey. Returns null
	 * if this AssociatedObject contains no association for the given
	 * AssociationKey.
	 * 
	 * @param <T>
	 *            The type of the AssociationKey and the Class of the object to
	 *            be returned
	 * @param key
	 *            The AssociationKey for which the associated value is to be
	 *            returned
	 * @return The value associated with the given AssociationKey.
	 */
    public <T> T getAssociation(AssociationKey<T> key);

    /**
	 * Returns a Collection of the AssociationKeys that are in this
	 * AssociatedObject.
	 * 
	 * It is intended that classes which implement the AssociatedObject
	 * interface will make this method value-semantic, meaning that ownership of
	 * the Collection returned by this method will be transferred to the calling
	 * object. Modification of the returned Collection should not result in
	 * modifying the AssociatedObject, and modifying the AssocaitedObject after
	 * the Collection is returned should not modify the Collection.
	 * 
	 * Note that it may be possible for an association to have a null value.
	 * This method should include the AssociationKey for that association, if it
	 * is present in the AssociatedObject, even if the value of the association
	 * is null.
	 * 
	 * @return a Collection of the AssociationKeys that are in this
	 *         AssociatedObject.
	 */
    public Collection<AssociationKey<?>> getAssociationKeys();

    /**
	 * Returns true if this AssociatedObject has any associations.
	 * 
	 * Note that it may be possible for an association to have a null value.
	 * This method should return true if the association is present, even if
	 * null.
	 * 
	 * @return true if this AssociatedObject has any associations; false
	 *         otherwise.
	 */
    public boolean hasAssociations();
}

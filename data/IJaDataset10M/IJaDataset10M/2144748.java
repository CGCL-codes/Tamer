package net.sf.jmoney.model2;

public abstract class ListPropertyAccessor<E extends ExtendableObject> extends PropertyAccessor {

    /**
	 * 
	 * the property set for the properties that are contained in the
	 * elements of this list.  Note that elements containing derived property
	 * sets may be added to the list.
	 */
    private ExtendablePropertySet<E> elementPropertySet;

    public ListPropertyAccessor(PropertySet parentPropertySet, String localName, String displayName, ExtendablePropertySet<E> elementPropertySet) {
        super(parentPropertySet, localName, displayName, elementPropertySet.classOfObject);
        this.elementPropertySet = elementPropertySet;
    }

    /**
	 * Indicates if the property is a list of objects.
	 */
    @Override
    public boolean isList() {
        return true;
    }

    /**
	 * Returns the class for the values in the lists. This is the class of
	 * the items contained in the collections returned by the getter method
	 * 
	 * @return
	 */
    public ExtendablePropertySet<E> getElementPropertySet() {
        return elementPropertySet;
    }

    /**
	 */
    public abstract ObjectCollection<E> getElements(ExtendableObject parentObject);
}

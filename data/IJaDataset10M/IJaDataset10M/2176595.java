package org.hip.kernel.bom.impl;

import java.util.Iterator;
import org.hip.kernel.bom.OrderItem;
import org.hip.kernel.bom.OrderObject;
import org.hip.kernel.util.AbstractSortedList;
import org.hip.kernel.util.SortableItem;
import org.hip.kernel.util.VInvalidSortCriteriaException;
import org.hip.kernel.util.VInvalidValueException;

/**
 * Implementation of the OrderObject interface.
 * 
 * Created on 13.09.2002
 * @author Benno Luthiger
 * @see org.hip.kernel.bom.OrderObject
 */
public class OrderObjectImpl extends AbstractSortedList implements OrderObject {

    private class ParameterObject extends Object {

        private String columnName;

        private boolean descending;

        private ParameterObject(String inColumnName, boolean inDescending) {
            super();
            columnName = inColumnName;
            descending = inDescending;
        }

        public String getColumnName() {
            return columnName;
        }

        public boolean isDescending() {
            return descending;
        }
    }

    /**
	 * Constructor for OrderObjectImpl.
	 */
    public OrderObjectImpl() {
        super();
    }

    /**
	 * @see org.hip.kernel.util.AbstractSortedList#create(Object, int)
	 */
    protected SortableItem create(Object inValue, int inPosition) throws VInvalidSortCriteriaException, VInvalidValueException {
        try {
            ParameterObject lParameter = (ParameterObject) inValue;
            return new OrderItemImpl(lParameter.getColumnName(), lParameter.isDescending(), inPosition);
        } catch (ClassCastException exc) {
            throw new VInvalidValueException();
        }
    }

    /**
	 * @see org.hip.kernel.util.AbstractSortedList#create(Object, Object)
	 */
    protected SortableItem create(Object inValue, Object inSortCriteria) throws VInvalidSortCriteriaException, VInvalidValueException {
        throw new VInvalidSortCriteriaException("SortCriteria not supported for OrderObject.");
    }

    /**
	 * This method sets an order item (i.e. column) with the specified name
	 * and default sort order (= ascending) at the specified position.
	 *
	 * @param inColumnName java.lang.String
	 * @param inPosition int
	 * @throws org.hip.kernel.util.VInvalidSortCriteriaException
	 * @throws org.hip.kernel.util.VInvalidValueException
	 */
    public void setValue(String inColumnName, int inPosition) throws VInvalidSortCriteriaException, VInvalidValueException {
        setValue(inColumnName, false, inPosition);
    }

    /**
	 * This method sets an order item (i.e. column) with the specified name
	 * and sort order at the specified position.
	 *
	 * @param inColumnName java.lang.String
	 * @param inDescending boolean
	 * @param inPosition int
	 * @throws org.hip.kernel.util.VInvalidSortCriteriaException
	 * @throws org.hip.kernel.util.VInvalidValueException
	 */
    public void setValue(String inColumnName, boolean inDescending, int inPosition) throws VInvalidSortCriteriaException, VInvalidValueException {
        add(create(new ParameterObject(inColumnName, inDescending), inPosition));
    }

    /**
	 * Compares all items of OrderObjects.
	 * 
	 * @return boolean
	 * @param inObject java.lang.Object
	 */
    public boolean equals(Object inObject) {
        if (inObject == null) return false;
        if (!(inObject instanceof OrderObject)) return false;
        OrderObject lOrderObject = ((OrderObject) inObject);
        if (lOrderObject.size() != size()) return false;
        Iterator<?> lThis = getItems();
        Iterator<?> lItems = lOrderObject.getItems2().iterator();
        while (lItems.hasNext()) {
            if (!((OrderItem) lItems.next()).equals(lThis.next())) return false;
        }
        return true;
    }

    /**
	 * Returns a hash code value for the order object.
	 *
	 * @return int
	 */
    public int hashCode() {
        int outHashCode = 0;
        for (Iterator<?> lItems = getItems(); lItems.hasNext(); ) {
            outHashCode ^= ((OrderItem) lItems.next()).hashCode();
        }
        return outHashCode;
    }
}

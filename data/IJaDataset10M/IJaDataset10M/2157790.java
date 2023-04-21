package org.hip.kernel.util;

/**
 * This is the default implementation for a visitor pattern to a name value list.
 */
public class DefaultNameValueListVisitor extends AbstractNameValueListVisitor {

    /**
	 * DefaultNameValueListVisitor default constructor.
	 */
    public DefaultNameValueListVisitor() {
        super();
    }

    /**
	 * @param inNameValue org.hip.kernel.util.NameValue
	 */
    public void visitNameValue(NameValue inNameValue) {
        String lName = inNameValue.getName() != null ? inNameValue.getName() : "?";
        String lValue;
        Object lValueObject = inNameValue.getValue();
        if (lValueObject != null) {
            lValue = lValueObject.toString();
            lValue = Debug.classMarkupString(lValueObject, "'" + lValue + "'");
        } else {
            lValue = "<value=?/>";
        }
        externalized += Debug.classMarkupString(inNameValue, "name='" + lName + "'", lValue);
    }

    /**
	 * @param inNameValueList org.hip.kernel.util.NameValueList
	 */
    public void visitNameValueList(NameValueList inNameValueList) {
        String lStore = externalized;
        externalized = "";
        for (NameValue lNameValue : inNameValueList.getNameValues2()) {
            lNameValue.accept(this);
            externalized += "\n";
        }
        externalized = lStore + Debug.classMarkupString(inNameValueList, "", externalized != "" ? externalized : "\t<values=?/>");
    }

    /**
	 * @param inSortableItem org.hip.kernel.util.SortableItem
	 */
    public void visitSortableItem(SortableItem inSortableItem) {
        String lValueString;
        Object lValue = inSortableItem.getValue();
        if (lValue != null) {
            lValueString = "<value='" + lValue.toString() + "' />";
        } else {
            lValueString = "<value=?/>";
        }
        externalized += Debug.classMarkupString(inSortableItem, lValueString);
    }

    /**
	 * @param inSortedList org.hip.kernel.util.SortedList
	 */
    public void visitSortedList(SortedList inSortedList) {
        String lStore = externalized;
        externalized = "";
        for (SortableItem lItem : inSortedList.getItems2()) {
            lItem.accept(this);
            externalized += "\n";
        }
        externalized = lStore + Debug.classMarkupString(inSortedList, "", externalized != "" ? externalized : "\t<values=?/>");
    }

    /**
	 * Clears the Visitor. If an instance of this Visitor is reused, it has
	 * to be cleared before reusing.
	 */
    public void clear() {
        externalized = "";
    }
}

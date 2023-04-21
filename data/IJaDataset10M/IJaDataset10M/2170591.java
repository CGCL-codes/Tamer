package uk.org.beton.css2.property.value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import uk.org.beton.css2.property.PropertyDescriptor;
import uk.org.beton.css2.property.ValueType;

/**
 * Holds a property value for a list.
 * 
 * @author Rick Beton
 * @version $Id: ListValue.java 7 2006-07-16 17:41:07Z rickbeton $
 */
public final class ListValue extends Value {

    private List list = null;

    private boolean numberList;

    ListValue(PropertyDescriptor descriptor) {
        super(descriptor);
    }

    static boolean matches(String s) {
        return s.indexOf(',') > 0;
    }

    public ValueType getType() {
        return ValueType.LIST;
    }

    public Object getValue() {
        return list;
    }

    protected void parseValue(String newValue) {
        List temp = new ArrayList();
        String[] bits = newValue.split(", ");
        if (Character.isDigit(newValue.charAt(0))) {
            for (int i = 0; i < bits.length; i++) {
                temp.add(Double.valueOf(bits[i]));
            }
            numberList = true;
        } else {
            for (int i = 0; i < bits.length; i++) {
                temp.add(bits[i]);
            }
            numberList = false;
        }
        list = Collections.unmodifiableList(temp);
    }

    /**
     * @return Returns the numberList.
     */
    public boolean isNumberList() {
        return numberList;
    }

    public List getList() {
        return list;
    }
}

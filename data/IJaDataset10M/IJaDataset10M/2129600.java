package rjws.utils;

import rjws.objects.composite.select.Option;
import rjws.objects.composite.select.Select;

/**
 * Helper-Class for Input-Fields
 * 
 * @author Thomas Rudin
 *
 */
public class InputFactory {

    /**
	 * Creates an Option-Field from an Enumeration
	 * @param fieldName The Field-Name
	 * @param jclass The Enumeration
	 * @return The Option-Field
	 */
    public static Object makeSelect(String fieldName, Class<? extends Enum<?>> jclass) {
        return makeSelect(fieldName, jclass, null);
    }

    /**
	 * Creates an Option-Field from an Enumeration
	 * @param fieldName The Field-Name
	 * @param jclass The Enumeration
	 * @param preselection The selected option
	 * @return The Option-Field
	 */
    public static Object makeSelect(String fieldName, Class<? extends Enum<?>> jclass, Enum<? extends Enum<?>> preselection) {
        Select select = new Select();
        select.setName(fieldName);
        for (Object o : jclass.getEnumConstants()) {
            if (preselection != null && preselection.toString().equals(o.toString())) select.add(new Option().setSelected(true).setValue(o).add(o)); else select.add(new Option().setValue(o).add(o));
        }
        return select;
    }
}

package net.sf.joafip.store.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.store.service.HelperReflect;

/**
 * to manage cache of persistable declared fields of classes<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class ClassDeclaredFields {

    /** cache for all declared fields obtains by reflection */
    private final Map<Class, Field[]> allDeclaredFieldsMap = new HashMap<Class, Field[]>();

    private static final HelperReflect helperReflect = HelperReflect.getInstance();

    /**
	 * get all declared fields for a class.<br>
	 * cache the fields obtains by reflection<br>
	 * 
	 * @param classForField
	 *            class definition where get all declared fields
	 * @return all declared fields for the class
	 */
    public Field[] allDeclaredFields(final Class classForField) {
        Field[] fieldsArray;
        fieldsArray = allDeclaredFieldsMap.get(classForField);
        if (fieldsArray == null) {
            final List<Field> fields = helperReflect.allDeclaredFieldsByReflection(classForField);
            fieldsArray = new Field[fields.size()];
            fields.toArray(fieldsArray);
            allDeclaredFieldsMap.put(classForField, fieldsArray);
        }
        return fieldsArray;
    }
}

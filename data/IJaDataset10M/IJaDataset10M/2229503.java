package halo.dao.query;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class FieldTypeUtil {

    public static Set<String> fieldTypeSet = new HashSet<String>();

    public static Set<String> idFieldTypeSet = new HashSet<String>();

    static {
        fieldTypeSet.add("int");
        fieldTypeSet.add("long");
        fieldTypeSet.add("char");
        fieldTypeSet.add("byte");
        fieldTypeSet.add("float");
        fieldTypeSet.add("double");
        fieldTypeSet.add("java.lang.String");
        fieldTypeSet.add("java.util.Date");
        idFieldTypeSet.add("int");
        idFieldTypeSet.add("long");
        idFieldTypeSet.add("java.lang.String");
    }

    public static void checkFieldType(Field field) {
        if (!fieldTypeSet.contains(field.getType().getName())) {
            throw new RuntimeException("field type only support field type [int,long,char,short,byte,float,double,java.lang.String,java.util.Date]");
        }
    }

    public static void checkIdFieldType(Field field) {
        if (!idFieldTypeSet.contains(field.getType().getName())) {
            throw new RuntimeException("id field type only support field type [int,long,java.lang.String]");
        }
    }
}

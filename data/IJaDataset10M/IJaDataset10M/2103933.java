package org.guberno.server.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TypeMapper {

    @SuppressWarnings("unchecked")
    public static final Class[] TYPECLASSES = { StringType.class, BooleanType.class, DoubleType.class, IntegerType.class };

    public static Map<String, Type> types;

    public static Type forName(String typename) {
        if (types == null) {
            types = new HashMap<String, Type>();
            for (Class<? extends Type> current : TYPECLASSES) {
                try {
                    Type tmptype = current.newInstance();
                    types.put(tmptype.getTypeName(), tmptype);
                } catch (Exception e) {
                    throw new IllegalArgumentException(current + " is not a valid type class!");
                }
            }
        }
        Type result = types.get(typename);
        if (result == null) throw new IllegalArgumentException("Type " + typename + " is not supported!");
        return result;
    }

    public static Set<String> getTypeNames() {
        return types.keySet();
    }
}

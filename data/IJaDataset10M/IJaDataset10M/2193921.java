package com.gwtent.client.reflection;

import java.util.HashMap;
import java.util.Map;

public class ClassTypeFactoryImpl implements ClassTypeFactory {

    private static Map map = new HashMap();

    private static ClassTypeFactory factory = new ClassTypeFactoryImpl();

    private ClassTypeFactoryImpl() {
    }

    public ClassType getClassType(String className) {
        ClassType type = (ClassType) map.get(className);
        if (type == null) {
        }
        return type;
    }

    public static void setClassType(String className, ClassType type) {
        map.put(className, type);
    }

    public static ClassTypeFactory getInstance() {
        return factory;
    }
}

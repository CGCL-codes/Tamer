package com.scully.korat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import javassist.expr.FieldAccess;

public class Util {

    public static final String KORAT_PREFIX = "$kor_";

    public static final String JML_PREFIX = "rac$";

    public static final String REP_OK = "repOk";

    private static List<Object> typesSupportedByInteger;

    private static List<Object> typesSupportedByIntegerArray;

    static {
        typesSupportedByInteger = new ArrayList<Object>();
        typesSupportedByInteger.add("Comparable");
        typesSupportedByInteger.add("java.lang.Comparable");
        typesSupportedByInteger.add("Object");
        typesSupportedByInteger.add("java.lang.Object");
        typesSupportedByInteger.add(Comparable.class);
        typesSupportedByInteger.add(Object.class);
        typesSupportedByIntegerArray = new ArrayList<Object>();
        typesSupportedByIntegerArray.add("Comparable[]");
        typesSupportedByIntegerArray.add("java.lang.Comparable[]");
        typesSupportedByIntegerArray.add("Object[]");
        typesSupportedByIntegerArray.add("java.lang.Object[]");
        typesSupportedByIntegerArray.add(Comparable[].class);
        typesSupportedByIntegerArray.add(Object[].class);
    }

    public static boolean isSkippableField(Field field) {
        return field.getName().startsWith(JML_PREFIX) || field.getName().startsWith(KORAT_PREFIX) || Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers());
    }

    public static boolean isSkippableField(IField field) throws JavaModelException {
        return Flags.isStatic(field.getFlags()) || Flags.isTransient(field.getFlags());
    }

    public static boolean isSkippableFieldAccess(FieldAccess fieldAccess) {
        return fieldAccess.getFieldName().startsWith(KORAT_PREFIX) || isInstrumentedMethod(fieldAccess) || fieldAccess.isStatic();
    }

    public static boolean isTypeSupportedByInteger(Class type) {
        return typesSupportedByInteger.contains(type);
    }

    public static boolean isTypeSupportedByInteger(String type) {
        return typesSupportedByInteger.contains(type);
    }

    public static boolean isTypeSupportedByIntegerArray(Class type) {
        return typesSupportedByIntegerArray.contains(type);
    }

    public static boolean isTypeSupportedByIntegerArray(String type) {
        return typesSupportedByIntegerArray.contains(type);
    }

    private static boolean isInstrumentedMethod(FieldAccess fieldAccess) {
        return fieldAccess.where().getMethodInfo().getName().startsWith(KORAT_PREFIX);
    }
}

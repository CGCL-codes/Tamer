package com.curlap.orb.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ClassProperty
 */
public class ClassProperty implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private ClassProperty superClass;

    private ClassProperty[] interfaceProperties;

    private ConstructorProperty[] constructorProperties;

    private FieldProperty[] fieldProperties;

    private MethodProperty[] methodProperties;

    private ClassProperty implementClass;

    private String serverClassName;

    public ClassProperty() {
    }

    public ClassProperty(String name) throws GeneratorException {
        this.name = name;
        this.serverClassName = name;
        if (isPrimitive(name)) return;
        try {
            Class cls = Class.forName(name);
            Class superClass = cls.getSuperclass();
            if (superClass != null && superClass.getName() != "java.lang.Object") this.superClass = new ClassProperty(superClass.getName());
            Class[] interfaces = cls.getInterfaces();
            interfaceProperties = new ClassProperty[interfaces.length];
            for (int i = 0; i < interfaces.length; i++) {
                interfaceProperties[i] = new ClassProperty(interfaces[i].getName());
                interfaceProperties[i].implementClass = this;
            }
            Constructor[] constructors = cls.getDeclaredConstructors();
            constructorProperties = new ConstructorProperty[constructors.length];
            for (int i = 0; i < constructors.length; i++) constructorProperties[i] = new ConstructorProperty(constructors[i]);
            Field[] fields = cls.getDeclaredFields();
            fieldProperties = new FieldProperty[fields.length];
            for (int i = 0; i < fields.length; i++) fieldProperties[i] = new FieldProperty(fields[i]);
            Method[] methods = cls.getDeclaredMethods();
            methodProperties = new MethodProperty[methods.length];
            for (int i = 0; i < methods.length; i++) methodProperties[i] = new MethodProperty(methods[i]);
        } catch (ClassNotFoundException e) {
            throw new GeneratorException(e);
        }
    }

    public ClassProperty(String name, String serverClassName) throws GeneratorException {
        this(name);
        this.serverClassName = serverClassName;
        for (int i = 0; i < interfaceProperties.length; i++) interfaceProperties[i].serverClassName = serverClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassProperty getSuperClass() {
        return superClass;
    }

    public void setSuperClass(ClassProperty superClass) {
        this.superClass = superClass;
    }

    public FieldProperty[] getFieldProperties() {
        return fieldProperties;
    }

    public void setFieldProperties(FieldProperty[] fieldProperties) {
        this.fieldProperties = fieldProperties;
    }

    public ConstructorProperty[] getConstructorProperties() {
        return constructorProperties;
    }

    public void setConstructorProperties(ConstructorProperty[] constructorProperties) {
        this.constructorProperties = constructorProperties;
    }

    public MethodProperty[] getMethodProperties() {
        return methodProperties;
    }

    public void setMethodProperties(MethodProperty[] methodProperties) {
        this.methodProperties = methodProperties;
    }

    public String getNameInContext() {
        return serverClassName;
    }

    public void setNameInContext(String nameInContext) {
        this.serverClassName = nameInContext;
    }

    private boolean isPrimitive(String name) {
        if (name.equals("int") || name.equals("boolean") || name.equals("byte") || name.equals("char") || name.equals("long") || name.equals("short") || name.equals("double") || name.equals("float") || name.equals("void")) {
            return true;
        }
        return false;
    }
}

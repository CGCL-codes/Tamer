package org.aspectme.cldc.reflect.model;

public interface ReflectConstructor extends ReflectMember {

    public String getName();

    public int getModifiers();

    public ReflectClass getDeclaringClass();

    public ReflectClass[] getParameterTypes();
}

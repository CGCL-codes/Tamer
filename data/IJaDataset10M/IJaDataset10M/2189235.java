package com.gwtent.client.reflection.impl;

import java.util.ArrayList;
import java.util.List;
import com.gwtent.client.reflection.EnumConstant;
import com.gwtent.client.reflection.EnumType;
import com.gwtent.client.reflection.Field;

public class EnumTypeImpl extends ClassTypeImpl implements EnumType {

    public EnumTypeImpl(PackageImpl declaringPackage, ClassTypeImpl enclosingType, boolean isLocalType, String name, boolean isInterface, boolean isDefaultInstantiable, Class<?> declaringClass) {
        super(declaringPackage, enclosingType, isLocalType, name, isInterface, isDefaultInstantiable, declaringClass);
    }

    public EnumTypeImpl(String qualifiedName, Class<?> declaringClass) {
        super(qualifiedName, declaringClass);
    }

    public EnumType isEnum() {
        return this;
    }

    public EnumConstant[] getEnumConstants() {
        if (lazyEnumConstants == null) {
            List<EnumConstant> enumConstants = new ArrayList<EnumConstant>();
            for (Field field : getFields()) {
                if (field.isEnumConstant() != null) {
                    enumConstants.add(field.isEnumConstant());
                }
            }
            lazyEnumConstants = enumConstants.toArray(new EnumConstant[0]);
        }
        return lazyEnumConstants;
    }

    private EnumConstant[] lazyEnumConstants;
}

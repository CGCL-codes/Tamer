package com.ssd.mda.core.metadata.provider.reflection.impl;

import com.ssd.mda.core.metadata.provider.reflection.ReflectionMetadataAttribute;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;

@Aspect("pertarget(com.ssd.mda.core.metadata.model.MetadataAttribute)")
public class ReflectionMetadataAttributeImpl implements ReflectionMetadataAttribute {

    @DeclareParents(value = "com.ssd.mda.core.metadata.model.MetadataAttribute", defaultImpl = ReflectionMetadataAttributeImpl.class)
    public static ReflectionMetadataAttribute mixin;

    private String name;

    private Class declaringClass;

    private Class type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(Class declaringClass) {
        this.declaringClass = declaringClass;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}

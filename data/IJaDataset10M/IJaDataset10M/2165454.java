package org.sodeja.generator.java;

import java.util.ArrayList;
import java.util.List;

public class JavaParameterizedType implements JavaType {

    private JavaClass type;

    private List<JavaType> typeArguments;

    public JavaParameterizedType(JavaClass type) {
        this.type = type;
        this.typeArguments = new ArrayList<JavaType>();
    }

    public JavaClass getType() {
        return type;
    }

    public List<JavaType> getTypeArguments() {
        return typeArguments;
    }
}

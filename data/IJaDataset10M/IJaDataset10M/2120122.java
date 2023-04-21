package org.sodeja.generator.java;

public class JavaMethodParameter {

    private JavaType type;

    private String name;

    private boolean isFinal;

    public JavaMethodParameter(JavaType type, String name) {
        this(type, name, true);
    }

    public JavaMethodParameter(JavaType type, String name, boolean isFinal) {
        this.type = type;
        this.name = name;
        this.isFinal = isFinal;
    }

    public JavaType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isFinal() {
        return isFinal;
    }
}

package org.sodeja.generator.java;

public class JavaInterface extends JavaClass {

    public static JavaInterface createFromClass(Class<?> clazz) {
        JavaPackage javaPackage = JavaPackage.createFromDots(clazz.getPackage().getName());
        return new JavaInterface(javaPackage, clazz.getSimpleName());
    }

    public JavaInterface(JavaPackage _package, String name) {
        super(_package, name);
    }

    @Override
    public void setParent(JavaClass parent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addMethod(JavaMethod method) {
        method.setAbstract(true);
        super.addMethod(method);
    }
}

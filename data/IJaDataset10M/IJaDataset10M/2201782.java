package com.db4o.odbgen.plugins.java.maingenerator.typedxmlschema;

/**
 * 
 * This represents a declaration of a type such as a class or interface.
 * The difference between {@link TypeDeclaration} and {@link DataType} is that
 * {@link DataType} represents the usage of the {@link TypeDeclaration}.
 * For example
 * class C1{} - Here C1 is a {@link TypeDeclaration}
 * C1 v = new C1(); - Here C1 is a {@link DataType}
 * @author liviug
 */
public interface TypeDeclaration {

    /**
     * Returns the name of the class.
     * @return
     */
    String getSimpleName();

    /**
     * Returns the package and name of the class.
     * @return
     */
    String getQualifiedName();

    /**
     * Returnst the package of the class.
     * This can never be null even for default package.
     * @return
     */
    String getPackage();
}

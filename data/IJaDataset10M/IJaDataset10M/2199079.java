package com.newisys.langschema.java;

/**
 * Represents a raw Java interface or similar construct, such as an annotation
 * type.
 * 
 * @author Trevor Robinson
 */
public abstract class JavaRawAbstractInterface extends JavaRawStructuredTypeImpl<JavaInterfaceMember> implements JavaAbstractInterface, JavaInterfaceMember, JavaClassMember {

    protected JavaRawAbstractInterface(JavaSchema schema, String id, JavaPackage pkg, JavaStructuredType<?> outerType) {
        super(schema, id, pkg, outerType);
    }

    public boolean isSubtype(JavaType type) {
        return super.isSubtype(type) || (type instanceof JavaPrimitiveType && (((JavaPrimitiveType) type).getWrapperClass().implementsInterface(this))) || (type instanceof JavaStructuredType && ((JavaStructuredType) type).implementsInterface(this));
    }

    public final JavaMemberVariable newField(String id, JavaType type) {
        JavaMemberVariable var = new JavaMemberVariable(id, type);
        var.setVisibility(JavaVisibility.PUBLIC);
        addMember(var);
        return var;
    }

    public final JavaFunction newMethod(String id, JavaType returnType) {
        JavaFunctionType funcType = new JavaFunctionType(returnType);
        JavaFunction func = new JavaFunction(id, funcType);
        func.setVisibility(JavaVisibility.PUBLIC);
        func.addModifier(JavaFunctionModifier.ABSTRACT);
        addMember(func);
        return func;
    }
}

package com.newisys.langschema.java;

import java.util.LinkedList;
import java.util.List;
import com.newisys.langschema.InstanceCreation;

/**
 * Represents a Java instance creation expression.
 * 
 * @author Trevor Robinson
 */
public final class JavaInstanceCreation extends JavaExpression implements InstanceCreation {

    private final JavaStructuredType type;

    private final JavaConstructor ctor;

    private final List<JavaExpression> arguments = new LinkedList<JavaExpression>();

    private JavaClass anonymousClass;

    public JavaInstanceCreation(JavaStructuredType type, JavaConstructor ctor) {
        super(type.getSchema());
        this.type = type;
        this.ctor = ctor;
    }

    public JavaType getResultType() {
        return type;
    }

    public JavaType getType() {
        return type;
    }

    public JavaConstructor getConstructor() {
        return ctor;
    }

    public List<JavaExpression> getArguments() {
        return arguments;
    }

    public void addArgument(JavaExpression expr) {
        arguments.add(expr);
    }

    public JavaClass getAnonymousClass() {
        return anonymousClass;
    }

    public void setAnonymousClass(JavaClass anonymousClass) {
        this.anonymousClass = anonymousClass;
    }

    public boolean isConstant() {
        return false;
    }

    public void accept(JavaExpressionVisitor visitor) {
        visitor.visit(this);
    }
}

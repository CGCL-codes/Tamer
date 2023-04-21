package com.newisys.langschema.java;

/**
 * Represents a Java local variable.
 * 
 * @author Trevor Robinson
 */
public final class JavaLocalVariable extends JavaVariable implements JavaBlockMember {

    private JavaStatement containingStatement;

    public JavaLocalVariable(String id, JavaType type) {
        super(id, type);
    }

    public final JavaStatement getContainingStatement() {
        return containingStatement;
    }

    public final void setContainingStatement(JavaStatement stmt) {
        this.containingStatement = stmt;
    }

    public void accept(JavaSchemaObjectVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(JavaBlockMemberVisitor visitor) {
        visitor.visit(this);
    }
}

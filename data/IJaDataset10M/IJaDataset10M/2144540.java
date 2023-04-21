package org.jmlspecs.jml6.boogie.ast;

import org.eclipse.jdt.core.dom.ASTNode;
import org.jmlspecs.jml6.core.boogie.BoogieSource;

public class TypeReference extends Expression {

    private String typeName;

    public TypeReference(String typeName, ASTNode javaNode, Scope scope) {
        super(javaNode, scope);
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void toBuffer(BoogieSource out) {
        out.append(isNative() ? getTypeName() : TOKEN_REF, getJavaNode());
    }

    public boolean isNative() {
        String nativeTypes[] = { "int", "bool", "long", "char", "double", "float" };
        for (int i = 0; i < nativeTypes.length; i++) {
            if (getTypeName().equals(nativeTypes[i])) return true;
        }
        return false;
    }

    public void traverse(Visitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}

package org.jmlspecs.jml4.fspv.theory.ast;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.jmlspecs.jml4.ast.JmlResultReference;

public class TheoryFieldReference extends TheoryReference {

    public TheoryFieldReference(ASTNode base, Theory theory) {
        super(base, theory);
    }

    public void traverse(TheoryVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public String getReceiverName() {
        FieldReference fieldReference = (FieldReference) this.base;
        if (fieldReference.receiver instanceof JmlResultReference) {
            return "result'";
        } else {
            return fieldReference.receiver.toString();
        }
    }

    public String getFieldName() {
        FieldReference fieldReference = (FieldReference) this.base;
        return new String(fieldReference.token);
    }

    public String getType() {
        return null;
    }
}

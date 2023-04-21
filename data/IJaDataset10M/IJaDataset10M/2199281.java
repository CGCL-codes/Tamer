package org.jmlspecs.jml4.fspv.theory.ast;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;

public class TheoryLocalDeclaration extends TheoryVariableDeclaration {

    public final TheoryExpression initialization;

    public TheoryLocalDeclaration(ASTNode base, Theory theory, TheoryExpression initialization) {
        super(base, theory);
        this.initialization = initialization;
    }

    public String getName() {
        LocalDeclaration localDeclaration = (LocalDeclaration) this.base;
        return new String(localDeclaration.name);
    }

    public String getType() {
        LocalDeclaration localDeclaration = (LocalDeclaration) this.base;
        return localDeclaration.type.toString();
    }

    private boolean isBaseType() {
        LocalDeclaration localDeclaration = (LocalDeclaration) this.base;
        return localDeclaration.binding.type.isBaseType();
    }

    public boolean isArrayType() {
        LocalDeclaration localDeclaration = (LocalDeclaration) this.base;
        return localDeclaration.binding.type.isArrayType();
    }

    public boolean isClassType() {
        LocalDeclaration localDeclaration = (LocalDeclaration) this.base;
        return localDeclaration.binding.type.isClass();
    }

    public boolean isIntType() {
        LocalDeclaration localDeclaration = (LocalDeclaration) this.base;
        return this.isBaseType() && localDeclaration.binding.type.id == TypeIds.T_int;
    }

    public boolean isBooleanType() {
        LocalDeclaration localDeclaration = (LocalDeclaration) this.base;
        return this.isBaseType() && localDeclaration.binding.type.id == TypeIds.T_boolean;
    }

    public void traverse(TheoryVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.initialization != null) {
                this.initialization.traverse(visitor);
            }
        }
        visitor.endVisit(this);
    }
}

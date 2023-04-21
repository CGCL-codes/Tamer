package compiler;

import net.sf.beezle.mork.classfile.Code;

public class Assign extends Statement {

    private LValue left;

    private Expression expr;

    public Assign(LValue left, Expression expr) throws SemanticError {
        this.left = left;
        this.expr = expr;
        if (!left.getType().isAssignableFrom(expr.getType())) {
            throw new SemanticError("type mismatch");
        }
    }

    @Override
    public void translate(Code dest) {
        expr.translate(dest);
        left.translateAssign(dest);
    }
}

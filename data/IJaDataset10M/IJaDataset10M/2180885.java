package net.sf.refactorit.classmodel.expressions;

import net.sf.refactorit.classmodel.BinItem;
import net.sf.refactorit.classmodel.BinPrimitiveType;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.parser.ASTImpl;

public final class BinLogicalExpression extends BinExpression {

    public BinLogicalExpression(BinExpression leftExpression, BinExpression rightExpression, ASTImpl rootAst) {
        super(rootAst);
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.assigmentType = rootAst.getType();
    }

    public final BinTypeRef getReturnType() {
        return BinPrimitiveType.BOOLEAN_REF;
    }

    public final BinExpression getLeftExpression() {
        return leftExpression;
    }

    public final BinExpression getRightExpression() {
        return rightExpression;
    }

    public final int getAssigmentType() {
        return assigmentType;
    }

    public final void accept(net.sf.refactorit.query.BinItemVisitor visitor) {
        visitor.visit(this);
    }

    public final void defaultTraverse(net.sf.refactorit.query.BinItemVisitor visitor) {
        if (leftExpression != null) {
            leftExpression.accept(visitor);
        }
        if (rightExpression != null) {
            rightExpression.accept(visitor);
        }
    }

    public final void clean() {
        if (leftExpression != null) {
            leftExpression.clean();
            leftExpression = null;
        }
        if (rightExpression != null) {
            rightExpression.clean();
            rightExpression = null;
        }
        super.clean();
    }

    public final boolean isSame(BinItem other) {
        if (!(other instanceof BinLogicalExpression)) {
            return false;
        }
        final BinLogicalExpression expr = (BinLogicalExpression) other;
        return this.assigmentType == expr.assigmentType && isBothNullOrSame(this.leftExpression, expr.leftExpression) && isBothNullOrSame(this.rightExpression, expr.rightExpression);
    }

    private BinExpression leftExpression;

    private BinExpression rightExpression;

    private final int assigmentType;
}

package net.sourceforge.nrl.parser.ast.constraints.impl;

import net.sourceforge.nrl.parser.ast.INRLAstVisitor;
import net.sourceforge.nrl.parser.ast.constraints.IBinaryOperatorStatement;
import net.sourceforge.nrl.parser.ast.constraints.IConstraint;
import net.sourceforge.nrl.parser.ast.impl.NRLActionParser;
import org.antlr.runtime.Token;

public class BinaryOperatorStatementImpl extends ConstraintImpl implements IBinaryOperatorStatement {

    Operator operator;

    public BinaryOperatorStatementImpl(Token token) {
        super(token);
        switch(token.getType()) {
            case NRLActionParser.AND:
                operator = Operator.AND;
                break;
            case NRLActionParser.OR:
                operator = Operator.OR;
                break;
            case NRLActionParser.IMPLIES:
                operator = Operator.IMPLIES;
                break;
            case NRLActionParser.IFF:
                operator = Operator.IFF;
                break;
            default:
                throw new RuntimeException("Internal error. Illegal token used to initialize boolean operator: " + token.getType());
        }
    }

    public void accept(INRLAstVisitor visitor) {
        if (visitor.visitBefore(this)) {
            getLeft().accept(visitor);
            getRight().accept(visitor);
        }
        visitor.visitAfter(this);
    }

    public String dump(int indent) {
        return doIndent(indent) + getOperatorAsString() + NEWLINE + getLeft().dump(indent + 1) + getRight().dump(indent + 1);
    }

    public IConstraint getLeft() {
        return (IConstraint) getChild(0);
    }

    public IConstraint getRight() {
        return (IConstraint) getChild(1);
    }

    public Operator getOperator() {
        return operator;
    }

    protected String getOperatorAsString() {
        switch(getOperator()) {
            case AND:
                return "and";
            case OR:
                return "or";
            case IFF:
                return "iff";
            case IMPLIES:
                return "implies";
        }
        return "";
    }
}

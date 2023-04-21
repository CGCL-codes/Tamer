package javassist.compiler.ast;

import javassist.compiler.TokenId;
import javassist.compiler.CompileError;

/**
 * Statement.
 */
public class Stmnt extends ASTList implements TokenId {

    protected int operatorId;

    public Stmnt(int op, ASTree _head, ASTList _tail) {
        super(_head, _tail);
        operatorId = op;
    }

    public Stmnt(int op, ASTree _head) {
        super(_head);
        operatorId = op;
    }

    public Stmnt(int op) {
        this(op, null);
    }

    public static Stmnt make(int op, ASTree oprand1, ASTree oprand2) {
        return new Stmnt(op, oprand1, new ASTList(oprand2));
    }

    public static Stmnt make(int op, ASTree op1, ASTree op2, ASTree op3) {
        return new Stmnt(op, op1, new ASTList(op2, new ASTList(op3)));
    }

    public void accept(Visitor v) throws CompileError {
        v.atStmnt(this);
    }

    public int getOperator() {
        return operatorId;
    }

    protected String getTag() {
        if (operatorId < 128) return "stmnt:" + (char) operatorId; else return "stmnt:" + operatorId;
    }
}

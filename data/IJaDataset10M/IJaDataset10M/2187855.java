package edu.gsbme.MMLParser2.MathML.MEE.MathAST;

public class Operator extends Terminal {

    public Operator(String value) {
        super(value);
    }

    @Override
    public Object visit(Visitor v, Object o) {
        return v.visitOperator(this, o);
    }
}

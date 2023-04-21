package edu.gsbme.MMLParser2.MathML.MEE.MathAST;

public class DataList extends List {

    public Expr D;

    public List DL;

    public DataList(Expr aAST, List alAST) {
        super();
        D = aAST;
        DL = alAST;
        D.parent = DL.parent = this;
    }

    public Object visit(Visitor v, Object o) {
        return v.visitDataList(this, o);
    }
}

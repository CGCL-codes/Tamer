package edu.gsbme.MMLParser2.MathML.MEE.MathAST;

public class PieceList extends List {

    public Expr piece;

    public List list;

    public PieceList(Expr aAST, List alAST) {
        super();
        piece = aAST;
        list = alAST;
        piece.parent = list.parent = this;
    }

    @Override
    public Object visit(Visitor v, Object o) {
        return v.visitPieceList(this, o);
    }
}

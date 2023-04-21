package ibex.ast;

import java.util.List;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;

public abstract class RhsUnary_c extends RhsExpr_c {

    RhsExpr item;

    public RhsUnary_c(Position pos, RhsExpr item) {
        super(pos);
        this.item = item;
    }

    public RhsExpr item() {
        return item;
    }

    public RhsExpr item(RhsExpr item) {
        RhsUnary_c n = (RhsUnary_c) copy();
        n.item = item;
        return n;
    }

    public Node visitChildren(NodeVisitor v) {
        RhsExpr item = (RhsExpr) visitChild(this.item, v);
        return item(item);
    }

    public Term firstChild() {
        return item;
    }

    public List<Term> acceptCFG(CFGBuilder v, List<Term> succs) {
        v.visitCFG(item, this, EXIT);
        return succs;
    }
}

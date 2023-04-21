package ibex.ast;

import ibex.types.ActionDef;
import ibex.types.ActionDef_c;
import ibex.types.IbexTypeSystem;
import ibex.types.TupleType;
import ibex.visit.Rewriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Formal;
import polyglot.ast.Local;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.Flags;
import polyglot.types.LocalDef;
import polyglot.types.Name;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.Types;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;

public class RhsOr_c extends RhsExpr_c implements RhsOr {

    List<RhsExpr> items;

    public RhsOr_c(Position pos, List<RhsExpr> items) {
        super(pos);
        assert items.size() >= 2;
        this.items = TypedList.copyAndCheck(items, RhsExpr.class, true);
    }

    public List<RhsExpr> items() {
        return Collections.unmodifiableList(items);
    }

    public RhsOr items(List<RhsExpr> items) {
        RhsOr_c n = (RhsOr_c) copy();
        n.items = TypedList.copyAndCheck(items, RhsExpr.class, true);
        return n;
    }

    @Override
    public Node visitChildren(NodeVisitor v) {
        List<RhsExpr> items = visitList(this.items, v);
        return items(items);
    }

    @Override
    public Term firstChild() {
        return listChild(items, null);
    }

    @Override
    public List<Term> acceptCFG(CFGBuilder v, List<Term> succs) {
        v.visitCFGList(items, this, EXIT);
        return succs;
    }

    @Override
    public Node typeCheck(ContextVisitor tc) throws SemanticException {
        IbexTypeSystem ts = (IbexTypeSystem) tc.typeSystem();
        RhsExpr left = items.get(0);
        Type lca = left.type();
        for (int i = 1; i < items.size(); i++) {
            RhsExpr right = items.get(i);
            lca = lca(this, lca, right.type(), tc);
        }
        List<RhsExpr> coerced = new ArrayList<RhsExpr>(items.size());
        for (RhsExpr e : items) {
            e = coerce(e, lca, tc);
            coerced.add(e);
        }
        return (RhsExpr) this.items(coerced).type(lca);
    }

    public static RhsExpr coerce(RhsExpr e, Type type, ContextVisitor tc) throws SemanticException {
        IbexNodeFactory nf = (IbexNodeFactory) tc.nodeFactory();
        IbexTypeSystem ts = (IbexTypeSystem) tc.typeSystem();
        if (ts.isSubtype(e.type(), type, tc.context())) return e;
        if (type.isVoid() || e.type().isVoid()) return e;
        Position pos = e.position();
        Name name = Name.makeFresh();
        LocalDef ld = ts.localDef(pos, Flags.FINAL, Types.ref(e.type()), name);
        Formal f = nf.Formal(pos, nf.FlagsNode(pos, ld.flags()), nf.CanonicalTypeNode(pos, ld.type()), nf.Id(pos, ld.name()));
        f = f.localDef(ld);
        Local l = nf.Local(pos, f.name());
        l = l.localInstance(ld.asInstance());
        l = (Local) l.type(ld.asInstance().type());
        Block body;
        if (e.type().isPrimitive() && type.isReference()) {
            body = nf.Block(pos, nf.Return(pos, Rewriter.box(e.type(), l, nf)));
        } else if (type.isPrimitive() && e.type().isReference()) {
            body = nf.Block(pos, nf.Return(pos, Rewriter.unbox(type, l, nf)));
        } else if (e.type().isPrimitive() && type.isPrimitive()) {
            body = nf.Block(pos, nf.Return(pos, nf.Cast(pos, nf.CanonicalTypeNode(pos, type), l).type(type)));
        } else {
            throw new SemanticException("Cannot coerce " + e.type() + " to " + type + ".", pos);
        }
        ActionDef ad = new ActionDef_c(ts, pos, Types.ref(type), Collections.EMPTY_LIST);
        return (RhsExpr) nf.RhsAction(pos, e, body).formal(f).actionDef(ad).rhs(e.rhs()).type(type);
    }

    static RhsExpr typeCheckOr(RhsExpr e, RhsExpr left, RhsExpr right, ContextVisitor tc) throws SemanticException {
        IbexTypeSystem ts = (IbexTypeSystem) tc.typeSystem();
        Type t = lca(e, left.type(), right.type(), tc);
        return (RhsExpr) e.type(t);
    }

    static Type lca(RhsExpr e, Type t1, Type t2, ContextVisitor tc) throws SemanticException {
        IbexTypeSystem ts = (IbexTypeSystem) tc.typeSystem();
        if (t1.isVoid() || t2.isVoid()) {
            return ts.Void();
        }
        if (ts.typeEquals(t1, t2, tc.context())) {
            return t1;
        }
        if (t1.isNumeric() && t2.isNumeric()) {
            if (t1.isByte() && t2.isShort() || t1.isShort() && t2.isByte()) {
                return ts.Short();
            }
            return ts.promote(t1, t2);
        }
        if (t1.isNull()) return ts.nullable(t2);
        if (t2.isNull()) return ts.nullable(t1);
        if (t1 instanceof TupleType && t2.isPrimitive()) return ts.Object();
        if (t2 instanceof TupleType && t1.isPrimitive()) return ts.Object();
        if (t1 instanceof TupleType && t2.isReference()) return ts.Object();
        if (t2 instanceof TupleType && t1.isReference()) return ts.Object();
        if (t1.isReference() && t2.isReference()) {
            if (ts.isImplicitCastValid(t1, t2, tc.context())) {
                return t2;
            } else if (ts.isImplicitCastValid(t2, t1, tc.context())) {
                return t1;
            }
        }
        throw new SemanticException("Could not determine type; cannot assign " + t1 + " to " + t2 + " or vice versa.", e.position());
    }

    /** Write the expression to an output file. */
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        String sep = "";
        for (RhsExpr left : items) {
            w.write(sep);
            if (sep.length() > 0) w.allowBreak(type() == null || type().isPrimitive() ? 2 : 0, " ");
            sep = " |";
            printSubExpr(left, true, w, tr);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (RhsExpr left : items) {
            sb.append(sep);
            sep = " | ";
            sb.append(left);
        }
        return sb.toString();
    }
}

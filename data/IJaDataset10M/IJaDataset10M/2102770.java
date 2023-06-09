package gnu.commonlisp.lang;

import kawa.lang.*;
import gnu.lists.*;
import gnu.expr.*;
import gnu.mapping.Symbol;

public class defvar extends Syntax {

    /** True for defconst, false for defvar. */
    boolean force;

    public defvar(boolean force) {
        this.force = force;
    }

    public boolean scanForDefinitions(Pair st, java.util.Vector forms, ScopeExp defs, Translator tr) {
        if (!(st.getCdr() instanceof Pair)) return super.scanForDefinitions(st, forms, defs, tr);
        Pair p = (Pair) st.getCdr();
        Object name = p.getCar();
        if (name instanceof String || name instanceof Symbol) {
            Declaration decl = defs.lookup(name);
            if (decl == null) {
                decl = new Declaration(name);
                decl.setFlag(Declaration.IS_DYNAMIC);
                defs.addDeclaration(decl);
            } else tr.error('w', "duplicate declaration for `" + name + "'");
            p = Translator.makePair(p, decl, p.getCdr());
            st = Translator.makePair(st, this, p);
            if (defs instanceof ModuleExp) {
                decl.setCanRead(true);
                decl.setCanWrite(true);
            }
        }
        forms.addElement(st);
        return true;
    }

    public Expression rewriteForm(Pair form, Translator tr) {
        Object obj = form.getCdr();
        Object name = null;
        Expression value = null;
        Declaration decl = null;
        if (obj instanceof Pair) {
            Pair p1 = (Pair) obj;
            if (p1.getCar() instanceof Declaration) {
                decl = (Declaration) p1.getCar();
                name = decl.getSymbol();
                if (p1.getCdr() instanceof Pair) {
                    Pair p2 = (Pair) p1.getCdr();
                    value = tr.rewrite(p2.getCar());
                    if (p2.getCdr() != LList.Empty) {
                    }
                } else if (p1.getCdr() != LList.Empty) name = null;
            }
        }
        if (name == null) return tr.syntaxError("invalid syntax for " + getName());
        if (value == null) {
            if (force) value = CommonLisp.nilExpr; else return new QuoteExp(name);
        }
        SetExp sexp = new SetExp(name, value);
        if (!force) sexp.setSetIfUnbound(true);
        sexp.setDefining(true);
        if (decl != null) {
            sexp.setBinding(decl);
            if (decl.context instanceof ModuleExp && decl.getCanWrite()) value = null;
            decl.noteValue(value);
        }
        return sexp;
    }
}

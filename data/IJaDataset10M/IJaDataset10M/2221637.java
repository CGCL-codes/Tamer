package net.sf.kpex.prolog;

import net.sf.kpex.io.IO;
import net.sf.kpex.util.Trail;

/**
 * Implements compound terms
 * 
 * @see Term
 */
public class Fun extends Const {

    protected static String watchNull(Term x) {
        return null == x ? "null" : x.toString();
    }

    public Term args[];

    public Fun(String s) {
        super(s);
        args = null;
    }

    public Fun(String s, int arity) {
        super(s);
        args = new Term[arity];
    }

    public Fun(String s, Term... terms) {
        this(s, terms.length);
        args = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            args[i] = terms[i];
        }
    }

    public final Term getArg(int i) {
        return args[i].getRef();
    }

    @Override
    public final int getArity() {
        return args.length;
    }

    public final int getIntArg(int i) {
        return (int) ((Int) getArg(i)).getValue();
    }

    public void init(int arity) {
        args = new Term[arity];
        for (int i = 0; i < arity; i++) {
            args[i] = new Var();
        }
    }

    @Override
    public Const listify() {
        Cons l = new Cons(new Const(getName()), Const.NIL);
        Cons curr = l;
        for (int i = 0; i < args.length; i++) {
            Cons tail = new Cons(args[i], Const.NIL);
            curr.args[1] = tail;
            curr = tail;
        }
        return l;
    }

    public final int putArg(int i, Term T, Prog p) {
        return getArg(i).unify(T, p.getTrail()) ? 1 : 0;
    }

    public final void setArg(int i, Term T) {
        args[i] = T;
    }

    @Override
    public Term token() {
        return args[0];
    }

    @Override
    public String toString() {
        return funToString();
    }

    protected final Fun funClone() {
        Fun f = null;
        try {
            f = (Fun) clone();
        } catch (CloneNotSupportedException e) {
            IO.errmes("clone: " + e);
        }
        return f;
    }

    protected final String funToString() {
        if (args == null) {
            return quotedName() + "()";
        }
        int l = args.length;
        return quotedName() + (l <= 0 ? "" : "(" + show_args() + ")");
    }

    protected Fun initializedClone() {
        Fun f = funClone();
        f.init(args.length);
        return f;
    }

    protected Fun unInitializedClone() {
        Fun f = funClone();
        f.args = new Term[args.length];
        return f;
    }

    @Override
    protected boolean bindTo(Term that, Trail trail) {
        return super.bindTo(that, trail) && args.length == ((Fun) that).args.length;
    }

    @Override
    protected boolean isClause() {
        return getArity() == 2 && getName().equals(":-");
    }

    @Override
    protected Term reaction(Term that) {
        Fun f = funClone();
        f.args = new Term[args.length];
        for (int i = 0; i < args.length; i++) {
            f.args[i] = args[i].reaction(that);
        }
        return f;
    }

    @Override
    protected boolean unifyTo(Term that, Trail trail) {
        if (bindTo(that, trail)) {
            for (int i = 0; i < args.length; i++) {
                if (!args[i].unify(((Fun) that).args[i], trail)) {
                    return false;
                }
            }
            return true;
        } else {
            return that.bindTo(this, trail);
        }
    }

    private String show_args() {
        StringBuffer s = new StringBuffer(watchNull(args[0]));
        for (int i = 1; i < args.length; i++) {
            s.append("," + watchNull(args[i]));
        }
        return s.toString();
    }
}

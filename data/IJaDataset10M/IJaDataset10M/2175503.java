package ail.syntax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ail.semantics.AILAgent;
import ail.semantics.Unifier;

/** 
 *  represents a logical formula with some logical operator ("&amp;",  "|", "not").
 */
public class LogExpr implements LogicalFormula {

    public enum LogicalOp {

        none {

            public String toString() {
                return "";
            }
        }
        , not {

            public String toString() {
                return "not ";
            }
        }
        , and {

            public String toString() {
                return " & ";
            }
        }
        , or {

            public String toString() {
                return " | ";
            }
        }

    }

    private LogicalFormula lhs, rhs;

    private LogicalOp op = LogicalOp.none;

    public LogExpr() {
        super();
    }

    public LogExpr(LogicalFormula f1, LogicalOp oper, LogicalFormula f2) {
        lhs = f1;
        op = oper;
        rhs = f2;
    }

    public LogExpr(LogicalOp oper, LogicalFormula f) {
        op = oper;
        rhs = f;
    }

    public Iterator<Unifier> logicalConsequence(final AILAgent ag, Unifier un) {
        try {
            final Iterator<Unifier> ileft;
            switch(op) {
                case not:
                    if (!rhs.logicalConsequence(ag, un).hasNext()) {
                        return createUnifIterator(un);
                    }
                    break;
                case none:
                    return rhs.logicalConsequence(ag, un);
                case and:
                    ileft = lhs.logicalConsequence(ag, un);
                    return new Iterator<Unifier>() {

                        Unifier current = null;

                        Iterator<Unifier> iright = null;

                        public boolean hasNext() {
                            if (current == null) get();
                            return current != null;
                        }

                        public Unifier next() {
                            if (current == null) get();
                            Unifier a = current;
                            current = null;
                            return a;
                        }

                        private void get() {
                            current = null;
                            while ((iright == null || !iright.hasNext()) && ileft.hasNext()) {
                                Unifier ul = ileft.next();
                                iright = rhs.logicalConsequence(ag, ul);
                            }
                            if (iright != null && iright.hasNext()) {
                                current = iright.next();
                            }
                        }

                        public void remove() {
                        }
                    };
                case or:
                    ileft = lhs.logicalConsequence(ag, un);
                    final Iterator<Unifier> iright = rhs.logicalConsequence(ag, un);
                    return new Iterator<Unifier>() {

                        Unifier current = null;

                        public boolean hasNext() {
                            if (current == null) get();
                            return current != null;
                        }

                        public Unifier next() {
                            if (current == null) get();
                            Unifier a = current;
                            get();
                            return a;
                        }

                        private void get() {
                            current = null;
                            if (ileft.hasNext()) {
                                current = ileft.next();
                            } else if (iright.hasNext()) {
                                current = iright.next();
                            }
                        }

                        public void remove() {
                        }
                    };
            }
        } catch (Exception e) {
            String slhs = "is null";
            if (lhs != null) {
                Iterator<Unifier> i = lhs.logicalConsequence(ag, un);
                if (i != null) {
                    slhs = "";
                    while (i.hasNext()) {
                        slhs += i.next().toString() + ", ";
                    }
                } else {
                    slhs = "iterator is null";
                }
            }
            String srhs = "is null";
            if (lhs != null) {
                Iterator<Unifier> i = rhs.logicalConsequence(ag, un);
                if (i != null) {
                    srhs = "";
                    while (i.hasNext()) {
                        srhs += i.next().toString() + ", ";
                    }
                } else {
                    srhs = "iterator is null";
                }
            }
        }
        ArrayList<Unifier> empty = new ArrayList<Unifier>();
        return empty.iterator();
    }

    /** create an iterator for a list of unifiers */
    public static Iterator<Unifier> createUnifIterator(Unifier... unifs) {
        List<Unifier> r = new ArrayList<Unifier>(unifs.length);
        for (int i = 0; i < unifs.length; i++) {
            r.add(unifs[i]);
        }
        return r.iterator();
    }

    /** make a hard copy of the terms */
    public Object clone() {
        LogExpr t = new LogExpr();
        if (lhs != null) {
            t.lhs = (LogicalFormula) lhs.clone();
        }
        t.op = this.op;
        if (rhs != null) {
            t.rhs = (LogicalFormula) rhs.clone();
        }
        return t;
    }

    @Override
    public boolean equals(Object t) {
        if (t != null && t instanceof LogExpr) {
            LogExpr eprt = (LogExpr) t;
            if (lhs == null && eprt.lhs != null) {
                return false;
            }
            if (lhs != null && !lhs.equals(eprt.lhs)) {
                return false;
            }
            if (op != eprt.op) {
                return false;
            }
            if (rhs == null && eprt.rhs != null) {
                return false;
            }
            if (rhs != null && !rhs.equals(eprt.rhs)) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = op.hashCode();
        if (lhs != null) code += lhs.hashCode();
        if (rhs != null) code += rhs.hashCode();
        return code;
    }

    /** gets the Operation of this Expression */
    public LogicalOp getOp() {
        return op;
    }

    /** gets the LHS of this Expression */
    public LogicalFormula getLHS() {
        return lhs;
    }

    /** gets the RHS of this Expression */
    public LogicalFormula getRHS() {
        return rhs;
    }

    public boolean isUnary() {
        return lhs == null;
    }

    @Override
    public String toString() {
        if (lhs == null) {
            return op + "(" + rhs + ")";
        } else {
            return "(" + lhs + op + rhs + ")";
        }
    }

    public List<String> getVarNames() {
        List<String> varnames = getRHS().getVarNames();
        if (!isUnary()) {
            varnames.addAll(getLHS().getVarNames());
        }
        return varnames;
    }

    public void renameVar(String oldname, String newname) {
        getRHS().renameVar(oldname, newname);
        if (!isUnary()) {
            getLHS().renameVar(oldname, newname);
        }
    }

    public void standardise_apart(Unifiable t, Unifier u) {
        List<String> tvarnames = t.getVarNames();
        List<String> myvarnames = getVarNames();
        ArrayList<String> replacednames = new ArrayList<String>();
        ArrayList<String> newnames = new ArrayList<String>();
        for (String s : myvarnames) {
            if (tvarnames.contains(s)) {
                if (!replacednames.contains(s)) {
                    String s1 = generate_fresh(s, tvarnames, myvarnames, newnames);
                    renameVar(s, s1);
                    u.renameVar(s, s1);
                }
            }
        }
    }

    public boolean isRule() {
        return false;
    }

    public boolean unifies(Unifiable t, Unifier u) {
        if (t instanceof LogExpr) {
            LogExpr le = (LogExpr) t;
            if (le.getOp().equals(getOp())) {
                boolean result = getRHS().unifies(le.getRHS(), u);
                if (!isUnary() && result) {
                    result = getLHS().unifies(le.getLHS(), u);
                    return result;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private String generate_fresh(String s, List<String> names1, List<String> names2, List<String> names3) {
        String sbase = s;
        for (int i = 0; i < names1.size() + names2.size() + names3.size(); i++) {
            String news = sbase + i;
            if (!names1.contains(news) & !names2.contains(news) & !names3.contains(news)) {
                break;
            }
        }
        return sbase;
    }

    public Term toTerm() {
        switch(op) {
            case none:
                return getRHS().toTerm();
            case not:
                Structure s = new Structure("not");
                s.addTerm(getRHS().toTerm());
                return s;
            case and:
                Structure s1 = new Structure("and");
                s1.addTerm(getLHS().toTerm());
                s1.addTerm(getRHS().toTerm());
                return s1;
            case or:
                Structure s2 = new Structure("or");
                s2.addTerm(getLHS().toTerm());
                s2.addTerm(getRHS().toTerm());
                return s2;
        }
        return (new Structure("error"));
    }

    public ArrayList<LogicalFormula> getPosTerms() {
        ArrayList<LogicalFormula> posterms = new ArrayList<LogicalFormula>();
        switch(op) {
            case none:
                posterms.add(getRHS());
                return posterms;
            case not:
                return posterms;
            case and:
                posterms.addAll(getLHS().getPosTerms());
                posterms.addAll(getRHS().getPosTerms());
                return posterms;
        }
        return posterms;
    }
}

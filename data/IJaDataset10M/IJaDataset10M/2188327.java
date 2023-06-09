package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.*;

/**
 <code>'$parse_tokens_brace'/3</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @author Douglas R. Miles (dmiles@users.sourceforge.net) for Object(s) 
 @version 1.0-dmiles
*/
class PRED_$parse_tokens_brace_3 extends PredicateBase {

    static Object s1 = makeAtom("{}");

    static Object s2 = makeAtom("}");

    static Object si3 = makeInteger(1201);

    static Object s4 = makeAtom("{}", 1);

    static Predicate _$parse_tokens_brace_3_var = new PRED_$parse_tokens_brace_3_var();

    static Predicate _$parse_tokens_brace_3_var_1 = new PRED_$parse_tokens_brace_3_var_1();

    static Predicate _$parse_tokens_brace_3_1 = new PRED_$parse_tokens_brace_3_1();

    static Predicate _$parse_tokens_brace_3_2 = new PRED_$parse_tokens_brace_3_2();

    public Object arg1, arg2, arg3;

    public PRED_$parse_tokens_brace_3(Object a1, Object a2, Object a3, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        this.cont = cont;
    }

    public PRED_$parse_tokens_brace_3() {
    }

    public void setArgument(Object[] args, Predicate cont) {
        arg1 = args[0];
        arg2 = args[1];
        arg3 = args[2];
        this.cont = cont;
    }

    public int arity() {
        return 3;
    }

    public String nameUQ() {
        return "$parse_tokens_brace";
    }

    public void sArg(int i0, Object val) {
        switch(i0) {
            case 0:
                arg1 = val;
                break;
            case 1:
                arg2 = val;
                break;
            case 2:
                arg3 = val;
                break;
            default:
                newIndexOutOfBoundsException("setarg", i0, val);
        }
    }

    public Object gArg(int i0) {
        switch(i0) {
            case 0:
                return arg1;
            case 1:
                return arg2;
            case 2:
                return arg3;
            default:
                return newIndexOutOfBoundsException("getarg", i0, null);
        }
    }

    public String toPrologString(java.util.Collection newParam) {
        return "'$parse_tokens_brace'(" + argString(arg1, newParam) + "," + argString(arg2, newParam) + "," + argString(arg3, newParam) + ")";
    }

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        engine_aregs[1] = arg1;
        engine_aregs[2] = arg2;
        engine_aregs[3] = arg3;
        engine.cont = cont;
        engine.setB0();
        return engine.switch_on_term(_$parse_tokens_brace_3_var, _$parse_tokens_brace_3_2, _$parse_tokens_brace_3_2, _$parse_tokens_brace_3_var, _$parse_tokens_brace_3_2, _$parse_tokens_brace_3_2);
    }
}

class PRED_$parse_tokens_brace_3_var extends PRED_$parse_tokens_brace_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.jtry(_$parse_tokens_brace_3_1, _$parse_tokens_brace_3_var_1);
    }
}

class PRED_$parse_tokens_brace_3_var_1 extends PRED_$parse_tokens_brace_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.trust(_$parse_tokens_brace_3_2);
    }
}

class PRED_$parse_tokens_brace_3_1 extends PRED_$parse_tokens_brace_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        Object a1, a2, a3, a4;
        Predicate cont;
        a1 = engine_aregs[1];
        a2 = engine_aregs[2];
        a3 = engine_aregs[3];
        cont = engine.cont;
        a1 = deref(a1);
        if (isAtomTerm(a1)) {
            if (!prologEquals(a1, s1)) return fail(engine);
        } else if (isVariable(a1)) {
            bind(a1, s1);
        } else {
            return fail(engine);
        }
        a2 = deref(a2);
        if (isListTerm(a2)) {
            Object[] args = consArgs(a2);
            if (!unify(s2, args[0])) return fail(engine);
            a4 = args[1];
        } else if (isVariable(a2)) {
            a4 = engine.makeVariable(this);
            bind(a2, makeList(s2, a4));
        } else {
            return fail(engine);
        }
        if (!unify(a4, a3)) return fail(engine);
        engine.neckCut();
        return exit(engine, cont);
    }
}

class PRED_$parse_tokens_brace_3_2 extends PRED_$parse_tokens_brace_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        Object a1, a2, a3, a4, a5, a6, a7;
        Predicate p1, p2, p3;
        Predicate cont;
        a1 = engine_aregs[1];
        a2 = engine_aregs[2];
        a3 = engine_aregs[3];
        cont = engine.cont;
        a4 = engine.makeVariable(this);
        a5 = engine.makeVariable(this);
        a6 = engine.makeVariable(this);
        Object[] y1 = { a4 };
        a7 = makeStructure(s4, y1);
        p1 = new PRED_$unify_2(a3, a6, cont);
        p2 = new PRED_$unify_2(a1, a7, p1);
        p3 = new PRED_$parse_tokens_expect_3(s2, a5, a6, p2);
        return exit(engine, new PRED_$parse_tokens_4(a4, si3, a2, a5, p3));
    }
}

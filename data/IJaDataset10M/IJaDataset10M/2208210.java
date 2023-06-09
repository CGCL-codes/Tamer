package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.*;

/**
 <code>'$parse_tokens_error1'/2</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @author Douglas R. Miles (dmiles@users.sourceforge.net) for Object(s) 
 @version 1.0-dmiles
*/
class PRED_$parse_tokens_error1_2 extends PredicateBase {

    static Object s1 = makeAtom("[]");

    static Object s2 = makeAtom("** here **");

    static Predicate _$parse_tokens_error1_2_var = new PRED_$parse_tokens_error1_2_var();

    static Predicate _$parse_tokens_error1_2_var_1 = new PRED_$parse_tokens_error1_2_var_1();

    static Predicate _$parse_tokens_error1_2_var_2 = new PRED_$parse_tokens_error1_2_var_2();

    static Predicate _$parse_tokens_error1_2_con = new PRED_$parse_tokens_error1_2_con();

    static Predicate _$parse_tokens_error1_2_con_1 = new PRED_$parse_tokens_error1_2_con_1();

    static Predicate _$parse_tokens_error1_2_lis = new PRED_$parse_tokens_error1_2_lis();

    static Predicate _$parse_tokens_error1_2_lis_1 = new PRED_$parse_tokens_error1_2_lis_1();

    static Predicate _$parse_tokens_error1_2_1 = new PRED_$parse_tokens_error1_2_1();

    static Predicate _$parse_tokens_error1_2_2 = new PRED_$parse_tokens_error1_2_2();

    static Predicate _$parse_tokens_error1_2_3 = new PRED_$parse_tokens_error1_2_3();

    public Object arg1, arg2;

    public PRED_$parse_tokens_error1_2(Object a1, Object a2, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        this.cont = cont;
    }

    public PRED_$parse_tokens_error1_2() {
    }

    public void setArgument(Object[] args, Predicate cont) {
        arg1 = args[0];
        arg2 = args[1];
        this.cont = cont;
    }

    public int arity() {
        return 2;
    }

    public String nameUQ() {
        return "$parse_tokens_error1";
    }

    public void sArg(int i0, Object val) {
        switch(i0) {
            case 0:
                arg1 = val;
                break;
            case 1:
                arg2 = val;
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
            default:
                return newIndexOutOfBoundsException("getarg", i0, null);
        }
    }

    public String toPrologString(java.util.Collection newParam) {
        return "'$parse_tokens_error1'(" + argString(arg1, newParam) + "," + argString(arg2, newParam) + ")";
    }

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        engine_aregs[1] = arg1;
        engine_aregs[2] = arg2;
        engine.cont = cont;
        engine.setB0();
        return engine.switch_on_term(_$parse_tokens_error1_2_var, _$parse_tokens_error1_2_2, _$parse_tokens_error1_2_2, _$parse_tokens_error1_2_con, _$parse_tokens_error1_2_2, _$parse_tokens_error1_2_lis);
    }
}

class PRED_$parse_tokens_error1_2_var extends PRED_$parse_tokens_error1_2 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.jtry(_$parse_tokens_error1_2_1, _$parse_tokens_error1_2_var_1);
    }
}

class PRED_$parse_tokens_error1_2_var_1 extends PRED_$parse_tokens_error1_2 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.retry(_$parse_tokens_error1_2_2, _$parse_tokens_error1_2_var_2);
    }
}

class PRED_$parse_tokens_error1_2_var_2 extends PRED_$parse_tokens_error1_2 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.trust(_$parse_tokens_error1_2_3);
    }
}

class PRED_$parse_tokens_error1_2_con extends PRED_$parse_tokens_error1_2 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.jtry(_$parse_tokens_error1_2_1, _$parse_tokens_error1_2_con_1);
    }
}

class PRED_$parse_tokens_error1_2_con_1 extends PRED_$parse_tokens_error1_2 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.trust(_$parse_tokens_error1_2_2);
    }
}

class PRED_$parse_tokens_error1_2_lis extends PRED_$parse_tokens_error1_2 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.jtry(_$parse_tokens_error1_2_2, _$parse_tokens_error1_2_lis_1);
    }
}

class PRED_$parse_tokens_error1_2_lis_1 extends PRED_$parse_tokens_error1_2 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.trust(_$parse_tokens_error1_2_3);
    }
}

class PRED_$parse_tokens_error1_2_1 extends PRED_$parse_tokens_error1_2 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        Object a1, a2;
        Predicate cont;
        a1 = engine_aregs[1];
        a2 = engine_aregs[2];
        cont = engine.cont;
        a1 = deref(a1);
        if (isAtomTerm(a1)) {
            if (!prologEquals(a1, s1)) return fail(engine);
        } else if (isVariable(a1)) {
            bind(a1, s1);
        } else {
            return fail(engine);
        }
        engine.neckCut();
        return exit(engine, cont);
    }
}

class PRED_$parse_tokens_error1_2_2 extends PRED_$parse_tokens_error1_2 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        Object a1, a2, a3;
        Predicate p1, p2, p3, p4;
        Predicate cont;
        a1 = engine_aregs[1];
        a2 = engine_aregs[2];
        cont = engine.cont;
        a3 = engine.makeVariable(this);
        if (!unify(a3, makeInteger(engine.B0))) {
            return fail(engine);
        }
        a1 = deref(a1);
        a2 = deref(a2);
        if (!prologEquals(a1, a2)) {
            return fail(engine);
        }
        a3 = deref(a3);
        if (!isCutter(a3)) {
            throw new IllegalTypeException("integer", a3);
        } else {
            engine.cut((a3));
        }
        p1 = new PRED_nl_0(cont);
        p2 = new PRED_$parse_tokens_error1_2(a1, s1, p1);
        p3 = new PRED_nl_0(p2);
        p4 = new PRED_write_1(s2, p3);
        return exit(engine, new PRED_nl_0(p4));
    }
}

class PRED_$parse_tokens_error1_2_3 extends PRED_$parse_tokens_error1_2 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        Object a1, a2, a3, a4;
        Predicate p1;
        Predicate cont;
        a1 = engine_aregs[1];
        a2 = engine_aregs[2];
        cont = engine.cont;
        a1 = deref(a1);
        if (isListTerm(a1)) {
            Object[] args = consArgs(a1);
            a3 = args[0];
            a4 = args[1];
        } else if (isVariable(a1)) {
            a3 = engine.makeVariable(this);
            a4 = engine.makeVariable(this);
            bind(a1, makeList(a3, a4));
        } else {
            return fail(engine);
        }
        p1 = new PRED_$parse_tokens_error1_2(a4, a2, cont);
        return exit(engine, new PRED_$parse_tokens_error2_1(a3, p1));
    }
}

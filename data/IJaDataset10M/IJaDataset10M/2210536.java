package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.*;

/**
 <code>'$builtin_append'/3</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @author Douglas R. Miles (dmiles@users.sourceforge.net) for Object(s) 
 @version 1.0-dmiles
*/
class PRED_$builtin_append_3 extends PredicateBase {

    static Object s1 = makeAtom("[]");

    static Predicate _$builtin_append_3_top = new PRED_$builtin_append_3_top();

    static Predicate _fail_0 = new PRED_fail_0();

    static Predicate _$builtin_append_3_var = new PRED_$builtin_append_3_var();

    static Predicate _$builtin_append_3_var_1 = new PRED_$builtin_append_3_var_1();

    static Predicate _$builtin_append_3_1 = new PRED_$builtin_append_3_1();

    static Predicate _$builtin_append_3_2 = new PRED_$builtin_append_3_2();

    public Object arg1, arg2, arg3;

    public PRED_$builtin_append_3(Object a1, Object a2, Object a3, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        this.cont = cont;
    }

    public PRED_$builtin_append_3() {
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
        return "$builtin_append";
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
        return "'$builtin_append'(" + argString(arg1, newParam) + "," + argString(arg2, newParam) + "," + argString(arg3, newParam) + ")";
    }

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        engine_aregs[1] = arg1;
        engine_aregs[2] = arg2;
        engine_aregs[3] = arg3;
        engine.cont = cont;
        return exit(engine, _$builtin_append_3_top);
    }
}

class PRED_$builtin_append_3_top extends PRED_$builtin_append_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        engine.setB0();
        return engine.switch_on_term(_$builtin_append_3_var, _fail_0, _fail_0, _$builtin_append_3_1, _fail_0, _$builtin_append_3_2);
    }
}

class PRED_$builtin_append_3_var extends PRED_$builtin_append_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.jtry(_$builtin_append_3_1, _$builtin_append_3_var_1);
    }
}

class PRED_$builtin_append_3_var_1 extends PRED_$builtin_append_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.trust(_$builtin_append_3_2);
    }
}

class PRED_$builtin_append_3_1 extends PRED_$builtin_append_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        Object a1, a2, a3;
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
        if (!unify(a2, a3)) return fail(engine);
        return exit(engine, cont);
    }
}

class PRED_$builtin_append_3_2 extends PRED_$builtin_append_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        Object a1, a2, a3, a4, a5, a6;
        Predicate cont;
        a1 = engine_aregs[1];
        a2 = engine_aregs[2];
        a3 = engine_aregs[3];
        cont = engine.cont;
        a1 = deref(a1);
        if (isListTerm(a1)) {
            Object[] args = consArgs(a1);
            a4 = args[0];
            a5 = args[1];
        } else if (isVariable(a1)) {
            a4 = engine.makeVariable(this);
            a5 = engine.makeVariable(this);
            bind(a1, makeList(a4, a5));
        } else {
            return fail(engine);
        }
        a3 = deref(a3);
        if (isListTerm(a3)) {
            Object[] args = consArgs(a3);
            if (!unify(a4, args[0])) return fail(engine);
            a6 = args[1];
        } else if (isVariable(a3)) {
            a6 = engine.makeVariable(this);
            bind(a3, makeList(a4, a6));
        } else {
            return fail(engine);
        }
        engine_aregs[1] = a5;
        engine_aregs[2] = a2;
        engine_aregs[3] = a6;
        engine.cont = cont;
        return exit(engine, _$builtin_append_3_top);
    }
}

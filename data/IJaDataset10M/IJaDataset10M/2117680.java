package net.sf.julie.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import sisc.data.Pair;
import sisc.data.Procedure;
import sisc.data.Quantity;
import sisc.data.SchemeString;
import sisc.data.Symbol;
import sisc.data.Value;
import sisc.interpreter.Context;
import sisc.interpreter.Interpreter;
import sisc.interpreter.SchemeCaller;
import sisc.interpreter.SchemeException;
import sisc.util.Util;

public class Scm {

    static {
        System.setProperty("sisc.heap", Scm.class.getResource("/julie.shp").toExternalForm());
    }

    public static final Pair EOL = Util.EMPTYLIST;

    public static Value eval(final String expr) {
        return (Value) Context.execute(new SchemeCaller() {

            @Override
            public Object execute(Interpreter r) throws SchemeException {
                Value result = null;
                try {
                    result = r.eval(expr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
        });
    }

    public static Value call(final Value procedure, final Value... args) {
        return (Value) Context.execute(new SchemeCaller() {

            @Override
            public Object execute(Interpreter r) throws SchemeException {
                Value result = null;
                result = r.eval((Procedure) procedure, args);
                return result;
            }
        });
    }

    public static Value call(final String procedure, final Value... args) {
        return (Value) Context.execute(new SchemeCaller() {

            @Override
            public Object execute(Interpreter r) throws SchemeException {
                Value result = null;
                Procedure proc;
                try {
                    proc = Util.proc(r.eval(procedure));
                    result = r.eval(proc, args);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return result;
            }
        });
    }

    public static Value fromLocaleString(String str) {
        return new SchemeString(str);
    }

    public static Value fromLocaleStringN(String str, int len) {
        return fromLocaleString(str);
    }

    public static Quantity fromInt(int i) {
        return Quantity.valueOf(i);
    }

    public static Pair cons(Value car, Value cdr) {
        return new Pair(car, cdr);
    }

    public static Pair list1(Value v1) {
        return Util.list(v1);
    }

    public static Pair list2(Value v1, Value v2) {
        return Util.list(v1, v2);
    }

    public static Pair list3(Value v1, Value v2, Value v3) {
        return Util.list(v1, v2, v3);
    }

    public static Pair list4(Value v1, Value v2, Value v3, Value v4) {
        return Util.list(v1, v2, v3, v4);
    }

    public static Pair list5(Value v1, Value v2, Value v3, Value v4, Value v5) {
        return Util.list(v1, v2, v3, v4, v5);
    }

    public static Value car(Value pair) {
        return ((Pair) pair).car();
    }

    public static Value cdr(Value pair) {
        return ((Pair) pair).cdr();
    }

    public static Value sum(Value v1, Value v2) {
        if (!(v1 instanceof Quantity) || !(v2 instanceof Quantity)) {
            throw new RuntimeException("must provide quantities");
        }
        return ((Quantity) v1).add((Quantity) v2);
    }

    public static Value difference(Value v1, Value v2) {
        if (!(v1 instanceof Quantity) || !(v2 instanceof Quantity)) {
            throw new RuntimeException("must provide quantities");
        }
        return ((Quantity) v1).sub((Quantity) v2);
    }

    public static Quantity fromDouble(Double value) {
        return Quantity.valueOf(value.doubleValue());
    }

    public static Double toDouble(Value value) {
        return ((Quantity) value).doubleValue();
    }

    public static Integer toInt(Value value) {
        return ((Quantity) value).intValue();
    }

    public static Pair reverseX(Value lst, Value newTail) {
        Pair result;
        result = Util.reverseInPlace((Pair) lst);
        if (newTail != null && newTail != Scm.EOL) {
            result = Scm.append(result, newTail);
        }
        return result;
    }

    public static Pair append(Value... lstlst) {
        List<Value> vals = new ArrayList<Value>();
        for (Value lst : lstlst) {
            vals.addAll(Arrays.asList(Util.pairToValues((Pair) lst)));
        }
        return Util.valArrayToList(vals.toArray(new Value[0]));
    }

    public static Value appendX(Value... lstlst) {
        return append(lstlst);
    }

    public static boolean isPair(Value value) {
        return value instanceof Pair && value != Util.EMPTYLIST;
    }

    public static boolean isSymbol(Value value) {
        return value instanceof Symbol;
    }

    public static boolean isString(Value value) {
        return value instanceof SchemeString;
    }

    public static boolean isNumber(Value value) {
        return value instanceof Quantity;
    }

    public static Symbol stringToSymbol(Value value) {
        return (Symbol) call("string->symbol", value);
    }

    public static Value assoc(Value key, Value alist) {
        return call("assoc", key, alist);
    }

    public static Value acons(Value key, Value value, Value alist) {
        return new Pair(new Pair(key, value), alist);
    }

    public static Pair listn(Value... values) {
        return Util.valArrayToList(values);
    }

    public static Value cadr(Value value) {
        return Scm.car(Scm.cdr(value));
    }

    public static Value cdar(Value value) {
        return Scm.cdr(Scm.car(value));
    }

    public static Value caar(Value value) {
        return Scm.car(Scm.car(value));
    }
}

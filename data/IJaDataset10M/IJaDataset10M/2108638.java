package net.masamic.flisp_j;

import java.util.*;
import java.lang.reflect.*;

/**
 * @author masamic
 * Definitions of Lisp Operators
 */
public class Operators {

    private static final String packname = Operators.class.getPackage().getName();

    private static Hashtable<String, String> aliases = new Hashtable<String, String>();

    static {
        aliases.put("+", "add");
        aliases.put("-", "sub");
        aliases.put("*", "times");
        aliases.put("/", "divide");
    }

    /**
	 * Private Constructor
	 * Anyone who create instance.
	 */
    private Operators() {
    }

    private static Method resolveOperator(String opname) throws Exception {
        Class cls = Class.forName(packname + ".Operators");
        String rep = aliases.get(opname);
        if (rep != null) {
            opname = rep;
        }
        Class[] args = { Class.forName(packname + ".Lisp"), Class.forName(packname + ".LispRootObject") };
        Method met = cls.getDeclaredMethod(opname, args);
        return met;
    }

    public static Symbol invokeOperator(Lisp env, String opname, Symbol lro) throws Exception {
        return (Symbol) resolveOperator(opname).invoke(null, env, lro);
    }

    public static Symbol invokeOperator(Lisp env, Atom op, Symbol lro) throws Exception {
        Object operator = op.getValue();
        if (operator instanceof Method) {
            return (Symbol) ((Method) operator).invoke(null, env, lro);
        } else {
            return (Symbol) resolveOperator(operator.toString()).invoke(null, env, lro);
        }
    }

    public static Object add(Lisp env, Symbol lst) throws Exception {
        double num = 0;
        if (!(lst instanceof Cons)) {
            throw new Exception("Not Enough Argument Exception");
        }
        ConsListIterator it = ((Cons) lst).consIterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof Cons) {
                obj = env.eval((Symbol) obj);
            }
            if (obj instanceof Atom) {
                obj = env.getAtomValue((Atom) obj);
                Double tmp = new Double(((Atom) obj).getValue().toString());
                num += tmp.doubleValue();
            }
        }
        return new Atom((new Double(num)).toString(), new Double(num));
    }

    public static Object de(Lisp env, Symbol lst) throws Exception {
        if (!(lst instanceof Cons)) {
            throw new Exception("Not Enough Argument Exception");
        }
        Symbol cur_cons = lst;
        Symbol funcname = ((Cons) cur_cons).car();
        cur_cons = ((Cons) cur_cons).cdr();
        if (!(funcname instanceof Atom)) {
            throw new Exception("No func name");
        }
        if (cur_cons == null) {
            throw new Exception("No Argument(s)");
        }
        Cons lambda = new Cons(new Atom("LAMBDA"), cur_cons);
        ((Atom) funcname).setValue(lambda);
        return (Atom) funcname;
    }

    public static Object quote(Lisp env, Symbol lst) {
        return lst;
    }

    public static Object lambda(Lisp env, Symbol lst) {
        return lst;
    }
}

package org.goobs.scheme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Scheme {

    private Environment global;

    public Scheme() {
        global = new Environment(null);
        global.putBinding(new ScmObject(InternalPrimitive.ADD, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.ADD, this));
        global.putBinding(new ScmObject(InternalPrimitive.SUBTRACT, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.SUBTRACT, this));
        global.putBinding(new ScmObject(InternalPrimitive.MULTIPLY, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.MULTIPLY, this));
        global.putBinding(new ScmObject(InternalPrimitive.DIVIDE, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.DIVIDE, this));
        global.putBinding(new ScmObject(InternalPrimitive.LESS_THAN, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.LESS_THAN, this));
        global.putBinding(new ScmObject(InternalPrimitive.GREATER_THAN, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.GREATER_THAN, this));
        global.putBinding(new ScmObject(InternalPrimitive.EQARITH, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.EQARITH, this));
        global.putBinding(new ScmObject(InternalPrimitive.CAR, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.CAR, this));
        global.putBinding(new ScmObject(InternalPrimitive.CDR, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.CDR, this));
        global.putBinding(new ScmObject(InternalPrimitive.CONS, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.CONS, this));
        global.putBinding(new ScmObject(InternalPrimitive.EQ, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.EQ, this));
        global.putBinding(new ScmObject(InternalPrimitive.EQUAL, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.EQUAL, this));
        global.putBinding(new ScmObject(InternalPrimitive.EVEN, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.EVEN, this));
        global.putBinding(new ScmObject(InternalPrimitive.EXIT, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.EXIT, this));
        global.putBinding(new ScmObject(InternalPrimitive.FLOOR, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.FLOOR, this));
        global.putBinding(new ScmObject(InternalPrimitive.LOAD, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.LOAD, this));
        global.putBinding(new ScmObject(InternalPrimitive.MAKEVECT, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.MAKEVECT, this));
        global.putBinding(new ScmObject(InternalPrimitive.NOT, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.NOT, this));
        global.putBinding(new ScmObject(InternalPrimitive.NUMBER, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.NUMBER, this));
        global.putBinding(new ScmObject(InternalPrimitive.PAIR, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.PAIR, this));
        global.putBinding(new ScmObject(InternalPrimitive.PRINT, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.PRINT, this));
        global.putBinding(new ScmObject(InternalPrimitive.PRINTF, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.PRINTF, this));
        global.putBinding(new ScmObject(InternalPrimitive.PROCEDURE, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.PROCEDURE, this));
        global.putBinding(new ScmObject(InternalPrimitive.SLEEP, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.SLEEP, this));
        global.putBinding(new ScmObject(InternalPrimitive.SYMBOL, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.SYMBOL, this));
        global.putBinding(new ScmObject(InternalPrimitive.VECTOR, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.VECTOR, this));
        global.putBinding(new ScmObject(InternalPrimitive.VECTREF, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.VECTREF, this));
        global.putBinding(new ScmObject(InternalPrimitive.VECTSET, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.VECTSET, this));
        global.putBinding(new ScmObject(InternalPrimitive.TYPE, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.TYPE, this));
        global.putBinding(new ScmObject(InternalPrimitive.TOLIST, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.TOLIST, this));
        global.putBinding(new ScmObject(InternalPrimitive.SAME_FORM, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.SAME_FORM, this));
        global.putBinding(new ScmObject(InternalPrimitive.SET_RESOLVE_BINDINGS, ScmObject.BINDING), new InternalPrimitive(InternalPrimitive.SET_RESOLVE_BINDINGS, this));
        global.putBinding(new ScmObject(SoftPrimitive.MODULO, ScmObject.BINDING), new SoftPrimitive(SoftPrimitive.MODULO, this));
        global.putBinding(new ScmObject("and", ScmObject.BINDING), InternalPrimitive.makeSpecialForm("and"));
        global.putBinding(new ScmObject("cond", ScmObject.BINDING), InternalPrimitive.makeSpecialForm("cond"));
        global.putBinding(new ScmObject("define", ScmObject.BINDING), InternalPrimitive.makeSpecialForm("define"));
        global.putBinding(new ScmObject("do", ScmObject.BINDING), InternalPrimitive.makeSpecialForm("do"));
        global.putBinding(new ScmObject("if", ScmObject.BINDING), InternalPrimitive.makeSpecialForm("if"));
        global.putBinding(new ScmObject("lambda", ScmObject.BINDING), InternalPrimitive.makeSpecialForm("lambda"));
        global.putBinding(new ScmObject("or", ScmObject.BINDING), InternalPrimitive.makeSpecialForm("or"));
        global.putBinding(new ScmObject("quote", ScmObject.BINDING), InternalPrimitive.makeSpecialForm("quote"));
        global.putBinding(new ScmObject("set!", ScmObject.BINDING), InternalPrimitive.makeSpecialForm("set!"));
        global.putBinding(new ScmObject("set-car!", ScmObject.BINDING), InternalPrimitive.makeSpecialForm("set-car!"));
        global.putBinding(new ScmObject("set-cdr!", ScmObject.BINDING), InternalPrimitive.makeSpecialForm("set-cdr!"));
        global.putBinding(new ScmObject("nil", ScmObject.BINDING), ScmObject.makeNil());
        global.putBinding(new ScmObject("true", ScmObject.BINDING), ScmObject.makeTrue());
        global.putBinding(new ScmObject("false", ScmObject.BINDING), ScmObject.makeFalse());
        try {
            loadLib("org/goobs/scheme/stdlib.scm");
        } catch (SchemeException e) {
            e.printError();
        }
    }

    public void addPrimitive(String key, ExternalPrimitive primitive) {
        global.putBinding(new ScmObject(InternalPrimitive.EQARITH, ScmObject.BINDING), primitive);
    }

    public ScmObject probe(String binding) {
        ScmObject bind = new ScmObject(binding, ScmObject.BINDING);
        ScmObject val = global.getBinding(bind);
        return val;
    }

    public String evaluate(String exp) throws SchemeException {
        exp = preProcess(exp);
        if (exp.trim().equals("")) {
            return "";
        }
        ScmObject retVal = evaluateList(ScmObject.specify("( " + exp + " )"));
        StringBuilder b = new StringBuilder();
        while (!retVal.isNil()) {
            b.append(retVal.car().toString()).append("\n");
            retVal = retVal.cdr();
        }
        return b.toString();
    }

    public ScmObject evaluate(ScmObject exp) {
        exp = ScmObject.makePair(exp, ScmObject.makeNil());
        return evaluateList(exp).car();
    }

    public ScmObject evaluateMaximally(ScmObject exp) {
        boolean save = Environment.getResolveBindings();
        Environment.setResolveBindings(false);
        ScmObject rtn = evaluate(exp);
        Environment.setResolveBindings(save);
        return rtn;
    }

    private ScmObject evaluateList(ScmObject exp) throws SchemeException {
        if (exp.isNil()) {
            return ScmObject.makeNil();
        }
        try {
            return ScmObject.makePair(global.eval(preProcess(exp.car())), evaluateList(exp.cdr()));
        } catch (StackOverflowError e) {
            throw new SchemeException("too many recursions: " + exp);
        } catch (RuntimeException e) {
            if (e instanceof SchemeException) {
                throw e;
            }
            e.printStackTrace();
            throw new SchemeException("[INTERNAL]: JAVA exception caught: " + e.getClass().getName() + ": " + e.getMessage() + "\n  stack trace printed");
        }
    }

    private void loadLib(String name) {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream(name);
        String exp = "";
        try {
            while (input.available() != 0) {
                String str = "" + (char) input.read();
                exp = exp + str;
            }
        } catch (Exception e) {
            throw new SchemeException("[INTERNAL]: could not load library: " + name);
        }
        evaluate(exp);
    }

    private static String read(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new SchemeException("could not access keyboard");
        }
    }

    private static String preProcess(String str) {
        String rtn = "";
        str = str.replace("'()", "nil");
        str = str.replace("'(", "(list ");
        str = str.replace("#(", "(" + InternalPrimitive.MAKEVECT + " ");
        int paren = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                paren++;
                rtn = rtn + " ( ";
            } else if (str.charAt(i) == ')') {
                paren--;
                rtn = rtn + " ) ";
            } else if (str.charAt(i) == '"') {
                rtn = rtn + "\" ";
                i++;
                while (str.charAt(i) != '"') {
                    if (i == str.length() - 1) {
                        throw new SchemeException("quotes do not match: " + str);
                    }
                    rtn = rtn + str.charAt(i);
                    i++;
                }
                rtn = rtn + " \" ";
            } else if (str.charAt(i) == ';') {
                while (i < str.length() && str.charAt(i) != '\n') {
                    i++;
                }
                rtn = rtn + " ";
            } else {
                rtn = rtn + str.charAt(i);
            }
        }
        if (paren != 0) {
            throw new SchemeException("parenthesis do not match: " + rtn);
        }
        return rtn;
    }

    private static ScmObject preProcess(ScmObject obj) {
        if (obj.getType() != ScmObject.PAIR) {
            return obj;
        }
        if (obj.car().equals(new ScmObject("define", ScmObject.BINDING)) && !obj.cdr().isNil() && obj.cdr().car().getType() == ScmObject.PAIR) {
            ScmObject define = obj.car();
            if (obj.cdr().isNil() || obj.cdr().car().isNil() || obj.cdr().isNil() || obj.cdr().cdr().isNil()) {
                throw new SchemeException("bad define format: " + obj);
            }
            ScmObject func = preProcess(obj.cdr().car().car());
            ScmObject args = preProcess(obj.cdr().car().cdr());
            ScmObject body = preProcess(obj.cdr().cdr());
            return ScmObject.makePair(define, ScmObject.makePair(func, ScmObject.makePair(ScmObject.makePair(new ScmObject("lambda", ScmObject.BINDING), ScmObject.makePair(args, body)), ScmObject.makeNil())));
        }
        if (obj.car().equals(new ScmObject("let", ScmObject.BINDING))) {
            if (obj.cdr().isNil() || obj.cdr().cdr().isNil() || obj.cdr().car().getType() != ScmObject.PAIR) {
                throw new SchemeException("bad let format: " + obj);
            }
            ScmObject lst = preProcess(obj.cdr().car());
            ScmObject body = preProcess(obj.cdr().cdr());
            ScmObject args = letHelper(lst, true);
            ScmObject vars = letHelper(lst, false);
            return ScmObject.makePair(ScmObject.makePair(new ScmObject("lambda", ScmObject.BINDING), ScmObject.makePair(args, body)), vars);
        }
        if (obj.car().equals(new ScmObject("let*", ScmObject.BINDING))) {
            if (obj.cdr().isNil() || obj.cdr().cdr().isNil() || obj.cdr().car().getType() != ScmObject.PAIR) {
                throw new SchemeException("bad let format: " + obj);
            }
            ScmObject lst = preProcess(obj.cdr().car());
            ScmObject body = preProcess(obj.cdr().cdr());
            return letStarHelper(lst, body).car();
        }
        return ScmObject.makePair(preProcess(obj.car()), preProcess(obj.cdr()));
    }

    private static ScmObject letHelper(ScmObject let, boolean car) {
        if (let.isNil()) {
            return ScmObject.makeNil();
        } else {
            ScmObject term = let.car();
            if (car) {
                return ScmObject.makePair(term.car(), letHelper(let.cdr(), true));
            } else {
                if (term.getType() != ScmObject.PAIR || term.cdr().isNil() || !term.cdr().cdr().isNil()) {
                    throw new SchemeException("bat let variable format: " + term);
                }
                return ScmObject.makePair(term.cdr().car(), letHelper(let.cdr(), false));
            }
        }
    }

    private static ScmObject letStarHelper(ScmObject lst, ScmObject body) {
        if (lst.isNil()) {
            return body;
        } else {
            ScmObject term = lst.car();
            if (term.getType() != ScmObject.PAIR || term.cdr().isNil() || !term.cdr().cdr().isNil()) {
                throw new SchemeException("bat let* variable format: " + term);
            }
            ScmObject rest = letStarHelper(lst.cdr(), body);
            ScmObject lambda = ScmObject.makePair(new ScmObject("lambda", ScmObject.BINDING), ScmObject.makePair(ScmObject.makePair(term.car(), ScmObject.makeNil()), rest));
            return ScmObject.makePair(ScmObject.makePair(lambda, ScmObject.makePair(term.cdr().car(), ScmObject.makeNil())), ScmObject.makeNil());
        }
    }

    private static void startCmdLine() {
        Scheme scm = new Scheme();
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e2) {
            }
            System.out.print("scm> ");
            try {
                System.out.print(scm.evaluate(read(System.in)));
            } catch (SchemeException e) {
                e.printError();
            }
        }
    }

    public static void main(String[] args) {
        startCmdLine();
    }
}

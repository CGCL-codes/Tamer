package org.armedbear.lisp;

public class Primitive0R extends Function {

    public Primitive0R(LispObject name) {
        super(name);
    }

    public Primitive0R(String name) {
        super(name);
    }

    public Primitive0R(String name, String arglist) {
        super(name, arglist);
    }

    public Primitive0R(LispObject name, LispObject lambdaList) {
        super(name, lambdaList);
    }

    public Primitive0R(String name, Package pkg) {
        super(name, pkg);
    }

    public Primitive0R(String name, Package pkg, boolean exported) {
        super(name, pkg, exported);
    }

    public Primitive0R(String name, Package pkg, boolean exported, String arglist) {
        super(name, pkg, exported, arglist);
    }

    public Primitive0R(String name, Package pkg, boolean exported, String arglist, String docstring) {
        super(name, pkg, exported, arglist, docstring);
    }

    public LispObject typeOf() {
        return Symbol.COMPILED_FUNCTION;
    }

    public LispObject execute() throws ConditionThrowable {
        return _execute(NIL);
    }

    public LispObject execute(LispObject arg) throws ConditionThrowable {
        return _execute(new Cons(arg));
    }

    public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
        return _execute(list2(first, second));
    }

    public LispObject execute(LispObject first, LispObject second, LispObject third) throws ConditionThrowable {
        return _execute(list3(first, second, third));
    }

    public LispObject execute(LispObject first, LispObject second, LispObject third, LispObject fourth) throws ConditionThrowable {
        return _execute(list4(first, second, third, fourth));
    }

    public LispObject execute(LispObject first, LispObject second, LispObject third, LispObject fourth, LispObject fifth) throws ConditionThrowable {
        return _execute(list5(first, second, third, fourth, fifth));
    }

    public LispObject execute(LispObject first, LispObject second, LispObject third, LispObject fourth, LispObject fifth, LispObject sixth) throws ConditionThrowable {
        return _execute(list6(first, second, third, fourth, fifth, sixth));
    }

    public LispObject execute(LispObject first, LispObject second, LispObject third, LispObject fourth, LispObject fifth, LispObject sixth, LispObject seventh) throws ConditionThrowable {
        return _execute(list7(first, second, third, fourth, fifth, sixth, seventh));
    }

    public LispObject execute(LispObject first, LispObject second, LispObject third, LispObject fourth, LispObject fifth, LispObject sixth, LispObject seventh, LispObject eighth) throws ConditionThrowable {
        return _execute(list8(first, second, third, fourth, fifth, sixth, seventh, eighth));
    }

    public LispObject execute(LispObject[] args) throws ConditionThrowable {
        LispObject list = NIL;
        for (int i = args.length; i-- > 0; ) list = new Cons(args[i], list);
        return _execute(list);
    }

    protected LispObject _execute(LispObject arg) throws ConditionThrowable {
        return error(new LispError("Not implemented."));
    }
}

package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;

public abstract class CompiledClosure extends Closure implements Cloneable {

    public ClosureBinding[] ctx;

    public CompiledClosure(LispObject lambdaList) throws ConditionThrowable {
        super(list(SymbolConstants.LAMBDA, lambdaList), null);
    }

    public final CompiledClosure setContext(ClosureBinding[] context) {
        ctx = context;
        return this;
    }

    public final CompiledClosure dup() {
        CompiledClosure result = null;
        try {
            result = (CompiledClosure) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public LispObject typep(LispObject typeSpecifier) throws ConditionThrowable {
        if (typeSpecifier == SymbolConstants.COMPILED_FUNCTION) return T;
        return super.typep(typeSpecifier);
    }

    private final LispObject notImplemented() throws ConditionThrowable {
        return error(new WrongNumberOfArgumentsException(this));
    }

    public LispObject execute() throws ConditionThrowable {
        return execute(ZERO_LISPOBJECTS);
    }

    public LispObject execute(LispObject first) throws ConditionThrowable {
        LispObject[] args = new LispObject[1];
        args[0] = first;
        return execute(args);
    }

    public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
        LispObject[] args = new LispObject[2];
        args[0] = first;
        args[1] = second;
        return execute(args);
    }

    public LispObject execute(LispObject first, LispObject second, LispObject third) throws ConditionThrowable {
        LispObject[] args = new LispObject[3];
        args[0] = first;
        args[1] = second;
        args[2] = third;
        return execute(args);
    }

    public LispObject execute(LispObject first, LispObject second, LispObject third, LispObject fourth) throws ConditionThrowable {
        LispObject[] args = new LispObject[4];
        args[0] = first;
        args[1] = second;
        args[2] = third;
        args[3] = fourth;
        return execute(args);
    }

    public LispObject execute(LispObject first, LispObject second, LispObject third, LispObject fourth, LispObject fifth) throws ConditionThrowable {
        LispObject[] args = new LispObject[5];
        args[0] = first;
        args[1] = second;
        args[2] = third;
        args[3] = fourth;
        args[4] = fifth;
        return execute(args);
    }

    public LispObject execute(LispObject first, LispObject second, LispObject third, LispObject fourth, LispObject fifth, LispObject sixth) throws ConditionThrowable {
        LispObject[] args = new LispObject[6];
        args[0] = first;
        args[1] = second;
        args[2] = third;
        args[3] = fourth;
        args[4] = fifth;
        args[5] = sixth;
        return execute(args);
    }

    public LispObject execute(LispObject first, LispObject second, LispObject third, LispObject fourth, LispObject fifth, LispObject sixth, LispObject seventh) throws ConditionThrowable {
        LispObject[] args = new LispObject[7];
        args[0] = first;
        args[1] = second;
        args[2] = third;
        args[3] = fourth;
        args[4] = fifth;
        args[5] = sixth;
        args[6] = seventh;
        return execute(args);
    }

    public LispObject execute(LispObject first, LispObject second, LispObject third, LispObject fourth, LispObject fifth, LispObject sixth, LispObject seventh, LispObject eighth) throws ConditionThrowable {
        LispObject[] args = new LispObject[8];
        args[0] = first;
        args[1] = second;
        args[2] = third;
        args[3] = fourth;
        args[4] = fifth;
        args[5] = sixth;
        args[6] = seventh;
        args[7] = eighth;
        return execute(args);
    }

    public LispObject execute(LispObject[] args) throws ConditionThrowable {
        return notImplemented();
    }

    private static final Primitive LOAD_COMPILED_FUNCTION = new Primitive("load-compiled-function", PACKAGE_SYS, true, "pathname") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            String namestring = null;
            if (arg instanceof Pathname) namestring = ((Pathname) arg).getNamestring(); else if (arg instanceof AbstractString) namestring = arg.getStringValue();
            if (namestring != null) return loadCompiledFunction(namestring);
            return error(new LispError("Unable to load " + arg.writeToString()));
        }
    };

    private static final Primitive VARLIST = new Primitive("varlist", PACKAGE_SYS, false) {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            if (arg instanceof Closure) return ((Closure) arg).getVariableList();
            return type_error(arg, SymbolConstants.COMPILED_FUNCTION);
        }
    };
}

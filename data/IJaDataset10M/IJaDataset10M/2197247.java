package mscheme.syntax;

import mscheme.Syntax;
import mscheme.code.Forceable;
import mscheme.environment.StaticEnvironment;
import mscheme.exceptions.SchemeException;
import mscheme.exceptions.SyntaxArityError;
import mscheme.util.Arity;
import mscheme.values.List;

abstract class CheckedSyntax implements Syntax {

    public static final String id = "$Id: CheckedSyntax.java 699 2004-03-26 10:32:44Z sielenk $";

    private final Arity _arity;

    protected CheckedSyntax(Arity arity) {
        _arity = arity;
    }

    protected static void arityError(List arguments, Arity arity) throws SyntaxArityError {
        throw new SyntaxArityError(arguments, arity);
    }

    protected void arityError(List arguments) throws SyntaxArityError {
        throw new SyntaxArityError(arguments, _arity);
    }

    public final Forceable translate(StaticEnvironment compilationEnv, List arguments) throws SchemeException {
        int len = arguments.getLength();
        if (!_arity.isValid(len)) {
            arityError(arguments);
        }
        preTranslate(compilationEnv);
        return checkedTranslate(compilationEnv, arguments);
    }

    protected void preTranslate(StaticEnvironment compilationEnv) {
        compilationEnv.setStateClosed();
    }

    protected abstract Forceable checkedTranslate(StaticEnvironment compilationEnv, List arguments) throws SchemeException;
}

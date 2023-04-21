package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;

public class ProgramError extends LispError {

    protected ProgramError(LispClass cls) throws ConditionThrowable {
        super(cls);
    }

    public ProgramError(LispObject initArgs) throws ConditionThrowable {
        super(StandardClass.PROGRAM_ERROR);
        initialize(initArgs);
        if (initArgs.isList() && initArgs.CAR().isString()) {
            setFormatControl(initArgs.CAR().getStringValue());
            setFormatArguments(initArgs.CDR());
        }
    }

    public ProgramError(String message) throws ConditionThrowable {
        super(StandardClass.PROGRAM_ERROR);
        setFormatControl(message);
        setFormatArguments(NIL);
    }

    @Override
    public LispObject typeOf() {
        return SymbolConstants.PROGRAM_ERROR;
    }

    @Override
    public LispObject classOf() {
        return StandardClass.PROGRAM_ERROR;
    }

    @Override
    public LispObject typep(LispObject type) throws ConditionThrowable {
        if (type == SymbolConstants.PROGRAM_ERROR) return T;
        if (type == StandardClass.PROGRAM_ERROR) return T;
        return super.typep(type);
    }
}

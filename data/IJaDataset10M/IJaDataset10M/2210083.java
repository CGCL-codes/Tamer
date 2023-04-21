package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;

public final class ReaderError extends StreamError {

    public ReaderError(String message) throws ConditionThrowable {
        super(StandardClass.READER_ERROR);
        setFormatControl(message);
        setFormatArguments(NIL);
    }

    public ReaderError(String message, Stream stream) throws ConditionThrowable {
        super(StandardClass.READER_ERROR);
        setFormatControl(message);
        setFormatArguments(NIL);
        setStream(stream);
    }

    public ReaderError(LispObject initArgs) throws ConditionThrowable {
        super(StandardClass.READER_ERROR);
        initialize(initArgs);
    }

    @Override
    public LispObject typeOf() {
        return SymbolConstants.READER_ERROR;
    }

    @Override
    public LispObject classOf() {
        return StandardClass.READER_ERROR;
    }

    @Override
    public LispObject typep(LispObject type) throws ConditionThrowable {
        if (type == SymbolConstants.READER_ERROR) return T;
        if (type == StandardClass.READER_ERROR) return T;
        if (type == SymbolConstants.PARSE_ERROR) return T;
        if (type == StandardClass.PARSE_ERROR) return T;
        return super.typep(type);
    }

    @Override
    public String getMessage() {
        return message;
    }
}

package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;

public final class UpcaseStream extends CaseFrobStream {

    public UpcaseStream(Stream target) throws ConditionThrowable {
        super(target);
    }

    @Override
    public void _writeChar(char c) throws ConditionThrowable {
        target._writeChar(LispCharacter.toUpperCase(c));
    }

    @Override
    public void _writeString(String s) throws ConditionThrowable {
        target._writeString(s.toUpperCase());
    }

    @Override
    public void _writeLine(String s) throws ConditionThrowable {
        target._writeLine(s.toUpperCase());
    }
}

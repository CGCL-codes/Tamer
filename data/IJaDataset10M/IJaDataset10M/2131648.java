package org.armedbear.lisp;

public final class read_char_no_hang extends Primitive {

    private read_char_no_hang() {
        super("read-char-no-hang", "&optional input-stream eof-error-p eof-value recursive-p");
    }

    public LispObject execute(LispObject[] args) throws ConditionThrowable {
        int length = args.length;
        if (length > 4) error(new WrongNumberOfArgumentsException(this));
        Stream stream = length > 0 ? inSynonymOf(args[0]) : getStandardInput();
        boolean eofError = length > 1 ? (args[1] != NIL) : true;
        LispObject eofValue = length > 2 ? args[2] : NIL;
        return stream.readCharNoHang(eofError, eofValue);
    }

    private static final Primitive READ_CHAR_NO_HANG = new read_char_no_hang();
}

package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;
import java.io.File;

public final class file_author extends Primitive {

    private file_author() {
        super("file-author");
    }

    @Override
    public LispObject execute(LispObject arg) throws ConditionThrowable {
        Pathname pathname = coerceToPathname(arg);
        if (pathname.isWild()) error(new FileError("Bad place for a wild pathname.", pathname));
        return NIL;
    }

    private static final Primitive FILE_AUTHOR = new file_author();
}

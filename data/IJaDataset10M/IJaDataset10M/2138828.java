package org.maverickdbms.util;

import org.maverickdbms.basic.ConstantString;
import org.maverickdbms.basic.MaverickException;
import org.maverickdbms.basic.File;
import org.maverickdbms.basic.Program;
import org.maverickdbms.basic.Session;
import org.maverickdbms.basic.MaverickString;

public class CREATE_056FILE implements Program {

    public ConstantString run(Session session, MaverickString[] args) throws MaverickException {
        MaverickString name = args[0];
        int flags = 0;
        ConstantString type = ConstantString.EMPTY;
        int offset = 1;
        if (name.toString().equals("DATA") || name.toString().equals("DICT")) {
            type = name;
            name = args[1];
            offset = 2;
        }
        ConstantString[] extraArgs = new ConstantString[args.length - offset];
        System.arraycopy(args, offset, extraArgs, 0, extraArgs.length);
        session.getFactory().createFile(type, name, flags, extraArgs);
        return null;
    }
}

;

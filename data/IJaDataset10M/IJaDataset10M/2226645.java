package org.maverickdbms.database.lh;

import org.maverickdbms.basic.mvConstants;
import org.maverickdbms.basic.mvConstantString;
import org.maverickdbms.basic.mvFile;
import org.maverickdbms.basic.mvProgram;
import org.maverickdbms.basic.mvResolver;
import org.maverickdbms.basic.mvSession;
import org.maverickdbms.basic.mvString;

public class lhResolver implements mvProgram, mvResolver {

    private mvResolver next;

    private mvSession session;

    public mvResolver getNextResolver() {
        return next;
    }

    public void createFile(mvConstantString type, mvConstantString name, mvConstantString[] fields) {
    }

    public void dropFile(mvConstantString type, mvConstantString name) {
    }

    public void init(mvSession s) {
        session = s;
    }

    public mvConstantString resolveFile(mvString var, mvConstantString type, mvConstantString key, int flags) {
        return (next != null) ? next.resolveFile(var, type, key, flags) : RETURN_ERROR;
    }

    public mvProgram resolveProgram(mvConstantString key) {
        return (next != null) ? next.resolveProgram(key) : null;
    }

    public mvConstantString run(mvString[] args) {
        session.getFactory().pushResolver(this);
        return null;
    }

    public void setNextResolver(mvResolver n) {
        next = n;
    }
}

;

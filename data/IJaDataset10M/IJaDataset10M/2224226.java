package org.nakedobjects.distribution.command;

import org.nakedobjects.distribution.Distribution;
import org.nakedobjects.distribution.ObjectData;
import org.nakedobjects.object.persistence.Oid;
import org.nakedobjects.object.security.Session;

public class MakePersistent extends AbstractRequest {

    private ObjectData object;

    public MakePersistent(Session session, ObjectData object) {
        super(session);
        this.object = object;
    }

    public void execute(Distribution distribution) {
        response = distribution.makePersistent(session, object);
    }

    public Oid[] getOids() {
        return (Oid[]) response;
    }
}

package org.nakedobjects.distribution.client;

import org.nakedobjects.distribution.ObjectRequest;
import org.nakedobjects.distribution.RequestContext;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectRuntimeException;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.ObjectStoreException;
import org.nakedobjects.object.io.Memento;

public class GetObjectRequest extends ObjectRequest {

    private static final long serialVersionUID = 1L;

    public GetObjectRequest(Object oid, NakedObjectSpecification hint) {
        super(oid, hint);
    }

    protected void generateResponse(RequestContext server) {
        try {
            NakedObject object = getObject(server.getLoadedObjects());
            response = new Memento(object);
        } catch (ObjectStoreException e) {
            response = e;
        }
    }

    public NakedObject getObject() {
        sendRequest();
        if (response instanceof ObjectStoreException) {
            throw new NakedObjectRuntimeException((Exception) response);
        }
        Memento memento = (Memento) response;
        return memento.recreateObject(getLoadedObjects());
    }

    public String toString() {
        return "GetObjectRequest [" + id + ",oid=" + externalOid + "]";
    }
}

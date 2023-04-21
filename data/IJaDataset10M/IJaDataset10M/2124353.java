package org.nakedobjects.distribution.client;

import org.nakedobjects.distribution.Request;
import org.nakedobjects.distribution.RequestContext;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.io.TransferableReader;
import org.nakedobjects.object.io.TransferableWriter;

public class NumberOfInstances extends Request {

    private static final long serialVersionUID = 1L;

    private String className;

    public NumberOfInstances(NakedObjectSpecification cls) {
        className = cls.getFullName();
    }

    protected void generateResponse(RequestContext context) {
        NakedObjectSpecification cls = NakedObjectSpecification.getSpecification(className);
        response.writeInt(context.getObjectManager().numberOfInstances(cls));
    }

    public int size() {
        sendRequest();
        return response.readInt();
    }

    public String toString() {
        return "Number of instances [" + className + "]";
    }

    public void writeData(TransferableWriter data) {
        data.writeString(className);
    }

    public void restore(TransferableReader data) {
        className = data.readString();
    }
}

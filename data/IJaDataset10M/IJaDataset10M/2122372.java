package org.nakedobjects.plugins.remoting.command.shared.requests;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.ByteDecoder;
import org.nakedobjects.metamodel.commons.encoding.ByteEncoder;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.remoting.shared.ServerFacade;
import org.nakedobjects.plugins.remoting.shared.encoding.object.data.ObjectData;

public class GetObject extends RequestAbstract {

    private static final long serialVersionUID = 1L;

    private final Oid oid;

    private final String specificationName;

    public GetObject(final AuthenticationSession session, final Oid oid, final String specificationName) {
        super(session);
        this.oid = oid;
        this.specificationName = specificationName;
    }

    public GetObject(final ByteDecoder decoder) {
        super(decoder);
        oid = (Oid) decoder.getObject();
        specificationName = decoder.getString();
    }

    @Override
    protected void doEncode(final ByteEncoder encoder) {
        encoder.add(oid);
        encoder.add(specificationName);
    }

    public ObjectData getObject() {
        return (ObjectData) response;
    }

    public void execute(final ServerFacade distribution) {
        final ObjectData object = distribution.getObject(session, oid, specificationName);
        setResponse(object);
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("oid", oid);
        str.append("spec", specificationName);
        return str.toString();
    }
}

package org.nakedobjects.plugins.remoting.command.shared.requests;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.ByteDecoder;
import org.nakedobjects.metamodel.commons.encoding.ByteEncoder;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.remoting.shared.ServerFacade;
import org.nakedobjects.plugins.remoting.shared.data.Data;
import org.nakedobjects.plugins.remoting.shared.encoding.object.data.IdentityData;

public class ResolveField extends RequestAbstract {

    private static final long serialVersionUID = 1L;

    private final IdentityData target;

    private final String fieldIdentifier;

    public ResolveField(final AuthenticationSession session, final IdentityData target, final String field) {
        super(session);
        this.target = target;
        this.fieldIdentifier = field;
    }

    public ResolveField(final ByteDecoder decoder) {
        super(decoder);
        fieldIdentifier = decoder.getString();
        target = (IdentityData) decoder.getObject();
    }

    @Override
    protected void doEncode(final ByteEncoder encoder) {
        encoder.add(fieldIdentifier);
        encoder.add(target);
    }

    public void execute(final ServerFacade sd) {
        setResponse(sd.resolveField(session, target, fieldIdentifier));
    }

    public Data getUpdateData() {
        return (Data) getResponse();
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("target", target);
        str.append("field", fieldIdentifier);
        return str.toString();
    }
}

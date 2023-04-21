package org.nakedobjects.remoting.command.shared.requests;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.ByteDecoder;
import org.nakedobjects.metamodel.commons.encoding.ByteEncoder;
import org.nakedobjects.remoting.shared.ServerFacade;
import org.nakedobjects.remoting.shared.encoding.object.data.IdentityData;
import org.nakedobjects.remoting.shared.encoding.object.data.ObjectData;

public class ClearValue extends RequestAbstract {

    private static final long serialVersionUID = 1L;

    private final String fieldIdentifier;

    private final IdentityData target;

    public ClearValue(final AuthenticationSession session, final String fieldIdentifier, final IdentityData target) {
        super(session);
        this.fieldIdentifier = fieldIdentifier;
        this.target = target;
    }

    public ClearValue(final ByteDecoder decoder) {
        super(decoder);
        fieldIdentifier = decoder.getString();
        target = (IdentityData) decoder.getObject();
    }

    @Override
    protected void doEncode(final ByteEncoder encoder) {
        encoder.add(fieldIdentifier);
        encoder.add(target);
    }

    public void execute(final ServerFacade distribution) {
        response = distribution.clearValue(session, fieldIdentifier, target);
    }

    public ObjectData[] getChanges() {
        return (ObjectData[]) response;
    }
}

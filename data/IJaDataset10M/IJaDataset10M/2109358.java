package org.nakedobjects.nos.remote.command;

import org.nakedobjects.noa.security.Session;
import org.nakedobjects.noa.util.ByteDecoder;
import org.nakedobjects.noa.util.ByteEncoder;
import org.nakedobjects.nof.core.util.ToString;
import org.nakedobjects.nof.reflect.remote.data.Data;
import org.nakedobjects.nof.reflect.remote.data.Distribution;
import org.nakedobjects.nof.reflect.remote.data.IdentityData;

public class ResolveField extends AbstractRequest {

    private static final long serialVersionUID = 1L;

    private final IdentityData target;

    private final String fieldIdentifier;

    public ResolveField(final Session session, final IdentityData target, final String field) {
        super(session);
        this.target = target;
        this.fieldIdentifier = field;
    }

    public ResolveField(ByteDecoder decoder) {
        super(decoder);
        fieldIdentifier = decoder.getString();
        target = (IdentityData) decoder.getObject();
    }

    protected void doEncode(final ByteEncoder encoder) {
        encoder.add(fieldIdentifier);
        encoder.add(target);
    }

    public void execute(final Distribution sd) {
        setResponse(sd.resolveField(session, target, fieldIdentifier));
    }

    public Data getUpdateData() {
        return (Data) getResponse();
    }

    public String toString() {
        ToString str = new ToString(this);
        str.append("target", target);
        str.append("field", fieldIdentifier);
        return str.toString();
    }
}

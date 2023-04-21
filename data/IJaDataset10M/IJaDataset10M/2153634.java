package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.encoding.DataInputExtended;
import org.nakedobjects.metamodel.encoding.DataOutputExtended;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.facade.ServerFacade;

public class ClearValueRequest extends RequestAbstract {

    private static final long serialVersionUID = 1L;

    private final String fieldIdentifier;

    private final IdentityData target;

    public ClearValueRequest(final AuthenticationSession session, final String fieldIdentifier, final IdentityData target) {
        super(session);
        this.fieldIdentifier = fieldIdentifier;
        this.target = target;
        initialized();
    }

    public ClearValueRequest(final DataInputExtended input) throws IOException {
        super(input);
        this.fieldIdentifier = input.readUTF();
        this.target = input.readEncodable(IdentityData.class);
        initialized();
    }

    @Override
    public void encode(DataOutputExtended outputStream) throws IOException {
        super.encode(outputStream);
        outputStream.writeUTF(fieldIdentifier);
        outputStream.writeEncodable(target);
    }

    private void initialized() {
    }

    public String getFieldIdentifier() {
        return fieldIdentifier;
    }

    public IdentityData getTarget() {
        return target;
    }

    /**
	 * {@link #setResponse(Object) Sets a response} of {@link ObjectData}[] (array).
	 */
    public void execute(final ServerFacade serverFacade) {
        ClearValueResponse response = serverFacade.clearValue(this);
        setResponse(response);
    }

    /**
     * Downcasts.
     */
    @Override
    public ClearValueResponse getResponse() {
        return (ClearValueResponse) super.getResponse();
    }
}

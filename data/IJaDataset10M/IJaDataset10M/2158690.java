package be.fedict.eid.applet.shared;

import be.fedict.eid.applet.shared.annotation.HttpHeader;
import be.fedict.eid.applet.shared.annotation.MessageDiscriminator;
import be.fedict.eid.applet.shared.annotation.StateTransition;
import be.fedict.eid.applet.shared.protocol.ProtocolState;

/**
 * Sign certificates request message transfer object.
 * 
 * @author Frank Cornelis
 * 
 */
@StateTransition(ProtocolState.SIGN_CERTS)
public class SignCertificatesRequestMessage extends AbstractProtocolMessage {

    @HttpHeader(TYPE_HTTP_HEADER)
    @MessageDiscriminator
    public static final String TYPE = SignCertificatesRequestMessage.class.getSimpleName();

    @HttpHeader(HTTP_HEADER_PREFIX + "IncludeIdentity")
    public boolean includeIdentity;

    @HttpHeader(HTTP_HEADER_PREFIX + "IncludeAddress")
    public boolean includeAddress;

    @HttpHeader(HTTP_HEADER_PREFIX + "IncludePhoto")
    public boolean includePhoto;

    @HttpHeader(HTTP_HEADER_PREFIX + "IncludeIntegrityData")
    public boolean includeIntegrityData;

    public SignCertificatesRequestMessage() {
        super();
    }

    public SignCertificatesRequestMessage(boolean includeIdentity, boolean includeAddress, boolean includePhoto, boolean includeIntegrityData) {
        this.includeIdentity = includeIdentity;
        this.includeAddress = includeAddress;
        this.includePhoto = includePhoto;
        this.includeIntegrityData = includeIntegrityData;
    }
}

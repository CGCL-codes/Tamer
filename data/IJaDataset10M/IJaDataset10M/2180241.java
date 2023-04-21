package org.mobicents.protocols.ss7.map.errors;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.errors.CallBarringCause;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageCallBarred;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPErrorMessageCallBarredImpl extends MAPErrorMessageImpl implements MAPErrorMessageCallBarred {

    public static final int unauthorisedMessageOriginator_TAG = 1;

    private long mapProtocolVersion;

    private CallBarringCause callBarringCause;

    private MAPExtensionContainer extensionContainer;

    private Boolean unauthorisedMessageOriginator;

    public MAPErrorMessageCallBarredImpl(long mapProtocolVersion, CallBarringCause callBarringCause, MAPExtensionContainer extensionContainer, Boolean unauthorisedMessageOriginator) {
        super((long) MAPErrorCode.callBarred);
        this.mapProtocolVersion = mapProtocolVersion;
        this.callBarringCause = callBarringCause;
        this.extensionContainer = extensionContainer;
        this.unauthorisedMessageOriginator = unauthorisedMessageOriginator;
    }

    protected MAPErrorMessageCallBarredImpl() {
        super((long) MAPErrorCode.callBarred);
    }

    @Override
    public boolean isEmCallBarred() {
        return true;
    }

    @Override
    public MAPErrorMessageCallBarred getEmCallBarred() {
        return this;
    }

    @Override
    public CallBarringCause getCallBarringCause() {
        return this.callBarringCause;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public Boolean getUnauthorisedMessageOriginator() {
        return this.unauthorisedMessageOriginator;
    }

    @Override
    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    @Override
    public void setCallBarringCause(CallBarringCause callBarringCause) {
        this.callBarringCause = callBarringCause;
    }

    @Override
    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    @Override
    public void setUnauthorisedMessageOriginator(Boolean unauthorisedMessageOriginator) {
        this.unauthorisedMessageOriginator = unauthorisedMessageOriginator;
    }

    @Override
    public void setMapProtocolVersion(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    @Override
    public int getTag() throws MAPException {
        if (this.mapProtocolVersion < 3) return Tag.ENUMERATED; else return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        if (this.mapProtocolVersion < 3) return true; else return false;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPErrorMessageCallBarred: " + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageCallBarred: " + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPErrorMessageCallBarred: " + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageCallBarred: " + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.callBarringCause = null;
        this.unauthorisedMessageOriginator = null;
        this.extensionContainer = null;
        if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL) throw new MAPParsingComponentException("Error decoding MAPErrorMessageCallBarred: bad tag class", MAPParsingComponentExceptionReason.MistypedParameter);
        switch(localAis.getTag()) {
            case Tag.ENUMERATED:
                if (!localAis.isTagPrimitive()) throw new MAPParsingComponentException("Error decoding MAPErrorMessageCallBarred: ENUMERATED tag but data is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                int code = (int) localAis.readIntegerData(length);
                this.callBarringCause = CallBarringCause.getInstance(code);
                if (this.callBarringCause == null) throw new MAPParsingComponentException("Error decoding MAPErrorMessageCallBarred.callBarringCause: bad code value", MAPParsingComponentExceptionReason.MistypedParameter);
                this.mapProtocolVersion = 2;
                break;
            case Tag.SEQUENCE:
                if (localAis.isTagPrimitive()) throw new MAPParsingComponentException("Error decoding MAPErrorMessageCallBarred: SEQUENCE tag but data is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                AsnInputStream ais = localAis.readSequenceStreamData(length);
                while (true) {
                    if (ais.available() == 0) break;
                    int tag = ais.readTag();
                    switch(ais.getTagClass()) {
                        case Tag.CLASS_UNIVERSAL:
                            switch(tag) {
                                case Tag.ENUMERATED:
                                    code = (int) ais.readInteger();
                                    this.callBarringCause = CallBarringCause.getInstance(code);
                                    if (this.callBarringCause == null) throw new MAPParsingComponentException("Error decoding MAPErrorMessageCallBarred.callBarringCause: bad code value", MAPParsingComponentExceptionReason.MistypedParameter);
                                    break;
                                case Tag.SEQUENCE:
                                    this.extensionContainer = new MAPExtensionContainerImpl();
                                    ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                                    break;
                                default:
                                    ais.advanceElement();
                                    break;
                            }
                            break;
                        case Tag.CLASS_CONTEXT_SPECIFIC:
                            switch(tag) {
                                case unauthorisedMessageOriginator_TAG:
                                    ais.readNull();
                                    this.unauthorisedMessageOriginator = true;
                                    break;
                                default:
                                    ais.advanceElement();
                                    break;
                            }
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                }
                this.mapProtocolVersion = 3;
                break;
            default:
                throw new MAPParsingComponentException("Error decoding MAPErrorMessageCallBarred: bad tag", MAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (this.unauthorisedMessageOriginator == null) this.unauthorisedMessageOriginator = false;
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        if (this.mapProtocolVersion < 3) this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.ENUMERATED); else this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            if (this.mapProtocolVersion < 3) asnOs.writeTag(tagClass, true, tag); else asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPErrorMessageCallBarred: " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream aos) throws MAPException {
        if (this.callBarringCause == null && (this.unauthorisedMessageOriginator == null || this.unauthorisedMessageOriginator == false) && this.extensionContainer == null) return;
        if (this.callBarringCause == null && this.mapProtocolVersion < 3) return;
        try {
            if (this.mapProtocolVersion < 3) {
                aos.writeIntegerData(this.callBarringCause.getCode());
            } else {
                if (this.callBarringCause != null) aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.callBarringCause.getCode());
                if (this.extensionContainer != null) ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(aos);
                if (this.unauthorisedMessageOriginator != null && this.unauthorisedMessageOriginator == true) aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, unauthorisedMessageOriginator_TAG);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding MAPErrorMessageCallBarred: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPErrorMessageCallBarred: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MAPErrorMessageCallBarred [");
        if (this.callBarringCause != null) sb.append("callBarringCause=" + this.callBarringCause.toString());
        if (this.extensionContainer != null) sb.append(", extensionContainer=" + this.extensionContainer.toString());
        if (this.unauthorisedMessageOriginator != null && this.unauthorisedMessageOriginator == true) sb.append(", unauthorisedMessageOriginator=true");
        sb.append("]");
        return sb.toString();
    }
}

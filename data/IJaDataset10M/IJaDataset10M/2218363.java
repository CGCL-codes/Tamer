package org.mobicents.protocols.ss7.cap.EsiBcsm;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TNoAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TNoAnswerSpecificInfoImpl implements TNoAnswerSpecificInfo, CAPAsnPrimitive {

    public static final int _ID_callForwarded = 50;

    public static final int _ID_forwardingDestinationNumber = 52;

    public static final String _PrimitiveName = "TNoAnswerSpecificInfo";

    private boolean callForwarded;

    private CalledPartyNumberCap forwardingDestinationNumber;

    public TNoAnswerSpecificInfoImpl() {
    }

    public TNoAnswerSpecificInfoImpl(boolean callForwarded, CalledPartyNumberCap forwardingDestinationNumber) {
        this.callForwarded = callForwarded;
        this.forwardingDestinationNumber = forwardingDestinationNumber;
    }

    @Override
    public boolean getCallForwarded() {
        return callForwarded;
    }

    @Override
    public CalledPartyNumberCap getForwardingDestinationNumber() {
        return forwardingDestinationNumber;
    }

    @Override
    public int getTag() throws CAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException, IOException, AsnException {
        this.callForwarded = false;
        this.forwardingDestinationNumber = null;
        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0) break;
            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch(tag) {
                    case _ID_callForwarded:
                        ais.readNull();
                        this.callForwarded = true;
                        break;
                    case _ID_forwardingDestinationNumber:
                        this.forwardingDestinationNumber = new CalledPartyNumberCapImpl();
                        ((CalledPartyNumberCapImpl) this.forwardingDestinationNumber).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {
        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {
        try {
            if (this.callForwarded) asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_callForwarded);
            if (this.forwardingDestinationNumber != null) ((CalledPartyNumberCapImpl) this.forwardingDestinationNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_forwardingDestinationNumber);
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.callForwarded) {
            sb.append("callForwarded");
        }
        if (this.forwardingDestinationNumber != null) {
            sb.append(", forwardingDestinationNumber= [");
            sb.append(forwardingDestinationNumber);
            sb.append("]");
        }
        sb.append("]");
        return sb.toString();
    }
}

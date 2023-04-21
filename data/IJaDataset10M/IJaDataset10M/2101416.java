package org.mobicents.protocols.ss7.map.service.sms;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MWStatusImpl implements MWStatus, MAPAsnPrimitive {

    private static final int _INDEX_ScAddressNotIncluded = 0;

    private static final int _INDEX_MnrfSet = 1;

    private static final int _INDEX_McefSet = 2;

    private static final int _INDEX_MnrgSet = 3;

    private BitSetStrictLength bitString = new BitSetStrictLength(6);

    public MWStatusImpl() {
    }

    public MWStatusImpl(boolean scAddressNotIncluded, boolean mnrfSet, boolean mcefSet, boolean mnrgSet) {
        if (scAddressNotIncluded) this.bitString.set(_INDEX_ScAddressNotIncluded);
        if (mnrfSet) this.bitString.set(_INDEX_MnrfSet);
        if (mcefSet) this.bitString.set(_INDEX_McefSet);
        if (mnrgSet) this.bitString.set(_INDEX_MnrgSet);
    }

    @Override
    public boolean getScAddressNotIncluded() {
        return this.bitString.get(_INDEX_ScAddressNotIncluded);
    }

    @Override
    public boolean getMnrfSet() {
        return this.bitString.get(_INDEX_MnrfSet);
    }

    @Override
    public boolean getMcefSet() {
        return this.bitString.get(_INDEX_McefSet);
    }

    @Override
    public boolean getMnrgSet() {
        return this.bitString.get(_INDEX_MnrgSet);
    }

    @Override
    public int getTag() throws MAPException {
        return Tag.STRING_BIT;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MWStatus: " + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MWStatus: " + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MWStatus: " + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MWStatus: " + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        if (length == 0 || length > 2) throw new MAPParsingComponentException("Error decoding MWStatus: the MWStatus field must contain from 1 or 2 octets. Contains: " + length, MAPParsingComponentExceptionReason.MistypedParameter);
        this.bitString = ansIS.readBitStringData(length);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_BIT);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, true, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MWStatus: " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            asnOs.writeBitStringData(this.bitString);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding MWStatus: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MWStatus: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "MWStatus [ScAddressNotIncluded=" + this.getScAddressNotIncluded() + ", MnrfSet=" + this.getMnrfSet() + ", McefSet=" + this.getMcefSet() + ", MnrgSet=" + this.getMnrgSet() + "]";
    }
}

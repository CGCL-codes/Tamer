package net.sourceforge.jwap.wtp.pdu;

import net.sourceforge.jwap.util.BitArrayOutputStream;
import net.sourceforge.jwap.util.Utils;

/**
 * This Class represents an Result PDU.
 * According to the WTP specification in section 8 this PDU
 * can be encoded into a byte array.
 * <br><br>
 * The first 3 bytes of the PDU are used for the WTP Layer.
 * After this the payload follows - e.g. the bytes of a upper Layer.
 * To encode the PDU call toByteArray().
 * <br><br>
 * There are to ways of creation: <b>Either</b> you construct a Object
 * manually by calling the constructor <b>or</b> you use CWTPFactory
 * to decode a byte Array.
 */
public class CWTPResult extends CWTPPDU {

    /**
     * @param payload The Bytes belonging to the layer above
     * @param TID the Transaction ID according to the spec
     */
    public CWTPResult(byte[] payload, int TID) {
        super(payload, TID, PDU_TYPE_RESULT);
    }

    /**
     * Encodes the PDU according to the WTP spec
     *
     * @return encoded bytes
     */
    public byte[] toByteArray() {
        BitArrayOutputStream result = new BitArrayOutputStream();
        result.write(CON);
        result.write(pduType, 4);
        result.write(GTR);
        result.write(TTR);
        result.write(RID);
        result.write(TID, 16);
        result.write(payload);
        return result.toByteArray();
    }

    /**
     * constructs a string representation of the object
     * invluding all fields.
     *
     * @return The constructed String with debug information
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[WTP Result PDU\n");
        sb.append(" CON: ").append(CON ? 1 : 0).append("\n");
        sb.append(" GTR: ").append(GTR ? 1 : 0).append("\n");
        sb.append(" TTR: ").append(TTR ? 1 : 0).append("\n");
        sb.append(" RID: ").append(RID ? 1 : 0).append("\n");
        sb.append(" TID: ").append(Utils.hexString(TID)).append("\n");
        sb.append(Utils.hexDump(" PAYLOAD: ", payload));
        sb.append("]");
        return sb.toString();
    }
}

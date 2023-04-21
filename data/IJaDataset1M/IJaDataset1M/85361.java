package net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.compare;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.tlv.TLV;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.codec.EncoderException;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.LdapConstants;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.LdapResponse;

/**
 * An CompareResponse Message. Its syntax is : 
 * 
 * CompareResponse ::= [APPLICATION 15] LDAPResult
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CompareResponse extends LdapResponse {

    /**
     * Creates a new CompareResponse object.
     */
    public CompareResponse() {
        super();
    }

    /**
     * Get the message type
     * 
     * @return Returns the type.
     */
    public int getMessageType() {
        return LdapConstants.COMPARE_RESPONSE;
    }

    /**
     * Compute the CompareResponse length 
     * 
     * CompareResponse :
     * 
     * 0x6F L1
     *  |
     *  +--> LdapResult
     * 
     * L1 = Length(LdapResult)
     * 
     * Length(CompareResponse) = Length(0x6F) + Length(L1) + L1
     */
    public int computeLength() {
        int ldapResponseLength = super.computeLength();
        return 1 + TLV.getNbBytes(ldapResponseLength) + ldapResponseLength;
    }

    /**
     * Encode the CompareResponse message to a PDU.
     * 
     * @param buffer The buffer where to put the PDU
     * @return The PDU.
     */
    public ByteBuffer encode(ByteBuffer buffer) throws EncoderException {
        if (buffer == null) {
            throw new EncoderException("Cannot put a PDU in a null buffer !");
        }
        try {
            buffer.put(LdapConstants.COMPARE_RESPONSE_TAG);
            buffer.put(TLV.getBytes(getLdapResponseLength()));
        } catch (BufferOverflowException boe) {
            throw new EncoderException("The PDU buffer size is too small !");
        }
        return super.encode(buffer);
    }

    /**
     * Get a String representation of an CompareResponse
     * 
     * @return An CompareResponse String
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("    Compare Response\n");
        sb.append(super.toString());
        return sb.toString();
    }
}

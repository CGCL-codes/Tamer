package net.sourceforge.myvd.protocol.ldap.mina.asn1.der;

import java.io.IOException;

/**
 * DER Bit String object.
 */
public class DERBitString extends DERObject {

    /**
     * Basic DERObject constructor.
     */
    public DERBitString(byte[] value) {
        super(BIT_STRING, value);
    }

    public byte[] getOctets() {
        return value;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        byte[] bytes = new byte[value.length + 1];
        bytes[0] = (byte) 0x00;
        System.arraycopy(value, 0, bytes, 1, bytes.length - 1);
        out.writeEncoded(BIT_STRING, bytes);
    }
}

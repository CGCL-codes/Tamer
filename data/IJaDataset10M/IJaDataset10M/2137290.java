package DE.FhG.IGD.atlas.lsp;

import DE.FhG.IGD.util.URL;
import DE.FhG.IGD.atlas.core.StorageDBEntry;
import DE.FhG.IGD.atlas.core.StorageDBImmutableEntry;
import codec.*;
import codec.asn1.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.math.BigInteger;

/**
 * Represents a LSP_Entry structure of the Location Service
 * Protocol (LSP).
 *
 * It contains all infomation stored in a Location Service Server
 * ({@link DE.FhG.IGD.atlas.core.LSServer LSServer}) used to locate
 * the identified mobile object. There exists a mapping between this class
 * and <code>StorageDBEntry</code> respectivley
 * <code>StorageDBImmutableEntry</code>.<p>
 * 
 * The ASN.1 structure of this entry is as follows
 * <blockquote><pre>
 * LSP_Entry ::= SEQUENCE
 * {
 *   implicitName     ImplicitName,
 *   contactaddress   ContactAddress,
 *   cookie           Cookie,
 *   timestamp        Timestamp
 * }
 * 
 * ImplicitName   ::= OCTET STRING
 * ContactAddress ::= OCTET STRING
 * Cookie         ::= OCTET STRING
 * Timestamp      ::= INTEGER
 * </pre></blockquote>
 *
 * The implicit name is the unique identifier of an mobile object
 * for instance the hashcode of its static part. The contact address
 * should consist of a URL string that specifies the protocol, address,
 * and port of a management service provided by the server, which is in
 * possession of the specified object. The cookie is used as authorisation
 * mechanism by the LS-Server permitting a client to update or delete an
 * entry. The timestamp represents the last modification time in milliseconds
 * since January 1, 1970, 00:00:00 GMT.
 * 
 * @author Jan Peters
 * @version "$Id: LSPEntry.java 462 2001-08-21 18:21:00Z vroth $"
 * @see DE.FhG.IGD.atlas.core.LSServerService
 * @see DE.FhG.IGD.atlas.core.StorageDBEntry
 * @see DE.FhG.IGD.atlas.core.StorageDBImmutableEntry
 */
public class LSPEntry extends ASN1Sequence {

    /**
     * The implicit name of this entry.
     */
    protected ASN1OctetString implicitName_;

    /**
     * The contact address of this entry.
     */
    protected ASN1OctetString contactAddress_;

    /**
     * The cookie of this entry.
     */
    protected ASN1OctetString cookie_;

    /**
     * The timestamp of this entry.
     */
    protected ASN1Integer timestamp_;

    /**
     * Creates a new entry from the given encoded entry.
     * 
     * @param code The encoded entry.
     * @return The decoded entry.
     * @exception CorruptedCodeException if the code is bad.
     * @exception IllegalStateException if an error occurs
     *   while decoding with <code>DERDecoder</code> (RuntimeException).
     * @exception NullPointerException if <code>code</code>
     *   is <code>null</code> (RuntimeException).
     */
    public static LSPEntry createEntry(byte[] code) throws CorruptedCodeException, IllegalStateException, NullPointerException {
        ByteArrayInputStream bis;
        LSPEntry entry;
        DERDecoder dec;
        if (code == null) {
            throw new NullPointerException("Need the code of an entry!");
        }
        bis = new ByteArrayInputStream(code);
        dec = new DERDecoder(bis);
        try {
            entry = new LSPEntry();
            entry.decode(dec);
            dec.close();
            return entry;
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (IOException e) {
            throw new IllegalStateException("Caught " + e.toString());
        } catch (ASN1Exception e) {
            throw new CorruptedCodeException("Caught " + e.toString());
        }
    }

    /**
     * Creates an instance ready for decoding.
     */
    public LSPEntry() {
        super(4);
        implicitName_ = new ASN1OctetString();
        contactAddress_ = new ASN1OctetString();
        cookie_ = new ASN1OctetString();
        timestamp_ = new ASN1Integer();
        add(implicitName_);
        add(contactAddress_);
        add(cookie_);
        add(timestamp_);
    }

    /**
     * Creates an instance from the given entry.
     *
     * @param entry The entry.
     * @exception NullPointerException if <code>entry</code>
     *   is <code>null</code> (RuntimeException).
     */
    public LSPEntry(StorageDBEntry entry) throws NullPointerException {
        super(4);
        if (entry == null) {
            throw new NullPointerException("Need an entry!");
        }
        implicitName_ = new ASN1OctetString(entry.getImplicitName());
        contactAddress_ = new ASN1OctetString(entry.getContactAddress().toString().getBytes());
        cookie_ = new ASN1OctetString(entry.getCookie());
        timestamp_ = new ASN1Integer(BigInteger.valueOf(entry.getTimestamp()));
        add(implicitName_);
        add(contactAddress_);
        add(cookie_);
        add(timestamp_);
    }

    /**
     * Creates an instance from the given entry.
     *
     * @param entry The entry.
     * @exception NullPointerException if <code>entry</code>
     *   is <code>null</code> (RuntimeException).
     */
    public LSPEntry(StorageDBImmutableEntry entry) throws NullPointerException {
        super(4);
        if (entry == null) {
            throw new NullPointerException("Need an entry!");
        }
        implicitName_ = new ASN1OctetString(entry.getImplicitName());
        contactAddress_ = new ASN1OctetString(entry.getContactAddress().toString().getBytes());
        cookie_ = new ASN1OctetString(entry.getCookie());
        timestamp_ = new ASN1Integer(BigInteger.valueOf(entry.getTimestamp()));
        add(implicitName_);
        add(contactAddress_);
        add(cookie_);
        add(timestamp_);
    }

    /**
     * Creates an instance with the given parameters.
     *
     * @param implicitName The implicit name of the entry.
     * @param contactAddress The contact address of the entry.
     * @param cookie The cookie of the entry.
     * @param timestamp The timestamp of the entry.
     */
    public LSPEntry(byte[] implicitName, URL contactAddress, byte[] cookie, long timestamp) {
        super(4);
        implicitName_ = new ASN1OctetString(implicitName);
        contactAddress_ = new ASN1OctetString(contactAddress.toString().getBytes());
        cookie_ = new ASN1OctetString(cookie);
        timestamp_ = new ASN1Integer(BigInteger.valueOf(timestamp));
        add(implicitName_);
        add(contactAddress_);
        add(cookie_);
        add(timestamp_);
    }

    /**
     * Returns the implicit name.
     *
     * @return The implicit name.
     */
    public byte[] getImplicitName() {
        return implicitName_.getByteArray();
    }

    /**
     * Returns the contact address or an empty URL, 
     * if the encoded contact address is not well-formed.
     *
     * @return The contact address.
     */
    public URL getContactAddress() {
        try {
            return new URL(new String(contactAddress_.getByteArray()));
        } catch (MalformedURLException e) {
            return new URL("", "", "");
        }
    }

    /**
     * Returns the cookie.
     *
     * @return The cookie.
     */
    public byte[] getCookie() {
        return cookie_.getByteArray();
    }

    /**
     * Returns the timestamp in milliseconds
     * since January 1, 1970, 00:00:00 GMT.
     *
     * @return The timestamp.
     */
    public long getTimestamp() {
        return timestamp_.getBigInteger().longValue();
    }

    /**
     * Returns the DER encoded entry.
     *
     * @return The encoded entry.
     * @exception IllegalStateException if an exception occures 
     *   during ASN1 encoding (RuntimeException).
     */
    public byte[] getEncoded() throws IllegalStateException {
        ByteArrayOutputStream bos;
        DEREncoder enc;
        byte[] b;
        bos = new ByteArrayOutputStream();
        enc = new DEREncoder(bos);
        try {
            encode(enc);
            b = bos.toByteArray();
            enc.close();
            return b;
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (ASN1Exception e) {
            throw new IllegalStateException("Caught " + e.toString());
        } catch (IOException e) {
            throw new IllegalStateException("Caught " + e.toString());
        }
    }

    /**
     * Returns the string representation of this entry.
     *
     * This is done in following manner
     * <blockquote><pre>
     * ASN1StructureName {
     * Property  : Value
     * }
     * </pre></blockquote>
     *
     * @return The string representation.
     */
    public String toString() {
        StringBuffer buf;
        String string;
        byte[] value;
        int i;
        buf = new StringBuffer();
        string = getClass().getName();
        string = string.substring(string.lastIndexOf('.') + 1);
        if (string.startsWith("ASN1")) {
            string = string.substring(4);
        }
        buf.append(string);
        buf.append(" {\n");
        buf.append("ImplicitName    : ");
        value = implicitName_.getByteArray();
        for (i = 0; i < value.length; i++) {
            buf.append(Character.forDigit((new Byte(value[i]).intValue() & 0xff) >> 4, 16));
            buf.append(Character.forDigit(new Byte(value[i]).intValue() & 0x0f, 16));
            buf.append(" ");
        }
        buf.append("\n");
        buf.append("ContactAddress  : ");
        buf.append(new String(contactAddress_.getByteArray()));
        buf.append("\n");
        buf.append("Cookie          : ");
        value = cookie_.getByteArray();
        for (i = 0; i < value.length; i++) {
            buf.append(Character.forDigit((new Byte(value[i]).intValue() & 0xff) >> 4, 16));
            buf.append(Character.forDigit(new Byte(value[i]).intValue() & 0x0f, 16));
            buf.append(" ");
        }
        buf.append("\n");
        buf.append("Timestamp       : ");
        buf.append(new Date(timestamp_.getBigInteger().longValue()));
        buf.append("\n");
        buf.append("}");
        return buf.toString();
    }
}

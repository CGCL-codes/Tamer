package de.fhg.igd.atlas.lsp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import codec.CorruptedCodeException;
import codec.asn1.ASN1Exception;
import codec.asn1.ASN1OctetString;
import codec.asn1.ASN1SetOf;
import codec.asn1.DERDecoder;
import codec.asn1.DEREncoder;
import de.fhg.igd.util.ByteArrayComparator;

/**
 * Represents a LSP_Refresh structure of the Location Service
 * Protocol (LSP).
 *
 * Encapsulated in a <code>LSPRequest</code> it is sent to the Location
 * Service Server ({@link de.fhg.igd.atlas.core.LSServer LSServer})
 * or the Location Service Proxy
 * ({@link de.fhg.igd.atlas.core.LSProxy LSProxy}) to refresh existing
 * entries specified by a list of implicit names. If successful, the
 * <code>LSPReply</code> message has the state <code>ACKNOWLEDGE</code>,
 * otherwise it transports a <code>LSPRefreshResult</code> structure. This
 * class is used by the Location Service Client
 * ({@link de.fhg.igd.atlas.core.LSClient LSClient}) to prevent,that the 
 * entries in <code>LSServer</code> and <code>LSProxy</code> expire after
 * a certain timeout.<p>
 *
 * The ASN.1 structure of this request is as follows
 * <blockquote><pre>
 * LSP_Refresh      ::= ImplicitNameList
 * ImplicitNameList ::= SET OF ImplicitName
 * ImplicitName     ::= OCTET STRING
 * </pre></blockquote>
 *
 * The implicit name is the unique identifier of an mobile object
 * for instance the hashcode of its static part.
 * 
 * @author Jan Peters
 * @version "$Id: LSPRefresh.java 1913 2007-08-08 02:41:53Z jpeters $"
 * @see LSPRefreshResult
 * @see LSPRequest
 * @see LSPReply
 */
public class LSPRefresh extends ASN1SetOf {

    /**
     * Creates a new request from the given encoded request.
     * 
     * @param code The encoded request.
     * @return The decoded request.
     
     * @exception CorruptedCodeException if the code is bad.
     * @exception IllegalStateException if an error occurs
     *   while decoding with <code>DERDecoder</code> (RuntimeException).
     * @exception NullPointerException if <code>code</code>
     *   is <code>null</code> (RuntimeException).
     */
    public static LSPRefresh createRequest(byte[] code) throws CorruptedCodeException, IllegalStateException, NullPointerException {
        ByteArrayInputStream bis;
        LSPRefresh request;
        DERDecoder dec;
        if (code == null) {
            throw new NullPointerException("Need the code of an request!");
        }
        bis = new ByteArrayInputStream(code);
        dec = new DERDecoder(bis);
        try {
            request = new LSPRefresh();
            request.decode(dec);
            dec.close();
            return request;
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
    public LSPRefresh() {
        super(ASN1OctetString.class);
    }

    /**
     * Creates an instance with the given set of 
     * implicit names.
     * 
     * @param implicitNames The set of implicit names
     *   of the type <code>byte[]</code>.
     */
    public LSPRefresh(Set implicitNames) {
        super(ASN1OctetString.class, implicitNames.size());
        Iterator it;
        for (it = implicitNames.iterator(); it.hasNext(); ) {
            add(new ASN1OctetString((byte[]) it.next()));
        }
    }

    /**
     * Adds a new implicit name to the existing
     * set of implicit names.
     * 
     * @param implicitName The implicit name.
     */
    public void add(byte[] implicitName) {
        super.add(new ASN1OctetString(implicitName));
    }

    /**
     * Returns the sorted set of implicit names.
     *
     * @return The sortet set of implicit names 
     *   of the type <code>byte[]</code>.
     */
    public SortedSet getImplicitNames() {
        Iterator it;
        TreeSet set;
        set = new TreeSet(new ByteArrayComparator());
        for (it = iterator(); it.hasNext(); ) {
            set.add(((ASN1OctetString) it.next()).getByteArray());
        }
        return set;
    }

    /**
     * Returns the DER encoded request.
     *
     * @return The encoded request.
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
     * Returns the string representation of this request.
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
        ASN1OctetString implicitName;
        StringBuffer buf;
        String string;
        byte[] value;
        Iterator it;
        int i;
        buf = new StringBuffer();
        string = getClass().getName();
        string = string.substring(string.lastIndexOf('.') + 1);
        if (string.startsWith("ASN1")) {
            string = string.substring(4);
        }
        buf.append(string);
        buf.append(" {\n");
        buf.append("Size: ");
        buf.append(size());
        buf.append("\n");
        for (it = iterator(); it.hasNext(); ) {
            implicitName = (ASN1OctetString) it.next();
            buf.append("> ImplicitName: ");
            value = implicitName.getByteArray();
            for (i = 0; i < value.length; i++) {
                buf.append(Character.forDigit((new Byte(value[i]).intValue() & 0xff) >> 4, 16));
                buf.append(Character.forDigit(new Byte(value[i]).intValue() & 0x0f, 16));
                buf.append(" ");
            }
            buf.append("\n");
        }
        buf.append("}");
        return buf.toString();
    }
}

package javaclient2.xdr;

import java.io.IOException;

/**
 * Instances of the class <code>XdrOpaque</code> represent (de-)serializeable
 * fixed-size opaque values, which are especially useful in cases where a result with only a
 * single opaque value is expected from a remote function call or only a single
 * opaque value parameter needs to be supplied.
 *
 * <p>Please note that this class is somewhat modelled after Java's primitive
 * data type wrappers. As for these classes, the XDR data type wrapper classes
 * follow the concept of values with no identity, so you are not allowed to
 * change the value after you've created a value object.
 *
 * @version $Revision: 1.1 $ $Date: 2006/02/20 22:44:57 $ $State: Exp $ $Locker:  $
 * @author Harald Albrecht
 */
public class XdrOpaque implements XdrAble {

    /**
     * Constructs and initializes a new <code>XdrOpaque</code> object.
     */
    public XdrOpaque(byte[] value) {
        this.value = value;
    }

    /**
     * Constructs and initializes a new <code>XdrOpaque</code> object given
     * only the size of the opaque value.
     *
     * @param length size of opaque value.
     */
    public XdrOpaque(int length) {
        this.value = new byte[length];
    }

    /**
     * Returns the value of this <code>XdrOpaque</code> object as a byte
     * vector.
     *
     * @return  The primitive <code>byte[]</code> value of this object.
     */
    public byte[] opaqueValue() {
        return this.value;
    }

    /**
     * Encodes -- that is: serializes -- a XDR opaque into a XDR stream in
     * compliance to RFC 1832.
     *
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public void xdrEncode(XdrEncodingStream xdr) throws OncRpcException, IOException {
        xdr.xdrEncodeOpaque(value);
    }

    /**
     * Decodes -- that is: deserializes -- a XDR opaque from a XDR stream in
     * compliance to RFC 1832.
     *
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public void xdrDecode(XdrDecodingStream xdr) throws OncRpcException, IOException {
        xdr.xdrDecodeOpaque(value);
    }

    /**
     * The encapsulated opaque value itself.
     */
    private byte[] value;
}

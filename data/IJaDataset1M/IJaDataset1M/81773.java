package osdb.redstone.xmlrpc.serializers;

import java.io.IOException;
import java.io.Writer;
import osdb.redstone.xmlrpc.XmlRpcCustomSerializer;
import osdb.redstone.xmlrpc.XmlRpcException;
import osdb.redstone.xmlrpc.XmlRpcSerializer;

/**
 *  Serializes primitiv longs. Note that unless setUseApacheExtension( true )
 *  has been invoked, the longs are demoted to integers before being serialized
 *  into regular XML-RPC &lt;i4>'s, possibly losing significant bits in the
 *  conversion.
 * 
 *  @author Greger Olsson
 */
public class LongPrimitiveSerializer implements XmlRpcCustomSerializer {

    public Class getSupportedClass() {
        return long.class;
    }

    /**
     *  Sets whether or not to use the &lt;i8> Apache extensions when
     *  serializing longs.
     *
     *  @param useApacheExtension Flag for specifying the Apache extension to be used.
     */
    public void setUseApacheExtension(boolean useApacheExtension) {
        this.useApacheExtension = useApacheExtension;
    }

    public void serialize(Object value, Writer writer, XmlRpcSerializer builtInSerializer) throws XmlRpcException, IOException {
        Long longValue = (Long) value;
        if (!useApacheExtension) {
            writer.write("<i4>");
            writer.write(Integer.toString(longValue.intValue()));
            writer.write("</i4>");
        } else {
            writer.write("<value><i8 xmlns=\"http://ws.apache.org/xmlrpc/namespaces/extensions\">");
            writer.write(Long.toString(longValue.longValue()));
            writer.write("</i8></value>");
        }
    }

    /** Flag indicating whether or not the Apache &lt;i8> extension should be used. */
    private boolean useApacheExtension;
}

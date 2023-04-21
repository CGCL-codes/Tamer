package osdb.redstone.xmlrpc.serializers;

import java.io.IOException;
import java.io.Writer;
import osdb.redstone.xmlrpc.XmlRpcCustomSerializer;
import osdb.redstone.xmlrpc.XmlRpcException;
import osdb.redstone.xmlrpc.XmlRpcSerializer;

/**
 *  Serializes arrays of primitive longs. Note that unless
 *  setUseApacheExtension( true ) has been invoked, the longs are demoted to
 *  integers before being serialized into regular XML-RPC &lt;i4>'s, possibly
 *  losing significant bits in the conversion.<p>
 * 
 *  @author Greger Olsson
 */
public class LongArraySerializer implements XmlRpcCustomSerializer {

    public Class getSupportedClass() {
        return long[].class;
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
        writer.write("<array><data>");
        long[] array = (long[]) value;
        for (int i = 0; i < array.length; ++i) {
            if (!useApacheExtension) {
                writer.write("<value><i4>");
                writer.write(Integer.toString((int) array[i]));
                writer.write("</i4></value>");
            } else {
                writer.write("<value><i8 xmlns=\"http://ws.apache.org/xmlrpc/namespaces/extensions\">");
                writer.write(Long.toString(array[i]));
                writer.write("</i8></value>");
            }
        }
        writer.write("</data></array>");
    }

    /** Flag indicating whether or not the Apache &lt;i8> extension should be used. */
    private boolean useApacheExtension;
}

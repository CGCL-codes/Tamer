package osdb.redstone.xmlrpc.serializers;

import java.io.IOException;
import java.io.Writer;
import osdb.redstone.xmlrpc.XmlRpcCustomSerializer;
import osdb.redstone.xmlrpc.XmlRpcException;
import osdb.redstone.xmlrpc.XmlRpcSerializer;

/**
 *  Serializes arrays of booleans.
 * 
 *  @author Greger Olsson
 */
public class BooleanArraySerializer implements XmlRpcCustomSerializer {

    public Class getSupportedClass() {
        return boolean[].class;
    }

    public void serialize(Object value, Writer writer, XmlRpcSerializer builtInSerializer) throws XmlRpcException, IOException {
        writer.write("<array><data>");
        boolean[] array = (boolean[]) value;
        for (int i = 0; i < array.length; ++i) {
            writer.write("<value><boolean>");
            writer.write(array[i] == true ? "1" : "0");
            writer.write("</boolean></value>");
        }
        writer.write("</data></array>");
    }
}

package osdb.redstone.xmlrpc.serializers.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import osdb.redstone.xmlrpc.XmlRpcCustomSerializer;
import osdb.redstone.xmlrpc.XmlRpcException;
import osdb.redstone.xmlrpc.XmlRpcSerializer;

/**
 *  Serializes java.util.Maps into JSON objects. For each value in the map
 *  it recursively calls the XmlRpcSerializer, which potentially ends up back
 *  in this class if a value in the Map is another Map. The key should be a String
 *  or something that properly implements toString().
 *
 *  @author Greger Olsson
 */
public class MapSerializer implements XmlRpcCustomSerializer {

    public Class getSupportedClass() {
        return Map.class;
    }

    public void serialize(Object value, Writer writer, XmlRpcSerializer builtInSerializer) throws XmlRpcException, IOException {
        writer.write('{');
        Map map = (Map) value;
        Set keys = map.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            Object key = it.next();
            writer.write('"');
            writer.write(key.toString());
            writer.write("\":");
            builtInSerializer.serialize(map.get(key), writer);
            if (it.hasNext()) {
                writer.write(',');
            }
        }
        writer.write('}');
    }
}

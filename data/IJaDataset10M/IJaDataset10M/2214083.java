package osdb.redstone.xmlrpc.serializers;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import osdb.redstone.xmlrpc.XmlRpcCustomSerializer;
import osdb.redstone.xmlrpc.XmlRpcException;
import osdb.redstone.xmlrpc.XmlRpcSerializer;

/**
 *  A custom serializer that support serialization of objects implementing
 *  the java.util.List interface. For ArrayLists, for instance, this may
 *  be more effective than the CollectionSerializer since this serializer
 *  uses random access rather that instantiating an iterator.
 *
 *  @author Greger Olsson
 */
public class ListSerializer implements XmlRpcCustomSerializer {

    public Class getSupportedClass() {
        return List.class;
    }

    public void serialize(Object value, Writer writer, XmlRpcSerializer builtInSerializer) throws XmlRpcException, IOException {
        writer.write("<array><data>");
        List list = (List) value;
        for (int i = 0; i < list.size(); ++i) {
            builtInSerializer.serialize(list.get(i), writer);
        }
        writer.write("</data></array>");
    }
}

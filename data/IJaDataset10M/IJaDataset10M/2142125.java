package flex.messaging.io;

import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Map;
import java.util.HashMap;

/**
 * Flex's ObjectProxy class allows an anonymous, dynamic ActionScript Object
 * to be bindable and report change events. Since ObjectProxy only wraps
 * the ActionScript Object type we can map the class to a java.util.HashMap on the
 * server, since the user would expect this type to be deserialized as a
 * java.util.HashMap as it is...
 *
 * @author Peter Farland
 */
public class ObjectProxy extends HashMap implements Externalizable {

    static final long serialVersionUID = 6978936573135117900L;

    public ObjectProxy() {
        super();
    }

    public ObjectProxy(int initialCapacity) {
        super(initialCapacity);
    }

    public ObjectProxy(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Object value = in.readObject();
        if (value instanceof Map) {
            putAll((Map) value);
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        Map map = new HashMap();
        map.putAll(this);
        out.writeObject(map);
    }
}

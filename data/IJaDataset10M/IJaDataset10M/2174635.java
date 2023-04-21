package configlib;

import java.util.ArrayList;

/**
 *
 * @author angr
 */
public class SerializableCompositeFactory implements SerializableFactory {

    ArrayList<SerializableFactory> childFactories = new ArrayList<SerializableFactory>();

    public synchronized boolean remove(Object o) {
        return childFactories.remove(o);
    }

    public synchronized boolean add(SerializableFactory e) {
        return childFactories.add(e);
    }

    public synchronized Serializable create(String type) {
        Serializable obj = null;
        for (SerializableFactory inOutableFactory : childFactories) {
            obj = inOutableFactory.create(type);
            if (obj != null) {
                return obj;
            }
        }
        return obj;
    }
}

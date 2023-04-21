package jaxlib.ee.jms.serial;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.jms.Destination;
import javax.jms.JMSException;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: SerialMap.java 2730 2009-04-21 01:12:29Z joerg_wassmer $
 */
final class SerialMap extends LinkedHashMap<String, Object> {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    SerialMap() {
        super();
    }

    SerialMap(final int initialCapacity) {
        super(initialCapacity);
    }

    /**
   * @serialData
   * @since JaXLib 1.0
   */
    private Object writeReplace() {
        return new SerialMap.WriteReplace(this);
    }

    private static final class WriteReplace extends Object implements Serializable {

        /**
     * @since JaXLib 1.0
     */
        private static final long serialVersionUID = 1L;

        private transient SerialMap map;

        WriteReplace(final SerialMap map) {
            super();
            this.map = map;
        }

        /**
     * @serialData
     * @since JaXLib 1.0
     */
        private void readObject(final ObjectInputStream in) throws ClassNotFoundException, IOException {
            in.defaultReadObject();
            final SerialMap map = new SerialMap(in.readInt());
            for (String key; (key = (String) in.readObject()) != null; ) map.put(key, in.readObject());
            this.map = map;
        }

        /**
     * @serialData
     * @since JaXLib 1.0
     */
        private Object readResolve() {
            return this.map;
        }

        /**
     * @serialData
     * @since JaXLib 1.0
     */
        private void writeObject(final ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            out.writeInt(this.map.size());
            try {
                for (final Map.Entry<String, Object> e : this.map.entrySet()) {
                    Object v = e.getValue();
                    if (v instanceof Destination) v = SerialDestination.marshal((Destination) v);
                    if (v instanceof Serializable) {
                        out.writeObject(e.getKey());
                        out.writeObject(v);
                    }
                }
                out.writeObject(null);
            } catch (final JMSException ex) {
                throw new IOException(ex);
            }
        }
    }
}

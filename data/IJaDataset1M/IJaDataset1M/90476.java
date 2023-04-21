package org.avis.federation.io.messages;

import java.util.Map;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolCodecException;
import org.avis.io.messages.Notify;
import org.avis.security.Keys;
import static org.avis.io.XdrCoding.getStringArray;
import static org.avis.io.XdrCoding.putStringArray;

public class FedNotify extends Notify {

    public static final int ID = 195;

    public String[] routing;

    public FedNotify() {
    }

    public FedNotify(Object... attributes) {
        super(attributes);
    }

    public FedNotify(Map<String, Object> attributes, boolean deliverInsecure, Keys keys, String[] routing) {
        super(attributes, deliverInsecure, keys);
        this.routing = routing;
    }

    public FedNotify(Notify original, String[] routing) {
        this(original.attributes, original.deliverInsecure, original.keys, routing);
    }

    public FedNotify(Notify original, Map<String, Object> attributes, Keys keys, String[] routing) {
        this(attributes, original.deliverInsecure, keys, routing);
    }

    @Override
    public int typeId() {
        return ID;
    }

    @Override
    public void decode(IoBuffer in) throws ProtocolCodecException {
        super.decode(in);
        routing = getStringArray(in);
    }

    @Override
    public void encode(IoBuffer out) throws ProtocolCodecException {
        super.encode(out);
        putStringArray(out, routing);
    }
}

package net.sf.ninjakore.packetdata;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractPacketData implements PacketData {

    private final short id;

    protected final Map<String, Object> fields;

    public AbstractPacketData(Map<String, Object> fields) {
        id = (fields.get("id") != null ? ((Number) fields.get("id")).shortValue() : 0);
        this.fields = new LinkedHashMap<String, Object>(fields);
    }

    @Override
    public Map<String, Object> getCopyOfFields() {
        return new LinkedHashMap<String, Object>(fields);
    }

    @Override
    public short getId() {
        return id;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Entry<String, Object> entry : fields.entrySet()) {
            if (entry.getValue() instanceof byte[]) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(Arrays.toString((byte[]) entry.getValue()));
            } else {
                sb.append(entry);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

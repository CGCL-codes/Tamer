package it.xargon.marshal;

import it.xargon.util.Bitwise;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class MarMap extends AbstractMarshaller {

    public MarMap() {
        super("MAP", Source.STREAM, Map.class);
    }

    public float getAffinity(Class<?> javaclass) {
        if (Map.class.isAssignableFrom(javaclass)) return 1f;
        return 0f;
    }

    @SuppressWarnings("unchecked")
    public void marshalToStream(Object obj, OutputStream out) throws IOException {
        Map<Object, Object> tmap = (Map<Object, Object>) obj;
        out.write(Bitwise.intToByteArray(tmap.size()));
        for (Map.Entry<Object, Object> e : tmap.entrySet()) dataBridge.marshal(e, false, out);
        out.flush();
    }

    @SuppressWarnings("unchecked")
    public Object unmarshalFromStream(InputStream in) throws IOException {
        int total = Bitwise.readInt(in);
        Map<Object, Object> result = new HashMap<Object, Object>();
        for (int cnt = 0; cnt < total; cnt++) {
            Map.Entry<Object, Object> e = (Entry<Object, Object>) dataBridge.unmarshal(in);
            result.put(e.getKey(), e.getValue());
        }
        return result;
    }
}

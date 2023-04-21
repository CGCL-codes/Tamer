package org.red5.server.net.rtmp.codec;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.BeanMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.io.object.Output;
import org.red5.io.object.Serializer;

/**
 * Data serializer for shared objects.
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Joachim Bauch (jojo@struktur.de)
 */
public class SharedObjectSerializer extends Serializer {

    protected static Log log = LogFactory.getLog(SharedObjectSerializer.class.getName());

    /**
	 * Writes a map to the output.
	 * 
	 * @param out
	 * 			output stream
	 * @param map
	 * 			Map object to serialize
	 */
    @Override
    public void writeMap(Output out, Map map) {
        if (log.isDebugEnabled()) {
            log.debug("writeMap");
        }
        final Set set = map.entrySet();
        out.writeStartObject(null);
        Iterator it = set.iterator();
        boolean isBeanMap = (map instanceof BeanMap);
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (isBeanMap && ((String) entry.getKey()).equals("class")) {
                continue;
            }
            out.writeItemKey(entry.getKey().toString());
            serialize(out, entry.getValue());
            if (it.hasNext()) {
                out.markPropertySeparator();
            }
        }
        out.markEndObject();
    }
}

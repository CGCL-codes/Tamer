package org.identifylife.descriptlet.store.oxm.jaxb;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.identifylife.descriptlet.store.model.Value;

/**
 * @author dbarnier
 *
 */
public class ValueMapAdapter extends XmlAdapter<ValueMap, Map<String, Value>> {

    @Override
    public ValueMap marshal(Map<String, Value> map) throws Exception {
        ValueMap result = new ValueMap();
        if (map != null && map.size() > 0) {
            for (String key : map.keySet()) {
                ValueMapEntry mapEntry = new ValueMapEntry();
                mapEntry.setKey(key);
                mapEntry.setValue(map.get(key));
                result.addEntry(mapEntry);
            }
        }
        return result;
    }

    @Override
    public Map<String, Value> unmarshal(ValueMap map) throws Exception {
        Map<String, Value> result = new HashMap<String, Value>();
        if (map != null && map.getEntries() != null) {
            for (ValueMapEntry entry : map.getEntries()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}

package org.identifylife.descriptlet.store.oxm.jaxb;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.identifylife.descriptlet.store.model.State;

/**
 * @author dbarnier
 *
 */
public class StateMapAdapter extends XmlAdapter<StateMap, Map<String, Set<State>>> {

    @Override
    public StateMap marshal(Map<String, Set<State>> map) throws Exception {
        StateMap result = new StateMap();
        for (String key : map.keySet()) {
            StateMapEntry mapEntry = new StateMapEntry();
            mapEntry.setKey(key);
            mapEntry.setStates(map.get(key));
            result.addEntry(mapEntry);
        }
        return result;
    }

    @Override
    public Map<String, Set<State>> unmarshal(StateMap map) throws Exception {
        Map<String, Set<State>> result = new HashMap<String, Set<State>>();
        for (StateMapEntry entry : map.getEntries()) {
            result.put(entry.getKey(), entry.getStates());
        }
        return result;
    }
}

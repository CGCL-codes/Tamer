package org.granite.config.flex;

import java.util.HashMap;
import java.util.Map;
import org.granite.util.XMap;

/**
 * @author Franck WOLFF
 */
public class Service {

    private final String id;

    private final String className;

    private final String messageTypes;

    private final Map<String, Adapter> adapters;

    private final Adapter defaultAdapter;

    private final Map<String, Destination> destinations;

    public Service(String id, String className, String messageTypes, Adapter defaultAdapter, Map<String, Adapter> adapters, Map<String, Destination> destinations) {
        this.id = id;
        this.className = className;
        this.messageTypes = messageTypes;
        this.defaultAdapter = defaultAdapter;
        this.adapters = adapters;
        this.destinations = destinations;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getMessageTypes() {
        return messageTypes;
    }

    public Destination findDestinationById(String id) {
        return destinations.get(id);
    }

    public Map<String, Destination> getDestinations() {
        return destinations;
    }

    public Adapter findAdapterById(String id) {
        return adapters.get(id);
    }

    public Adapter getDefaultAdapter() {
        return defaultAdapter;
    }

    public void addAdapter(Adapter adapter) {
        adapters.put(adapter.getId(), adapter);
    }

    public static Service forElement(XMap element) {
        String id = element.get("@id");
        String className = element.get("@class");
        String messageTypes = element.get("@messageTypes");
        Adapter defaultAdapter = null;
        Map<String, Adapter> adaptersMap = new HashMap<String, Adapter>();
        for (XMap adapter : element.getAll("adapters/adapter-definition")) {
            Adapter ad = Adapter.forElement(adapter);
            if (Boolean.TRUE.toString().equals(adapter.get("@default"))) defaultAdapter = ad;
            adaptersMap.put(ad.getId(), ad);
        }
        Map<String, Destination> destinations = new HashMap<String, Destination>();
        for (XMap destinationElt : element.getAll("destination")) {
            Destination destination = Destination.forElement(destinationElt, defaultAdapter, adaptersMap);
            destinations.put(destination.getId(), destination);
        }
        return new Service(id, className, messageTypes, defaultAdapter, adaptersMap, destinations);
    }
}

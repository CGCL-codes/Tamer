package org.granite.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.granite.util.JDOMUtil;
import org.jdom.Element;

public class Service {

    private String id;

    private String className;

    private String messageTypes;

    private Map<String, Destination> destinations = new HashMap<String, Destination>();

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

    @SuppressWarnings("unchecked")
    public static Service forElement(Element element) {
        JDOMUtil.checkElement(element, "service", "id", "class", "messageTypes");
        Service service = new Service();
        service.id = element.getAttributeValue("id");
        service.className = element.getAttributeValue("class");
        service.messageTypes = element.getAttributeValue("messageTypes");
        for (Element destination : (List<Element>) element.getChildren("destination")) {
            Destination dest = Destination.forElement(destination);
            service.destinations.put(dest.getId(), dest);
        }
        return service;
    }
}

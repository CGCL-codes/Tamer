package soapdust;

import java.util.LinkedHashMap;
import java.util.Map;

class WsdlElement {

    String namespace;

    Map<String, WsdlElement> children = new LinkedHashMap<String, WsdlElement>();

    WsdlElement(String namespace) {
        this.namespace = namespace;
    }
}

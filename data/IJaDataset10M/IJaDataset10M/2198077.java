package argo.jdom;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class JsonObject extends AbstractJsonObject {

    private final Map<JsonStringNode, JsonNode> fields;

    JsonObject(final Map<JsonStringNode, JsonNode> fields) {
        this.fields = Collections.unmodifiableMap(new HashMap<JsonStringNode, JsonNode>(fields));
    }

    @Override
    public Map<JsonStringNode, JsonNode> getFields() {
        return fields;
    }
}

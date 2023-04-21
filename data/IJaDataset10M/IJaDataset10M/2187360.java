package automenta.netention.client.data;

import java.util.Map;
import automenta.netention.client.property.Property;

/** pattern data, with all its defined properties PropertyData's included */
public class PatternDataEx extends PatternData {

    private Map<String, Property> definedPropertyData;

    public PatternDataEx() {
        super();
    }

    public PatternDataEx(String id, String name, String description, String[] ext, String[] properties, String[] definedProperties, Map<String, Property> definedPropertyData) {
        super(id, name, description, ext, properties, definedProperties);
        this.definedPropertyData = definedPropertyData;
    }

    public Map<String, Property> getDefinedPropertyData() {
        return definedPropertyData;
    }
}

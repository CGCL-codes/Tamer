package ru.milofon.praktikon.as01;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author kapych
 */
public class Structure {

    private Map definition;

    private Map data;

    protected Structure(String definition) {
        Yaml yaml = new Yaml();
        this.definition = (Map) yaml.load(definition);
        this.data = Structure.build(this.definition, new HashMap());
    }

    protected static Map build(Map definition, Map data) {
        for (String atomName : (Set<String>) definition.keySet()) {
            if (!atomName.equals("_config")) {
                Map atom = (Map) definition.get(atomName);
                Map atomConfig = (Map) atom.get("_config");
                if (atomConfig.get("datatype").equals("node")) {
                    data.put(atomName, Structure.build(atom, new HashMap()));
                } else {
                    data.put(atomName, null);
                }
            }
        }
        return data;
    }

    public void set(String field, Object value) throws Exception {
        String[] parts = field.split(".");
        Map part = this.definition;
        for (int i = 0; i <= parts.length; i++) {
            if (i < parts.length) {
                if (part.get(parts[i]) == null) {
                    throw new Exception("Узел " + parts[i] + " в структуре СЕИ '" + ((Map) this.definition.get("_config")).get("name") + "' отсутствует");
                } else {
                    part = (Map) part.get(parts[i]);
                }
            } else {
                if (part.get(parts[i]) == null) {
                    throw new Exception("Поле " + parts[i] + " в структуре СЕИ '" + ((Map) this.definition.get("_config")).get("name") + "' отсутствует");
                } else {
                    part.put(parts[i], value);
                }
            }
        }
    }
}

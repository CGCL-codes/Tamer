package pl.edu.zut.wi.vsl.app.modules;

import java.io.Serializable;

/**
 * Parameter for the module.
 *
 * @author Michal Wegrzyn
 */
public class ModuleParameter implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -4863178801181714313L;

    private String value;

    private String description;

    private final String defaultValue;

    private final String name;

    public ModuleParameter(String name, String value, String description) {
        this.value = value;
        this.defaultValue = value;
        this.name = name;
        this.description = description;
    }

    ModuleParameter(ModuleParameter param) {
        this(param.getName(), param.getValue(), param.getDescription());
    }

    public void reset() {
        this.value = this.defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

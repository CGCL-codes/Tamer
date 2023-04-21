package backend.param.args.generic;

import backend.param.args.ArgumentDefinition;

/**
 * 
 * @author hindlem
 *
 */
public class StringArgumentDefinition implements ArgumentDefinition {

    private String name;

    private boolean required;

    private String defaultValue;

    private boolean canHaveMultipleInstances = false;

    private String description;

    /**
	 * 
	 * @param name the argument name
	 * @param required is this a required argument
	 * @param defaultValue the default value
	 * @deprecated
	 */
    @Deprecated
    public StringArgumentDefinition(String name, boolean required, String defaultValue) {
        this.name = name;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    /**
	 * 
	 * @param name the argument name
	 * @param description
	 * @param required is this a required argument
	 * @param defaultValue the default value
	 * @param multiple instances allowed
	 */
    public StringArgumentDefinition(String name, String description, boolean required, String defaultValue, boolean multipleInstancesAllowed) {
        this.name = name;
        this.description = description;
        this.required = required;
        this.defaultValue = defaultValue;
        this.canHaveMultipleInstances = multipleInstancesAllowed;
    }

    public String getName() {
        return name;
    }

    public Class getClassType() {
        return String.class;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public boolean isValidArgument(Object obj) {
        if (obj instanceof String) {
            if (((String) obj).length() > 0) {
                return true;
            } else if (((String) obj).equalsIgnoreCase(defaultValue)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRequiredArgument() {
        return required;
    }

    public boolean isAllowedMultipleInstances() {
        return canHaveMultipleInstances;
    }

    public void setCanHaveMultipleInstances(boolean canHaveMultipleInstances) {
        this.canHaveMultipleInstances = canHaveMultipleInstances;
    }

    public Object parseString(String argument) throws Exception {
        return argument;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

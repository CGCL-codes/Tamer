package fr.cnes.sitools.plugins.filters.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import fr.cnes.sitools.common.model.ExtensionParameter;

/**
 * Class for parameters of resources
 *
 * @author m.marseille (AKKA Technologies)
 */
@XStreamAlias("filterParameter")
public final class FilterParameter extends ExtensionParameter {

    /**
   * Type of the parameter
   */
    private FilterParameterType type;

    /**
   * Constructor
   */
    public FilterParameter() {
        super();
        this.type = FilterParameterType.PARAMETER_ATTACHMENT;
    }

    /**
   * Constructor
   * @param name the name of the parameter
   * @param description the description of the parameter
   * @param type the type of the parameter
   */
    public FilterParameter(String name, String description, FilterParameterType type) {
        super(name, description);
        this.type = type;
    }

    /**
   * Sets the value of type
   * @param type the type to set
   */
    public void setType(FilterParameterType type) {
        this.type = type;
    }

    /**
   * Gets the type value
   * @return the type
   */
    public FilterParameterType getType() {
        return type;
    }
}

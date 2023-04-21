package net.sourceforge.smartconversion.api.port;

import net.sourceforge.smartconversion.api.meta.ModelDefinition;
import net.sourceforge.smartconversion.api.meta.ModelDefinitionImpl;

/**
 * Reference abstract implementor of {@link Port}
 *
 * @author Ovidiu Dolha
 */
public abstract class AbstractPort implements Port {

    private static final long serialVersionUID = 1L;

    /** The model definition of this port. */
    protected ModelDefinition modelDefinition;

    /** The alias/name of this port */
    protected String name;

    /** The usage type of this port */
    protected PortUsageType usageType;

    /**
   * {@inheritDoc}
   */
    @Override
    public ModelDefinition getModelDefinition() {
        return modelDefinition;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void setModelDefinition(ModelDefinition modelDefinition) {
        this.modelDefinition = modelDefinition;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public String getName() {
        return name;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public PortUsageType getUsageType() {
        return usageType;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void setUsageType(PortUsageType usageType) {
        this.usageType = usageType;
    }

    /**
   * Creates the right instance of {@link ModelDefinition}. By default, {@link ModelDefinitionImpl} 
   * is returned, but sub-classes can override it to return other types.
   * 
   * @return The created instance of {@link ModelDefinition}
   */
    public ModelDefinition createModelDefinition() {
        return new ModelDefinitionImpl();
    }
}

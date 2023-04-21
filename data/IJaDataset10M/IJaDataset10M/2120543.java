package net.sf.oval.configuration.pojo.elements;

import java.util.List;

/**
 * @author Sebastian Thomschke
 */
public class ConstructorConfiguration extends ConfigurationElement {

    private static final long serialVersionUID = 1L;

    public List<ParameterConfiguration> parameterConfigurations;

    public Boolean postCheckInvariants;
}

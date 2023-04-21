package net.sf.oval.configuration.pojo.elements;

import java.util.List;
import net.sf.oval.Check;

/**
 * @author Sebastian Thomschke
 */
public class MethodReturnValueConfiguration extends ConfigurationElement {

    private static final long serialVersionUID = 1L;

    /**
	 * checks for a method's return value that need to be verified after method execution
	 */
    public List<Check> checks;
}

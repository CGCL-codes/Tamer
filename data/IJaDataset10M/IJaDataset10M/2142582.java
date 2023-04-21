package net.sf.oval.configuration.pojo.elements;

import java.util.List;
import net.sf.oval.guard.PreCheck;

/**
 * @author Sebastian Thomschke
 */
public class MethodPreExecutionConfiguration extends ConfigurationElement {

    private static final long serialVersionUID = 1L;

    /**
	 * checks that need to be verified after method execution
	 */
    public List<PreCheck> checks;
}

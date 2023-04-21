package org.arastreju.core.terminology.flection.common;

import java.util.Set;

/**
 * Definition of a suffix for a string.
 * 
 * Created: 14.05.2008
 *
 * @author Oliver Tigges
 * 
 */
public interface SuffixDefinition {

    /**
	 * Get the possible bases of given postfixed string.
	 * @param in 
	 * @return
	 */
    Set<String> unPostfix(String in);

    /**
	 * Add the defined suffix to given base.
	 * @param base
	 * @return
	 */
    String postfix(String base);
}

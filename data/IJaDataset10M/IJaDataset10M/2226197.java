package org.ikayzo.iset.yautt.model;

import java.awt.Component;
import java.util.List;

/**
 * 
 * @author hanning ni
 * @reviewer (please review me)
 */
public interface ICodeHelperProvider {

    /**
	 * if you type ICodeHelperP, then ctrl-space, getComplete
	 * will be invoked and return a list of String that will
	 * make it complete. 
	 * @param input
	 * @return
	 */
    public List<String> getComplete(String input);

    /**
	 * if you type new Date().  this method will be invoked
	 * and a list of available method will show up.
	 * @param input
	 * @return
	 */
    public List<String> getSuggesion(String input);

    /**
	 * need a callback to helper system
	 * @param selection
	 */
    public void select(String selection);

    /**
	 * 
	 * @return all cached selections.
	 */
    public List<String> getCached();

    public List<Component> getUsedComponents();
}

package com.triplea.rolap.plugins;

/**
 * Such filter will be called after 'get'/'set' operation
 * @author kononyhin
 *
 */
public interface IPostprocessFilter extends IFilter {

    /**
	 * Implement tuple processing and return result one.
	 * @param tuple
	 * @return Tuple
	 */
    public Tuple postProcess(Tuple tuple);
}

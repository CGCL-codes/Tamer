package org.peaseplate.utils.cache;

/**
 * Any object which may be cached has to implement cachable
 * 
 * @author Manfred HANTSCHEL
 */
public interface Cachable {

    /**
	 * Estimates the weight (size) of the object
	 * 
	 * @return the weight (size) of the object
	 */
    public long estimateWeight();
}

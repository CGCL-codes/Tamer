package org.ccsoft.mc.core;

/**
 * 
 * @author chao cai
 *  the client implemented this interface, will be noticed when service is ready
 */
public interface ServiceChangeListener {

    /**
	 * The method is invoked when some service is ready
	 * @param bundle the component is ready
	 */
    public void onServiceReady(ServiceBundle bundle);
}

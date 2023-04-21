package org.contextor.resource.context.node.web;

import java.util.Collection;

/**
 * 
 * 
 * 
 * @author Behrooz Nobakht [behrooz dot nobakht at gmail dot com]
 **/
public interface WebSiteNetworkContext extends WebSiteContext {

    /**
	 * 
	 */
    String DEFAULT_DEPTH_KEY = "web.site.network.default.depth";

    /**
	 * 
	 */
    int DEFAULT_DEPTH = 2;

    /**
	 * @return
	 */
    boolean isRoot();

    /**
	 * @return
	 */
    WebSiteNetworkContext getRoot();

    /**
	 * @return
	 */
    Collection<WebSiteContext> getNetworkContexts();

    /**
	 * @param contexts
	 */
    void addAllToNetwork(Collection<WebSiteContext> contexts);

    /**
	 * @param context
	 */
    void addToNetwork(WebSiteContext context);
}

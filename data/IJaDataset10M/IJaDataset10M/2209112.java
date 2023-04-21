package com.ewansilver.raindrop.demo.cache;

import java.util.Map;
import com.ewansilver.raindrop.nio.SocketContext;

/**
 * <p>
 * Abstract base class for the commands that alter the cache. The <code>execute</code> method
 * should be overridden to provide sub Command specific capabilities.
 * </p>
 * 
 * @author Ewan Silver
 * 
 */
public abstract class AbstractCacheCommand {

    private SocketContext context;

    /**
	 * Constructor.
	 * 
	 * @param aContext
	 *            the SocketContext associated with this request.
	 */
    public AbstractCacheCommand(SocketContext aContext) {
        context = aContext;
    }

    /**
	 * Gets the SocketContext associated with the request.
	 * 
	 * @return the SocketContext
	 */
    protected SocketContext getContext() {
        return context;
    }

    /**
	 * 
	 * @param aCache 
	 * @return
	 * @throws Exception
	 */
    public abstract AbstractCacheCommand execute(Map<String, Store> aCache) throws Exception;
}

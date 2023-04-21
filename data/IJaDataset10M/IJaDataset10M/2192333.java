package org.xsocket.connection.multiplexed;

import java.io.IOException;
import org.xsocket.connection.IDisconnectHandler;

/**
 * The connect handler will be called if a new pipeline has been disconnected.  
 * 
 * @author grro@xsocket.org
 */
public interface IPipelineDisconnectHandler extends IPipelineHandler {

    /**
	 * see {@link IDisconnectHandler}
	 */
    public boolean onDisconnect(INonBlockingPipeline pipeline) throws IOException;
}

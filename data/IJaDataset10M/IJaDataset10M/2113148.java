package org.xsocket.server;

import java.net.InetAddress;

/**
 * Context object of the handler. The context object will be set by using dependency injection. 
 * To do this the <code>Resource</code> annotation has to be used.<br>
 * 
 * E.g. 
 * <pre> 
 *  class MyHandler implements IDataHandler {
 *
 *     &#064Resource
 *     private IHandlerContext ctx;
 *     
 *     public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException {
 *         ...
 *         int srvPort = ctx.getLocalePort();
 *         ...
 *         
 *         return true;
 *     }
 *  }
 * </pre> 
 * 
 * @author grro@xsocket.org
 */
public interface IHandlerContext {

    /**
	 * get the locale server port 
	 * 
	 * @return the locale server port
	 */
    public int getLocalePort();

    /**
	 * get the locale server address
	 * 
	 * @return the locale server address
	 */
    public InetAddress getLocaleAddress();

    /**
	 * get the domain name 
	 * 
	 * @return the domain name
	 */
    public String getDomainname();
}

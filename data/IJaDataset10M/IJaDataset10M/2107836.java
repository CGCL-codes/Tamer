package com.beanstalktech.servlet.command;

import com.beanstalktech.common.context.Application;
import com.beanstalktech.common.context.AppEvent;
import com.beanstalktech.servlet.template.Template;
import com.beanstalktech.common.utility.Logger;

/**
 * Base class for all commands. Commands are the initiators
 * of work and the implementors of business logic that is
 * independent of client or server implementations (i.e.
 * protocol-neutral). The default processing behavior
 * is to run on the caller's thread (typically the client
 * connection).
 * <P>
 * Commands are instantiated by the Command Manager in response
 * to specific events. 
 * <P>	           
 * To implement a command that handles a specific event,
 * create a concrete sub-class that includes a method of
 * the type:
 * <PRE>	           
 * 	public void handle_XXXXXX(AppEvent event)
 * 	{
 *      try
 *      {
 *          initialize(event);
 * 	        (handling logic)
 *      }
 *      catch(Exception e)
 *      {
 *          (error handling logic)
 *      }
 * 	}
 * </PRE>
 * ... where "XXXXXX" is an identifier for an application
 * event to be handled and "handling logic" is logic that
 * responds to the event. Typically, this logic would
 * examine the session context, invoke assemblers for
 * back-end server requests, and invoke assemblers for 
 * client responses.
 * <P>
 * Commands that make calls to back-end servers should
 * implement ListenerCommand, which is a sub-class of
 * this class. Commands which perform background tasks
 * (i.e. processing after the client connection is closed)
 * should implement ThreadCommand, a sub-class of 
 * ListenerCommand.
 * <P>
 * Concrete extensions of this classe must be registered 
 * with the Command Manager so that handle methods can be
 * discovered at runtime.
 * 
 * @author Stuart Sugarman
 * @version 1.0 12/5/1999
 * @since Beanstalk V1.0
 * @see com.beanstalktech.common.command.ListenerCommand
 * @see com.beanstalktech.common.command.ThreadCommand
 */
public abstract class Command extends Template {

    public static void handleError(AppEvent evt, int errorCode) {
        evt.setStatus(errorCode, "Unable to handle request");
        evt.getApplication().getErrorHandlerManager().handleError(evt);
    }
}

package org.jactr.tools.async.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.demux.ExceptionHandler;
import org.jactr.core.runtime.controller.IController;
import org.jactr.core.runtime.controller.debug.IDebugController;
import org.jactr.tools.async.common.BaseIOHandler;
import org.jactr.tools.async.controller.handlers.BreakpointHandler;
import org.jactr.tools.async.controller.handlers.LoginHandler;
import org.jactr.tools.async.controller.handlers.LogoutHandler;
import org.jactr.tools.async.controller.handlers.ModelStateHandler;
import org.jactr.tools.async.controller.handlers.ProductionHandler;
import org.jactr.tools.async.controller.handlers.RuntimeStateHandler;
import org.jactr.tools.async.message.command.breakpoint.IBreakpointCommand;
import org.jactr.tools.async.message.command.breakpoint.IProductionCommand;
import org.jactr.tools.async.message.command.login.LoginCommand;
import org.jactr.tools.async.message.command.login.LogoutCommand;
import org.jactr.tools.async.message.command.state.IModelStateCommand;
import org.jactr.tools.async.message.command.state.IRuntimeStateCommand;
import org.jactr.tools.async.message.event.login.LoginAcknowledgedMessage;

/**
 * @author developer
 */
public class RemoteIOHandler extends BaseIOHandler {

    /**
   * logger definition
   */
    private static final Log LOGGER = LogFactory.getLog(RemoteIOHandler.class);

    public static final String CREDENTIALS = "jactr.credentials";

    private IoSession _ownerSession;

    private boolean _allowListeners = false;

    private IController _controller;

    /**
   * 
   */
    public RemoteIOHandler(IController controller) {
        _controller = controller;
        addReceivedMessageHandler(LoginCommand.class, new LoginHandler(this));
        addReceivedMessageHandler(IRuntimeStateCommand.class, new RuntimeStateHandler(this));
        addReceivedMessageHandler(IModelStateCommand.class, new ModelStateHandler(this));
        addReceivedMessageHandler(LogoutCommand.class, new LogoutHandler());
        if (_controller instanceof IDebugController) {
            addReceivedMessageHandler(IBreakpointCommand.class, new BreakpointHandler(this));
            addReceivedMessageHandler(IProductionCommand.class, new ProductionHandler(this));
        }
        addExceptionHandler(Throwable.class, new ExceptionHandler<Throwable>() {

            public void exceptionCaught(IoSession session, Throwable exception) throws Exception {
                LOGGER.error("Exception caught from session " + session + ", closing. ", exception);
                session.close();
            }
        });
    }

    /**
   * return the controller, we require the session so we can ensure no errant
   * handlers access the controller
   */
    public IController getController(IoSession session) {
        allowsCommands(session);
        return _controller;
    }

    public final synchronized boolean isOwner(IoSession session) {
        boolean couldBeOwner = getCredentials().equals(session.getAttribute(CREDENTIALS));
        if (couldBeOwner) {
            if (_ownerSession == null) {
                _ownerSession = session;
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Acknowledging login");
                session.write(new LoginAcknowledgedMessage(true, "You are the owner of this runtime"));
            }
            if (session != _ownerSession) {
                String msg = "Another session with the same credentials owns this runtime";
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Rejecting login : " + msg);
                session.write(new LoginAcknowledgedMessage(false, msg));
                throw new SecurityException(msg);
            }
        } else if (LOGGER.isDebugEnabled()) LOGGER.debug(getCredentials() + " do not match those of session " + session.getAttribute(CREDENTIALS));
        return couldBeOwner;
    }

    public final void allowsCommands(IoSession session) {
        if (!isOwner(session)) {
            String message = session + " is not allowed to send commands, disconnecting ";
            SecurityException e = new SecurityException(message);
            if (LOGGER.isWarnEnabled()) LOGGER.warn(message, e);
            throw e;
        }
    }

    public final void allowsListeners(IoSession session) {
        if (!isOwner(session) && !_allowListeners) {
            String message = "Listening is not permitted by anyone other than owner, closing " + session;
            session.write(new LoginAcknowledgedMessage(false, message));
            if (LOGGER.isWarnEnabled()) LOGGER.warn(message);
            throw new SecurityException(message);
        }
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Accepted connection from " + session);
        _ownerSession = session;
        super.sessionOpened(session);
    }

    public IoSession getOwner() {
        return _ownerSession;
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        if (isOwner(session)) {
            _ownerSession = null;
            if (_controller.isRunning()) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("No master is connected, must permit uninterrupted run");
                if (_controller instanceof IDebugController) ((IDebugController) _controller).clearBreakpoints();
                if (_controller.isSuspended()) _controller.resume();
            }
        }
    }
}

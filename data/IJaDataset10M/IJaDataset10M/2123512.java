package com.tech99.example.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.customware.gwt.dispatch.client.standard.StandardDispatchService;
import net.customware.gwt.dispatch.server.Dispatch;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;
import net.customware.gwt.dispatch.shared.Result;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DispatchServlet extends RemoteServiceServlet implements StandardDispatchService {

    private final Dispatch dispatch;

    private final Logger logger = Logger.getLogger("Dispatcher");

    @Inject
    public DispatchServlet(Dispatch dispatch) {
        this.dispatch = dispatch;
    }

    public Result execute(Action<?> action) throws DispatchException {
        logger.info("Executing action " + action.toString());
        try {
            return dispatch.execute(action);
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Exception while executing " + action.toString(), e);
            throw new ActionException(e.getMessage());
        }
    }

    @Override
    protected void doUnexpectedFailure(Throwable e) {
        logger.log(Level.SEVERE, "doUnexpectedFailure", e);
        super.doUnexpectedFailure(e);
    }
}

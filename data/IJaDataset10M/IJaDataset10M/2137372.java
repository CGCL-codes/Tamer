package xbird.engine.sched;

import java.rmi.RemoteException;
import xbird.engine.RequestContext;
import xbird.engine.ResponseListener;
import xbird.engine.Request.Signature;

/**
 * Listener interface for the executors of scheduling tasks.
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public interface ScheduledEventListener {

    public Signature associatedWith();

    public ResponseListener getResponseHandler();

    public void fire(RequestContext rc) throws RemoteException;

    public void cancel(RequestContext rc) throws RemoteException;
}

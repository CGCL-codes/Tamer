package xbird.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import xbird.server.ServerConstants.ReturnType;
import xbird.server.sched.AbstractScheduler;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public interface RequestManager extends Serializable {

    public int getNumberOfQueryProcessors();

    public AbstractScheduler getScheduler();

    public <T extends Serializable> T execute(Request request, ReturnType returnType) throws RemoteException;
}

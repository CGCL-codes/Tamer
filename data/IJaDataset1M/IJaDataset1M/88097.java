package xbird.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import xbird.config.Settings;
import xbird.server.ServerConstants.ReturnType;
import xbird.util.PrintUtils;
import xbird.util.net.NetUtils;
import xbird.xquery.XQueryException;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public class XQEngineServer extends RemoteServerBase implements XQEngine {

    private static final int _localRegistryPort = Integer.parseInt(Settings.get("xbird.rmi.registry.local.port"));

    private static final String _localRegistryUrl = "//" + NetUtils.getLocalHostName() + ":" + _localRegistryPort + '/' + Settings.get("xbird.rmi.serv.name");

    private static final int _servPort = Integer.parseInt(Settings.get("xbird.rmi.serv.port", "0"));

    private RequestManager _requestManager = null;

    public XQEngineServer() {
        super(_localRegistryUrl, _servPort);
    }

    @Override
    protected void run(String[] args) throws XQueryException {
        super.run(args);
        this._requestManager = new LocalRequestManager(Settings.getProperties());
    }

    @Override
    protected void start() throws ServerSideException {
        try {
            Registry registry = prepareRegistry(_localRegistryPort);
            assert (registry != null);
        } catch (RemoteException e) {
            throw new ServerSideException("failed to launch registry on port " + _localRegistryPort, e);
        }
        super.start();
    }

    private static Registry prepareRegistry(final int port) throws RemoteException {
        try {
            return LocateRegistry.getRegistry(port);
        } catch (RemoteException e) {
            return LocateRegistry.createRegistry(port);
        }
    }

    public Object execute(Request request) throws RemoteException {
        return execute(request, ReturnType.Sequence);
    }

    public Object execute(Request request, ReturnType returnType) throws RemoteException {
        assert (_requestManager != null);
        return _requestManager.execute(request, returnType);
    }

    public static void main(String[] args) {
        try {
            new XQEngineServer().run(args);
        } catch (Throwable e) {
            PrintUtils.prettyPrintStackTrace(e, System.err);
            System.exit(1);
        }
    }
}

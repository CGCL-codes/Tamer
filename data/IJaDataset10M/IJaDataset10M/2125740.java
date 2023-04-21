package us.gibb.dev.gwt.server.appengine.inject;

import javax.servlet.http.HttpServletRequest;
import net.sf.gilead.adapter4appengine.EngineRemoteService;
import us.gibb.dev.gwt.server.appengine.remote.AppengineProcessCallHandler;
import us.gibb.dev.gwt.server.remote.RemoteServiceAdapter;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class AppengineRemoteServiceServlet extends EngineRemoteService implements RemoteServiceAdapter {

    private static final long serialVersionUID = 68052617558196206L;

    @Inject
    private Injector injector;

    @Inject
    private AppengineProcessCallHandler processCallHandler;

    @Override
    public String processCall(String payload) throws SerializationException {
        return processCallHandler.processCall(payload, this);
    }

    @Override
    public void onAfterRequestDeserialized(RPCRequest rpcRequest) {
        super.onAfterRequestDeserialized(rpcRequest);
    }

    @Override
    public RemoteService getRemoteService(Class<?> serviceClass) {
        return (RemoteService) injector.getInstance(serviceClass);
    }

    @Override
    public HttpServletRequest getRequest() {
        return getThreadLocalRequest();
    }
}

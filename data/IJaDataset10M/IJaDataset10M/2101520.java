package pl.xperios.rdk.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import pl.xperios.rdk.client.common.Subject;
import pl.xperios.rdk.shared.exceptions.ApplicationException;

@RemoteServiceRelativePath("GWT.rpc")
public interface RdkRpcService extends RemoteService {

    public Subject loginSubject(String login, String password) throws ApplicationException;

    public Subject getCurrentSubject();
}

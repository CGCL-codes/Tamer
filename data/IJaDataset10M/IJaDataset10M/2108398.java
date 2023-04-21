package pl.xperios.rdk.client.rpcservices;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import pl.xperios.rdk.client.commons.GenericRpcService;
import pl.xperios.rdk.shared.beans.RoleGroup;

@RemoteServiceRelativePath("GWT.rpc")
public interface RoleGroupRpcService extends GenericRpcService<RoleGroup> {
}

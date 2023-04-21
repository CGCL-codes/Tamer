package pl.xperios.rdk.server.rpc;

import com.google.inject.Inject;
import pl.xperios.rdk.client.rpc.Roles4UsersActualRpcService;
import pl.xperios.rdk.shared.dtos.Roles4UsersActual;
import pl.xperios.rdk.server.service.Roles4UsersActualService;
import pl.xperios.rdk.server.common.AbstractGetRpcService;

public class Roles4UsersActualRpcServiceImpl extends AbstractGetRpcService<Roles4UsersActual> implements Roles4UsersActualRpcService {

    @Inject
    public void setService(Roles4UsersActualService service) {
        bindService(service);
    }
}

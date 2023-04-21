package org.bsplus.rpc;

import java.util.List;
import org.bsplus.rpc.dto.UserDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../rpc/user")
public interface UserRPCService extends RemoteService {

    List<UserDTO> getAllUsers();
}

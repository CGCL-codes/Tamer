package com.turnengine.client.global.user.command.gwt;

import static com.javabi.common.dependency.DependencyFactory.getDependency;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteServiceServlet;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;
import com.javabi.command.executor.ICommandExecutorService;
import com.turnengine.client.global.user.bean.IUser;
import com.turnengine.client.global.user.command.GetUserById;

/**
 * The Get User By Id Servlet.
 */
public class GetUserByIdServlet extends RemoteServiceServlet implements IGeneratedRemoteServiceServlet, GetUserByIdService {

    public IUser getUserById(long loginId, int id) throws GwtErrorCodeException {
        GetUserById command = new GetUserById(loginId, id);
        ICommandExecutorService service = getDependency(ICommandExecutorService.class);
        IExecutableCommandResponse<IUser> response = service.execute(command);
        if (response.hasErrors()) {
            throw new GwtErrorCodeException(response);
        }
        return response.getReturnValue();
    }
}

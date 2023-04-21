package com.turnengine.client.local.action.command.gwt;

import static com.javabi.common.dependency.DependencyFactory.getDependency;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteServiceServlet;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;
import com.javabi.command.executor.ICommandExecutorService;
import com.turnengine.client.local.action.command.StartActionAtPlayer;

/**
 * The Start Action At Player Servlet.
 */
public class StartActionAtPlayerServlet extends RemoteServiceServlet implements IGeneratedRemoteServiceServlet, StartActionAtPlayerService {

    public Boolean startActionAtPlayer(long loginId, int instanceId, int id, long amount) throws GwtErrorCodeException {
        StartActionAtPlayer command = new StartActionAtPlayer(loginId, instanceId, id, amount);
        ICommandExecutorService service = getDependency(ICommandExecutorService.class);
        IExecutableCommandResponse<Boolean> response = service.execute(command);
        if (response.hasErrors()) {
            throw new GwtErrorCodeException(response);
        }
        return response.getReturnValue();
    }
}

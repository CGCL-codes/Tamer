package com.turnengine.client.local.faction.command.gwt;

import static com.javabi.common.dependency.DependencyFactory.getDependency;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteServiceServlet;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;
import com.javabi.command.executor.ICommandExecutorService;
import com.turnengine.client.local.faction.bean.IFaction;
import com.turnengine.client.local.faction.command.GetFactions;
import java.util.List;

/**
 * The Get Factions Servlet.
 */
public class GetFactionsServlet extends RemoteServiceServlet implements IGeneratedRemoteServiceServlet, GetFactionsService {

    public List<IFaction> getFactions(long loginId, int instanceId) throws GwtErrorCodeException {
        GetFactions command = new GetFactions(loginId, instanceId);
        ICommandExecutorService service = getDependency(ICommandExecutorService.class);
        IExecutableCommandResponse<List<IFaction>> response = service.execute(command);
        if (response.hasErrors()) {
            throw new GwtErrorCodeException(response);
        }
        return response.getReturnValue();
    }
}

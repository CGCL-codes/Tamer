package com.turnengine.client.local.property.command.gwt;

import static com.javabi.common.dependency.DependencyFactory.getDependency;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteServiceServlet;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;
import com.javabi.command.executor.ICommandExecutorService;
import com.turnengine.client.local.property.command.GetLocalProperty;

/**
 * The Get Local Property Servlet.
 */
public class GetLocalPropertyServlet extends RemoteServiceServlet implements IGeneratedRemoteServiceServlet, GetLocalPropertyService {

    public String getLocalProperty(long loginId, int instanceId, String key) throws GwtErrorCodeException {
        GetLocalProperty command = new GetLocalProperty(loginId, instanceId, key);
        ICommandExecutorService service = getDependency(ICommandExecutorService.class);
        IExecutableCommandResponse<String> response = service.execute(command);
        if (response.hasErrors()) {
            throw new GwtErrorCodeException(response);
        }
        return response.getReturnValue();
    }
}

package org.nicocube.airain.game.client.rpc;

import java.util.Set;
import org.nicocube.airain.domain.client.action.Action;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("as.rpc")
public interface ActionService extends RemoteService {

    public Set<Action> retrieveAction(Long gcOrder);

    public Set<Action> saveAction(Set<Action> action);
}

package org.lopatka.idonc.web.page.detachablemodel;

import org.apache.wicket.model.LoadableDetachableModel;
import org.lopatka.idonc.model.user.IdoncUser;
import org.lopatka.idonc.service.IdoncService;
import org.lopatka.idonc.web.IdoncSession;

public class DetachableIdoncUserModel extends LoadableDetachableModel {

    private static final long serialVersionUID = 1L;

    private final long id;

    private final IdoncService service;

    private IdoncSession session = IdoncSession.get();

    public DetachableIdoncUserModel(IdoncUser user, IdoncService service) {
        super(user);
        this.id = user.getId();
        this.service = service;
    }

    @Override
    protected IdoncUser load() {
        String username = session.getLoggedUserName();
        String sessionId = session.getSessionId();
        return service.loadUser(username, sessionId, id);
    }
}

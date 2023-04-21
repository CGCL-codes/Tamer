package name.emu.webapp.kos.wicket.model.domain;

import name.emu.webapp.kos.dao.DaoRegistry;
import name.emu.webapp.kos.domain.DomainObject;
import name.emu.webapp.kos.wicket.Application;
import org.apache.wicket.model.LoadableDetachableModel;

public abstract class DomainObjectModel<T extends DomainObject> extends LoadableDetachableModel<T> {

    private long id;

    public DomainObjectModel(long id) {
        this.id = id;
    }

    public DomainObjectModel(T domainObject) {
        id = domainObject.getId();
    }

    protected DaoRegistry getDaoRegistry() {
        return (Application.get()).getServiceRegistry().getDaoRegistry();
    }

    protected long getId() {
        return id;
    }
}

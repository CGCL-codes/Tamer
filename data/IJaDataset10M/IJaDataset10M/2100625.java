package org.monet.manager.control.actions;

import org.monet.manager.core.constants.ErrorCode;
import org.monet.kernel.components.ComponentPersistence;
import org.monet.kernel.model.ServiceProviderList;

public class ActionDoLoadServiceLinkList extends Action {

    private ComponentPersistence componentPersistence;

    public ActionDoLoadServiceLinkList() {
        super();
        this.componentPersistence = ComponentPersistence.getInstance();
    }

    public String execute() {
        ServiceProviderList serviceLinkList = new ServiceProviderList();
        if (!this.kernel.isLogged()) {
            return ErrorCode.USER_NOT_LOGGED;
        }
        serviceLinkList = this.componentPersistence.loadServiceLinkList();
        return serviceLinkList.serializeToJSON();
    }
}

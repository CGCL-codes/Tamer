package org.blueoxygen.enclave.action.service;

import org.blueoxygen.enclave.entity.Item;
import org.blueoxygen.enclave.entity.Service;

/**
 * @author BeCeeL
 *
 */
public class Delete extends Form {

    public String execute() {
        if (getService().getId() != null || !"".equalsIgnoreCase(getService().getId())) {
            setService((Service) manager.getById(Service.class, getService().getId()));
            manager.remove(getService());
            setStatus("Deleted");
        }
        return SUCCESS;
    }
}

package org.blueoxygen.obat.service.actions;

import java.util.Iterator;
import org.blueoxygen.cimande.LogInformation;
import org.blueoxygen.obat.topic.Topic;

public class DeleteService extends ViewService {

    public String execute() {
        super.execute();
        logInfo = service.getLogInformation();
        logInfo.setActiveFlag(0);
        service.setLogInformation(logInfo);
        pm.save(service);
        return SUCCESS;
    }
}

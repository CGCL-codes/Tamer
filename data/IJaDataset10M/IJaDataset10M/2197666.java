package org.ourgrid.broker.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.ourgrid.broker.commands.CommandExecutor;
import org.ourgrid.broker.commands.SchedulerData;
import org.ourgrid.broker.dao.JobDAO;
import org.ourgrid.broker.scheduler.SchedulerIF;
import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.container.servicemanager.actions.RepeatedAction;

/**
 *
 */
public class SchedulerAction implements RepeatedAction {

    public SchedulerAction() {
    }

    public void run(Serializable handler, ServiceManager serviceManager) {
        Set<SchedulerIF> schedulers = serviceManager.getDAO(JobDAO.class).getSchedulers();
        List<SchedulerData> outputCommands = new ArrayList<SchedulerData>();
        for (SchedulerIF schedulerIF : schedulers) {
            outputCommands.addAll(schedulerIF.schedule());
        }
        CommandExecutor.getInstance().execute(outputCommands, serviceManager);
    }
}

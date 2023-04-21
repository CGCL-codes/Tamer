package de.iph.arbeitsgruppenassistent.client.reward.calculation;

import java.util.Date;
import de.iph.arbeitsgruppenassistent.server.mediator.session.MediatorAdminRemote;
import de.iph.arbeitsgruppenassistent.server.resource.entity.EmployeeEntity;
import de.iph.arbeitsgruppenassistent.server.task.session.TaskAdminRemote;

/**
 *
 *
 *
 */
public class EmployeeRewardCalculation extends AbstractRewardCalculation {

    TaskAdminRemote srvTask;

    int usedQualifikation, maxUsedQualifikation;

    public EmployeeRewardCalculation(TaskAdminRemote srvTask, MediatorAdminRemote srvMediator) {
        super(srvMediator);
        this.srvTask = srvTask;
    }

    /**
 * Eingesetzte Qualifikationen
 * @param emp
 * @param startDate
 * @param endDate
 * @return
 */
    public float deployedQualifikation(EmployeeEntity emp, Date startDate, Date endDate) {
        usedQualifikation = srvTask.getUsedQualificationCount(emp, startDate, endDate);
        maxUsedQualifikation = srvTask.getUsedMaximumQualificationCount(startDate, endDate);
        return (float) usedQualifikation / maxUsedQualifikation;
    }

    public int getUsedQualifikation() {
        return usedQualifikation;
    }

    public int getMaxUsedQualifikation() {
        return maxUsedQualifikation;
    }
}

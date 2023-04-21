package org.libreplan.web.planner.order;

import java.util.Date;
import java.util.List;
import org.libreplan.business.common.exceptions.ValidationException;
import org.libreplan.business.externalcompanies.daos.IExternalCompanyDAO;
import org.libreplan.business.externalcompanies.entities.ExternalCompany;
import org.libreplan.business.planner.daos.ISubcontractedTaskDataDAO;
import org.libreplan.business.planner.entities.SubcontractedTaskData;
import org.libreplan.business.planner.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.ganttz.data.GanttDate;

/**
 * Model for UI operations related with subcontract process and
 * {@link SubcontractedTaskData} entity.
 *
 * @author Manuel Rego Casasnovas <mrego@igalia.com>
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SubcontractModel implements ISubcontractModel {

    /**
     * Conversation state
     */
    private Task task;

    private org.zkoss.ganttz.data.Task ganttTask;

    private Date startDate;

    private Date endDate;

    private SubcontractedTaskData subcontractedTaskData;

    private SubcontractedTaskData currentSubcontractedTaskData;

    @Autowired
    private IExternalCompanyDAO externalCompanyDAO;

    @Autowired
    private ISubcontractedTaskDataDAO subcontractedTaskDataDAO;

    @Override
    @Transactional(readOnly = true)
    public void init(Task task, org.zkoss.ganttz.data.Task ganttTask) {
        this.task = task;
        this.startDate = task.getStartDate();
        this.endDate = task.getEndDate();
        this.ganttTask = ganttTask;
        SubcontractedTaskData subcontractedTaskData = task.getSubcontractedTaskData();
        this.currentSubcontractedTaskData = subcontractedTaskData;
        if (subcontractedTaskData == null) {
            this.subcontractedTaskData = SubcontractedTaskData.create(task);
        } else {
            this.subcontractedTaskData = SubcontractedTaskData.createFrom(subcontractedTaskData);
        }
    }

    @Override
    public SubcontractedTaskData getSubcontractedTaskData() {
        return subcontractedTaskData;
    }

    @Override
    @Transactional(readOnly = true)
    public void confirm() throws ValidationException {
        if (task != null) {
            if (subcontractedTaskData == null) {
                task.setSubcontractedTaskData(null);
            } else {
                subcontractedTaskDataDAO.save(subcontractedTaskData);
                if (currentSubcontractedTaskData == null) {
                    task.setSubcontractedTaskData(subcontractedTaskData);
                } else {
                    currentSubcontractedTaskData.applyChanges(subcontractedTaskData);
                }
                task.removeAllSatisfiedResourceAllocations();
                Task.convertOnStartInFixedDate(task);
            }
            recalculateTaskLength();
        }
    }

    private void recalculateTaskLength() {
        GanttDate previousStartDate = ganttTask.getBeginDate();
        GanttDate previousEnd = ganttTask.getEndDate();
        task.setStartDate(startDate);
        task.setEndDate(endDate);
        ganttTask.fireChangesForPreviousValues(previousStartDate, previousEnd);
    }

    @Override
    public void cancel() {
        task = null;
        startDate = null;
        endDate = null;
        currentSubcontractedTaskData = null;
        subcontractedTaskData = null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExternalCompany> getSubcontractorExternalCompanies() {
        return externalCompanyDAO.findSubcontractor();
    }

    @Override
    public void setExternalCompany(ExternalCompany externalCompany) {
        if (subcontractedTaskData != null) {
            subcontractedTaskData.setExternalCompany(externalCompany);
        }
    }

    @Override
    public boolean hasResourceAllocations() {
        if (task != null) {
            return !task.getSatisfiedResourceAllocations().isEmpty();
        }
        return false;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public void removeSubcontractedTaskData() {
        subcontractedTaskData = null;
    }
}

package net.sourceforge.solexatools.business.impl;

import java.util.SortedSet;
import net.sourceforge.solexatools.business.WorkflowParamService;
import net.sourceforge.solexatools.dao.WorkflowParamDAO;
import net.sourceforge.solexatools.model.WorkflowParam;
import net.sourceforge.solexatools.model.WorkflowParamValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorkflowParamServiceImpl implements WorkflowParamService {

    private WorkflowParamDAO workflowParamDAO = null;

    private static final Log log = LogFactory.getLog(WorkflowParamServiceImpl.class);

    public WorkflowParamServiceImpl() {
        super();
    }

    public void setWorkflowParamDAO(WorkflowParamDAO workflowParamDAO) {
        this.workflowParamDAO = workflowParamDAO;
    }

    public void insert(WorkflowParam workflowParam) {
        workflowParamDAO.insert(workflowParam);
    }

    public void update(WorkflowParam workflowParam) {
        workflowParamDAO.update(workflowParam);
    }

    public void delete(WorkflowParam workflowParam) {
        workflowParamDAO.delete(workflowParam);
    }

    public WorkflowParam findByID(Integer id) {
        WorkflowParam workflowParam = null;
        if (id != null) {
            try {
                workflowParam = workflowParamDAO.findByID(id);
            } catch (Exception exception) {
                log.error("Cannot find Lane by expID " + id);
                log.error(exception.getMessage());
            }
        }
        return workflowParam;
    }

    public WorkflowParamValue findValueByID(WorkflowParam workflowParam, Integer id) {
        WorkflowParamValue value = null;
        SortedSet<WorkflowParamValue> values = workflowParam.getValues();
        for (WorkflowParamValue workflowParamValue : values) {
            if (workflowParamValue != null && workflowParamValue.getWorkflowParamValueId().equals(id)) {
                value = workflowParamValue;
            }
        }
        return value;
    }
}

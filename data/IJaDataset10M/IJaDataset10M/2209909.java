package com.narirelays.ems.services;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import com.narirelays.ems.applogic.OperResult;
import com.narirelays.ems.applogic.ProcessModelerManagement;
import com.narirelays.ems.persistence.orm.BackwardModel;
import com.narirelays.ems.persistence.orm.ForwardModel;
import com.narirelays.ems.persistence.orm.Process;
import static com.narirelays.ems.resources.EMSi18n.*;

public class ProcessModelManagementService {

    private ProcessModelerManagement processModelerManagement;

    public ProcessModelerManagement getProcessModelerManagement() {
        return processModelerManagement;
    }

    public void setProcessModelerManagement(ProcessModelerManagement processModelerManagement) {
        this.processModelerManagement = processModelerManagement;
    }

    public OperResult appendProcessForwardModel(String process_id, Map properties) {
        return processModelerManagement.appendProcessForwardModel(process_id, properties);
    }

    public OperResult appendProcessBackwardModel(String process_id, Map properties) {
        return processModelerManagement.appendProcessBackwardModel(process_id, properties);
    }

    public OperResult modifyProcessTemplateForwardModel(String process_id, String pt_id) {
        return processModelerManagement.modifyProcessTemplateForwardModel(process_id, pt_id);
    }

    public OperResult modifyProcessTemplateBackwardModel(String process_id, String pt_id) {
        return processModelerManagement.modifyProcessTemplateBackwardModel(process_id, pt_id);
    }
}

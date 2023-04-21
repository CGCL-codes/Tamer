package com.techstar.dmis.service.workflow.impl;

import com.techstar.dmis.common.DispatchConstants;
import com.techstar.dmis.dao.IHandleHistoryDao;
import com.techstar.dmis.helper.dto.WorkflowHandleDto;
import com.techstar.dmis.service.workflow.IDdDoutageplanWFService;
import com.techstar.dmis.service.workflow.impl.exception.WorkflowException;
import com.techstar.framework.service.workflow.dao.IWorkflowDao;
import org.springframework.dao.DataAccessException;

public class DdDoutageplanWFServiceImpl implements IDdDoutageplanWFService {

    private IWorkflowDao workFlowDao;

    IHandleHistoryDao handleHistoryDao;

    public DdDoutageplanWFServiceImpl() {
    }

    public void setWorkFlowDao(IWorkflowDao workFlowDao) {
        this.workFlowDao = workFlowDao;
    }

    public void setHandleHistoryDao(IHandleHistoryDao handleHistoryDao) {
        this.handleHistoryDao = handleHistoryDao;
    }

    public int ddDayPlanCoursign(WorkflowHandleDto dto) throws DataAccessException {
        int ireturn = 0;
        try {
            String roles[] = { "" };
            if (dto.getNextTaskRoles() != null) roles = dto.getNextTaskRoles();
            String returnStr = workFlowDao.oneTicketNoForCountersign(Long.parseLong(dto.getTaskInstanceId()), dto.getRoleId(), Integer.parseInt(dto.getTransitionFlag()));
            if ("1".equals(returnStr)) {
                workFlowDao.endTask(Long.parseLong(dto.getTaskInstanceId()), DispatchConstants.DdDayPlan_WORKFLOW_AREA_COURTERSIGN_TRANSIT_YES, dto.getUserId(), roles);
                ireturn = 1;
            } else if ("-1".equals(returnStr)) {
                workFlowDao.endTask(Long.parseLong(dto.getTaskInstanceId()), DispatchConstants.DdDayPlan_WORKFLOW_AREA_COURTERSIGN_TRANSIT_NO, dto.getUserId(), roles);
                ireturn = -1;
            } else {
                ireturn = 0;
            }
            handleHistoryDao.saveOrUpdate(dto);
        } catch (Exception ex) {
            throw new WorkflowException();
        }
        return ireturn;
    }

    public int ddDayPlanCustomerApprove(WorkflowHandleDto dto) throws DataAccessException {
        try {
            workFlowDao.endTask(Long.parseLong(dto.getTaskInstanceId()), dto.getUserId(), dto.getNextTaskRoles());
            handleHistoryDao.saveOrUpdate(dto);
        } catch (Exception ex) {
            throw new WorkflowException();
        }
        return 1;
    }

    public int ddDayPlanFillExec(WorkflowHandleDto dto) throws DataAccessException {
        try {
            workFlowDao.endTask(Long.parseLong(dto.getTaskInstanceId()), dto.getUserId(), dto.getNextTaskRoles());
            handleHistoryDao.saveOrUpdate(dto);
        } catch (Exception ex) {
            throw new WorkflowException();
        }
        return 1;
    }

    public int ddDayPlanInfocustomer(WorkflowHandleDto dto) throws DataAccessException {
        try {
            workFlowDao.endTask(Long.parseLong(dto.getTaskInstanceId()), dto.getUserId(), dto.getNextTaskRoles());
            handleHistoryDao.saveOrUpdate(dto);
        } catch (Exception ex) {
            throw new WorkflowException();
        }
        return 1;
    }

    public int ddDayPlanSent(WorkflowHandleDto dto) throws DataAccessException {
        try {
            String transitionflag = dto.getTransitionFlag();
            if ("-1".equals(transitionflag)) workFlowDao.endTask(Long.parseLong(dto.getTaskInstanceId()), DispatchConstants.DdDayPlan_WORKFLOW_AREA_SENT_TRANSIT_THIS, dto.getUserId(), dto.getNextTaskRoles()); else if ("1".equals(transitionflag)) workFlowDao.endTask(Long.parseLong(dto.getTaskInstanceId()), DispatchConstants.DdDayPlan_WORKFLOW_AREA_SENT_TRANSIT_OTHER, dto.getUserId(), dto.getNextTaskRoles());
            handleHistoryDao.saveOrUpdate(dto);
        } catch (Exception ex) {
            throw new WorkflowException();
        }
        return 1;
    }

    public int ddDayPlanSpecArrange(WorkflowHandleDto dto) throws DataAccessException {
        try {
            String transitionflag = dto.getTransitionFlag();
            if ("2".equals(transitionflag)) workFlowDao.endTask(Long.parseLong(dto.getTaskInstanceId()), DispatchConstants.DdDayPlan_WORKFLOW_AREA_ARRANGE_TRANSIT_NEED, dto.getUserId(), dto.getNextTaskRoles()); else if ("1".equals(transitionflag)) workFlowDao.endTask(Long.parseLong(dto.getTaskInstanceId()), DispatchConstants.DdDayPlan_WORKFLOW_AREA_ARRANGE_TRANSIT_NO_NEED, dto.getUserId(), dto.getNextTaskRoles()); else if ("3".equals(transitionflag)) workFlowDao.endTask(Long.parseLong(dto.getTaskInstanceId()), DispatchConstants.DdDayPlan_WORKFLOW_AREA_ARRANGE_TRANSIT_REBACK, dto.getUserId(), dto.getNextTaskRoles());
            handleHistoryDao.saveOrUpdate(dto);
        } catch (Exception ex) {
            throw new WorkflowException();
        }
        return 1;
    }

    public int ddDayPlanSpecKeepon(WorkflowHandleDto dto) throws DataAccessException {
        try {
            workFlowDao.endTask(Long.parseLong(dto.getTaskInstanceId()), dto.getUserId(), dto.getNextTaskRoles());
            handleHistoryDao.saveOrUpdate(dto);
        } catch (Exception ex) {
            throw new WorkflowException();
        }
        return 1;
    }

    public int start(WorkflowHandleDto dto) throws DataAccessException {
        try {
            workFlowDao.start(DispatchConstants.DdDayPlan_WORKFLOW_AREA_PROCESSDIFINITION, dto.getBusId(), dto.getUserId(), dto.getNextTaskRoles());
            handleHistoryDao.saveOrUpdate(dto);
        } catch (Exception ex) {
            throw new WorkflowException();
        }
        return 1;
    }

    public int ddDayPlanEnd(WorkflowHandleDto dto) throws DataAccessException {
        try {
            workFlowDao.end(Long.parseLong(dto.getTaskInstanceId()));
        } catch (Exception ex) {
            throw new WorkflowException();
        }
        return 1;
    }
}

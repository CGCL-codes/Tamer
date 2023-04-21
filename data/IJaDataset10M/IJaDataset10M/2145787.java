package com.techstar.dmis.service.workflow;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import com.techstar.dmis.helper.dto.WorkflowHandleDto;

public interface IDdCutofftitleWFService {

    public int start(WorkflowHandleDto dto) throws DataAccessException;

    public int limitLineSeqFormHeaderApprove(WorkflowHandleDto dto) throws DataAccessException;

    public int limitLineSeqFormRelease(WorkflowHandleDto dto) throws DataAccessException;

    public int limitLineSeqFormModify(WorkflowHandleDto dto) throws DataAccessException;

    public int limitLineSeqFormSpecApprove(WorkflowHandleDto dto) throws DataAccessException;
}

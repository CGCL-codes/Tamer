package com.inet.qlcbcc.facade.support;

import static com.inet.qlcbcc.util.Utils.split;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.webos.core.message.ResponseMessage;
import org.webos.core.option.Option;
import org.webos.core.service.BusinessServiceException;
import com.inet.qlcbcc.domain.ServantsInfo;
import com.inet.qlcbcc.domain.WorkingProcess;
import com.inet.qlcbcc.dto.ErrorMessage;
import com.inet.qlcbcc.dto.ServantsDto;
import com.inet.qlcbcc.facade.AbstractFacade;
import com.inet.qlcbcc.facade.QualificationsModifiableException;
import com.inet.qlcbcc.facade.WorkingProcessFacade;
import com.inet.qlcbcc.facade.WorkingProcessModifiableException;
import com.inet.qlcbcc.internal.util.StringUtils;
import com.inet.qlcbcc.service.ServantsInfoService;
import com.inet.qlcbcc.service.WorkingProcessService;

/**
 * WorkingProcessFacadeSupport.
 *
 * @author Thoang Tran
 * @version $Id: WorkingProcessFacadeSupport.java Mar 12, 2012 9:17:58 PM thoangtd $
 *
 * @since 1.0
 */
@Component(value = "workingProcessFacade")
@Transactional(noRollbackFor = WorkingProcessModifiableException.class, readOnly = true, propagation = Propagation.SUPPORTS)
public class WorkingProcessFacadeSupport extends AbstractFacade implements WorkingProcessFacade {

    @Autowired(required = false)
    @Qualifier("workingProcessService")
    private WorkingProcessService workingProcessService;

    @Autowired(required = false)
    @Qualifier("servantsInfoService")
    private ServantsInfoService servantsInfoService;

    @Transactional(noRollbackFor = WorkingProcessModifiableException.class, isolation = Isolation.READ_COMMITTED, readOnly = false, propagation = Propagation.REQUIRED)
    public ResponseMessage save(ServantsDto servantsDto) {
        try {
            if (!StringUtils.hasLength(servantsDto.getServantsId())) {
                return createInvalidIdResponse();
            }
            Option<ServantsInfo> servantsInfo = servantsInfoService.findById(servantsDto.getServantsId());
            if (!servantsInfo.isDefined()) {
                return createFailureResponse("qlcbcc.working_process.save.not_exists_servants_info");
            }
            servantsDto.getWorkingProcess().setServantsInfo(servantsInfo.get());
            workingProcessService.save(servantsDto.getWorkingProcess());
            return createSuccessResponse(servantsDto.getWorkingProcess());
        } catch (BusinessServiceException bex) {
            throw new QualificationsModifiableException(bex.getMessage(), bex);
        }
    }

    /**
   * @see com.inet.qlcbcc.facade.WorkingProcessFacade#update(com.inet.qlcbcc.domain.WorkingProcess)
   */
    @Transactional(noRollbackFor = WorkingProcessModifiableException.class, isolation = Isolation.READ_COMMITTED, readOnly = false, propagation = Propagation.REQUIRED)
    public ResponseMessage update(WorkingProcess workingProcess) {
        try {
            if (!StringUtils.hasLength(workingProcess.getId())) {
                return createInvalidIdResponse();
            }
            Option<WorkingProcess> workingProcessOption = workingProcessService.findById(workingProcess.getId());
            if (!workingProcessOption.isDefined()) {
                return createFailureResponse("qlcbcc.working_process.not_exsts");
            }
            workingProcessOption.get().setDescription(workingProcess.getDescription());
            workingProcessOption.get().setFromDate(workingProcess.getFromDate());
            workingProcessOption.get().setToDate(workingProcess.getToDate());
            workingProcessOption.get().setVersion(workingProcess.getVersion());
            workingProcessOption.get().setWorkPlace(workingProcess.getWorkPlace());
            workingProcessOption.get().setWorkUnit(workingProcess.getWorkUnit());
            workingProcessService.update(workingProcessOption.get());
            return createSuccessResponse(workingProcessOption.get());
        } catch (BusinessServiceException bex) {
            throw new QualificationsModifiableException(bex.getMessage(), bex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
   * @see com.inet.qlcbcc.facade.WorkingProcessFacade#findById(java.lang.String)
   */
    public ResponseMessage findById(String id) {
        try {
            if (!StringUtils.hasLength(id)) {
                return createInvalidIdResponse();
            }
            Option<WorkingProcess> workingProcess = workingProcessService.findById(id);
            if (!workingProcess.isDefined()) {
                return createFailureResponse("qlcbcc.working_process.find_by_id.not_exists_ working_process");
            }
            return createSuccessResponse(workingProcess.get());
        } catch (BusinessServiceException bex) {
            return createFailureResponse("qlcbcc. qualifications.find_by_id", bex);
        }
    }

    /**
   * @see com.inet.qlcbcc.facade.WorkingProcessFacade#delete(java.lang.String)
   */
    @Transactional(noRollbackFor = WorkingProcessModifiableException.class, isolation = Isolation.READ_COMMITTED, readOnly = false, propagation = Propagation.REQUIRED)
    public ResponseMessage delete(String ids) {
        try {
            if (!StringUtils.hasLength(ids)) {
                return createInvalidIdResponse();
            }
            ErrorMessage errorMessage = workingProcessService.delete(split(ids, ids.length()));
            if (ErrorMessage.NULL.equals(errorMessage)) {
                return createSuccessResponse();
            } else {
                return createFailureResponse("qlcbcc.working_process.delete", errorMessage);
            }
        } catch (BusinessServiceException ex) {
            throw new QualificationsModifiableException(ex.getMessage(), ex);
        }
    }
}

package com.fisoft.phucsinh.phucsinhsrv.service.receivable;

import com.fisoft.phucsinh.phucsinhsrv.entity.CmBusinessCaseConfig;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityExistsException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityLockedException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityRemovedException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPInvalidEntityException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPUniqueConstraintViolationException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPUnrecoverableException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author vantinh
 */
@Local
public interface IBusinessCaseManager {

    /**
     * edit, add business case config
     * @param pCmBusinessCaseConfigListUpdate
     * @param pCmBusinessCaseConfigListAdd
     * @throws ERPInvalidEntityException
     * @throws ERPEntityExistsException
     * @throws ERPUniqueConstraintViolationException
     * @throws ERPUnrecoverableException
     * @throws ERPEntityRemovedException
     * @throws ERPEntityLockedException
     */
    void updateBusinessCaseConfig(List<CmBusinessCaseConfig> pCmBusinessCaseConfigListUpdate, List<CmBusinessCaseConfig> pCmBusinessCaseConfigListAdd) throws ERPInvalidEntityException, ERPEntityExistsException, ERPUniqueConstraintViolationException, ERPUnrecoverableException, ERPEntityRemovedException, ERPEntityLockedException;
}

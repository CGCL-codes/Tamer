package org.plazmaforge.bsolution.finance.common.services;

import org.plazmaforge.bsolution.finance.common.beans.LedgerAccount;
import org.plazmaforge.framework.core.exception.DAOException;
import org.plazmaforge.framework.service.EntityService;

/**
 * 
 * @author Oleh Hapon
 *
 * $Id: LedgerAccountService.java,v 1.3 2010/12/05 07:55:56 ohapon Exp $
 */
public interface LedgerAccountService extends EntityService<LedgerAccount, Integer> {

    public LedgerAccount findByCode(final String code) throws DAOException;
}

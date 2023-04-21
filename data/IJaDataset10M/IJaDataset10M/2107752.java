package org.plazmaforge.bsolution.finance.server.services;

import org.plazmaforge.bsolution.document.server.services.AbstractDocumentService;
import org.plazmaforge.bsolution.finance.common.beans.Contract;
import org.plazmaforge.bsolution.finance.common.services.ContractService;

/**
 * @author hapon
 *
 */
public class ContractServiceImpl extends AbstractDocumentService<Contract, Integer> implements ContractService {

    protected Class getEntityClass() {
        return Contract.class;
    }
}

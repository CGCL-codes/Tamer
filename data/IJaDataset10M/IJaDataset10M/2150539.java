package org.slasoi.businessManager.billingEngine.dao;

import org.slasoi.businessManager.common.dao.AbstractHibernateDAO;
import org.slasoi.businessManager.common.model.billing.EmPartyBank;

public interface PartyBankDAO extends AbstractHibernateDAO<EmPartyBank, Long> {

    /**
     * Get the account the one party by its id.
     * @param party
     * @return
     */
    public EmPartyBank getPartyBankActiveByParty(Long party) throws Exception;
}

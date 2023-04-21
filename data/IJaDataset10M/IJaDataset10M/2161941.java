package com.csc.at.services.health.simplebank.dao;

import com.csc.at.services.health.simplebank.model.Account;

/**
 *
 * @author werwuifi
 * 
 */
public interface AccountDAO extends AbstractEntityDAO<Account> {

    /**
	 * 
	 * @param accountId
	 * @return
	 */
    public Account findByAccountId(Long accountId);
}

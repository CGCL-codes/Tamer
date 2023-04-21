package org.helianto.partner.service;

import java.util.List;
import javax.annotation.Resource;
import org.helianto.core.filter.Filter;
import org.helianto.core.repository.FilterDao;
import org.helianto.partner.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Default account service interface implementation.
 * 
 * @author Mauricio Fernandes de Castro
 */
@Service("accountMgr")
public class AccountMgrImpl implements AccountMgr {

    public List<Account> findAccounts(Filter accountFilter) {
        List<Account> accountList = (List<Account>) accountDao.find(accountFilter);
        if (logger.isDebugEnabled() && accountList != null) {
            logger.debug("Found account list of size {}", accountList.size());
        }
        return accountList;
    }

    public Account storeAccount(Account account) {
        return accountDao.merge(account);
    }

    public void removeAccount(Account account) {
        accountDao.remove(account);
    }

    private FilterDao<Account> accountDao;

    @Resource(name = "accountDao")
    public void setAccountDao(FilterDao<Account> accountDao) {
        this.accountDao = accountDao;
    }

    private Logger logger = LoggerFactory.getLogger(AccountMgrImpl.class);
}

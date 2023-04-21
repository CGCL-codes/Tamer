package com.windsor.node.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.windsor.node.common.domain.Activity;
import com.windsor.node.common.domain.ActivityType;
import com.windsor.node.common.domain.NodeVisit;
import com.windsor.node.common.domain.SystemRoleType;
import com.windsor.node.common.domain.UserAccount;
import com.windsor.node.common.exception.WinNodeException;
import com.windsor.node.conf.NAASConfig;
import com.windsor.node.conf.NOSConfig;
import com.windsor.node.data.dao.AccountDao;
import com.windsor.node.data.dao.ActivityDao;

public class BaseService {

    /** Logger for this class and subclasses */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserAccount adminAccount;

    private ActivityDao activityDao;

    private NAASConfig naasConfig;

    private AccountDao accountDao;

    private NOSConfig nosConfig;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void afterPropertiesSet() {
        if (activityDao == null) {
            throw new RuntimeException("ActivityDao Not Set");
        }
        if (naasConfig == null) {
            throw new RuntimeException("NaasConfig Not Set");
        }
        if (accountDao == null) {
            throw new RuntimeException("AccountDao Not Set");
        }
        if (nosConfig == null) {
            throw new RuntimeException("NosConfig Not Set");
        }
    }

    protected void validateByRole(UserAccount account, SystemRoleType minimalRole) {
        if (account == null || StringUtils.isBlank(account.getId())) {
            throw new IllegalArgumentException("UserAccount not provided.");
        }
        if (minimalRole == null) {
            throw new IllegalArgumentException("SystemRoleType not provided.");
        }
        logger.debug("User Role : " + account.getRole());
        logger.debug("Required  : " + minimalRole);
        if (!account.getRole().equals(SystemRoleType.Admin)) {
            logger.debug("User not an admin  : " + account.getRole().name());
            if (minimalRole.equals(SystemRoleType.Admin)) {
                throw new WinNodeException("Admin permissions required to perform requested operation");
            } else if (minimalRole.equals(SystemRoleType.Program) && !account.getRole().equals(SystemRoleType.Program)) {
                throw new WinNodeException("User has insufficient privileges to use Node Admin");
            } else {
                logger.debug("User visit good to go");
            }
        }
    }

    /**
     * Used from inheriting classes to provide a default user account
     * 
     * @return
     */
    protected NodeVisit getAdminVisit() {
        return new NodeVisit(getAdminAccount(), nosConfig.getLocalhostIp());
    }

    protected UserAccount getAdminAccount() {
        if (naasConfig == null) {
            throw new IllegalArgumentException("NAASConfig not set.");
        }
        if (naasConfig.getAdminAccount() == null) {
            throw new IllegalArgumentException("NAASConfig AdminAccount not set.");
        }
        if (adminAccount == null) {
            logger.debug("Setting AdminVisit.");
            adminAccount = accountDao.getByNAASAccount(naasConfig.getAdminAccount().getUsername());
            if (adminAccount == null) {
                throw new IllegalArgumentException("NAASAdmin AdminAccount not in database.");
            }
        }
        logger.debug("getAdminAccount: " + adminAccount);
        return adminAccount;
    }

    protected Activity makeNewActivity(ActivityType type, NodeVisit visit) {
        if (visit == null || visit.getUserAccount() == null) {
            throw new RuntimeException("Null visit or Account");
        }
        Activity logEntry = new Activity();
        logEntry.setModifiedById(visit.getUserAccount().getId());
        logEntry.setIp(visit.getIp());
        logEntry.setType(type);
        return logEntry;
    }

    protected Activity makeNewActivity(ActivityType type) {
        return makeNewActivity(type, getNosConfig().getLocalhostIp());
    }

    protected Activity makeNewActivity(ActivityType type, String fromIp) {
        Activity logEntry = new Activity();
        logEntry.setModifiedById(getAdminVisit().getUserAccount().getId());
        logEntry.setIp(fromIp);
        logEntry.setType(type);
        return logEntry;
    }

    /**
     * @param visit
     * @param minimalRole
     */
    protected void validateByRole(NodeVisit visit, SystemRoleType minimalRole) {
        if (visit == null) {
            throw new IllegalArgumentException("VisitIdentity not provided.");
        }
        validateByRole(visit.getUserAccount(), minimalRole);
    }

    public ActivityDao getActivityDao() {
        return activityDao;
    }

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public NAASConfig getNaasConfig() {
        return naasConfig;
    }

    public void setNaasConfig(NAASConfig naasConfig) {
        this.naasConfig = naasConfig;
    }

    public NOSConfig getNosConfig() {
        return nosConfig;
    }

    public void setNosConfig(NOSConfig nosConfig) {
        this.nosConfig = nosConfig;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
}

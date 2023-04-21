package org.ministone.mlets.user.service;

/**
 * <p>
 * Spring Service base class for <code>org.ministone.mlets.user.service.UserService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see org.ministone.mlets.user.service.UserService
 */
public abstract class UserServiceBase implements org.ministone.mlets.user.service.UserService {

    private org.ministone.mlets.user.domain.AccountDataDao accountDataDao;

    /**
     * Sets the reference to <code>accountData</code>'s DAO.
     */
    public void setAccountDataDao(org.ministone.mlets.user.domain.AccountDataDao accountDataDao) {
        this.accountDataDao = accountDataDao;
    }

    /**
     * Gets the reference to <code>accountData</code>'s DAO.
     */
    protected org.ministone.mlets.user.domain.AccountDataDao getAccountDataDao() {
        return this.accountDataDao;
    }

    private org.ministone.mlets.user.domain.UserProfileDataDao userProfileDataDao;

    /**
     * Sets the reference to <code>userProfileData</code>'s DAO.
     */
    public void setUserProfileDataDao(org.ministone.mlets.user.domain.UserProfileDataDao userProfileDataDao) {
        this.userProfileDataDao = userProfileDataDao;
    }

    /**
     * Gets the reference to <code>userProfileData</code>'s DAO.
     */
    protected org.ministone.mlets.user.domain.UserProfileDataDao getUserProfileDataDao() {
        return this.userProfileDataDao;
    }

    /**
     * @see org.ministone.mlets.user.service.UserService#removeAccount(java.lang.String)
     */
    public void removeAccount(java.lang.String idlist) {
        if (idlist == null || idlist.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.removeAccount(java.lang.String idlist) - 'idlist' can not be null or empty");
        }
        try {
            this.handleRemoveAccount(idlist);
        } catch (Throwable th) {
            throw new org.ministone.mlets.user.service.UserServiceException("Error performing 'org.ministone.mlets.user.service.UserService.removeAccount(java.lang.String idlist)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #removeAccount(java.lang.String)}
      */
    protected abstract void handleRemoveAccount(java.lang.String idlist) throws java.lang.Exception;

    /**
     * @see org.ministone.mlets.user.service.UserService#getAccountById(java.lang.String)
     */
    public org.ministone.mlets.user.vo.Account getAccountById(java.lang.String accountId) {
        if (accountId == null || accountId.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.getAccountById(java.lang.String accountId) - 'accountId' can not be null or empty");
        }
        try {
            return this.handleGetAccountById(accountId);
        } catch (Throwable th) {
            throw new org.ministone.mlets.user.service.UserServiceException("Error performing 'org.ministone.mlets.user.service.UserService.getAccountById(java.lang.String accountId)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #getAccountById(java.lang.String)}
      */
    protected abstract org.ministone.mlets.user.vo.Account handleGetAccountById(java.lang.String accountId) throws java.lang.Exception;

    /**
     * @see org.ministone.mlets.user.service.UserService#getAccountByUsername(java.lang.String)
     */
    public org.ministone.mlets.user.vo.Account getAccountByUsername(java.lang.String userName) {
        if (userName == null || userName.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.getAccountByUsername(java.lang.String userName) - 'userName' can not be null or empty");
        }
        try {
            return this.handleGetAccountByUsername(userName);
        } catch (Throwable th) {
            throw new org.ministone.mlets.user.service.UserServiceException("Error performing 'org.ministone.mlets.user.service.UserService.getAccountByUsername(java.lang.String userName)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #getAccountByUsername(java.lang.String)}
      */
    protected abstract org.ministone.mlets.user.vo.Account handleGetAccountByUsername(java.lang.String userName) throws java.lang.Exception;

    /**
     * @see org.ministone.mlets.user.service.UserService#queryAccount(org.ministone.mlets.user.criteria.AccountCriteria)
     */
    public org.ministone.util.PagedObject queryAccount(org.ministone.mlets.user.criteria.AccountCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.queryAccount(org.ministone.mlets.user.criteria.AccountCriteria criteria) - 'criteria' can not be null");
        }
        if (criteria.getUserName() == null || criteria.getUserName().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.queryAccount(org.ministone.mlets.user.criteria.AccountCriteria criteria) - 'criteria.userName' can not be null or empty");
        }
        try {
            return this.handleQueryAccount(criteria);
        } catch (Throwable th) {
            throw new org.ministone.mlets.user.service.UserServiceException("Error performing 'org.ministone.mlets.user.service.UserService.queryAccount(org.ministone.mlets.user.criteria.AccountCriteria criteria)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #queryAccount(org.ministone.mlets.user.criteria.AccountCriteria)}
      */
    protected abstract org.ministone.util.PagedObject handleQueryAccount(org.ministone.mlets.user.criteria.AccountCriteria criteria) throws java.lang.Exception;

    /**
     * @see org.ministone.mlets.user.service.UserService#fetchPasswordViaEmail(java.lang.String)
     */
    public void fetchPasswordViaEmail(java.lang.String userName) {
        if (userName == null || userName.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.fetchPasswordViaEmail(java.lang.String userName) - 'userName' can not be null or empty");
        }
        try {
            this.handleFetchPasswordViaEmail(userName);
        } catch (Throwable th) {
            throw new org.ministone.mlets.user.service.UserServiceException("Error performing 'org.ministone.mlets.user.service.UserService.fetchPasswordViaEmail(java.lang.String userName)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #fetchPasswordViaEmail(java.lang.String)}
      */
    protected abstract void handleFetchPasswordViaEmail(java.lang.String userName) throws java.lang.Exception;

    /**
     * @see org.ministone.mlets.user.service.UserService#activateAccount(java.lang.String)
     */
    public void activateAccount(java.lang.String accountId) {
        if (accountId == null || accountId.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.activateAccount(java.lang.String accountId) - 'accountId' can not be null or empty");
        }
        try {
            this.handleActivateAccount(accountId);
        } catch (Throwable th) {
            throw new org.ministone.mlets.user.service.UserServiceException("Error performing 'org.ministone.mlets.user.service.UserService.activateAccount(java.lang.String accountId)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #activateAccount(java.lang.String)}
      */
    protected abstract void handleActivateAccount(java.lang.String accountId) throws java.lang.Exception;

    /**
     * @see org.ministone.mlets.user.service.UserService#disableAccount(java.lang.String)
     */
    public void disableAccount(java.lang.String accountId) {
        if (accountId == null || accountId.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.disableAccount(java.lang.String accountId) - 'accountId' can not be null or empty");
        }
        try {
            this.handleDisableAccount(accountId);
        } catch (Throwable th) {
            throw new org.ministone.mlets.user.service.UserServiceException("Error performing 'org.ministone.mlets.user.service.UserService.disableAccount(java.lang.String accountId)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #disableAccount(java.lang.String)}
      */
    protected abstract void handleDisableAccount(java.lang.String accountId) throws java.lang.Exception;

    /**
     * @see org.ministone.mlets.user.service.UserService#registerAccount(org.ministone.mlets.user.vo.Account)
     */
    public void registerAccount(org.ministone.mlets.user.vo.Account account) throws org.ministone.mlets.user.UserExistException {
        if (account == null) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.registerAccount(org.ministone.mlets.user.vo.Account account) - 'account' can not be null");
        }
        if (account.getUserName() == null || account.getUserName().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.registerAccount(org.ministone.mlets.user.vo.Account account) - 'account.userName' can not be null or empty");
        }
        if (account.getEmail() == null || account.getEmail().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.registerAccount(org.ministone.mlets.user.vo.Account account) - 'account.email' can not be null or empty");
        }
        if (account.getAccountStatus() == null || account.getAccountStatus().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.registerAccount(org.ministone.mlets.user.vo.Account account) - 'account.accountStatus' can not be null or empty");
        }
        if (account.getPasswd() == null || account.getPasswd().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.registerAccount(org.ministone.mlets.user.vo.Account account) - 'account.passwd' can not be null or empty");
        }
        try {
            this.handleRegisterAccount(account);
        } catch (org.ministone.mlets.user.UserExistException ex) {
            throw ex;
        } catch (Throwable th) {
            throw new org.ministone.mlets.user.service.UserServiceException("Error performing 'org.ministone.mlets.user.service.UserService.registerAccount(org.ministone.mlets.user.vo.Account account)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #registerAccount(org.ministone.mlets.user.vo.Account)}
      */
    protected abstract void handleRegisterAccount(org.ministone.mlets.user.vo.Account account) throws java.lang.Exception;

    /**
     * @see org.ministone.mlets.user.service.UserService#updateProfile(org.ministone.mlets.user.vo.UserProfile)
     */
    public void updateProfile(org.ministone.mlets.user.vo.UserProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.updateProfile(org.ministone.mlets.user.vo.UserProfile profile) - 'profile' can not be null");
        }
        try {
            this.handleUpdateProfile(profile);
        } catch (Throwable th) {
            throw new org.ministone.mlets.user.service.UserServiceException("Error performing 'org.ministone.mlets.user.service.UserService.updateProfile(org.ministone.mlets.user.vo.UserProfile profile)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #updateProfile(org.ministone.mlets.user.vo.UserProfile)}
      */
    protected abstract void handleUpdateProfile(org.ministone.mlets.user.vo.UserProfile profile) throws java.lang.Exception;

    /**
     * @see org.ministone.mlets.user.service.UserService#changePassword(java.lang.String, java.lang.String)
     */
    public void changePassword(java.lang.String newPassword, java.lang.String accountId) {
        if (newPassword == null || newPassword.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.changePassword(java.lang.String newPassword, java.lang.String accountId) - 'newPassword' can not be null or empty");
        }
        if (accountId == null || accountId.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.changePassword(java.lang.String newPassword, java.lang.String accountId) - 'accountId' can not be null or empty");
        }
        try {
            this.handleChangePassword(newPassword, accountId);
        } catch (Throwable th) {
            throw new org.ministone.mlets.user.service.UserServiceException("Error performing 'org.ministone.mlets.user.service.UserService.changePassword(java.lang.String newPassword, java.lang.String accountId)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #changePassword(java.lang.String, java.lang.String)}
      */
    protected abstract void handleChangePassword(java.lang.String newPassword, java.lang.String accountId) throws java.lang.Exception;

    /**
     * @see org.ministone.mlets.user.service.UserService#updateEmail(java.lang.String, java.lang.String)
     */
    public void updateEmail(java.lang.String email, java.lang.String accountId) {
        if (email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.updateEmail(java.lang.String email, java.lang.String accountId) - 'email' can not be null or empty");
        }
        if (accountId == null || accountId.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.updateEmail(java.lang.String email, java.lang.String accountId) - 'accountId' can not be null or empty");
        }
        try {
            this.handleUpdateEmail(email, accountId);
        } catch (Throwable th) {
            throw new org.ministone.mlets.user.service.UserServiceException("Error performing 'org.ministone.mlets.user.service.UserService.updateEmail(java.lang.String email, java.lang.String accountId)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #updateEmail(java.lang.String, java.lang.String)}
      */
    protected abstract void handleUpdateEmail(java.lang.String email, java.lang.String accountId) throws java.lang.Exception;

    /**
     * @see org.ministone.mlets.user.service.UserService#login(java.lang.String, java.lang.String)
     */
    public org.ministone.mlets.user.vo.Account login(java.lang.String userName, java.lang.String passwd) throws org.ministone.mlets.user.PasswordNotMatchException, org.ministone.mlets.user.UserNotExistException {
        if (userName == null || userName.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.login(java.lang.String userName, java.lang.String passwd) - 'userName' can not be null or empty");
        }
        if (passwd == null || passwd.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.mlets.user.service.UserService.login(java.lang.String userName, java.lang.String passwd) - 'passwd' can not be null or empty");
        }
        try {
            return this.handleLogin(userName, passwd);
        } catch (org.ministone.mlets.user.PasswordNotMatchException ex) {
            throw ex;
        } catch (org.ministone.mlets.user.UserNotExistException ex) {
            throw ex;
        } catch (Throwable th) {
            throw new org.ministone.mlets.user.service.UserServiceException("Error performing 'org.ministone.mlets.user.service.UserService.login(java.lang.String userName, java.lang.String passwd)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #login(java.lang.String, java.lang.String)}
      */
    protected abstract org.ministone.mlets.user.vo.Account handleLogin(java.lang.String userName, java.lang.String passwd) throws java.lang.Exception;

    /**
     * Gets the current <code>principal</code> if one has been set,
     * otherwise returns <code>null</code>.
     *
     * @return the current principal
     */
    protected java.security.Principal getPrincipal() {
        return org.ministone.PrincipalStore.get();
    }
}

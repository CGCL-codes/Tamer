package org.ministone.arena.service;

/**
 * <p>
 * Spring Service base class for <code>org.ministone.arena.service.ArenaService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see org.ministone.arena.service.ArenaService
 */
public abstract class ArenaServiceBase implements org.ministone.arena.service.ArenaService {

    private org.ministone.mlets.user.service.UserService userService;

    /**
     * Sets the reference to <code>userService</code>.
     */
    public void setUserService(org.ministone.mlets.user.service.UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets the reference to <code>userService</code>.
     */
    protected org.ministone.mlets.user.service.UserService getUserService() {
        return this.userService;
    }

    private org.ministone.portal.service.PortalService portalService;

    /**
     * Sets the reference to <code>portalService</code>.
     */
    public void setPortalService(org.ministone.portal.service.PortalService portalService) {
        this.portalService = portalService;
    }

    /**
     * Gets the reference to <code>portalService</code>.
     */
    protected org.ministone.portal.service.PortalService getPortalService() {
        return this.portalService;
    }

    private org.ministone.mlets.admin.service.MletService mletService;

    /**
     * Sets the reference to <code>mletService</code>.
     */
    public void setMletService(org.ministone.mlets.admin.service.MletService mletService) {
        this.mletService = mletService;
    }

    /**
     * Gets the reference to <code>mletService</code>.
     */
    protected org.ministone.mlets.admin.service.MletService getMletService() {
        return this.mletService;
    }

    private org.ministone.arena.domain.ArenaConfigDao arenaConfigDao;

    /**
     * Sets the reference to <code>arenaConfig</code>'s DAO.
     */
    public void setArenaConfigDao(org.ministone.arena.domain.ArenaConfigDao arenaConfigDao) {
        this.arenaConfigDao = arenaConfigDao;
    }

    /**
     * Gets the reference to <code>arenaConfig</code>'s DAO.
     */
    protected org.ministone.arena.domain.ArenaConfigDao getArenaConfigDao() {
        return this.arenaConfigDao;
    }

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

    private org.ministone.arena.domain.BlogConfigDao blogConfigDao;

    /**
     * Sets the reference to <code>blogConfig</code>'s DAO.
     */
    public void setBlogConfigDao(org.ministone.arena.domain.BlogConfigDao blogConfigDao) {
        this.blogConfigDao = blogConfigDao;
    }

    /**
     * Gets the reference to <code>blogConfig</code>'s DAO.
     */
    protected org.ministone.arena.domain.BlogConfigDao getBlogConfigDao() {
        return this.blogConfigDao;
    }

    /**
     * @see org.ministone.arena.service.ArenaService#registerArena(org.ministone.arena.vo.Arena)
     */
    public void registerArena(org.ministone.arena.vo.Arena arena) throws org.ministone.mlets.user.UserExistException {
        if (arena == null) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.registerArena(org.ministone.arena.vo.Arena arena) - 'arena' can not be null");
        }
        if (arena.getDomainName() == null || arena.getDomainName().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.registerArena(org.ministone.arena.vo.Arena arena) - 'arena.domainName' can not be null or empty");
        }
        try {
            this.handleRegisterArena(arena);
        } catch (org.ministone.mlets.user.UserExistException ex) {
            throw ex;
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.registerArena(org.ministone.arena.vo.Arena arena)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #registerArena(org.ministone.arena.vo.Arena)}
      */
    protected abstract void handleRegisterArena(org.ministone.arena.vo.Arena arena) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#registerUser(org.ministone.mlets.user.vo.Account)
     */
    public void registerUser(org.ministone.mlets.user.vo.Account account) throws org.ministone.mlets.user.UserExistException {
        if (account == null) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.registerUser(org.ministone.mlets.user.vo.Account account) - 'account' can not be null");
        }
        if (account.getUserName() == null || account.getUserName().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.registerUser(org.ministone.mlets.user.vo.Account account) - 'account.userName' can not be null or empty");
        }
        if (account.getEmail() == null || account.getEmail().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.registerUser(org.ministone.mlets.user.vo.Account account) - 'account.email' can not be null or empty");
        }
        if (account.getAccountStatus() == null || account.getAccountStatus().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.registerUser(org.ministone.mlets.user.vo.Account account) - 'account.accountStatus' can not be null or empty");
        }
        if (account.getPasswd() == null || account.getPasswd().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.registerUser(org.ministone.mlets.user.vo.Account account) - 'account.passwd' can not be null or empty");
        }
        try {
            this.handleRegisterUser(account);
        } catch (org.ministone.mlets.user.UserExistException ex) {
            throw ex;
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.registerUser(org.ministone.mlets.user.vo.Account account)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #registerUser(org.ministone.mlets.user.vo.Account)}
      */
    protected abstract void handleRegisterUser(org.ministone.mlets.user.vo.Account account) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#registerBlog(boolean, org.ministone.arena.vo.Blog)
     */
    public void registerBlog(boolean needCreateArena, org.ministone.arena.vo.Blog blog) {
        if (blog == null) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.registerBlog(boolean needCreateArena, org.ministone.arena.vo.Blog blog) - 'blog' can not be null");
        }
        try {
            this.handleRegisterBlog(needCreateArena, blog);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.registerBlog(boolean needCreateArena, org.ministone.arena.vo.Blog blog)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #registerBlog(boolean, org.ministone.arena.vo.Blog)}
      */
    protected abstract void handleRegisterBlog(boolean needCreateArena, org.ministone.arena.vo.Blog blog) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#getArenaById(java.lang.String)
     */
    public org.ministone.arena.vo.Arena getArenaById(java.lang.String arenaId) {
        if (arenaId == null || arenaId.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.getArenaById(java.lang.String arenaId) - 'arenaId' can not be null or empty");
        }
        try {
            return this.handleGetArenaById(arenaId);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.getArenaById(java.lang.String arenaId)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #getArenaById(java.lang.String)}
      */
    protected abstract org.ministone.arena.vo.Arena handleGetArenaById(java.lang.String arenaId) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#getArenaByUserId(java.lang.String)
     */
    public org.ministone.arena.vo.Arena getArenaByUserId(java.lang.String accountId) {
        if (accountId == null || accountId.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.getArenaByUserId(java.lang.String accountId) - 'accountId' can not be null or empty");
        }
        try {
            return this.handleGetArenaByUserId(accountId);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.getArenaByUserId(java.lang.String accountId)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #getArenaByUserId(java.lang.String)}
      */
    protected abstract org.ministone.arena.vo.Arena handleGetArenaByUserId(java.lang.String accountId) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#getArenaByUserName(java.lang.String)
     */
    public org.ministone.arena.vo.Arena getArenaByUserName(java.lang.String userName) {
        if (userName == null || userName.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.getArenaByUserName(java.lang.String userName) - 'userName' can not be null or empty");
        }
        try {
            return this.handleGetArenaByUserName(userName);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.getArenaByUserName(java.lang.String userName)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #getArenaByUserName(java.lang.String)}
      */
    protected abstract org.ministone.arena.vo.Arena handleGetArenaByUserName(java.lang.String userName) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#getArenaByDomainName(java.lang.String)
     */
    public org.ministone.arena.vo.Arena getArenaByDomainName(java.lang.String domainName) {
        if (domainName == null || domainName.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.getArenaByDomainName(java.lang.String domainName) - 'domainName' can not be null or empty");
        }
        try {
            return this.handleGetArenaByDomainName(domainName);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.getArenaByDomainName(java.lang.String domainName)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #getArenaByDomainName(java.lang.String)}
      */
    protected abstract org.ministone.arena.vo.Arena handleGetArenaByDomainName(java.lang.String domainName) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#loadBlogPlainContent(java.lang.String, java.lang.String, java.util.Map)
     */
    public org.ministone.arena.vo.PlainStageContent loadBlogPlainContent(java.lang.String blogId, java.lang.String pageId, java.util.Map params) {
        if (blogId == null || blogId.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.loadBlogPlainContent(java.lang.String blogId, java.lang.String pageId, java.util.Map params) - 'blogId' can not be null or empty");
        }
        try {
            return this.handleLoadBlogPlainContent(blogId, pageId, params);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.loadBlogPlainContent(java.lang.String blogId, java.lang.String pageId, java.util.Map params)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #loadBlogPlainContent(java.lang.String, java.lang.String, java.util.Map)}
      */
    protected abstract org.ministone.arena.vo.PlainStageContent handleLoadBlogPlainContent(java.lang.String blogId, java.lang.String pageId, java.util.Map params) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#queryArena(org.ministone.arena.criteria.ArenaCriteria)
     */
    public org.ministone.util.PagedObject queryArena(org.ministone.arena.criteria.ArenaCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.queryArena(org.ministone.arena.criteria.ArenaCriteria criteria) - 'criteria' can not be null");
        }
        if (criteria.getUserName() == null || criteria.getUserName().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.queryArena(org.ministone.arena.criteria.ArenaCriteria criteria) - 'criteria.userName' can not be null or empty");
        }
        if (criteria.getDomainName() == null || criteria.getDomainName().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.queryArena(org.ministone.arena.criteria.ArenaCriteria criteria) - 'criteria.domainName' can not be null or empty");
        }
        if (criteria.getUserId() == null || criteria.getUserId().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.queryArena(org.ministone.arena.criteria.ArenaCriteria criteria) - 'criteria.userId' can not be null or empty");
        }
        try {
            return this.handleQueryArena(criteria);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.queryArena(org.ministone.arena.criteria.ArenaCriteria criteria)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #queryArena(org.ministone.arena.criteria.ArenaCriteria)}
      */
    protected abstract org.ministone.util.PagedObject handleQueryArena(org.ministone.arena.criteria.ArenaCriteria criteria) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#getStageOfUser(java.lang.String)
     */
    public java.util.Collection getStageOfUser(java.lang.String accountId) {
        if (accountId == null || accountId.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.getStageOfUser(java.lang.String accountId) - 'accountId' can not be null or empty");
        }
        try {
            return this.handleGetStageOfUser(accountId);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.getStageOfUser(java.lang.String accountId)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #getStageOfUser(java.lang.String)}
      */
    protected abstract java.util.Collection handleGetStageOfUser(java.lang.String accountId) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#login(java.lang.String, java.lang.String)
     */
    public org.ministone.mlets.user.vo.Account login(java.lang.String userName, java.lang.String password) throws org.ministone.mlets.user.PasswordNotMatchException, org.ministone.mlets.user.UserNotExistException {
        if (userName == null || userName.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.login(java.lang.String userName, java.lang.String password) - 'userName' can not be null or empty");
        }
        if (password == null || password.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.login(java.lang.String userName, java.lang.String password) - 'password' can not be null or empty");
        }
        try {
            return this.handleLogin(userName, password);
        } catch (org.ministone.mlets.user.PasswordNotMatchException ex) {
            throw ex;
        } catch (org.ministone.mlets.user.UserNotExistException ex) {
            throw ex;
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.login(java.lang.String userName, java.lang.String password)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #login(java.lang.String, java.lang.String)}
      */
    protected abstract org.ministone.mlets.user.vo.Account handleLogin(java.lang.String userName, java.lang.String password) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#getAllArenaCategories()
     */
    public java.util.Collection getAllArenaCategories() {
        try {
            return this.handleGetAllArenaCategories();
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.getAllArenaCategories()' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #getAllArenaCategories()}
      */
    protected abstract java.util.Collection handleGetAllArenaCategories() throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#createArenaCategory(org.ministone.arena.vo.ArenaCategory)
     */
    public void createArenaCategory(org.ministone.arena.vo.ArenaCategory arenaCategory) {
        if (arenaCategory == null) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.createArenaCategory(org.ministone.arena.vo.ArenaCategory arenaCategory) - 'arenaCategory' can not be null");
        }
        if (arenaCategory.getCategoryName() == null || arenaCategory.getCategoryName().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.createArenaCategory(org.ministone.arena.vo.ArenaCategory arenaCategory) - 'arenaCategory.categoryName' can not be null or empty");
        }
        try {
            this.handleCreateArenaCategory(arenaCategory);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.createArenaCategory(org.ministone.arena.vo.ArenaCategory arenaCategory)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #createArenaCategory(org.ministone.arena.vo.ArenaCategory)}
      */
    protected abstract void handleCreateArenaCategory(org.ministone.arena.vo.ArenaCategory arenaCategory) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#updateArenaCategory(org.ministone.arena.vo.ArenaCategory)
     */
    public void updateArenaCategory(org.ministone.arena.vo.ArenaCategory arenaCategory) {
        if (arenaCategory == null) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.updateArenaCategory(org.ministone.arena.vo.ArenaCategory arenaCategory) - 'arenaCategory' can not be null");
        }
        if (arenaCategory.getCategoryName() == null || arenaCategory.getCategoryName().trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.updateArenaCategory(org.ministone.arena.vo.ArenaCategory arenaCategory) - 'arenaCategory.categoryName' can not be null or empty");
        }
        try {
            this.handleUpdateArenaCategory(arenaCategory);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.updateArenaCategory(org.ministone.arena.vo.ArenaCategory arenaCategory)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #updateArenaCategory(org.ministone.arena.vo.ArenaCategory)}
      */
    protected abstract void handleUpdateArenaCategory(org.ministone.arena.vo.ArenaCategory arenaCategory) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#updateUserProfile(org.ministone.mlets.user.vo.UserProfile)
     */
    public void updateUserProfile(org.ministone.mlets.user.vo.UserProfile userPofile) {
        if (userPofile == null) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.updateUserProfile(org.ministone.mlets.user.vo.UserProfile userPofile) - 'userPofile' can not be null");
        }
        try {
            this.handleUpdateUserProfile(userPofile);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.updateUserProfile(org.ministone.mlets.user.vo.UserProfile userPofile)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #updateUserProfile(org.ministone.mlets.user.vo.UserProfile)}
      */
    protected abstract void handleUpdateUserProfile(org.ministone.mlets.user.vo.UserProfile userPofile) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#removeArena(java.lang.String)
     */
    public void removeArena(java.lang.String idlist) {
        if (idlist == null || idlist.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.removeArena(java.lang.String idlist) - 'idlist' can not be null or empty");
        }
        try {
            this.handleRemoveArena(idlist);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.removeArena(java.lang.String idlist)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #removeArena(java.lang.String)}
      */
    protected abstract void handleRemoveArena(java.lang.String idlist) throws java.lang.Exception;

    /**
     * @see org.ministone.arena.service.ArenaService#removeArenaCategory(java.lang.String)
     */
    public void removeArenaCategory(java.lang.String idlist) {
        if (idlist == null || idlist.trim().length() == 0) {
            throw new IllegalArgumentException("org.ministone.arena.service.ArenaService.removeArenaCategory(java.lang.String idlist) - 'idlist' can not be null or empty");
        }
        try {
            this.handleRemoveArenaCategory(idlist);
        } catch (Throwable th) {
            throw new org.ministone.arena.service.ArenaServiceException("Error performing 'org.ministone.arena.service.ArenaService.removeArenaCategory(java.lang.String idlist)' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #removeArenaCategory(java.lang.String)}
      */
    protected abstract void handleRemoveArenaCategory(java.lang.String idlist) throws java.lang.Exception;

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

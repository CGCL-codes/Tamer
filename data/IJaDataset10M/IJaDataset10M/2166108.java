package org.az.paccman.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.az.model.Account;
import org.az.model.PersistentSession;
import org.az.model.User;
import org.az.model.Account.Status;
import org.az.model.common.UserSession;
import org.az.paccman.ConfigurationConst;
import org.az.paccman.dao.AccountDAO;
import org.az.tb.common.vo.client.ContactVo;
import org.az.tb.common.vo.client.ErrorsSetVO;
import org.az.tb.common.vo.client.ResourceIDConst;
import org.az.tb.common.vo.client.UserProfileVo;
import org.az.tb.common.vo.client.exceptions.AuthenticationException;
import org.az.tb.common.vo.client.exceptions.InvalidUserException;
import org.az.tb.common.vo.client.exceptions.NoSuchUserException;
import org.az.tb.common.vo.client.exceptions.TechException;
import org.az.tb.common.vo.client.exceptions.ValidationException;
import org.az.tb.services.client.BeanNamesConst;
import org.az.tb.services.client.SnippetService;
import org.az.tb.services.client.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;

@Component(BeanNamesConst.USER_SERVICE_BEAN_NAME)
public final class UserServiceImpl implements UserService {

    private static final String FLD_PASSWORD = "password";

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private AccountDAO dao;

    @Autowired
    private ConfigService config;

    @Autowired
    private UserSession session;

    @Autowired
    private UserAccountService accountService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private VoMapper voMapper;

    @Autowired
    private SnippetService rs;

    @Resource(name = BeanNamesConst.PASSWORD_VALIDATOR)
    private Validator passwordValidator;

    @Autowired
    private ValidationService validator;

    @Autowired
    private MailService mailService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ContactVo login(String username, String password, String persistentSessionId, String invitationCode, boolean rememberMe) {
        if (logger.isDebugEnabled()) {
            logger.debug("login username=" + username + " sessionId=" + persistentSessionId + " invitationCode=" + invitationCode + " rememberMe=" + rememberMe);
        }
        User user = null;
        if (StringUtils.isNotEmpty(username)) {
            user = login(username, password, rememberMe);
        } else if (StringUtils.isNotEmpty(persistentSessionId)) {
            user = restoreUserFromSession(persistentSessionId);
        } else if (StringUtils.isEmpty(invitationCode)) {
            throw new AuthenticationException(rs.get(ResourceIDConst.ERROR_AUTH_FAILED));
        }
        if (user == null) {
            return null;
        }
        ContactVo profile = accountService.initLoggedInUser(user, rememberMe);
        logger.debug("user log in OK: " + profile.toString());
        return profile;
    }

    private User createAndActivateAccount(User user) {
        Account account = new Account();
        account.setDefaultProfile(user);
        account.setUsername(user.getEmail());
        account.setStatus(Status.CONFIRMED);
        account.setPassword(CryptoTicketUtil.generateInvitationCode(config.getIntValue(ConfigurationConst.GENERATED_PASSWORD_LENGTH)));
        dao.save(account);
        user.setAccount(account);
        dao.update(user);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("user", user);
        model.put("account", account);
        model.put("fromPersonal", config.getValue(ConfigurationConst.SYSTEM_NAME));
        try {
            mailService.send(EmailTemplateNameConst.GENERATED_PASSWORD_TEMPLATE, null, user.getEmail(), rs.get(ResourceIDConst.EMAIL_SUBJECT_AUTO_REGISTRATION), model);
        } catch (MailException e) {
            logger.error("error while sending auto generated password", e);
        }
        return user;
    }

    private User login(String username, String password, boolean isSessionPersistent) {
        User person = dao.getProfile(username, password);
        if (person == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("login failed for username " + username);
            }
            throw new AuthenticationException(rs.get(ResourceIDConst.ERROR_LOGIN_OR_PASSWORD_INCORRECT));
        } else {
            if (session.getUser().getId().equals(person.getId())) {
                person = session.getUser();
                logger.error("user " + person.getId() + " is already logged in! This is unacceptable!");
                return person;
            } else {
                person.getAccount().setLastAccessDate(new Date());
                dao.update(person);
                Status status = person.getAccount().getStatus();
                boolean illegalStatus = (status == Account.Status.DISABLED || status == Account.Status.ERROR || status == Account.Status.NEW || status == Account.Status.NOT_CONFIRMED);
                if (illegalStatus && !config.getBoolValue(ConfigurationConst.IGNORE_ACCOUNT_STATUS_ON_LOGIN, false)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Invalid account status " + status + " for user " + person.getId());
                    }
                    throw new AuthenticationException(rs.get(ResourceIDConst.ERROR_ACCOUNT_DISABLED));
                }
                return person;
            }
        }
    }

    /**
     * receives request from the registration form.
     * ACHTUNG!! 2 transactions in this method
     */
    public void register(UserProfileVo form) {
        registrationService.register(form);
    }

    @Transactional
    public ContactVo changeUserData(UserProfileVo user) {
        Account account = session.getUser().getAccount();
        if (!account.getPassword().equals(user.getPassword())) {
            BindException errors = new BindException(user, "editProfile");
            errors.rejectValue(FLD_PASSWORD, ResourceIDConst.ERROR_INVALID_OLD_PWD);
            throwErrorsIfAny(errors);
        }
        User person = session.getUser();
        voMapper.map2User(user, person);
        dao.update(person);
        if (logger.isDebugEnabled()) {
            logger.debug("changed the user data for user " + user);
        }
        return user;
    }

    @Transactional
    public void changePassword(String oldPassword, UserProfileVo user) {
        BindException errors = new BindException(user, "editorForm");
        Account account = session.getUser().getAccount();
        if (!account.getPassword().equals(oldPassword)) {
            errors.rejectValue(FLD_PASSWORD, ResourceIDConst.ERROR_INVALID_OLD_PWD);
        } else {
            passwordValidator.validate(user, errors);
        }
        throwErrorsIfAny(errors);
        account.setPassword(user.getPassword());
        dao.update(account);
        dao.deleteUserSessions(user.getId());
        logger.debug("changed the password for the user " + user.getId());
    }

    @Transactional
    public void logout() {
        User u = session.getUser();
        logger.debug("User " + session.getUser().getId() + " logged out");
        dao.update(u);
        PersistentSession persistentSession = dao.getSession(session.getSessionId());
        if (persistentSession != null) {
            logger.debug("Persistent session " + session.getSessionId() + " was found, deleting because user is logging of");
            dao.delete(persistentSession);
        }
        session.setInvitationCode(null);
        session.setSessionId(null);
        session.setUser(new User());
    }

    @Transactional(readOnly = true)
    public void restoreAccount(String email) throws AuthenticationException, TechException {
        User user = UserUtil.getRegisteredOrAnyUser(dao.getUserByEmail(email));
        if (user == null) {
            logger.info("password for non-existant account " + email + " was requested");
            throw new AuthenticationException(rs.get(ResourceIDConst.ERROR_NO_USER_WITH_SUCH_EMAIL));
        }
        if (user.getAccount() == null) {
            logger.info("password for user without account was requested " + email);
            throw new AuthenticationException(rs.get(ResourceIDConst.ERROR_PASSWORD_REQUESTED_FOR_USER_WITHOUT_ACCOUNT));
        }
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("user", user);
        model.put("account", user.getAccount());
        model.put("url", config.getValue(ConfigurationConst.SYSTEM_URL));
        try {
            logger.debug("Sending password for " + user.getEmail());
            mailService.send(EmailTemplateNameConst.PASSWORD_RECOVERY, null, user.getEmail(), rs.get(ResourceIDConst.EMAIL_SUBJECT_PASSWORD_RECOVERY), model);
        } catch (MailException ex) {
            logger.error(ex.getMessage(), ex);
            throw new TechException(rs.get(ResourceIDConst.ERROR_EMAIL_NOT_SENT));
        }
    }

    @Transactional
    public ContactVo confirmEmailAndLogin(String id, String key) {
        logger.info("confirmEmailAndLogin: id=" + id + " key=" + key);
        try {
            Account account = dao.getAccount(Long.parseLong(id));
            if (account != null) {
                String okey = registrationService.createConfirmationKey(account);
                if (okey.equals(key)) {
                    account.setLastAccessDate(new Date());
                    if (Account.Status.NOT_CONFIRMED.equals(account.getStatus()) || Account.Status.NEW.equals(account.getStatus())) {
                        account.setStatus(Account.Status.CONFIRMED);
                        dao.update(account);
                        return accountService.initLoggedInUser(account.getDefaultProfile(), true);
                    } else {
                        if (Account.Status.CONFIRMED.equals(account.getStatus())) {
                            throw new AuthenticationException("Ваша учетная запись уже активирована");
                        } else {
                            throw new AuthenticationException("invalid account state");
                        }
                    }
                } else {
                    throw new AuthenticationException("wrong confirmation key");
                }
            } else {
                throw new AuthenticationException("account not found");
            }
        } catch (NumberFormatException e) {
            throw new TechException("got invalid parameter: " + id);
        }
    }

    private User restoreUserFromSession(String sessionId) {
        if (session.isAuthorized() && session.getSessionId().equals(sessionId)) {
            return session.getUser();
        } else {
            return restorePersistentSession(sessionId);
        }
    }

    private User restorePersistentSession(String persistentSessionId) {
        PersistentSession persistentSession = dao.getEntityById(PersistentSession.class, persistentSessionId);
        if (persistentSession != null) {
            if ((new Date()).after(persistentSession.getValidTill())) {
                logger.warn("someone tried to restore session with id " + persistentSessionId + " (valid till + " + persistentSession.getValidTill() + ") on " + new Date());
                throw new AuthenticationException(rs.get(ResourceIDConst.ERROR_SESSION_EXPIRED));
            }
            if (logger.isDebugEnabled()) {
                logger.debug("found persistent session : " + persistentSession.getSessionId() + " of user " + persistentSession.getUser().getId());
            }
            session.setSessionId(persistentSession.getSessionId());
            return persistentSession.getUser();
        }
        throw new AuthenticationException(rs.get(ResourceIDConst.ERROR_SESSION_EMPTY));
    }

    private void throwErrorsIfAny(BindException errors) {
        if (errors.getErrorCount() > 0) {
            ErrorsSetVO errorsSet = new ErrorsSetVO();
            errorsSet = validator.translate(errors, errorsSet);
            ValidationException ae = new ValidationException(errorsSet);
            throw ae;
        }
    }

    @Transactional(readOnly = true)
    public ContactVo getUserPublicProfile(Long userId) {
        User user = dao.getProfile(userId);
        if (user == null) {
            logger.debug("user with id " + userId + " not found in database");
            throw new NoSuchUserException(rs.get(ResourceIDConst.ERROR_INVALID_USER));
        } else {
            ContactVo vo = voMapper.map2ContactVo(user);
            Account account = user.getAccount();
            if (account != null) {
                if (Account.Status.DISABLED.equals(account.getStatus())) {
                    logger.warn("access to disabled account:" + account.getId());
                    throw new InvalidUserException(rs.get(ResourceIDConst.ERROR_USER_DISABLED));
                }
                if (Account.Status.ERROR.equals(account.getStatus())) {
                    logger.error("access to problematic account:" + account.getId());
                    throw new InvalidUserException(rs.get(ResourceIDConst.ERROR_USER_ERROR));
                }
                vo.setAccountExists(true);
            }
            if (!session.isAuthorized()) {
                vo.setEmail(null);
            }
            return vo;
        }
    }

    @Transactional
    public void bindInviteeAndInviter(User inviter) {
    }

    /**
     *   
     * @return returns list of contactVOs of the 10 top rated users.
     * reating should be calculated on a daily base
     */
    @Transactional(readOnly = true)
    public List<ContactVo> getTopUsers() {
        List<User> users = dao.getTopUsers();
        List<ContactVo> ret = new ArrayList<ContactVo>();
        for (User user : users) {
            ContactVo vo = voMapper.map2ContactVo(user);
            vo.setEmail(null);
            ret.add(vo);
        }
        return ret;
    }

    public List<ContactVo> getUsersByProperty(String keyword, int pageSize, int startIndex) {
        String[] fields = { "email", "firstName", "lastName", "id" };
        String[] filters = { "accountStatus" };
        List<User> users = dao.searchEntityByFields(pageSize, startIndex, createSearchQuery(fields, keyword), User.class, filters);
        List<ContactVo> list = new ArrayList<ContactVo>();
        for (User user : users) {
            ContactVo vo = voMapper.map2ContactVo(user);
            list.add(vo);
        }
        return list;
    }

    private BooleanQuery createSearchQuery(String[] fields, final String text) {
        BooleanQuery bq = new BooleanQuery();
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new RussianAnalyzer());
        MultiFieldQueryParser idParser = new MultiFieldQueryParser(fields, new KeywordAnalyzer());
        try {
            org.apache.lucene.search.Query query = parser.parse(text);
            org.apache.lucene.search.Query idQuery = idParser.parse(text);
            bq.add(query, Occur.SHOULD);
            bq.add(idQuery, Occur.SHOULD);
        } catch (ParseException e) {
            logger.error(e);
            throw new ValidationException(rs.get(ResourceIDConst.ERROR_INVALID_SEARCH_STRING));
        }
        return bq;
    }
}

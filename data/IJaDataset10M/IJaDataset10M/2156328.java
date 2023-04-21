package org.geogrid.aist.tsukubagama.services.request.impl;

import org.gridsphere.portlet.service.spi.PortletServiceProvider;
import org.gridsphere.portlet.service.spi.PortletServiceConfig;
import org.gridsphere.portlet.service.spi.PortletServiceFactory;
import org.gridsphere.portlet.service.PortletServiceException;
import org.gridsphere.portlet.service.PortletService;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;
import org.geogrid.aist.tsukubagama.services.request.AccountRequestService;
import org.geogrid.aist.tsukubagama.services.request.AccountRequest;
import org.geogrid.aist.tsukubagama.services.request.AccountRequestResult;
import org.geogrid.aist.tsukubagama.services.request.AccountActivationException;
import org.geogrid.aist.tsukubagama.services.request.helper.AccountRequestHelper;
import org.geogrid.aist.tsukubagama.services.request.dao.AccountRequestDAO;
import org.geogrid.aist.tsukubagama.services.request.dao.AccountRequestDAOFactory;
import org.geogrid.aist.tsukubagama.services.webservice.TsukubaGAMARegistryService;
import org.geogrid.aist.tsukubagama.services.webservice.TsukubaGAMARegistryResult;
import org.geogrid.aist.tsukubagama.services.webservice.VOMRSRegistryService;
import org.geogrid.aist.tsukubagama.services.webservice.VOMRSAccountRequest;
import org.geogrid.aist.tsukubagama.services.request.form.AccountRequestAttr;
import org.geogrid.aist.tsukubagama.services.request.form.AccountRequestAttrCollection;
import org.geogrid.aist.tsukubagama.services.request.form.AccountRequestAttrDescriptor;
import org.geogrid.aist.tsukubagama.services.mail.SendMailService;
import org.geogrid.aist.tsukubagama.util.validation.FormInputValidator;
import org.geogrid.aist.tsukubagama.util.password.PasswordGenerator;
import static org.geogrid.aist.tsukubagama.util.PortletServiceConfigUtil.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.commons.lang.StringUtils.isBlank;

public class AccountRequestServiceImpl implements AccountRequestService, PortletServiceProvider {

    private static Log log = LogFactory.getLog(AccountRequestServiceImpl.class);

    private AccountRequestDAO accountRequestDao = null;

    private TsukubaGAMARegistryService gamaClient = null;

    private VOMRSRegistryService vomrs = null;

    private boolean emailToAdmin;

    private boolean notifyAdmin;

    private String requestSubjectCn;

    private String requestSubjectDn;

    private String requestFormDir = null;

    private AccountRequestMail accountRequestMail = null;

    private boolean checkCertificateValidity = false;

    private String sendRenewalNoticeTo = "USER";

    private Pattern firstNamePattern = Pattern.compile("FirstName");

    private Pattern lastNamePattern = Pattern.compile("LastName");

    private Pattern userNamePattern = Pattern.compile("Username");

    private Pattern emailAddressPattern = Pattern.compile("EmailAddress");

    private Pattern organizationPattern = Pattern.compile("XXXX_ORGANIZATION");

    private static final String ACTIVATION_ERROR_MSG = "There was a problem activating your account. Please contact the portal administrator.";

    public void init(PortletServiceConfig config) {
        log.debug("Entering init");
        SendMailService mailService = null;
        AccountRequestDAOFactory daoFactory = null;
        try {
            daoFactory = (AccountRequestDAOFactory) createPortletService(AccountRequestDAOFactory.class);
            gamaClient = (TsukubaGAMARegistryService) createPortletService(TsukubaGAMARegistryService.class);
            vomrs = (VOMRSRegistryService) createPortletService(VOMRSRegistryService.class);
            mailService = (SendMailService) createPortletService(SendMailService.class);
        } catch (PortletServiceException e) {
            log.error("Unable to initialize Service: " + e.getMessage());
        }
        this.accountRequestDao = daoFactory.getAccountRequestDAO();
        requestFormDir = config.getServletContext().getRealPath("/WEB-INF/request-def/");
        if (log.isDebugEnabled()) {
            log.debug("Request Form Directory: " + requestFormDir);
        }
        this.emailToAdmin = getInitParameterAsBoolean(config, "EMAIL_TO_ADMINISTRATOR");
        this.notifyAdmin = getInitParameterAsBoolean(config, "ACTIVATION_COMPLETE_NOTIFY");
        this.requestSubjectCn = getInitParameter(config, "REQUEST_SUBJECT_CN");
        if (isBlank(requestSubjectCn)) {
            requestSubjectCn = "FirstName LastName";
        }
        this.requestSubjectDn = getInitParameter(config, "REQUEST_SUBJECT_DN");
        this.accountRequestMail = new AccountRequestMail(mailService);
        this.accountRequestMail.setAccountRequestNotifySubject(getInitParameter(config, "ACCOUNT_REQUEST_NOTIFY_SUBJECT"));
        this.accountRequestMail.setAccountRequestRenewNotifySubject(getInitParameter(config, "ACCOUNT_REQUEST_RENEW_NOTIFY_SUBJECT"));
        this.accountRequestMail.setAccountRequestResetNotifySubject(getInitParameter(config, "ACCOUNT_REQUEST_RESET_NOTIFY_SUBJECT"));
        this.accountRequestMail.setActivationMessageSubject(getInitParameter(config, "ACTIVATION_MESSAGE_SUBJECT"));
        this.accountRequestMail.setAdminMessageSubject(getInitParameter(config, "ADMIN_MESSAGE_SUBJECT"));
        this.accountRequestMail.setRejectMessageSubject(getInitParameter(config, "REJECT_MESSAGE_SUBJECT"));
        this.accountRequestMail.setRenewMessageSubject(getInitParameter(config, "RENEW_MESSAGE_SUBJECT"));
        this.accountRequestMail.setRenewAdminMessageSubject(getInitParameter(config, "RENEW_ADMIN_MESSAGE_SUBJECT"));
        this.checkCertificateValidity = getInitParameterAsBoolean(config, "CHECK_CERTIFICATE_VALIDITY");
        this.sendRenewalNoticeTo = getInitParameter(config, "SEND_RENEWAL_NOTICE_TO");
        log.debug("Exit init");
    }

    private static PortletService createPortletService(java.lang.Class serviceClass) throws PortletServiceException {
        return PortletServiceFactory.createPortletService(serviceClass, true);
    }

    public void destroy() {
    }

    public void save(AccountRequest request) {
        request.setActionDate(new Date());
        this.accountRequestDao.save(request);
    }

    public void delete(AccountRequest request) {
        this.accountRequestDao.delete(request);
    }

    public List getAccountRequestsByStatus(String status) {
        return this.accountRequestDao.getAccountRequestsByStatus(status);
    }

    public List getAccountRequestsByAttribute(String key, String value) {
        return this.accountRequestDao.getAccountRequestsByAttribute(key, value);
    }

    public AccountRequest getAccountRequestByOid(String oid) {
        return this.accountRequestDao.getAccountRequestByOid(oid);
    }

    public AccountRequest getAccountRequestByUsername(String username) {
        return this.accountRequestDao.getAccountRequestByUsername(username);
    }

    public void emptyTrash() {
    }

    public void saveAccount(Map params) {
        AccountRequest request = new AccountRequest();
        request.setAttributes(params);
        request.setUsername((String) params.get("username"));
        request.setEmailAddress((String) params.get("emailAddress"));
        request.setOpenId((String) params.get("openid"));
        request.setAccountType((String) params.get("account_type"));
        request.appendAdminComments("Request requires admin approval");
        String dn = buildUserDN(request);
        request.setAttribute("predictedSubject", dn);
        if (log.isDebugEnabled()) {
            log.debug("Save AccountRequest: \n" + request);
        }
        save(request);
        if (emailToAdmin) {
            try {
                accountRequestMail.sendAccountRequestNotice(request);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void deleteAccount(AccountRequest request) {
        log.debug("Entering deleteAccount");
        if (log.isDebugEnabled()) {
            log.debug(request.toString());
        }
        if (request.isActivated() || request.isRenewRequest()) {
            String username = request.getUsername();
            if (gamaClient.existsUser(username)) {
                log.info("Deleting Account for " + username);
                try {
                    gamaClient.deleteUser(username);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    request.appendAdminComments("Could not delete user \"" + username + "\" from the server.");
                    return;
                }
            }
            String subject = request.getAttribute("subject");
            try {
                this.vomrs.deleteMember(subject);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                request.appendAdminComments("Could not delete member \"" + subject + "\" from the server");
                return;
            }
        }
        delete(request);
    }

    public AccountRequestResult updateAccount(AccountRequest request, AccountRequest.Status nextStatus, Map params) {
        if (request.status().equals(nextStatus)) {
            log.error("This account has been already " + nextStatus.toString());
            return AccountRequestResult.error("This account has been already " + nextStatus.toString());
        }
        AccountRequestResult result = null;
        switch(nextStatus) {
            case NEW:
                result = updateAccountStatus(request, nextStatus, params);
            case RENEW:
                result = updateAccountToRenew(request, params);
                break;
            case RESET:
                result = updateAccountToReset(request, params);
                break;
            case APPROVED:
                result = updateAccountToApprove(request, params);
                break;
            case ACTIVATED:
                result = updateAccountToActivate(request, params);
                break;
            case REJECTED:
                result = updateAccountToReject(request, params);
                break;
            default:
                log.info("Unknown Status.");
                result = AccountRequestResult.error("Unknown Status.");
                break;
        }
        save(request);
        return result;
    }

    private AccountRequestResult updateAccountStatus(AccountRequest request, AccountRequest.Status nextStatus, Map params) {
        request.changeStatus(nextStatus);
        String operator = (String) params.get("operator");
        if (operator == null) {
            operator = "guest";
        }
        request.appendAdminComments("Status changed to " + nextStatus + " by " + operator);
        return AccountRequestResult.success("Request has been updated.");
    }

    private AccountRequestResult updateAccountToRenew(AccountRequest request, Map params) {
        if (!request.isActivated()) {
            request.appendAdminComments("Unexpected status " + request.status());
            return AccountRequestResult.error("Unexpected status " + request.status());
        }
        String renewPIN = (String) params.get("renewPIN");
        if (renewPIN == null) {
            return AccountRequestResult.error("PIN for renew is null");
        }
        request.setAttribute("renewPIN", renewPIN);
        String dn = buildUserDN(request);
        request.setAttribute("predictedSubject", dn);
        AccountRequestResult result = updateAccountStatus(request, AccountRequest.Status.RENEW, params);
        if (emailToAdmin) {
            try {
                accountRequestMail.sendAccountRequestRenewNotice(request);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        request.setRenewRequest();
        return result;
    }

    private AccountRequestResult updateAccountToReset(AccountRequest request, Map params) {
        if (!request.isActivated()) {
            request.appendAdminComments("Unexpected status " + request.status());
            return AccountRequestResult.error("Unexpected status " + request.status());
        }
        String pin = (String) params.get("resetPIN");
        if (pin == null) {
            return AccountRequestResult.error("PIN for reset is null");
        }
        request.setAttribute("resetPIN", pin);
        AccountRequestResult result = updateAccountStatus(request, AccountRequest.Status.RESET, params);
        if (emailToAdmin) {
            try {
                accountRequestMail.sendAccountRequestResetNotice(request);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        request.setResetRequest();
        return result;
    }

    private AccountRequestResult updateAccountToApprove(AccountRequest request, Map params) {
        if (!request.nextStatus().contains(AccountRequest.Status.APPROVED)) {
            request.appendAdminComments("Unexpected status " + request.status());
            return AccountRequestResult.error("Unexpected status " + request.status());
        }
        String emailOid = generateEmailOid();
        request.setAttribute("emailOid", emailOid);
        String activationUrl = (String) params.get("activation_url");
        try {
            accountRequestMail.sendActivationInstruction(request, activationUrl);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            request.removeAttribute("emailOid");
            return AccountRequestResult.error("Couldn't send activation mail.");
        }
        String operator = (String) params.get("operator");
        request.appendAdminComments("Emailed link");
        request.appendAdminComments("Request approved by " + operator);
        request.changeStatus(AccountRequest.Status.APPROVED);
        return new AccountRequestResult();
    }

    private static final String RNG_ALGORITHM = "SHA1PRNG";

    private static final int SIZE_OF_RANDOM_BYTES = 20;

    private String generateEmailOid() {
        try {
            SecureRandom srand = SecureRandom.getInstance(RNG_ALGORITHM);
            byte[] rnd = new byte[SIZE_OF_RANDOM_BYTES];
            srand.nextBytes(rnd);
            return toHexString(rnd);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int b1 = (int) ((bytes[i] & 0xF0) >>> 4);
            int b2 = (int) (bytes[i] & 0x0F);
            sb.append(hexChars[b1]).append(hexChars[b2]);
        }
        return sb.toString();
    }

    private AccountRequestResult updateAccountToActivate(AccountRequest request, Map params) {
        request.removeAttribute("emailOid");
        if (!request.isApproved()) {
            request.appendAdminComments(AccountRequest.Status.APPROVED + "status expected, but status is " + request.status());
            return AccountRequestResult.error("Unexpected status " + request.status());
        }
        log.info("About to activate account: " + request.getOid());
        try {
            String password = (String) params.get("password");
            activate(request, password);
            log.info("successfully activated accountrequest: " + request.getOid());
            if (notifyAdmin) {
                try {
                    accountRequestMail.sendActivationComplete(request);
                } catch (Exception e) {
                    request.appendAdminComments("Error emailing activation link to administrator.");
                }
            }
        } catch (AccountActivationException e) {
            request.appendAdminComments("Error activating account " + e.getMessage());
            if (request.isRenewRequest()) {
                log.error("Activation failed. Rollback to a renew state");
                request.changeStatus(AccountRequest.Status.RENEW);
            } else {
                log.error("Activation failed. Rollback to a new state");
                request.changeStatus(AccountRequest.Status.NEW);
            }
            return AccountRequestResult.error(ACTIVATION_ERROR_MSG);
        }
        if (request.isRenewRequest()) {
            request.clearRenewRequest();
        } else if (request.isResetRequest()) {
            request.clearResetRequest();
        }
        return new AccountRequestResult();
    }

    private AccountRequestResult updateAccountToReject(AccountRequest request, Map params) {
        String operator = (String) params.get("operator");
        try {
            accountRequestMail.sendRejectMessage(request, operator);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AccountRequestResult.error("Couldn't send reject mail.");
        }
        request.changeStatus(AccountRequest.Status.REJECTED);
        request.appendAdminComments("Request rejected by " + operator);
        return new AccountRequestResult();
    }

    public void changePassword(AccountRequest request, String current, String newPassword) throws Exception {
        gamaClient.changePassword(request.getUsername(), current, newPassword);
        request.appendAdminComments("Password was changed.");
        save(request);
    }

    public String validate(String account_type, AccountRequestAttr attr) {
        String attrName = attr.getName().toLowerCase();
        String attrValue = attr.getValue();
        if (!FormInputValidator.isAscii(attrValue)) {
            return "Contains non US-ASCII characters";
        }
        if (attrName.startsWith("phone") || attrName.endsWith("phone")) {
            if (!FormInputValidator.phoneTest(attrValue)) {
                return "Invalid phone number";
            }
        } else if (attrName.equals("emailaddress")) {
            if (!FormInputValidator.emailTest(attrValue)) {
                return "Invalid email address";
            }
            if (findDuplicateEmail(attrValue)) {
                return "That email address is already in use";
            }
        } else if (attrName.equals("username")) {
            if (findDuplicateUsername(attrValue)) {
                return "That username is already in use";
            }
            if (AccountRequest.TYPE_PASSWORD.equals(account_type)) {
                return FormInputValidator.validateUsername(attrValue);
            }
        } else if (attrName.startsWith("pinnumber")) {
            if (!FormInputValidator.pinTest(attrValue)) {
                return "Invalid PIN number";
            }
        }
        return null;
    }

    public boolean findDuplicateUsername(String username) {
        if (duplicateUsername(username) || gamaClient.existsUser(username)) {
            return true;
        }
        return false;
    }

    private boolean duplicateUsername(String username) {
        boolean foundDup = false;
        List result = this.accountRequestDao.getAccountRequestsByUsername(username);
        if (result.isEmpty()) {
            return false;
        }
        for (Iterator itr = result.iterator(); itr.hasNext(); ) {
            AccountRequest request = (AccountRequest) itr.next();
            AccountRequest.Status status = request.status();
            if ((status.equals(AccountRequest.Status.NEW)) || (status.equals(AccountRequest.Status.APPROVED))) {
                foundDup = true;
                break;
            }
        }
        return foundDup;
    }

    public boolean findDuplicateEmail(String email) {
        boolean foundDup = false;
        List result = this.accountRequestDao.getAccountRequestsByEmailAddress(email);
        if (result.isEmpty()) {
            return false;
        }
        for (Iterator itr = result.iterator(); itr.hasNext(); ) {
            AccountRequest request = (AccountRequest) itr.next();
            AccountRequest.Status status = request.status();
            if (!status.equals(AccountRequest.Status.REJECTED)) {
                foundDup = true;
                break;
            }
        }
        return foundDup;
    }

    public AccountRequestAttrCollection newAccountRequestAttrCollection(String type) {
        return AccountRequestAttrDescriptor.load(requestFormDir, type);
    }

    public void activate(AccountRequest request, String password) throws AccountActivationException {
        if (request == null) {
            throw new IllegalArgumentException("AccountRequest is NULL");
        }
        if (password == null) {
            throw new IllegalArgumentException("password is NULL");
        }
        request.appendAdminComments("Starting activation.");
        String username = request.getUsername();
        String email = request.getEmailAddress();
        Map<String, String> attrs = new HashMap<String, String>();
        attrs.put("emailAddress", email);
        String openid = request.getOpenId();
        if (!isBlank(openid)) {
            attrs.put("openId", openid);
        }
        String subject = buildUserDN(request);
        if (log.isDebugEnabled()) {
            log.debug("Requested CSR Subject: " + subject);
        }
        attrs.put("subject", subject);
        String firstName = request.getAttribute("firstName");
        if (!isBlank(firstName)) {
            attrs.put("firstName", firstName);
        }
        String lastName = request.getAttribute("lastName");
        if (!isBlank(lastName)) {
            attrs.put("lastName", lastName);
        }
        TsukubaGAMARegistryResult result = null;
        try {
            result = gamaClient.addUser(username, password, subject, attrs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AccountActivationException(e);
        }
        addNewMemberToVOMRS(request, result);
        request.removeAttribute("predictedSubject");
        request.setAttribute("subject", result.getIssuedCertificateSubject());
        request.setExpireDate(result.getExpireDate());
        request.changeStatus(AccountRequest.Status.ACTIVATED);
        request.appendAdminComments("Account activated.");
    }

    private String getInstitution(AccountRequest request) {
        String organization = request.getAttribute("organization");
        AccountRequestAttrCollection attrCollection = newAccountRequestAttrCollection(request.getAccountType());
        AccountRequestAttr attr = attrCollection.findByName("organization");
        if (attr.isTypeOfSelectBox()) {
            AccountRequestAttr.SelectOption option = attr.findOptionByValue(organization);
            if (option != null) {
                String vomrsValue = option.getVomrsValue();
                if (vomrsValue != null) {
                    return vomrsValue;
                }
            }
        }
        return organization;
    }

    private void addNewMemberToVOMRS(AccountRequest request, TsukubaGAMARegistryResult tgamaResult) {
        VOMRSAccountRequest vomrsAccountRequest = new VOMRSAccountRequest();
        vomrsAccountRequest.setAccountRequest(request);
        vomrsAccountRequest.setDn(tgamaResult.getIssuedCertificateSubject());
        vomrsAccountRequest.setCa(tgamaResult.getIssuerSubject());
        vomrsAccountRequest.setCertSn(tgamaResult.getIssuedCertificateSerial().toString());
        String institution = getInstitution(request);
        vomrsAccountRequest.setInstitution(institution);
        try {
            if (this.vomrs.addNewMember(vomrsAccountRequest)) {
                request.appendAdminComments("VOMRS Registration succeded.");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            request.appendAdminComments("Couldn't add user to VOMRS. Subject: \"" + tgamaResult.getIssuedCertificateSubject() + "\", SN: \"" + tgamaResult.getIssuedCertificateSerial().toString() + "\"");
        }
    }

    private String buildUserCN(AccountRequest request) {
        String firstName = request.getAttribute("firstName");
        String lastName = request.getAttribute("lastName");
        String userName = request.getUsername();
        String emailAddress = request.getEmailAddress();
        String result = null;
        result = firstNamePattern.matcher(requestSubjectCn).replaceAll(firstName);
        result = lastNamePattern.matcher(result).replaceAll(lastName);
        result = userNamePattern.matcher(result).replaceAll(userName);
        result = emailAddressPattern.matcher(result).replaceAll(emailAddress);
        if (log.isDebugEnabled()) {
            log.debug("Build CN: " + result);
        }
        return result;
    }

    public String buildUserDN(AccountRequest request) {
        String result = null;
        String ou = request.getAttribute("organization");
        if (ou != null) {
            String escapedOu = ou.replaceAll(",", " ");
            result = organizationPattern.matcher(requestSubjectDn).replaceAll(escapedOu);
        } else {
            result = requestSubjectDn;
        }
        StringBuffer sb = new StringBuffer(result);
        sb.append("/CN=");
        sb.append(buildUserCN(request));
        return sb.toString();
    }

    public void sendRenewalNotice(AccountRequest request, Map param) {
        log.debug("Entering sendRenewalNotice");
        String renewURL = (String) param.get("renewURL");
        try {
            accountRequestMail.sendRenewalNotice(request, renewURL);
            log.info("Sent Renewal Notice to '" + request.getEmailAddress() + "'");
            request.appendAdminComments("Sent Renewal Notice");
            save(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendActivationInstruction(AccountRequest request, Map param) {
        log.debug("Entering sendActivationInstruction");
        String emailOid = generateEmailOid();
        request.setAttribute("emailOid", emailOid);
        String activationUrl = (String) param.get("activation_url");
        try {
            accountRequestMail.sendActivationInstruction(request, activationUrl);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            request.removeAttribute("emailOid");
            return;
        }
        String operator = (String) param.get("operator");
        request.appendAdminComments("Sent Activation Instruction");
        save(request);
    }

    public AccountRequestResult cancelRenewRequest(AccountRequest request) {
        if (!request.isRenewRequest()) {
            log.error("Account is not renew request");
            return AccountRequestResult.error("Account is not renew request");
        }
        request.clearRenewRequest();
        request.removeAttribute("emailOid");
        request.removeAttribute("renewPIN");
        request.changeStatus(AccountRequest.Status.ACTIVATED);
        request.appendAdminComments("Renew request was canceled.");
        save(request);
        return AccountRequestResult.success("Renew request was successfully canceled.");
    }

    public AccountRequestResult cancelResetRequest(AccountRequest request) {
        if (!request.isResetRequest()) {
            log.error("Account is not reset request");
            return AccountRequestResult.error("Account is not reset request");
        }
        request.clearResetRequest();
        request.removeAttribute("emailOid");
        request.removeAttribute("resetPIN");
        request.changeStatus(AccountRequest.Status.ACTIVATED);
        request.appendAdminComments("Reset request was canceled.");
        save(request);
        return AccountRequestResult.success("Reset request was successfully canceled.");
    }

    public void checkValidity(Map param) {
        if (!checkCertificateValidity) {
            log.debug("Certificate validity check is disabled.");
            return;
        }
        List result = this.accountRequestDao.getExpireAccountRequest();
        if (result.isEmpty()) {
            log.info("No Account will be expired.");
            return;
        }
        try {
            if ("ADMIN".equals(sendRenewalNoticeTo)) {
                accountRequestMail.sendRenewalNoticeToAdministrator(result);
                log.info("Sent Renewal Notice to administrator.");
            } else {
                for (Iterator itr = result.iterator(); itr.hasNext(); ) {
                    AccountRequest accountRequest = (AccountRequest) itr.next();
                    sendRenewalNotice(accountRequest, param);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

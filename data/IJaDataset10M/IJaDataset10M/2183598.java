package org.jamwiki.servlets;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jamwiki.DataHandler;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.mail.WikiEmailService;
import org.jamwiki.model.WikiUser;
import org.jamwiki.model.WikiUserInfo;
import org.jamwiki.utils.Encryption;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.WikiLogger;
import org.springframework.web.servlet.ModelAndView;

public class EmailServlet extends JAMWikiServlet {

    private static final WikiLogger logger = WikiLogger.getLogger(EmailServlet.class.getName());

    private static final String JSP_NEW_PASSWORD = "new-password.jsp";

    private static final String JSP_SHOW_MESSAGE = "show-message.jsp";

    private WikiEmailService emailService;

    protected ModelAndView handleJAMWikiRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
        String newPasswordEntered = request.getParameter("newPasswordEntered");
        logger.info("NewPasswordEntered=" + newPasswordEntered);
        if (request.getParameter("newPasswordEntered") != null) {
            newPassword(request, response, next, pageInfo);
            return next;
        }
        String action = request.getParameter(WikiEmailService.PARAMETER_ACTION);
        logger.info("Parameter action=" + action);
        if (WikiEmailService.ACTION_CONFIRM_REGISTRATION.equals(action)) {
            confirmRegistration(request, response, next, pageInfo);
        } else if (WikiEmailService.ACTION_RESET_PASSWORD.equals(action)) {
            resetPassword(request, response, next, pageInfo);
        } else {
        }
        return next;
    }

    private void confirmRegistration(HttpServletRequest request, HttpServletResponse response, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
        logger.info("confirmRegistration");
        String code = request.getParameter(WikiEmailService.PARAMETER_CODE);
        logger.info("code=" + code);
        String username = request.getParameter(WikiEmailService.PARAMETER_USER);
        DataHandler handler = WikiBase.getDataHandler();
        WikiUser user = handler.lookupWikiUser(username, null);
        if (user == null) {
            throw new WikiException(new WikiMessage("email.user.notfound"));
        }
        WikiUserInfo userInfo = WikiBase.getUserHandler().lookupWikiUserInfo(username);
        String savedCode = user.getValidationCode();
        if (savedCode != null && savedCode.equals(code) && user.getEnabled() == 0) {
            user.setEnabled(1);
            user.setValidationCode("");
            handler.writeWikiUser(user, userInfo, null);
        } else {
            throw new WikiException(new WikiMessage("email.code.notvalid"));
        }
        ServletUtil.redirect(next, "en", "Special:Login");
    }

    private void resetPassword(HttpServletRequest request, HttpServletResponse response, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
        logger.info("resetPassword");
        String code = request.getParameter(WikiEmailService.PARAMETER_CODE);
        logger.info("code=" + code);
        String username = request.getParameter(WikiEmailService.PARAMETER_USER);
        logger.info("username=" + username);
        next.addObject("code", code);
        next.addObject("username", username);
        DataHandler handler = WikiBase.getDataHandler();
        WikiUser user = handler.lookupWikiUser(username, null);
        if (user == null) {
            throw new WikiException(new WikiMessage("email.user.notfound"));
        }
        WikiUserInfo userInfo = WikiBase.getUserHandler().lookupWikiUserInfo(username);
        String savedCode = user.getValidationCode();
        if (savedCode != null && savedCode.equals(code) && user.getEnabled() == 2) {
            logger.info("Code is valid");
            pageInfo.setSpecial(true);
            pageInfo.setPageTitle(new WikiMessage("email.reset.title"));
            pageInfo.setContentJsp(JSP_NEW_PASSWORD);
        } else {
            throw new WikiException(new WikiMessage("email.validation.invalid"));
        }
    }

    private void newPassword(HttpServletRequest request, HttpServletResponse response, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
        logger.info("newPassword");
        String code = request.getParameter("code");
        logger.info("code=" + code);
        String username = request.getParameter("username");
        logger.info("username=" + username);
        String newPassword = request.getParameter("newPassword");
        logger.info("newPassword=" + newPassword);
        String confirmPassword = request.getParameter("confirmPassword");
        logger.info("confirmPassword");
        DataHandler handler = WikiBase.getDataHandler();
        WikiUser user = handler.lookupWikiUser(username, null);
        if (user == null) {
            throw new WikiException(new WikiMessage("email.user.notfound"));
        }
        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            Vector errors = new Vector();
            errors.add(new WikiMessage("admin.message.passwordsnomatch"));
            next.addObject("errors", errors);
            pageInfo.setSpecial(true);
            pageInfo.setContentJsp(JSP_NEW_PASSWORD);
            pageInfo.setPageTitle(new WikiMessage("login.title"));
        } else {
            String savedCode = user.getValidationCode();
            if (savedCode != null && savedCode.equals(code) && user.getEnabled() == 2) {
                logger.info("Password reset!");
                String encPassword = Encryption.encrypt(newPassword);
                WikiUserInfo userInfo = WikiBase.getUserHandler().lookupWikiUserInfo(username);
                userInfo.setEncodedPassword(encPassword);
                user.setPassword(encPassword);
                user.setEnabled(1);
                user.setValidationCode("");
                handler.writeWikiUser(user, userInfo, null);
                next.addObject("message", "email.reset.message");
                next.addObject("items", new Object[] { "Special:Login" });
                pageInfo.setSpecial(true);
                pageInfo.setContentJsp(JSP_SHOW_MESSAGE);
                pageInfo.setPageTitle(new WikiMessage("login.title"));
            } else {
                throw new WikiException(new WikiMessage("email.validation.invalid"));
            }
        }
    }

    public WikiEmailService getEmailService() {
        return emailService;
    }

    public void setEmailService(WikiEmailService emailService) {
        this.emailService = emailService;
    }
}

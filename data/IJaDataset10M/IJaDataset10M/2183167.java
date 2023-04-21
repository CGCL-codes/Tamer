package org.commonlibrary.lcms.web.springmvc.login;

import org.apache.velocity.app.VelocityEngine;
import org.commonlibrary.lcms.security.service.UserService;
import org.commonlibrary.lcms.support.service.EmailSenderService;
import org.commonlibrary.lcms.support.spring.beans.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author Jeff Wysong
 *         Date: Sep 11, 2008
 *         Time: 2:51:13 PM
 */
@org.springframework.stereotype.Controller
@RequestMapping("/forgotpassword.spr")
public class ForgotPasswordController implements Controller {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private UserService userService;

    @Property("clv2.deployment.host")
    private String deploymentHost;

    @Property("clv2.deployment.webcontext")
    private String deploymentWebContext;

    private String hostDomain = deploymentHost + "/" + deploymentWebContext;

    public EmailSenderService getForgottenPasswordEmailSenderService() {
        return emailSenderService;
    }

    public void setForgottenPasswordEmailSenderService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setEmailSenderService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    public void setDeploymentHost(String deploymentHost) {
        this.deploymentHost = deploymentHost;
    }

    public void setDeploymentWebContext(String deploymentWebContext) {
        this.deploymentWebContext = deploymentWebContext;
    }

    public void setHostDomain(String hostDomain) {
        this.hostDomain = hostDomain;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        Locale locale = request.getLocale();
        ModelAndView mvc = new ModelAndView();
        try {
            userService.forgottenPasswordUpdate(username, locale, this.hostDomain);
        } catch (IllegalArgumentException e) {
            mvc.addObject("result", false);
        }
        mvc.setViewName("login");
        return mvc;
    }

    /**
     * Returns <tt>true</tt> if and only if email address exists in the persistence layer.
     *
     * @param emailAddress the email address to check.
     * @return <tt>true</tt> if email address exists in the system, <tt>false</tt> otherwise.
     */
    public boolean doesEmailAddressExist(String emailAddress) {
        return userService.doesEmailAddressExist(emailAddress);
    }
}

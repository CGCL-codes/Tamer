package com.jeecms.bbs.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.core.action.front.LoginAct.MESSAGE;
import static com.jeecms.core.action.front.LoginAct.PROCESS_URL;
import static com.jeecms.core.action.front.LoginAct.RETURN_URL;
import static com.jeecms.core.manager.AuthenticationMng.AUTH_KEY;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.security.BadCredentialsException;
import com.jeecms.common.security.DisabledException;
import com.jeecms.common.security.UsernameNotFoundException;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.entity.Authentication;
import com.jeecms.core.manager.AuthenticationMng;
import com.jeecms.core.web.WebErrors;
import com.octo.captcha.service.image.ImageCaptchaService;

@Controller
public class CasLoginAct {

    public static final String LOGIN_INPUT = "tpl.loginInput";

    public static final String LOGIN_STATUS = "tpl.loginStatus";

    @RequestMapping(value = "/login_cookie.jspx")
    public String login_cookie(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        CmsSite site = CmsUtils.getSite(request);
        String ip = RequestUtils.getIpAddr(request);
        try {
            Cookie c_username = CookieUtils.getCookie(request, "bbs_username");
            Cookie c_password = CookieUtils.getCookie(request, "bbs_password");
            String username = c_username.getValue();
            String password = c_password.getValue();
            Authentication auth = authMng.login(username, password, ip, request, response, session);
            bbsUserMng.updateLoginInfo(auth.getUid(), ip);
            BbsUser user = bbsUserMng.findById(auth.getUid());
            if (user.getDisabled() != null && user.getDisabled()) {
                authMng.deleteById(auth.getId());
                session.logout(request, response);
                throw new DisabledException("user disabled");
            }
            String view = getView(null, site.getUrlWhole(), auth.getId());
            if (view != null) {
                session.setAttribute(request, response, "processCookies", "true");
                return view;
            } else {
                FrontUtils.frontData(request, model, site);
                session.setAttribute(request, response, "processCookies", "false");
                return "redirect:login.jspx";
            }
        } catch (Exception e) {
            FrontUtils.frontData(request, model, site);
            session.setAttribute(request, response, "processCookies", "false");
            return getView(null, site.getUrlWhole(), null);
        }
    }

    @RequestMapping(value = "/login.jspx", method = RequestMethod.POST)
    public String submit(String username, String password, String captcha, String processUrl, String cookieType, String returnUrl, String message, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        CmsSite site = CmsUtils.getSite(request);
        String sol = site.getSolutionPath();
        WebErrors errors = validateSubmit(username, password, request, captcha, response);
        if (!errors.hasErrors()) {
            try {
                String ip = RequestUtils.getIpAddr(request);
                Authentication auth = authMng.login(username, password, ip, request, response, session);
                bbsUserMng.updateLoginInfo(auth.getUid(), ip);
                BbsUser user = bbsUserMng.findById(auth.getUid());
                if (user.getDisabled() != null && user.getDisabled()) {
                    authMng.deleteById(auth.getId());
                    session.logout(request, response);
                    throw new DisabledException("user disabled");
                }
                String view = getView(processUrl, returnUrl, auth.getId());
                if (view != null) {
                    Integer maxDate = 0;
                    if ("0".equals(cookieType)) {
                        maxDate = Integer.MAX_VALUE;
                    } else if ("1".equals(cookieType)) {
                        maxDate = 360 * 24 * 60 * 60;
                    } else if ("2".equals(cookieType)) {
                        maxDate = 30 * 24 * 60 * 60;
                    } else if ("3".equals(cookieType)) {
                        maxDate = 1 * 24 * 60 * 60;
                    }
                    CookieUtils.addCookie(request, response, "bbs_username", username, maxDate);
                    CookieUtils.addCookie(request, response, "bbs_password", password, maxDate);
                    return view;
                } else {
                    FrontUtils.frontData(request, model, site);
                    return "redirect:login.jspx";
                }
            } catch (UsernameNotFoundException e) {
                errors.addErrorString(e.getMessage());
            } catch (BadCredentialsException e) {
                errors.addErrorString(e.getMessage());
            } catch (DisabledException e) {
                errors.addErrorString(e.getMessage());
            }
        }
        errors.toModel(model);
        FrontUtils.frontData(request, model, site);
        if (!StringUtils.isBlank(processUrl)) {
            model.addAttribute(PROCESS_URL, processUrl);
        }
        if (!StringUtils.isBlank(returnUrl)) {
            model.addAttribute(RETURN_URL, returnUrl);
        }
        if (!StringUtils.isBlank(message)) {
            model.addAttribute(MESSAGE, message);
        }
        return FrontUtils.getTplPath(request, sol, TPLDIR_MEMBER, LOGIN_INPUT);
    }

    @RequestMapping(value = "/logout.jspx")
    public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String authId = (String) session.getAttribute(request, AUTH_KEY);
        if (authId != null) {
            authMng.deleteById(authId);
            session.logout(request, response);
        }
        String processUrl = RequestUtils.getQueryParam(request, PROCESS_URL);
        String returnUrl = RequestUtils.getQueryParam(request, RETURN_URL);
        String view = getView(processUrl, returnUrl, authId);
        session.setAttribute(request, response, "processCookies", "false");
        if (view != null) {
            CookieUtils.addCookie(request, response, "bbs_username", null, Integer.MAX_VALUE);
            CookieUtils.addCookie(request, response, "bbs_password", null, Integer.MAX_VALUE);
            return view;
        } else {
            return "redirect:login.jspx";
        }
    }

    private WebErrors validateSubmit(String username, String password, HttpServletRequest request, String captcha, HttpServletResponse response) {
        WebErrors errors = WebErrors.create(request);
        if (!imageCaptchaService.validateResponseForID(session.getSessionId(request, response), captcha)) {
            errors.addErrorCode("error.invalidCaptcha");
            return errors;
        }
        if (errors.ifOutOfLength(username, "username", 1, 100)) {
            return errors;
        }
        if (errors.ifOutOfLength(password, "password", 1, 32)) {
            return errors;
        }
        return errors;
    }

    /**
	 * 获得地址
	 * 
	 * @param processUrl
	 * @param returnUrl
	 * @param authId
	 * @param defaultUrl
	 * @return
	 */
    private String getView(String processUrl, String returnUrl, String authId) {
        if (!StringUtils.isBlank(processUrl)) {
            StringBuilder sb = new StringBuilder("redirect:");
            sb.append(processUrl).append("?").append(AUTH_KEY).append("=").append(authId);
            if (!StringUtils.isBlank(returnUrl)) {
                sb.append("&").append(RETURN_URL).append("=").append(returnUrl);
            }
            return sb.toString();
        } else if (!StringUtils.isBlank(returnUrl)) {
            StringBuilder sb = new StringBuilder("redirect:");
            sb.append(returnUrl);
            return sb.toString();
        } else {
            return null;
        }
    }

    @Autowired
    private BbsUserMng bbsUserMng;

    @Autowired
    private AuthenticationMng authMng;

    @Autowired
    private SessionProvider session;

    @Autowired
    private ImageCaptchaService imageCaptchaService;
}

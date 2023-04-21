package com.hk.web.pub.action;

import java.util.List;
import javax.mail.MessagingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.bean.User;
import com.hk.bean.UserFgtMail;
import com.hk.bean.UserOtherInfo;
import com.hk.bean.UserProtect;
import com.hk.frame.util.HkUtil;
import com.hk.frame.util.mail.MailUtil;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.sms.SmsClient;
import com.hk.sms2.SmsPortProcessAble;
import com.hk.svr.UserService;
import com.hk.svr.pub.Err;
import com.hk.svr.pub.HkSvrUtil;
import com.hk.svr.pub.ProtectBean;
import com.hk.svr.pub.ProtectConfig;
import com.hk.svr.user.exception.SendOutOfLimitException;
import com.hk.svr.user.validate.UserValidate;
import com.hk.web.util.HkStatus;
import com.hk.web.util.HkWebUtil;

/**
 * 用户用手机号码验证时，提示请用手机发送新密码到10669160257来修改账号密码
 * 如果用户没有设置过密码保护，直接把密码发送到注册填写的E-mail中，如果用户设置过密码保护，就到密码保护回答问题页面，尽心验证
 * 
 * @author akwei
 */
@Component("/pub/fgtpwd/fgtpwd")
public class FgtPwdAction extends BaseAction {

    @Autowired
    private UserService userService;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private SmsClient smsClient;

    private final Log log = LogFactory.getLog(FgtPwdAction.class);

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        User loginUser = this.getLoginUser(req);
        if (loginUser != null) {
            return this.getLoginForward();
        }
        return "r:/pub/fgtpwd/fgtpwd_toInputEmail.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toInputEmail(HkRequest req, HkResponse resp) throws Exception {
        User loginUser = this.getLoginUser(req);
        if (loginUser != null) {
            return this.getLoginForward();
        }
        return "/WEB-INF/page/pub/fgtpwd/inputemail.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String checkEmail(HkRequest req, HkResponse resp) throws Exception {
        User loginUser = this.getLoginUser(req);
        if (loginUser != null) {
            return this.getLoginForward();
        }
        String email = req.getString("email");
        String mobile = null;
        if (email == null) {
            req.setMessage("请输入正确的E-mail");
            return "/pub/fgtpwd/fgtpwd_toInputEmail.do";
        }
        UserOtherInfo userOtherInfo = null;
        if (email.indexOf("@") == -1) {
            mobile = email;
            userOtherInfo = this.userService.getUserOtherInfoByMobile(mobile);
        } else {
            userOtherInfo = this.userService.getUserOtherInfoByeEmail(email);
        }
        if (userOtherInfo == null) {
            req.setMessage("用户不存在,请重新输入");
            return "/pub/fgtpwd/fgtpwd_toInputEmail.do";
        }
        req.setSessionValue("fgtpwd_userId", userOtherInfo.getUserId());
        if (mobile != null) {
            SmsPortProcessAble smsPortProcessAble = (SmsPortProcessAble) HkUtil.getBean("updatePwd_smsPort");
            String number = this.smsClient.getSmsConfig().getSpNumber() + smsPortProcessAble.getBaseSmsPort();
            req.setAttribute("number", number);
            return "/WEB-INF/page/pub/fgtpwd/mopwd.jsp";
        }
        return "r:/pub/fgtpwd/fgtpwd_toInputProtect.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toInputProtect(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser(req);
        if (loginUser != null) {
            return this.getLoginForward();
        }
        Long userId = (Long) req.getSessionValue("fgtpwd_userId");
        if (userId == null) {
            return "r:/tologin.do";
        }
        if (HkSvrUtil.isNotUser(userId)) {
            return "r:/tologin.do";
        }
        List<ProtectBean> list = ProtectConfig.getProtectList();
        UserProtect userProtect = this.userService.getUserProtect(userId);
        if (userProtect == null) {
            try {
                String value = this.userService.createDedValueForFgtPwd(userId);
                String title = req.getText("func.mail.fgtpwd.mail.title");
                String content = req.getText("func.mail.fgtpwd.mail.content", value);
                req.setMessage(req.getText("func.mail.fgtpwd.mail.sendok"));
                UserOtherInfo info = this.userService.getUserOtherInfo(userId);
                this.mailUtil.sendHtmlMail(info.getEmail(), title, content);
                return "/WEB-INF/page/pub/fgtpwd/emailsendok.jsp";
            } catch (SendOutOfLimitException e) {
                req.setMessage(req.getText("func.mail.fgtpwd.mail.sendfail_limit"));
                return "/WEB-INF/page/pub/fgtpwd/emailsendok.jsp";
            } catch (MessagingException e) {
                log.warn(e.getMessage());
                req.setMessage(req.getText("func.mail.sendfail"));
                return "/pub/fgtpwd/fgtpwd_toInputProtect.do";
            }
        }
        req.setAttribute("list", list);
        req.setAttribute("userProtect", userProtect);
        return "/WEB-INF/page/pub/fgtpwd/inputprotect.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String chekProtect(HkRequest req, HkResponse resp) throws Exception {
        User loginUser = this.getLoginUser(req);
        if (loginUser != null) {
            return this.getLoginForward();
        }
        Long userId = (Long) req.getSessionValue("fgtpwd_userId");
        if (userId == null) {
            return "r:/tologin.do";
        }
        UserProtect userProtect = this.userService.getUserProtect(userId);
        if (userProtect == null) {
            return "r:/tologin.do";
        }
        String pvalue = req.getString("pvalue");
        if (pvalue.equals(userProtect.getPvalue())) {
            req.setSessionValue("protect_check_ok", true);
            return "r:/pub/fgtpwd/fgtpwd_toInputNewPwd.do";
        }
        req.setMessage("答案错误,请重新输入");
        return "/pub/fgtpwd/fgtpwd_toInputProtect.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toInputNewPwd(HkRequest req, HkResponse resp) throws Exception {
        User loginUser = this.getLoginUser(req);
        if (loginUser != null) {
            return this.getLoginForward();
        }
        Long userId = (Long) req.getSessionValue("fgtpwd_userId");
        if (userId == null) {
            return "r:/tologin.do";
        }
        Boolean check = (Boolean) req.getSessionValue("protect_check_ok");
        if (check == null) {
            return "r:/tologin.do";
        }
        return "/WEB-INF/page/pub/fgtpwd/inputnewpwd.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String saveNewPwd(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser(req);
        if (loginUser != null) {
            return this.getLoginForward();
        }
        Long userId = (Long) req.getSessionValue("fgtpwd_userId");
        if (userId == null) {
            return "r:/tologin.do";
        }
        Boolean check = (Boolean) req.getSessionValue("protect_check_ok");
        if (check == null) {
            return "r:/tologin.do";
        }
        String password = req.getString("password");
        if (UserValidate.validatePassword(password) != Err.SUCCESS) {
            req.setMessage("请输入正确的密码");
            return "/pub/fgtpwd/fgtpwd_toInputNewPwd.do";
        }
        this.userService.updateNewPwd(userId, password);
        req.setSessionMessage("密码修改成功");
        req.removeSessionvalue("fgtpwd_userId");
        req.removeSessionvalue("protect_check_ok");
        User user = this.userService.getUser(userId);
        UserOtherInfo userOtherInfo = this.userService.getUserOtherInfo(userId);
        HkStatus hkStatus = new HkStatus();
        hkStatus.setUserId(user.getUserId());
        hkStatus.setInput(userOtherInfo.getEmail());
        HkWebUtil.setHkCookie(req, resp, hkStatus);
        return "r:/tologin.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toInputNewPwd2(HkRequest req, HkResponse resp) throws Exception {
        User loginUser = this.getLoginUser(req);
        if (loginUser != null) {
            return this.getLoginForward();
        }
        String v = req.getString("v");
        UserFgtMail userFgtMail = this.userService.getUserFgtMailByDesValue(v);
        if (userFgtMail == null) {
            return "r:/tologin.do";
        }
        req.setSessionValue("v", v);
        return "/WEB-INF/page/pub/fgtpwd/inputnewpwd2.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String saveNewPwd2(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser(req);
        if (loginUser != null) {
            return this.getLoginForward();
        }
        String v = (String) req.getSessionValue("v");
        UserFgtMail userFgtMail = this.userService.getUserFgtMailByDesValue(v);
        if (userFgtMail == null) {
            return "r:/tologin.do";
        }
        long userId = userFgtMail.getUserId();
        String password = req.getString("password");
        if (UserValidate.validatePassword(password) != Err.SUCCESS) {
            req.setMessage("请输入正确的密码");
            return "/pub/fgtpwd/fgtpwd_toInputNewPwd2.do?v=" + v;
        }
        this.userService.updateNewPwd(userId, password);
        this.userService.removeUsrFgtMail(userId);
        req.setSessionMessage("密码修改成功");
        req.removeSessionvalue("fgtpwd_userId");
        req.removeSessionvalue("protect_check_ok");
        User user = this.userService.getUser(userId);
        UserOtherInfo userOtherInfo = this.userService.getUserOtherInfo(userId);
        HkStatus hkStatus = new HkStatus();
        hkStatus.setUserId(user.getUserId());
        hkStatus.setInput(userOtherInfo.getEmail());
        HkWebUtil.setHkCookie(req, resp, hkStatus);
        return "r:/tologin.do";
    }
}

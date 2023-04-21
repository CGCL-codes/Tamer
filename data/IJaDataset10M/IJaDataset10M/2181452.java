package web.epp.action;

import java.util.List;
import javax.mail.MessagingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.pub.action.EppBaseAction;
import com.hk.bean.CmpInfo;
import com.hk.bean.Company;
import com.hk.bean.User;
import com.hk.bean.UserFgtMail;
import com.hk.bean.UserOtherInfo;
import com.hk.bean.UserProtect;
import com.hk.frame.util.DataUtil;
import com.hk.frame.util.mail.MailUtil;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.UserService;
import com.hk.svr.pub.Err;
import com.hk.svr.pub.ProtectBean;
import com.hk.svr.pub.ProtectConfig;
import com.hk.svr.user.exception.SendOutOfLimitException;

@Component("/epp/web/pwd")
public class PwdAction extends EppBaseAction {

    @Autowired
    private UserService userService;

    @Autowired
    private MailUtil mailUtil;

    private String session_fgtpwd_userId = "fgtpwd_userId";

    private String session_protect_check_ok = "protect_check_ok";

    private final Log log = LogFactory.getLog(PwdAction.class);

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        Company o = (Company) req.getAttribute("o");
        if (o.getCmpflg() == 0) {
            return this.exe0(req, resp);
        }
        if (o.getCmpflg() == 2) {
            return this.exe2(req, resp);
        }
        return null;
    }

    private String exe0(HkRequest req, HkResponse resp) throws Exception {
        int tmlflg = this.getTmlflg(req);
        if (tmlflg == 0) {
            return this.exe00(req, resp);
        }
        if (tmlflg == 1) {
            return this.exe01(req, resp);
        }
        return null;
    }

    private String exe2(HkRequest req, HkResponse resp) throws Exception {
        CmpInfo cmpInfo = (CmpInfo) req.getAttribute("cmpInfo");
        if (cmpInfo.getTmlflg() == 0) {
            return this.exe20(req, resp);
        }
        return null;
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-7-14
	 */
    private String exe00(HkRequest req, HkResponse resp) throws Exception {
        long companyId = req.getLong("companyId");
        User loginUser = this.getLoginUser2(req);
        if (loginUser != null) {
            return "r:http//" + req.getServerName();
        }
        int ch = req.getInt("ch");
        if (ch == 0) {
            return this.getWebPath("pwd/inputmail.jsp");
        }
        String email = req.getString("email");
        if (DataUtil.isEmpty(email)) {
            return this.onError(req, Err.EMAIL_ERROR, "pwderror", null);
        }
        UserOtherInfo userOtherInfo = this.userService.getUserOtherInfoByeEmail(email);
        if (userOtherInfo == null) {
            return this.onError(req, Err.USER_NOT_EXIST, "pwderror", null);
        }
        long userId = userOtherInfo.getUserId();
        UserProtect userProtect = this.userService.getUserProtect(userId);
        if (userProtect == null) {
            try {
                String value = this.userService.createDedValueForFgtPwd(userId);
                Company company = (Company) req.getAttribute("o");
                String title = req.getText("epp.mail.fgtpwd.mail.title", company.getName());
                String url = "http://" + req.getServerName() + req.getContextPath() + "/epp/web/pwd_checkv.do?companyId=" + companyId + "&v=" + value;
                String content = req.getText("epp.mail.fgtpwd.mail.content", url);
                this.mailUtil.sendHtmlMail(userOtherInfo.getEmail(), title, content);
                return this.onSuccess2(req, "pwdok", null);
            } catch (SendOutOfLimitException e) {
                return this.onError(req, Err.EMAIL_SENDCOUNT_LIMIT, "pwderror", null);
            } catch (MessagingException e) {
                log.warn(e.getMessage());
                return this.onError(req, Err.EMAIL_SEND_ERROR, "pwderror", null);
            }
        }
        req.setSessionValue("fgtpwd_userId", userId);
        return this.onSuccess2(req, "pwdok2", userId);
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-7-14
	 */
    private String exe01(HkRequest req, HkResponse resp) throws Exception {
        long companyId = req.getLong("companyId");
        User loginUser = this.getLoginUser2(req);
        if (loginUser != null) {
            return "r:http//" + req.getServerName();
        }
        int ch = req.getInt("ch");
        if (ch == 0) {
            return this.getWebPath("mod/0/1/pwd/inputmail.jsp");
        }
        String email = req.getString("email");
        if (DataUtil.isEmpty(email)) {
            return this.onError(req, Err.EMAIL_ERROR, "pwderror", null);
        }
        UserOtherInfo userOtherInfo = this.userService.getUserOtherInfoByeEmail(email);
        if (userOtherInfo == null) {
            return this.onError(req, Err.USER_NOT_EXIST, "pwderror", null);
        }
        long userId = userOtherInfo.getUserId();
        UserProtect userProtect = this.userService.getUserProtect(userId);
        if (userProtect == null) {
            try {
                String value = this.userService.createDedValueForFgtPwd(userId);
                Company company = (Company) req.getAttribute("o");
                String title = req.getText("epp.mail.fgtpwd.mail.title", company.getName());
                String url = "http://" + req.getServerName() + req.getContextPath() + "/epp/web/pwd_checkv.do?companyId=" + companyId + "&v=" + value;
                String content = req.getText("epp.mail.fgtpwd.mail.content", url);
                this.mailUtil.sendHtmlMail(userOtherInfo.getEmail(), title, content);
                return this.onSuccess2(req, "pwdok", null);
            } catch (SendOutOfLimitException e) {
                return this.onError(req, Err.EMAIL_SENDCOUNT_LIMIT, "pwderror", null);
            } catch (MessagingException e) {
                log.warn(e.getMessage());
                return this.onError(req, Err.EMAIL_SEND_ERROR, "pwderror", null);
            }
        }
        req.setSessionValue("fgtpwd_userId", userId);
        return this.onSuccess2(req, "pwdok2", userId);
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-7-14
	 */
    private String exe20(HkRequest req, HkResponse resp) throws Exception {
        long companyId = req.getLong("companyId");
        User loginUser = this.getLoginUser2(req);
        if (loginUser != null) {
            return "r:http//" + req.getServerName();
        }
        int ch = req.getInt("ch");
        if (ch == 0) {
            return this.getWebPath("mod/2/0/pwd/inputmail.jsp");
        }
        String email = req.getString("email");
        if (DataUtil.isEmpty(email)) {
            return this.onError(req, Err.EMAIL_ERROR, "pwderror", null);
        }
        UserOtherInfo userOtherInfo = this.userService.getUserOtherInfoByeEmail(email);
        if (userOtherInfo == null) {
            return this.onError(req, Err.USER_NOT_EXIST, "pwderror", null);
        }
        long userId = userOtherInfo.getUserId();
        UserProtect userProtect = this.userService.getUserProtect(userId);
        if (userProtect == null) {
            try {
                String value = this.userService.createDedValueForFgtPwd(userId);
                Company company = (Company) req.getAttribute("o");
                String title = req.getText("epp.mail.fgtpwd.mail.title", company.getName());
                String url = "http://" + req.getServerName() + req.getContextPath() + "/epp/web/pwd_checkv.do?companyId=" + companyId + "&v=" + value;
                String content = req.getText("epp.mail.fgtpwd.mail.content", url);
                this.mailUtil.sendHtmlMail(userOtherInfo.getEmail(), title, content);
                return this.onSuccess2(req, "pwdok", null);
            } catch (SendOutOfLimitException e) {
                return this.onError(req, Err.EMAIL_SENDCOUNT_LIMIT, "pwderror", null);
            } catch (MessagingException e) {
                log.warn(e.getMessage());
                return this.onError(req, Err.EMAIL_SEND_ERROR, "pwderror", null);
            }
        }
        req.setSessionValue("fgtpwd_userId", userId);
        return this.onSuccess2(req, "pwdok2", userId);
    }

    /**
	 * 邮件发送成功
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String mailok(HkRequest req, HkResponse resp) throws Exception {
        Company o = (Company) req.getAttribute("o");
        if (o.getCmpflg() == 0) {
            return this.mailok0(req, resp);
        }
        if (o.getCmpflg() == 2) {
            return this.mailok2(req, resp);
        }
        return null;
    }

    /**
	 * 邮件发送成功
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String mailok0(HkRequest req, HkResponse resp) throws Exception {
        int tmlflg = this.getTmlflg(req);
        if (tmlflg == 0) {
            return this.mailok00(req, resp);
        }
        if (tmlflg == 1) {
            return this.mailok01(req, resp);
        }
        return null;
    }

    /**
	 * 邮件发送成功
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String mailok00(HkRequest req, HkResponse resp) throws Exception {
        req.reSetAttribute("email");
        return this.getWebPath("pwd/inputmailok.jsp");
    }

    /**
	 * 邮件发送成功
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String mailok01(HkRequest req, HkResponse resp) throws Exception {
        req.reSetAttribute("email");
        return this.getWebPath("mod/0/1/pwd/inputmailok.jsp");
    }

    /**
	 * 邮件发送成功
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String mailok2(HkRequest req, HkResponse resp) throws Exception {
        CmpInfo cmpInfo = (CmpInfo) req.getAttribute("cmpInfo");
        if (cmpInfo.getTmlflg() == 0) {
            return this.mailok20(req, resp);
        }
        return null;
    }

    /**
	 * 邮件发送成功
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String mailok20(HkRequest req, HkResponse resp) throws Exception {
        req.reSetAttribute("email");
        return this.getWebPath("mod/2/0/pwd/inputmailok.jsp");
    }

    /**
	 * 通验证email中的验证码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String checkv(HkRequest req, HkResponse resp) {
        int cmpflg = this.getCmpflg(req);
        if (cmpflg == 0) {
            return this.checkv0(req, resp);
        }
        if (cmpflg == 2) {
            return this.checkv2(req, resp);
        }
        return null;
    }

    /**
	 * 通验证email中的验证码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String checkv0(HkRequest req, HkResponse resp) {
        int tmlflg = this.getTmlflg(req);
        if (tmlflg == 0) {
            return this.checkv00(req, resp);
        }
        if (tmlflg == 1) {
            return this.checkv01(req, resp);
        }
        return null;
    }

    /**
	 * 通验证email中的验证码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String checkv2(HkRequest req, HkResponse resp) {
        int tmlflg = this.getTmlflg(req);
        if (tmlflg == 0) {
            return this.checkv20(req, resp);
        }
        return null;
    }

    /**
	 * 通验证email中的验证码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String checkv00(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser2(req);
        if (loginUser != null) {
            return "r:http://" + req.getServerName();
        }
        String v = req.getStringAndSetAttr("v");
        UserFgtMail userFgtMail = this.userService.getUserFgtMailByDesValue(v);
        if (userFgtMail == null) {
            return "r:http://" + req.getServerName();
        }
        return this.getWebPath("pwd/savenewpwd.jsp");
    }

    /**
	 * 通验证email中的验证码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String checkv01(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser2(req);
        if (loginUser != null) {
            return "r:http://" + req.getServerName();
        }
        String v = req.getStringAndSetAttr("v");
        UserFgtMail userFgtMail = this.userService.getUserFgtMailByDesValue(v);
        if (userFgtMail == null) {
            return "r:http://" + req.getServerName();
        }
        return this.getWebPath("pwd/savenewpwd.jsp");
    }

    /**
	 * 通验证email中的验证码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String checkv20(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser2(req);
        if (loginUser != null) {
            return "r:http://" + req.getServerName();
        }
        String v = req.getStringAndSetAttr("v");
        UserFgtMail userFgtMail = this.userService.getUserFgtMailByDesValue(v);
        if (userFgtMail == null) {
            return "r:http://" + req.getServerName();
        }
        return this.getWebPath("mod/2/0/pwd/savenewpwd.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String inputprotect(HkRequest req, HkResponse resp) {
        int cmpflg = this.getCmpflg(req);
        if (cmpflg == 0) {
            return this.inputprotect0(req, resp);
        }
        if (cmpflg == 2) {
            return this.inputprotect2(req, resp);
        }
        return null;
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String inputprotect0(HkRequest req, HkResponse resp) {
        int tml = this.getTmlflg(req);
        if (tml == 0) {
            return this.inputprotect00(req, resp);
        }
        if (tml == 1) {
            return this.inputprotect01(req, resp);
        }
        return null;
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String inputprotect2(HkRequest req, HkResponse resp) {
        int tml = this.getTmlflg(req);
        if (tml == 0) {
            return this.inputprotect20(req, resp);
        }
        return null;
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String inputprotect00(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser2(req);
        if (loginUser != null) {
            return "r:http://" + req.getServerName();
        }
        long userId = req.getLongAndSetAttr("userId");
        if (userId <= 0) {
            return "r:http://" + req.getServerName();
        }
        int ch = req.getInt("ch");
        if (ch == 0) {
            List<ProtectBean> list = ProtectConfig.getProtectList();
            UserProtect userProtect = this.userService.getUserProtect(userId);
            req.setAttribute("list", list);
            req.setAttribute("userProtect", userProtect);
            for (ProtectBean o : list) {
                if (o.getId() == userProtect.getPconfig()) {
                    req.setAttribute("question", o);
                    break;
                }
            }
            return this.getWebPath("pwd/inputprotect.jsp");
        }
        UserProtect userProtect = this.userService.getUserProtect(userId);
        if (userProtect == null) {
            return this.onError(req, Err.FGTPWD_PROTECT_ERROR, "perror", null);
        }
        String pvalue = req.getString("pvalue");
        if (pvalue != null && pvalue.equals(userProtect.getPvalue())) {
            req.setSessionValue(session_protect_check_ok, true);
            req.setSessionValue(session_fgtpwd_userId, userId);
            return this.onSuccess2(req, "pok", null);
        }
        return this.onError(req, Err.FGTPWD_PROTECT_ERROR, "perror", null);
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String inputprotect01(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser2(req);
        if (loginUser != null) {
            return "r:http://" + req.getServerName();
        }
        long userId = req.getLongAndSetAttr("userId");
        if (userId <= 0) {
            return "r:http://" + req.getServerName();
        }
        int ch = req.getInt("ch");
        if (ch == 0) {
            List<ProtectBean> list = ProtectConfig.getProtectList();
            UserProtect userProtect = this.userService.getUserProtect(userId);
            req.setAttribute("list", list);
            req.setAttribute("userProtect", userProtect);
            for (ProtectBean o : list) {
                if (o.getId() == userProtect.getPconfig()) {
                    req.setAttribute("question", o);
                    break;
                }
            }
            return this.getWebPath("mod/0/1/pwd/inputprotect.jsp");
        }
        UserProtect userProtect = this.userService.getUserProtect(userId);
        if (userProtect == null) {
            return this.onError(req, Err.FGTPWD_PROTECT_ERROR, "perror", null);
        }
        String pvalue = req.getString("pvalue");
        if (pvalue != null && pvalue.equals(userProtect.getPvalue())) {
            req.setSessionValue(session_protect_check_ok, true);
            req.setSessionValue(session_fgtpwd_userId, userId);
            return this.onSuccess2(req, "pok", null);
        }
        return this.onError(req, Err.FGTPWD_PROTECT_ERROR, "perror", null);
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String inputprotect20(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser2(req);
        if (loginUser != null) {
            return "r:http://" + req.getServerName();
        }
        long userId = req.getLongAndSetAttr("userId");
        if (userId <= 0) {
            return "r:http://" + req.getServerName();
        }
        int ch = req.getInt("ch");
        if (ch == 0) {
            List<ProtectBean> list = ProtectConfig.getProtectList();
            UserProtect userProtect = this.userService.getUserProtect(userId);
            req.setAttribute("list", list);
            req.setAttribute("userProtect", userProtect);
            for (ProtectBean o : list) {
                if (o.getId() == userProtect.getPconfig()) {
                    req.setAttribute("question", o);
                    break;
                }
            }
            return this.getWebPath("mod/2/0/pwd/inputprotect.jsp");
        }
        UserProtect userProtect = this.userService.getUserProtect(userId);
        if (userProtect == null) {
            return this.onError(req, Err.FGTPWD_PROTECT_ERROR, "perror", null);
        }
        String pvalue = req.getString("pvalue");
        if (pvalue != null && pvalue.equals(userProtect.getPvalue())) {
            req.setSessionValue(session_protect_check_ok, true);
            req.setSessionValue(session_fgtpwd_userId, userId);
            return this.onSuccess2(req, "pok", null);
        }
        return this.onError(req, Err.FGTPWD_PROTECT_ERROR, "perror", null);
    }

    /**
	 * 通过email收取验证码之后输入新的密码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String savenewpwd(HkRequest req, HkResponse resp) {
        int cmpflg = this.getCmpflg(req);
        if (cmpflg == 0) {
            return this.savenewpwd0(req, resp);
        }
        if (cmpflg == 2) {
            return this.savenewpwd2(req, resp);
        }
        return null;
    }

    /**
	 * 通过email收取验证码之后输入新的密码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String savenewpwd0(HkRequest req, HkResponse resp) {
        int tml = this.getTmlflg(req);
        if (tml == 0) {
            return this.savenewpwd00(req, resp);
        }
        if (tml == 1) {
            return this.savenewpwd01(req, resp);
        }
        return null;
    }

    /**
	 * 通过email收取验证码之后输入新的密码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String savenewpwd2(HkRequest req, HkResponse resp) {
        int tml = this.getTmlflg(req);
        if (tml == 0) {
            return this.savenewpwd20(req, resp);
        }
        return null;
    }

    /**
	 * 通过email收取验证码之后输入新的密码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String savenewpwd00(HkRequest req, HkResponse resp) {
        int ch = req.getInt("ch");
        if (ch == 0) {
            return this.getWebPath("pwd/savenewpwd.jsp");
        }
        String v = req.getString("v");
        Boolean protect_check_ok = (Boolean) req.getSessionValue(session_protect_check_ok);
        UserFgtMail userFgtMail = this.userService.getUserFgtMailByDesValue(v);
        if (userFgtMail == null && protect_check_ok == null) {
            return null;
        }
        long userId = 0;
        if (userFgtMail != null) {
            userId = userFgtMail.getUserId();
        } else {
            if (protect_check_ok != null) {
                userId = (Long) req.getSessionValue(session_fgtpwd_userId);
            }
        }
        String password = req.getString("password", "");
        int code = User.validatePassword(password);
        if (code != Err.SUCCESS) {
            return this.onError(req, code, "pwderror", null);
        }
        this.userService.updateNewPwd(userId, password);
        this.userService.removeUsrFgtMail(userId);
        req.removeSessionvalue(session_fgtpwd_userId);
        req.removeSessionvalue(session_protect_check_ok);
        req.setSessionText("func.user.updatepwdok");
        return this.onSuccess2(req, "pwdok", null);
    }

    /**
	 * 通过email收取验证码之后输入新的密码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String savenewpwd01(HkRequest req, HkResponse resp) {
        int ch = req.getInt("ch");
        if (ch == 0) {
            return this.getWebPath("mod/0/1/pwd/savenewpwd.jsp");
        }
        String v = req.getString("v");
        Boolean protect_check_ok = (Boolean) req.getSessionValue(session_protect_check_ok);
        UserFgtMail userFgtMail = this.userService.getUserFgtMailByDesValue(v);
        if (userFgtMail == null && protect_check_ok == null) {
            return null;
        }
        long userId = 0;
        if (userFgtMail != null) {
            userId = userFgtMail.getUserId();
        } else {
            if (protect_check_ok != null) {
                userId = (Long) req.getSessionValue(session_fgtpwd_userId);
            }
        }
        String password = req.getString("password", "");
        int code = User.validatePassword(password);
        if (code != Err.SUCCESS) {
            return this.onError(req, code, "pwderror", null);
        }
        this.userService.updateNewPwd(userId, password);
        this.userService.removeUsrFgtMail(userId);
        req.removeSessionvalue(session_fgtpwd_userId);
        req.removeSessionvalue(session_protect_check_ok);
        req.setSessionText("func.user.updatepwdok");
        return this.onSuccess2(req, "pwdok", null);
    }

    /**
	 * 通过email收取验证码之后输入新的密码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    private String savenewpwd20(HkRequest req, HkResponse resp) {
        int ch = req.getInt("ch");
        if (ch == 0) {
            return this.getWebPath("mod/2/0/pwd/savenewpwd.jsp");
        }
        String v = req.getString("v");
        Boolean protect_check_ok = (Boolean) req.getSessionValue(session_protect_check_ok);
        UserFgtMail userFgtMail = this.userService.getUserFgtMailByDesValue(v);
        if (userFgtMail == null && protect_check_ok == null) {
            return null;
        }
        long userId = 0;
        if (userFgtMail != null) {
            userId = userFgtMail.getUserId();
        } else {
            if (protect_check_ok != null) {
                userId = (Long) req.getSessionValue(session_fgtpwd_userId);
            }
        }
        String password = req.getString("password", "");
        int code = User.validatePassword(password);
        if (code != Err.SUCCESS) {
            return this.onError(req, code, "pwderror", null);
        }
        this.userService.updateNewPwd(userId, password);
        this.userService.removeUsrFgtMail(userId);
        req.removeSessionvalue(session_fgtpwd_userId);
        req.removeSessionvalue(session_protect_check_ok);
        req.setSessionText("func.user.updatepwdok");
        return this.onSuccess2(req, "pwdok", null);
    }
}

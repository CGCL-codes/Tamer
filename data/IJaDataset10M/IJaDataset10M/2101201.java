package sunsite.action;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Locale;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import sunsite.po.Usr;
import sunsite.service.UserService;
import sunsite.tools.Json;

/**
 *
 * @author Ruby
 */
public class GetbackPsw extends ActionSupport {

    private UserService userService;

    private ResourceBundleMessageSource messageSource;

    public static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GetbackPsw.class);

    private String userName;

    private String email;

    private String verifyCode;

    private String message;

    @Override
    public String execute() throws Exception {
        String verifyCodeSession = (String) ServletActionContext.getRequest().getSession().getAttribute(sunsite.tools.Naming.SessionName.VerifyCodeSession);
        if (verifyCode == null || !verifyCode.equals(verifyCodeSession)) {
            Json json = new Json();
            json.add(Json.Answer, Json.ModalMessageBox);
            json.add(Json.MsgTitle, messageSource.getMessage("message.errorMsgTitle", null, Locale.getDefault()));
            json.add(Json.MsgContent, messageSource.getMessage("user.checkCodeError", null, Locale.getDefault()));
            message = json.toString();
            return SUCCESS;
        }
        try {
            Usr user = userService.validateEmail(userName, email);
            if (user == null) {
                Json json = new Json();
                json.add(Json.Answer, Json.ModalMessageBox);
                json.add(Json.MsgTitle, messageSource.getMessage("message.defaultMsgTitle", null, Locale.getDefault()));
                json.add(Json.MsgContent, messageSource.getMessage("user.invalidEmail", null, Locale.getDefault()));
                message = json.toString();
                return SUCCESS;
            }
            userService.reSetPassword(user);
            Json json = new Json();
            json.add(Json.Answer, Json.Informantion);
            json.add(Json.MsgTitle, messageSource.getMessage("message.defaultMsgTitle", null, Locale.getDefault()));
            json.add(Json.MsgContent, messageSource.getMessage("message.pswReset", null, Locale.getDefault()));
            message = json.toString();
            return SUCCESS;
        } catch (Exception err) {
            logger.error("取回密码时出错：" + err);
            Json json = new Json();
            json.add(Json.Answer, Json.ModalMessageBox);
            json.add(Json.MsgTitle, messageSource.getMessage("message.errorMsgTitle", null, Locale.getDefault()));
            json.add(Json.MsgContent, messageSource.getMessage("message.mailAddError", null, Locale.getDefault()));
            message = json.toString();
        }
        return SUCCESS;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getMessage() {
        return message;
    }

    public void setMessageSource(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param verifyCode the verifyCode to set
     */
    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}

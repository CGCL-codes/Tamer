package apps.qq.action;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import apps.qq.utils.CookieUtils;
import com.opensymphony.xwork2.ActionSupport;
import connect_tx_sdk.config.QQConfig;
import connect_tx_sdk.service.TempToken;
import connect_tx_sdk.utils.ConnectUtils;

/**
 * 
 * @创建作者：hiyoucai@126.com
 * @创建时间：2011-6-17 下午04:54:47
 * @文件描述：使用腾讯登录连接
 */
@Namespace("/qq")
public class LoginAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private static final long serialVersionUID = -4256784944442943240L;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private String fromurl = "";

    private String confirm = "";

    @Action("login")
    public String execute() throws Exception {
        String appid = QQConfig.appid;
        if (appid.indexOf("your") >= 0) {
            return "qq_init_failure";
        }
        String callback = QQConfig.callback;
        String redirect_url = "http://openapi.qzone.qq.com/oauth/qzoneoauth_authorize?oauth_consumer_key=" + appid;
        TempToken token = new TempToken();
        String tokenA = token.temp();
        Map<String, String> tokens = ConnectUtils.parseTokenResult(tokenA);
        String oauth_token = tokens.get("oauth_token");
        String oauth_token_secret = tokens.get("oauth_token_secret");
        CookieUtils.setCookie(request, response, "oauth_token_secret", oauth_token_secret, -1, "/");
        redirect_url += "&oauth_token=" + oauth_token;
        redirect_url += "&oauth_callback=" + callback;
        response.sendRedirect(redirect_url);
        return null;
    }

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getFromurl() {
        return fromurl;
    }

    public void setFromurl(String fromurl) {
        this.fromurl = fromurl;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}

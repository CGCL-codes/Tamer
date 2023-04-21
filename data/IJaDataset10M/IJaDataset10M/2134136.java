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
import connect_tx_sdk.service.AccessToken;
import connect_tx_sdk.utils.ConnectUtils;
import connect_tx_sdk.utils.SecretUtils;

/**
 * 
 * @创建作者：hiyoucai@126.com
 * @创建时间：2011-6-17 下午04:54:25
 * @文件描述：腾讯认证成功callback页面
 */
@Namespace("/qq")
public class CallAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private static final long serialVersionUID = -7660991665576453595L;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private String oauth_token;

    private String openid;

    private String oauth_signature;

    private String oauth_vericode;

    private String timestamp;

    @Action("call")
    public String execute() throws Exception {
        if (!ConnectUtils.verifyOpenId(openid, timestamp, oauth_signature, QQConfig.appkey)) {
            return "qq_verify_failure";
        }
        AccessToken token = new AccessToken();
        String oauth_token_secret = CookieUtils.getCookieValue(request, "oauth_token_secret");
        String access_token = token.access(oauth_token, oauth_token_secret, oauth_vericode);
        Map<String, String> tokens = ConnectUtils.parseTokenResult(access_token);
        if (tokens.get("error_code") != null) {
            return "qq_connect_error";
        }
        if (!ConnectUtils.verifyOpenId(tokens.get("openid"), tokens.get("timestamp"), tokens.get("oauth_signature"), QQConfig.appkey)) {
            return "qq_verify_failure";
        }
        CookieUtils.setCookie(request, response, "access_token", access_token, -1, "/");
        String connectSession = openid + "-" + SecretUtils.MD5(openid);
        String is_bind_qq = CookieUtils.getCookieValue(request, "is_bind_qq");
        if ("1".equals(is_bind_qq)) {
            String redirect_url = "/qq//qq/welcome.x?connectSession=" + connectSession;
            response.sendRedirect(redirect_url);
            return null;
        }
        String redirect_url = "/qq/bind.x?connectSession=" + connectSession;
        response.sendRedirect(redirect_url);
        return null;
    }

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getOauth_token() {
        return oauth_token;
    }

    public void setOauth_token(String oauth_token) {
        this.oauth_token = oauth_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOauth_signature() {
        return oauth_signature;
    }

    public void setOauth_signature(String oauth_signature) {
        this.oauth_signature = oauth_signature;
    }

    public String getOauth_vericode() {
        return oauth_vericode;
    }

    public void setOauth_vericode(String oauth_vericode) {
        this.oauth_vericode = oauth_vericode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

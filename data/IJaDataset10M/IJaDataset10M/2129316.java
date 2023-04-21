package com.netease.tlive.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import com.netease.tlive.entity.Authorization;
import com.netease.tlive.entity.User;
import com.netease.tlive.manager.AuthorizationManager;
import com.netease.tlive.manager.UserManager;
import com.netease.tlive.util.CookieHelper;
import com.netease.tlive.util.HttpUtil;
import com.netease.tlive.util.OAuthConsumer;
import com.netease.tlive.util.PropertyHelper;

public class AuthCallbackServlet extends HttpServlet {

    private static final long serialVersionUID = -567514200867286806L;

    private static final Logger logger = Logger.getLogger(AuthCallbackServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        String request_token = req.getParameter("oauth_token");
        if (null == request_token) {
            resp.setContentType("text/plain");
            resp.getWriter().println("获取request token出现错误");
            return;
        }
        User user = UserManager.getUserByRequestToken(request_token);
        if (null == user) {
            Authorization auth = AuthorizationManager.getAuthDataByRequestToken(request_token);
            if (null == auth) {
                throw new RuntimeException("无法找到的授权信息,request_token:" + request_token + "不存在");
            } else {
                try {
                    user = createNewUserFromAuth(auth);
                } catch (Exception e) {
                    throw new RuntimeException("获取用户微博授权信息出错", e);
                }
            }
        } else {
            try {
                user = updateUser(user);
            } catch (Exception e) {
                throw new RuntimeException("更新用户微博授权信息出错", e);
            }
        }
        CookieHelper.createUserInfoCookie(req, resp, user);
        resp.sendRedirect("/mypage.jsp");
    }

    private User createNewUserFromAuth(Authorization auth) throws Exception {
        OAuthConsumer consumer = new OAuthConsumer();
        consumer.setConsumerKey(PropertyHelper.getNeteaseAppKey());
        consumer.setConsumerSecret(PropertyHelper.getNeteaseAppKeySecret());
        consumer.setRequestToken(auth.getRequestToken());
        String access_token_url = PropertyHelper.getNeteaseAccessTokenURL();
        consumer.setAccessTokenURL(access_token_url);
        String request_token_secret = auth.getRequestTokenSecret();
        String requesturl = "";
        consumer.setRequestTokenSecret(request_token_secret);
        requesturl = consumer.generateAccessTokenUrl();
        logger.log(Level.INFO, "Send request to access token url:" + requesturl);
        HttpClient httpClient = new HttpClient();
        GetMethod method = new GetMethod(requesturl);
        int stateCode = httpClient.executeMethod(method);
        if (stateCode == HttpURLConnection.HTTP_OK) {
            String responseContent = method.getResponseBodyAsString();
            Map<String, String> requestTokenResponse = HttpUtil.parseResponseMessage(responseContent);
            String oauth_token = requestTokenResponse.get("oauth_token");
            String oauth_token_secret = requestTokenResponse.get("oauth_token_secret");
            if (null != oauth_token && null != oauth_token_secret) {
                JSONObject json = getUserInfoJSONObject(oauth_token, oauth_token_secret);
                String userId = json.getString("id");
                String userName = json.getString("name");
                String userScreenName = json.getString("screen_name");
                User user = new User();
                user.setUserid(userId);
                user.setName(userName);
                user.setScreenName(userScreenName);
                user.setRequestToken(auth.getRequestToken());
                user.setRequestTokenSecret(auth.getRequestTokenSecret());
                user.setAccessToken(oauth_token);
                user.setAccessTokenSecret(oauth_token_secret);
                user = UserManager.create(user);
                AuthorizationManager.remove(auth.getUuid());
                return user;
            }
        }
        return null;
    }

    private User updateUser(User user) throws Exception {
        if (null != user && StringUtils.isNotBlank(user.getAccessToken()) && StringUtils.isNotBlank(user.getAccessTokenSecret())) {
            JSONObject json = getUserInfoJSONObject(user.getAccessToken(), user.getAccessTokenSecret());
            String userId = json.getString("id");
            String userName = json.getString("name");
            String userScreenName = json.getString("screen_name");
            user.setUserid(userId);
            user.setName(userName);
            user.setScreenName(userScreenName);
            user = UserManager.update(user);
            return user;
        } else {
            return null;
        }
    }

    private JSONObject getUserInfoJSONObject(String access_token, String access_token_secret) throws Exception {
        OAuthConsumer consumer = new OAuthConsumer();
        consumer.setConsumerKey(PropertyHelper.getNeteaseAppKey());
        consumer.setConsumerSecret(PropertyHelper.getNeteaseAppKeySecret());
        consumer.setAccessToken(access_token);
        consumer.setAccessTokenSecret(access_token_secret);
        String getserinfourl = consumer.generateAccessResourceUrl("GET", PropertyHelper.getAppSetting("t.163.api.verifyCurrentUser"), null);
        HttpClient httpClient = new HttpClient();
        GetMethod method = new GetMethod(getserinfourl);
        int stateCode = httpClient.executeMethod(method);
        if (stateCode == HttpURLConnection.HTTP_OK) {
            String getuserInfoResponseContent = method.getResponseBodyAsString();
            JSONObject json = JSONObject.fromObject(getuserInfoResponseContent);
            return json;
        } else {
            return null;
        }
    }
}

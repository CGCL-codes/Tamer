package org.light.portal.core.auth.twitter;

import static org.light.portal.util.Constants._PORTAL_INDEX;
import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.light.portal.context.Context;
import org.light.portal.core.auth.Authentication;
import org.light.portal.logger.Logger;
import org.light.portal.logger.LoggerFactory;
import org.light.portal.user.model.User;
import org.light.portal.user.model.UserProfile;
import org.light.portal.util.PropUtil;
import org.light.portal.util.StringUtil;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.RequestToken;

/**
 * 
 * @author Jianmin Liu
 **/
public class TwitterAuthentication extends Authentication {

    private static TwitterAuthentication _instance = new TwitterAuthentication();

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String ID_PREFIX = "TW_";

    public static TwitterAuthentication getInstance() {
        return _instance;
    }

    private TwitterAuthentication() {
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.sendRedirect(getURL(request, "/openId/twitter.jsp"));
    }

    public void request(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.sendRedirect(getURL(request, "/openId/twitterSync.jsp"));
    }

    public void verifyResponse(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");
        try {
            twitter.getOAuthAccessToken(requestToken, verifier);
            int id = twitter.getId();
            String name = twitter.getScreenName();
            ResponseList<twitter4j.User> users = twitter.lookupUsers(new int[] { id });
            setupUser(request, ID_PREFIX + String.valueOf(id), name);
            if (users != null && users.size() == 1) {
                twitter4j.User user = users.get(0);
                copyData(request, user);
            }
            request.getSession().removeAttribute("twitter");
            request.getSession().removeAttribute("requestToken");
        } catch (TwitterException e) {
            log.error("Twitter login failed: " + e.getMessage());
        }
        String contextPath = request.getContextPath();
        if (contextPath.indexOf(PropUtil.getString("portal.openid.url.prefix")) >= 0) contextPath = contextPath.substring(0, contextPath.indexOf(PropUtil.getString("portal.openid.url.prefix")));
        response.sendRedirect(contextPath + _PORTAL_INDEX);
    }

    public void syncResponse(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");
        try {
            twitter.getOAuthAccessToken(requestToken, verifier);
            int id = twitter.getId();
            ResponseList<twitter4j.User> users = twitter.lookupUsers(new int[] { id });
            if (users != null && users.size() == 1) {
                twitter4j.User user = users.get(0);
                copyData(request, user);
            }
            request.getSession().removeAttribute("twitter");
            request.getSession().removeAttribute("requestToken");
        } catch (TwitterException e) {
            log.error("Twitter login failed: " + e.getMessage());
        }
        String contextPath = request.getContextPath();
        if (contextPath.indexOf(PropUtil.getString("portal.openid.url.prefix")) >= 0) contextPath = contextPath.substring(0, contextPath.indexOf(PropUtil.getString("portal.openid.url.prefix")));
        response.sendRedirect(contextPath + _PORTAL_INDEX);
    }

    private String getURL(HttpServletRequest request, String callback) {
        String url = null;
        try {
            String host = request.getHeader("Host") + request.getContextPath();
            String consumerKey = org.light.portal.util.PropUtil.getString("twitter.signin.consumer.key");
            String consumerSecret = org.light.portal.util.PropUtil.getString("twitter.signin.consumer.secret");
            Twitter twitter = new TwitterFactory().getInstance();
            request.getSession().setAttribute("twitter", twitter);
            twitter.setOAuthConsumer(consumerKey, consumerSecret);
            RequestToken requestToken = twitter.getOAuthRequestToken("http://" + host + callback);
            request.getSession().setAttribute("requestToken", requestToken);
            url = requestToken.getAuthenticationURL();
        } catch (TwitterException e) {
            log.error("Twitter login failed: " + e.getMessage());
        }
        return url;
    }

    private void copyData(HttpServletRequest request, twitter4j.User tuser) {
        URL url = tuser.getProfileImageURL();
        int width = PropUtil.getInt("default.user.portrait.width");
        int height = PropUtil.getInt("default.user.portrait.height");
        try {
            java.awt.image.BufferedImage image = javax.imageio.ImageIO.read(url);
            if (image != null) {
                width = image.getWidth();
                height = image.getHeight();
            }
        } catch (Exception e) {
        }
        User user = Context.getInstance().getUser(request);
        user = this.getUserService().getUserById(user.getId());
        user.setPhotoUrl(url.toString());
        user.setPhotoWidth(width);
        user.setPhotoHeight(height);
        user.setLanguage(tuser.getLang());
        user.setTimeZone(user.getTimeZone());
        user.setCity(tuser.getLocation());
        this.getUserService().save(user);
        Context.getInstance().setUser(request, user);
        UserProfile userProfile = this.getUserService().getUserProfileById(user.getId());
        if (userProfile == null) {
            userProfile = new UserProfile(user.getId());
        }
        String firstName = tuser.getName();
        if (!StringUtil.isEmpty(firstName)) userProfile.setFirstName(firstName);
        String aboutMe = tuser.getDescription();
        if (!StringUtil.isEmpty(aboutMe)) userProfile.setAboutMe(aboutMe);
        this.getUserService().save(userProfile);
    }
}

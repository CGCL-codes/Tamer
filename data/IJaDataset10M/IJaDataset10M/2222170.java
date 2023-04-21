package com.netease.tlive.api;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import com.netease.tlive.util.OAuthConsumer;
import com.netease.tlive.util.PropertyHelper;

public class NeteaseMicroBlogAPI {

    private static final String ENCODING = "utf8";

    private static final String CONSUMER_KEY = PropertyHelper.getNeteaseAppKey();

    private static final String CONSUMER_KEY_SECRET = PropertyHelper.getNeteaseAppKeySecret();

    private static final String API_URL_PUBLISH_NEW_TWEET = PropertyHelper.getAppSetting("t.163.api.publishBlog");

    private static final String TWEET_DEFAULT_SOURCE = "网易微博第三方微博直播室";

    public static APIResult publishNewTweet(String accessToken, String accessTokenSecret, String tweetContent) {
        OAuthConsumer consumer = new OAuthConsumer();
        consumer.setConsumerKey(CONSUMER_KEY);
        consumer.setConsumerSecret(CONSUMER_KEY_SECRET);
        consumer.setAccessToken(accessToken);
        consumer.setAccessTokenSecret(accessTokenSecret);
        APIResult result = new APIResult();
        try {
            Properties param = new Properties();
            param.setProperty("status", URLEncoder.encode(tweetContent, ENCODING));
            param.setProperty("source", TWEET_DEFAULT_SOURCE);
            HttpClient httpClient = new HttpClient();
            PostMethod post = new PostMethod(API_URL_PUBLISH_NEW_TWEET);
            post.setParameter("status", URLEncoder.encode(tweetContent, ENCODING));
            post.setParameter("source", TWEET_DEFAULT_SOURCE);
            List<Header> headers = new ArrayList<Header>();
            headers.add(new Header("Authorization", consumer.generateOAuthHeader("POST", API_URL_PUBLISH_NEW_TWEET, param)));
            headers.add(new Header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
            headers.add(new Header("Content-Length", Integer.toString(post.getResponseBody().length)));
            httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
            int statusCode = httpClient.executeMethod(post);
            if (statusCode == HttpURLConnection.HTTP_OK) {
                result.setSuccess(true);
                result.setHttpResponseCode(statusCode);
                result.setHttpResponseContent(new String(post.getResponseBodyAsString()));
                return result;
            } else {
                result.setSuccess(false);
                result.setHttpResponseCode(statusCode);
                result.setHttpResponseContent(new String(post.getResponseBodyAsString()));
                return result;
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setException(e);
            return result;
        }
    }
}

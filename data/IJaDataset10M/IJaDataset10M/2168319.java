package leeon.mobile.BBSBrowser.test;

import java.util.List;
import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;

/**
 * @author hezhou
 *
 */
public class XAuthTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        Weibo weibo = new Weibo();
        try {
            String userId = "";
            String passWord = "";
            AccessToken accessToken = weibo.getXAuthAccessToken(userId, passWord, "client_auth");
            System.out.println("Got access token.");
            System.out.println("Access token: " + accessToken.getToken());
            System.out.println("Access token secret: " + accessToken.getTokenSecret());
        } catch (WeiboException e) {
            e.printStackTrace();
        }
        try {
            List<Status> friendsTimeline = weibo.getFriendsTimeline();
            StringBuilder stringBuilder = new StringBuilder("1");
            for (Status status : friendsTimeline) {
                stringBuilder.append(status.getUser().getScreenName() + "说:" + status.getText() + "\n");
                if (status.getRetweetDetails() != null) {
                    stringBuilder.append("\t\t" + status.getRetweetDetails().getRetweetingUser().getScreenName() + "说:" + status.getRetweetDetails().getRetweetText() + "\n");
                }
            }
            System.out.println(stringBuilder.toString());
        } catch (WeiboException e) {
            e.printStackTrace();
        }
    }
}

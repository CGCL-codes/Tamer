package weibo4j.examples.friendships;

import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * @author hezhou
 *
 */
public class CreateFriendship {

    /**
	 * 关注某用户 
	 * @param args
	 */
    public static void main(String[] args) {
        System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
            User user = getWeibo(true, args).createFriendship(args[2]);
            System.out.println(user.toString());
        } catch (WeiboException e) {
            e.printStackTrace();
        }
    }

    private static Weibo getWeibo(boolean isOauth, String[] args) {
        Weibo weibo = new Weibo();
        if (isOauth) {
            weibo.setToken(args[0], args[1]);
        } else {
            weibo.setUserId(args[0]);
            weibo.setPassword(args[1]);
        }
        return weibo;
    }
}

package weibo4j.examples.statuses;

import weibo4j.Status;
import weibo4j.Weibo;

/**
 * @author hezhou
 *
 */
public class Repost {

    /**
	 * 转发一条微博信息
	 * @param args
	 */
    public static void main(String[] args) {
        System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
            Weibo weibo = getWeibo(true, args);
            String sid = "";
            Thread.sleep(1000);
            Status status = weibo.repost(sid, args[2]);
            System.out.println(status.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Weibo getWeibo(boolean isOauth, String... args) {
        Weibo weibo = new Weibo();
        if (isOauth) {
            weibo.setToken("ea9751fb23b109b59ff415df99f611a8", "b71b4df1dc85c336c15fc79330ecad0b");
        } else {
            weibo.setUserId(args[0]);
            weibo.setPassword(args[1]);
        }
        return weibo;
    }
}

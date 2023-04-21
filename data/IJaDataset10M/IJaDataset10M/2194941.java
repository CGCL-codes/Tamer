package weibo4j.examples.timeline;

import java.util.List;
import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * @author hezhou
 *
 */
public class GetPublicTimeline {

    /**
	 * 获取最新更新的公共微博消息
	 * @param args
	 */
    public static void main(String[] args) {
        System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
            List<Status> statuses = getWeibo(false, args).getPublicTimeline();
            for (Status status : statuses) {
                System.out.println(status.getUser().getName() + ":" + status.getText() + ":" + status.getCreatedAt());
            }
        } catch (WeiboException e) {
            e.printStackTrace();
        }
    }

    private static Weibo getWeibo(boolean isOauth, String[] args) {
        Weibo weibo = new Weibo();
        if (isOauth) {
            weibo.setToken(args[0], args[1]);
        } else {
            weibo.setUserId("hezhou414@126.com");
            weibo.setPassword("hezhou88");
        }
        return weibo;
    }
}

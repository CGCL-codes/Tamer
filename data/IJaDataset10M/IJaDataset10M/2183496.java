package twitter4j;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Version {

    private static final String VERSION = "1.0 Beta";

    private static final String TITLE = "twittersina";

    public static String getVersion() {
        return VERSION;
    }

    public static void main(String[] args) {
        System.out.println(TITLE + " " + VERSION);
    }
}

package weibo4andriod;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Version {

    private static final String VERSION = "2.0.10";

    private static final String TITLE = "Weibo4J";

    public static String getVersion() {
        return VERSION;
    }

    public static void main(String[] args) {
        System.out.println(TITLE + " " + VERSION);
    }
}

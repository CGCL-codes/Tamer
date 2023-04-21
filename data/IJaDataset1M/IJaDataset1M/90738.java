package spider.crawl;

/**
 * Global parameters that are used by the crawlers
 *
 * @author gautam_pant
 */
public class Globals {

    public static String TOKENBREAK = "([^a-zA-Z0-9\\-]+|\\-{2,})";

    public static String eMail = "";

    public static void setMail(String mail) {
        eMail = mail;
    }
}

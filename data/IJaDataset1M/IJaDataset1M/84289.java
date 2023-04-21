package wtanaka.praya.yahoo;

/**
 * @author wtanaka
 * @version $Name:  $ $Date: 2003/12/17 01:30:15 $
 **/
public class YahooStatus {

    public static final YahooStatus YAHOO_STATUS_AVAILABLE = new YahooStatus(0);

    public static final YahooStatus YAHOO_STATUS_BRB = new YahooStatus(1);

    public static final YahooStatus YAHOO_STATUS_BUSY = new YahooStatus(2);

    public static final YahooStatus YAHOO_STATUS_NOTATHOME = new YahooStatus(3);

    public static final YahooStatus YAHOO_STATUS_NOTATDESK = new YahooStatus(4);

    public static final YahooStatus YAHOO_STATUS_NOTINOFFICE = new YahooStatus(5);

    public static final YahooStatus YAHOO_STATUS_ONPHONE = new YahooStatus(6);

    public static final YahooStatus YAHOO_STATUS_ONVACATION = new YahooStatus(7);

    public static final YahooStatus YAHOO_STATUS_OUTTOLUNCH = new YahooStatus(8);

    public static final YahooStatus YAHOO_STATUS_STEPPEDOUT = new YahooStatus(9);

    public static final YahooStatus YAHOO_STATUS_INVISIBLE = new YahooStatus(12);

    public static final YahooStatus YAHOO_STATUS_CUSTOM = new YahooStatus(99);

    public static final YahooStatus YAHOO_STATUS_IDLE = new YahooStatus(999);

    public static final YahooStatus YAHOO_STATUS_WEBLOGIN = new YahooStatus(0x5a55aa55);

    public static final YahooStatus YAHOO_STATUS_OFFLINE = new YahooStatus(0x5a55aa56);

    public static final YahooStatus YAHOO_STATUS_TYPING = new YahooStatus(0x16);

    private static final java.util.Hashtable map = new java.util.Hashtable();

    static {
        map.put(new Integer(YAHOO_STATUS_AVAILABLE.val), YAHOO_STATUS_AVAILABLE);
        map.put(new Integer(YAHOO_STATUS_BRB.val), YAHOO_STATUS_BRB);
        map.put(new Integer(YAHOO_STATUS_BUSY.val), YAHOO_STATUS_BUSY);
        map.put(new Integer(YAHOO_STATUS_NOTATHOME.val), YAHOO_STATUS_NOTATHOME);
        map.put(new Integer(YAHOO_STATUS_NOTATDESK.val), YAHOO_STATUS_NOTATDESK);
        map.put(new Integer(YAHOO_STATUS_NOTINOFFICE.val), YAHOO_STATUS_NOTINOFFICE);
        map.put(new Integer(YAHOO_STATUS_ONPHONE.val), YAHOO_STATUS_ONPHONE);
        map.put(new Integer(YAHOO_STATUS_ONVACATION.val), YAHOO_STATUS_ONVACATION);
        map.put(new Integer(YAHOO_STATUS_OUTTOLUNCH.val), YAHOO_STATUS_OUTTOLUNCH);
        map.put(new Integer(YAHOO_STATUS_STEPPEDOUT.val), YAHOO_STATUS_STEPPEDOUT);
        map.put(new Integer(YAHOO_STATUS_INVISIBLE.val), YAHOO_STATUS_INVISIBLE);
        map.put(new Integer(YAHOO_STATUS_CUSTOM.val), YAHOO_STATUS_CUSTOM);
        map.put(new Integer(YAHOO_STATUS_IDLE.val), YAHOO_STATUS_IDLE);
        map.put(new Integer(YAHOO_STATUS_WEBLOGIN.val), YAHOO_STATUS_WEBLOGIN);
        map.put(new Integer(YAHOO_STATUS_OFFLINE.val), YAHOO_STATUS_OFFLINE);
        map.put(new Integer(YAHOO_STATUS_TYPING.val), YAHOO_STATUS_TYPING);
    }

    public final int val;

    public YahooStatus(int status) {
        this.val = status;
    }

    /**
    * @param i
    * @return
    **/
    public static YahooStatus from(int i) {
        return (YahooStatus) map.get(new Integer(i));
    }
}

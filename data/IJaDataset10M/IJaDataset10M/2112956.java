package com.ivata.mask.web.browser;

/**
 * <p>
 * Just what the name says: this class contains constants used by
 * {@link Browser}.
 * </p>
 *
 * @since ivata masks 0.4 (2002-09-12)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class BrowserConstants {

    /**
     * <p>
     * Identifies any unrecognized web browser.
     * </p>
     */
    public static final Integer TYPE_UNKNOWN;

    /**
     * <p>
     * Identifies Microsoft Internet Explorer web browser.
     * </p>
     */
    public static final Integer TYPE_INTERNET_EXPLORER;

    /**
     * <p>
     * Identifies Netscape (not Mozilla) web browsers.
     * </p>
     */
    public static final Integer TYPE_NETSCAPE;

    /**
     * <p>
     * Identifies Mozilla (post Netscape code-release) web browsers.
     * </p>
     */
    public static final Integer TYPE_MOZILLA;

    /**
     * <p>
     * Identifies Opera web browser.
     * </p>
     */
    public static final Integer TYPE_OPERA;

    /**
     * <p>
     * Identifies Lynx text web browser.
     * </p>
     */
    public static final Integer TYPE_LYNX;

    /**
     * <p>
     * Identifies KDE Konqueror web browser.
     * </p>
     */
    public static final Integer TYPE_KONQUEROR;

    /**
     * <p>
     * Identifies KDE Konqueror web browser.
     * </p>
     */
    public static final Integer TYPE_GALEON;

    /**
     * <p>
     * Identifies any cyborg, robot, spider or otherwise synthetic browser.
     * </p>
     */
    public static final Integer TYPE_ROBOT;

    static {
        int counter = 0;
        TYPE_UNKNOWN = new Integer(counter++);
        TYPE_INTERNET_EXPLORER = new Integer(counter++);
        TYPE_NETSCAPE = new Integer(counter++);
        TYPE_MOZILLA = new Integer(counter++);
        TYPE_OPERA = new Integer(counter++);
        TYPE_LYNX = new Integer(counter++);
        TYPE_KONQUEROR = new Integer(counter++);
        TYPE_GALEON = new Integer(counter++);
        TYPE_ROBOT = new Integer(counter++);
    }
}

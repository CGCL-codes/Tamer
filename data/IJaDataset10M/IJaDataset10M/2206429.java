package at.ac.tuwien.webentw.g41.ue2.util;

/**
 * Klasse mit allgemeinen Utilitymethoden.
 * 
 * @author LUCNYGR
 * @since 2009-04-09
 */
public final class Utils {

    private Utils() {
        super();
    }

    /**
     * �berpr�ft, ob der �bergebene Paremeter echte Zeichen enth�lt.
     * 
     * @param text
     * @return
     */
    public static boolean hasText(String text) {
        if (text == null) {
            return false;
        }
        if (text.trim().length() > 0) {
            return true;
        }
        return false;
    }
}

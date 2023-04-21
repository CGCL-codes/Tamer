package net.infordata.em.tn5250;

/**
 * Used internally.<br>
 * It is raised if an error is detected while reading and parsing the 5250 stream.
 *
 * @version  
 * @author   Valentino Proietti - Infordata S.p.A.
 */
public class XI5250Exception extends Exception {

    private static final long serialVersionUID = 1L;

    private final int ivErrorCode;

    public XI5250Exception(String s, int errorCode) {
        super(s);
        ivErrorCode = errorCode;
    }

    public final int getErrorCode() {
        return ivErrorCode;
    }
}

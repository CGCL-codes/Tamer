package business;

/**
 * 
 * @author Jörg
 *
 */
public class CException_SpielzugUnmoeglich extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static final String MSG_DEFAULT = "Gew�nschter Zug nicht ausf�hrbar.";

    public CException_SpielzugUnmoeglich(String ss_msg) {
        super(ss_msg);
    }
}

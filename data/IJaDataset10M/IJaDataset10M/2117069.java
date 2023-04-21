package ftc;

/**
 *
 * @author Colin
 */
public class Global {

    private static Global uniqueInstance = new Global();

    public static EncodingLookup lookup = new EncodingLookup();

    ;

    /** Creates a new instance of Global */
    private Global() {
    }
}

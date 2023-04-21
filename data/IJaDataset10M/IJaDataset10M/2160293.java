package hyperocha.freenet.fcp;

/**
 * @author saces
 * @version $Id: Persistence.java 2415 2006-11-16 20:08:39Z saces $
 *
 */
public class Persistence {

    public static final Persistence CONNECTION = new Persistence("connection");

    public static final Persistence REBOOT = new Persistence("reboot");

    public static final Persistence FOREVER = new Persistence("forever");

    private String name;

    private Persistence(String name) {
        this.name = name;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}

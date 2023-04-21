package jimm.twice.ice.xml;

/**
 * A ping request.
 *
 * @author Jim Menard, <a href="mailto:jimm@io.com">jimm@io.com</a>
 */
public class Ping extends Message {

    /**
 * Constructor.
 */
    public Ping() {
        super("ice:ping");
    }
}

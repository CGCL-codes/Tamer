package gov.sns.xal.slg;

/**
 * The marker element (a thin element).
 *
 * @author  wdklotz
 * @version $Id: Marker.java 2 2006-08-17 12:20:30 +0000 (Thursday, 17 8 2006) t6p $
 */
public class Marker extends ThinElement {

    private static final String type = "marker";

    /** Creates a new instance of Marker */
    public Marker(double position, double len, String name) {
        super(name, position, 0.0);
        handleAsThick = false;
    }

    /** Creates a new instance of Marker */
    public Marker(double position, double len) {
        this(position, len, "***");
    }

    /** Creates a new instance of Marker */
    public Marker(double position) {
        this(position, 0.0);
    }

    /** Creates a new instance of Marker */
    public Marker(Double position, Double len, String name) {
        this(position.doubleValue(), len.doubleValue(), name);
    }

    /** Creates a new instance of Marker */
    public Marker(Double position, Double len) {
        this(position.doubleValue(), len.doubleValue());
    }

    /** Creates a new instance of Marker */
    public Marker(Double position) {
        this(position.doubleValue());
    }

    /**
     * Return the element type.
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * When called with a Visitor reference the implementor can either
     * reject to be visited (empty method body) or call the Visitor by
     * passing its own object reference.
     *
     *@param the Visitor which wants to visit this object.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}

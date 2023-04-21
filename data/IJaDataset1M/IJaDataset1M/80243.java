package visolate.simulator;

public class Arc extends Stroke {

    private static final String cvsid = "$Id: Arc.java,v 1.2 2004/06/30 17:26:29 vona Exp $";

    public Arc(Aperture aperture, Vertex start, Vertex end, Vertex center) {
        super(aperture, start, end);
        this.center = center;
    }

    public double getLength() {
        throw new UnsupportedOperationException("TBD Arc.getLength() unimplemented");
    }

    public double getStartForwardDirection() {
        throw new UnsupportedOperationException("TBD Arc direction unimplemented");
    }

    public double getStartReverseDirection() {
        throw new UnsupportedOperationException("TBD Arc direction unimplemented");
    }

    public double getEndForwardDirection() {
        throw new UnsupportedOperationException("TBD Arc direction unimplemented");
    }

    public double getEndReverseDirection() {
        throw new UnsupportedOperationException("TBD Arc direction unimplemented");
    }

    protected void makeBounds() {
        throw new UnsupportedOperationException("TBD Arc bounds unimplimented");
    }

    protected void makeGeometries() {
        throw new UnsupportedOperationException("TBD Arc geometry not implemented");
    }

    public String toString() {
        return "arc from " + start + " to " + end + "; center " + center + "; " + aperture;
    }

    private Vertex center;
}

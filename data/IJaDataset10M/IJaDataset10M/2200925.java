package vegetation3d.topology.core;

/**
 * PlantTopology represents topology of plant. It is actually a wrapper class,
 * that stores a root segment of entire tree of segments.
 * @author janczar
 */
public class PlantTopology {

    private Segment root;

    /**
     * Creates new PlantTopology for root segment.
     * @param root Root segment
     */
    public PlantTopology(Segment root) {
        this.root = root;
    }

    /**
     * Return root segment.
     * @return Root segment
     */
    public Segment getRoot() {
        return root;
    }
}

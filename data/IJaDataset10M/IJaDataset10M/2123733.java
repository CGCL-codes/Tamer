package databionics.hut.topmeasure;

/**
 *
 * @author lherrmann
 */
public interface RetinaPosition extends Comparable<RetinaPosition> {

    /** */
    public int row();

    /** */
    public int column();

    /** */
    public boolean equals(Object o);
}

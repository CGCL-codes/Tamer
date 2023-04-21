package ar.edu.unq.yaqc4j.randoms;

/**
 * 
 * Left side of the bell curve. Values range from 0.0 to 1.0. Values near 1.0
 * are the most probable.
 * 
 * @author Pablo
 * 
 */
public class NegativeNormalDistribution extends NormalDistribution {

    /**
     * the serialVersionUID.
     */
    private static final long serialVersionUID = -5916123064995405414L;

    /**
     * @{inheritDoc
     */
    @Override
    public final double nextRandomNumber() {
        return Math.abs(-1 + Math.abs(this.nextGausian()));
    }
}

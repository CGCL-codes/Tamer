package math.real;

import math.abstractalgebra.Group;
import math.abstractalgebra.OrderedRing;
import math.abstractalgebra.SqrtNotDefined;

/**
 * Represents arithmetic for Real Numbers.
 * @author egoff
 *
 */
public class RealRing implements OrderedRing<Double> {

    private static final long serialVersionUID = 1L;

    /** The singleton instance of Ring Real */
    public static final RealRing instance = new RealRing();

    public double abs(Double x) {
        return Math.abs(x);
    }

    public Group<Double> getAdd() {
        return RealAddGroup.instance;
    }

    public Group<Double> getMul() {
        return RealMulGroup.instance;
    }

    public boolean lessThanOrEqual(Double x, Double y) {
        return x <= y;
    }

    public Double sqrt(Double x) {
        if (x < 0) {
            throw new SqrtNotDefined();
        }
        return Math.sqrt(x);
    }
}

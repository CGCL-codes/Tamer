package maltcms.commands.distances;

import org.apache.commons.configuration.Configuration;
import ucar.ma2.Array;
import cross.Logging;
import cross.annotations.Configurable;

/**
 * @author hoffmann
 * 
 */
public class ArrayDotLp implements IArrayDoubleComp {

    private final IArrayDoubleComp dot = new ArrayDot();

    private final IArrayDoubleComp lp = new ArrayLp();

    @Configurable(name = "expansion_weight")
    private double wExp = 1.0d;

    @Configurable(name = "compression_weight")
    private double wComp = 1.0d;

    @Configurable(name = "diagonal_weight")
    private double wDiag = 1.0d;

    @Override
    public Double apply(final int i1, final int i2, final double time1, final double time2, final Array t1, final Array t2) {
        final double dotv = this.dot.apply(i1, i2, time1, time2, t1, t2);
        final Double d = this.lp.apply(i1, i2, time1, time2, t1, t2);
        return d.doubleValue() * (1.0d - dotv);
    }

    @Override
    public void configure(final Configuration cfg) {
        this.wComp = cfg.getDouble(this.getClass().getName() + ".compression_weight", 1.0d);
        this.wExp = cfg.getDouble(this.getClass().getName() + ".expansion_weight", 1.0d);
        this.wDiag = cfg.getDouble(this.getClass().getName() + ".diagonal_weight", 1.0d);
        StringBuilder sb = new StringBuilder();
        sb.append("wComp: " + this.wComp + ", ");
        sb.append("wExp: " + this.wExp + ", ");
        sb.append("wDiag: " + this.wDiag);
        Logging.getLogger(this).info("Parameters of class {}: {}", this.getClass().getName(), sb.toString());
    }

    public double getCompressionWeight() {
        return this.wComp;
    }

    public double getDiagonalWeight() {
        return this.wDiag;
    }

    public double getExpansionWeight() {
        return this.wExp;
    }

    @Override
    public boolean minimize() {
        return true;
    }
}

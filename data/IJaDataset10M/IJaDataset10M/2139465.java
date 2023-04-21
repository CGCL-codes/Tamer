package maltcms.commands.distances;

import org.apache.commons.configuration.Configuration;
import ucar.ma2.Array;
import ucar.ma2.MAVector;
import cross.Logging;
import cross.annotations.Configurable;

/**
 * Implementation of Tanimoto score.
 * 
 * @author Mathias Wilhelm(mwilhelm A T TechFak.Uni-Bielefeld.DE)
 */
public class ArrayTanimoto implements IArrayDoubleComp {

    @Configurable(name = "expansion_weight")
    private double wExp = 1.0d;

    @Configurable(name = "compression_weight")
    private double wComp = 1.0d;

    @Configurable(name = "diagonal_weight")
    private double wDiag = 1.0d;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Double apply(final int i1, final int i2, final double time1, final double time2, final Array t1, final Array t2) {
        double score = Double.MIN_VALUE;
        if ((t1.getRank() == 1) && (t2.getRank() == 1)) {
            final MAVector ma1 = new MAVector(t1);
            final MAVector ma2 = new MAVector(t2);
            final double dot = ma1.dot(ma2);
            score = dot / (ma1.dot(ma1) + ma2.dot(ma2) - dot);
        }
        return score;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public double getCompressionWeight() {
        return wComp;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public double getDiagonalWeight() {
        return wDiag;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public double getExpansionWeight() {
        return wExp;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean minimize() {
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void configure(final Configuration cfg) {
        this.wComp = cfg.getDouble(this.getClass().getName() + ".compression_weight", 1.0d);
        this.wExp = cfg.getDouble(this.getClass().getName() + ".expansion_weight", 1.0d);
        this.wDiag = cfg.getDouble(this.getClass().getName() + ".diagonal_weight", 2.25d);
        StringBuilder sb = new StringBuilder();
        sb.append("wComp: " + this.wComp + ", ");
        sb.append("wExp: " + this.wExp + ", ");
        sb.append("wDiag: " + this.wDiag);
        Logging.getLogger(this).info("Parameters of class {}: {}", this.getClass().getName(), sb.toString());
    }
}

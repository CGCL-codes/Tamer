package playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.selectors;

import java.util.Comparator;
import java.util.List;
import org.jgap.IChromosome;

/**
 * Comparator aimed at sorting chromosomes according to their distance to a given
 * chromosome.
 * @author thibautd
 */
public abstract class ChromosomeDistanceComparator implements Comparator<IChromosome> {

    private IChromosome newBorn;

    public void setComparisonData(final IChromosome newBorn, final List<IChromosome> window) {
        this.newBorn = newBorn;
    }

    /**
	 * A chromosome is "greater" than another if it is closer to the new borned.
	 * This method computes distances on the fly.
	 */
    @Override
    public int compare(final IChromosome chr1, final IChromosome chr2) {
        double d1, d2;
        if (chr1.equals(chr2)) {
            return 0;
        }
        d1 = getDistance(newBorn, chr1);
        d2 = getDistance(newBorn, chr2);
        return d1 == d2 ? 0 : (d1 > d2 ? 1 : -1);
    }

    /**
	 * Defines the distance.
	 */
    protected abstract double getDistance(IChromosome chr1, IChromosome chr2);
}

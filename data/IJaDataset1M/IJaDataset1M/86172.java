package vademecum.classifier.uknow.abstractions;

/**
 * This class extends SigEntry to an Entry for a Characterization-Matrix
 * It is extended by adding an additional datafield Interval.
 * 
 * @see Interval
 *
 */
public class CharSigEntry extends SigEntry {

    private Interval interval;

    /**
	 * Generate a new CharSigEntry
	 * @param sigvalue the significancevalue of this entry
	 * @param interval the characteristic interval of this entry
	 * @param numofAttribute the number of the according attribute
	 */
    public CharSigEntry(Double sigvalue, Interval interval, int numofAttribute) {
        super(sigvalue, numofAttribute);
        this.interval = interval;
    }

    /**
	 * Get the interval of the entry 
	 * @return the entry's interval
	 */
    public Interval getInterval() {
        return interval;
    }
}

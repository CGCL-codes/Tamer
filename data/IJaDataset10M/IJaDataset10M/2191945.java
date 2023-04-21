package net.openchrom.chromatogram.msd.filter.supplier.denoising.internal.core.support;

public class IonNoise implements Comparable<IonNoise> {

    private int ion;

    private float abundance;

    public IonNoise(int ion, float abundance) {
        this.ion = ion;
        this.abundance = abundance;
    }

    /**
	 * Returns the ion.
	 * 
	 * @return int
	 */
    public int getIon() {
        return ion;
    }

    /**
	 * Returns the abundance.
	 * 
	 * @return float
	 */
    public float getAbundance() {
        return abundance;
    }

    /**
	 * Do not remove this method, see: Denoising.calculateCorrelationFactor()
	 */
    @Override
    public int compareTo(IonNoise other) {
        int result;
        if (abundance == other.getAbundance()) {
            result = 0;
        } else {
            result = (abundance > other.getAbundance()) ? -1 : 1;
        }
        return result;
    }
}

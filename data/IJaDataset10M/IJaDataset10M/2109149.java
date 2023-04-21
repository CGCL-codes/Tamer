package eg.nileu.cis.nilestore.redundancy.port;

/**
 * The Class RedundancyParameters.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class RedundancyParameters {

    /** The k. */
    private final int k;

    /** The n. */
    private final int n;

    /**
	 * Instantiates a new redundancy parameters.
	 * 
	 * @param k
	 *            the k
	 * @param n
	 *            the n
	 */
    public RedundancyParameters(int k, int n) {
        this.k = k;
        this.n = n;
    }

    /**
	 * Gets the k.
	 * 
	 * @return the k
	 */
    public int getK() {
        return k;
    }

    /**
	 * Gets the n.
	 * 
	 * @return the n
	 */
    public int getN() {
        return n;
    }

    /**
	 * Checks if is 16 bit.
	 * 
	 * @return true, if is 16 bit
	 */
    public boolean is16Bit() {
        return this.n > 256;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + k;
        result = prime * result + n;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RedundancyParameters other = (RedundancyParameters) obj;
        if (k != other.k) return false;
        if (n != other.n) return false;
        return true;
    }
}

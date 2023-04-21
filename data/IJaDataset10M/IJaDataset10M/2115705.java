package ca.uwaterloo.crysp.otr.crypt;

/**
 * Abstract class for a DSA signing (private) or verification (public) key.
 * 
 * @author Andrew Chung (kachung@uwaterloo.ca)
 */
public abstract class DSAKey implements Key {

    /**
	 * Returns the algorithm name.
	 * 
	 * @return the algorithm name.
	 */
    public final String getAlgorithm() {
        return "DSA";
    }

    /**
	 * Returns the base, g.
	 * 
	 * @return the base, g.
	 */
    public abstract byte[] getG();

    /**
	 * Returns the prime, p.
	 * 
	 * @return the prime, p.
	 */
    public abstract byte[] getP();

    /**
	 * Returns the subprime, q.
	 * 
	 * @return the subprime, q.
	 */
    public abstract byte[] getQ();
}

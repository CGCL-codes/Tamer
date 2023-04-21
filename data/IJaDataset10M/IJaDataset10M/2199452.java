package xutools.helpers;

/**
 * General-purpose generic pair class.
 * 
 * @author Tobias Weigel
 * @date 04.11.2008
 *
 * @param <T1>
 * @param <T2>
 */
public class Pair<T1, T2> {

    /**
     * @param alpha
     * @param beta
     */
    public Pair(T1 alpha, T2 beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    private T1 alpha;

    private T2 beta;

    /**
     * @return the alpha
     */
    public T1 getAlpha() {
        return alpha;
    }

    /**
     * @param alpha the alpha to set
     */
    public void setAlpha(T1 alpha) {
        this.alpha = alpha;
    }

    /**
     * @return the beta
     */
    public T2 getBeta() {
        return beta;
    }

    /**
     * @param beta the beta to set
     */
    public void setBeta(T2 beta) {
        this.beta = beta;
    }

    @Override
    public String toString() {
        return alpha.toString() + ":" + beta.toString();
    }
}

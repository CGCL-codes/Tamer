package xutools.helpers;

/**
 * Provides weights for a WeightedChooser.
 * 
 * @author Tobias Weigel
 * @date 24.11.2008
 *
 */
public interface WeightProvider {

    /**
     * Provides a weight to be used in a WeightedChooser.
     * 
     * @return a float weight 
     */
    public float provideWeight();
}

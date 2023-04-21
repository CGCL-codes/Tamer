package genomicMap.worker;

/**
 *
 * @author stewari1
 */
public interface IncrementallyAnnealable extends Annealable {

    public double delta(int k, int l);
}

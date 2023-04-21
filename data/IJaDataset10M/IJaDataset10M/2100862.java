package xtrememp.visualization.spectrum;

/**
 * Interface for band distribution types. Band distribution refers to combining
 * band data into groups therefore reducing the number of visible bands. For
 * example, a traditional 10 band spectrum analyzer contains only 10 visible
 * frequency bands sampled from a potentially more than hundreds or more
 * frequency bands. In order to distribute the bands into only 10, several
 * different distributions can be typically be used such as log or simply linear
 * distribution.
 *
 * @see LinearBandDistribution
 * @see LogBandDistribution
 * 
 * Based on KJ-DSS project by Kristofer Fudalewski (http://sirk.sytes.net).
 *
 * @author Besmir Beqiri
 */
public interface BandDistribution {

    /**
     * @param bandCount  The desired number of visible bands.
     * @param fft        The FFT instance used for the spectrum analyser.
     * @param sampleRate The sample rate of the data to process.
     *
     * @return A band distribution table.
     */
    Band[] create(int bandCount, FFT fft, float sampleRate);
}

package edu.cmu.sphinx.frontend.frequencywarp;

/**
 * Defines the Bark frequency warping function. This class provides methods to convert frequencies from a linear scale
 * to the bark scale. The bark scale is originated from measurements of the critical bandwidth. Please find more details
 * in books about psychoacoustics or speech analysis/recognition.
 *
 * @author <a href="mailto:rsingh@cs.cmu.edu">rsingh</a>
 * @version 1.0
 */
public class FrequencyWarper {

    /**
     * Compute Bark frequency from linear frequency in Hertz.
     * The function is:
     * bark = 6.0*log(hertz/600 + sqrt((hertz/600)^2 + 1))
     *
     * @param hertz the input frequency in Hertz
     *
     * @return the frequency in a Bark scale
     *
     */
    public double hertzToBark(double hertz) {
        double x = hertz / 600;
        return (6.0 * Math.log(x + Math.sqrt(x * x + 1)));
    }

    /**
     * Compute linear frequency in Hertz from Bark frequency. The function is: hertz = 300*(exp(bark/6.0) -
     * exp(-bark/6.0))
     *
     * @param bark the input frequency in Barks
     * @return the frequency in Hertz
     */
    public double barkToHertz(double bark) {
        double x = bark / 6.0;
        return (300.0 * (Math.exp(x) - Math.exp(-x)));
    }
}

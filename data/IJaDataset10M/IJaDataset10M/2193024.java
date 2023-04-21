package uk.co.silentsoftware.core.converters.image.orderedditherstrategy;

import static uk.co.silentsoftware.config.SpectrumDefaults.SPECTRUM_COLOURS_BRIGHT;
import uk.co.silentsoftware.config.OptionsObject;
import uk.co.silentsoftware.core.colourstrategy.ColourChoiceStrategy;
import uk.co.silentsoftware.core.colourstrategy.FullColourStrategy;
import uk.co.silentsoftware.core.helpers.ColourHelper;

/**
 * Base class for applying an ordered dither strategy
 */
public abstract class AbstractOrderedDitherStrategy {

    /**
	 * Applies the sub class' dither coefficients to
	 * the given rgb matrix from an image
	 * 
	 * @param outRgb
	 * @return
	 */
    public int[] applyDither(int[] outRgb) {
        int[] coeffs = getCoefficients();
        int intensity = OptionsObject.getOrderedDitherIntensity();
        for (int i = 0; i < outRgb.length; i++) {
            int[] rgb = ColourHelper.intToRgbComponents(outRgb[i]);
            int adjustedCoeff = Math.round((float) coeffs[i] / (float) intensity);
            int oldRed = (rgb[0] + adjustedCoeff);
            int oldGreen = (rgb[1] + adjustedCoeff);
            int oldBlue = (rgb[2] + adjustedCoeff);
            ColourChoiceStrategy colourMode = OptionsObject.getColourMode();
            if (colourMode instanceof FullColourStrategy) {
                outRgb[i] = ColourHelper.getClosestSpectrumColour(oldRed, oldGreen, oldBlue);
            } else {
                outRgb[i] = ColourHelper.getMonochromeColour(oldRed, oldGreen, oldBlue, SPECTRUM_COLOURS_BRIGHT[OptionsObject.getMonochromeInkIndex()], SPECTRUM_COLOURS_BRIGHT[OptionsObject.getMonochromePaperIndex()]);
            }
        }
        return outRgb;
    }

    /**
	 * Retrieves the coefficients to apply
	 * 
	 * @return
	 */
    public abstract int[] getCoefficients();

    /**
	 * Retrieves the width of the matrix (1 dimension only)
	 * @return
	 */
    public abstract int getMatrixWidth();

    /**
	 * Retrieves the height of the matrix (1 dimension only)
	 * @return
	 */
    public abstract int getMatrixHeight();
}

package wizworld.navigate.port;

import java.io.IOException;

/** Diurnal harmonic constant height type (F) object
 * @author (c) Stephen Denham 2002
 * @version 0.1
 */
public final class DiurnalHeightHarmonicConstant extends HeightHarmonicConstant {

    /** Convert F value string to F value object
   * @param	fValue The F value string
   * @exception	IOException if invalid value
   */
    public DiurnalHeightHarmonicConstant(String fValue) throws IOException {
        super(fValue);
    }

    /** Convert F value to F value object
   * @param	f The F value
   * @exception	IOException if invalid value
   */
    public DiurnalHeightHarmonicConstant(double f) throws IOException {
        super(f);
    }
}

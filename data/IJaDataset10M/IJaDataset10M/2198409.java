package icc.lut;

import icc.tags.ICCCurveType;

/**
 * A Gamma based 16 bit lut.
 * 
 * @see jj2000.j2k.icc.tags.ICCCurveType
 * @version 1.0
 * @author Bruce A. Kern
 */
public class LookUpTable16Gamma extends LookUpTable16 {

    public LookUpTable16Gamma(ICCCurveType curve, int dwNumInput, int dwMaxOutput) {
        super(curve, dwNumInput, dwMaxOutput);
        double dfE = ICCCurveType.CurveGammaToDouble(curve.entry(0));
        for (int i = 0; i < dwNumInput; i++) lut[i] = (short) Math.floor(Math.pow((double) i / (dwNumInput - 1), dfE) * dwMaxOutput + 0.5);
    }
}

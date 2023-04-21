package domain.attributes.organ_attributes;

/**
 * This is a Service for computing allometric relationships.
 * <p>
 * All methods in this class are auxilliary and static final, though.
 *  
 * @author Uwe Grueters,	email: uwe.grueters@bot2.bio.uni-giessen.de
 * @author Roland Dahlem,	email: roland.dahlem@mni.fh-giessen.de
 */
public class AllometryService {

    /**
	 * This auxilliary method returns the y-value of an 
	 * allometric relationship.
	 * @param allometricCoefficient
	 * @param allometricConstant
	 * @param x
	 * @return y
	 */
    public static final double computeAllometry(double allometricCoefficient, double allometricConstant, double x) {
        double log10x, log10y, y;
        log10x = Math.log10(x);
        log10y = allometricCoefficient * log10x + allometricConstant;
        y = Math.pow(10.0, log10y);
        return y;
    }

    /**
	 * This auxilliary method returns the y-value of 
	 * a third-order allometry.
	 * 
	 * @param allomCoeffA
	 * @param allomCoeffB
	 * @param allomCoeffC
	 * @param allomConst
	 * @param x (diameter_cm)
	 * @return y (tissueDensityLive) 
	 */
    public static final double computeThirdOrderAllometry(double allomCoeffA, double allomCoeffB, double allomCoeffC, double allomConst, double x) {
        double log10x, log10x2, log10x3, log10y, y;
        log10x = Math.log10(x);
        log10x2 = Math.pow(log10x, 2.0);
        log10x3 = Math.pow(log10x, 3.0);
        log10y = allomCoeffA * log10x3 + allomCoeffB * log10x2 + allomCoeffC * log10x + allomConst;
        y = Math.pow(10.0, log10y);
        return y;
    }
}

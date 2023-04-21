package org.jcryptool.analysis.kegver.layer3.incubation;

import org.jcryptool.analysis.kegver.test.Polynomial;

public class _Closeness {

    /**
	 * We refer to a probability d as overwhelming in parameter l if for any polynomial poly
	 * there is some L that d>1-(1/|poly(l)|) for l > L.
	 * 
	 * @param d a probability from 0 to 1.
	 * @param l a positive parameter
	 * @param P a polynomial
	 * @return wheter d is overwhelming in l
	 */
    public static boolean isOverwhelming(double d, int l, Polynomial P) {
        if (!(0 <= d && d <= 100) && !(0 <= l)) {
            throw new IllegalArgumentException();
        }
        boolean isOverwhelming = false;
        for (int L = l; L >= 0; L--) {
            if (d > _Closeness.evaluate(L, P)) {
                isOverwhelming = true;
                break;
            }
        }
        return isOverwhelming;
    }

    /**
	 * We refer to a probability d as overwhelming in parameter l if for any polynomial poly
	 * there is some L that d>1-(1/|poly(l)|) for l > L.
	 */
    public static double evaluate(int L, Polynomial P) {
        return 1d - (1d / Math.abs(P.evaluate(L)));
    }
}

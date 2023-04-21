package shu.thesis.recover;

import shu.cms.*;
import shu.math.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author cms.shu.edu.tw
 * @version 1.0
 * @deprecated
 */
public class IlluminantSelector {

    public static void main(String[] args) {
        Spectra E = Illuminant.E.getSpectra().reduce(400, 700, 10);
        Spectra D50 = Illuminant.D50.getSpectra().reduce(400, 700, 10);
        Spectra D55 = Illuminant.D55.getSpectra().reduce(400, 700, 10);
        Spectra D75 = Illuminant.D75.getSpectra().reduce(400, 700, 10);
        Spectra D65 = Illuminant.D65.getSpectra().reduce(400, 700, 10);
        Spectra A = Illuminant.A.getSpectra().reduce(400, 700, 10);
        Spectra F2 = Illuminant.F2.getSpectra().reduce(400, 700, 10);
        Spectra F8 = Illuminant.F8.getSpectra().reduce(400, 700, 10);
        Spectra F11 = Illuminant.F11.getSpectra().reduce(400, 700, 10);
        Spectra b50 = CorrelatedColorTemperature.getSpectraOfBlackbodyRadiator(5000).reduce(400, 700, 10);
        Spectra b55 = CorrelatedColorTemperature.getSpectraOfBlackbodyRadiator(5500).reduce(400, 700, 10);
        Spectra b65 = CorrelatedColorTemperature.getSpectraOfBlackbodyRadiator(6500).reduce(400, 700, 10);
        Spectra b75 = CorrelatedColorTemperature.getSpectraOfBlackbodyRadiator(7500).reduce(400, 700, 10);
        Spectra[] ill = new Spectra[] { D50, D65, A, F2, F8, F11, D55, D75 };
        int size = ill.length;
        for (int x = 0; x < size; x++) {
            double total = 0;
            for (int y = 0; y < size; y++) {
                if (x != y) {
                    double rmsd = Maths.RMSD(ill[x].getData(), ill[y].getData());
                    total += rmsd;
                }
            }
            System.out.println(ill[x].getName() + " " + total);
        }
    }
}

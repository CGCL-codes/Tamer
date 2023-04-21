package Transmission;

import java.io.PrintWriter;
import java.util.*;

/** This class contains the constructor and methods associated with an absorption
* system object. It creates an absorption system which can then be used in
* other objects to calculate the absorption profile of the system.
* 
* 
  * Copyright 2011 Savid Stock, Chris M. Harrison and Avery Meiksin
 * Contact: aam@roe.ac.uk
 * 
 *    This file is part of IGMtrasnsmission.
 *
 *    IGMtransmission is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    IGMtransmission is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with IGMtransmission.  If not, see <http://www.gnu.org/licenses/>.
 */
public class AbsorptionSys {

    /**
	 * This constructor initialises an array of Lyman Limit Systems the number
	 * of which is drawn from a Poisson distribution with an average of
	 * <code>avgN</code>.
	 * 
	 * @param avgN
	 */
    public LyLimSys[] abssystem;

    private Random generator = new Random();

    public AbsorptionSys(double avgN, double z0, double taumax, double gamma, double beta, PrintWriter absSys) {
        int k = getNo(avgN);
        if ((avgN + 3 * Math.sqrt(avgN)) > 100) {
            System.out.println("WARNING cannot accurately produce LLSs as average number per line of sight is too large");
        }
        absSys.println("k = " + k);
        abssystem = new LyLimSys[k];
        for (int i = 0; i < k; i++) {
            abssystem[i] = new LyLimSys(z0, taumax, gamma, beta, absSys);
        }
    }

    public AbsorptionSys(double avgN, double z0, double z1, double z2, double taumax, double gamma1, double gamma2, double gamma3, double beta, PrintWriter absSys) {
        int k = getNo(avgN);
        absSys.println(k);
        abssystem = new LyLimSys[k];
        for (int i = 0; i < k; i++) {
            abssystem[i] = new LyLimSys(z0, z1, z2, taumax, gamma1, gamma2, gamma3, beta, absSys);
        }
    }

    public AbsorptionSys() {
        abssystem = new LyLimSys[1];
        abssystem[0] = new LyLimSys(0, 0);
    }

    public AbsorptionSys(int no, double[] z, double[] tau) {
        abssystem = new LyLimSys[no];
        for (int k = 0; k < no; k++) {
            abssystem[k] = new LyLimSys(z[k], tau[k]);
        }
    }

    public int getlength() {
        return this.abssystem.length;
    }

    public int getNo(double avgN) {
        int order = 100;
        if ((avgN + 3 * Math.sqrt(avgN)) > 100) {
            System.out.println("WARNING cannot accurately produce lyman limit systems as average number per line of sight is too large");
        }
        double[] factorials = new double[order];
        factorials[0] = 1;
        for (int i = 1; i <= 30; i++) {
            factorials[i] = i * factorials[i - 1];
        }
        double[] probabilities = new double[order];
        for (int j = 0; j < order; j++) {
            if (j <= 30) {
                probabilities[j] = Math.pow(avgN, j) * Math.exp(-avgN) * Math.pow(factorials[j], -1);
            } else {
                probabilities[j] = Math.exp(j - avgN) * Math.pow((avgN / j), j) / Math.sqrt(2 * Math.PI * j);
            }
        }
        double[] cumulative = new double[order];
        cumulative[0] = probabilities[0];
        for (int k = 1; k < order; k++) {
            cumulative[k] = cumulative[k - 1] + probabilities[k];
        }
        double random = generator.nextDouble();
        if (random < cumulative[0]) {
            return 0;
        }
        for (int j = 1; j < order; j++) {
            if (random > cumulative[j - 1] && random < cumulative[j]) {
                return j;
            }
        }
        return 100;
    }

    /**
	 * This method calculates the photoelectric absorption due to the LLSs for
	 * one random realization.
	 * 
	 * @return An array corresponding to wavelengths containing absorption
	 *         fractions.
	 * @throws IOException
	 */
    public double[] absorb(int urange, int lrange, double dopplervel, double spacing) {
        Photoelectric PE = new Photoelectric();
        double lambdal2 = 912 / spacing;
        int upperr = (int) ((double) urange / spacing);
        int lowerr = (int) ((double) lrange / spacing);
        double[] receive = new double[upperr];
        for (int h = 0; h < upperr; h++) {
            receive[h] = 1;
        }
        int nosys = abssystem.length;
        for (int i = 0; i < nosys; i++) {
            double taul = abssystem[i].gettau();
            double z = abssystem[i].getZ();
            double zeff = z + 1;
            for (int p = lowerr; p < upperr; p++) {
                double wavelength = p * spacing;
                if (p < (int) (lambdal2 * zeff)) {
                    receive[p] = receive[p] * PE.am(wavelength, taul, z, dopplervel);
                }
            }
        }
        return receive;
    }

    public double[] getallZ() {
        int length = this.abssystem.length;
        double[] z = new double[length];
        for (int i = 0; i < length; i++) {
            z[i] = this.abssystem[i].getZ();
        }
        return z;
    }

    public double[] getalltau() {
        int length = this.abssystem.length;
        double[] tau = new double[length];
        for (int i = 0; i < length; i++) {
            tau[i] = this.abssystem[i].gettau();
        }
        return tau;
    }
}

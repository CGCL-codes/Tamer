package org.ccpo.helper;

public class GAMutationHelper {

    public static double executeHaupt(double gmin, double gmax, int zecs, double value) {
        double result;
        result = (gmin + Math.random() * (gmax - gmin));
        return (result);
    }

    public static double executeTriangle(double gmin, double gmax, int zecs, double value) {
        double dmin = Math.min(value - gmin, gmax - value);
        dmin = Math.random() * dmin;
        return MathHelper.triangle(value - dmin, value, value + dmin);
    }

    public static double executeNatural(double gmin, double gmax, int zecs, double value, Double a) {
        if (a == null) {
            a = 1.0;
        }
        double result;
        double sigma = Math.min(value - gmin, gmax - value) / a;
        double u1 = Math.random();
        double u2 = Math.random();
        double v = Math.random();
        if (u2 == 0) u2 = 1E-20;
        if (sigma == 0) {
            if (value == gmin) {
                result = gmin + Math.min(u1, u2) * (gmax - gmin) / a;
            } else {
                result = gmax - Math.min(u1, u2) * (gmax - gmin) / a;
            }
        } else {
            double sinus, cosinus, radical;
            sinus = Math.sin(2 * Math.PI * u1);
            cosinus = Math.cos(2 * Math.PI * u1);
            radical = Math.sqrt(-2 * Math.log(u2));
            if (v < 0.5) {
                result = value + sigma * sinus * radical;
            } else {
                result = value + sigma * cosinus * radical;
            }
        }
        if (result > gmax) result = gmax;
        if (result < gmin) result = gmin;
        return result;
    }

    public static double executeNaturalX(double gmin, double gmax, int zecs, double value, Double a) {
        if (a == null) {
            a = 1.0;
        }
        double sigma;
        double v1, v2, rsquare, radical;
        double leftRange = value - gmin;
        double rightRange = gmax - value;
        if (leftRange <= rightRange) sigma = leftRange / a; else sigma = rightRange / a;
        do {
            v1 = 2 * Math.random() - 1;
            v2 = 2 * Math.random() - 1;
            rsquare = v1 * v1 + v2 * v2;
        } while (rsquare > 1.);
        radical = Math.sqrt(-2 * Math.log(rsquare) / rsquare);
        double mutant;
        if (Math.random() <= 0.5) mutant = value + sigma * v1 * radical; else mutant = value + sigma * v2 * radical;
        return mutant;
    }

    public static double executeDeb(double gmin, double gmax, int zecs, double value, Double etam) {
        if (etam == null) {
            etam = 100.0;
        }
        double result, xu, xl, delta, u, deltaq;
        xu = gmax;
        xl = gmin;
        u = Math.random();
        delta = (Math.min(value - xl, xu - value)) / (xu - xl);
        if (u <= 0.5) {
            deltaq = Math.pow(2 * u + (1 - 2 * u) * Math.pow(1 - delta, etam + 1), 1 / (etam + 1)) - 1;
        } else {
            deltaq = 1 - Math.pow(2 * (1 - u) + (2 * u - 1) * Math.pow(1 - delta, etam + 1), 1 / (etam + 1));
        }
        result = value + deltaq * (xu - xl);
        return (result);
    }

    public static double executeGray(double gmin, double gmax, int zecs, double value) {
        double range, g1;
        int gi1;
        int lungime;
        range = (gmax - gmin) / Math.pow(10, -1 * zecs);
        lungime = MathHelper.nrPozitii(range, 2);
        g1 = Math.round((value - gmin) / (gmax - gmin) * (Math.pow(2, lungime) - 1));
        int[] btg1, gtb1 = new int[lungime];
        gtb1 = MathHelper.cod(g1, 2, lungime);
        btg1 = MathHelper.binary_to_gray(gtb1, lungime);
        int k;
        k = (int) (Math.random() * lungime);
        for (int i = 0; i <= lungime - 1; i++) {
            if (i != k) {
                gtb1[i] = btg1[i];
            } else {
                if (btg1[i] == 0) gtb1[i] = 1; else gtb1[i] = 0;
            }
        }
        btg1 = MathHelper.gray_to_binary(gtb1, lungime);
        gi1 = MathHelper.nr(btg1, 2);
        g1 = gmin + gi1 * ((gmax - gmin) / (Math.pow(2, lungime) - 1));
        return (g1);
    }
}

package de.d3s.alricg.logic;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 
 * 1/20 * 1/ 20 * 19/20
 * 1/20 * 19/ 20 * 1/20
 * 19/20 * 1/ 20 * 1/20
 * 1/20 * 1/ 20 * 1/20
 * => 58/8000
 */
public class DsaTest {

    private BigDecimal divisor = BigDecimal.valueOf(8000);

    private BigDecimal minErfolgsWahrscheinlichkeit = BigDecimal.valueOf(58).divide(divisor);

    private BigDecimal maxErfolgsWahrscheinlichkeit = divisor.subtract(minErfolgsWahrscheinlichkeit).divide(divisor);

    private int eigen1, eigen2, eigen3, effektiverTaw;

    /**
	 * 
	 * @param eigen1
	 * @param eigen2
	 * @param eigen3
	 * @param taw
	 * @param modi ein NEGATIVER MODI ist eine erschwernis!
	 */
    public DsaTest(int eigen1, int eigen2, int eigen3, int taw, int modi) {
        this.eigen1 = eigen1;
        this.eigen2 = eigen2;
        this.eigen3 = eigen3;
        this.effektiverTaw = taw + modi;
    }

    public double berechneProbenWahrscheinlichkeit() {
        int tawBonus = 0;
        int eigenschaftAbzug = 0;
        if (effektiverTaw > 0) {
            tawBonus = berechneZusaetzlicheMoeglichkeitenDurchTaw();
        } else {
            eigenschaftAbzug = effektiverTaw;
        }
        BigDecimal bi = BigDecimal.valueOf(((eigen1 + eigenschaftAbzug) * (eigen2 + eigenschaftAbzug) * (eigen3 + eigenschaftAbzug)) + tawBonus).divide(divisor);
        if (bi.compareTo(minErfolgsWahrscheinlichkeit) < 0) return minErfolgsWahrscheinlichkeit.doubleValue();
        if (bi.compareTo(maxErfolgsWahrscheinlichkeit) > 0) return maxErfolgsWahrscheinlichkeit.doubleValue();
        return bi.doubleValue();
    }

    public int berechneZusaetzlicheMoeglichkeitenDurchTaw() {
        int bonus = 0;
        for (int i = 0; i <= effektiverTaw; i++) {
            if (eigen1 + i > 20) break;
            if (i != 0) bonus += berecheEig(eigen2, eigen3);
            for (int ii = 0; ii <= (effektiverTaw - i); ii++) {
                if (eigen2 + ii > 20) break;
                if (ii != 0 && i != 0) bonus += eigen3;
                if (ii != 0 && i == 0) bonus += berecheEig(eigen1, eigen3);
                for (int iii = 1; iii <= (effektiverTaw - i - ii); iii++) {
                    if (eigen3 + iii > 20) break;
                    if (ii != 0 && i != 0) bonus += 1;
                    if (ii == 0 && i == 0) bonus += berecheEig(eigen1, eigen2);
                    if (ii == 0 && i != 0) bonus += eigen2;
                    if (ii != 0 && i == 0) bonus += eigen1;
                }
            }
        }
        return bonus;
    }

    private int berecheEig(int eigA, int eigB) {
        return eigA * eigB;
    }
}

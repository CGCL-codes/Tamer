package org.shef.clef.scorer;

import java.math.BigDecimal;

public class IRMetrics {

    public static double getPrecision(int COR, int PAR, int INC, int SPU) {
        int ACT = COR + INC + PAR + SPU;
        double PRE;
        if (ACT == 0) {
            PRE = 0;
        } else {
            PRE = (COR + Config.getPartialMatchWeight() * PAR) / ACT;
        }
        return PRE;
    }

    public static double getRecall(int COR, int PAR, int INC, int MIS) {
        int POS = COR + INC + PAR + MIS;
        double REC;
        if (POS == 0) {
            REC = 0;
        } else {
            REC = (COR + Config.getPartialMatchWeight() * PAR) / POS;
        }
        return REC;
    }

    public static double getFMeasure(int COR, int PAR, int INC, int MIS, int SPU) {
        int POS = COR + INC + PAR + MIS;
        int ACT = COR + INC + PAR + SPU;
        double REC = getRecall(COR, PAR, INC, MIS);
        double PRE = getPrecision(COR, PAR, INC, SPU);
        double F;
        if (PRE == 0.0 && REC == 0.0) {
            F = 0;
        } else {
            F = (((Config.getF_beta() * 1 + 1.0) * PRE * REC) / ((Config.getF_beta() * 1 * PRE) + REC));
        }
        return F;
    }
}

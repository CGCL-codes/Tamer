package gov.nih.ncgc.batch;

/**
 *
 * @author southalln
 */
public class CurveClass {

    private boolean useMask_ = true;

    private boolean allowBell_ = true;

    private double SD_ = 10.;

    private double SDfactor_ = 3.0;

    private double robust_ = 80.;

    private double R2cutoff_ = HillConstants.R2;

    private double asymThresh_ = 0.75;

    /** Creates a new instance of CurveClass */
    public CurveClass() {
    }

    public void setUsemask(boolean useMask) {
        useMask_ = useMask;
    }

    public void setAllowBell(boolean allowBell) {
        allowBell_ = allowBell;
    }

    public void setSD(double SD) {
        SD_ = SD;
    }

    public void setSDfactor(double SDfactor) {
        SDfactor_ = SDfactor;
    }

    public void setRobust(double robust) {
        robust_ = robust;
    }

    public void setR2Cutoff(double R2cutoff) {
        R2cutoff_ = R2cutoff;
    }

    public void setAsym(double threshold) {
        asymThresh_ = threshold;
    }

    public double enzCurveClass(HillCurve hc) {
        double maxact = -Double.MAX_VALUE;
        double minact = Double.MAX_VALUE;
        int uSDf = 0, uSD = 0, dSDf = 0, dSD = 0;
        for (int i = 0; i < hc.getAct().length; i++) {
            if (!useMask_ || hc.getActMask(i)) {
                double act = hc.getAct(i);
                if (maxact < act) maxact = act;
                if (minact > act) minact = act;
                if (act > SD_) {
                    uSD++;
                    if (act > SD_ * SDfactor_) uSDf++;
                } else if (act < -1 * SD_) {
                    dSD++;
                    if (act < -1 * SD_ * SDfactor_) dSDf++;
                }
            }
        }
        int sign = -1;
        if (hc.hasCurve()) {
            if (minact < -1 * SD_ * SDfactor_ && maxact < -1 * SD_ * SDfactor_) sign = -1; else if (minact > SD_ * SDfactor_ && maxact > SD_ * SDfactor_) sign = 1; else if (hc.getZeroAct() < hc.getInfAct()) sign = 1;
        } else {
            if (maxact < SD_ * SDfactor_ && minact < -1 * SD_ * SDfactor_) sign = -1; else if (maxact > SD_ * SDfactor_ && minact > -1 * SD_ * SDfactor_) sign = 1; else if (maxact < SD_ * SDfactor_ && minact > -1 * SD_ * SDfactor_) return 4.0; else {
                if (uSDf > dSDf) sign = 1; else if (dSDf > uSDf) sign = -1; else if (uSD > dSD) sign = 1; else sign = -1;
            }
        }
        double sigval = maxact;
        int ssigpts = uSDf;
        int sigpts = uSD;
        if (sign == -1) {
            sigval = minact;
            ssigpts = dSDf;
            sigpts = dSD;
        }
        if (hc.hasCurve()) {
            if (allowBell_ && dSDf > 0 && uSDf > 0) {
                boolean hasBell = false;
                for (int i = 1; i < hc.getAct().length; i++) if (hc.getAct(i) == sigval) continue; else if (sign == 1 && hc.getAct(i) < -1 * SD_ * SDfactor_) hasBell = true; else if (sign == -1 && hc.getAct(i) > SD_ * SDfactor_) hasBell = true;
                if (hasBell) return 5.0;
            }
            int asymptote = 1;
            int pts = 0;
            for (int i = 0; i < hc.getAct().length; i++) {
                if (!useMask_ || hc.getActMask(i)) {
                    if (hc.getActConc()[i] > hc.getAC50() && Math.abs(hc.getAct(i)) > asymThresh_ * Math.abs(hc.getInfAct())) pts++;
                }
            }
            if (pts > 1) asymptote++;
            if (ssigpts == 1) {
                pts = 0;
                boolean pastSig = false;
                for (int i = 0; i < hc.getAct().length; i++) {
                    if (!useMask_ || hc.getActMask(i)) {
                        if (hc.getAct(i) == sigval) pastSig = true; else if (pastSig && (Math.abs(hc.getAct(i)) < SD_)) pts++;
                    }
                }
                if (pts > 1) return 4.0;
                pts = 0;
                boolean atsig = false;
                for (int i = 0; i < hc.getAct().length; i++) {
                    if (!useMask_ || hc.getActMask(-i - 1)) {
                        pts++;
                        if (hc.getAct(-i - 1) == sigval) atsig = true; else if (atsig) {
                            atsig = false;
                            if (hc.getAct(-i - 1) * sign < SD_ && pts < 4 && asymptote < 2) return 3.0 * sign;
                        }
                    }
                }
            }
            if (sigpts < 2 || ssigpts < 1) return 4.0; else if (asymptote == 2) {
                if (hc.getR2() > R2cutoff_) {
                    if (Math.abs(sigval) > robust_) return 1.1 * sign; else if (ssigpts < 2) return 1.4 * sign; else return 1.2 * sign;
                } else if (Math.abs(sigval) > robust_) return 1.3 * sign;
                return 1.4 * sign;
            } else {
                if (hc.getR2() > R2cutoff_) {
                    if (Math.abs(sigval) > robust_) return 2.1 * sign; else return 2.2 * sign;
                } else if (Math.abs(sigval) > robust_) return 2.3 * sign;
                return 2.4 * sign;
            }
        } else {
            if ((sigpts < 2 || ssigpts < 1) || (ssigpts == 1 && (sigval != hc.getAct(-1) || hc.getAct(-2) * sign < SD_))) {
                if (ssigpts == 1 && sigval == hc.getAct(-1)) return 3.0 * sign;
                return 4.0;
            } else if (allowBell_ && dSDf > 0 && uSDf > 0) return 5.0; else if (hc.getAct(-1) > SD_ * SDfactor_ || hc.getAct(-2) > SD_ * SDfactor_) return 5.0; else return 4.0;
        }
    }
}

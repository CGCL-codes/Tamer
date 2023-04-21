package flanagan.math;

import java.util.*;
import flanagan.math.Fmath;
import flanagan.io.FileOutput;

public class Minimisation {

    protected boolean iseOption = true;

    protected int nParam = 0;

    protected double[] paramValue = null;

    protected String[] paraName = null;

    protected double functValue = 0.0D;

    protected double lastFunctValNoCnstrnt = 0.0D;

    protected double minimum = 0.0D;

    protected int prec = 4;

    protected int field = 13;

    protected boolean convStatus = false;

    protected boolean suppressNoConvergenceMessage = false;

    protected int scaleOpt = 0;

    protected double[] scale = null;

    protected boolean penalty = false;

    protected boolean sumPenalty = false;

    protected int nConstraints = 0;

    protected int nSumConstraints = 0;

    protected int maxConstraintIndex = -1;

    protected ArrayList<Object> penalties = new ArrayList<Object>();

    protected ArrayList<Object> sumPenalties = new ArrayList<Object>();

    protected int[] penaltyCheck = null;

    protected int[] sumPenaltyCheck = null;

    protected double penaltyWeight = 1.0e30;

    protected int[] penaltyParam = null;

    protected int[][] sumPenaltyParam = null;

    protected double[][] sumPlusOrMinus = null;

    protected int[] sumPenaltyNumber = null;

    protected double[] constraints = null;

    protected double constraintTolerance = 1e-4;

    protected double[] sumConstraints = null;

    protected int constraintMethod = 0;

    protected int nMax = 3000;

    protected int nIter = 0;

    protected int konvge = 3;

    protected int kRestart = 0;

    protected double fTol = 1e-13;

    protected double rCoeff = 1.0D;

    protected double eCoeff = 2.0D;

    protected double cCoeff = 0.5D;

    protected double[] startH = null;

    protected double[] step = null;

    protected double dStep = 0.5D;

    protected int minTest = 0;

    protected double simplexSd = 0.0D;

    public Minimisation() {
        this.iseOption = true;
    }

    public void suppressNoConvergenceMessage() {
        this.suppressNoConvergenceMessage = true;
    }

    public void supressNoConvergenceMessage() {
        this.suppressNoConvergenceMessage = true;
    }

    public void nelderMead(MinimisationFunction gg, double[] start, double[] step, double fTol, int nMax) {
        Object g = (Object) gg;
        this.nelderMead(g, start, step, fTol, nMax);
    }

    public void nelderMead(MinimizationFunction gg, double[] start, double[] step, double fTol, int nMax) {
        Object g = (Object) gg;
        this.nelderMead(g, start, step, fTol, nMax);
    }

    public void nelderMead(Object g, double[] start, double[] step, double fTol, int nMax) {
        boolean testContract = false;
        int np = start.length;
        if (this.maxConstraintIndex >= np) throw new IllegalArgumentException("You have entered more constrained parameters (" + this.maxConstraintIndex + ") than minimisation parameters (" + np + ")");
        this.nParam = np;
        this.convStatus = true;
        int nnp = np + 1;
        this.lastFunctValNoCnstrnt = 0.0D;
        if (this.scaleOpt < 2) this.scale = new double[np];
        if (scaleOpt == 2 && scale.length != start.length) throw new IllegalArgumentException("scale array and initial estimate array are of different lengths");
        if (step.length != start.length) throw new IllegalArgumentException("step array length " + step.length + " and initial estimate array length " + start.length + " are of different");
        for (int i = 0; i < np; i++) if (step[i] == 0.0D) throw new IllegalArgumentException("step " + i + " size is zero");
        this.paramValue = new double[np];
        this.startH = new double[np];
        this.step = new double[np];
        double[] pmin = new double[np];
        double[][] pp = new double[nnp][nnp];
        double[] yy = new double[nnp];
        double[] pbar = new double[nnp];
        double[] pstar = new double[nnp];
        double[] p2star = new double[nnp];
        if (this.penalty) {
            Integer itemp = (Integer) this.penalties.get(1);
            this.nConstraints = itemp.intValue();
            this.penaltyParam = new int[this.nConstraints];
            this.penaltyCheck = new int[this.nConstraints];
            this.constraints = new double[this.nConstraints];
            Double dtemp = null;
            int j = 2;
            for (int i = 0; i < this.nConstraints; i++) {
                itemp = (Integer) this.penalties.get(j);
                this.penaltyParam[i] = itemp.intValue();
                j++;
                itemp = (Integer) this.penalties.get(j);
                this.penaltyCheck[i] = itemp.intValue();
                j++;
                dtemp = (Double) this.penalties.get(j);
                this.constraints[i] = dtemp.doubleValue();
                j++;
            }
        }
        if (this.sumPenalty) {
            Integer itemp = (Integer) this.sumPenalties.get(1);
            this.nSumConstraints = itemp.intValue();
            this.sumPenaltyParam = new int[this.nSumConstraints][];
            this.sumPlusOrMinus = new double[this.nSumConstraints][];
            this.sumPenaltyCheck = new int[this.nSumConstraints];
            this.sumPenaltyNumber = new int[this.nSumConstraints];
            this.sumConstraints = new double[this.nSumConstraints];
            int[] itempArray = null;
            double[] dtempArray = null;
            Double dtemp = null;
            int j = 2;
            for (int i = 0; i < this.nSumConstraints; i++) {
                itemp = (Integer) this.sumPenalties.get(j);
                this.sumPenaltyNumber[i] = itemp.intValue();
                j++;
                itempArray = (int[]) this.sumPenalties.get(j);
                this.sumPenaltyParam[i] = itempArray;
                j++;
                dtempArray = (double[]) this.sumPenalties.get(j);
                this.sumPlusOrMinus[i] = dtempArray;
                j++;
                itemp = (Integer) this.sumPenalties.get(j);
                this.sumPenaltyCheck[i] = itemp.intValue();
                j++;
                dtemp = (Double) this.sumPenalties.get(j);
                this.sumConstraints[i] = dtemp.doubleValue();
                j++;
            }
        }
        for (int i = 0; i < np; i++) this.startH[i] = start[i];
        if (this.scaleOpt > 0) {
            boolean testzero = false;
            for (int i = 0; i < np; i++) if (start[i] == 0.0D) testzero = true;
            if (testzero) {
                System.out.println("Neler and Mead Simplex: a start value of zero precludes scaling");
                System.out.println("Regression performed without scaling");
                this.scaleOpt = 0;
            }
        }
        switch(this.scaleOpt) {
            case 0:
                for (int i = 0; i < np; i++) scale[i] = 1.0D;
                break;
            case 1:
                for (int i = 0; i < np; i++) {
                    scale[i] = 1.0 / start[i];
                    step[i] = step[i] / start[i];
                    start[i] = 1.0D;
                }
                break;
            case 2:
                for (int i = 0; i < np; i++) {
                    step[i] *= scale[i];
                    start[i] *= scale[i];
                }
                break;
        }
        this.fTol = fTol;
        this.nMax = nMax;
        this.nIter = 0;
        for (int i = 0; i < np; i++) {
            this.step[i] = step[i];
            this.scale[i] = scale[i];
        }
        double sho = 0.0D;
        for (int i = 0; i < np; ++i) {
            sho = start[i];
            pstar[i] = sho;
            p2star[i] = sho;
            pmin[i] = sho;
        }
        int jcount = this.konvge;
        for (int i = 0; i < np; ++i) {
            pp[i][nnp - 1] = start[i];
        }
        yy[nnp - 1] = this.functionValue(g, start);
        for (int j = 0; j < np; ++j) {
            start[j] = start[j] + step[j];
            for (int i = 0; i < np; ++i) pp[i][j] = start[i];
            yy[j] = this.functionValue(g, start);
            start[j] = start[j] - step[j];
        }
        double ynewlo = 0.0D;
        double ystar = 0.0D;
        double y2star = 0.0D;
        double ylo = 0.0D;
        double fMin;
        double curMin = 00D, sumnm = 0.0D, summnm = 0.0D, zn = 0.0D;
        int ilo = 0;
        int ihi = 0;
        int ln = 0;
        boolean test = true;
        while (test) {
            ylo = yy[0];
            ynewlo = ylo;
            ilo = 0;
            ihi = 0;
            for (int i = 1; i < nnp; ++i) {
                if (yy[i] < ylo) {
                    ylo = yy[i];
                    ilo = i;
                }
                if (yy[i] > ynewlo) {
                    ynewlo = yy[i];
                    ihi = i;
                }
            }
            for (int i = 0; i < np; ++i) {
                zn = 0.0D;
                for (int j = 0; j < nnp; ++j) {
                    zn += pp[i][j];
                }
                zn -= pp[i][ihi];
                pbar[i] = zn / np;
            }
            for (int i = 0; i < np; ++i) pstar[i] = (1.0 + this.rCoeff) * pbar[i] - this.rCoeff * pp[i][ihi];
            ystar = this.functionValue(g, pstar);
            ++this.nIter;
            if (ystar < ylo) {
                for (int i = 0; i < np; ++i) p2star[i] = pstar[i] * (1.0D + this.eCoeff) - this.eCoeff * pbar[i];
                y2star = this.functionValue(g, p2star);
                ++this.nIter;
                if (y2star < ylo) {
                    for (int i = 0; i < np; ++i) pp[i][ihi] = p2star[i];
                    yy[ihi] = y2star;
                } else {
                    for (int i = 0; i < np; ++i) pp[i][ihi] = pstar[i];
                    yy[ihi] = ystar;
                }
            } else {
                ln = 0;
                for (int i = 0; i < nnp; ++i) if (i != ihi && ystar > yy[i]) ++ln;
                if (ln == np) {
                    if (ystar <= yy[ihi]) {
                        for (int i = 0; i < np; ++i) pp[i][ihi] = pstar[i];
                        yy[ihi] = ystar;
                    }
                    for (int i = 0; i < np; ++i) p2star[i] = this.cCoeff * pp[i][ihi] + (1.0 - this.cCoeff) * pbar[i];
                    y2star = this.functionValue(g, p2star);
                    ++this.nIter;
                    if (y2star > yy[ihi]) {
                        for (int j = 0; j < nnp; ++j) {
                            for (int i = 0; i < np; ++i) {
                                pp[i][j] = 0.5 * (pp[i][j] + pp[i][ilo]);
                                pmin[i] = pp[i][j];
                            }
                            yy[j] = this.functionValue(g, pmin);
                        }
                        this.nIter += nnp;
                    } else {
                        for (int i = 0; i < np; ++i) pp[i][ihi] = p2star[i];
                        yy[ihi] = y2star;
                    }
                } else {
                    for (int i = 0; i < np; ++i) pp[i][ihi] = pstar[i];
                    yy[ihi] = ystar;
                }
            }
            sumnm = 0.0;
            ynewlo = yy[0];
            ilo = 0;
            for (int i = 0; i < nnp; ++i) {
                sumnm += yy[i];
                if (ynewlo > yy[i]) {
                    ynewlo = yy[i];
                    ilo = i;
                }
            }
            sumnm /= (double) (nnp);
            summnm = 0.0;
            for (int i = 0; i < nnp; ++i) {
                zn = yy[i] - sumnm;
                summnm += zn * zn;
            }
            curMin = Math.sqrt(summnm / np);
            switch(this.minTest) {
                case 0:
                    if (curMin < fTol) test = false;
                    break;
            }
            this.minimum = ynewlo;
            if (!test) {
                for (int i = 0; i < np; ++i) pmin[i] = pp[i][ilo];
                yy[nnp - 1] = ynewlo;
                this.simplexSd = curMin;
                --jcount;
                if (jcount > 0) {
                    test = true;
                    for (int j = 0; j < np; ++j) {
                        pmin[j] = pmin[j] + step[j];
                        for (int i = 0; i < np; ++i) pp[i][j] = pmin[i];
                        yy[j] = this.functionValue(g, pmin);
                        pmin[j] = pmin[j] - step[j];
                    }
                }
            }
            if (test && this.nIter > this.nMax) {
                if (!this.suppressNoConvergenceMessage) {
                    System.out.println("Maximum iteration number reached, in Minimisation.simplex(...)");
                    System.out.println("without the convergence criterion being satisfied");
                    System.out.println("Current parameter estimates and function value returned");
                }
                this.convStatus = false;
                for (int i = 0; i < np; ++i) pmin[i] = pp[i][ilo];
                yy[nnp - 1] = ynewlo;
                test = false;
            }
        }
        for (int i = 0; i < np; ++i) {
            pmin[i] = pp[i][ilo];
            paramValue[i] = pmin[i] / this.scale[i];
        }
        this.minimum = ynewlo;
        this.kRestart = this.konvge - jcount;
    }

    public void nelderMead(MinimisationFunction g, double[] start, double[] step, double fTol) {
        int nMaxx = this.nMax;
        this.nelderMead(g, start, step, fTol, nMaxx);
    }

    public void nelderMead(MinimizationFunction g, double[] start, double[] step, double fTol) {
        int nMaxx = this.nMax;
        this.nelderMead(g, start, step, fTol, nMaxx);
    }

    public void nelderMead(MinimisationFunction g, double[] start, double[] step, int nMax) {
        double fToll = this.fTol;
        this.nelderMead(g, start, step, fToll, nMax);
    }

    public void nelderMead(MinimizationFunction g, double[] start, double[] step, int nMax) {
        double fToll = this.fTol;
        this.nelderMead(g, start, step, fToll, nMax);
    }

    public void nelderMead(MinimisationFunction g, double[] start, double[] step) {
        double fToll = this.fTol;
        int nMaxx = this.nMax;
        this.nelderMead(g, start, step, fToll, nMaxx);
    }

    public void nelderMead(MinimizationFunction g, double[] start, double[] step) {
        double fToll = this.fTol;
        int nMaxx = this.nMax;
        this.nelderMead(g, start, step, fToll, nMaxx);
    }

    public void nelderMead(MinimisationFunction g, double[] start, double fTol, int nMax) {
        int n = start.length;
        double[] stepp = new double[n];
        for (int i = 0; i < n; i++) stepp[i] = this.dStep * start[i];
        this.nelderMead(g, start, stepp, fTol, nMax);
    }

    public void nelderMead(MinimizationFunction g, double[] start, double fTol, int nMax) {
        int n = start.length;
        double[] stepp = new double[n];
        for (int i = 0; i < n; i++) stepp[i] = this.dStep * start[i];
        this.nelderMead(g, start, stepp, fTol, nMax);
    }

    public void nelderMead(MinimisationFunction g, double[] start, double fTol) {
        int n = start.length;
        int nMaxx = this.nMax;
        double[] stepp = new double[n];
        for (int i = 0; i < n; i++) stepp[i] = this.dStep * start[i];
        this.nelderMead(g, start, stepp, fTol, nMaxx);
    }

    public void nelderMead(MinimizationFunction g, double[] start, double fTol) {
        int n = start.length;
        int nMaxx = this.nMax;
        double[] stepp = new double[n];
        for (int i = 0; i < n; i++) stepp[i] = this.dStep * start[i];
        this.nelderMead(g, start, stepp, fTol, nMaxx);
    }

    public void nelderMead(MinimisationFunction g, double[] start, int nMax) {
        int n = start.length;
        double fToll = this.fTol;
        double[] stepp = new double[n];
        for (int i = 0; i < n; i++) stepp[i] = this.dStep * start[i];
        this.nelderMead(g, start, stepp, fToll, nMax);
    }

    public void nelderMead(MinimizationFunction g, double[] start, int nMax) {
        int n = start.length;
        double fToll = this.fTol;
        double[] stepp = new double[n];
        for (int i = 0; i < n; i++) stepp[i] = this.dStep * start[i];
        this.nelderMead(g, start, stepp, fToll, nMax);
    }

    public void nelderMead(MinimisationFunction g, double[] start) {
        int n = start.length;
        int nMaxx = this.nMax;
        double fToll = this.fTol;
        double[] stepp = new double[n];
        for (int i = 0; i < n; i++) stepp[i] = this.dStep * start[i];
        this.nelderMead(g, start, stepp, fToll, nMaxx);
    }

    public void nelderMead(MinimizationFunction g, double[] start) {
        int n = start.length;
        int nMaxx = this.nMax;
        double fToll = this.fTol;
        double[] stepp = new double[n];
        for (int i = 0; i < n; i++) stepp[i] = this.dStep * start[i];
        this.nelderMead(g, start, stepp, fToll, nMaxx);
    }

    protected double functionValue(Object g, double[] x) {
        if (this.iseOption) {
            return functionValue((MinimisationFunction) g, x);
        } else {
            return functionValue((MinimizationFunction) g, x);
        }
    }

    protected double functionValue(MinimisationFunction g, double[] x) {
        double funcVal = -3.0D;
        double[] param = new double[this.nParam];
        for (int i = 0; i < this.nParam; i++) param[i] = x[i] / scale[i];
        double tempFunctVal = this.lastFunctValNoCnstrnt;
        boolean test = true;
        if (this.penalty) {
            int k = 0;
            for (int i = 0; i < this.nConstraints; i++) {
                k = this.penaltyParam[i];
                switch(penaltyCheck[i]) {
                    case -1:
                        if (param[k] < constraints[i]) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(constraints[i] - param[k]);
                            test = false;
                        }
                        break;
                    case 0:
                        if (param[k] < constraints[i] * (1.0 - this.constraintTolerance)) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(constraints[i] * (1.0 - this.constraintTolerance) - param[k]);
                            test = false;
                        }
                        if (param[k] > constraints[i] * (1.0 + this.constraintTolerance)) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(param[k] - constraints[i] * (1.0 + this.constraintTolerance));
                            test = false;
                        }
                        break;
                    case 1:
                        if (param[k] > constraints[i]) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(param[k] - constraints[i]);
                            test = false;
                        }
                        break;
                }
            }
        }
        if (this.sumPenalty) {
            int kk = 0;
            double pSign = 0;
            double sumPenaltySum = 0.0D;
            for (int i = 0; i < this.nSumConstraints; i++) {
                for (int j = 0; j < this.sumPenaltyNumber[i]; j++) {
                    kk = this.sumPenaltyParam[i][j];
                    pSign = this.sumPlusOrMinus[i][j];
                    sumPenaltySum += param[kk] * pSign;
                }
                switch(this.sumPenaltyCheck[i]) {
                    case -1:
                        if (sumPenaltySum < sumConstraints[i]) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(sumConstraints[i] - sumPenaltySum);
                            test = false;
                        }
                        break;
                    case 0:
                        if (sumPenaltySum < sumConstraints[i] * (1.0 - this.constraintTolerance)) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(sumConstraints[i] * (1.0 - this.constraintTolerance) - sumPenaltySum);
                            test = false;
                        }
                        if (sumPenaltySum > sumConstraints[i] * (1.0 + this.constraintTolerance)) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(sumPenaltySum - sumConstraints[i] * (1.0 + this.constraintTolerance));
                            test = false;
                        }
                        break;
                    case 1:
                        if (sumPenaltySum > sumConstraints[i]) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(sumPenaltySum - sumConstraints[i]);
                            test = false;
                        }
                        break;
                }
            }
        }
        if (test) {
            funcVal = g.function(param);
            this.lastFunctValNoCnstrnt = funcVal;
        }
        return funcVal;
    }

    protected double functionValue(MinimizationFunction g, double[] x) {
        double funcVal = -3.0D;
        double[] param = new double[this.nParam];
        for (int i = 0; i < this.nParam; i++) param[i] = x[i] / scale[i];
        double tempFunctVal = this.lastFunctValNoCnstrnt;
        boolean test = true;
        if (this.penalty) {
            int k = 0;
            for (int i = 0; i < this.nConstraints; i++) {
                k = this.penaltyParam[i];
                switch(penaltyCheck[i]) {
                    case -1:
                        if (param[k] < constraints[i]) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(constraints[i] - param[k]);
                            test = false;
                        }
                        break;
                    case 0:
                        if (param[k] < constraints[i] * (1.0 - this.constraintTolerance)) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(constraints[i] * (1.0 - this.constraintTolerance) - param[k]);
                            test = false;
                        }
                        if (param[k] > constraints[i] * (1.0 + this.constraintTolerance)) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(param[k] - constraints[i] * (1.0 + this.constraintTolerance));
                            test = false;
                        }
                        break;
                    case 1:
                        if (param[k] > constraints[i]) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(param[k] - constraints[i]);
                            test = false;
                        }
                        break;
                }
            }
        }
        if (this.sumPenalty) {
            int kk = 0;
            double pSign = 0;
            double sumPenaltySum = 0.0D;
            for (int i = 0; i < this.nSumConstraints; i++) {
                for (int j = 0; j < this.sumPenaltyNumber[i]; j++) {
                    kk = this.sumPenaltyParam[i][j];
                    pSign = this.sumPlusOrMinus[i][j];
                    sumPenaltySum += param[kk] * pSign;
                }
                switch(this.sumPenaltyCheck[i]) {
                    case -1:
                        if (sumPenaltySum < sumConstraints[i]) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(sumConstraints[i] - sumPenaltySum);
                            test = false;
                        }
                        break;
                    case 0:
                        if (sumPenaltySum < sumConstraints[i] * (1.0 - this.constraintTolerance)) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(sumConstraints[i] * (1.0 - this.constraintTolerance) - sumPenaltySum);
                            test = false;
                        }
                        if (sumPenaltySum > sumConstraints[i] * (1.0 + this.constraintTolerance)) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(sumPenaltySum - sumConstraints[i] * (1.0 + this.constraintTolerance));
                            test = false;
                        }
                        break;
                    case 1:
                        if (sumPenaltySum > sumConstraints[i]) {
                            funcVal = tempFunctVal + this.penaltyWeight * Fmath.square(sumPenaltySum - sumConstraints[i]);
                            test = false;
                        }
                        break;
                }
            }
        }
        if (test) {
            funcVal = g.function(param);
            this.lastFunctValNoCnstrnt = funcVal;
        }
        return funcVal;
    }

    public void addConstraint(int paramIndex, int conDir, double constraint) {
        this.penalty = true;
        if (this.penalties.isEmpty()) this.penalties.add(new Integer(this.constraintMethod));
        if (penalties.size() == 1) {
            this.penalties.add(new Integer(1));
        } else {
            int nPC = ((Integer) this.penalties.get(1)).intValue();
            nPC++;
            this.penalties.set(1, new Integer(nPC));
        }
        this.penalties.add(new Integer(paramIndex));
        this.penalties.add(new Integer(conDir));
        this.penalties.add(new Double(constraint));
        if (paramIndex > this.maxConstraintIndex) this.maxConstraintIndex = paramIndex;
    }

    public void addConstraint(int[] paramIndices, int[] plusOrMinus, int conDir, double constraint) {
        ArrayMaths am = new ArrayMaths(plusOrMinus);
        double[] dpom = am.getArray_as_double();
        addConstraint(paramIndices, dpom, conDir, constraint);
    }

    public void addConstraint(int[] paramIndices, double[] plusOrMinus, int conDir, double constraint) {
        int nCon = paramIndices.length;
        int nPorM = plusOrMinus.length;
        if (nCon != nPorM) throw new IllegalArgumentException("num of parameters, " + nCon + ", does not equal number of parameter signs, " + nPorM);
        this.sumPenalty = true;
        if (this.sumPenalties.isEmpty()) this.sumPenalties.add(new Integer(this.constraintMethod));
        if (sumPenalties.size() == 1) {
            this.sumPenalties.add(new Integer(1));
        } else {
            int nPC = ((Integer) this.sumPenalties.get(1)).intValue();
            nPC++;
            this.sumPenalties.set(1, new Integer(nPC));
        }
        this.sumPenalties.add(new Integer(nCon));
        this.sumPenalties.add(paramIndices);
        this.sumPenalties.add(plusOrMinus);
        this.sumPenalties.add(new Integer(conDir));
        this.sumPenalties.add(new Double(constraint));
        ArrayMaths am = new ArrayMaths(paramIndices);
        int maxI = am.getMaximum_as_int();
        if (maxI > this.maxConstraintIndex) this.maxConstraintIndex = maxI;
    }

    public void setConstraintMethod(int conMeth) {
        this.constraintMethod = conMeth;
        if (!this.penalties.isEmpty()) this.penalties.set(0, new Integer(this.constraintMethod));
    }

    public void removeConstraints() {
        if (!this.penalties.isEmpty()) {
            int m = this.penalties.size();
            for (int i = m - 1; i >= 0; i--) {
                this.penalties.remove(i);
            }
        }
        this.penalty = false;
        this.nConstraints = 0;
        if (!this.sumPenalties.isEmpty()) {
            int m = this.sumPenalties.size();
            for (int i = m - 1; i >= 0; i--) {
                this.sumPenalties.remove(i);
            }
        }
        this.sumPenalty = false;
        this.nSumConstraints = 0;
        this.maxConstraintIndex = -1;
    }

    public void setConstraintTolerance(double tolerance) {
        this.constraintTolerance = tolerance;
    }

    public void print(String filename, int prec) {
        this.prec = prec;
        this.print(filename);
    }

    public void print(int prec) {
        this.prec = prec;
        String filename = "MinimisationOutput.txt";
        this.print(filename);
    }

    public void print(String filename) {
        if (filename.indexOf('.') == -1) filename = filename + ".txt";
        FileOutput fout = new FileOutput(filename, 'n');
        fout.dateAndTimeln(filename);
        fout.println(" ");
        fout.println("Simplex minimisation, using the method of Nelder and Mead,");
        fout.println("of the function y = f(c[0], c[1], c[2] . . .)");
        this.paraName = new String[this.nParam];
        for (int i = 0; i < this.nParam; i++) this.paraName[i] = "c[" + i + "]";
        fout.println();
        if (!this.convStatus) {
            fout.println("Convergence criterion was not satisfied");
            fout.println("The following results are the current estimates on exiting the minimisation method");
            fout.println();
        }
        fout.println("Value of parameters at the minimum");
        fout.println(" ");
        fout.printtab(" ", this.field);
        fout.printtab("Value at", this.field);
        fout.printtab("Initial", this.field);
        fout.println("Initial");
        fout.printtab(" ", this.field);
        fout.printtab("mimium", this.field);
        fout.printtab("estimate", this.field);
        fout.println("step");
        for (int i = 0; i < this.nParam; i++) {
            fout.printtab(this.paraName[i], this.field);
            fout.printtab(Fmath.truncate(paramValue[i], this.prec), this.field);
            fout.printtab(Fmath.truncate(this.startH[i], this.prec), this.field);
            fout.println(Fmath.truncate(this.step[i], this.prec));
        }
        fout.println();
        fout.println(" ");
        fout.printtab("Number of paramaters");
        fout.println(this.nParam);
        fout.printtab("Number of iterations taken");
        fout.println(this.nIter);
        fout.printtab("Maximum number of iterations allowed");
        fout.println(this.nMax);
        fout.printtab("Number of restarts taken");
        fout.println(this.kRestart);
        fout.printtab("Maximum number of restarts allowed");
        fout.println(this.konvge);
        fout.printtab("Standard deviation of the simplex at the minimum");
        fout.println(Fmath.truncate(this.simplexSd, this.prec));
        fout.printtab("Convergence tolerance");
        fout.println(this.fTol);
        switch(minTest) {
            case 0:
                if (this.convStatus) {
                    fout.println("simplex sd < the tolerance");
                } else {
                    fout.println("NOTE!!! simplex sd > the tolerance");
                }
                break;
        }
        fout.println();
        fout.println("End of file");
        fout.close();
    }

    public void print() {
        String filename = "MinimisationOutput.txt";
        this.print(filename);
    }

    public boolean getConvStatus() {
        return this.convStatus;
    }

    public void setScale(int n) {
        if (n < 0 || n > 1) throw new IllegalArgumentException("The argument must be 0 (no scaling) 1(initial estimates all scaled to unity) or the array of scaling factors");
        this.scaleOpt = n;
    }

    public void setScale(double[] sc) {
        this.scale = sc;
        this.scaleOpt = 2;
    }

    public double[] getScale() {
        return this.scale;
    }

    public void setMinTest(int n) {
        if (n < 0 || n > 1) throw new IllegalArgumentException("minTest must be 0 or 1");
        this.minTest = n;
    }

    public int getMinTest() {
        return this.minTest;
    }

    public double getSimplexSd() {
        return this.simplexSd;
    }

    public double[] getParamValues() {
        return this.paramValue;
    }

    public double getMinimum() {
        return this.minimum;
    }

    public int getNiter() {
        return this.nIter;
    }

    public void setNmax(int nmax) {
        this.nMax = nmax;
    }

    public int getNmax() {
        return this.nMax;
    }

    public int getNrestarts() {
        return this.kRestart;
    }

    public void setNrestartsMax(int nrs) {
        this.konvge = nrs;
    }

    public int getNrestartsMax() {
        return this.konvge;
    }

    public void setNMreflect(double refl) {
        this.rCoeff = refl;
    }

    public double getNMreflect() {
        return this.rCoeff;
    }

    public void setNMextend(double ext) {
        this.eCoeff = ext;
    }

    public double getNMextend() {
        return this.eCoeff;
    }

    public void setNMcontract(double con) {
        this.cCoeff = con;
    }

    public double getNMcontract() {
        return cCoeff;
    }

    public void setTolerance(double tol) {
        this.fTol = tol;
    }

    public double getTolerance() {
        return this.fTol;
    }
}

package gov.sns.xal.model.elem;

import java.io.PrintWriter;
import java.util.Iterator;
import gov.sns.tools.beam.PhaseMap;
import gov.sns.tools.beam.PhaseMatrix;
import gov.sns.tools.beam.optics.DriftSpace;
import gov.sns.tools.beam.optics.QuadrupoleLens;
import gov.sns.xal.model.IProbe;
import gov.sns.xal.model.ModelException;

/**
 * Represents an ideal permenant magnet quadrupole for a beam 
 * transport/accelerator system.
 * 
 * These magnets have significant field profiles and, thus, must
 * be stepped through
 *
 * @author  Hiroyuki Sako
 */
public class IdealPermMagQuad extends ThickElement implements IElectromagnet {

    static final boolean debugT3d = false;

    /** string type identifier for all IdealMagQuad objects */
    public static final String s_strType = "IdealMagQuad";

    /** Parameters for XAL MODEL LATTICE dtd */
    public static final String s_strParamOrient = "Orientation";

    public static final String s_strParamField = "MagField";

    /** debugging flag */
    private boolean debug = false;

    /** BRho Scaling factor (only valid when fieldpathflag=1 */
    private double bRhoScaling = 1;

    public double getBRhoScaling() {
        return bRhoScaling;
    }

    public void setBRhoScaling(double d) {
        bRhoScaling = d;
    }

    /** Orientation of quadrupole */
    private int m_enmOrient = ORIENT_NONE;

    /** Magnetic field strength */
    private double m_dblField = 0.0;

    public double K = 0;

    private int fringeLeft = 0;

    private int fringeRight = 0;

    private double radIn = 0.0;

    private double radOut = 0.0;

    private double effLength = 0.0;

    private double fringeFactor = 0;

    private double sCenter = 0.0;

    private double sLength = 0.0;

    private double K1 = 0.0;

    int ori = -100;

    public double getOrientProbe() {
        return ori;
    }

    protected void setOrientProbe(int orient) {
        ori = orient;
    }

    public void setFringeLeft(int edge) {
        fringeLeft = edge;
    }

    public void setFringeRight(int edge) {
        fringeRight = edge;
    }

    public int getFringeLeft() {
        return fringeLeft;
    }

    public int getFringeRight() {
        return fringeRight;
    }

    public double getFringeFactor() {
        return fringeFactor;
    }

    public double getK() {
        return K;
    }

    public void setRadIn(double ri) {
        radIn = ri;
    }

    public void setRadOut(double ro) {
        radOut = ro;
    }

    public void setSCenter(double se) {
        sCenter = se;
    }

    public void setSLength(double sl) {
        sLength = sl;
    }

    public void setEffLength(double el) {
        effLength = el;
    }

    public void setFringeLenEntr(double fl) {
    }

    public void setFringeLenExit(double fl) {
    }

    public double getRadIn() {
        return radIn;
    }

    public double getRadOut() {
        return radOut;
    }

    public double getSCenter() {
        return sCenter;
    }

    public double getSLength() {
        return sLength;
    }

    public double getEffLength() {
        return effLength;
    }

    private double fieldPathFlag = 0.0;

    private double nominalKineEnergy = 0.0;

    public void setFieldPathFlag(double ba) {
        fieldPathFlag = ba;
    }

    public void setNominalKineEnergy(double ba) {
        nominalKineEnergy = ba;
    }

    public double getFieldPathFlag() {
        return fieldPathFlag;
    }

    public double getNominalKineEnergy() {
        return nominalKineEnergy;
    }

    public double getK1() {
        return K1;
    }

    public void setK1(double k1) {
        K1 = k1;
    }

    /** 
     *  Creates a new instance of IdealMagQuad 
     *
     *  @param  strId     identifier for this IdealMagQuad object
     *  @param  enmOrient enumeration specifying the quadrupole orientation
     *                    (ORIENT_HOR or ORIENT_VER)
     *  @param  dblFld    field gradient strength (in <b>Tesla/meter</b>)
     *  @param  dblLen    length of the quadrupole
     */
    public IdealPermMagQuad(String strId, int enmOrient, double dblFld, double dblLen) {
        super(s_strType, strId, dblLen);
        this.setOrientation(enmOrient);
        this.setMagField(dblFld);
    }

    ;

    /** 
     *  JavaBean constructor - creates a new unitialized instance of IdealMagQuad
     *
     *  <b>BE CAREFUL</b>
     */
    public IdealPermMagQuad() {
        super(s_strType);
    }

    ;

    /**
     *  Return the orientation enumeration code.
     *
     *  @return     ORIENT_HOR  - dipole has steering action in x (horizontal) plane
     *              ORIENT_VER  - dipole has steering action in y (vertical) plane
     *              ORIENT_NONE - error
     */
    public int getOrientation() {
        return m_enmOrient;
    }

    ;

    /**  
     *  Get the magnetic field strength of the electromagnet
     *
     *  @return     magnetic field (in <bold>Tesla</bold>).
     */
    public double getMagField() {
        if (fieldPathFlag == 0) {
            return m_dblField;
        } else {
            return getBRhoScaling() * m_dblField;
        }
    }

    ;

    /**
     *  Set the magnet orientation
     *  
     *  @param  enmOrient   magnet orientation enumeration code
     *
     *  @see    #getOrientation
     */
    public void setOrientation(int enmOrient) {
        m_enmOrient = enmOrient;
    }

    ;

    /**  
     *  Set the magnetic field strength of the electromagnet.
     *
     *  @param  dblField    magnetic field (in <bold>Tesla</bold>).
     */
    public void setMagField(double dblField) {
        m_dblField = dblField;
    }

    ;

    /**
     * Returns the time taken for the probe to drift through part of the
     * element.
     * 
     *  @param  probe   propagating probe
     *  @param  dblLen  length of subsection to propagate through <b>meters</b>
     *  
     *  @return         the elapsed time through section<bold>Units: seconds</bold> 
     */
    @Override
    public double elapsedTime(IProbe probe, double dblLen) {
        return super.compDriftingTime(probe, dblLen);
    }

    /**
     *  Return the energy ga 
in imparted to a particular probe.  For an ideal quadrupole
     *  magnet this value is always zero.
     *
     *  @param  dblLen  dummy argument
     *  @param  probe   dummy argument
     *
     *  @return         returns a zero value
     */
    @Override
    public double energyGain(IProbe probe, double dblLen) {
        return 0.0;
    }

    ;

    public static PhaseMap transferMap(IProbe probe, double dL, double k, int orientation, double alignx, double aligny, double alignz) {
        double[][] arrF = QuadrupoleLens.transferFocPlane(k, dL);
        double[][] arrD = QuadrupoleLens.transferDefPlane(k, dL);
        double[][] arr0 = DriftSpace.transferDriftPlane(dL);
        PhaseMatrix matPhi = new PhaseMatrix();
        matPhi.setSubMatrix(4, 5, 4, 5, arr0);
        matPhi.setElem(6, 6, 1.0);
        try {
            switch(orientation) {
                case ORIENT_HOR:
                    matPhi.setSubMatrix(0, 1, 0, 1, arrF);
                    matPhi.setSubMatrix(2, 3, 2, 3, arrD);
                    break;
                case ORIENT_VER:
                    matPhi.setSubMatrix(0, 1, 0, 1, arrD);
                    matPhi.setSubMatrix(2, 3, 2, 3, arrF);
                    break;
                default:
                    throw new ModelException("IdealMagQuad::computeTransferMatrix() - Bad magnet orientation.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        matPhi = applyAlignErrorStatic(matPhi, alignx, aligny, alignz);
        return new PhaseMap(matPhi);
    }

    ;

    protected double KFringeFromOthers(IProbe probe, double dblLen) {
        double KSum = 0;
        double dxSum = 0;
        double dySum = 0;
        double dzSum = 0;
        int KSumCount = 0;
        if (this.getCloseElements() != null) {
            Iterator<Element> it = this.getCloseElements().iterator();
            while (it.hasNext()) {
                Element elem = it.next();
                if (elem instanceof IdealPermMagQuad) {
                    IdealPermMagQuad permQuad = (IdealPermMagQuad) elem;
                    double K = permQuad.calcK(probe, dblLen);
                    double dx = permQuad.getAlignX();
                    double dy = permQuad.getAlignY();
                    double dz = permQuad.getAlignZ();
                    dxSum += dx;
                    dySum += dy;
                    dzSum += dz;
                    if (debug) {
                        System.out.println("id, K = " + permQuad.getId() + " " + K);
                    }
                    double q = probe.getSpeciesCharge();
                    double fld = permQuad.getMagField();
                    if (q * fld >= 0) {
                        KSum += (K * K);
                    } else {
                        KSum -= (K * K);
                    }
                    KSumCount++;
                }
            }
            if (debug) {
                System.out.println("xxxxxxxxxxxxxx IdealPermQuad(" + this.getId() + ") (Kfringefromothers, KSum, KSumCount = " + KSum + " " + KSumCount);
            }
        }
        if (KSumCount > 0) {
            dxSum /= KSumCount;
            dySum /= KSumCount;
            dzSum /= KSumCount;
        }
        if (KSum >= 0) {
            KSum = Math.sqrt(KSum);
        } else {
            KSum = -Math.sqrt(-KSum);
        }
        return KSum;
    }

    private static PhaseMatrix applyAlignErrorStatic(PhaseMatrix matPhi, double delx, double dely, double delz) {
        if ((delx == 0) && (dely == 0) && (delz == 0)) {
            return matPhi;
        }
        PhaseMatrix T = new PhaseMatrix();
        for (int i = 0; i < 7; i++) {
            T.setElem(i, i, 1);
        }
        T.setElem(0, 6, -delx);
        T.setElem(2, 6, -dely);
        T.setElem(4, 6, -delz);
        PhaseMatrix Phidx = T.inverse().times(matPhi).times(T);
        return Phidx;
    }

    public double calcK(IProbe probe, double dblLen) {
        double bPathFlag = getFieldPathFlag();
        double w = probe.getKineticEnergy();
        double Er = probe.getSpeciesRestEnergy();
        double p = Math.sqrt(w * (w + 2 * Er));
        double r1 = radIn;
        double r2 = radOut;
        double s = probe.getPosition() + dblLen / 2.;
        double s1 = sCenter - sLength / 2;
        double s2 = sCenter + sLength / 2;
        double f = fringe(s, s - s2, s - s1, r1, r2);
        if (debug) {
            System.out.println("IdealPermMagQuad (" + this.getId() + ")::transferMap, s, s1, s2 = " + s + " " + s1 + " " + s2);
            System.out.println("r1, r2");
            System.out.println("f = " + f);
            System.out.println("dblLen = " + dblLen);
            System.out.println("probe.getPosition() = " + probe.getPosition());
        }
        if (f < 0) {
            System.out.println("****** WARNING: f = " + f);
            System.out.println("IdealPermMagQuad (" + this.getId() + ")::transferMap, s, s1, s2 = " + s + " " + s1 + " " + s2);
            System.out.println("r1, r2");
            System.out.println("f = " + f);
            System.out.println("dblLen = " + dblLen);
            System.out.println("probe.getPosition() = " + probe.getPosition());
            f = 0;
        } else if (f > 1) {
            System.out.println("****** WARNING: f = " + f);
            System.out.println("IdealPermMagQuad (" + this.getId() + ")::transferMap, s, s1, s2 = " + s + " " + s1 + " " + s2);
            System.out.println("r1, r2");
            System.out.println("f = " + f);
            System.out.println("dblLen = " + dblLen);
            System.out.println("probe.getPosition() = " + probe.getPosition());
            f = 1;
        }
        double G0 = Math.abs(this.getMagField());
        double G = G0 * f;
        double k = Math.sqrt((LightSpeed * G) / p);
        if (K1 != 0.) {
            System.out.println("K1, k = " + K1 + " " + k);
            k = Math.sqrt(Math.abs(K1) * f);
        }
        if (debugT3d) {
            System.out.println("XAL element,z,grad = " + probe.getCurrentElement() + " " + s * 1000 + " " + G);
        }
        return k;
    }

    public double KFringe(IProbe probe, double dblLen) {
        double KSum = 0;
        if (this.getCloseElements() != null) {
            if (debug) {
                System.out.println("xxxxxxxxxxxxxx in IdealDrift, s, dblLen  = " + this.getPosition() + " " + dblLen);
            }
            Iterator<Element> it = this.getCloseElements().iterator();
            int KSumCount = 0;
            while (it.hasNext()) {
                Element elem = it.next();
                if (elem instanceof IdealPermMagQuad) {
                    IdealPermMagQuad permQuad = (IdealPermMagQuad) elem;
                    double K = permQuad.calcK(probe, dblLen);
                    if (debug) {
                        System.out.println("id, K = " + permQuad.getId() + " " + K);
                    }
                    if (permQuad.getOrientation() == IElectromagnet.ORIENT_HOR) {
                        KSum += (K * K);
                    } else {
                        KSum -= (K * K);
                    }
                    KSumCount++;
                }
            }
            if (KSum >= 0) {
                KSum = Math.sqrt(KSum);
            } else {
                KSum = -Math.sqrt(-KSum);
            }
            if (debug) {
                System.out.println("xxxxxxxxxxxxxx end IdealPermQuad(" + this.getId() + "), KSum, KSumCount = " + KSum + " " + KSumCount);
            }
        }
        return KSum;
    }

    /**
     *  Compute the partial transfer map of permenant magnet quadrupole 
     *  for the particular probe.
     *  Computes transfer map for a section of quadrupole <code>dblLen</code> meters
     *  in length.
     *
     *  @param  dblLen  compute transfer matrix for section of this length
     *  @param  probe   uses the rest and kinetic energy parameters from the probe
     *
     *  @return         transfer map of ideal quadrupole for particular probe
     *
     *  @exception  ModelException    unknown quadrupole orientation
     */
    static final boolean useApproxLens = true;

    public static boolean getUseApproxLens() {
        return useApproxLens;
    }

    @Override
    public PhaseMap transferMap(IProbe probe, double dblLen) throws ModelException {
        double dL = dblLen;
        double bPathFlag = getFieldPathFlag();
        double w = probe.getKineticEnergy();
        double Er = probe.getSpeciesRestEnergy();
        double p = Math.sqrt(w * (w + 2 * Er));
        if (bPathFlag != 0.) {
            double w0 = getNominalKineEnergy();
            if (w0 == 0.) {
                w0 = w;
                setNominalKineEnergy(w0);
            }
            double p0 = Math.sqrt(w0 * (w0 + 2 * Er));
            setBRhoScaling(p / p0);
        }
        K = calcK(probe, dL);
        double q = probe.getSpeciesCharge();
        double assymmetryX = 1;
        double assymmetryY = 1;
        double assymmetryF = assymmetryX;
        double assymmetryD = assymmetryY;
        if (q * getMagField() < 0) {
            assymmetryF = assymmetryY;
            assymmetryD = assymmetryX;
        }
        if (debug) {
            System.out.println("XAL(PMQ)total K, dL = " + K + " " + dL);
        }
        double[][] arrF;
        if (useApproxLens) {
            arrF = QuadrupoleLens.transferFocPlaneApproxSandWitch(K * assymmetryF, dL);
        } else {
            arrF = QuadrupoleLens.transferFocPlane(K * assymmetryF, dL);
        }
        double[][] arrD;
        if (useApproxLens) {
            arrD = QuadrupoleLens.transferDefPlaneApproxSandWitch(K * assymmetryD, dL);
        } else {
            arrD = QuadrupoleLens.transferDefPlane(K * assymmetryD, dL);
        }
        double[][] arr0 = DriftSpace.transferDriftPlane(dL);
        PhaseMatrix matPhi = new PhaseMatrix();
        matPhi.setSubMatrix(4, 5, 4, 5, arr0);
        matPhi.setElem(6, 6, 1.0);
        if (q * getMagField() >= 0) {
            matPhi.setSubMatrix(0, 1, 0, 1, arrF);
            matPhi.setSubMatrix(2, 3, 2, 3, arrD);
        } else {
            matPhi.setSubMatrix(0, 1, 0, 1, arrD);
            matPhi.setSubMatrix(2, 3, 2, 3, arrF);
        }
        matPhi = applyAlignError(matPhi);
        return new PhaseMap(matPhi);
    }

    /**
     * new variable represents for PQEXT parameter in Trace3d
     * which shows the extent of fringe field
     * this must be consistent with IdealDrift
     */
    static final double pqExt = 2.5;

    /**
     * based on trace3d pmqf subroutine
     *
     */
    private final double fringe(double s, double z1, double z2, double r1, double r2) {
        if (r1 <= 0 && r2 <= 0) {
            if (z1 <= 0 && z2 >= 0) {
                return 1;
            } else {
                return 0;
            }
        }
        if (z1 > 0) {
            if (z1 >= pqExt * r1) {
                return 0;
            }
        } else if (z2 < 0) {
            if (-z2 >= pqExt * r1) {
                return 0;
            }
        }
        if (r2 < r1) {
            System.err.println("IdealMagQuad, name = " + this.getId());
            System.err.println("IdealMagQuad::fringe - outer radius r2 (" + r2 + ") is smaller than inner radius r1 (" + r1 + ")... Aborting");
            System.exit(0);
            return 0;
        }
        double mins = this.getPosition() - this.getLength() / 2;
        double maxs = this.getPosition() + this.getLength() / 2;
        if ((fringeLeft == 0) && (s < mins)) {
            return 0;
        } else if ((fringeRight == 0) && (maxs < s)) {
            return 0;
        }
        double v1 = 0;
        if (r1 != 0) {
            v1 = 1 / Math.sqrt(1 + (z1 / r1) * (z1 / r1));
        }
        double v2 = 0;
        if (r2 != 0) {
            v2 = 1 / Math.sqrt(1 + (z1 / r2) * (z1 / r2));
        }
        double v1s = v1 * v1;
        double v2s = v2 * v2;
        double v12m = v1 * v2;
        double v12a = v1 + v2;
        double vtemp = (v1s * v2s * (v1s + v2s + v12m + 4 + 8 / v12m)) / v12a;
        double f1 = 0.5 * (1 - 0.125 * z1 * (1 / r1 + 1 / r2) * vtemp);
        double w1 = 0;
        if (r1 != 0) {
            w1 = 1 / Math.sqrt(1 + (z2 / r1) * (z2 / r1));
        }
        double w2 = 0;
        if (r2 != 0) {
            w2 = 1 / Math.sqrt(1 + (z2 / r2) * (z2 / r2));
        }
        double w1s = w1 * w1;
        double w2s = w2 * w2;
        double w12m = w1 * w2;
        double w12a = w1 + w2;
        double wtemp = (w1s * w2s * (w1s + w2s + w12m + 4 + 8 / w12m)) / w12a;
        double f2 = 0.5 * (1 - 0.125 * z2 * (1 / r1 + 1 / r2) * wtemp);
        if (debugT3d) {
            System.out.println("XAL: s,z1,z2,r1,r2 =" + s * 1000 + " " + z1 * 1000 + " " + z2 * 1000 + " " + r1 * 1000 + " " + r2 * 1000);
        }
        fringeFactor = f1 - f2;
        return fringeFactor;
    }

    /**
     *  Dump current state and content to output stream.
     *
     *  @param  os      output stream object
     */
    @Override
    public void print(PrintWriter os) {
        super.print(os);
        os.println("  magnetic field     : " + this.getMagField());
        os.println("  magnet orientation : " + this.getOrientation());
    }

    ;
}

;

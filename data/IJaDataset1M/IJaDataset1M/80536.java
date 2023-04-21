package gov.sns.xal.model.elem;

import java.io.PrintWriter;
import gov.sns.tools.beam.PhaseMap;
import gov.sns.tools.beam.PhaseMatrix;
import gov.sns.tools.beam.optics.DriftSpace;
import gov.sns.tools.beam.optics.QuadrupoleLens;
import gov.sns.xal.model.IProbe;

/**
 * Represents an ideal magnetic quadrupole magnet for a beam 
 * transport/accelerator system.
 *
 * @author  Christopher K. Allen
 */
public class IdealMagQuad extends ThickElement implements IElectromagnet {

    /** string type identifier for all IdealMagQuad objects */
    public static final String s_strType = "IdealMagQuad";

    /** Parameters for XAL MODEL LATTICE dtd */
    public static final String s_strParamOrient = "Orientation";

    public static final String s_strParamField = "MagField";

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

    private double radIn = 0.0;

    private double radOut = 0.0;

    private double K1 = 0.0;

    /**
     * for fringe field calculation based on SAD
     * H. Matsuda et al, NIM 103 (1972) 117
     */
    private double f1Entr = 0;

    private double f1Exit = 0;

    private double f2Entr = 0;

    private double f2Exit = 0;

    public double getFringeIntegral1Entr() {
        return f1Entr;
    }

    public double getFringeIntegral1Exit() {
        return f1Exit;
    }

    public double getFringeIntegral2Entr() {
        return f2Entr;
    }

    public double getFringeIntegral2Exit() {
        return f2Exit;
    }

    public void setFringeIntegral1Entr(double dbl) {
        f1Entr = dbl;
    }

    public void setFringeIntegral1Exit(double dbl) {
        f1Exit = dbl;
    }

    public void setFringeIntegral2Entr(double dbl) {
        f2Entr = dbl;
    }

    public void setFringeIntegral2Exit(double dbl) {
        f2Exit = dbl;
    }

    public void setRadIn(double ri) {
        radIn = ri;
    }

    public void setRadOut(double ro) {
        radOut = ro;
    }

    public double getRadIn() {
        return radIn;
    }

    public double getRadOut() {
        return radOut;
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
    public IdealMagQuad(String strId, int enmOrient, double dblFld, double dblLen) {
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
    public IdealMagQuad() {
        super(s_strType);
    }

    ;

    /**
     *  Return the orientation enumeration code.
     *
     *  @return     ORIENT_HOR  - quadrupole focuses in x (horizontal) plane
     *              ORIENT_VER  - quadrupole focuses in y ( vertical ) plane
     *              ORIENT_NONE - error
     */
    public int getOrientation() {
        return m_enmOrient;
    }

    ;

    public double getMagField() {
        return m_dblField;
    }

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
     *  @param  dblField    magnetic field (in <bold>Tesla/meter</bold>).
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
     *  Return the energy gain imparted to a particular probe.  For an ideal quadrupole
     *  magnet this value is always zero.
     *  @param  probe   dummy argument
     *  @param  dblLen  dummy argument
     *  @return         returns a zero value
     */
    @Override
    public double energyGain(IProbe probe, double dblLen) {
        return 0.0;
    }

    /**
     *  Compute the partial transfer map of an ideal quadrupole for the particular probe.
     *  Computes transfer map for a section of quadrupole <code>dblLen</code> meters in length.
     *  @param  probe   supplies the charge, rest and kinetic energy parameters
     *  @param  length  compute transfer matrix for section of this length
     *  @return         transfer map of ideal quadrupole for particular probe
     */
    @Override
    public PhaseMap transferMap(final IProbe probe, final double length) {
        double charge = probe.getSpeciesCharge();
        double Er = probe.getSpeciesRestEnergy();
        double w = probe.getKineticEnergy();
        double p = Math.sqrt(w * (w + 2 * Er));
        double bPathFlag = getFieldPathFlag();
        if (bPathFlag == 1) {
            double w0 = getNominalKineEnergy();
            if (w0 == 0.) {
                w0 = probe.getKineticEnergy();
                setNominalKineEnergy(w0);
            }
            double p0 = Math.sqrt(w0 * (w0 + 2 * Er));
            setBRhoScaling(p / p0);
        }
        double k = 0;
        if (bPathFlag == 0) {
            k = (charge * LightSpeed * getMagField()) / p;
        } else if (bPathFlag == 1) {
            k = (charge * LightSpeed * getMagField() * getBRhoScaling()) / p;
        } else {
            k = K1;
        }
        double kSqrt = Math.sqrt(Math.abs(k));
        final double[][] arrF = QuadrupoleLens.transferFocPlane(kSqrt, length);
        final double[][] arrD = QuadrupoleLens.transferDefPlane(kSqrt, length);
        final double[][] arr0 = DriftSpace.transferDriftPlane(length);
        PhaseMatrix matPhi = new PhaseMatrix();
        matPhi.setSubMatrix(4, 5, 4, 5, arr0);
        matPhi.setElem(6, 6, 1.0);
        if (k >= 0.0) {
            matPhi.setSubMatrix(0, 1, 0, 1, arrF);
            matPhi.setSubMatrix(2, 3, 2, 3, arrD);
        } else if (k < 0.0) {
            matPhi.setSubMatrix(0, 1, 0, 1, arrD);
            matPhi.setSubMatrix(2, 3, 2, 3, arrF);
        }
        PhaseMatrix Phidx = applyAlignError(matPhi);
        matPhi = Phidx;
        return new PhaseMap(matPhi);
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

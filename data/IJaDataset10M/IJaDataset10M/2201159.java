package jp.jparc.tools.beam.tests;

import gov.sns.tools.beam.CorrelationMatrix;
import gov.sns.tools.beam.PhaseIndexHom;
import gov.sns.tools.beam.PhaseMatrix;
import gov.sns.tools.beam.PhaseVector;
import gov.sns.tools.beam.Twiss;
import gov.sns.tools.math.ElementaryFunction;
import gov.sns.tools.math.EllipticIntegral;
import gov.sns.tools.math.r3.R3x3;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jp.jparc.tools.beam.em.BeamEllipsoid;
import junit.framework.JUnit4TestAdapter;

/**
 * Class for performing JUnit 4.x test on the <code>BeamEllipsoid</code> class
 * in the <code>gov.sns.tools.math</code> package.
 * 
 * @author Christopher K. Allen
 *
 */
public class TestBeamEllipsoid {

    /** solution precision */
    public static final double ERROR_TOLERANCE = 1.0e-5;

    /** relativistic parameter for stationary beam */
    public static final double GAMMA_STAT = 1.0;

    /** relativistic parameter for MEBT */
    public static final double GAMMA_MEBT = 1.003193863;

    /** relativistic parameter for L3BT */
    public static final double GAMMA_L3BT = 1.192678;

    /** testing step length */
    public static final double DBL_STEP = 0.01;

    /** generalized beam perveance */
    public static final double DBL_PERVEANCE = 1.0e-5;

    /** JPARC MEBT x-plane Twiss parameters */
    public static final Twiss TWISS_X = new Twiss(-1.2187, 0.13174, 3.1309642E-6);

    /** JPARC MEBT y-plane Twiss parameters */
    public static final Twiss TWISS_Y = new Twiss(2.1885, 0.22344, 2.5075842000000002E-6);

    /** JPARC MEBT z-plane Twiss parameters */
    public static final Twiss TWISS_Z = new Twiss(0.08, 0.7819530229746938, 3.106895634426948E-6);

    /** beam centroid offset */
    public static final PhaseVector VEC_OFFSET = new PhaseVector(0.0, 0.0, 0.001, -0.010, 0.0, 0.0);

    /** rotation angle for ellipsoid */
    public static final double DBL_ANGLE = 30.0 * (Math.PI / 180.0);

    /** stationary beam spheroid */
    private BeamEllipsoid sphereStat;

    /** stationary beam ellipsoid */
    private BeamEllipsoid ellipsoidStat;

    /** relativistic beam ellipsoid */
    private BeamEllipsoid ellipsoidRelat;

    /** rotated beam ellipsoid */
    private BeamEllipsoid ellipsoidRotate;

    /**
     * Return a JUnit 3.x version <code>TestBeamEllipsoid</code> instance that encapsulates 
     * this test suite.  This is a convenience method for attaching to old JUnit testing
     * frameworks, for example, using Eclipse.
     * 
     * @return  a JUnit 3.8 type test object adaptor
     */
    public static junit.framework.Test getJUnitTest() {
        return new JUnit4TestAdapter(TestBeamEllipsoid.class);
    }

    /**
     *  Create a new <code>TestR3x3JacobiDecomposition</code> class for 
     *  JUnit 4.x testing of the <code>TableSchema</code> class. 
     */
    public TestBeamEllipsoid() {
        super();
    }

    /**
     * Setup the test fixture by creating a the test matrices.
     */
    @Before
    public void setup() {
    }

    /**
     * Test the properties of a stationary spheroidal beam.  
     * 
     * The formula for the Carlson elliptic integrals with equal arguments 
     * simplies to the analytic form
     * 
     *      RD(x,x,x) = 1/x^3/2
     *      
     * This situation corresponds to the case of a sphere in the beam frame.
     * 
     * For this case, the formula for the normalized space charge defocusing 
     * constants <i>kn</i> in the beam frame, in the beam coordinates, is
     * 
     *      kn^2 = (gamma^2/2)*(K/5^3/2)*(1/(emit*beta)^3/2)
     *      
     * where <i>gamma</i> is the relativistic factor, <i>emit</i> is the beam emittance, 
     * and <i>beta</i> is the beam velocity normaized to the speed of light.  The 
     * unnormalized values <i>k</i>^2 are multiplied by <i>K*ds</i> where <i>K</i> 
     * is the generalized beam perveance, and <i>ds</i> is the incremenation path
     * length. 
     */
    @Test
    public void testStationarySpheroid() {
        CorrelationMatrix matTau = CorrelationMatrix.buildCorrelation(TWISS_X, TWISS_X, TWISS_X);
        BeamEllipsoid sphere = new BeamEllipsoid(TestBeamEllipsoid.GAMMA_STAT, matTau);
        double dblEnvR = TWISS_X.getEnvelopeRadius();
        double[] arrSemiA = sphere.getSemiAxes();
        for (double axis : arrSemiA) {
            Assert.assertTrue(ElementaryFunction.approxEq(axis, dblEnvR));
        }
        double dbl2ndMm = sphere.get2ndMomentX();
        double dblDefoc = ((GAMMA_STAT * GAMMA_STAT) / 2.0) * (1.0 / Math.pow(5.0 * dbl2ndMm, 1.5));
        double[] arrDefoc = sphere.getDefocusingConstants();
        for (double defoc : arrDefoc) {
            Assert.assertTrue(ElementaryFunction.approxEq(defoc, dblDefoc));
        }
    }

    /**
     * Test the properties of spherical beam moving at relativistic speed.
     * 
     * A spherical beam moving at relativistic speed admits several analytic
     * formulas.
     * 
     * The Carlson elliptic integral RD(r,r,z) simplifies again to an
     * analytic formula involving inverse cosines and hyperbolic cosines.
     * The form RD(r,r,z) occurs because of the length contraction in the
     * longitudinal plane.
     * 
     * We can also directly check the Lorentz transform length contraction. 
     */
    @Test
    public void testRelativisticSpheroid() {
        double gamma = TestBeamEllipsoid.GAMMA_L3BT;
        CorrelationMatrix matTau = CorrelationMatrix.buildCorrelation(TWISS_X, TWISS_X, TWISS_X);
        BeamEllipsoid sphere = new BeamEllipsoid(gamma, matTau);
        double dblEnvR = TWISS_X.getEnvelopeRadius();
        double dblEnvZ = dblEnvR * gamma;
        double dblSemiR = sphere.getSemiAxisX();
        double dblSemiZ = sphere.getSemiAxisZ();
        Assert.assertTrue(ElementaryFunction.approxEq(dblSemiR, dblEnvR));
        Assert.assertTrue(ElementaryFunction.approxEq(dblSemiZ, dblEnvZ));
        double dblR_2 = dblEnvR * dblEnvR;
        double dblZ_2 = dblEnvZ * dblEnvZ;
        double dblDefocR = ((gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDr(dblR_2, dblZ_2);
        double dblDefocZ = ((gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDz(dblR_2, dblZ_2);
        double dblKnR = sphere.getDefocusingConstantX();
        double dblKnZ = sphere.getDefocusingConstantZ();
        Assert.assertTrue(ElementaryFunction.approxEq(dblDefocR, dblKnR, 10000));
        Assert.assertTrue(ElementaryFunction.approxEq(dblDefocZ, dblKnZ, 10000));
    }

    /**
     * Test the properties of an ellipsoidal beam moving at relativistic speed.
     * 
     * An ellipsoidal beam moving at relativistic speed also admits several analytic
     * formulas.
     * 
     * The Carlson elliptic integral RD(r,r,z) simplifies again to an
     * analytic formula involving inverse cosines and hyperbolic cosines.
     * 
     * Again, we can also directly check the Lorentz transform length contraction. 
     */
    @Test
    public void testRelativisticEllipsoid() {
        double gamma = TestBeamEllipsoid.GAMMA_L3BT;
        CorrelationMatrix matTau = CorrelationMatrix.buildCorrelation(TWISS_X, TWISS_X, TWISS_Z);
        BeamEllipsoid ellipsoid = new BeamEllipsoid(gamma, matTau);
        double dblEnvR = TWISS_X.getEnvelopeRadius();
        double dblEnvZ = TWISS_Z.getEnvelopeRadius() * gamma;
        double dblSemiR = ellipsoid.getSemiAxisX();
        double dblSemiZ = ellipsoid.getSemiAxisZ();
        Assert.assertTrue(ElementaryFunction.approxEq(dblSemiR, dblEnvR));
        Assert.assertTrue(ElementaryFunction.approxEq(dblSemiZ, dblEnvZ));
        double dblR_2 = dblEnvR * dblEnvR;
        double dblZ_2 = dblEnvZ * dblEnvZ;
        double dblDefocR = ((gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDr(dblR_2, dblZ_2);
        double dblDefocZ = ((gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDz(dblR_2, dblZ_2);
        double dblKnR = ellipsoid.getDefocusingConstantX();
        double dblKnZ = ellipsoid.getDefocusingConstantZ();
        Assert.assertTrue(ElementaryFunction.approxEq(dblDefocR, dblKnR, 10000));
        Assert.assertTrue(ElementaryFunction.approxEq(dblDefocZ, dblKnZ, 10000));
    }

    /**
     * Test the properties of rotated stationary ellipsoidal beam.
     * 
     * We can use the properties of the rotation group SO(3) to 
     * check the rotated beam ellipsoid.
     */
    @Test
    public void testRotatedEllipsoid() {
        R3x3 matSO3 = R3x3.rotationY(TestBeamEllipsoid.DBL_ANGLE);
        PhaseMatrix matRot = PhaseMatrix.rotationProduct(matSO3);
        CorrelationMatrix matTau0 = CorrelationMatrix.buildCorrelation(TWISS_X, TWISS_X, TWISS_Z);
        PhaseMatrix matTau1 = matTau0.conjugateTrans(matRot);
        CorrelationMatrix matTau = new CorrelationMatrix(matTau1);
        double gamma = TestBeamEllipsoid.GAMMA_STAT;
        BeamEllipsoid ellipsoid = new BeamEllipsoid(gamma, matTau);
        double dblEnvR = TWISS_X.getEnvelopeRadius();
        double dblEnvZ = TWISS_Z.getEnvelopeRadius() * gamma;
        double dblSemiX = ellipsoid.getSemiAxisX();
        double dblSemiY = ellipsoid.getSemiAxisY();
        double dblSemiZ = ellipsoid.getSemiAxisZ();
        Assert.assertTrue(ElementaryFunction.approxEq(dblSemiX, dblSemiY));
        Assert.assertTrue(ElementaryFunction.approxEq(dblSemiX, dblEnvR));
        Assert.assertTrue(ElementaryFunction.approxEq(dblSemiZ, dblEnvZ));
        double dblR_2 = dblEnvR * dblEnvR;
        double dblZ_2 = dblEnvZ * dblEnvZ;
        double dblDefocR = ((gamma * gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDr(dblR_2, dblZ_2);
        double dblDefocZ = ((gamma * gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDz(dblR_2, dblZ_2);
        double dblKnR = ellipsoid.getDefocusingConstantX();
        double dblKnZ = ellipsoid.getDefocusingConstantZ();
        Assert.assertTrue(ElementaryFunction.approxEq(dblDefocR, dblKnR, 10000));
        Assert.assertTrue(ElementaryFunction.approxEq(dblDefocZ, dblKnZ, 10000));
    }

    /**
     * Test the properties of an offset, rotated, stationary, 
     * ellipsoidal beam.
     * 
     * We can use the properties of the rotation group SO(3) to 
     * check the rotated beam ellipsoid.  The correlation matrix can be 
     * built from the covariance matrix and the outer product of the
     * offset phase vector. 
     */
    @Test
    public void testOffsetEllipsoid() {
        PhaseMatrix matTau = CorrelationMatrix.buildCorrelation(TWISS_X, TWISS_X, TWISS_Z, VEC_OFFSET);
        R3x3 matSO3 = R3x3.rotationY(TestBeamEllipsoid.DBL_ANGLE);
        PhaseMatrix matRot = PhaseMatrix.rotationProduct(matSO3);
        matTau = matTau.conjugateTrans(matRot);
        double gamma = TestBeamEllipsoid.GAMMA_STAT;
        BeamEllipsoid ellipsoid = new BeamEllipsoid(gamma, new CorrelationMatrix(matTau));
        double dblEnvR = TWISS_X.getEnvelopeRadius();
        double dblEnvZ = TWISS_Z.getEnvelopeRadius() * gamma;
        double dblSemiX = ellipsoid.getSemiAxisX();
        double dblSemiY = ellipsoid.getSemiAxisY();
        double dblSemiZ = ellipsoid.getSemiAxisZ();
        Assert.assertTrue(ElementaryFunction.approxEq(dblSemiX, dblSemiY));
        Assert.assertTrue(ElementaryFunction.approxEq(dblSemiX, dblEnvR));
        Assert.assertTrue(ElementaryFunction.approxEq(dblSemiZ, dblEnvZ));
        double dblR_2 = dblEnvR * dblEnvR;
        double dblZ_2 = dblEnvZ * dblEnvZ;
        double dblDefocR = ((gamma * gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDr(dblR_2, dblZ_2);
        double dblDefocZ = ((gamma * gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDz(dblR_2, dblZ_2);
        double dblKnR = ellipsoid.getDefocusingConstantX();
        double dblKnZ = ellipsoid.getDefocusingConstantZ();
        Assert.assertTrue(ElementaryFunction.approxEq(dblDefocR, dblKnR, 10000));
        Assert.assertTrue(ElementaryFunction.approxEq(dblDefocZ, dblKnZ, 10000));
    }

    /**
     * Test the generator matrix generation for a rotated, stationary
     * ellipsoidal beam.
     * 
     * We can compute the generator matrix independently in this case and compare
     * it with that produced by the <code>BeamEllipsoid</code> class.
     */
    @Test
    public void testStatRotGeneratorMatrix() {
        R3x3 matSO3 = R3x3.rotationY(TestBeamEllipsoid.DBL_ANGLE);
        PhaseMatrix matRot = PhaseMatrix.rotationProduct(matSO3);
        CorrelationMatrix matTau0 = CorrelationMatrix.buildCorrelation(TWISS_X, TWISS_X, TWISS_Z);
        PhaseMatrix matTau1 = matTau0.conjugateTrans(matRot);
        CorrelationMatrix matTau = new CorrelationMatrix(matTau1);
        double gamma = TestBeamEllipsoid.GAMMA_STAT;
        BeamEllipsoid ellipsoid = new BeamEllipsoid(gamma, matTau);
        PhaseMatrix matGenEl = ellipsoid.computeScheffGenerator(TestBeamEllipsoid.DBL_PERVEANCE);
        double dblEnvR = TWISS_X.getEnvelopeRadius();
        double dblEnvZ = TWISS_Z.getEnvelopeRadius() * gamma;
        double dblR_2 = dblEnvR * dblEnvR;
        double dblZ_2 = dblEnvZ * dblEnvZ;
        double dblDefocR = ((gamma * gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDr(dblR_2, dblZ_2);
        double dblDefocZ = ((gamma * gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDz(dblR_2, dblZ_2);
        PhaseMatrix matGenAn = PhaseMatrix.zero();
        matGenAn.setElem(PhaseIndexHom.Xp, PhaseIndexHom.X, dblDefocR * DBL_PERVEANCE);
        matGenAn.setElem(PhaseIndexHom.Yp, PhaseIndexHom.Y, dblDefocR * DBL_PERVEANCE);
        matGenAn.setElem(PhaseIndexHom.Zp, PhaseIndexHom.Z, dblDefocZ * DBL_PERVEANCE);
        PhaseMatrix matL0 = PhaseMatrix.identity();
        matL0.setElem(PhaseIndexHom.Z, PhaseIndexHom.Z, gamma);
        matL0.setElem(PhaseIndexHom.Zp, PhaseIndexHom.Zp, 1.0 / gamma);
        PhaseMatrix T = matRot.transpose().times(matL0);
        PhaseMatrix Ti = T.inverse();
        matGenAn = Ti.times(matGenAn.times(T));
        PhaseMatrix matRes = matGenAn.minus(matGenEl);
        double dblErr = matRes.normF();
        Assert.assertTrue(dblErr < TestBeamEllipsoid.ERROR_TOLERANCE);
    }

    /**
     * Test the generator matrix generation for an aligned, offset, relativistic
     * ellipsoidal beam.
     * 
     * We can compute the generator matrix independently in this case and compare
     * it with that produced by the <code>BeamEllipsoid</code> class.
     */
    @Test
    public void testRelOffsetGeneratorMatrix() {
        PhaseMatrix matTau = CorrelationMatrix.buildCorrelation(TWISS_X, TWISS_X, TWISS_Z, VEC_OFFSET);
        double gamma = TestBeamEllipsoid.GAMMA_L3BT;
        BeamEllipsoid ellipsoid = new BeamEllipsoid(gamma, new CorrelationMatrix(matTau));
        PhaseMatrix matGenEl = ellipsoid.computeScheffGenerator(TestBeamEllipsoid.DBL_PERVEANCE);
        double dblEnvR = TWISS_X.getEnvelopeRadius();
        double dblEnvZ = TWISS_Z.getEnvelopeRadius() * gamma;
        double dblR_2 = dblEnvR * dblEnvR;
        double dblZ_2 = dblEnvZ * dblEnvZ;
        double dblDefocR = ((gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDr(dblR_2, dblZ_2);
        double dblDefocZ = ((gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDz(dblR_2, dblZ_2);
        PhaseMatrix matGenAn = PhaseMatrix.zero();
        matGenAn.setElem(PhaseIndexHom.Xp, PhaseIndexHom.X, dblDefocR * DBL_PERVEANCE);
        matGenAn.setElem(PhaseIndexHom.Yp, PhaseIndexHom.Y, dblDefocR * DBL_PERVEANCE);
        matGenAn.setElem(PhaseIndexHom.Zp, PhaseIndexHom.Z, dblDefocZ * DBL_PERVEANCE);
        PhaseMatrix matL0 = PhaseMatrix.identity();
        matL0.setElem(PhaseIndexHom.Z, PhaseIndexHom.Z, gamma);
        matL0.setElem(PhaseIndexHom.Zp, PhaseIndexHom.Zp, 1.0 / gamma);
        PhaseMatrix matT0 = PhaseMatrix.translation(VEC_OFFSET.negate());
        PhaseMatrix T = matT0.times(matL0);
        PhaseMatrix Ti = T.inverse();
        matGenAn = Ti.times(matGenAn.times(T));
        PhaseMatrix matRes = matGenAn.minus(matGenEl);
        double dblErr = matRes.normF();
        Assert.assertTrue(dblErr < TestBeamEllipsoid.ERROR_TOLERANCE);
    }

    /**
     * Test the transfer matrix generation for a rotated stationary
     * ellipsoidal beam.
     * 
     * We can compute the transfer matrix independently in this case and compare
     * it with that produced by the <code>BeamEllipsoid</code> class.
     */
    @Test
    public void testStatRotTransferMatrix() {
        R3x3 matSO3 = R3x3.rotationY(TestBeamEllipsoid.DBL_ANGLE);
        PhaseMatrix matRot = PhaseMatrix.rotationProduct(matSO3);
        CorrelationMatrix matTau0 = CorrelationMatrix.buildCorrelation(TWISS_X, TWISS_X, TWISS_Z);
        PhaseMatrix matTau1 = matTau0.conjugateTrans(matRot);
        CorrelationMatrix matTau = new CorrelationMatrix(matTau1);
        double gamma = TestBeamEllipsoid.GAMMA_STAT;
        BeamEllipsoid ellipsoid = new BeamEllipsoid(gamma, matTau);
        PhaseMatrix matPhiEl = ellipsoid.computeScheffMatrix(TestBeamEllipsoid.DBL_STEP, TestBeamEllipsoid.DBL_PERVEANCE);
        double dblEnvR = TWISS_X.getEnvelopeRadius();
        double dblEnvZ = TWISS_Z.getEnvelopeRadius() * gamma;
        double dblR_2 = dblEnvR * dblEnvR;
        double dblZ_2 = dblEnvZ * dblEnvZ;
        double dblDefocR = ((gamma * gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDr(dblR_2, dblZ_2);
        double dblDefocZ = ((gamma * gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDz(dblR_2, dblZ_2);
        PhaseMatrix matPhiAn = PhaseMatrix.identity();
        matPhiAn.setElem(PhaseIndexHom.Xp, PhaseIndexHom.X, dblDefocR * DBL_STEP * DBL_PERVEANCE);
        matPhiAn.setElem(PhaseIndexHom.Yp, PhaseIndexHom.Y, dblDefocR * DBL_STEP * DBL_PERVEANCE);
        matPhiAn.setElem(PhaseIndexHom.Zp, PhaseIndexHom.Z, dblDefocZ * DBL_STEP * DBL_PERVEANCE);
        PhaseMatrix matL0 = PhaseMatrix.identity();
        matL0.setElem(PhaseIndexHom.Z, PhaseIndexHom.Z, gamma);
        matL0.setElem(PhaseIndexHom.Zp, PhaseIndexHom.Zp, 1.0 / gamma);
        PhaseMatrix T = matRot.transpose().times(matL0);
        PhaseMatrix Ti = T.inverse();
        matPhiAn = Ti.times(matPhiAn.times(T));
        PhaseMatrix matRes = matPhiAn.minus(matPhiEl);
        double dblErr = matRes.normF();
        Assert.assertTrue(dblErr < TestBeamEllipsoid.ERROR_TOLERANCE);
    }

    /**
     * Test the transfer matrix generation for a rotated relativistic 
     * ellipsoidal beam.
     * 
     * We can compute the transfer matrix independently in this case and compare
     * it with that produced by the <code>BeamEllipsoid</code> class.
     */
    @Test
    public void testRotRelTransferMatrix() {
        PhaseMatrix matTau = CorrelationMatrix.buildCorrelation(TWISS_X, TWISS_X, TWISS_Z);
        R3x3 matSO3 = R3x3.rotationY(TestBeamEllipsoid.DBL_ANGLE);
        PhaseMatrix matRot = PhaseMatrix.rotationProduct(matSO3);
        matTau = matTau.conjugateTrans(matRot);
        double gamma = TestBeamEllipsoid.GAMMA_L3BT;
        PhaseMatrix matL0 = PhaseMatrix.identity();
        matL0.setElem(PhaseIndexHom.Z, PhaseIndexHom.Z, gamma);
        matL0.setElem(PhaseIndexHom.Zp, PhaseIndexHom.Zp, 1.0 / gamma);
        PhaseMatrix matL0i = matL0.inverse();
        matTau = matTau.conjugateTrans(matL0i);
        CorrelationMatrix corTau = new CorrelationMatrix(matTau);
        BeamEllipsoid ellipsoid = new BeamEllipsoid(gamma, corTau);
        PhaseMatrix matPhiEl = ellipsoid.computeScheffMatrix(TestBeamEllipsoid.DBL_STEP, TestBeamEllipsoid.DBL_PERVEANCE);
        double dblEnvR = TWISS_X.getEnvelopeRadius();
        double dblEnvZ = TWISS_Z.getEnvelopeRadius();
        double dblR_2 = dblEnvR * dblEnvR;
        double dblZ_2 = dblEnvZ * dblEnvZ;
        double dblDefocR = ((gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDr(dblR_2, dblZ_2);
        double dblDefocZ = ((gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDz(dblR_2, dblZ_2);
        PhaseMatrix matPhiAn = PhaseMatrix.identity();
        matPhiAn.setElem(PhaseIndexHom.Xp, PhaseIndexHom.X, dblDefocR * DBL_STEP * DBL_PERVEANCE);
        matPhiAn.setElem(PhaseIndexHom.Yp, PhaseIndexHom.Y, dblDefocR * DBL_STEP * DBL_PERVEANCE);
        matPhiAn.setElem(PhaseIndexHom.Zp, PhaseIndexHom.Z, dblDefocZ * DBL_STEP * DBL_PERVEANCE);
        PhaseMatrix T = matRot.transpose().times(matL0);
        PhaseMatrix Ti = T.inverse();
        matPhiAn = Ti.times(matPhiAn.times(T));
        PhaseMatrix matRes = matPhiAn.minus(matPhiEl);
        double dblErr = matRes.normF();
        Assert.assertTrue(dblErr < TestBeamEllipsoid.ERROR_TOLERANCE);
    }

    /**
     * Test the transfer matrix generation for a rotated, offset, relativistic 
     * ellipsoidal beam.
     * 
     */
    @Test
    public void testEveryting() {
        PhaseMatrix matTau = CorrelationMatrix.buildCorrelation(TWISS_X, TWISS_X, TWISS_Z, VEC_OFFSET);
        R3x3 matSO3 = R3x3.rotationY(TestBeamEllipsoid.DBL_ANGLE);
        PhaseMatrix matRot = PhaseMatrix.rotationProduct(matSO3);
        matTau = matTau.conjugateTrans(matRot);
        double gamma = TestBeamEllipsoid.GAMMA_L3BT;
        PhaseMatrix matL0 = PhaseMatrix.identity();
        matL0.setElem(PhaseIndexHom.Z, PhaseIndexHom.Z, gamma);
        matL0.setElem(PhaseIndexHom.Zp, PhaseIndexHom.Zp, 1.0 / gamma);
        PhaseMatrix matL0i = matL0.inverse();
        matTau = matTau.conjugateTrans(matL0i);
        CorrelationMatrix corTau = new CorrelationMatrix(matTau);
        BeamEllipsoid ellipsoid = new BeamEllipsoid(gamma, corTau);
        PhaseMatrix matPhiEl = ellipsoid.computeScheffMatrix(TestBeamEllipsoid.DBL_STEP, TestBeamEllipsoid.DBL_PERVEANCE);
        double dblEnvR = TWISS_X.getEnvelopeRadius();
        double dblEnvZ = TWISS_Z.getEnvelopeRadius();
        double dblR_2 = dblEnvR * dblEnvR;
        double dblZ_2 = dblEnvZ * dblEnvZ;
        double dblDefocR = ((gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDr(dblR_2, dblZ_2);
        double dblDefocZ = ((gamma) / BeamEllipsoid.CONST_UNIFORM_BEAM) * EllipticIntegral.symmetricRDz(dblR_2, dblZ_2);
        PhaseMatrix matPhiAn = PhaseMatrix.identity();
        matPhiAn.setElem(PhaseIndexHom.Xp, PhaseIndexHom.X, dblDefocR * DBL_STEP * DBL_PERVEANCE);
        matPhiAn.setElem(PhaseIndexHom.Yp, PhaseIndexHom.Y, dblDefocR * DBL_STEP * DBL_PERVEANCE);
        matPhiAn.setElem(PhaseIndexHom.Zp, PhaseIndexHom.Z, dblDefocZ * DBL_STEP * DBL_PERVEANCE);
        PhaseMatrix matT0 = PhaseMatrix.translation(VEC_OFFSET.negate());
        PhaseMatrix T = matRot.transpose().times(matT0.times(matL0));
        PhaseMatrix Ti = T.inverse();
        matPhiAn = Ti.times(matPhiAn.times(T));
        PhaseMatrix matRes = matPhiAn.minus(matPhiEl);
        double dblErr = matRes.normF();
        Assert.assertTrue(dblErr < TestBeamEllipsoid.ERROR_TOLERANCE);
    }

    /**
      * Check assertions 
      */
    private void checkAssertions() {
    }
}

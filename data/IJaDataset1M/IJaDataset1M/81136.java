package net.java.dev.joode.joint;

import net.java.dev.joode.ClonedReferences;
import net.java.dev.joode.SimState;
import net.java.dev.joode.World;
import net.java.dev.joode.stepper.StepperFunction;
import net.java.dev.joode.util.*;

/**
 * @author tom larkworthy
 */
public class JointConfigurable extends Joint {

    private static final long serialVersionUID = 2125705130258447449L;

    private static final Matrix3 J1 = new Matrix3();

    private static final Matrix3 J2 = new Matrix3();

    private static final Matrix3 O1 = new Matrix3();

    private static final Matrix3 O2 = new Matrix3();

    private static final Matrix3 T = new Matrix3();

    private static final Matrix3 tmpM = new Matrix3();

    private static final Vector3 tmpV = new Vector3();

    private static final Vector3 tmpVa = new Vector3();

    private static final Vector3 a1 = new Vector3();

    private static final Vector3 a2 = new Vector3();

    private static final Vector3 g1 = new Vector3();

    private static final Vector3 g2 = new Vector3();

    private static final Vector3 c = new Vector3();

    private static final Quaternion tmpA = new Quaternion();

    private static final Quaternion tmpB = new Quaternion();

    private static final Quaternion tmpC = new Quaternion();

    private final Vector3 c0 = new Vector3();

    private final Vector3 ch = new Vector3();

    private final Vector3 jerkA3 = new Vector3();

    private final Vector3 jerkA4 = new Vector3();

    /**
     * body relative anchor point from the first body.
     */
    private final Vector3 anchor1 = new Vector3();

    /**
     * body relative anchor point from the second body.
     */
    private final Vector3 anchor2 = new Vector3();

    /**
     * an additional rotation applied to the second bodies frame of reference, or the identity frame if there is no second body, to
     * form the linear frame of reference (which defines the three orthogonal linear axis for constraint)
     */
    private final Matrix3 t2 = new Matrix3();

    /**
     * an additional rotation applied to the second bodies frame of reference, or the identity frame if there is no second body, to
     * form the angular frame of reference (which defines the three orthogonal angular axis for constraint)
     */
    private final Quaternion rotT2 = new Quaternion();

    /**
     * Error Reduction Parameter (ERP) settings for linear unbounded dimentions
     */
    private final Vector3 linearERP = new Vector3();

    /**
     * Constraint Force Mixing (CFM) settings for linear unbounded dimentions
     */
    private final Vector3 linearCFM = new Vector3();

    /**
     * Error Reduction Parameter (ERP) settings for angular unbounded dimentions
     */
    private final Vector3 angularERP = new Vector3();

    /**
     * Constraint Force Mixing (CFM) settings for angular unbounded dimentions
     */
    private final Vector3 angularCFM = new Vector3();

    /**
     * these objects hold the parameters for linear dimentions that are constrained by motors.
     */
    private JointLimitMotor[] linearLimitMotors = new JointLimitMotor[3];

    /**
     * these objects hold the parameters for angular dimentions that are constrained by motors.
     */
    private JointLimitMotor[] angularLimitMotors = new JointLimitMotor[3];

    /**
     * the constraint types for each of the three linear dimentions.
     * Either NO_CONSTRAINT, UNBOUNDED_CONSTRAINT or MOTOR_CONSTRAINT.
     * Parameters for each of the constraint types are set in either (linearERP and linearCFM) for
     * unbounded constraints) or (linearLimitMotor) for motor constrained dimentions. The axis of each
     * dimention is defined by the linear frame or reference.
     */
    private final int linearConstraints[] = new int[3];

    /**
     * the constraint types for each of the three angular dimentions.
     * Either NO_CONSTRAINT, UNBOUNDED_CONSTRAINT or MOTOR_CONSTRAINT.
     * Parameters for each of the constraint types are set in either (angularERP and angularCFM) for
     * unbounded constraints) or (angularLimitMotor) for motor constrained dimentions. The axis of each
     * dimention is defined by the angular frame of referenece.
     */
    private final int angularConstraints[] = new int[3];

    /**
     * indicates a joint dimention has no constraint enforced upon it
     */
    public static final int NO_CONSTRAINT = 0;

    /**
     * indicates a joint dimention has an unbounded constraint enforced upon it. The joint will attempt
     * to reduce the error between the two bodies along the dimention by use of the ERP and CFM setting for the dimention
     */
    public static final int UNBOUNDED_CONSTRAINT = 1;

    /**
     * indicates a joint dimention has an motor constraint enforced upon it. The joint will apply
     * the motor settings for the diminesion
     */
    public static final int MOTOR_CONSTRAINT = 2;

    public JointConfigurable(World world) {
        super(world);
        for (int i = 0; i < 3; i++) {
            linearERP.m[i] = world.getGlobalERP();
            linearCFM.m[i] = world.getGlobalCFM();
            angularERP.m[i] = world.getGlobalERP();
            angularCFM.m[i] = world.getGlobalCFM();
            linearLimitMotors[i] = new JointLimitMotor(world);
            angularLimitMotors[i] = new JointLimitMotor(world);
        }
    }

    /**
     * Sets the body relative anchor point from the first body.
     * 
     * @param x
     * @param y
     * @param z
     */
    public final void setAnchor1(float x, float y, float z) {
        this.anchor1.set(x, y, z);
    }

    /**
     * Sets the body relative anchor point from the first body.
     * 
     * @param anchor
     */
    public final void setAnchor1(Vector3 anchor) {
        this.anchor1.set(anchor);
    }

    /**
     * @return the body relative anchor point from the first body
     */
    public final Vector3 getAnchor1() {
        return anchor1;
    }

    /**
     * Sets the body relative anchor point from the second body.
     * 
     * @param x
     * @param y
     * @param z
     */
    public final void setAnchor2(float x, float y, float z) {
        this.anchor2.set(x, y, z);
    }

    /**
     * Sets the body relative anchor point from the second body.
     * 
     * @param anchor
     */
    public final void setAnchor2(Vector3 anchor) {
        this.anchor2.set(anchor);
    }

    /**
     * @return the body relative anchor point from the second body.
     */
    public final Vector3 getAnchor2() {
        return anchor2;
    }

    /**
     * an additional rotation applied to the second bodies frame of reference, or the identity frame if there is no second body, to
     * form the linear frame of reference (which defines the three orthogonal linear axis for constraint)
     * 
     * @param t2
     */
    public final void setAdditionalRotationL2(Matrix3 t2) {
        this.t2.set(t2);
    }

    /**
     * an additional rotation applied to the second bodies frame of reference, or the identity frame if there is no second body, to
     * form the linear frame of reference (which defines the three orthogonal linear axis for constraint)
     */
    public final Matrix3 getAdditionalRotationL2() {
        return t2;
    }

    /**
     * an additional rotation applied to the second bodies frame of reference, or the identity frame if there is no second body, to
     * form the angular frame of reference (which defines the three orthogonal angular axis for constraint)
     * 
     * @param rotT2
     */
    public final void setAdditionalRotationR2(Quaternion rotT2) {
        this.rotT2.set(rotT2);
    }

    /**
     * an additional rotation applied to the second bodies frame of reference, or the identity frame if there is no second body, to
     * form the angular frame of reference (which defines the three orthogonal angular axis for constraint)
     */
    public final Quaternion getAdditionalRotationR2() {
        return rotT2;
    }

    /**
     * Error Reduction Parameter (ERP) settings for linear unbounded dimentions
     * 
     * @param linearERP
     */
    public final void setLinearERP(Vector3 linearERP) {
        this.linearERP.set(linearERP);
    }

    /**
     * Error Reduction Parameter (ERP) settings for linear unbounded dimentions
     */
    public final Vector3 getLinearERP() {
        return linearERP;
    }

    /**
     * Constraint Force Mixing (CFM) settings for linear unbounded dimentions
     * 
     * @param linearCFM
     */
    public final void setLinearCFM(Vector3 linearCFM) {
        this.linearCFM.set(linearCFM);
    }

    /**
     * Constraint Force Mixing (CFM) settings for linear unbounded dimentions
     */
    public final Vector3 getLinearCFM() {
        return linearCFM;
    }

    /**
     * Error Reduction Parameter (ERP) settings for angular unbounded dimentions
     * 
     * @param angularERP
     */
    public final void setAngularERP(Vector3 angularERP) {
        this.angularERP.set(angularERP);
    }

    /**
     * Error Reduction Parameter (ERP) settings for angular unbounded dimentions
     */
    public final Vector3 getAngularERP() {
        return angularERP;
    }

    /**
     * Constraint Force Mixing (CFM) settings for angular unbounded dimentions
     * 
     * @param angularCFM
     */
    public final void setAngularCFM(Vector3 angularCFM) {
        this.angularCFM.set(angularCFM);
    }

    /**
     * Constraint Force Mixing (CFM) settings for angular unbounded dimentions
     */
    public final Vector3 getAngularCFM() {
        return angularCFM;
    }

    public final int getNumLinearLimitMotors() {
        return linearLimitMotors.length;
    }

    /**
     * these objects hold the parameters for linear dimentions that are constrained by motors.
     * 
     * @param i
     * @param limot
     */
    public final void setLinearLimitMotor(int i, JointLimitMotor limot) {
        this.linearLimitMotors[i] = limot;
    }

    /**
     * these objects hold the parameters for linear dimentions that are constrained by motors.
     * 
     * @param i
     */
    public final JointLimitMotor getLinearLimitMotor(int i) {
        return linearLimitMotors[i];
    }

    /**
     * these objects hold the parameters for angular dimentions that are constrained by motors.
     */
    public final int getNumAngularLimitMotors() {
        return angularLimitMotors.length;
    }

    /**
     * these objects hold the parameters for angular dimentions that are constrained by motors.
     * 
     * @param i
     * @param limot
     */
    public final void setAngularLimitMotor(int i, JointLimitMotor limot) {
        this.angularLimitMotors[i] = limot;
    }

    /**
     * these objects hold the parameters for angular dimentions that are constrained by motors.
     * 
     * @param i
     */
    public final JointLimitMotor getAngularLimitMotor(int i) {
        return angularLimitMotors[i];
    }

    /**
     * the constraint types for each of the three linear dimentions.
     * Either NO_CONSTRAINT, UNBOUNDED_CONSTRAINT or MOTOR_CONSTRAINT.
     * Parameters for each of the constraint types are set in either (linearERP and linearCFM) for
     * unbounded constraints) or (linearLimitMotor) for motor constrained dimentions. The axis of each
     * dimention is defined by the linear frame or reference.
     */
    public final int getNumLinearConstraints() {
        return linearConstraints.length;
    }

    /**
     * the constraint types for each of the three linear dimentions.
     * Either NO_CONSTRAINT, UNBOUNDED_CONSTRAINT or MOTOR_CONSTRAINT.
     * Parameters for each of the constraint types are set in either (linearERP and linearCFM) for
     * unbounded constraints) or (linearLimitMotor) for motor constrained dimentions. The axis of each
     * dimention is defined by the linear frame or reference.
     * 
     * @param i
     * @param value
     */
    public final void setLinearConstraints(int i, int value) {
        this.linearConstraints[i] = value;
    }

    /**
     * the constraint types for each of the three linear dimentions.
     * Either NO_CONSTRAINT, UNBOUNDED_CONSTRAINT or MOTOR_CONSTRAINT.
     * Parameters for each of the constraint types are set in either (linearERP and linearCFM) for
     * unbounded constraints) or (linearLimitMotor) for motor constrained dimentions. The axis of each
     * dimention is defined by the linear frame or reference.
     * 
     * @param i
     */
    public final int getLinearConstraints(int i) {
        return linearConstraints[i];
    }

    /**
     * the constraint types for each of the three angular dimentions.
     * Either NO_CONSTRAINT, UNBOUNDED_CONSTRAINT or MOTOR_CONSTRAINT.
     * Parameters for each of the constraint types are set in either (angularERP and angularCFM) for
     * unbounded constraints) or (angularLimitMotor) for motor constrained dimentions. The axis of each
     * dimention is defined by the angular frame of referenece.
     */
    public final int getNumAngularConstraints() {
        return angularConstraints.length;
    }

    /**
     * the constraint types for each of the three angular dimentions.
     * Either NO_CONSTRAINT, UNBOUNDED_CONSTRAINT or MOTOR_CONSTRAINT.
     * Parameters for each of the constraint types are set in either (angularERP and angularCFM) for
     * unbounded constraints) or (angularLimitMotor) for motor constrained dimentions. The axis of each
     * dimention is defined by the angular frame of referenece.
     * 
     * @param i
     * @param value
     */
    public final void setAngularConstraints(int i, int value) {
        this.angularConstraints[i] = value;
    }

    /**
     * the constraint types for each of the three angular dimentions.
     * Either NO_CONSTRAINT, UNBOUNDED_CONSTRAINT or MOTOR_CONSTRAINT.
     * Parameters for each of the constraint types are set in either (angularERP and angularCFM) for
     * unbounded constraints) or (angularLimitMotor) for motor constrained dimentions. The axis of each
     * dimention is defined by the angular frame of referenece.
     * 
     * @param i
     */
    public final int getAngularConstraints(int i) {
        return angularConstraints[i];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getInfo1(Info1 info1) {
        Vector3 linearDistance = Vector3.pool.aquire();
        Vector3 angularDistance = Vector3.pool.aquire();
        getLinearDistance(linearDistance);
        if (angularConstraints[0] != NO_CONSTRAINT || angularConstraints[1] != NO_CONSTRAINT || angularConstraints[2] != NO_CONSTRAINT) {
            getAngularDistance(angularDistance);
        }
        for (int i = 0; i < 3; i++) {
            switch(linearConstraints[i]) {
                case UNBOUNDED_CONSTRAINT:
                    info1.m++;
                    info1.nub++;
                    break;
                case MOTOR_CONSTRAINT:
                    if ((linearLimitMotors[i].getLowStop() > Float.NEGATIVE_INFINITY || linearLimitMotors[i].getHighStop() < Float.POSITIVE_INFINITY) && linearLimitMotors[i].getLowStop() <= linearLimitMotors[i].getHighStop()) {
                        if (linearDistance.m[i] <= linearLimitMotors[i].getLowStop()) {
                            linearLimitMotors[i].setLimit(1);
                            linearLimitMotors[i].setLimitError(linearDistance.m[i] - linearLimitMotors[i].getLowStop());
                            info1.m++;
                        } else if (linearDistance.m[i] >= linearLimitMotors[i].getHighStop()) {
                            linearLimitMotors[i].setLimit(2);
                            linearLimitMotors[i].setLimitError(linearDistance.m[i] - linearLimitMotors[i].getHighStop());
                            info1.m++;
                        } else {
                            linearLimitMotors[i].setLimit(0);
                            if (linearLimitMotors[i].getMaxForce() > 0f) {
                                info1.m++;
                            }
                        }
                    } else {
                        if (linearLimitMotors[i].getMaxForce() > 0f) {
                            linearLimitMotors[i].setLimit(0);
                            info1.m++;
                        }
                    }
                    break;
                default:
                    assert linearConstraints[i] == NO_CONSTRAINT;
            }
            switch(angularConstraints[i]) {
                case UNBOUNDED_CONSTRAINT:
                    info1.m++;
                    info1.nub++;
                    break;
                case MOTOR_CONSTRAINT:
                    if ((angularLimitMotors[i].getLowStop() > Float.NEGATIVE_INFINITY || angularLimitMotors[i].getHighStop() < Float.POSITIVE_INFINITY) && angularLimitMotors[i].getLowStop() <= angularLimitMotors[i].getHighStop()) {
                        if (angularDistance.m[i] <= angularLimitMotors[i].getLowStop()) {
                            angularLimitMotors[i].setLimit(1);
                            angularLimitMotors[i].setLimitError(angularDistance.m[i] - angularLimitMotors[i].getLowStop());
                            info1.m++;
                        } else if (angularDistance.m[i] >= angularLimitMotors[i].getHighStop()) {
                            angularLimitMotors[i].setLimit(2);
                            angularLimitMotors[i].setLimitError(angularDistance.m[i] - angularLimitMotors[i].getHighStop());
                            info1.m++;
                        } else {
                            angularLimitMotors[i].setLimit(0);
                            if (angularLimitMotors[i].getMaxForce() > 0f) {
                                info1.m++;
                            }
                        }
                    } else {
                        if (angularLimitMotors[i].getMaxForce() > 0f) {
                            angularLimitMotors[i].setLimit(0);
                            info1.m++;
                        }
                    }
                    break;
                default:
                    assert angularConstraints[i] == NO_CONSTRAINT;
            }
        }
        Vector3.pool.release(angularDistance);
        Vector3.pool.release(linearDistance);
    }

    /**
     * looks at each linear dimention and measures the distance between the two anchors,
     * in frame 2
     *
     * @param passback the three distances for each dimention
     */
    public Vector3 getLinearDistance(Vector3 passback) {
        getAnchor1InGlobalCoords(g1);
        getAnchor2InGlobalCoords(g2);
        c.sub(g2);
        c.set(g1);
        convertGlobalFrameToFrame2(c, passback);
        return passback;
    }

    /**
     * looks at each angular dimention and measures the distance between two rotations
     *
     * @param passback the three angular distances for each dimention
     */
    public Vector3 getAngularDistance(Vector3 passback) {
        Quaternion frame2Q;
        Matrix3 frame2M = Matrix3.pool.aquire();
        if (getBody(1) != null) {
            frame2Q = getBody(1).getQuaternion().mul(rotT2, Quaternion.pool.aquire());
        } else {
            frame2Q = Quaternion.pool.aquire();
            frame2Q.set(rotT2);
        }
        frame2Q.toMatrix(frame2M);
        Vector3 axis1 = Vector3.pool.aquire();
        Vector3 axis2 = Vector3.pool.aquire();
        Vector3 axis3 = Vector3.pool.aquire();
        frame2M.getColumn(0, axis1);
        frame2M.getColumn(1, axis2);
        frame2M.getColumn(2, axis3);
        Quaternion.getAngles(getBody(0).getQuaternion(), frame2Q, axis1, axis2, axis3, passback);
        Matrix3.pool.release(frame2M);
        Quaternion.pool.release(frame2Q);
        Vector3.pool.release(axis1);
        Vector3.pool.release(axis2);
        Vector3.pool.release(axis3);
        return passback;
    }

    /**
     * writes the constraint equations into the Info2 object
     *
     * @param jinfo
     */
    @Override
    public void getInfo2(Info2 jinfo) {
        getAnchor1InGlobalFrame(a1);
        getAnchor2InGlobalFrame(a2);
        getAnchor1InGlobalCoords(g1);
        getAnchor2InGlobalCoords(g2);
        c.set(g2);
        c.sub(g1);
        if (jinfo.stepType != StepperFunction.EULER) {
            if (jinfo.t == 0) {
                c0.set(c);
                convertGlobalFrameToFrame2(c0, tmpV);
                for (int j = 0; j < 3; j++) {
                    tmpVa.m[j] = tmpV.m[j] * (1 - linearERP.m[j]);
                }
                convertFrame2CoordsToGlobalFrame(tmpVa, ch);
                for (int i = 0; i < 3; i++) {
                    if (getBody(1) != null) {
                        jerkA3.m[i] = +3 * (c0.m[i] - ch.m[i]) * jinfo.fps * jinfo.fps - 3 * (getBody(0).getLinearVel().m[i] - getBody(1).getLinearVel().m[i]) * jinfo.fps;
                        jerkA4.m[i] = -2 * (c0.m[i] - ch.m[i]) * jinfo.fps * jinfo.fps * jinfo.fps + 2 * (getBody(0).getLinearVel().m[i] - getBody(1).getLinearVel().m[i]) * jinfo.fps * jinfo.fps;
                    } else {
                        jerkA3.m[i] = +3 * (c0.m[i] - ch.m[i]) * jinfo.fps * jinfo.fps - 3 * getBody(0).getLinearVel().m[i] * jinfo.fps;
                        jerkA4.m[i] = -2 * (c0.m[i] - ch.m[i]) * jinfo.fps * jinfo.fps * jinfo.fps + 2 * getBody(0).getLinearVel().m[i] * jinfo.fps * jinfo.fps;
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                if (getBody(1) != null) {
                    c.m[i] = (2 * jerkA3.m[i] + 6 * jerkA4.m[i] * jinfo.t / jinfo.fps) / jinfo.fps + (getBody(0).getLinearVel().m[i] - getBody(1).getLinearVel().m[i]);
                } else {
                    c.m[i] = (2 * jerkA3.m[i] + 6 * jerkA4.m[i] * jinfo.t / jinfo.fps) / jinfo.fps + (getBody(0).getLinearVel().m[i]);
                }
            }
        }
        J1.setIdentity();
        a1.bar(O1);
        O1.scale(-1);
        if (getBody(1) != null) {
            J2.setIdentity();
            J2.scale(-1);
            a2.bar(O2);
        }
        getGlobalFrameToFrame2(T);
        T.mul(J1, tmpM);
        J1.set(tmpM);
        T.mul(O1, tmpM);
        O1.set(tmpM);
        T.mul(J2, tmpM);
        J2.set(tmpM);
        T.mul(O2, tmpM);
        O2.set(tmpM);
        tmpV.setZero();
        T.mulInc(c, tmpV);
        c.set(tmpV);
        int jrowskip = jinfo.rowskip;
        int mrowskip = J1.getColumns();
        int currentRow = 0;
        for (int i = 0; i < 3; i++) {
            if (linearConstraints[i] == UNBOUNDED_CONSTRAINT) {
                for (int c = 0; c < 3; c++) {
                    jinfo.J1l.setValue(c + currentRow * jrowskip, J1.m[c + i * mrowskip]);
                    jinfo.J1a.setValue(c + currentRow * jrowskip, O1.m[c + i * mrowskip]);
                    if (getBody(1) != null) {
                        jinfo.J2l.setValue(c + currentRow * jrowskip, J2.m[c + i * mrowskip]);
                        jinfo.J2a.setValue(c + currentRow * jrowskip, O2.m[c + i * mrowskip]);
                    }
                }
                if (jinfo.stepType != StepperFunction.EULER) {
                    jinfo.c.setValue(currentRow, c.m[i]);
                } else {
                    jinfo.c.setValue(currentRow, jinfo.fps * linearERP.m[i] * c.m[i]);
                }
                jinfo.cfm.setValue(currentRow, linearCFM.m[i]);
                currentRow++;
            } else if (linearConstraints[i] == MOTOR_CONSTRAINT) {
                Vector3 axis = Vector3.pool.aquire();
                T.getColumn(i, axis);
                if (linearLimitMotors[i].addLimot(this, jinfo, currentRow, axis, false)) {
                    currentRow++;
                }
                Vector3.pool.release(axis);
            }
        }
        if (angularConstraints[0] != NO_CONSTRAINT || angularConstraints[1] != NO_CONSTRAINT || angularConstraints[2] != NO_CONSTRAINT) {
            J1.setZero();
            J2.setZero();
            O1.setIdentity();
            O2.set(O1);
            O2.scale(-1);
            Quaternion frame2Q;
            Matrix3 frame2M = T;
            if (getBody(1) != null) {
                tmpC.setZero();
                frame2Q = getBody(1).getQuaternion().mul(rotT2, tmpC);
            } else {
                frame2Q = tmpC;
                frame2Q.set(rotT2);
            }
            frame2Q.toMatrix(frame2M);
            Vector3 axis1 = Vector3.pool.aquire();
            Vector3 axis2 = Vector3.pool.aquire();
            Vector3 axis3 = Vector3.pool.aquire();
            frame2M.getColumn(0, axis1);
            frame2M.getColumn(1, axis2);
            frame2M.getColumn(2, axis3);
            Quaternion.weightedSLERP(getBody(0).getQuaternion(), frame2Q, axis1, axis2, axis3, angularERP, null, tmpA);
            Vector3.pool.release(axis1);
            Vector3.pool.release(axis2);
            Vector3.pool.release(axis3);
            tmpB.set(getBody(0).getQuaternion());
            tmpB.invert();
            Quaternion change = tmpC;
            tmpA.mul(tmpB, change);
            getBody(0).getAngularVelocityToTurn(change, jinfo.fps, c);
            frame2M.transpose();
            frame2M.mul(O1, tmpM);
            O1.set(tmpM);
            frame2M.mul(O2, tmpM);
            O2.set(tmpM);
            tmpV.setZero();
            for (int i = 0; i < 3; i++) {
                if (angularConstraints[i] == UNBOUNDED_CONSTRAINT) {
                    for (int c = 0; c < 3; c++) {
                        jinfo.J1a.setValue(c + (currentRow) * jrowskip, O1.m[c + i * mrowskip]);
                        if (getBody(1) != null) {
                            jinfo.J2a.setValue(c + (currentRow) * jrowskip, O2.m[c + i * mrowskip]);
                        }
                    }
                    jinfo.c.setValue(currentRow, c.m[i]);
                    jinfo.cfm.setValue(currentRow, angularCFM.m[i]);
                    currentRow++;
                } else if (angularConstraints[i] == MOTOR_CONSTRAINT) {
                    Vector3 axis = Vector3.pool.aquire();
                    frame2M.getColumn(i, axis);
                    JOODELog.debug("axis = ", axis);
                    if (angularLimitMotors[i].addLimot(this, jinfo, currentRow, axis, true)) {
                        currentRow++;
                    }
                    Vector3.pool.release(axis);
                }
            }
        }
    }

    public SimState cloneState(ClonedReferences util) {
        return null;
    }

    /**
     * *************COORDINATE CONVERSION UTILITY METHODS************?
     * /**
     * takes global frame body relative coordinates, and converts them
     * to body relative body frame coordinates (for frame 1, the first body)
     */
    public Vector3 convertGlobalCoordsToFrame1Coords(Vector3 relative, Vector3 passback) {
        passback.setZero();
        tmpV.set(relative);
        tmpV.sub((getBody(0).getPosition()));
        getBody(0).getRotation().transposeMul(tmpV, passback);
        return passback;
    }

    /**
     * takes global coordinates, and converts them to body relative body frame coordinates
     * (for frame 2, the second body or global if the second body does not exist)
     */
    public Vector3 convertGlobalCoordsToFrame2Coords(Vector3 relative, Vector3 passback) {
        passback.setZero();
        if (getBody(1) != null) {
            tmpVa.setZero();
            tmpV.set(relative);
            tmpV.sub(getBody(1).getPosition());
            getBody(1).getRotation().transposeMul(tmpV, tmpVa);
            t2.transposeMul(tmpVa, passback);
        } else {
            t2.transposeMul(relative, passback);
        }
        return passback;
    }

    /**
     * takes global frame vectors, and converts them to frame2 vectors
     * (for frame 2, the second body or global if the second body does not exist)
     */
    public Vector3 convertGlobalFrameToFrame2(Vector3 relative, Vector3 passback) {
        passback.setZero();
        if (getBody(1) != null) {
            tmpVa.setZero();
            getBody(1).getRotation().transposeMul(relative, tmpVa);
            t2.transposeMul(tmpVa, passback);
        } else {
            t2.transposeMul(relative, passback);
        }
        return passback;
    }

    /**
     * takes relative coordinates, assumed to be in frame 1
     * (coordinates in relation to the first body),
     * and returns the same coordinates in the global frame.
     * NOTE although the returned coords use the global frame of reference
     * they are still relative to the first body
     */
    public Vector3 convertFrame1CoordsToGlobalFrame(Vector3 relative, Vector3 passback) {
        passback.setZero();
        getBody(0).getRotation().mulInc(relative, passback);
        return passback;
    }

    /**
     * takes relative coordinates, assumed to be in frame 2
     * (coordinates in relation to the second body, or the global frame if there is no second body)
     * and returns the same coordinates in the global frame.
     * NOTE although the returned coords use the global frame of reference
     * they are still relative to the second body if applicable
     */
    public Vector3 convertFrame2CoordsToGlobalFrame(Vector3 relative, Vector3 passback) {
        passback.setZero();
        tmpV.setZero();
        t2.mulInc(relative, tmpV);
        if (getBody(1) != null) getBody(1).getRotation().mulInc(tmpV, passback); else passback.set(tmpV);
        return passback;
    }

    /**
     * takes relative coordinates, assumed to be in frame 1
     * (coordinates in relation to the first body),
     * and returns the same coordinates in the global frame using the global origin
     */
    public Vector3 convertFrame1CoordsToGlobalCoords(Vector3 relative, Vector3 passback) {
        convertFrame1CoordsToGlobalFrame(relative, passback);
        passback.add(getBody(0).getPosition());
        return passback;
    }

    /**
     * takes relative coordinates, assumed to be in frame 2
     * (coordinates in relation to the second body, or the global frame if there is no second body)
     * and returns the same coordinates in the global frame using the global origin
     */
    public Vector3 convertFrame2CoordsToGlobalCoords(Vector3 relative, Vector3 passback) {
        convertFrame2CoordsToGlobalFrame(relative, passback);
        if (getBody(1) != null) passback.add(getBody(1).getPosition());
        return passback;
    }

    /**
     * returns the bodyRelAnchor attached to the first body in the global frame of reference
     */
    public Vector3 getAnchor1InGlobalFrame(Vector3 passback) {
        return convertFrame1CoordsToGlobalFrame(anchor1, passback);
    }

    /**
     * returns the bodyRelAnchor attached to the second body in the global frame of reference,
     * or, if no second body is present, the global bodyRelAnchor
     */
    public Vector3 getAnchor2InGlobalFrame(Vector3 passback) {
        return convertFrame2CoordsToGlobalFrame(anchor2, passback);
    }

    /**
     * returns the bodyRelAnchor attached to the first body in the global coordinates
     */
    public Vector3 getAnchor1InGlobalCoords(Vector3 passback) {
        return convertFrame1CoordsToGlobalCoords(anchor1, passback);
    }

    /**
     * returns the bodyRelAnchor attached to the second body in global coordinates,
     * or, if no second body is present, the global bodyRelAnchor
     */
    public Vector3 getAnchor2InGlobalCoords(Vector3 passback) {
        return convertFrame2CoordsToGlobalCoords(anchor2, passback);
    }

    /**
     * returns a rotation matrix that can convert from global frame to linear frame 2
     *
     * @param passback
     */
    public void getGlobalFrameToFrame2(Matrix3 passback) {
        if (getBody(1) != null) {
            t2.mul(getBody(1).getRotation(), passback);
        } else {
            passback.set(t2);
        }
        passback.transpose();
    }
}

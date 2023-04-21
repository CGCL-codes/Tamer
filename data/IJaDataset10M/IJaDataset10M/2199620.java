package org.ode4j.ode.internal.joints;

import static org.ode4j.ode.OdeConstants.dInfinity;
import static org.ode4j.ode.OdeMath.dCalcVectorCross3;
import static org.ode4j.ode.OdeMath.dCalcVectorDot3;
import static org.ode4j.ode.OdeMath.dMultiply0_331;
import static org.ode4j.ode.internal.Common.M_PI;
import static org.ode4j.ode.internal.Rotation.dQMultiply1;
import org.ode4j.math.DMatrix3;
import org.ode4j.math.DMatrix3C;
import org.ode4j.math.DQuaternion;
import org.ode4j.math.DVector3;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.DPRJoint;
import org.ode4j.ode.internal.DxWorld;

/**
 *  Prismatic and Rotoide.
 * 
 * The axisP must be perpendicular to axis2
 * <PRE>
 *                                        +-------------+
 *                                        |      x      |
 *                                        +------------\+
 * Prismatic articulation                   ..     ..
 *                       |                ..     ..
 *                      \/              ..      ..
 * +--------------+    --|        __..      ..  anchor2
 * |      x       | .....|.......(__)     ..
 * +--------------+    --|         ^     <
 *        |----------------------->|
 *            Offset               |--- Rotoide articulation
 * </PRE>
 */
public class DxJointPR extends DxJoint implements DPRJoint {

    /**
	 * Position of the rotoide articulation w.r.t second body.
	 * @note Position of body 2 in world frame + anchor2 in world frame give 
	 * the position of the rotoide articulation.
	 */
    DVector3 _anchor2;

    /** 
	 * Axis of the rotoide articulation w.r.t first body.
	 * @note This is considered as axis1 from the parameter view.
	 */
    DVector3 axisR1;

    /** Axis of the rotoide articulation w.r.t second body.
	 * @note This is considered also as axis1 from the parameter view.
	 */
    DVector3 axisR2;

    /** Axis for the prismatic articulation w.r.t first body.
	 * @note This is considered as axis2 in from the parameter view.
	 */
    DVector3 axisP1;

    /** Initial relative rotation body1 -> body2. */
    DQuaternion qrel;

    /** 
	 * Vector between the body1 and the rotoide articulation.
	 *  
	 * Going from the first to the second in the frame of body1.
	 * That should be aligned with body1 center along axisP.
	 * This is calculated when the axis are set.
	 */
    DVector3 offset;

    /** limit and motor information for the rotoide articulation. */
    public DxJointLimitMotor limotR;

    /** limit and motor information for the prismatic articulation. */
    public DxJointLimitMotor limotP;

    DxJointPR(DxWorld w) {
        super(w);
        _anchor2 = new DVector3();
        axisR1 = new DVector3(1, 0, 0);
        axisR2 = new DVector3(1, 0, 0);
        axisP1 = new DVector3(0, 1, 0);
        qrel = new DQuaternion();
        offset = new DVector3();
        limotR = new DxJointLimitMotor();
        limotR.init(world);
        limotP = new DxJointLimitMotor();
        limotP.init(world);
    }

    double dJointGetPRPosition() {
        DVector3 q = new DVector3();
        dMultiply0_331(q, node[0].body.posr().R(), offset);
        if (node[1].body != null) {
            DVector3 anchor2 = new DVector3();
            dMultiply0_331(anchor2, node[1].body.posr().R(), _anchor2);
            q.eqSum(node[0].body.posr().pos(), q).sub(node[1].body.posr().pos()).sub(anchor2);
        } else {
            q.eqSum(node[0].body.posr().pos(), q).sub(_anchor2);
            if (isFlagsReverse()) {
                q.scale(-1);
            }
        }
        DVector3 axP = new DVector3();
        dMultiply0_331(axP, node[0].body.posr().R(), axisP1);
        return dCalcVectorDot3(axP, q);
    }

    public double dJointGetPRPositionRate() {
        DVector3 ax1 = new DVector3();
        dMultiply0_331(ax1, node[0].body.posr().R(), axisP1);
        if (node[1].body != null) {
            DVector3 lv2 = new DVector3();
            node[1].body.dBodyGetRelPointVel(_anchor2, lv2);
            return dCalcVectorDot3(ax1, node[0].body.lvel) - dCalcVectorDot3(ax1, lv2);
        } else {
            double rate = ax1.dot(node[0].body.lvel);
            return (isFlagsReverse() ? -rate : rate);
        }
    }

    double dJointGetPRAngle() {
        if (node[0].body != null) {
            double ang = getHingeAngle(node[0].body, node[1].body, axisR1, qrel);
            if (isFlagsReverse()) return -ang; else return ang;
        } else return 0;
    }

    double dJointGetPRAngleRate() {
        if (node[0].body != null) {
            DVector3 axis = new DVector3();
            dMultiply0_331(axis, node[0].body.posr().R(), axisR1);
            double rate = dCalcVectorDot3(axis, node[0].body.avel);
            if (node[1].body != null) rate -= dCalcVectorDot3(axis, node[1].body.avel);
            if (isFlagsReverse()) rate = -rate;
            return rate;
        } else return 0;
    }

    @Override
    void getSureMaxInfo(SureMaxInfo info) {
        info.max_m = 6;
    }

    @Override
    public void getInfo1(DxJoint.Info1 info) {
        info.setNub(4);
        info.setM(4);
        limotP.limit = 0;
        if ((limotP.lostop > -dInfinity || limotP.histop < dInfinity) && limotP.lostop <= limotP.histop) {
            double pos = dJointGetPRPosition();
            limotP.testRotationalLimit(pos);
        }
        if (limotP.limit != 0 || limotP.fmax > 0) info.incM();
        limotR.limit = 0;
        if ((limotR.lostop >= -M_PI || limotR.histop <= M_PI) && limotR.lostop <= limotR.histop) {
            double angle = getHingeAngle(node[0].body, node[1].body, axisR1, qrel);
            limotR.testRotationalLimit(angle);
        }
        if (limotR.limit != 0 || limotR.fmax > 0) info.incM();
    }

    @Override
    public void getInfo2(DxJoint.Info2 info) {
        int s = info.rowskip();
        int s2 = 2 * s;
        int s3 = 3 * s;
        double k = info.fps * info.erp;
        DVector3 q = new DVector3();
        DVector3C pos1, pos2 = null;
        DMatrix3C R1 = new DMatrix3(), R2 = null;
        pos1 = node[0].body.posr().pos();
        R1 = node[0].body.posr().R();
        if (node[1].body != null) {
            pos2 = node[1].body.posr().pos();
            R2 = node[1].body.posr().R();
        } else {
        }
        DVector3 axP = new DVector3();
        dMultiply0_331(axP, R1, axisP1);
        DVector3 wanchor2 = new DVector3(0, 0, 0), dist = new DVector3();
        if (node[1].body != null) {
            dMultiply0_331(wanchor2, R2, _anchor2);
            dist.eqSum(wanchor2, pos2).sub(pos1);
        } else {
            if (isFlagsReverse()) {
                dist.eqDiff(pos1, _anchor2);
            } else {
                dist.eqDiff(_anchor2, pos1);
            }
        }
        DVector3 ax1 = new DVector3();
        dMultiply0_331(ax1, node[0].body.posr().R(), axisR1);
        dCalcVectorCross3(q, ax1, axP);
        info._J[info.J1ap + 0] = axP.get0();
        info._J[info.J1ap + 1] = axP.get1();
        info._J[info.J1ap + 2] = axP.get2();
        info._J[info.J1ap + s + 0] = q.get0();
        info._J[info.J1ap + s + 1] = q.get1();
        info._J[info.J1ap + s + 2] = q.get2();
        if (node[1].body != null) {
            info._J[info.J2ap + 0] = -axP.get0();
            info._J[info.J2ap + 1] = -axP.get1();
            info._J[info.J2ap + 2] = -axP.get2();
            info._J[info.J2ap + s + 0] = -q.get0();
            info._J[info.J2ap + s + 1] = -q.get1();
            info._J[info.J2ap + s + 2] = -q.get2();
        }
        DVector3 ax2 = new DVector3();
        if (node[1].body != null) {
            dMultiply0_331(ax2, R2, axisR2);
        } else {
            ax2.set(axisR2);
        }
        DVector3 b = new DVector3();
        dCalcVectorCross3(b, ax1, ax2);
        info.setC(0, k * dCalcVectorDot3(b, axP));
        info.setC(1, k * dCalcVectorDot3(b, q));
        dCalcVectorCross3(info._J, (info.J1ap) + s2, dist, ax1);
        dCalcVectorCross3(info._J, (info.J1ap) + s3, dist, q);
        info._J[info.J1lp + s2 + 0] = ax1.get0();
        info._J[info.J1lp + s2 + 1] = ax1.get1();
        info._J[info.J1lp + s2 + 2] = ax1.get2();
        info._J[info.J1lp + s3 + 0] = q.get0();
        info._J[info.J1lp + s3 + 1] = q.get1();
        info._J[info.J1lp + s3 + 2] = q.get2();
        if (node[1].body != null) {
            dCalcVectorCross3(info._J, (info.J2ap) + s2, ax2, wanchor2);
            dCalcVectorCross3(info._J, (info.J2ap) + s3, q, wanchor2);
            info._J[info.J2lp + s2 + 0] = -ax1.get0();
            info._J[info.J2lp + s2 + 1] = -ax1.get1();
            info._J[info.J2lp + s2 + 2] = -ax1.get2();
            info._J[info.J2lp + s3 + 0] = -q.get0();
            info._J[info.J2lp + s3 + 1] = -q.get1();
            info._J[info.J2lp + s3 + 2] = -q.get2();
        }
        DVector3 err = new DVector3();
        dMultiply0_331(err, R1, offset);
        err.eqDiff(dist, err);
        info.setC(2, k * dCalcVectorDot3(ax1, err));
        info.setC(3, k * dCalcVectorDot3(q, err));
        int row = 4;
        if (node[1].body != null || !isFlagsReverse()) {
            row += limotP.addLimot(this, info, 4, axP, false);
        } else {
            DVector3 rAxP = new DVector3();
            rAxP.sub(axP);
            row += limotP.addLimot(this, info, 4, rAxP, false);
        }
        limotR.addLimot(this, info, row, ax1, true);
    }

    void computeInitialRelativeRotation() {
        if (node[0].body != null) {
            if (node[1].body != null) {
                dQMultiply1(qrel, node[0].body._q, node[1].body._q);
            } else {
                qrel.set(0, node[0].body._q.get(0));
                for (int i = 1; i < 4; i++) qrel.set(i, -node[0].body._q.get(i));
            }
        }
    }

    public void dJointSetPRAnchor(double x, double y, double z) {
        dJointSetPRAnchor(new DVector3(x, y, z));
    }

    public void dJointSetPRAnchor(DVector3C xyz) {
        setAnchors(xyz, offset, _anchor2);
    }

    public void dJointSetPRAxis1(double x, double y, double z) {
        setAxes(x, y, z, axisP1, null);
        computeInitialRelativeRotation();
    }

    public void dJointSetPRAxis2(double x, double y, double z) {
        setAxes(x, y, z, axisR1, axisR2);
        computeInitialRelativeRotation();
    }

    public void dJointSetPRParam(PARAM_N parameter, double value) {
        if (parameter.isGroup2()) {
            limotR.set(parameter.toSUB(), value);
        } else {
            limotP.set(parameter.toSUB(), value);
        }
    }

    public void dJointGetPRAnchor(DVector3 result) {
        if (node[1].body != null) getAnchor2(result, _anchor2); else {
            result.set(_anchor2);
        }
    }

    void dJointGetPRAxis1(DVector3 result) {
        getAxis(result, axisP1);
    }

    void dJointGetPRAxis2(DVector3 result) {
        getAxis(result, axisR1);
    }

    public double dJointGetPRParam(PARAM_N parameter) {
        if (parameter.isGroup2()) {
            return limotR.get(parameter.toSUB());
        } else {
            return limotP.get(parameter.toSUB());
        }
    }

    void dJointAddPRTorque(double torque) {
        DVector3 axis = new DVector3();
        if (isFlagsReverse()) torque = -torque;
        getAxis(axis, axisR1);
        axis.scale(torque);
        if (node[0].body != null) node[0].body.dBodyAddTorque(axis.get0(), axis.get1(), axis.get2());
        if (node[1].body != null) node[1].body.dBodyAddTorque(-axis.get0(), -axis.get1(), -axis.get2());
    }

    @Override
    void setRelativeValues() {
        DVector3 anchor = new DVector3();
        dJointGetPRAnchor(anchor);
        setAnchors(anchor, offset, _anchor2);
        DVector3 axis = new DVector3();
        dJointGetPRAxis1(axis);
        setAxes(axis, axisP1, null);
        dJointGetPRAxis2(axis);
        setAxes(axis, axisR1, axisR2);
        computeInitialRelativeRotation();
    }

    public void setAnchor(double x, double y, double z) {
        dJointSetPRAnchor(x, y, z);
    }

    public void setAnchor(DVector3C a) {
        dJointSetPRAnchor(a);
    }

    public void setAxis1(double x, double y, double z) {
        dJointSetPRAxis1(x, y, z);
    }

    public void setAxis1(DVector3C a) {
        setAxis1(a.get0(), a.get1(), a.get2());
    }

    public void setAxis2(double x, double y, double z) {
        dJointSetPRAxis2(x, y, z);
    }

    public void setAxis2(DVector3C a) {
        setAxis2(a.get0(), a.get1(), a.get2());
    }

    public void getAnchor(DVector3 result) {
        dJointGetPRAnchor(result);
    }

    public void getAxis1(DVector3 result) {
        dJointGetPRAxis1(result);
    }

    public void getAxis2(DVector3 result) {
        dJointGetPRAxis2(result);
    }

    @Override
    public double getAngle() {
        return dJointGetPRAngle();
    }

    @Override
    public double getAngleRate() {
        return dJointGetPRAngleRate();
    }

    public double getPosition() {
        return dJointGetPRPosition();
    }

    public double getPositionRate() {
        return dJointGetPRPositionRate();
    }

    @Override
    public void setParam(PARAM_N parameter, double value) {
        dJointSetPRParam(parameter, value);
    }

    @Override
    public double getParam(PARAM_N parameter) {
        return dJointGetPRParam(parameter);
    }

    @Override
    public void setParamHiStop(double d) {
        dJointSetPRParam(PARAM_N.dParamHiStop1, d);
    }

    @Override
    public void setParamLoStop(double d) {
        dJointSetPRParam(PARAM_N.dParamLoStop1, d);
    }

    @Override
    public void setParamHiStop2(double d) {
        dJointSetPRParam(PARAM_N.dParamHiStop2, d);
    }

    @Override
    public void setParamLoStop2(double d) {
        dJointSetPRParam(PARAM_N.dParamLoStop2, d);
    }

    @Override
    public void setParamFMax2(double d) {
        dJointSetPRParam(PARAM_N.dParamFMax2, d);
    }

    @Override
    public void setParamVel2(double d) {
        dJointSetPRParam(PARAM_N.dParamVel2, d);
    }

    @Override
    public void addTorque(double torque) {
        dJointAddPRTorque(torque);
    }
}

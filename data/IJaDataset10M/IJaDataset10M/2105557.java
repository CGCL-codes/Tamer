package org.ode4j.ode;

import org.ode4j.math.DVector3;
import org.ode4j.math.DVector3C;

public interface DLMotorJoint extends DJoint {

    /**
	 * Set the number of axes that will be controlled by the LMotor.
	 * @param num can range from 0 (which effectively deactivates the joint) to 3.
	 * @ingroup joints
	 */
    void setNumAxes(int num);

    /**
	 * Get nr of axes.
	 * @ingroup joints
	 */
    int getNumAxes();

    /**
	 * Set the AMotor axes.
	 * @param anum selects the axis to change (0,1 or 2).
	 * @param rel Each axis can have one of three ``relative orientation'' modes
	 * <li> 0: The axis is anchored to the global frame.
	 * <li> 1: The axis is anchored to the first body.
	 * <li> 2: The axis is anchored to the second body.
	 * @remarks The axis vector is always specified in global coordinates
	 * regardless of the setting of rel.
	 * @ingroup joints
	 */
    void setAxis(int anum, int rel, double x, double y, double z);

    /**
	 * Set the AMotor axes.
	 * @param anum selects the axis to change (0,1 or 2).
	 * @param rel Each axis can have one of three ``relative orientation'' modes
	 * <li> 0: The axis is anchored to the global frame.
	 * <li> 1: The axis is anchored to the first body.
	 * <li> 2: The axis is anchored to the second body.
	 * @remarks The axis vector is always specified in global coordinates
	 * regardless of the setting of rel.
	 * @ingroup joints
	 */
    void setAxis(int anum, int rel, DVector3C a);

    /**
	 * Get axis.
	 * @ingroup joints
	 */
    void getAxis(int anum, DVector3 result);

    /**
	 * Set joint parameter.
	 * @ingroup joints
	 */
    @Override
    void setParam(PARAM_N parameter, double value);

    /**
	 * Get joint parameter.
	 * @ingroup joints
	 */
    @Override
    double getParam(PARAM_N parameter);

    double getParamVel();

    double getParamVel2();

    double getParamVel3();

    double getParamFMax();

    double getParamFMax2();

    double getParamFMax3();

    void setParamVel(double d);

    void setParamVel2(double d);

    void setParamVel3(double d);

    void setParamFMax(double d);

    void setParamFMax2(double d);

    void setParamFMax3(double d);
}

package org.ode4j.cpp.internal;

import org.ode4j.math.DVector3;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;

/**
 * @defgroup world World
 *
 * The world object is a container for rigid bodies and joints. Objects in
 * different worlds can not interact, for example rigid bodies from two
 * different worlds can not collide.
 *
 * All the objects in a world exist at the same point in time, thus one
 * reason to use separate worlds is to simulate systems at different rates.
 * Most applications will only need one world.
 */
public abstract class ApiCppWorld extends ApiCppBody {

    public static DWorld dWorldCreate() {
        return OdeHelper.createWorld();
    }

    public static void dWorldDestroy(DWorld world) {
        world.destroy();
    }

    public static void dWorldSetGravity(DWorld w, double x, double y, double z) {
        w.setGravity(x, y, z);
    }

    public static void dWorldGetGravity(DWorld w, DVector3 gravity) {
        w.getGravity(gravity);
    }

    public static void dWorldSetERP(DWorld w, double erp) {
        w.setERP(erp);
    }

    public static double dWorldGetERP(DWorld w) {
        return w.getERP();
    }

    public static void dWorldSetCFM(DWorld w, double cfm) {
        w.setCFM(cfm);
    }

    public static double dWorldGetCFM(DWorld w) {
        return w.getCFM();
    }

    public static void dWorldStep(DWorld w, double stepsize) {
        w.step(stepsize);
    }

    public static void dWorldImpulseToForce(DWorld w, double stepsize, double ix, double iy, double iz, DVector3 force) {
        w.impulseToForce(stepsize, ix, iy, iz, force);
    }

    public static void dWorldQuickStep(DWorld w, double stepsize) {
        w.quickStep(stepsize);
    }

    public static void dWorldSetQuickStepNumIterations(DWorld w, int num) {
        w.setQuickStepNumIterations(num);
    }

    public static int dWorldGetQuickStepNumIterations(DWorld w) {
        return w.getQuickStepNumIterations();
    }

    public static void dWorldSetQuickStepW(DWorld w, double over_relaxation) {
        w.setQuickStepW(over_relaxation);
    }

    public static double dWorldGetQuickStepW(DWorld w) {
        return w.getQuickStepW();
    }

    public static void dWorldSetContactMaxCorrectingVel(DWorld w, double vel) {
        w.setContactMaxCorrectingVel(vel);
    }

    public static double dWorldGetContactMaxCorrectingVel(DWorld w) {
        return w.getContactMaxCorrectingVel();
    }

    public static void dWorldSetContactSurfaceLayer(DWorld w, double depth) {
        w.setContactSurfaceLayer(depth);
    }

    public static double dWorldGetContactSurfaceLayer(DWorld w) {
        return w.getContactSurfaceLayer();
    }

    public static double dWorldGetAutoDisableLinearThreshold(DWorld w) {
        return w.getAutoDisableLinearThreshold();
    }

    public static void dWorldSetAutoDisableLinearThreshold(DWorld w, double linear_threshold) {
        w.setAutoDisableLinearThreshold(linear_threshold);
    }

    public static double dWorldGetAutoDisableAngularThreshold(DWorld w) {
        return w.getAutoDisableAngularThreshold();
    }

    public static void dWorldSetAutoDisableAngularThreshold(DWorld w, double angular_threshold) {
        w.setAutoDisableAngularThreshold(angular_threshold);
    }

    public static double dWorldGetAutoDisableLinearAverageThreshold(DWorld w) {
        return w.getAutoDisableLinearAverageThreshold();
    }

    public static void dWorldSetAutoDisableLinearAverageThreshold(DWorld w, double linear_average_threshold) {
        w.setAutoDisableLinearAverageThreshold(linear_average_threshold);
    }

    public static double dWorldGetAutoDisableAngularAverageThreshold(DWorld w) {
        return w.getAutoDisableAngularAverageThreshold();
    }

    public static void dWorldSetAutoDisableAngularAverageThreshold(DWorld w, double angular_average_threshold) {
        w.setAutoDisableAngularAverageThreshold(angular_average_threshold);
    }

    public static int dWorldGetAutoDisableAverageSamplesCount(DWorld w) {
        return w.getAutoDisableAverageSamplesCount();
    }

    public static void dWorldSetAutoDisableAverageSamplesCount(DWorld w, int average_samples_count) {
        w.setAutoDisableAverageSamplesCount(average_samples_count);
    }

    public static int dWorldGetAutoDisableSteps(DWorld w) {
        return w.getAutoDisableSteps();
    }

    public static void dWorldSetAutoDisableSteps(DWorld w, int steps) {
        w.setAutoDisableSteps(steps);
    }

    public static double dWorldGetAutoDisableTime(DWorld w) {
        return w.getAutoDisableTime();
    }

    public static void dWorldSetAutoDisableTime(DWorld w, double time) {
        w.setAutoDisableTime(time);
    }

    public static boolean dWorldGetAutoDisableFlag(DWorld w) {
        return w.getAutoDisableFlag();
    }

    public static void dWorldSetAutoDisableFlag(DWorld w, boolean do_auto_disable) {
        w.setAutoDisableFlag(do_auto_disable);
    }

    public static double dWorldGetLinearDampingThreshold(DWorld w) {
        return w.getLinearDampingThreshold();
    }

    public static void dWorldSetLinearDampingThreshold(DWorld w, double threshold) {
        w.setLinearDampingThreshold(threshold);
    }

    public static double dWorldGetAngularDampingThreshold(DWorld w) {
        return w.getAngularDampingThreshold();
    }

    public static void dWorldSetAngularDampingThreshold(DWorld w, double threshold) {
        w.setAngularDampingThreshold(threshold);
    }

    public static double dWorldGetLinearDamping(DWorld w) {
        return w.getLinearDamping();
    }

    public static void dWorldSetLinearDamping(DWorld w, double scale) {
        w.setLinearDamping(scale);
    }

    public static double dWorldGetAngularDamping(DWorld w) {
        return w.getAngularDamping();
    }

    public static void dWorldSetAngularDamping(DWorld w, double scale) {
        w.setAngularDamping(scale);
    }

    public static void dWorldSetDamping(DWorld w, double linear_scale, double angular_scale) {
        w.setDamping(linear_scale, angular_scale);
    }

    public static double dWorldGetMaxAngularSpeed(DWorld w) {
        return w.getMaxAngularSpeed();
    }

    public static void dWorldSetMaxAngularSpeed(DWorld w, double max_speed) {
        w.setMaxAngularSpeed(max_speed);
    }
}

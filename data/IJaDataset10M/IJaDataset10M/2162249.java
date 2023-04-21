package jp.seraph.jsade.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.seraph.jsade.effector.Effector;
import jp.seraph.jsade.effector.EffectorBuilder;
import jp.seraph.jsade.effector.EffectorKind;
import jp.seraph.jsade.math.Angle;
import jp.seraph.jsade.math.AngleVelocity;
import jp.seraph.jsade.model.AngleVelocityCalculator;
import jp.seraph.jsade.model.DefaultAngleVelocityCalculator;
import jp.seraph.jsade.model.ForceResistance;
import jp.seraph.jsade.model.GyroValue;
import jp.seraph.jsade.model.Joint;
import jp.seraph.jsade.model.JointIdentifier;
import jp.seraph.jsade.model.ModelManager;
import jp.seraph.jsade.model.ModelObjectIdentifier;
import jp.seraph.jsade.model.ModelPart;

/**
 * Playerのデフォルト実装です。
 *
 *
 */
public class DefaultPlayer implements Player {

    public DefaultPlayer(EffectorBuilder aBuilder, ModelManager aManager) {
        mBuilder = aBuilder;
        mManager = aManager;
        mDefaultVelocityCalculator = new DefaultAngleVelocityCalculator();
    }

    private EffectorBuilder mBuilder;

    private ModelManager mManager;

    private Map<JointIdentifier, AngleVelocity> mAngleVelocityMap = new HashMap<JointIdentifier, AngleVelocity>();

    private AngleVelocityCalculator mDefaultVelocityCalculator;

    /**
     *
     * @see jp.seraph.jsade.core.Player#getJointAngle(jp.seraph.jsade.model.JointIdentifier)
     */
    public Angle getJointAngle(JointIdentifier aID) {
        return mManager.getJoint(aID).getAngle();
    }

    /**
     *
     * @see jp.seraph.jsade.core.Player#getJointAngleVelocity(jp.seraph.jsade.model.JointIdentifier)
     */
    public AngleVelocity getJointAngleVelocity(JointIdentifier aID) {
        AngleVelocity tResult = mAngleVelocityMap.get(aID);
        if (tResult == null) return AngleVelocity.ZERO;
        return tResult;
    }

    /**
     *
     * @see jp.seraph.jsade.core.Player#setJointAngle(jp.seraph.jsade.model.JointIdentifier,
     *      jp.seraph.jsade.math.Angle)
     */
    public AngleVelocity setJointAngle(JointIdentifier aID, Angle aTargetAngle) {
        return this.setJointAngle(aID, aTargetAngle, mDefaultVelocityCalculator);
    }

    /**
     *
     * @see jp.seraph.jsade.core.Player#setJointAngle(jp.seraph.jsade.model.JointIdentifier,
     *      jp.seraph.jsade.math.Angle, double)
     */
    public AngleVelocity setJointAngle(JointIdentifier aID, Angle aTargetAngle, AngleVelocityCalculator aCalculator) {
        return this.setJointAngleVelocity(aID, aCalculator.calc(this, aID, aTargetAngle));
    }

    /**
     *
     * @see jp.seraph.jsade.core.Player#setJointAngleVelocity(jp.seraph.jsade.model.JointIdentifier,
     *      jp.seraph.jsade.math.Angle)
     */
    public AngleVelocity setJointAngleVelocity(JointIdentifier aID, AngleVelocity aAngleVelocity) {
        mBuilder.appendJoint(mManager.getJoint(aID), aAngleVelocity);
        mAngleVelocityMap.put(aID, aAngleVelocity);
        return aAngleVelocity;
    }

    /**
     *
     * @see jp.seraph.jsade.core.Player#getJoint(jp.seraph.jsade.model.JointIdentifier)
     */
    public Joint getJoint(JointIdentifier aID) {
        return mManager.getJoint(aID);
    }

    /**
     *
     * @see jp.seraph.jsade.core.Player#getAllJoints()
     */
    public List<Joint> getAllJoints() {
        return mManager.getAllJoints();
    }

    /**
     *
     * @see jp.seraph.jsade.core.Player#getObject(jp.seraph.jsade.model.ModelObjectIdentifier)
     */
    public ModelPart getObject(ModelObjectIdentifier aID) {
        return mManager.getObject(aID);
    }

    /**
     *
     * @see jp.seraph.jsade.core.Player#getAllObjects()
     */
    public List<ModelPart> getAllObjects() {
        return mManager.getAllObjects();
    }

    /**
     *
     * @see jp.seraph.jsade.core.Player#getGyroValue()
     */
    public GyroValue getGyroValue() {
        return mManager.getGyroValue();
    }

    /**
     *
     * @see jp.seraph.jsade.core.Player#getTotalGyroValue()
     */
    public GyroValue getTotalGyroValue() {
        return mManager.getTotalGyroValue();
    }

    /**
     *
     * @see jp.seraph.jsade.core.Player#getLeftFootForceResistance()
     */
    public ForceResistance getLeftFootForceResistance() {
        return mManager.getLeftFootForceResistance();
    }

    /**
     *
     * @see jp.seraph.jsade.core.Player#getRightFootForceResistance()
     */
    public ForceResistance getRightFootForceResistance() {
        return mManager.getRightFootForceResistance();
    }

    /**
     * @param aEffector
     * @throws IllegalArgumentException
     * @see jp.seraph.jsade.effector.EffectorBuilder#append(jp.seraph.jsade.effector.Effector)
     */
    public void append(Effector aEffector) throws IllegalArgumentException {
        mBuilder.append(aEffector);
    }

    /**
     * @param aEffector
     * @param aValue
     * @throws IllegalArgumentException
     * @see jp.seraph.jsade.effector.EffectorBuilder#append(jp.seraph.jsade.effector.EffectorKind,
     *      double[])
     */
    public void append(EffectorKind aEffector, double... aValue) throws IllegalArgumentException {
        mBuilder.append(aEffector, aValue);
    }

    /**
     * @param aEffector
     * @param aValue
     * @throws IllegalArgumentException
     * @see jp.seraph.jsade.effector.EffectorBuilder#append(jp.seraph.jsade.effector.EffectorKind,
     *      double)
     */
    public void append(EffectorKind aEffector, double aValue) throws IllegalArgumentException {
        mBuilder.append(aEffector, aValue);
    }

    /**
     * @param aEffector
     * @param aValue
     * @throws IllegalArgumentException
     * @see jp.seraph.jsade.effector.EffectorBuilder#append(jp.seraph.jsade.effector.EffectorKind,
     *      int[])
     */
    public void append(EffectorKind aEffector, int... aValue) throws IllegalArgumentException {
        mBuilder.append(aEffector, aValue);
    }

    /**
     * @param aEffector
     * @param aValue
     * @throws IllegalArgumentException
     * @see jp.seraph.jsade.effector.EffectorBuilder#append(jp.seraph.jsade.effector.EffectorKind,
     *      int)
     */
    public void append(EffectorKind aEffector, int aValue) throws IllegalArgumentException {
        mBuilder.append(aEffector, aValue);
    }

    /**
     * @param aEffector
     * @param aValue
     * @throws IllegalArgumentException
     * @see jp.seraph.jsade.effector.EffectorBuilder#append(jp.seraph.jsade.effector.EffectorKind,
     *      java.lang.String[])
     */
    public void append(EffectorKind aEffector, String... aValue) throws IllegalArgumentException {
        mBuilder.append(aEffector, aValue);
    }

    /**
     * @param aEffector
     * @param aValue
     * @throws IllegalArgumentException
     * @see jp.seraph.jsade.effector.EffectorBuilder#append(jp.seraph.jsade.effector.EffectorKind,
     *      java.lang.String)
     */
    public void append(EffectorKind aEffector, String aValue) throws IllegalArgumentException {
        mBuilder.append(aEffector, aValue);
    }

    /**
     * @param aX
     * @param aY
     * @param aZ
     * @throws IllegalArgumentException
     * @see jp.seraph.jsade.effector.EffectorBuilder#appendBeam(double, double,
     *      double)
     */
    public void appendBeam(double aX, double aY, double aZ) throws IllegalArgumentException {
        mBuilder.appendBeam(aX, aY, aZ);
    }

    /**
     * @param aTargetJoint
     * @param aValue
     * @throws IllegalArgumentException
     * @see jp.seraph.jsade.effector.EffectorBuilder#appendJoint(jp.seraph.jsade.model.Joint,
     *      double)
     */
    public void appendJoint(Joint aTargetJoint, double aValue) throws IllegalArgumentException {
        mBuilder.appendJoint(aTargetJoint, aValue);
    }

    /**
     *
     * @see jp.seraph.jsade.effector.EffectorBuilder#appendJoint(jp.seraph.jsade.model.Joint, jp.seraph.jsade.math.AngleVelocity)
     */
    public void appendJoint(Joint aTargetJoint, AngleVelocity aVelocity) throws IllegalArgumentException {
        mBuilder.appendJoint(aTargetJoint, aVelocity);
    }

    /**
     *
     * @see jp.seraph.jsade.effector.EffectorBuilder#appendSay(java.lang.String)
     */
    public void appendSay(String aMessage) {
        mBuilder.appendSay(aMessage);
    }

    /**
     * @return
     * @see jp.seraph.jsade.effector.EffectorBuilder#buildEffector()
     */
    public List<Effector> buildEffector() {
        return mBuilder.buildEffector();
    }

    /**
     *
     * @see jp.seraph.jsade.effector.EffectorBuilder#clear()
     */
    public void clear() {
        mBuilder.clear();
    }
}

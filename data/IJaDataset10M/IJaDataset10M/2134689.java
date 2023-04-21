package physics.physx;

import com.JPhysX.NxActor;
import com.JPhysX.NxActorDesc;
import com.JPhysX.NxBitField;
import com.JPhysX.NxBodyDesc;
import com.JPhysX.NxBoxShape;
import com.JPhysX.NxBoxShapeDesc;
import com.JPhysX.NxCapsuleShape;
import com.JPhysX.NxCapsuleShapeDesc;
import com.JPhysX.NxD6Joint;
import com.JPhysX.NxD6JointDriveType;
import com.JPhysX.NxFixedJoint;
import com.JPhysX.NxFixedJointDesc;
import com.JPhysX.NxIsoUniversalJointDesc;
import com.JPhysX.NxJointDriveDesc;
import com.JPhysX.NxJointProjectionMode;
import com.JPhysX.NxMat33;
import com.JPhysX.NxMotorDesc;
import com.JPhysX.NxQuat;
import com.JPhysX.NxRevoluteJoint;
import com.JPhysX.NxRevoluteJointDesc;
import com.JPhysX.NxScene;
import com.JPhysX.NxShape;
import com.JPhysX.NxShapeDesc;
import com.JPhysX.NxSphereShape;
import com.JPhysX.NxSphereShapeDesc;
import com.JPhysX.NxVec3;
import java.util.Hashtable;
import java.util.Vector;
import javax.vecmath.Matrix3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import physics.AtomicShape;
import physics.agent.SoccerbotHumanoid;
import utility.math.TDMath;

public class SoccerbotHumanoidPhysx extends SoccerbotHumanoid {

    private float sizeScale = 1f;

    private float massScale = 0.0001f;

    private float footLength = 0.6f * sizeScale;

    private float footWidth = 0.956f * sizeScale;

    private float footHeight = 0.095f * sizeScale;

    private float footMass = 0.1f * massScale;

    private float ankleBoxLength = 0.355f * sizeScale;

    private float ankleBoxWidth = 0.143f * sizeScale;

    private float ankleBoxHeight = 0.476f * sizeScale;

    private float shankLength = 0.52f * sizeScale;

    private float shankWidth = 0.45f * sizeScale;

    private float shankHeight = 0.964f * sizeScale;

    private float shankMass = 0.25f * massScale;

    private float thighLength = 0.56f * sizeScale;

    private float thighWidth = 0.45f * sizeScale;

    private float thighHeight = 1.3f * sizeScale;

    private float thighMass = 0.25f * massScale;

    private float hipLength = 0.273f * sizeScale;

    private float hipWidth = 0.273f * sizeScale;

    private float hipHeight = 0.3f * sizeScale;

    private float hipMass = 0.1f * massScale;

    private float handBox1Length = 0.082f * sizeScale;

    private float handBox1Width = 0.272f * sizeScale;

    private float handBox1Height = 0.57f * sizeScale;

    private float handBox2Length = 0.242f * sizeScale;

    private float handBox2Width = 0.265f * sizeScale;

    private float handBox2Height = 0.164f * sizeScale;

    private float handBox3Length = 0.074f * sizeScale;

    private float handBox3Width = 0.272f * sizeScale;

    private float handBox3Height = 0.2f * sizeScale;

    private float lowerarmLength = 0.445f * sizeScale;

    private float lowerarmWidth = 0.316f * sizeScale;

    private float lowerarmHeight = 0.6f * sizeScale;

    private float lowerarmMass = 0.2f * massScale;

    private float elbowDirection = 1f * sizeScale;

    private float elbowRadius = 0.134f * sizeScale;

    private float elbowLength = 0.3f * sizeScale;

    private float upperarmLength = 0.445f * sizeScale;

    private float upperarmWidth = 0.398f * sizeScale;

    private float upperarmHeight = 0.506f * sizeScale;

    private float upperarmMass = 0.2f * massScale;

    private float shoulderLength = 0.445f * sizeScale;

    private float shoulderWidth = 1.017f * sizeScale;

    private float shoulderHeight = 0.536f * sizeScale;

    private float shoulderMass = 0.5f * massScale;

    private float headRadius = 0.39f * sizeScale;

    private float headMass = 0.3f * massScale;

    private float upperTorsoLength = 1.37f * sizeScale;

    private float upperTorsoWidth = 0.96f * sizeScale;

    private float upperTorsoHeight = 1.00f * sizeScale;

    private float upperTorsoMass = 1.2f * massScale;

    private float lowerTorsoLength = 1.32f * sizeScale;

    private float lowerTorsoWidth = 0.55f * sizeScale;

    private float lowerTorsoHeight = 0.55f * sizeScale;

    private float lowerTorsoMass = 0.60f * massScale;

    private float torsoCylinderDirection = 1f;

    private float torsoCylinderRadius = 0.1f * sizeScale;

    private float torsoCylinderLength = 0.3f * sizeScale;

    private float rle1Min = -90;

    private float rle1Max = 60;

    private float rle2Min = -45;

    private float rle2Max = 120;

    private float rle3Min = -75;

    private float rle3Max = 45;

    private float rle4Min = -160;

    private float rle4Max = 10;

    private float rle5Min = -90;

    private float rle5Max = 90;

    private float rle6Min = -45;

    private float rle6Max = 45;

    private float lle1Min = -60;

    private float lle1Max = 90;

    private float lle2Min = -45;

    private float lle2Max = 120;

    private float lle3Min = -45;

    private float lle3Max = 75;

    private float lle4Min = -160;

    private float lle4Max = 10;

    private float lle5Min = -90;

    private float lle5Max = 90;

    private float lle6Min = -45;

    private float lle6Max = 45;

    private float rae1Min = -90;

    private float rae1Max = 180;

    private float rae2Min = -180;

    private float rae2Max = 10;

    private float rae3Min = -135;

    private float rae3Max = 135;

    private float rae4Min = -10;

    private float rae4Max = 130;

    private float lae1Min = -90;

    private float lae1Max = 180;

    private float lae2Min = -10;

    private float lae2Max = 180;

    private float lae3Min = -135;

    private float lae3Max = 135;

    private float lae4Min = -10;

    private float lae4Max = 130;

    private float upperTorsoPosX = 0 * sizeScale;

    private float upperTorsoPosY = 3.75f * sizeScale;

    private float upperTorsoPosZ = 0 * sizeScale;

    private float standingPosZ = (upperTorsoHeight / 2 + hipHeight + thighHeight + shankHeight + footHeight) * sizeScale;

    private float offsetTorsoBox2X = 0.0f * sizeScale;

    private float offsetTorsoBox2Y = (-1 * ((upperTorsoWidth / 2.0f) + (lowerTorsoWidth / 2.0f))) * sizeScale;

    private float offsetTorsoBox2Z = (-1 * ((upperTorsoHeight / 2.0f) - (lowerTorsoHeight / 2.0f))) * sizeScale;

    private float offsetLeftShoulderCylX = (-1 * (upperTorsoLength / 2.0f)) * sizeScale;

    private float offsetLeftShoulderCylZ = 0.0f * sizeScale;

    private float offsetLeftShoulderCylY = ((upperTorsoHeight / 2.0f) - (shoulderHeight / 2.0f)) * sizeScale;

    private float offsetRightShoulderCylX = (upperTorsoLength / 2.0f) * sizeScale;

    private float offsetRightShoulderCylZ = 0.0f * sizeScale;

    private float offsetRightShoulderCylY = ((upperTorsoHeight / 2.0f) - (shoulderHeight / 2.0f)) * sizeScale;

    public Quat4f initialQuaternion;

    private Hashtable bodies = new Hashtable();

    private Hashtable joints = new Hashtable();

    private Hashtable motors = new Hashtable();

    private NxActor lowerTorso;

    private NxActor head;

    private NxActor leftFoot;

    private NxActor leftHip;

    private NxActor leftLowerArm;

    private NxActor leftShank;

    private NxActor leftShoulder;

    private NxActor leftThigh;

    private NxActor leftUpperArm;

    private NxActor neck;

    private NxActor rightFoot;

    private NxActor rightHip;

    private NxActor rightLowerArm;

    private NxActor rightShank;

    private NxActor rightShoulder;

    private NxActor rightThigh;

    private NxActor rightUpperArm;

    private NxActor torso;

    private NxCapsuleShapeDesc leftShoulderCapsuleDesc;

    private NxCapsuleShapeDesc rightShoulderCapsuleDesc;

    private NxShapeDesc rightShoulderCylinder;

    private NxShapeDesc staticLeftCylinderElbow;

    private NxShapeDesc staticRightCylinderElbow;

    private NxShapeDesc staticLeftHand1;

    private NxShapeDesc staticLeftHand2;

    private NxShapeDesc staticLeftHand3;

    private NxShapeDesc staticRightHand1;

    private NxShapeDesc staticRightHand2;

    private NxShapeDesc staticRightHand3;

    private NxShapeDesc staticLeftBackFoot;

    private NxShapeDesc staticRightBackFoot;

    private NxRevoluteJoint leftLowerTorsoJoint;

    private NxRevoluteJoint rightLowerTorsoJoint;

    private NxFixedJoint headJoint;

    private NxRevoluteJoint leftHipJoint;

    private NxRevoluteJoint leftLowerArmJonit;

    private NxRevoluteJoint leftShankJoint;

    private NxRevoluteJoint leftUpperArmJoint;

    private NxRevoluteJoint rightHipJoint;

    private NxRevoluteJoint rightLowerArmJoint;

    private NxRevoluteJoint rightShankJoint;

    private NxRevoluteJoint rightUpperArmJoint;

    private NxD6Joint leftFootJoint;

    private NxD6Joint leftShoulderJoint;

    private NxD6Joint leftThighJoint;

    private NxD6Joint rightFootJoint;

    private NxD6Joint rightShoulderJoint;

    private NxD6Joint rightThighJoint;

    private NxScene physicScene;

    private NxMotorDesc revMotor = new NxMotorDesc();

    /**
     * @param physicScene Novodex world model
     * @param posx x position of torso
     * @param posy y position of torso
     * @param posz z position of torso
     */
    public SoccerbotHumanoidPhysx(NxScene physicScene, float posx, float posy, float posz) {
        super();
        this.physicScene = physicScene;
        upperTorsoPosX = posx;
        upperTorsoPosY = posy;
        upperTorsoPosZ = posz;
        makeHoap2();
        initBodies();
    }

    /**
     * @param physicScene Novodex world model
     * @param posx x position of torso
     * @param posy y position of torso     
     */
    public SoccerbotHumanoidPhysx(NxScene physicScene, float posx, float posz) {
        this(physicScene, posx, 3.75f, posz);
    }

    /**
     * @param physicScene Novodex world model
     */
    public SoccerbotHumanoidPhysx(NxScene physicScene) {
        this(physicScene, 0, 3.75f, 0f);
    }

    private void makeHoap2() {
        NxActorDesc torsoActorDesc = new NxActorDesc();
        NxBodyDesc torsoBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc torsoBoxShapeDesc = new NxBoxShapeDesc();
        torsoBoxShapeDesc.getDimensions().set(upperTorsoLength / 2, upperTorsoHeight / 2, upperTorsoWidth / 2);
        torsoBoxShapeDesc.getLocalPose().getT().set(0, 0, 0);
        torsoActorDesc.getShapes().push_back(torsoBoxShapeDesc);
        leftShoulderCapsuleDesc = new NxCapsuleShapeDesc();
        leftShoulderCapsuleDesc.setRadius(torsoCylinderRadius);
        leftShoulderCapsuleDesc.setHeight(torsoCylinderLength);
        leftShoulderCapsuleDesc.getLocalPose().getT().set(offsetLeftShoulderCylX, offsetLeftShoulderCylY, offsetLeftShoulderCylZ);
        leftShoulderCapsuleDesc.getLocalPose().getM().setRowMajor(TDMath.getZRotation(90));
        torsoActorDesc.getShapes().push_back(leftShoulderCapsuleDesc);
        rightShoulderCapsuleDesc = new NxCapsuleShapeDesc();
        rightShoulderCapsuleDesc.setRadius(torsoCylinderRadius);
        rightShoulderCapsuleDesc.setHeight(torsoCylinderLength);
        rightShoulderCapsuleDesc.getLocalPose().getT().set(offsetRightShoulderCylX, offsetRightShoulderCylY, offsetRightShoulderCylZ);
        rightShoulderCapsuleDesc.getLocalPose().getM().setRowMajor(TDMath.getZRotation(90));
        torsoActorDesc.getShapes().push_back(rightShoulderCapsuleDesc);
        torsoActorDesc.setBody(torsoBodyDesc);
        torsoActorDesc.setDensity(20);
        torsoActorDesc.getGlobalPose().getT().set(upperTorsoPosX, upperTorsoPosY, upperTorsoPosZ);
        torso = physicScene.createActor(torsoActorDesc);
        torso.setMass(upperTorsoMass);
        torso.setAngularDamping(0);
        torso.setMaxAngularVelocity(10);
        float lowerTorsoPosX = upperTorsoPosX;
        float lowerTorsoPosZ = upperTorsoPosZ;
        float lowerTorsoPosY = upperTorsoPosY - (upperTorsoHeight / 2.0f) - (lowerTorsoHeight / 2.0f) + 0.25f;
        NxActorDesc lowerTorsoActorDesc = new NxActorDesc();
        NxBodyDesc lowerTorsoBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc lowerTorsoBoxShapeDesc = new NxBoxShapeDesc();
        lowerTorsoBoxShapeDesc.getDimensions().set(lowerTorsoLength / 2, lowerTorsoHeight / 2, lowerTorsoWidth / 2);
        lowerTorsoActorDesc.getShapes().push_back(lowerTorsoBoxShapeDesc);
        lowerTorsoActorDesc.setBody(lowerTorsoBodyDesc);
        lowerTorsoActorDesc.setDensity(20);
        lowerTorsoActorDesc.getGlobalPose().getT().set(lowerTorsoPosX, lowerTorsoPosY, lowerTorsoPosZ);
        lowerTorsoActorDesc.getGlobalPose().getM().setRowMajor(TDMath.getXRotation(45));
        lowerTorso = physicScene.createActor(lowerTorsoActorDesc);
        lowerTorso.setMass(lowerTorsoMass);
        lowerTorso.setAngularDamping(0);
        lowerTorso.setMaxAngularVelocity(10);
        NxRevoluteJointDesc leftLowerTorsoJointDesc = new NxRevoluteJointDesc();
        leftLowerTorsoJointDesc.setActor1(torso);
        leftLowerTorsoJointDesc.setActor2(lowerTorso);
        leftLowerTorsoJointDesc.setGlobalAnchor(new NxVec3(lowerTorsoPosX - lowerTorsoLength / 2, lowerTorsoPosY, lowerTorsoPosZ - lowerTorsoWidth / 2));
        leftLowerTorsoJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        leftLowerTorsoJointDesc.getLimit().getHigh().setValue(0.01f);
        leftLowerTorsoJointDesc.getLimit().getLow().setValue(0.01f);
        leftLowerTorsoJoint = physicScene.createJoint(leftLowerTorsoJointDesc).isRevoluteJoint();
        leftLowerTorsoJoint.setBreakable(100, 100);
        NxRevoluteJointDesc rightLowerTorsoJointDesc = new NxRevoluteJointDesc();
        rightLowerTorsoJointDesc.setActor1(torso);
        rightLowerTorsoJointDesc.setActor2(lowerTorso);
        rightLowerTorsoJointDesc.setGlobalAnchor(new NxVec3(lowerTorsoPosX + lowerTorsoLength / 2, lowerTorsoPosY, lowerTorsoPosZ + lowerTorsoWidth / 2));
        rightLowerTorsoJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        rightLowerTorsoJointDesc.getLimit().getHigh().setValue(0.01f);
        rightLowerTorsoJointDesc.getLimit().getLow().setValue(0.01f);
        rightLowerTorsoJoint = physicScene.createJoint(rightLowerTorsoJointDesc).isRevoluteJoint();
        rightLowerTorsoJoint.setBreakable(100, 100);
        float headPosX = upperTorsoPosX;
        float headPosZ = upperTorsoPosZ + 0.05f;
        float headPosY = upperTorsoPosY + (upperTorsoHeight / 2.0f) + 0.1f + (headRadius / 2.0f);
        NxActorDesc headActorDesc = new NxActorDesc();
        NxSphereShapeDesc headShapeDesc = new NxSphereShapeDesc();
        NxBodyDesc headBodyDesc = new NxBodyDesc();
        headShapeDesc.setRadius(headRadius);
        headActorDesc.getShapes().push_back(headShapeDesc);
        headActorDesc.setBody(headBodyDesc);
        headActorDesc.setDensity(20);
        headActorDesc.getGlobalPose().getT().set(headPosX, headPosY, headPosZ);
        head = physicScene.createActor(headActorDesc);
        head.setMass(headMass);
        head.setAngularDamping(0);
        head.setMaxAngularVelocity(10);
        NxFixedJointDesc headJointDesc = new NxFixedJointDesc();
        headJointDesc.setActor1(torso);
        headJointDesc.setActor2(head);
        headJointDesc.setGlobalAnchor(new NxVec3(headPosX, headPosY - headRadius, headPosZ));
        headJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        headJoint = physicScene.createJoint(headJointDesc).isFixedJoint();
        float leftShoulderPosX = upperTorsoPosX - (upperTorsoLength / 2.0f) - (shoulderLength / 2.0f) - (torsoCylinderLength / 2.0f);
        float leftShoulderPosZ = upperTorsoPosZ;
        float leftShoulderPosY = upperTorsoPosY + (upperTorsoHeight / 2.0f) - (shoulderHeight / 2.0f);
        float rightShoulderPosX = upperTorsoPosX + (upperTorsoLength / 2.0f) + (shoulderLength / 2.0f) + (torsoCylinderLength / 2.0f);
        float rightShoulderPosZ = upperTorsoPosZ;
        float rightShoulderPosY = leftShoulderPosY;
        NxActorDesc leftShoulderActorDesc = new NxActorDesc();
        NxBodyDesc leftShoulderBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc leftShoulderShapeDesc = new NxBoxShapeDesc();
        leftShoulderShapeDesc.getDimensions().set(shoulderLength / 2, shoulderHeight / 2, shoulderWidth / 2);
        leftShoulderActorDesc.getShapes().push_back(leftShoulderShapeDesc);
        leftShoulderActorDesc.setBody(leftShoulderBodyDesc);
        leftShoulderActorDesc.setDensity(20);
        leftShoulderActorDesc.getGlobalPose().getT().set(leftShoulderPosX, leftShoulderPosY, leftShoulderPosZ);
        leftShoulder = physicScene.createActor(leftShoulderActorDesc);
        leftShoulder.setMass(shoulderMass);
        leftShoulder.setAngularDamping(0);
        leftShoulder.setMaxAngularVelocity(10);
        NxIsoUniversalJointDesc leftShoulderJointDesc = new NxIsoUniversalJointDesc();
        leftShoulderJointDesc.setActor1(torso);
        leftShoulderJointDesc.setActor2(leftShoulder);
        leftShoulderJointDesc.setGlobalAnchor(new NxVec3(leftShoulderPosX, leftShoulderPosY, leftShoulderPosZ));
        leftShoulderJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        NxJointDriveDesc leftShoulderDriver = new NxJointDriveDesc();
        NxBitField leftShoulderBitFld = new NxBitField();
        leftShoulderBitFld.raiseFlag(NxD6JointDriveType.NX_D6JOINT_DRIVE_POSITION);
        leftShoulderDriver.setDriveType(leftShoulderBitFld);
        leftShoulderDriver.setForceLimit(10);
        leftShoulderDriver.setSpring(250.0f);
        leftShoulderDriver.setDamping(0.01f);
        leftShoulderJointDesc.setSwingDrive(leftShoulderDriver);
        leftShoulderJointDesc.setProjectionMode(NxJointProjectionMode.NX_JPM_LINEAR_MINDIST);
        leftShoulderJoint = physicScene.createJoint(leftShoulderJointDesc).isD6Joint();
        leftShoulderJoint.setBreakable(100, 100);
        NxActorDesc rightShoulderActorDesc = new NxActorDesc();
        NxBodyDesc rightShoulderBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc rightShoulderShapeDesc = new NxBoxShapeDesc();
        rightShoulderShapeDesc.getDimensions().set(shoulderLength / 2, shoulderHeight / 2, shoulderWidth / 2);
        rightShoulderActorDesc.getShapes().push_back(rightShoulderShapeDesc);
        rightShoulderActorDesc.setBody(rightShoulderBodyDesc);
        rightShoulderActorDesc.setDensity(20);
        rightShoulderActorDesc.getGlobalPose().getT().set(rightShoulderPosX, rightShoulderPosY, rightShoulderPosZ);
        rightShoulder = physicScene.createActor(rightShoulderActorDesc);
        rightShoulder.setMass(shoulderMass);
        rightShoulder.setAngularDamping(0);
        rightShoulder.setMaxAngularVelocity(10);
        NxIsoUniversalJointDesc rightShoulderJointDesc = new NxIsoUniversalJointDesc();
        rightShoulderJointDesc.setActor1(torso);
        rightShoulderJointDesc.setActor2(rightShoulder);
        rightShoulderJointDesc.setGlobalAnchor(new NxVec3(rightShoulderPosX, rightShoulderPosY, rightShoulderPosZ));
        rightShoulderJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        NxJointDriveDesc rightShoulderDriver = new NxJointDriveDesc();
        NxBitField rightShoulderBitFld = new NxBitField();
        rightShoulderBitFld.raiseFlag(NxD6JointDriveType.NX_D6JOINT_DRIVE_POSITION);
        rightShoulderDriver.setDriveType(rightShoulderBitFld);
        rightShoulderDriver.setForceLimit(10);
        rightShoulderDriver.setSpring(250.0f);
        rightShoulderDriver.setDamping(0.01f);
        rightShoulderJointDesc.setSwingDrive(rightShoulderDriver);
        rightShoulderJointDesc.setProjectionMode(NxJointProjectionMode.NX_JPM_LINEAR_MINDIST);
        rightShoulderJoint = physicScene.createJoint(rightShoulderJointDesc).isD6Joint();
        rightShoulderJoint.setBreakable(100, 100);
        float leftUpperArmPosX = leftShoulderPosX;
        float leftUpperArmPosZ = leftShoulderPosZ;
        float leftUpperArmPosY = leftShoulderPosY - (shoulderHeight / 2.0f) - (upperarmHeight / 2.0f);
        float rightUpperArmPosX = rightShoulderPosX;
        float rightUpperArmPosZ = rightShoulderPosZ;
        float rightUpperArmPosY = rightShoulderPosY - (shoulderHeight / 2.0f) - (upperarmHeight / 2.0f);
        NxActorDesc leftUpperArmActorDesc = new NxActorDesc();
        NxBodyDesc leftUpperArmBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc leftUpperArmBoxShapeDesc = new NxBoxShapeDesc();
        leftUpperArmBoxShapeDesc.getDimensions().set(upperarmLength / 2, upperarmHeight / 2, upperarmWidth / 2);
        leftUpperArmBoxShapeDesc.getLocalPose().getT().set(0, 0, 0);
        leftUpperArmActorDesc.getShapes().push_back(leftUpperArmBoxShapeDesc);
        NxCapsuleShapeDesc leftElbowShapeDesc = new NxCapsuleShapeDesc();
        leftElbowShapeDesc.setRadius(elbowRadius);
        leftElbowShapeDesc.setHeight(elbowLength);
        leftElbowShapeDesc.getLocalPose().getT().set(0, -1 * ((upperarmHeight / 2.0f) + (elbowRadius / 2.0f)), 0);
        leftElbowShapeDesc.getLocalPose().getM().setRowMajor(TDMath.getZRotation(90));
        leftUpperArmActorDesc.getShapes().push_back(leftElbowShapeDesc);
        leftUpperArmActorDesc.setBody(leftUpperArmBodyDesc);
        leftUpperArmActorDesc.setDensity(20);
        leftUpperArmActorDesc.getGlobalPose().getT().set(leftUpperArmPosX, leftUpperArmPosY, leftUpperArmPosZ);
        leftUpperArm = physicScene.createActor(leftUpperArmActorDesc);
        leftUpperArm.setMass(upperarmMass);
        leftUpperArm.setAngularDamping(0);
        leftUpperArm.setMaxAngularVelocity(10);
        NxRevoluteJointDesc leftUpperArmJointDesc = new NxRevoluteJointDesc();
        leftUpperArmJointDesc.setActor1(leftShoulder);
        leftUpperArmJointDesc.setActor2(leftUpperArm);
        leftUpperArmJointDesc.setGlobalAnchor(new NxVec3(leftUpperArmPosX, leftUpperArmPosY + upperarmHeight / 2.0f, leftUpperArmPosZ));
        leftUpperArmJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        leftUpperArmJointDesc.setJointFlags(leftUpperArmJointDesc.getFlags() | Constans.NX_JF_COLLISION_ENABLED | Constans.NX_RJF_MOTOR_ENABLED);
        leftUpperArmJoint = physicScene.createJoint(leftUpperArmJointDesc).isRevoluteJoint();
        leftUpperArmJoint.setBreakable(100, 100);
        NxMotorDesc leftUpperArmMotor = new NxMotorDesc();
        leftUpperArmJoint.setMotor(leftUpperArmMotor);
        NxActorDesc rightUpperArmActorDesc = new NxActorDesc();
        NxBodyDesc rightUpperArmBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc rightUpperArmBoxShapeDesc = new NxBoxShapeDesc();
        rightUpperArmBoxShapeDesc.getDimensions().set(upperarmLength / 2, upperarmHeight / 2, upperarmWidth / 2);
        rightUpperArmBoxShapeDesc.getLocalPose().getT().set(0, 0, 0);
        rightUpperArmActorDesc.getShapes().push_back(rightUpperArmBoxShapeDesc);
        NxCapsuleShapeDesc rightElbowShapeDesc = new NxCapsuleShapeDesc();
        rightElbowShapeDesc.setRadius(elbowRadius);
        rightElbowShapeDesc.setHeight(elbowLength);
        rightElbowShapeDesc.getLocalPose().getT().set(0, -1 * ((upperarmHeight / 2.0f) + (elbowRadius / 2.0f)), 0);
        rightElbowShapeDesc.getLocalPose().getM().setRowMajor(TDMath.getZRotation(90));
        rightUpperArmActorDesc.getShapes().push_back(rightElbowShapeDesc);
        rightUpperArmActorDesc.setBody(rightUpperArmBodyDesc);
        rightUpperArmActorDesc.setDensity(20);
        rightUpperArmActorDesc.getGlobalPose().getT().set(rightUpperArmPosX, rightUpperArmPosY, rightUpperArmPosZ);
        rightUpperArm = physicScene.createActor(rightUpperArmActorDesc);
        rightUpperArm.setMass(upperarmMass);
        rightUpperArm.setAngularDamping(0);
        rightUpperArm.setMaxAngularVelocity(10);
        NxRevoluteJointDesc rightUpperArmJointDesc = new NxRevoluteJointDesc();
        rightUpperArmJointDesc.setActor1(rightShoulder);
        rightUpperArmJointDesc.setActor2(rightUpperArm);
        rightUpperArmJointDesc.setGlobalAnchor(new NxVec3(rightUpperArmPosX, rightUpperArmPosY + upperarmHeight / 2.0f, rightUpperArmPosZ));
        rightUpperArmJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        rightUpperArmJointDesc.setJointFlags(rightUpperArmJointDesc.getFlags() | Constans.NX_JF_COLLISION_ENABLED | Constans.NX_RJF_MOTOR_ENABLED);
        rightUpperArmJoint = physicScene.createJoint(rightUpperArmJointDesc).isRevoluteJoint();
        rightUpperArmJoint.setBreakable(100, 100);
        NxMotorDesc rightUpperArmMotor = new NxMotorDesc();
        rightUpperArmJoint.setMotor(rightUpperArmMotor);
        float leftLowerArmPosX = leftUpperArmPosX;
        float leftLowerArmPosZ = leftUpperArmPosZ;
        float leftLowerArmPosY = leftUpperArmPosY - (upperarmHeight / 2.0f) - elbowRadius - (lowerarmHeight / 2.0f);
        float rightLowerArmPosX = rightUpperArmPosX;
        float rightLowerArmPosZ = rightUpperArmPosZ;
        float rightLowerArmPosY = rightUpperArmPosY - (upperarmHeight / 2.0f) - elbowRadius - (lowerarmHeight / 2.0f);
        NxActorDesc leftLowerArmActorDesc = new NxActorDesc();
        NxBodyDesc leftLowerArmBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc leftLowerArmBoxShapeDesc = new NxBoxShapeDesc();
        leftLowerArmBoxShapeDesc.getDimensions().set(lowerarmLength / 2, lowerarmHeight / 2, lowerarmWidth / 2);
        leftLowerArmBoxShapeDesc.getLocalPose().getT().set(0, 0, 0);
        leftLowerArmActorDesc.getShapes().push_back(leftLowerArmBoxShapeDesc);
        NxBoxShapeDesc staticLeftHand1ShapeDesc = new NxBoxShapeDesc();
        staticLeftHand1ShapeDesc.getDimensions().set(handBox1Length / 2, handBox1Height / 2, handBox1Width / 2);
        staticLeftHand1ShapeDesc.getLocalPose().getT().set(-1 * ((lowerarmLength / 2.0f) - (handBox1Length / 2.0f) - 0.06f), -1 * ((lowerarmHeight / 2.0f) + (handBox1Height / 2.0f)), 0);
        leftLowerArmActorDesc.getShapes().push_back(staticLeftHand1ShapeDesc);
        NxBoxShapeDesc staticLeftHand2ShapeDesc = new NxBoxShapeDesc();
        staticLeftHand2ShapeDesc.getDimensions().set(handBox2Length / 2, handBox2Height / 2, handBox2Width / 2);
        staticLeftHand2ShapeDesc.getLocalPose().getT().set((lowerarmLength / 2.0f) - (handBox2Length / 2.0f) - 0.06f, -1 * ((lowerarmHeight / 2.0f) + (handBox2Height / 2.0f)), 0);
        leftLowerArmActorDesc.getShapes().push_back(staticLeftHand2ShapeDesc);
        NxBoxShapeDesc staticLeftHand3ShapeDesc = new NxBoxShapeDesc();
        staticLeftHand3ShapeDesc.getDimensions().set(handBox3Length / 2, handBox3Height / 2, handBox3Width / 2);
        staticLeftHand3ShapeDesc.getLocalPose().getT().set((lowerarmLength / 2.0f) - (handBox3Length / 2.0f) - 0.06f, -1 * ((lowerarmHeight / 2.0f) + handBox2Height + (handBox3Height / 2.0f)), 0);
        leftLowerArmActorDesc.getShapes().push_back(staticLeftHand3ShapeDesc);
        leftLowerArmActorDesc.setBody(leftLowerArmBodyDesc);
        leftLowerArmActorDesc.setDensity(20);
        leftLowerArmActorDesc.getGlobalPose().getT().set(leftLowerArmPosX, leftLowerArmPosY, leftLowerArmPosZ);
        leftLowerArm = physicScene.createActor(leftLowerArmActorDesc);
        leftLowerArm.setMass(lowerarmMass);
        leftLowerArm.setAngularDamping(0);
        leftLowerArm.setMaxAngularVelocity(10);
        NxRevoluteJointDesc leftLowerArmJonitDesc = new NxRevoluteJointDesc();
        leftLowerArmJonitDesc.setActor1(leftLowerArm);
        leftLowerArmJonitDesc.setActor2(leftUpperArm);
        leftLowerArmJonitDesc.setGlobalAnchor(new NxVec3(leftLowerArmPosX, leftLowerArmPosY + lowerarmHeight / 2.0f, leftLowerArmPosZ));
        leftLowerArmJonitDesc.setGlobalAxis(new NxVec3(1, 0, 0));
        leftLowerArmJonitDesc.setJointFlags(leftLowerArmJonitDesc.getFlags() | Constans.NX_JF_COLLISION_ENABLED | Constans.NX_RJF_MOTOR_ENABLED);
        leftLowerArmJonit = physicScene.createJoint(leftLowerArmJonitDesc).isRevoluteJoint();
        leftLowerArmJonit.setBreakable(100, 100);
        NxMotorDesc leftLowerArmMotor = new NxMotorDesc();
        leftLowerArmJonit.setMotor(leftLowerArmMotor);
        NxActorDesc rightLowerArmActorDesc = new NxActorDesc();
        NxBodyDesc rightLowerArmBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc rightLowerArmBoxShapeDesc = new NxBoxShapeDesc();
        rightLowerArmBoxShapeDesc.getDimensions().set(lowerarmLength / 2, lowerarmHeight / 2, lowerarmWidth / 2);
        rightLowerArmBoxShapeDesc.getLocalPose().getT().set(0, 0, 0);
        rightLowerArmActorDesc.getShapes().push_back(rightLowerArmBoxShapeDesc);
        NxBoxShapeDesc staticRightHand1ShapeDesc = new NxBoxShapeDesc();
        staticRightHand1ShapeDesc.getDimensions().set(handBox1Length / 2, handBox1Height / 2, handBox1Width / 2);
        staticRightHand1ShapeDesc.getLocalPose().getT().set(-1 * ((lowerarmLength / 2.0f) - (handBox1Length / 2.0f) - 0.06f), -1 * ((lowerarmHeight / 2.0f) + (handBox1Height / 2.0f)), 0);
        rightLowerArmActorDesc.getShapes().push_back(staticRightHand1ShapeDesc);
        NxBoxShapeDesc staticRightHand2ShapeDesc = new NxBoxShapeDesc();
        staticRightHand2ShapeDesc.getDimensions().set(handBox2Length / 2, handBox2Height / 2, handBox2Width / 2);
        staticRightHand2ShapeDesc.getLocalPose().getT().set((lowerarmLength / 2.0f) - (handBox2Length / 2.0f) - 0.06f, -1 * ((lowerarmHeight / 2.0f) + (handBox2Height / 2.0f)), 0);
        rightLowerArmActorDesc.getShapes().push_back(staticRightHand2ShapeDesc);
        NxBoxShapeDesc staticRightHand3ShapeDesc = new NxBoxShapeDesc();
        staticRightHand3ShapeDesc.getDimensions().set(handBox3Length / 2, handBox3Height / 2, handBox3Width / 2);
        staticRightHand3ShapeDesc.getLocalPose().getT().set((lowerarmLength / 2.0f) - (handBox3Length / 2.0f) - 0.06f, -1 * ((lowerarmHeight / 2.0f) + handBox2Height + (handBox3Height / 2.0f)), 0);
        rightLowerArmActorDesc.getShapes().push_back(staticRightHand3ShapeDesc);
        rightLowerArmActorDesc.setBody(rightLowerArmBodyDesc);
        rightLowerArmActorDesc.setDensity(20);
        rightLowerArmActorDesc.getGlobalPose().getT().set(rightLowerArmPosX, rightLowerArmPosY, rightLowerArmPosZ);
        rightLowerArmActorDesc.getGlobalPose().getM().setRowMajor(TDMath.getYRotation(180));
        rightLowerArm = physicScene.createActor(rightLowerArmActorDesc);
        rightLowerArm.setMass(lowerarmMass);
        rightLowerArm.setAngularDamping(0);
        rightLowerArm.setMaxAngularVelocity(10);
        NxRevoluteJointDesc rightLowerArmJonitDesc = new NxRevoluteJointDesc();
        rightLowerArmJonitDesc.setActor1(rightLowerArm);
        rightLowerArmJonitDesc.setActor2(rightUpperArm);
        rightLowerArmJonitDesc.setGlobalAnchor(new NxVec3(rightLowerArmPosX, rightLowerArmPosY + lowerarmHeight / 2.0f, rightLowerArmPosZ));
        rightLowerArmJonitDesc.setGlobalAxis(new NxVec3(1, 0, 0));
        rightLowerArmJonitDesc.setJointFlags(rightLowerArmJonitDesc.getFlags() | Constans.NX_JF_COLLISION_ENABLED | Constans.NX_RJF_MOTOR_ENABLED);
        rightLowerArmJoint = physicScene.createJoint(rightLowerArmJonitDesc).isRevoluteJoint();
        rightLowerArmJoint.setBreakable(100, 100);
        NxMotorDesc rightLowerArmMotor = new NxMotorDesc();
        rightLowerArmJoint.setMotor(rightLowerArmMotor);
        float leftHipPosX = lowerTorsoPosX - (lowerTorsoLength / 4.0f) - 0.06f;
        float leftHipPosZ = lowerTorsoPosZ;
        float leftHipPosY = lowerTorsoPosY - (lowerTorsoHeight / 2.0f) - (hipHeight / 2.0f);
        float rightHipPosX = lowerTorsoPosX + (lowerTorsoLength / 4.0f) + 0.06f;
        float rightHipPosZ = lowerTorsoPosZ;
        float rightHipPosY = lowerTorsoPosY - (lowerTorsoHeight / 2.0f) - (hipHeight / 2.0f);
        NxActorDesc leftHipActorDesc = new NxActorDesc();
        NxBodyDesc leftHipBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc leftHipShapeDesc = new NxBoxShapeDesc();
        leftHipShapeDesc.getDimensions().set(hipLength / 2, hipHeight / 2, hipWidth / 2);
        leftHipShapeDesc.getLocalPose().getT().set(0, 0, 0);
        leftHipActorDesc.getShapes().push_back(leftHipShapeDesc);
        leftHipActorDesc.setBody(leftHipBodyDesc);
        leftHipActorDesc.setDensity(20);
        leftHipActorDesc.getGlobalPose().getT().set(leftHipPosX, leftHipPosY, leftHipPosZ);
        leftHip = physicScene.createActor(leftHipActorDesc);
        leftHip.setMass(hipMass);
        leftHip.setAngularDamping(0);
        leftHip.setMaxAngularVelocity(10);
        NxRevoluteJointDesc leftHipJointDesc = new NxRevoluteJointDesc();
        leftHipJointDesc.setActor1(leftHip);
        leftHipJointDesc.setActor2(lowerTorso);
        leftHipJointDesc.setGlobalAnchor(new NxVec3(leftHipPosX, leftHipPosY + hipHeight / 2.0f, leftHipPosZ));
        leftHipJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        leftHipJointDesc.setJointFlags(leftHipJointDesc.getFlags() | Constans.NX_RJF_MOTOR_ENABLED);
        leftHipJoint = physicScene.createJoint(leftHipJointDesc).isRevoluteJoint();
        leftHipJoint.setBreakable(100, 100);
        NxMotorDesc leftHipMotor = new NxMotorDesc();
        leftHipJoint.setMotor(leftHipMotor);
        NxActorDesc rightHipActorDesc = new NxActorDesc();
        NxBodyDesc rightHipBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc rightHipShapeDesc = new NxBoxShapeDesc();
        rightHipShapeDesc.getDimensions().set(hipLength / 2, hipHeight / 2, hipWidth / 2);
        rightHipShapeDesc.getLocalPose().getT().set(0, 0, 0);
        rightHipActorDesc.getShapes().push_back(rightHipShapeDesc);
        rightHipActorDesc.setBody(rightHipBodyDesc);
        rightHipActorDesc.setDensity(20);
        rightHipActorDesc.getGlobalPose().getT().set(rightHipPosX, rightHipPosY, rightHipPosZ);
        rightHip = physicScene.createActor(rightHipActorDesc);
        rightHip.setMass(hipMass);
        rightHip.setAngularDamping(0);
        rightHip.setMaxAngularVelocity(10);
        NxRevoluteJointDesc rightHipJointDesc = new NxRevoluteJointDesc();
        rightHipJointDesc.setActor1(rightHip);
        rightHipJointDesc.setActor2(lowerTorso);
        rightHipJointDesc.setGlobalAnchor(new NxVec3(rightHipPosX, rightHipPosY + hipHeight / 2.0f, rightHipPosZ));
        rightHipJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        rightHipJointDesc.setJointFlags(rightHipJointDesc.getFlags() | Constans.NX_RJF_MOTOR_ENABLED);
        rightHipJoint = physicScene.createJoint(rightHipJointDesc).isRevoluteJoint();
        rightHipJoint.setBreakable(100, 100);
        NxMotorDesc rightHipMotor = new NxMotorDesc();
        rightHipJoint.setMotor(rightHipMotor);
        float leftThighPosX = leftHipPosX;
        float leftThighPosZ = leftHipPosZ;
        float leftThighPosY = leftHipPosY - (hipHeight / 2.0f) - (thighHeight / 2.0f);
        float rightThighPosX = rightHipPosX;
        float rightThighPosZ = rightHipPosZ;
        float rightThighPosY = rightHipPosY - (hipHeight / 2.0f) - (thighHeight / 2.0f);
        NxActorDesc leftThighActorDesc = new NxActorDesc();
        NxBodyDesc leftThighBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc leftThighBoxShapeDesc = new NxBoxShapeDesc();
        leftThighBoxShapeDesc.getDimensions().set(thighLength / 2, thighHeight / 2, thighWidth / 2);
        leftThighBoxShapeDesc.getLocalPose().getT().set(0, 0, 0);
        leftThighActorDesc.getShapes().push_back(leftThighBoxShapeDesc);
        NxCapsuleShapeDesc leftKneeShapeDesc = new NxCapsuleShapeDesc();
        leftKneeShapeDesc.setRadius(0.1f);
        leftKneeShapeDesc.setHeight(thighLength - 0.2f);
        leftKneeShapeDesc.getLocalPose().getT().set(0, (thighHeight * -0.5f) - 0.025f, 0);
        leftKneeShapeDesc.getLocalPose().getM().setRowMajor(TDMath.getZRotation(90));
        leftThighActorDesc.getShapes().push_back(leftKneeShapeDesc);
        leftThighActorDesc.setBody(leftThighBodyDesc);
        leftThighActorDesc.setDensity(20);
        leftThighActorDesc.getGlobalPose().getT().set(leftThighPosX, leftThighPosY, leftThighPosZ);
        leftThigh = physicScene.createActor(leftThighActorDesc);
        leftThigh.setMass(thighMass);
        leftThigh.setAngularDamping(0);
        leftThigh.setMaxAngularVelocity(10);
        NxIsoUniversalJointDesc leftThighJointDesc = new NxIsoUniversalJointDesc();
        leftThighJointDesc.setActor1(leftThigh);
        leftThighJointDesc.setActor2(leftHip);
        leftThighJointDesc.setGlobalAnchor(new NxVec3(leftThighPosX, leftThighPosY + thighHeight / 2.0f, leftThighPosZ));
        leftThighJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        NxJointDriveDesc leftThighDriver = new NxJointDriveDesc();
        NxBitField leftThighBitFld = new NxBitField();
        leftThighBitFld.raiseFlag(NxD6JointDriveType.NX_D6JOINT_DRIVE_POSITION);
        leftThighDriver.setDriveType(leftThighBitFld);
        leftThighDriver.setForceLimit(10);
        leftThighDriver.setSpring(250.0f);
        leftThighDriver.setDamping(0.01f);
        leftThighJointDesc.setSwingDrive(leftThighDriver);
        leftThighJointDesc.setProjectionMode(NxJointProjectionMode.NX_JPM_LINEAR_MINDIST);
        leftThighJoint = physicScene.createJoint(leftThighJointDesc).isD6Joint();
        leftThighJoint.setBreakable(100, 100);
        NxActorDesc rightThighActorDesc = new NxActorDesc();
        NxBodyDesc rightThighBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc rightThighBoxShapeDesc = new NxBoxShapeDesc();
        rightThighBoxShapeDesc.getDimensions().set(thighLength / 2, thighHeight / 2, thighWidth / 2);
        rightThighBoxShapeDesc.getLocalPose().getT().set(0, 0, 0);
        rightThighActorDesc.getShapes().push_back(rightThighBoxShapeDesc);
        NxCapsuleShapeDesc rightKneeShapeDesc = new NxCapsuleShapeDesc();
        rightKneeShapeDesc.setRadius(0.1f);
        rightKneeShapeDesc.setHeight(thighLength - 0.2f);
        rightKneeShapeDesc.getLocalPose().getT().set(0, (thighHeight * -0.5f) - 0.025f, 0);
        rightKneeShapeDesc.getLocalPose().getM().setRowMajor(TDMath.getZRotation(90));
        rightThighActorDesc.getShapes().push_back(rightKneeShapeDesc);
        rightThighActorDesc.setBody(rightThighBodyDesc);
        rightThighActorDesc.setDensity(20);
        rightThighActorDesc.getGlobalPose().getT().set(rightThighPosX, rightThighPosY, rightThighPosZ);
        rightThigh = physicScene.createActor(rightThighActorDesc);
        rightThigh.setMass(thighMass);
        rightThigh.setAngularDamping(0);
        rightThigh.setMaxAngularVelocity(10);
        NxIsoUniversalJointDesc rightThighJointDesc = new NxIsoUniversalJointDesc();
        rightThighJointDesc.setActor1(rightThigh);
        rightThighJointDesc.setActor2(rightHip);
        rightThighJointDesc.setGlobalAnchor(new NxVec3(rightThighPosX, rightThighPosY + thighHeight / 2.0f, rightThighPosZ));
        rightThighJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        NxJointDriveDesc rightThighDriver = new NxJointDriveDesc();
        NxBitField rightThighBitFld = new NxBitField();
        rightThighBitFld.raiseFlag(NxD6JointDriveType.NX_D6JOINT_DRIVE_POSITION);
        rightThighDriver.setDriveType(rightThighBitFld);
        rightThighDriver.setForceLimit(10);
        rightThighDriver.setSpring(250.0f);
        rightThighDriver.setDamping(0.01f);
        rightThighJointDesc.setSwingDrive(rightThighDriver);
        rightThighJointDesc.setProjectionMode(NxJointProjectionMode.NX_JPM_LINEAR_MINDIST);
        rightThighJoint = physicScene.createJoint(rightThighJointDesc).isD6Joint();
        rightThighJoint.setBreakable(100, 100);
        float leftShankPosX = leftThighPosX;
        float leftShankPosZ = leftThighPosZ;
        float leftShankPosY = leftThighPosY - (thighHeight / 2.0f) - (shankHeight / 2.0f) - 0.05f;
        float rightShankPosX = rightThighPosX;
        float rightShankPosZ = rightThighPosZ;
        float rightShankPosY = rightThighPosY - (thighHeight / 2.0f) - (shankHeight / 2.0f) - 0.05f;
        NxActorDesc leftShankActorDesc = new NxActorDesc();
        NxBodyDesc leftShankBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc leftShankShapeDesc = new NxBoxShapeDesc();
        leftShankShapeDesc.getDimensions().set(shankLength / 2, shankHeight / 2, shankWidth / 2);
        leftShankShapeDesc.getLocalPose().getT().set(0, 0, 0);
        leftShankActorDesc.getShapes().push_back(leftShankShapeDesc);
        leftShankActorDesc.setBody(leftShankBodyDesc);
        leftShankActorDesc.setDensity(20);
        leftShankActorDesc.getGlobalPose().getT().set(leftShankPosX, leftShankPosY, leftShankPosZ);
        leftShank = physicScene.createActor(leftShankActorDesc);
        leftShank.setMass(shankMass);
        leftShank.setAngularDamping(0);
        leftShank.setMaxAngularVelocity(10);
        NxRevoluteJointDesc leftShankJointDesc = new NxRevoluteJointDesc();
        leftShankJointDesc.setToDefault();
        leftShankJointDesc.setActor1(leftShank);
        leftShankJointDesc.setActor2(leftThigh);
        leftShankJointDesc.setGlobalAnchor(new NxVec3(leftShankPosX, leftShankPosY + shankHeight / 2, leftShankPosZ));
        leftShankJointDesc.setGlobalAxis(new NxVec3(1, 0, 0));
        leftShankJointDesc.setJointFlags(leftShankJointDesc.getFlags() | Constans.NX_RJF_MOTOR_ENABLED);
        leftShankJoint = physicScene.createJoint(leftShankJointDesc).isRevoluteJoint();
        leftShankJoint.setBreakable(100, 100);
        NxMotorDesc leftShankMotor = new NxMotorDesc();
        leftShankJoint.setMotor(leftShankMotor);
        NxActorDesc rightShankActorDesc = new NxActorDesc();
        NxBodyDesc rightShankBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc rightShankShapeDesc = new NxBoxShapeDesc();
        rightShankShapeDesc.getDimensions().set(shankLength / 2, shankHeight / 2, shankWidth / 2);
        rightShankShapeDesc.getLocalPose().getT().set(0, 0, 0);
        rightShankActorDesc.getShapes().push_back(rightShankShapeDesc);
        rightShankActorDesc.setBody(rightShankBodyDesc);
        rightShankActorDesc.setDensity(20);
        rightShankActorDesc.getGlobalPose().getT().set(rightShankPosX, rightShankPosY, rightShankPosZ);
        rightShank = physicScene.createActor(rightShankActorDesc);
        rightShank.setMass(shankMass);
        rightShank.setAngularDamping(0);
        rightShank.setMaxAngularVelocity(10);
        NxRevoluteJointDesc rightShankJointDesc = new NxRevoluteJointDesc();
        rightShankJointDesc.setActor1(rightShank);
        rightShankJointDesc.setActor2(rightThigh);
        rightShankJointDesc.setGlobalAnchor(new NxVec3(rightShankPosX, rightShankPosY + shankHeight / 2, rightShankPosZ));
        rightShankJointDesc.setGlobalAxis(new NxVec3(1, 0, 0));
        rightShankJointDesc.setJointFlags(rightShankJointDesc.getFlags() | Constans.NX_RJF_MOTOR_ENABLED);
        rightShankJoint = physicScene.createJoint(rightShankJointDesc).isRevoluteJoint();
        rightShankJoint.setBreakable(100, 100);
        NxMotorDesc rightShankMotor = new NxMotorDesc();
        rightShankJoint.setMotor(rightShankMotor);
        NxActorDesc leftFootActorDesc = new NxActorDesc();
        NxBodyDesc leftFootBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc leftFootBoxShapeDesc = new NxBoxShapeDesc();
        leftFootBoxShapeDesc.getDimensions().set(footLength / 2, footHeight / 2, footWidth / 2);
        leftFootBoxShapeDesc.getLocalPose().getT().set(0, 0, 0);
        leftFootActorDesc.getShapes().push_back(leftFootBoxShapeDesc);
        NxBoxShapeDesc leftBackFootShapeDesc = new NxBoxShapeDesc();
        leftBackFootShapeDesc.getDimensions().set(ankleBoxLength / 2, ankleBoxHeight / 2, ankleBoxWidth / 2);
        leftBackFootShapeDesc.getLocalPose().getT().set(0, (footHeight / 2.0f) + (ankleBoxHeight / 2.0f), -1 * ((footWidth / 2.0f) - (ankleBoxWidth / 2.0f)));
        leftFootActorDesc.getShapes().push_back(leftBackFootShapeDesc);
        leftFootActorDesc.setBody(leftFootBodyDesc);
        leftFootActorDesc.setDensity(20);
        leftFootActorDesc.getGlobalPose().getT().set(leftShankPosX, leftShankPosY - (shankHeight / 2.0f) - (footHeight / 2.0f) - 0.05f, leftShankPosZ);
        leftFoot = physicScene.createActor(leftFootActorDesc);
        leftFoot.setMass(footMass);
        leftFoot.setAngularDamping(0);
        leftFoot.setMaxAngularVelocity(10);
        NxIsoUniversalJointDesc leftFootJointDesc = new NxIsoUniversalJointDesc();
        leftFootJointDesc.setActor1(leftFoot);
        leftFootJointDesc.setActor2(leftShank);
        leftFootJointDesc.setGlobalAnchor(new NxVec3(leftShankPosX, (leftShankPosY - (shankHeight / 2.0f) - (footHeight / 2.0f) - 0.05f) + (footHeight / 2.0f), leftShankPosZ));
        leftFootJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        NxJointDriveDesc leftFootDriver = new NxJointDriveDesc();
        NxBitField leftFootBitFld = new NxBitField();
        leftFootBitFld.raiseFlag(NxD6JointDriveType.NX_D6JOINT_DRIVE_POSITION);
        leftFootDriver.setDriveType(leftFootBitFld);
        leftFootDriver.setForceLimit(10);
        leftFootDriver.setSpring(250.0f);
        leftFootDriver.setDamping(0.01f);
        rightThighJointDesc.setSwingDrive(leftFootDriver);
        rightThighJointDesc.setProjectionMode(NxJointProjectionMode.NX_JPM_LINEAR_MINDIST);
        leftFootJoint = physicScene.createJoint(leftFootJointDesc).isD6Joint();
        leftFootJoint.setBreakable(100, 100);
        NxActorDesc rightFootActorDesc = new NxActorDesc();
        NxBodyDesc rightFootBodyDesc = new NxBodyDesc();
        NxBoxShapeDesc rightFootBoxShapeDesc = new NxBoxShapeDesc();
        rightFootBoxShapeDesc.getDimensions().set(footLength / 2, footHeight / 2, footWidth / 2);
        rightFootBoxShapeDesc.getLocalPose().getT().set(0, 0, 0);
        rightFootActorDesc.getShapes().push_back(rightFootBoxShapeDesc);
        NxBoxShapeDesc rightBackFootShapeDesc = new NxBoxShapeDesc();
        rightBackFootShapeDesc.getDimensions().set(ankleBoxLength / 2, ankleBoxHeight / 2, ankleBoxWidth / 2);
        rightBackFootShapeDesc.getLocalPose().getT().set(0, (footHeight / 2.0f) + (ankleBoxHeight / 2.0f), -1 * ((footWidth / 2.0f) - (ankleBoxWidth / 2.0f)));
        rightFootActorDesc.getShapes().push_back(rightBackFootShapeDesc);
        rightFootActorDesc.setBody(rightFootBodyDesc);
        rightFootActorDesc.setDensity(20);
        rightFootActorDesc.getGlobalPose().getT().set(rightShankPosX, rightShankPosY - (shankHeight / 2.0f) - (footHeight / 2.0f) - 0.05f, rightShankPosZ);
        rightFoot = physicScene.createActor(rightFootActorDesc);
        rightFoot.setMass(footMass);
        rightFoot.setAngularDamping(0);
        rightFoot.setMaxAngularVelocity(10);
        NxIsoUniversalJointDesc rightFootJointDesc = new NxIsoUniversalJointDesc();
        rightFootJointDesc.setActor1(rightFoot);
        rightFootJointDesc.setActor2(rightShank);
        rightFootJointDesc.setGlobalAnchor(new NxVec3(rightShankPosX, (rightShankPosY - (shankHeight / 2.0f) - (footHeight / 2.0f) - 0.05f) + (footHeight / 2.0f), rightShankPosZ));
        rightFootJointDesc.setGlobalAxis(new NxVec3(0, 1, 0));
        NxJointDriveDesc rightFootDriver = new NxJointDriveDesc();
        NxBitField rightFootBitFld = new NxBitField();
        rightFootBitFld.raiseFlag(NxD6JointDriveType.NX_D6JOINT_DRIVE_POSITION);
        rightFootDriver.setDriveType(rightFootBitFld);
        rightFootDriver.setForceLimit(10);
        rightFootDriver.setSpring(250.0f);
        rightFootDriver.setDamping(0.01f);
        rightThighJointDesc.setSwingDrive(rightFootDriver);
        rightThighJointDesc.setProjectionMode(NxJointProjectionMode.NX_JPM_LINEAR_MINDIST);
        rightFootJoint = physicScene.createJoint(rightFootJointDesc).isD6Joint();
        rightFootJoint.setBreakable(100, 100);
    }

    public void larm_eff_1_2(float v1, float v2) {
        leftShoulderJoint.setDriveAngularVelocity(new NxVec3(0, v1, v2));
    }

    public double[] getLarm_eff_1_2() {
        return new double[] { leftShoulderJoint.getGlobalAxis().getY(), leftShoulderJoint.getGlobalAxis().getZ() };
    }

    public void larm_eff_3(float v) {
        revMotor.setVelTarget(v);
        revMotor.setMaxForce(10);
        revMotor.setFreeSpin(1);
        leftUpperArmJoint.setMotor(revMotor);
    }

    public double getLarm_eff_3() {
        return leftUpperArmJoint.getVelocity();
    }

    public void larm_eff_4(float v) {
        NxMotorDesc motor = new NxMotorDesc();
        motor.setVelTarget(v);
        leftLowerArmJonit.setMotor(motor);
    }

    public double getLarm_eff_4() {
        return leftLowerArmJonit.getVelocity();
    }

    public void lleg_eff_1(float v) {
        revMotor.setVelTarget(v);
        revMotor.setMaxForce(10);
        revMotor.setFreeSpin(1);
        leftHipJoint.setMotor(revMotor);
    }

    public double getLleg_eff_1() {
        return leftHipJoint.getVelocity();
    }

    public void lleg_eff_2_3(float v1, float v2) {
        leftThighJoint.setDriveAngularVelocity(new NxVec3(0, v1, v2));
    }

    public double[] getLleg_eff_2_3() {
        return new double[] { leftThighJoint.getGlobalAxis().getY(), leftThighJoint.getGlobalAxis().getZ() };
    }

    public void lleg_eff_4(float v) {
        revMotor.setVelTarget(v);
        revMotor.setMaxForce(10);
        revMotor.setFreeSpin(1);
        leftShankJoint.setMotor(revMotor);
    }

    /**
     * 
     * @return left shank velocity
     */
    public double getLleg_eff_4() {
        return leftShankJoint.getVelocity();
    }

    public void lleg_eff_5_6(float v1, float v2) {
        leftFootJoint.setDriveAngularVelocity(new NxVec3(0, v1, v2));
    }

    public void rarm_eff_1_2(float v1, float v2) {
        rightShoulderJoint.setDriveAngularVelocity(new NxVec3(0, v1, v2));
    }

    public void rarm_eff_3(float v) {
        revMotor.setVelTarget(v);
        revMotor.setMaxForce(10);
        revMotor.setFreeSpin(1);
        rightUpperArmJoint.setMotor(revMotor);
    }

    public void rarm_eff_4(float v) {
        revMotor.setVelTarget(v);
        revMotor.setMaxForce(10);
        revMotor.setFreeSpin(1);
        rightLowerArmJoint.setMotor(revMotor);
    }

    public void rleg_eff_1(float v) {
        revMotor.setVelTarget(v);
        revMotor.setMaxForce(10);
        revMotor.setFreeSpin(1);
        rightHipJoint.setMotor(revMotor);
    }

    public void rleg_eff_2_3(float v1, float v2) {
        rightThighJoint.setDriveAngularVelocity(new NxVec3(0, v1, v2));
    }

    public void rleg_eff_4(float v) {
        revMotor.setVelTarget(v);
        revMotor.setMaxForce(10);
        revMotor.setFreeSpin(1);
        rightShankJoint.setMotor(revMotor);
    }

    public void rleg_eff_5_6(float v1, float v2) {
        rightFootJoint.setDriveAngularVelocity(new NxVec3(0, v1, v2));
    }

    public double[] getLleg_eff_5_6() {
        return new double[] { leftFootJoint.getGlobalAxis().getY(), leftFootJoint.getGlobalAxis().getZ() };
    }

    public double[] getRarm_eff_1_2() {
        return new double[] { rightShoulderJoint.getGlobalAxis().getY(), rightShoulderJoint.getGlobalAxis().getZ() };
    }

    public double getRarm_eff_3() {
        return rightUpperArmJoint.getVelocity();
    }

    public double getRarm_eff_4() {
        return rightLowerArmJoint.getVelocity();
    }

    public double getRleg_eff_1() {
        return rightHipJoint.getVelocity();
    }

    public double[] getRleg_eff_2_3() {
        return new double[] { rightThighJoint.getGlobalAxis().getY(), rightThighJoint.getGlobalAxis().getZ() };
    }

    public double getRleg_eff_4() {
        return rightShankJoint.getVelocity();
    }

    public double[] getRleg_eff_5_6() {
        return new double[] { rightFootJoint.getGlobalAxis().getY(), rightFootJoint.getGlobalAxis().getZ() };
    }

    @Override
    protected void finalize() {
    }

    @Override
    protected void initBodies() {
        bodies.put("lowerTorso", lowerTorso);
        bodies.put("head", head);
        bodies.put("leftFoot", leftFoot);
        bodies.put("leftHip", leftHip);
        bodies.put("leftShoulder", leftShoulder);
        bodies.put("leftUpperArm", leftUpperArm);
        bodies.put("leftLowerArm", leftLowerArm);
        bodies.put("leftShank", leftShank);
        bodies.put("leftThigh", leftThigh);
        bodies.put("rightFoot", rightFoot);
        bodies.put("rightHip", rightHip);
        bodies.put("rightShoulder", rightShoulder);
        bodies.put("rightUpperArm", rightUpperArm);
        bodies.put("rightLowerArm", rightLowerArm);
        bodies.put("rightShank", rightShank);
        bodies.put("rightThigh", rightThigh);
        bodies.put("torso", torso);
    }

    @Override
    public Vector3f getAgentPosition() {
        return new Vector3f(torso.getGlobalPose().getT().get());
    }

    @Override
    public Vector3f getAgentBodyPosition(String bodyName) {
        return new Vector3f(((NxActor) bodies.get(bodyName)).getGlobalPose().getT().get());
    }

    @Override
    public Quat4f getAgentBodyQuaternion(String bodyName) {
        NxQuat quat = ((NxActor) bodies.get(bodyName)).getGlobalOrientationQuat();
        return new Quat4f(quat.getW(), quat.getX(), quat.getY(), quat.getZ());
    }

    @Override
    public Matrix3f getAgentBodyRotation(String bodyName) {
        NxMat33 mat = ((NxActor) bodies.get(bodyName)).getGlobalOrientation();
        return new Matrix3f(mat.getRowMajor());
    }

    @Override
    public Vector<AtomicShape> getAtomicShapes() {
        Vector<AtomicShape> out = new Vector<AtomicShape>();
        out.addAll(getActorAtomicShapes(lowerTorso, "lowerTorso"));
        out.addAll(getActorAtomicShapes(head, "head"));
        out.addAll(getActorAtomicShapes(leftFoot, "lFoot"));
        out.addAll(getActorAtomicShapes(leftHip, "lHip"));
        out.addAll(getActorAtomicShapes(leftShoulder, "lShoulder"));
        out.addAll(getActorAtomicShapes(leftUpperArm, "lUpperArm"));
        out.addAll(getActorAtomicShapes(leftLowerArm, "lLowerArm"));
        out.addAll(getActorAtomicShapes(leftShank, "lShank"));
        out.addAll(getActorAtomicShapes(leftThigh, "lThigh"));
        out.addAll(getActorAtomicShapes(rightFoot, "rFoot"));
        out.addAll(getActorAtomicShapes(rightHip, "rHip"));
        out.addAll(getActorAtomicShapes(rightShoulder, "rShoulder"));
        out.addAll(getActorAtomicShapes(rightUpperArm, "rUpperArm"));
        out.addAll(getActorAtomicShapes(rightLowerArm, "rLowerArm"));
        out.addAll(getActorAtomicShapes(rightShank, "rShank"));
        out.addAll(getActorAtomicShapes(rightThigh, "rThigh"));
        out.addAll(getActorAtomicShapes(torso, "torso"));
        return out;
    }

    /**
     * 
     * @param actor any NxActor instance
     * @return list of atomic shapes of input NxActor
     */
    private Vector<AtomicShape> getActorAtomicShapes(NxActor actor, String actorName) {
        Vector<AtomicShape> out = new Vector<AtomicShape>();
        AtomicShape atomicShape;
        for (int i = 0; i < actor.getNbShapes(); i++) {
            NxShape shape = actor.getShape(i);
            atomicShape = nxShapeToAtomicShape(shape);
            atomicShape.setUsingSection(actorName);
            out.add(atomicShape);
        }
        return out;
    }

    /**
     * <p> convert NxShape to AtomicShape </p>
     * @param nxShape any NxShape instance
     * @return AtomicShape of input NxShape
     */
    private AtomicShape nxShapeToAtomicShape(NxShape nxShape) {
        AtomicShape out = new AtomicShape();
        NxBoxShape nxBox;
        NxSphereShape nxSphere;
        NxCapsuleShape nxCapsule;
        out.setPosition(new Vector3f(nxShape.getGlobalPose().getT().get()));
        NxQuat quat = new NxQuat();
        nxShape.getGlobalOrientation().toQuat(quat);
        out.setRotMatrix(nxShape.getGlobalOrientation().getRowMajor());
        out.setQuaternion(new Quat4f(quat.getW(), quat.getX(), quat.getY(), quat.getZ()));
        if ((nxBox = nxShape.isBox()) != null) {
            out.setType(AtomicShape.BOX_TYPE);
            out.setScale(new Vector3f(nxBox.getDimensions().getX() * 2, nxBox.getDimensions().getY() * 2, nxBox.getDimensions().getZ() * 2));
        } else if ((nxSphere = nxShape.isSphere()) != null) {
            out.setType(AtomicShape.SPHERE_TYPE);
            out.setScale(new Vector3f(nxSphere.getRadius(), nxSphere.getRadius(), nxSphere.getRadius()));
        } else if ((nxCapsule = nxShape.isCapsule()) != null) {
            out.setType(AtomicShape.CAPSULE_TYPE);
            out.setScale(new Vector3f(nxCapsule.getHeight(), nxCapsule.getRadius(), nxCapsule.getRadius()));
        } else {
            out.setType(AtomicShape.UNKNOWN_TYPE);
            out.setScale(new Vector3f(-1, -1, -1));
        }
        return out;
    }
}

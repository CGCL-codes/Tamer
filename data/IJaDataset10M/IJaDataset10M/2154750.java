package jp.seraph.jspf.pose;

import jp.seraph.jsade.model.AngleVelocityCalculator;

public class DefaultPoseElement implements PoseElement {

    public DefaultPoseElement(Pose aPose, AngleVelocityCalculator aCalculator) {
        mPose = aPose;
        mCalculator = aCalculator;
    }

    private Pose mPose;

    private AngleVelocityCalculator mCalculator;

    /**
     * 
     * @see jp.seraph.jspf.pose.PoseElement#getCalculator()
     */
    public AngleVelocityCalculator getCalculator() {
        return mCalculator;
    }

    /**
     * 
     * @see jp.seraph.jspf.pose.PoseElement#getPose()
     */
    public Pose getPose() {
        return mPose;
    }
}

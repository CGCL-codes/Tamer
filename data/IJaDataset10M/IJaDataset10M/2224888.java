package jp.seraph.same.controller;

import jp.seraph.same.model.PoseModel;

public interface PoseManagerController {

    public void removePose(String aPoseName);

    public PoseModel getPose(String aPoseName);

    public PoseModel createPose(String aPoseName);
}

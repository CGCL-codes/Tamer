package jp.seraph.robocup.soccer.sample.agent;

import jp.seraph.jsade.core.DefaultAgentRunner;
import jp.seraph.jsade.core.DefaultWorld;
import jp.seraph.jsade.core.NopSeeObjectCalculator;
import jp.seraph.jsade.math.Angle;
import jp.seraph.jsade.model.nao.NaoJointIdentifier;
import jp.seraph.jsade.model.nao.NaoModelManager;
import jp.seraph.jsade.task.TaskSelectorAgent;
import jp.seraph.jsmf.motion.DefaultMotion;
import jp.seraph.jsmf.motion.DefaultMotionElement;
import jp.seraph.jsmf.motion.DefaultMotionSession;
import jp.seraph.jsmf.motion.MonoMotionSelector;
import jp.seraph.jspf.pose.DefaultPose;
import jp.seraph.jspf.pose.Pose;

public class MotionAgentSampleRunner {

    public static void main(String[] args) {
        DefaultMotion tMotion = new DefaultMotion();
        DefaultMotionSession tSession1 = new DefaultMotionSession("Session1", new DefaultMotionElement("Element1", createPose1()));
        DefaultMotionSession tSession2 = new DefaultMotionSession("Session2", new DefaultMotionElement("Element2", createPose2()));
        tMotion.addSession(tSession1);
        tMotion.addSession(tSession2);
        tMotion.addSession(tSession1);
        tMotion.addSession(tSession2);
        tMotion.addSession(tSession1);
        tMotion.addSession(tSession2);
        tMotion.addSession(tSession1);
        tMotion.addSession(tSession2);
        tMotion.addSession(tSession1);
        tMotion.addSession(tSession2);
        tMotion.addSession(tSession1);
        tMotion.addSession(tSession2);
        TaskSelectorAgent tAgent = new TaskSelectorAgent(new MonoMotionSelector(tMotion));
        DefaultAgentRunner tRunner = new DefaultAgentRunner(new DefaultWorld(new NaoModelManager(0), new NopSeeObjectCalculator()), new SampleAgentContext(), tAgent);
        tRunner.start();
        try {
            tRunner.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Pose createPose1() {
        DefaultPose tResult = new DefaultPose();
        tResult.setJointAngle(NaoJointIdentifier.HJ1, Angle.createAngleAsDegree(100));
        return tResult;
    }

    private static Pose createPose2() {
        DefaultPose tResult = new DefaultPose();
        tResult.setJointAngle(NaoJointIdentifier.HJ1, Angle.createAngleAsDegree(-100));
        return tResult;
    }
}

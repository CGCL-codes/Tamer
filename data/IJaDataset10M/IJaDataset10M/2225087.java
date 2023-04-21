package jp.seraph.same.core;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.JFrame;
import org.apache.commons.io.FileUtils;
import jp.seraph.jsade.core.DefaultAgentRunner;
import jp.seraph.jsade.core.DefaultWorld;
import jp.seraph.jsade.core.NopSeeObjectCalculator;
import jp.seraph.jsade.math.Angle;
import jp.seraph.jsade.model.nao.NaoJointIdentifier;
import jp.seraph.jsade.model.nao.NaoJointIdentifierContainer;
import jp.seraph.jsade.model.nao.NaoModelManager;
import jp.seraph.jsade.model.nao.NaoObjectContainer;
import jp.seraph.jsade.task.TaskSelectorAgent;
import jp.seraph.jsmf.codec.FileMotionManager;
import jp.seraph.jsmf.codec.OldMotionCodec;
import jp.seraph.jspf.codec.FilePoseManager;
import jp.seraph.jspf.codec.JsonPoseCodec;
import jp.seraph.jspf.pose.DefaultPose;
import jp.seraph.jspf.pose.PoseManager;
import jp.seraph.same.controller.DefaultMotionController;
import jp.seraph.same.model.DefaultMotionCollectionModel;
import jp.seraph.same.model.DefaultPoseCollectionModel;
import jp.seraph.same.model.MotionCollectionModel;
import jp.seraph.same.model.NaoJointPositionMapper;
import jp.seraph.same.model.PoseCollectionModel;
import jp.seraph.same.view.MotionPanel;

public class MotionPanelRunner {

    /**
     * @param args
     */
    public static void main(String[] args) {
        EditorMotionSelector tSelector = new EditorMotionSelector();
        TaskSelectorAgent tAgent = new TaskSelectorAgent(tSelector);
        final File tMotionDir = new File("testMotion");
        final File tPoseDIr = new File("testPose");
        final DefaultAgentRunner tRunner = new DefaultAgentRunner(new DefaultWorld(new NaoModelManager(0), new NopSeeObjectCalculator()), new SampleAgentContext(), tAgent);
        PoseManager tPoseManager = new FilePoseManager(tPoseDIr, new JsonPoseCodec(new NaoJointIdentifierContainer()), true);
        FileMotionManager tManager = new FileMotionManager(tMotionDir, new OldMotionCodec(tPoseManager, new NaoJointIdentifierContainer()));
        DefaultPose tPose1 = new DefaultPose();
        DefaultPose tPose2 = new DefaultPose();
        DefaultPose tPose3 = new DefaultPose();
        tPose1.setJointAngle(NaoJointIdentifier.HJ1, Angle.createAngleAsDegree(-50));
        tPose2.setJointAngle(NaoJointIdentifier.HJ1, Angle.createAngleAsDegree(50));
        tPose3.setJointAngle(NaoJointIdentifier.HJ1, Angle.createAngleAsDegree(0));
        tPoseManager.setPose("test1", tPose1);
        tPoseManager.setPose("test2", tPose2);
        tPoseManager.setPose("test3", tPose3);
        PoseCollectionModel tPoseModel = new DefaultPoseCollectionModel(tPoseManager, new NaoObjectContainer(), new NaoJointIdentifierContainer(), new NaoJointPositionMapper());
        MotionCollectionModel tMotionModel = new DefaultMotionCollectionModel(tManager, tPoseManager, new NaoJointIdentifierContainer(), tPoseModel, "zero");
        MotionPanel tPanel = new MotionPanel(tMotionModel, tPoseModel, new DefaultMotionController(tSelector, tMotionModel));
        JFrame tFrame = new JFrame("MotionPanel");
        tFrame.add(tPanel);
        tFrame.setSize(600, 400);
        tFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tFrame.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent aE) {
            }

            public void windowClosed(WindowEvent aE) {
                tRunner.stopAgent();
            }

            public void windowClosing(WindowEvent aE) {
            }

            public void windowDeactivated(WindowEvent aE) {
            }

            public void windowDeiconified(WindowEvent aE) {
            }

            public void windowIconified(WindowEvent aE) {
            }

            public void windowOpened(WindowEvent aE) {
            }
        });
        tFrame.setVisible(true);
        tRunner.start();
        try {
            tRunner.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FileUtils.deleteQuietly(tMotionDir);
        FileUtils.deleteQuietly(tPoseDIr);
    }
}

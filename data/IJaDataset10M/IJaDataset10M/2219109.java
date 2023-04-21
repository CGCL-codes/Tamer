package fakegame.flow.operations;

import common.RND;
import configuration.fakegame.ProjectTo2DConfig;
import fakegame.flow.Blackboard;
import fakegame.flow.utils.DatasetOverview;
import fakegame.flow.utils.Projection2DContainer;
import game.data.projection.*;
import game.report.ReportManager;
import game.report.subreports.ProjectTo2DSR;
import game.utils.Exceptions.InvalidArgument;
import preprocessing.storage.SimplePreprocessingStorage;

/**
 * Created by IntelliJ IDEA.
 * User: drchaj1
 * Date: Nov 8, 2009
 * Time: 1:55:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectTo2D implements IOperation {

    private ProjectTo2DConfig cfg;

    public static final String P2D_PROJECTION_SAMMON_EUC = "projection_2d_sammon_euc";

    public static final String P2D_PROJECTION_SAMMON_MAN = "projection_2d_sammon_man";

    public static final String P2D_PROJECTION_EQA_EUC = "projection_2d_eqa_euc";

    public static final String P2D_PROJECTION_EQA_MAN = "projection_2d_eqa_man";

    public static final String P2D_PROJECTION_EQB_EUC = "projection_2d_eqb_euc";

    public static final String P2D_PROJECTION_EQB_MAN = "projection_2d_eqb_man";

    public static final String P2D_REDUCED = "projection_2d_reduced";

    public static final String P2D_SELECTED = "projection_2d_selected";

    public static final int MAX_INPUTATTRIBUTES = 10;

    public void run(Blackboard blackboard) {
        this.cfg = (ProjectTo2DConfig) blackboard.getConfigurations().getOperationsConfig(ProjectTo2DConfig.class);
        SimplePreprocessingStorage storage = null;
        try {
            storage = chooseStorage(blackboard);
        } catch (InvalidArgument invalidArgument) {
            invalidArgument.printStackTrace();
            return;
        }
        if (storage.getNumberOfInputAttributes() > MAX_INPUTATTRIBUTES) return;
        double[][] data = storage.getAttributesAsDoubles(storage.getInputAttributesIndices());
        int[] classIndices = null;
        if (blackboard.getMiningType() != DatasetOverview.MiningType.REGRESSION) {
            classIndices = storage.getClassIndices();
        }
        double[][] distanceMatrixEuclidean = null;
        double[][] distanceMatrixManhattan = null;
        if (cfg.isEuclidean()) {
            distanceMatrixEuclidean = DistanceMatrix.createDistanceMatrix(data, new DistanceEuclidean(storage.getNumberOfInputAttributes()));
        }
        if (cfg.isManhattan()) {
            distanceMatrixManhattan = DistanceMatrix.createDistanceMatrix(data, new DistanceManhattan(storage.getNumberOfInputAttributes()));
        }
        if (cfg.isSammon()) {
            if (cfg.isEuclidean()) {
                createProjection(blackboard, new EquationsSammon(), distanceMatrixEuclidean, classIndices, P2D_PROJECTION_SAMMON_EUC);
            }
            if (cfg.isManhattan()) {
                createProjection(blackboard, new EquationsSammon(), distanceMatrixManhattan, classIndices, P2D_PROJECTION_SAMMON_MAN);
            }
        }
        if (cfg.isDrchalA()) {
            if (cfg.isEuclidean()) {
                createProjection(blackboard, new EquationsA(), distanceMatrixEuclidean, classIndices, P2D_PROJECTION_EQA_EUC);
            }
            if (cfg.isManhattan()) {
                createProjection(blackboard, new EquationsA(), distanceMatrixManhattan, classIndices, P2D_PROJECTION_EQA_MAN);
            }
        }
        if (cfg.isDrchalB()) {
            if (cfg.isEuclidean()) {
                createProjection(blackboard, new EquationsB(), distanceMatrixEuclidean, classIndices, P2D_PROJECTION_EQB_EUC);
            }
            if (cfg.isManhattan()) {
                createProjection(blackboard, new EquationsB(), distanceMatrixManhattan, classIndices, P2D_PROJECTION_EQB_MAN);
            }
        }
        ReportManager.INSTANCE.storeSubreport(new ProjectTo2DSR(blackboard));
    }

    private SimplePreprocessingStorage chooseStorage(Blackboard blackboard) throws InvalidArgument {
        SimplePreprocessingStorage storage;
        SimplePreprocessingStorage masterStorage = blackboard.getMasterDatasetStorage();
        if (cfg.isReduced() && cfg.isSelected()) {
            storage = blackboard.getReducedSelectedMasterDatasetStorage();
            if (masterStorage.getAttributeLength(0) > storage.getAttributeLength(0)) {
                blackboard.set(P2D_REDUCED, storage.getAttributeLength(0));
            }
            if (masterStorage.getNumberOfInputAttributes() > storage.getNumberOfInputAttributes()) {
                blackboard.set(P2D_SELECTED, storage.getNumberOfInputAttributes());
            }
        } else if (cfg.isReduced()) {
            storage = blackboard.getReducedMasterDatasetStorage();
            if (masterStorage.getAttributeLength(0) > storage.getAttributeLength(0)) {
                blackboard.set(P2D_REDUCED, storage.getAttributeLength(0));
            }
        } else if (cfg.isSelected()) {
            storage = blackboard.getSelectedMasterDatasetStorage();
            if (masterStorage.getNumberOfInputAttributes() > storage.getNumberOfInputAttributes()) {
                blackboard.set(P2D_SELECTED, storage.getNumberOfInputAttributes());
            }
        } else {
            storage = masterStorage;
        }
        return storage;
    }

    private void createProjection(Blackboard blackboard, EquationsInterface equationsInterface, double[][] distanceMatrix, int[] classIndices, String code) {
        int numOfInstances = distanceMatrix.length;
        RND.initialize(1234l);
        DistanceProjection prj = new DistanceProjection(distanceMatrix, equationsInterface);
        prj.project();
        double[] x = new double[numOfInstances];
        double[] y = new double[numOfInstances];
        prj.getProjectionXArray(x);
        prj.getProjectionYArray(y);
        Projection2DContainer projection2DContainer = new Projection2DContainer(x, y, classIndices, prj.getEnergy());
        blackboard.set(code, projection2DContainer);
    }

    public String getName() {
        return "Project dataset to 2D";
    }

    public String getDescription() {
        return "Projects dataset to 2D using Sammon's or Drchal's projection.";
    }
}

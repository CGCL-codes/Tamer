package rcscene.preprocessing.clustering;

import java.util.ArrayList;
import java.util.List;
import rcscene.DistanceCalculation;
import rcscene.preprocessing.clustering.metrics.DistanceMatrix;
import rcscene.preprocessing.clustering.vectorAlgorithms.KMeans;
import rcscene.preprocessing.clustering.vectorAlgorithms.VectorPoint;
import sceneInfo.Scene;
import sceneInfo.SceneLib;

public class FeatureMappedFixedNumberOfClusters {

    private List<VectorPoint> points;

    private SceneLib cases;

    private DistanceCalculation distCalc;

    public FeatureMappedFixedNumberOfClusters(SceneLib casebase, DistanceCalculation dc) {
        if (casebase == null || dc == null) {
            throw new IllegalArgumentException("FeatureMappedFixedNumberOfClusters constructed with null values.");
        }
        this.distCalc = dc;
        this.cases = casebase;
        DistanceMatrix m = new DistanceMatrix(casebase, distCalc);
        points = new ArrayList<VectorPoint>();
        for (int ii = 0; ii < cases.size(); ii++) {
            points.add(new VectorPoint("" + ii, m.getCaseDistances(ii)));
        }
    }

    public List<ArrayList<Scene>> cluster(int numClusters) {
        if (numClusters < 1 || numClusters > cases.size()) {
            throw new IllegalArgumentException("Invalid number of clusters requested");
        }
        KMeans km = new KMeans(points);
        List<ArrayList<VectorPoint>> vectorCluster = km.cluster(numClusters);
        List<ArrayList<Scene>> actualClusters = new ArrayList<ArrayList<Scene>>(vectorCluster.size());
        ArrayList<Scene> caseList = cases.getSceneList();
        for (int ii = 0; ii < vectorCluster.size(); ii++) {
            ArrayList<VectorPoint> vpCluster = vectorCluster.get(ii);
            ArrayList<Scene> caseCluster = new ArrayList<Scene>(vpCluster.size());
            for (int jj = 0; jj < vpCluster.size(); jj++) {
                VectorPoint pnt = vpCluster.get(jj);
                int caseIndex = Integer.parseInt(pnt.getID());
                caseCluster.add(caseList.get(caseIndex));
            }
            actualClusters.add(caseCluster);
        }
        return actualClusters;
    }
}

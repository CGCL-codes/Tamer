package cluster;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: shyam.s
 * Date: Apr 18, 2004
 * Time: 4:26:06 PM
 */
public class PrgMain {

    public static void main(String args[]) {
        List<DataPoint> dataPoints = new ArrayList<DataPoint>();
        dataPoints.add(new DataPoint(new float[] { 22, 21 }, 1));
        dataPoints.add(new DataPoint(new float[] { 2, 70 }, 2));
        dataPoints.add(new DataPoint(new float[] { 23, 71 }, 3));
        dataPoints.add(new DataPoint(new float[] { 4, 21 }, 4));
        dataPoints.add(new DataPoint(new float[] { 22, 21 }, 5));
        JCA jca = new JCA(2, 1000, dataPoints);
        jca.startAnalysis();
        System.out.println(jca.getCluster(0));
        System.out.println(jca.getCluster(1));
        for (List<DataPoint> tempV : jca.getClusterOutput()) {
            for (DataPoint dpTemp : tempV) {
            }
        }
    }
}

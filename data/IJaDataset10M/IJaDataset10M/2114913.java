package imp.cluster;

import java.io.Serializable;
import java.util.*;

public class ClusterSet implements Serializable {

    private final int numberOfRelatedPoints = 20;

    private Cluster original;

    private Vector<Cluster> relatives = new Vector<Cluster>();

    Cluster[] allClusters;

    private Vector<DataPoint> points = new Vector<DataPoint>();

    public ClusterSet(Cluster[] all, Cluster orig) {
        original = orig;
        allClusters = all;
        calcRelatives();
        setDataPoints();
    }

    public Vector<Cluster> getSimilarClusters() {
        return relatives;
    }

    private void setDataPoints() {
        points.addAll(original.getDataPoints());
        for (int i = 0; i < relatives.size(); i++) {
            Cluster c = relatives.get(i);
            points.addAll(c.getDataPoints());
        }
    }

    private void calcRelatives() {
        Vector<double[]> similarities = getSimilarities();
        Collections.sort((List) similarities, new ClusterSimilarityComparer());
        int numRelatives = 6;
        for (int i = 0; i < numRelatives && i < similarities.size(); i++) {
            if (this.getNumPointsInRelatives() >= numberOfRelatedPoints) break;
            int index = (int) similarities.get(i)[0];
            Cluster c = allClusters[index];
            relatives.add(c);
        }
    }

    private Vector<double[]> getSimilarities() {
        Vector<double[]> sims = new Vector<double[]>();
        for (int i = 0; i < allClusters.length; i++) {
            Cluster other = allClusters[i];
            if (!(other.equals(original))) {
                double[] sim = new double[2];
                sim[0] = other.getNumber();
                sim[1] = getPairwiseSimilarity(original, other);
                sims.add(sim);
            }
        }
        return sims;
    }

    private double getPairwiseSimilarity(Cluster a, Cluster b) {
        double distance = 0;
        int pairsToCheck = 100;
        for (int i = 0; i < pairsToCheck; i++) {
            DataPoint pointA = a.getRandomDataPoint();
            DataPoint pointB = b.getRandomDataPoint();
            distance += pointA.calcEuclideanDistance(pointB);
        }
        distance /= (double) pairsToCheck;
        return distance;
    }

    public int getNumPointsInRelatives() {
        int num = 0;
        for (int i = 0; i < relatives.size(); i++) {
            num += relatives.get(i).getNumDataPoints();
        }
        return num;
    }

    public int getNumRelatives() {
        return relatives.size();
    }

    public Cluster getOriginal() {
        return original;
    }

    public Cluster getRandomCluster() {
        Random rand = new Random();
        int p = rand.nextInt(relatives.size() + 1);
        if (p == relatives.size()) {
            return original;
        } else {
            return relatives.get(p);
        }
    }

    public Vector<Cluster> getStarterClusters() {
        Vector<Cluster> starters = new Vector<Cluster>();
        if (original.containsStarter()) {
            starters.add(original);
        }
        for (int i = 0; i < relatives.size(); i++) {
            Cluster c = relatives.get(i);
            if (c.containsStarter()) {
                starters.add(c);
            }
        }
        return starters;
    }

    public DataPoint getRandomPoint() {
        Random rand = new Random();
        int p = rand.nextInt(points.size());
        return points.get(p);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("cluster set of size " + relatives.size() + "\n");
        for (Enumeration<Cluster> e = relatives.elements(); e.hasMoreElements(); ) {
            buffer.append("");
            buffer.append(e.nextElement().toString());
            buffer.append("\n");
        }
        return buffer.toString();
    }
}

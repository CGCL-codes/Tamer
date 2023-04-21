package env;

import jason.environment.grid.Location;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import jia.Vec;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.clusterers.Cobweb;
import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.clusterers.XMeans;
import weka.clusterers.sIB;
import arch.LocalWorldModel;

/**
 * Make the clustering of the cows and stores the Clusters in a Model, it calculates the clustering with the weka tool.
 * 
 * @author Gustavo Pacianotto Gouveia
 * @author Victor Lassance Oliveira e Silva 
 */
public class ClusterModel implements Runnable, IClusterModel {

    public static final int CLUSTERER_COBWEB = 3;

    public static final int CLUSTERER_EM = 1;

    public static final int CLUSTERER_FF = 5;

    public static final int CLUSTERER_sIB = 4;

    public static final int CLUSTERER_XMEANS = 2;

    public static final int DOUBLED_MEDIAN = 4;

    public static final int RADIUS_DBLDAVERAGE = 3;

    public static final int RADIUS_MAXDIST = 1;

    public static final int RADIUS_SD = 2;

    private static final int DOWN = 0, UP = 1, LEFT = 2, RIGHT = 3;

    private static final String CENTERS_NAME_FILE = "centers.arff";

    private static final String NAME_FILE = "cows.arff";

    Cluster[] clusters = new Cluster[0];

    ClusterEvaluation ce;

    ICowModel cModel = CowModelFactory.getModel("ClusterModel");

    List<Integer> treeToAdd = new Vector<Integer>();

    private boolean spc = true;

    private boolean[][] trees;

    private boolean useTuner = false;

    private Cow[] cows;

    private File file;

    private File previous;

    private int actStep = -1;

    private int[] alloc = new int[2];

    private int clStep;

    private int[][] cowsCluster;

    private int[][] data = new int[2][2];

    private int dataNumber;

    private int gH = 0;

    private int gW = 0;

    private int[] maxDist;

    private int n;

    private int[] numcows;

    private int prefRadius = 10, prefnCows = 50, kPTC = 100;

    private int[] s;

    private int useClusterer = 1;

    private int useRadius = 4;

    private Logger logger = Logger.getLogger(ClusterModel.class.getName());

    private Vec[] Center;

    /**
	 * Constructs this Model and verify if the tuner needs to be called
	 */
    public ClusterModel() {
        this.init();
        if (useTuner) {
            configTable.main(null);
        }
    }

    public void init() {
        clusters = new Cluster[0];
        treeToAdd = new Vector<Integer>();
        trees = null;
        cows = null;
        actStep = -1;
        alloc = new int[2];
        cowsCluster = null;
        data = new int[2][2];
        gH = 0;
        gW = 0;
        numcows = null;
        s = null;
        Center = null;
        System.gc();
    }

    /**
	 * It is the function that implements the process, it needs to be called every step.
	 */
    public synchronized void run() {
        if (actStep < clStep) {
            actStep = clStep;
            alloc = calculate();
            cCenters();
            setMap();
            cMaxDist();
            printClusters();
            for (Cluster c : clusters) {
                logger.info("GGG " + c.toString());
            }
        }
    }

    /**
	 * Just for debuging, it prints the clusters and it's positions.
	 */
    private void printClusters() {
        String str = new String();
        str = "HHH the number of clusters is " + n + "| ";
        for (int i = 0; i < n; i++) {
            str += "cluster " + i + " with position (" + Center[i].getX() + "," + (gH - Center[i].getY() - 1) + ") and size = " + maxDist[i] + "|";
        }
        logger.info(str);
    }

    /**
	 * It returns the Radius of the Cluster, i.e. the distance of the faraway cow from the Cluster center;
	 * @return maxDist
	 */
    public int[] getMaxDist() {
        return maxDist;
    }

    /**
	 * Sets the step of the simulation before the clustering.
	 * @param step
	 */
    public void setStepcl(int step) {
        if (step > clStep) clStep = step;
        if (step == -1) {
            clStep = 0;
            actStep = -1;
        }
    }

    /**
	 * It calculates the Radius of each Cluster and stores it on the model.
	 */
    private void cMaxDist() {
        maxDist = new int[clusters.length];
        for (int i = 0; i < maxDist.length; i++) maxDist[i] = clusters[i].getRadius();
    }

    /**
	 * It gives the allocation of the cows, the i-th cow is in the al[i] cluster.
	 * @return alloc
	 */
    public int[] getAlloc() {
        int[] al = alloc.clone();
        return al;
    }

    /**
	 * It returns the number of clusters on the evaluation;
	 */
    public int getNumberOfCluster() {
        return clusters.length;
    }

    /**
	 * It returns the number of Cows in each Cluster.
	 * @return
	 */
    public int[] getNumCows() {
        return numcows;
    }

    /**
	 * It sets the id of the cows to the id of the Cluster
	 */
    private void setMap() {
        s = new int[1000];
        for (int i = 0; i < 998; i++) s[i] = -1;
        for (int i = 0; i < dataNumber; i++) {
            s[cows[i].id] = alloc[i];
        }
    }

    public int[] getMap() {
        if (s == null) {
            s = new int[1000];
            for (int i = 0; i < 998; i++) s[i] = -1;
        }
        return s;
    }

    /**
	 * it calculates the center of each cluster, a simple Center of mass.
	 */
    private void cCenters() {
        Center = new Vec[clusters.length];
        numcows = new int[clusters.length];
        for (int i = 0; i < clusters.length; i++) {
            Center[i] = clusters[i].getCenter();
            numcows[i] = clusters[i].getNumCows();
        }
    }

    /**
	 * Print the centers on the logger, just for debbuging.
	 */
    @SuppressWarnings("unused")
    private void printCenters() {
        Vec[] c = Center.clone();
        String str = new String();
        str += ">";
        for (int i = 0; i < c.length; i++) {
            logger.info(str);
            str += "  [" + i + "]" + "(" + (int) c[i].x + "," + (int) c[i].y + ")";
        }
        logger.info(str);
    }

    /**
	 * It returns the center of each cluster.
	 * @return Centers
	 */
    public Vec[] getCenters() {
        return Center;
    }

    /**
	 * it returns the data used for the Clustering, i.e. the position of each cow.
	 * @return
	 */
    public int[][] getData() {
        return data.clone();
    }

    /**
	 * Saves the centers of the clusters inside a file, where the new iteration can read the values, 
	 * It should help to maintain the id of the clusters more stable. But it also 
	 * makes the number of clusters constant. Not in use.
	 */
    @SuppressWarnings("unused")
    private void createCentersFile() {
        try {
            previous = new File(CENTERS_NAME_FILE);
            if (!previous.exists()) previous.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(previous, false));
            out.write("@relation 'centers'\n");
            out.write("@attribute X real\n@attribute Y real\n@data\n\n");
            for (int i = 0; i < Center.length; i++) {
                out.write(Center[i].getX() + "," + Center[i].getY() + "\n");
            }
            out.write("\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * It creates the files used for the weka tool.
	 * @param null
	 */
    private void createFile() {
        int i;
        try {
            file = new File(NAME_FILE);
            if (!file.exists()) file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
            out.write("@relation 'cows'\n");
            out.write("@attribute X real\n@attribute Y real\n@data\n\n");
            for (i = 0; i < dataNumber; i++) out.write(data[0][i] + "," + data[1][i] + "\n");
            out.write("\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * It gets the Cows from CowModel and do the clustering. It returns the allocation of each cow to it's cluster.
	 * @param NumCows
	 * @return alloc
	 */
    private void defineData(int[][] inf) {
        data = inf.clone();
    }

    /**
	 * Change the class that will be used for clustering.
	 * @Use 1 EM; 2 Xmeans;3 cobWeb;4 sIB;5 FF
	 */
    public void changeClusterer(int a) {
        useClusterer = a;
    }

    /**
	 * Change the way of calculating the radius of a cluster.
	 */
    public void changeMaxDist(int a) {
        useRadius = a;
    }

    /**
	 * Change the values used on Weights of clusters.
	 * @param radius
	 * @param nCows
	 */
    public void changeWCprop(int radius, int nCows) {
        prefRadius = radius;
        prefnCows = nCows;
    }

    /**
	 * returns the preferential radius for a cluster. (WofC)
	 * @return
	 */
    public int getPrefRadius() {
        return prefRadius;
    }

    /**
	 * returns the preferential number of cows to be herded.
	 * @return
	 */
    public int getPrefNCows() {
        return prefnCows;
    }

    /**
	 * change the k factor used in position_to_cluster.
	 * @param k
	 */
    public void changePTCprop(int k) {
        kPTC = k;
    }

    /**
	 * returns the k factor used in position_to_cluster.
	 * @return
	 */
    public double getPrefkPTC() {
        return (double) kPTC / 100.0;
    }

    /**
	 * It inserts trees on the model.
	 */
    public synchronized void insertTree(int x, int y) {
        if (!(x >= gW || x < 0 || y >= gH || y < 0)) {
            while (treeToAdd.size() > 0) {
                int tmp = treeToAdd.remove(0);
                insertTree(tmp / 10000, tmp % 10000);
            }
            try {
                trees[x][-y + gH - 1] = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (gW == 0 || gH == 0) {
                treeToAdd.add(x * 10000 + y);
            }
        }
    }

    public void setCows(Cow[] c, int H, int W) {
        spc = false;
        cows = c;
        gH = H;
        gW = W;
    }

    /**
	 * It is the kernel of the algorithm of clustering, it calls weka and parses the String returned.
	 * @return Allocations
	 */
    private int[] calculate() {
        ce = new ClusterEvaluation();
        int countStepsWithProblem = 0;
        if (spc) {
            cModel.updateCows();
            cows = cModel.getCows();
            while (gH <= 0 || gW <= 0 || clStep == -1) {
                gH = cModel.getSizeh();
                gW = cModel.getSizew();
                if (countStepsWithProblem++ > 20) {
                    break;
                }
            }
        }
        if (cows == null || cows.length <= 0) {
            dataNumber = 0;
            gH = gW = 0;
        } else dataNumber = cows.length;
        trees = new boolean[gW][gH];
        int[][] infos = new int[2][dataNumber];
        if (dataNumber == 0) return null;
        for (int i = 0; i < dataNumber; i++) {
            infos[0][i] = cows[i].x;
            infos[1][i] = gH - 1 - cows[i].y;
        }
        defineData(infos);
        createFile();
        Clusterer cluster = null;
        String[] options = null;
        switch(useClusterer) {
            case 1:
                cluster = new EM();
                options = new String[4];
                options[0] = "-t";
                options[1] = file.getAbsolutePath();
                options[2] = "-p";
                options[3] = "0";
                break;
            case 2:
                cluster = new XMeans();
                options = new String[8];
                options[0] = "-t";
                options[1] = file.getAbsolutePath();
                options[2] = "-p";
                options[3] = "0";
                options[4] = "-H";
                options[5] = "6";
                options[6] = "-D";
                options[7] = "weka.core.ChebyshevDistance";
                break;
            case 3:
                cluster = new Cobweb();
                options = new String[4];
                options[0] = "-t";
                options[1] = file.getAbsolutePath();
                options[2] = "-p";
                options[3] = "0";
                break;
            case 4:
                cluster = new sIB();
                options = new String[4];
                options[0] = "-t";
                options[1] = file.getAbsolutePath();
                options[2] = "-p";
                options[3] = "0";
                break;
            case 5:
                cluster = new FarthestFirst();
                options = new String[4];
                options[0] = "-t";
                options[1] = file.getAbsolutePath();
                options[2] = "-p";
                options[3] = "0";
                break;
        }
        try {
            String str = ClusterEvaluation.evaluateClusterer(cluster, options);
            logger.info("HHH calculated with " + useClusterer);
            int[] assign = new int[dataNumber];
            String[] tmp = str.split("\n");
            int numberOfClusters = 0;
            int[] cowsPerCluster = new int[dataNumber];
            for (int i = 0; i < dataNumber; i++) {
                assign[i] = (int) (Double.parseDouble(tmp[i].split(" ")[1]) + 0.1);
                if (assign[i] + 1 > numberOfClusters) {
                    numberOfClusters = assign[i] + 1;
                }
                cowsPerCluster[assign[i]]++;
            }
            genCluster(infos, assign, numberOfClusters);
            return assign;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void genCluster(int[][] infos, int[] assign, int numberOfClusters) {
        clusters = new Cluster[numberOfClusters];
        for (int i = 0; i < assign.length; i++) {
            if (clusters[assign[i]] == null) clusters[assign[i]] = new Cluster();
            clusters[assign[i]].getCows().add(new Vec(infos[0][i], infos[1][i]));
        }
        for (int i = 0; i < clusters.length; i++) clusters[i].calculateProperties(useRadius);
    }

    public static class Cluster {

        private Vec center;

        private int numCows;

        private int radius;

        private Vec[] border;

        private boolean usingPolygon;

        private Vector<Vec> cows;

        public String toString() {
            String str = "";
            str += "C[" + center.x + "," + center.y + "] ";
            str += "numcows=" + numCows;
            str += " radius=" + radius;
            return str;
        }

        public void calculateProperties(int useRadius) {
            this.numCows = cows.size();
            calculateCenter();
            calculateRadius(useRadius);
        }

        public void calculateBubble(Vec direction) {
            border = OrdenableVec.grahanScan(direction, center, cows);
        }

        public Vec[] getBubble() {
            return border;
        }

        public void setCows(int[][] data, int dataNum) {
            cows = new Vector<Vec>();
            for (int i = 0; i < dataNum; i++) {
                cows.add(new Vec(data[0][i], data[1][i]));
            }
        }

        public Vec getCenter() {
            return (Vec) center.clone();
        }

        public int getRadius() {
            return radius;
        }

        public void calculateCenter() {
            center = Vec.mean(cows);
        }

        void calculateRadius(int useRadius) {
            switch(useRadius) {
                case 1:
                    radius = 0;
                    for (int i = 0; i < numCows; i++) {
                        Vec tmp = cows.get(i).sub(center);
                        int radTmp = Math.max(Math.abs((int) tmp.x), Math.abs((int) tmp.y));
                        if (radius < radTmp) {
                            radius = radTmp;
                        }
                    }
                    break;
                case 2:
                    radius = 0;
                    for (int i = 0; i < numCows; i++) {
                        Vec tmp = cows.get(i).sub(center);
                        double radTmp = Math.max(Math.abs(tmp.x), Math.abs(tmp.y));
                        radius += radTmp * radTmp;
                    }
                    radius /= numCows;
                    radius = (int) (Math.sqrt(radius) + 0.5);
                    break;
                case 3:
                    radius = 0;
                    for (int i = 0; i < numCows; i++) {
                        Vec tmp = cows.get(i).sub(center);
                        radius += Math.max(Math.abs(tmp.x), Math.abs(tmp.y));
                    }
                    radius /= numCows;
                    radius = (int) (radius * 2 + 0.5);
                    break;
                case 4:
                    Vector<Integer> temp = new Vector<Integer>();
                    for (int i = 0; i < cows.size(); i++) {
                        Vec tmp = cows.get(i).sub(center);
                        int radTmp = Math.max(Math.abs((int) (tmp.x)), Math.abs((int) (tmp.y)));
                        temp.add(radTmp);
                    }
                    Collections.sort(temp);
                    double distOut = 0;
                    if (temp.size() > 3) {
                        double pos3Quartil = 0.75 * (double) temp.size();
                        double pos1Quartil = 0.25 * (double) temp.size();
                        distOut = 2.5 * temp.get((int) pos3Quartil) - 1.5 * temp.get((int) pos1Quartil);
                    }
                    int binS = Collections.binarySearch(temp, (int) distOut);
                    if (binS < 0) binS = -binS - 1;
                    switch(binS % 2) {
                        case 1:
                            radius = 2 * temp.get(binS / 2);
                            break;
                        case 0:
                            radius = temp.get(binS / 2);
                            if (binS != 0) radius += temp.get(binS / 2 - 1);
                            break;
                    }
                    for (int i = 0; i < cows.size(); i++) {
                        Vec tmp = cows.get(i).sub(center);
                        int radTmp = Math.max(Math.abs((int) (tmp.x)), Math.abs((int) (tmp.y)));
                        if (radTmp > distOut && distOut > 0) {
                            cows.remove(i);
                        }
                    }
                    break;
            }
        }

        public Vector<Vec> getCows() {
            if (cows == null) cows = new Vector<Vec>();
            return cows;
        }

        public void setCows(Vector<Vec> cows) {
            this.cows = cows;
        }

        public int getNumCows() {
            return numCows;
        }
    }

    public static class OrdenableVec extends Vec implements Comparator<OrdenableVec> {

        private static final long serialVersionUID = 5241512769005110956L;

        static OrdenableVec pivot;

        static OrdenableVec center;

        static OrdenableVec direction;

        public OrdenableVec(double x, double y) {
            super(x, y);
        }

        public OrdenableVec(LocalWorldModel model, Location l) {
            super(model, l);
        }

        public OrdenableVec(LocalWorldModel model, int x, int y) {
            super(model, x, y);
        }

        public OrdenableVec() {
            super(0, 0);
        }

        public int compare(OrdenableVec arg0, OrdenableVec arg1) {
            return (int) (10 * (arg0.cross(direction) - arg1.cross(direction)));
        }

        double cross(OrdenableVec o) {
            return this.x * o.y - this.y * o.x;
        }

        private OrdenableVec distanceVector(OrdenableVec u) {
            OrdenableVec proj = (OrdenableVec) direction.product((u.sub(pivot)).dot(direction) / direction.r);
            return (OrdenableVec) u.sub(direction).sub(proj);
        }

        static synchronized Vec[] grahanScan(Vec direction, Vec center, Vector<Vec> cows) {
            OrdenableVec.direction = new OrdenableVec(direction.x, direction.y);
            OrdenableVec.center = new OrdenableVec(center.x, center.y);
            pivot = new OrdenableVec(cows.get(0).x, cows.get(0).y);
            int itPivot = 0, M;
            OrdenableVec[] points = new OrdenableVec[cows.size() + 1];
            points[0] = new OrdenableVec(cows.get(0).x, cows.get(0).y);
            for (int i = 0; i < cows.size(); i++) {
                points[i + 1] = new OrdenableVec(cows.get(i).x, cows.get(i).y);
                if ((new OrdenableVec()).compare(points[itPivot], points[i + 1]) > 0) {
                    itPivot = i + 1;
                }
            }
            pivot = points[itPivot];
            points[itPivot] = points[1];
            points[1] = pivot;
            Comparator<OrdenableVec> c = new OrdenableVec();
            Arrays.sort(points, 2, points.length, c);
            points[0] = points[points.length - 1];
            M = 2;
            for (int i = 3; i < points.length; i++) {
                while (ccw(points[M - 1], points[M], points[i]) <= 0) {
                    M--;
                }
                M++;
                OrdenableVec temp = points[M];
                points[M] = points[i];
                points[i] = temp;
            }
            Vec[] resp = new Vec[M];
            for (int i = 0; i < M; i++) {
                resp[i] = new Vec(points[i + 1].x, points[i + 1].y);
            }
            return resp;
        }

        static double ccw(Vec p1, Vec p2, Vec p3) {
            return ((p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x));
        }

        static int smallerYX(Vec v1, Vec v2) {
            if ((int) v1.y != (int) v2.y) return (int) (v1.y - v2.y);
            return (int) (v1.x - v2.y);
        }
    }

    public void update() {
    }
}

package com.lordjoe.collectiveintelligence;

import java.util.*;
import java.io.*;

/**
 * com.lordjoe.collectiveintelligence.TestHandler
 *  code.googlecom/p/wind-ding
 *
 *  xp-dev.com - subversion site
 * @author Steve Lewis
 * @date Feb 24, 2009
 */
public class TestHandler {

    public static TestHandler[] EMPTY_ARRAY = {};

    public static Class THIS_CLASS = TestHandler.class;

    public static final Random RND = new Random();

    public static final String DATA_PATH = "../python/ch03/";

    private String[] m_ColumnNames;

    private List<ICluster> m_Urls;

    private List<ICluster> m_ActiveClusters;

    private Map<PairId<ICluster>, Double> m_Correlations;

    /**
     * classd to perform clustering
     * @param dataFile
     * @throws IOException
     */
    public TestHandler(String dataFile) throws IOException {
        m_Correlations = new HashMap<PairId<ICluster>, Double>();
        m_Urls = new ArrayList<ICluster>();
        m_ActiveClusters = new ArrayList<ICluster>();
        LineNumberReader rdr = new LineNumberReader(new FileReader(DATA_PATH + dataFile));
        String cols = rdr.readLine();
        m_ColumnNames = cols.split("\t");
        String line = rdr.readLine();
        while (line != null) {
            buildNamedVector(line);
            line = rdr.readLine();
        }
    }

    /**
     * find correlations between clusters and cache them
     */
    private void buildCorrelations() {
        m_ActiveClusters.clear();
        m_Correlations.clear();
        NamedVector[] vecs = getVectors();
        for (int i = 0; i < vecs.length; i++) {
            NamedVector vec1 = vecs[i];
            m_ActiveClusters.add(vec1);
            for (int j = i + 1; j < vecs.length; j++) {
                NamedVector vec2 = vecs[j];
                double correlation = PearsonDistance.Instance.distance(vec1, vec2);
                addCorrelation(vec1, vec2, correlation);
            }
        }
    }

    /**
     * generate clusters and write them out
     */
    public void cluster() {
        buildCorrelations();
        while (m_ActiveClusters.size() > 1) {
            doClustersStep();
        }
        ICluster cluster = m_ActiveClusters.get(0);
        writeCluster(cluster);
    }

    /**
     *  Do K CLustering
     *
     * @param nClusters number of clusters
     */
    public void kcluster(int nClusters, IDistanceMeter metre) {
        buildKClusters(nClusters, metre);
        while (moveKClusters(metre) > 0) ;
        writeKClusters();
    }

    /**
     * write clusters to console
     */
    protected void writeKClusters() {
        for (ICluster cluster : m_ActiveClusters) {
            writeCluster(cluster, 0);
        }
    }

    /**
     *  reassigning vecdtors to nearest cluster
     * @param metre non-null distance metric
     * @return  number of changes
     */
    protected int moveKClusters(IDistanceMeter metre) {
        ICluster[][] oldAssignments = saveAssignments();
        for (ICluster cluster : m_ActiveClusters) {
            cluster.clearParts();
        }
        assignVectors(metre);
        repositionClusters();
        ICluster[][] newAssignments = saveAssignments();
        int numberMovers = numberMoves(oldAssignments, newAssignments);
        return numberMovers;
    }

    /**
     * compute changes
     * @param oldAssignments vectors assigned to clusters before move
     * @param newAssignments  vectors assigned to clusters after move
     * @return  number changes
     */
    protected static int numberMoves(ICluster[][] oldAssignments, ICluster[][] newAssignments) {
        int sum = 0;
        for (int i = 0; i < oldAssignments.length; i++) {
            ICluster[] oldAssignment = oldAssignments[i];
            ICluster[] newAssignment = newAssignments[i];
            sum += numberMoves(oldAssignment, newAssignment);
        }
        return sum;
    }

    protected static int numberMoves(ICluster[] oldAssignment, ICluster[] newAssignment) {
        Set<ICluster> oldItems = new HashSet(Arrays.asList(oldAssignment));
        Set<ICluster> newItems = new HashSet(Arrays.asList(newAssignment));
        oldItems.removeAll(new HashSet(Arrays.asList(newAssignment)));
        newItems.removeAll(new HashSet(Arrays.asList(oldAssignment)));
        return oldItems.size() + newItems.size();
    }

    /**
     * move cluster to centroid of data
     */
    protected void repositionClusters() {
        for (ICluster cluster : m_ActiveClusters) {
            ((MultiCluster) cluster).resetValues();
        }
    }

    protected ICluster[][] saveAssignments() {
        List<ICluster[]> holder = new ArrayList<ICluster[]>();
        for (ICluster cluster : m_ActiveClusters) {
            ICluster[] assigned = cluster.getParts();
            holder.add(assigned);
        }
        ICluster[][] ret = new ICluster[holder.size()][];
        holder.toArray(ret);
        return ret;
    }

    public void assignVectors(IDistanceMeter metre) {
        for (ICluster url : m_Urls) {
            double minDist = Double.MAX_VALUE;
            ICluster closestCluster = null;
            for (ICluster cluster : m_ActiveClusters) {
                double dist = metre.distance(url, cluster);
                if (dist < minDist) {
                    closestCluster = cluster;
                    minDist = dist;
                }
            }
            if (closestCluster != null) closestCluster.addPart(url); else minDist = Double.MAX_VALUE;
        }
    }

    /**
     * build randomly located clusters within the space of data
     * @param nClusters number of clusters desired
     * @param metre  non-null metric function
     */
    private void buildKClusters(int nClusters, IDistanceMeter metre) {
        m_ActiveClusters.clear();
        m_Correlations.clear();
        ICluster[] clusters = m_Urls.toArray(ICluster.EMPTY_ARRAY);
        int numberItems = clusters[0].getValues().length;
        double[] minValue = new double[numberItems];
        double[] maxValue = new double[numberItems];
        for (int i = 0; i < clusters.length; i++) {
            ICluster clstr = clusters[i];
            double[] items = clstr.getValues();
            for (int j = 0; j < items.length; j++) {
                minValue[j] = Math.min(minValue[j], items[j]);
                maxValue[j] = Math.max(maxValue[j], items[j]);
            }
        }
        for (int i = 0; i < nClusters; i++) {
            buildKCluster(i, minValue, maxValue);
        }
    }

    protected void buildKCluster(int index, double[] minValue, double[] maxValue) {
        double[] values = new double[minValue.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = minValue[i] + (maxValue[i] - minValue[i]) * RND.nextDouble();
        }
        m_ActiveClusters.add(new KCluster("cluster " + index, values));
    }

    protected void writeCluster(ICluster cluster) {
        writeCluster(cluster, 0);
    }

    protected void writeCluster(ICluster cluster, int indent) {
        indent(indent);
        System.out.println(cluster.toString());
        if (cluster instanceof NamedVector) return;
        ICluster[] clusters = cluster.getParts();
        for (int i = 0; i < clusters.length; i++) {
            ICluster clstr = clusters[i];
            writeCluster(clstr, indent + 1);
        }
    }

    protected void indent(int indent) {
        for (int i = 0; i < indent; i++) System.out.print("   ");
    }

    protected void doClustersStep() {
        PairId<ICluster> closest = findClosestPair();
        insertNewCluster(closest);
    }

    protected void insertNewCluster(PairId<ICluster> added) {
        m_ActiveClusters.remove(added.getItem1());
        m_ActiveClusters.remove(added.getItem2());
        m_ActiveClusters.add(new BiCluster(added.getItem1(), added.getItem2()));
    }

    protected PairId<ICluster> findClosestPair() {
        ICluster[] clusters = m_ActiveClusters.toArray(ICluster.EMPTY_ARRAY);
        PairId<ICluster> closest = null;
        double closestDist = Double.MAX_VALUE;
        for (int i = 0; i < clusters.length; i++) {
            ICluster c1 = clusters[i];
            for (int j = i + 1; j < clusters.length; j++) {
                ICluster c2 = clusters[j];
                double dist = getCorrelation(c1, c2);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = new PairId<ICluster>(c1, c2);
                }
            }
        }
        return closest;
    }

    public void addCorrelation(ICluster v1, ICluster v2, double value) {
        m_Correlations.put(new PairId(v1, v2), value);
    }

    public Double getCorrelation(ICluster v1, ICluster v2) {
        Double ret = m_Correlations.get(new PairId(v1, v2));
        if (ret == null) {
            double value = PearsonDistance.Instance.distance(v1, v2);
            addCorrelation(v2, v2, value);
            return value;
        }
        return ret;
    }

    public String[] getColumnNames() {
        return m_ColumnNames;
    }

    public void setColumnNames(String[] pColumnNames) {
        m_ColumnNames = pColumnNames;
    }

    public NamedVector[] getVectors() {
        return (NamedVector[]) m_Urls.toArray(NamedVector.EMPTY_ARRAY);
    }

    protected void buildNamedVector(String line) {
        String[] Items = line.split("\t");
        String name = Items[0];
        double[] values = new double[Items.length - 1];
        for (int i = 1; i < Items.length; i++) {
            String item = Items[i];
            if (item.length() > 0) values[i - 1] = Integer.parseInt(item);
        }
        m_Urls.add(new NamedVector(name, values));
    }

    public static final int NUMBER_CLUSTERS = 7;

    public static void main(String[] args) throws IOException {
        TestHandler test = new TestHandler("blogdata.txt");
        test.kcluster(NUMBER_CLUSTERS, PearsonDistance.Instance);
    }
}

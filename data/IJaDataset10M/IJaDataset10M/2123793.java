package shellkk.qiq.math.ml.cluster;

import java.util.ArrayList;
import java.util.List;
import shellkk.qiq.math.distance.Distance;
import shellkk.qiq.math.ml.Cluster;
import shellkk.qiq.math.ml.Sample;

public class KMedoidsCluster implements Cluster {

    protected List<KMedoidsCluster> children = new ArrayList();

    protected List<Sample> elements = new ArrayList();

    protected KMedoidsCluster parent;

    protected Object center;

    protected double weight;

    protected double cost;

    protected Distance distance;

    protected int centerIndex;

    public KMedoidsCluster(Distance distance) {
        this.distance = distance;
    }

    public void add(Object element) {
        elements.add((Sample) element);
    }

    public void addChild(Cluster child) {
        children.add((KMedoidsCluster) child);
    }

    public void clearChildren() {
        children.clear();
    }

    public double unsimilar(Object element) {
        Sample s = (Sample) element;
        return distance.distance(center, s.x);
    }

    public double unsimilarWith(Cluster c) {
        KMedoidsCluster kc = (KMedoidsCluster) c;
        return distance.distance(center, kc.center);
    }

    public List getAll() {
        return new ArrayList(elements);
    }

    public Cluster[] getChildren() {
        return children.toArray(new Cluster[children.size()]);
    }

    public KMedoidsCluster getParent() {
        return parent;
    }

    public KMedoidsCluster merge(Cluster c) {
        KMedoidsCluster brother = (KMedoidsCluster) c;
        KMedoidsCluster merge = new KMedoidsCluster(distance);
        merge.weight = weight + brother.weight;
        merge.elements.addAll(elements);
        merge.elements.addAll(brother.elements);
        merge.cost = Double.MAX_VALUE;
        for (Sample s : merge.elements) {
            double cost = 0;
            for (Sample si : merge.elements) {
                cost += si.weight * distance.distance(s.x, si.x);
            }
            if (cost < merge.cost) {
                merge.center = s.x;
                merge.cost = cost;
            }
        }
        return merge;
    }

    public void computeMetrics() {
        cost = 0;
        weight = 0;
        for (Sample si : elements) {
            cost += si.weight * distance.distance(center, si.x);
            weight += si.weight;
        }
    }

    public void remove(Object element) {
        elements.remove(element);
    }

    public void removeChild(Cluster child) {
        children.remove(child);
    }

    public void setParent(Cluster parent) {
        this.parent = (KMedoidsCluster) parent;
    }

    public Object getCenter() {
        return center;
    }

    public void setCenter(Object center) {
        this.center = center;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getCenterIndex() {
        return centerIndex;
    }

    public void setCenterIndex(int centerIndex) {
        this.centerIndex = centerIndex;
    }

    public int getDepth() {
        int maxDepth = 0;
        for (KMedoidsCluster c : children) {
            int depth = c.getDepth();
            if (depth > maxDepth) {
                maxDepth = depth;
            }
        }
        return maxDepth + 1;
    }

    public int getLevel() {
        if (parent == null) {
            return 0;
        } else {
            return parent.getLevel() + 1;
        }
    }
}

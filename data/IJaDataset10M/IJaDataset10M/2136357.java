package shellkk.qiq.jdm.clustering;

public class ClusteringApplyResult {

    protected int clusterId;

    protected double probability;

    protected double qualityOfFit;

    protected double distance;

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getQualityOfFit() {
        return qualityOfFit;
    }

    public void setQualityOfFit(double qualityOfFit) {
        this.qualityOfFit = qualityOfFit;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

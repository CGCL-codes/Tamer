package ar.com.omnipresence.game.client;

import java.io.Serializable;

/**
 * Identifier of a cluster.
 * @author Martín Straus
 */
public class ClusterCoordinateTO implements Serializable {

    protected Long cluster;

    public ClusterCoordinateTO() {
    }

    public ClusterCoordinateTO(Long cluster) {
        this.cluster = cluster;
    }

    public Long getCluster() {
        return cluster;
    }

    public void setCluster(Long clusterId) {
        this.cluster = clusterId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClusterCoordinateTO other = (ClusterCoordinateTO) obj;
        if (this.cluster != other.cluster && (this.cluster == null || !this.cluster.equals(other.cluster))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.cluster != null ? this.cluster.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return String.valueOf(cluster);
    }
}

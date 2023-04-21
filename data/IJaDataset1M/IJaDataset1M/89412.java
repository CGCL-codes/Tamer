package shellkk.qiq.jdm.clustering;

import java.util.Collection;
import java.util.List;
import javax.datamining.JDMException;
import javax.datamining.clustering.Cluster;
import javax.datamining.clustering.ClusteringModelProperty;
import shellkk.qiq.jdm.base.IModelDetail;
import shellkk.qiq.jdm.data.LogicalDataRecord;

public interface ClusteringModelDetail extends IModelDetail {

    public Collection<Cluster> getClusters() throws JDMException;

    public List<String> getInputAttributeNames();

    public Double getSimilarity(int clusterIdentifier1, int clusterIdentifier2) throws JDMException;

    public boolean hasProperty(ClusteringModelProperty property);

    public ClusteringApplyResult[] getClusteringApplyResult(LogicalDataRecord logicRecord);
}

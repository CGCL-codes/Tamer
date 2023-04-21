package ra.lrrr.clustering.clustering;

import java.util.ArrayList;
import javax.vecmath.Point3d;
import org.apache.log4j.Logger;
import ra.lrrr.clustering.container.PDBAtomWithOriginID;
import ra.raMa.RaMa;

/**
 * takes a simple clustering and regenerates an optimal 
 * mittelpunkt for the cluster
 * 
 * PRO:
 * - better than only k-means
 * - easier than hirarchical clustering
 * 
 * CON:
 * - for two equally dense cluster (the old mittelpunkt is in the
 * middle of the two clusters) it does not work cool.
 * 
 * => only works good for one cluster....
 * 
 * 
 * @author ra
 *
 */
public class Clustering implements IClustering {

    private static org.apache.log4j.Logger log = Logger.getLogger(Clustering.class);

    ArrayList<PDBAtomWithOriginID> allAtoms = new ArrayList<PDBAtomWithOriginID>();

    ArrayList<PDBAtomWithOriginID> allAminoSpecificAtoms = new ArrayList<PDBAtomWithOriginID>();

    ArrayList<PDBAtomWithOriginID> refinedGoodCluster = new ArrayList<PDBAtomWithOriginID>();

    /** iteratins for ext k-means clustering... */
    String aminoAcidToClusterFor = "";

    double MAXIMUM_DISTANCE_CLUSTER = 8.00;

    public Clustering(ArrayList<PDBAtomWithOriginID> allAtoms) {
        this.allAtoms = allAtoms;
    }

    public void computeClusterFor(String aminoAcidToClusterFor) {
        double diameterBindingSite = ClusteringToolsPDBAtomWithOriginID.getMaximumDistance(this.allAtoms);
        this.aminoAcidToClusterFor = aminoAcidToClusterFor;
        this.allAminoSpecificAtoms = ClusteringToolsPDBAtomWithOriginID.getArrayListWithAllAtomsPartOf(this.aminoAcidToClusterFor, this.allAtoms);
        this.refinedGoodCluster = this.allAminoSpecificAtoms;
        double maximumDistanceThisCluster;
        while ((maximumDistanceThisCluster = ClusteringToolsPDBAtomWithOriginID.getMaximumDistance(this.refinedGoodCluster)) > (MAXIMUM_DISTANCE_CLUSTER)) {
            Point3d mittelpunkt = ClusteringToolsPDBAtomWithOriginID.generateMittelvektorOfPunktWolke(this.refinedGoodCluster);
            ArrayList<PDBAtomWithOriginID> tempRefinedGoodCluster = new ArrayList<PDBAtomWithOriginID>();
            maximumDistanceThisCluster /= 2;
            for (PDBAtomWithOriginID pdbAtomWithOriginID : this.allAminoSpecificAtoms) {
                double distance = RaMa.getEuclidicDistance(pdbAtomWithOriginID.getPDBAtom().getPoint3d(), mittelpunkt);
                if (distance < maximumDistanceThisCluster) {
                    tempRefinedGoodCluster.add(pdbAtomWithOriginID);
                }
            }
            this.refinedGoodCluster = tempRefinedGoodCluster;
        }
    }

    public Point3d getCenterOfCluster() {
        return ClusteringToolsPDBAtomWithOriginID.generateMittelvektorOfPunktWolke(this.refinedGoodCluster);
    }

    /**
	 * the quality of a cluster is a value from 1 (best) to 0 (worst)
	 * 
	 * it is dependent upon:
	 * 
	 * 
	 * 
	 * 
	 */
    public double getClusterQuality() {
        double allRelevantAmiosOverall = this.allAminoSpecificAtoms.size();
        double aminosInCluster = this.refinedGoodCluster.size();
        double returnVal = aminosInCluster / allRelevantAmiosOverall;
        double ratioPArtiticpationPDBIdsToAllPDBIds = ((double) ClusteringToolsPDBAtomWithOriginID.getUniquePDBIsFrom(this.refinedGoodCluster).size() / (double) ClusteringToolsPDBAtomWithOriginID.getUniquePDBIsFrom(this.allAtoms).size());
        returnVal *= ratioPArtiticpationPDBIdsToAllPDBIds;
        if (returnVal == 0) {
            return 0;
        } else return returnVal;
    }

    /**
	 * @return the aminoAcidToClusterFor
	 */
    public String getAminoAcidToClusterFor() {
        return aminoAcidToClusterFor;
    }

    /**
	 * @return the refinedGoodCluster
	 */
    public ArrayList<PDBAtomWithOriginID> getRefinedGoodCluster() {
        return refinedGoodCluster;
    }

    /**
	 * @return the allAtoms
	 */
    public ArrayList<PDBAtomWithOriginID> getAllAtoms() {
        return allAtoms;
    }

    /**
	 * @param refinedGoodCluster the refinedGoodCluster to set
	 */
    public void setRefinedGoodCluster(ArrayList<PDBAtomWithOriginID> refinedGoodCluster) {
        this.refinedGoodCluster = refinedGoodCluster;
    }

    /**
	 * @return the allAminoSpecificAtoms
	 */
    public ArrayList<PDBAtomWithOriginID> getAllAminoSpecificAtoms() {
        return allAminoSpecificAtoms;
    }

    public Point3d getMittelPunktOfRefinedCluster() {
        return ClusteringToolsPDBAtomWithOriginID.generateMittelvektorOfPunktWolke(this.refinedGoodCluster);
    }

    private double getClusterToAllAtomsRatio() {
        double allAtomsDistance = ClusteringToolsPDBAtomWithOriginID.getMaximumDistance(this.allAtoms);
        double clusterAtomsDistance = ClusteringToolsPDBAtomWithOriginID.getMaximumDistance(this.refinedGoodCluster);
        return (clusterAtomsDistance / allAtomsDistance);
    }
}

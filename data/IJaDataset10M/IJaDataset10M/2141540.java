package domain.structure_new.attributes.ramet_attributes;

import java.io.Serializable;

/**
 * This class contains the resources to be partiioned to the plant organs, i.e.
 * instances of FlowerWithMeris, Leaf, Internode, ClonalGrowthOrgran,
 * CoarseRoots, FineRoots class.
 * 
 * <p> Because the state of PartitioningPattern objects 
 * does change frequently, the class is organized as a 
 * mutable value object and its field variables 
 * are not encapsulated. Usage of a mutable object
 * is also preferable, since the PartitioningPatternService
 * computes the field variables of the PartitioningPattern
 * consecutively.
 * 
 * @author Uwe Grueters, email: uwe.grueters@bot2.bio.uni-giessen.de
 * @author Roland Dahlem, email: roland.dahlem@mni.fh-giessen.de
 */
public class PartitioningPattern implements Serializable {

    private static final long serialVersionUID = 1L;

    public Resources leavesResources;

    public Resources internodesResources;

    public Resources clonalGrowthOrganResources;

    public Resources fineRootsResources;

    public Resources coarseRootsResources;

    public Resources flowersResources;

    /**
	 * Default constructor: assigns all organ-specific
	 * resources to zero
	 */
    public PartitioningPattern() {
        leavesResources = new Resources();
        internodesResources = new Resources();
        clonalGrowthOrganResources = new Resources();
        fineRootsResources = new Resources();
        coarseRootsResources = new Resources();
        flowersResources = new Resources();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null | obj.getClass() != this.getClass()) {
            return false;
        }
        PartitioningPattern pp = (PartitioningPattern) obj;
        if (pp.leavesResources.equals(this.leavesResources) && pp.internodesResources.equals(this.internodesResources) && pp.clonalGrowthOrganResources.equals(this.clonalGrowthOrganResources) && pp.fineRootsResources.equals(this.fineRootsResources) && pp.coarseRootsResources.equals(this.coarseRootsResources) && pp.flowersResources.equals(this.flowersResources)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return detailedView();
    }

    /**
	 * This private method is used in the toString() method.
	 * 
	 * @return detailedView
	 */
    private String detailedView() {
        return "Partition:" + "\tleaves: " + printResources(leavesResources) + "\tinternodes: " + printResources(internodesResources) + "\tcgo: " + printResources(clonalGrowthOrganResources) + "\tfine roots: " + printResources(fineRootsResources) + "\tcoarse roots: " + printResources(coarseRootsResources) + "\flowers: " + printResources(flowersResources);
    }

    /**
	 * This private method is used in the toString() method and returns the resources as
	 * a String.
	 * 
	 * @param res
	 * @return the resources
	 */
    private String printResources(Resources res) {
        return "\tC: " + res.carbon_g + "\tN: " + res.nitrogen_g;
    }
}

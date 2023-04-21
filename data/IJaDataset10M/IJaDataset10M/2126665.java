package mil.army.usace.ehlschlaeger.rgik.core;

/**
 *  Copyright Charles R. Ehlschlaeger,
 *  work: 309-298-1841, fax: 309-298-3003,
 *	<http://faculty.wiu.edu/CR-Ehlschlaeger2/>
 *  This software is freely usable for research and educational purposes. Contact C. R. Ehlschlaeger
 *  for permission for other purposes.
 *  Use of this software requires appropriate citation in all published and unpublished documentation.
 */
public abstract class LatticeErrorDistributionModel extends ErrorDistributionModel {

    private GISLattice genMap;

    private LatticeRandomFieldTemplate templet;

    public LatticeErrorDistributionModel() {
        super();
    }

    public void setRandomFieldTemplate(LatticeRandomFieldTemplate templet) {
        this.templet = templet;
    }

    public LatticeRandomFieldTemplate getRandomFieldTemplate() {
        return templet;
    }

    public void setApplicationGrid(GISLattice applicationGrid) {
        setApplicationArea((GISData) applicationGrid);
    }

    public void setGeneralizedMap(GISLattice generalizedMap) {
        genMap = generalizedMap;
    }

    public GISLattice getGeneralizedMap() {
        return genMap;
    }

    public GISGrid getApplicationGrid() {
        return ((GISLattice) getApplicationArea());
    }
}

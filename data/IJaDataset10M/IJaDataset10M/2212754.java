package toxTree.query;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.isomorphism.matchers.IQueryBond;

/**
 * TODO add description
 * @author ThinClient
 * <b>Modified</b> 2005-9-23
 */
public class AssociationQueryBond extends MyAssociationBond implements IQueryBond {

    private static final long serialVersionUID = 2683414374611772988L;

    /**
	 * @param atom1
	 * @param atom2
	 */
    public AssociationQueryBond(IAtom atom1, IAtom atom2) {
        super(atom1, atom2);
    }

    /**
	 * 
	 */
    public AssociationQueryBond() {
        super();
    }

    public boolean matches(IBond bond) {
        return bond instanceof MyAssociationBond;
    }
}

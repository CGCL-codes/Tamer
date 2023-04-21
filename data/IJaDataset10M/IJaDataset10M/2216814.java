package ca.ucalgary.ebe.j3dperfunit.locator;

import javax.media.j3d.Leaf;

/**
 * Use this class to search a Leaf object
 * @author shu
 *
 */
public class LeafLocator extends NodeLocator {

    /**
	 * 
	 *
	 */
    public LeafLocator() {
    }

    /**
	 * 
	 * @param identifier name of a Leaf object
	 */
    public LeafLocator(Object identifier) {
        super(identifier, false);
    }

    /**
	 * 
	 * @param identifier name of a Leaf object
	 * @param caseIndependent
	 */
    public LeafLocator(Object identifier, final boolean caseIndependent) {
        super(identifier, caseIndependent);
    }

    @Override
    public boolean testComponent(Object comp) {
        return ((comp != null) && isValidForProcessing(comp, Leaf.class) && checkIfIdentifierMatch(((Leaf) comp).getName()));
    }
}

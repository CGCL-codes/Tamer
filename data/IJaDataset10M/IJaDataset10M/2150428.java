package verhaar.rules;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IRingSet;
import toxTree.exceptions.DecisionMethodException;
import toxTree.query.MolFlags;
import verhaar.query.FunctionalGroups;
import verhaar.rules.helper.RuleOnlyAllowedSubstructuresCounter;

/**
 * Monocyclic compounds substituted with halogens.
 * @author Nina Jeliazkova jeliazkova.nina@gmail.com
 * <b>Modified</b> July 12, 2011
 */
public class Rule142 extends RuleOnlyAllowedSubstructuresCounter {

    protected static transient String[] halogens = { "Cl", "Br", "F", "I" };

    /**
	 * 
	 */
    private static final long serialVersionUID = 8731063548318963732L;

    public Rule142() {
        super();
        id = "1.4.2";
        setTitle("Be monocyclic compounds substituted with halogens");
        examples[1] = "c1ccccc1Cl";
        examples[0] = "c1ccc(cc1)c2ccc(cc2)Cl";
        editable = false;
        for (int i = 0; i < halogens.length; i++) addSubstructure(FunctionalGroups.ringSubstituted(halogens[i]));
        addSubstructure(FunctionalGroups.ringSubstituted(null));
    }

    protected IRingSet hasRingsToProcess(IAtomContainer mol) throws DecisionMethodException {
        MolFlags mf = (MolFlags) mol.getProperty(MolFlags.MOLFLAGS);
        if (mf == null) throw new DecisionMethodException(ERR_STRUCTURENOTPREPROCESSED);
        IRingSet rings = mf.getRingset();
        if ((rings == null)) {
            logger.debug("Acyclic structure");
            return null;
        } else if (rings.getAtomContainerCount() == 1) {
            logger.debug("Monocyclic\tYES");
            return rings;
        } else {
            logger.debug("More than one ring\t", rings.getAtomContainerCount());
            return null;
        }
    }

    public boolean isImplemented() {
        return true;
    }

    public boolean verifyRule(IAtomContainer mol) throws DecisionMethodException {
        logger.info(toString());
        if (hasRingsToProcess(mol) != null) return super.verifyRule(mol); else return false;
    }
}

package toxTree.tree.cramer;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IRing;
import org.openscience.cdk.interfaces.IRingSet;
import toxTree.exceptions.DecisionMethodException;
import toxTree.query.FunctionalGroups;
import toxTree.query.MolAnalyser;
import toxTree.query.MolFlags;
import toxTree.tree.rules.RuleRingAllowedSubstituents;

/**
 * Rule 24 of the Cramer scheme (see {@link toxTree.tree.cramer.CramerRules})
 * @author Nina Jeliazkova <br>
 * @version 0.1, 2005-5-2
 */
public class RuleMonocarbocyclic extends RuleRingAllowedSubstituents {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -625423309210659129L;

    /**
	 * Constructor
	 * 
	 */
    public RuleMonocarbocyclic() {
        super();
        addSubstructure(FunctionalGroups.alcohol(true));
        addSubstructure(FunctionalGroups.aldehyde());
        addSubstructure(FunctionalGroups.acyclic_acetal());
        addSubstructure(FunctionalGroups.sidechain_ketone());
        addSubstructure(FunctionalGroups.carboxylicAcid());
        addSubstructure(FunctionalGroups.ester());
        addSubstructure(FunctionalGroups.sulphonate(new String[] { "Na", "K", "Ca" }));
        addSubstructure(FunctionalGroups.sulphonate(null, false));
        addSubstructure(FunctionalGroups.sulphamate(new String[] { "Na", "K", "Ca" }));
        addSubstructure(FunctionalGroups.sulphamate(null));
        id = "24";
        title = "Monocarbocyclic with simple substituents";
        explanation.append("<html>");
        explanation.append("Is the substance monocarbocyclic (excluding cyclopropane or cyclobutane and their derivatives) with ring or <i>aliphatic</i> (A) side chains,");
        explanation.append("unsubstituted or containing only alcohol, aldehyde, side-chain ketone, acid, ester, or Na, K or Ca sulphonate or sulphamate, or acyclic acetal or ketal?");
        explanation.append("/<html>");
        examples[0] = "O=C1CCCCC1";
        examples[1] = "CC(=O)C1CCCCC1";
        editable = false;
    }

    @Override
    public boolean isImplemented() {
        return true;
    }

    @Override
    protected IRingSet hasRingsToProcess(IAtomContainer mol) throws DecisionMethodException {
        logger.info(toString());
        MolFlags mf = (MolFlags) mol.getProperty(MolFlags.MOLFLAGS);
        if (mf == null) throw new DecisionMethodException(ERR_STRUCTURENOTPREPROCESSED);
        IRingSet rings = mf.getRingset();
        if (rings == null) return null;
        if (rings.getAtomContainerCount() > 1) {
            logger.info("Not monocarbocyclic, >1 rings found", rings.getAtomContainerCount());
            return null;
        }
        IRing r = (IRing) rings.getAtomContainer(0);
        if (r.getAtomCount() < 5) return null;
        Object o = r.getProperty(MolAnalyser.HETEROCYCLIC);
        if ((o != null) && ((Boolean) o).booleanValue()) {
            logger.info("Heteroring found");
            return null;
        } else {
            logger.info("Monocarbocyclic ring found");
            return rings;
        }
    }

    @Override
    public boolean verifyRule(IAtomContainer mol) throws DecisionMethodException {
        return verifyRule(mol, null);
    }

    @Override
    public boolean verifyRule(IAtomContainer molecule, IAtomContainer selected) throws DecisionMethodException {
        Object o = molecule.getProperty(MolFlags.PARENT);
        IAtomContainer mol = molecule;
        if ((o != null) && (molecule instanceof IAtomContainer)) {
            logger.debug("Parent compound found, will continue analyzing the parent");
            mol = (IAtomContainer) o;
        }
        return super.verifyRule(mol, selected);
    }
}

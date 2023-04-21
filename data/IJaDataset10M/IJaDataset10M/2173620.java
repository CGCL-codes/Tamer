package mic.rules;

import java.util.List;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import toxTree.exceptions.DecisionMethodException;
import toxTree.query.FunctionalGroups;
import toxTree.tree.rules.StructureAlertCDK;
import ambit2.core.data.MoleculeTools;
import ambit2.smarts.query.SMARTSException;

public class SA10 extends StructureAlertCDK {

    /**
	 * 
	 */
    private static final long serialVersionUID = 610255293661685341L;

    protected static String AB_UNSATURATED_CARBONYLS = "α,β unsaturated carbonyls";

    protected static String AB_UNSATURATED_CARBONYLS_SMARTS = "[!a,#1;!$(C1(=O)C=CC(=O)C=C1)][#6]([!a,#1;!$(C1(=O)C=CC(=O)C=C1)])!:;=[#6][#6](=O)[!O;!$([#6]1:,=[#6][#6](=O)[#6]:,=[#6][#6](=O)1)]";

    protected QueryAtomContainer query = FunctionalGroups.ab_unsaturated_carbonyl();

    public SA10() throws SMARTSException {
        setContainsAllSubstructures(true);
        addSubstructure(AB_UNSATURATED_CARBONYLS, AB_UNSATURATED_CARBONYLS_SMARTS);
        setID("SA10");
        setTitle(AB_UNSATURATED_CARBONYLS);
        StringBuffer b = new StringBuffer();
        b.append(AB_UNSATURATED_CARBONYLS);
        b.append(". Exclude :");
        b.append("<ul>");
        b.append("<li>");
        b.append("α,β unsaturated carboxylic acid and carboxylate");
        b.append("<li>");
        b.append("Quinones");
        b.append("<li>");
        b.append("Acyclic (linear) chemicals with the β carbon with substituents with C >= 6, or aromatic ring");
        b.append("</ul>");
        setExplanation(b.toString());
        examples[0] = "[H]OC(=O)C=C";
        examples[1] = "CCCC(=O)C=C";
    }

    @Override
    public boolean verifyRule(IAtomContainer mol) throws DecisionMethodException {
        return ((!CH6SubstituentAtBetaCarbon(mol)) && super.verifyRule(mol));
    }

    public IMoleculeSet detachSubstituentAtBetaCarbon(IAtomContainer c) {
        List<?> map = FunctionalGroups.getBondMap(c, query, false);
        FunctionalGroups.markMaps(c, query, map);
        if (map == null) return null;
        return FunctionalGroups.detachGroup(c, query);
    }

    public boolean CH6SubstituentAtBetaCarbon(IAtomContainer c) {
        try {
            IAtomContainer cc = (IAtomContainer) c.clone();
            IMoleculeSet sc = detachSubstituentAtBetaCarbon(cc);
            if (sc != null) {
                for (int i = 0; i < sc.getAtomContainerCount(); i++) {
                    IAtomContainer a = sc.getAtomContainer(i);
                    int ringAtoms = 0;
                    int aromaticAtoms = 0;
                    for (int j = 0; j < a.getAtomCount(); j++) {
                        if (a.getAtom(j).getFlag(CDKConstants.ISINRING)) ringAtoms++;
                        if (a.getAtom(j).getFlag(CDKConstants.ISAROMATIC)) aromaticAtoms++;
                    }
                    if (ringAtoms > 0) continue;
                    if (FunctionalGroups.hasGroupMarked(sc.getAtomContainer(i), query.getID())) continue; else {
                        IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(sc.getAtomContainer(i));
                        int catoms = MolecularFormulaManipulator.getElementCount(formula, MoleculeTools.newElement(formula.getBuilder(), "C"));
                        if (catoms >= 6) {
                            logger.debug("Substituent at beta carbon with >=6 C atoms\t", catoms);
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception x) {
            logger.error(x);
            return false;
        }
    }
}

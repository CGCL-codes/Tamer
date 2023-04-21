package mutant.rules;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import toxTree.exceptions.DecisionMethodException;
import toxTree.tree.rules.StructureAlertCDK;
import ambit2.core.data.MoleculeTools;
import ambit2.smarts.query.SMARTSException;

/**
 * Aromatic mono- and dialkylamine (with exceptions).
 * TODO SO3H subrule should be implemented
 * @author Nina Jeliazkova
 *
 */
public class SA28bis_gen extends StructureAlertCDK {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3084959606481108406L;

    public static String SA28bis_title = "Aromatic mono- and dialkylamine";

    public static String[][] amines = { { SA28bis_title, "[NX3;v3]([#1,CH3])([CH3])" }, { SA28bis_title, "[NX3;v3]([#1,CH3])([CH2][CH3])" }, { SA28bis_title, "[NX3;v3]([CH2][CH3])([CH2][CH3])" } };

    public SA28bis_gen() throws SMARTSException {
        super();
        setContainsAllSubstructures(false);
        StringBuffer b = new StringBuffer();
        b.append("[a");
        for (int i = 0; i < SA28_gen.exclusion_rules.length; i++) {
            b.append(";!$(");
            b.append(SA28_gen.exclusion_rules[i][1]);
            b.append(")");
        }
        b.append("]!@[");
        for (int i = 0; i < amines.length; i++) {
            if (i > 0) b.append(',');
            b.append("$(");
            b.append(amines[i][1]);
            b.append(")");
        }
        b.append("]");
        addSubstructure(SA28bis_title, b.toString());
        setID("SA28bis_gen");
        setTitle(SA28bis_title);
        StringBuffer e = new StringBuffer();
        e.append("<html>");
        e.append("Mono- or di- methyl or ethyl aromatic amines, are included.However:");
        e.append("<ul>");
        e.append("<li>");
        e.append("Aromatic amino groups with ortho-disubstitution or with a carboxylic acid substituent in ortho position should be excluded.");
        e.append("<li>");
        e.append("If a sulfonic acid group (-SO3H) is present on the ring that contains also the amino group, the substance should be excluded from the alert.");
        e.append("<li>");
        e.append("see also the two examples of exceptions for the nitro alert (alert 27))");
        e.append("</ul>");
        e.append("</html>");
        setExplanation(e.toString());
        examples[0] = "CNC(=O)OC=1C=C(C)C(=C(C)C=1)N(C)C";
        examples[1] = "CN(C)C=1C=CC=CC=1";
        editable = false;
    }

    protected boolean isAPossibleHit(IAtomContainer mol, IAtomContainer processedObject) throws DecisionMethodException {
        IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(mol);
        return MolecularFormulaManipulator.containsElement(formula, MoleculeTools.newElement(formula.getBuilder(), "N"));
    }
}

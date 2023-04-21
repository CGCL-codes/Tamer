package eye;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.DescriptorSpecification;
import toxTree.core.IDecisionInteractive;
import toxTree.core.IDecisionResult;
import toxTree.exceptions.DecisionMethodException;
import toxTree.exceptions.DecisionResultException;
import toxTree.tree.CategoriesList;
import toxTree.tree.DecisionNodesFactory;
import toxTree.tree.UserDefinedTree;

/**
 * Decision tree for estimating skin irritation and corrosion potential.
 * Implements rules publlished in 
 * <i>?????????</i>. <br>
 * <p> Rules: 
 * <ul>
 * <li>physicochemical property limits
 * <li>structural rules 
 * <li>implementation in {@link eye.rules} package
 * </ul>
 * <p>Categories:
 * <ul>
 * <li>{@link eye.categories.CategoryNotCorrosive2Skin}
 * <li>{@link eye.categories.CategoryNotIrritatingOrCorrosive2Eye}
 * <li>{@link eye.categories.CategoryNotIrritating2Eye}
 * <li>{@link eye.categories.CategoryIrritating}
 * <li>{@link eye.categories.CategoryCorrosiveEye}
 * <li>{@link eye.categories.CategoryIrritatingOrCorrosive}
 * <li>{@link eye.categories.CategoryUnknown} 
 * </ul>
 * 
 * @author Nina Jeliazkova 
 * @author Vania Tsakovska
 * @version 0.1, 2008-3-31
 */
public class EyeIrritationRules extends UserDefinedTree implements IDecisionInteractive {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 0;

    protected boolean residuesIDVisible;

    protected boolean interactive = true;

    public static final transient String[] c_rules = { "eye.rules.Rule1", "eye.rules.RuleLogP", "eye.rules.RuleLogP1", "eye.rules.RuleLipidSolubility", "eye.rules.RuleAqueousSolubility1", "eye.rules.RuleAqueousSolubility", "eye.rules.RuleMolWeight", "eye.rules.RuleHasOnlyC_H_O", "eye.rules.RuleMeltingPoint_8_1", "eye.rules.RuleMolWeight_8_2", "eye.rules.RuleAqueousSolubility_8_3", "eye.rules.RuleAqueousSolubility_8_4", "eye.rules.RuleHasOnlyC_H_O_N", "eye.rules.RuleLipidSolubility_9_1", "eye.rules.RuleMolWeight_9_2", "eye.rules.RuleAqueousSolubility_9_3", "eye.rules.RuleLogP_9_4", "eye.rules.RuleHasOnlyC_H_O_N_Halogen", "eye.rules.RuleLogP_10_1", "eye.rules.RuleAqueousSolubility_10_5", "eye.rules.RuleMolWeight_10_3", "eye.rules.RuleLipidSolubility_10_4", "eye.rules.RuleAqueousSolubility_10_2", "eye.rules.RuleHasOnlyC_H_O_N_S", "eye.rules.RuleMolWeight_11_1", "eye.rules.RuleMeltingPoint_11_2", "eye.rules.RuleMelting_Point_11_3", "eye.rules.RuleLogP_11_4", "eye.rules.RuleLogP_11_5", "eye.rules.RuleLogP_11_6", "eye.rules.RuleAqueousSolubility_11_7", "eye.rules.RuleHasOnlyC_H_O_Halogen", "eye.rules.RuleMolweight_12_1", "eye.rules.RuleMolWeight_12_2", "eye.rules.RuleMeltingPoint_12_3", "eye.rules.RuleLogP_12_4", "eye.rules.Rule13_AliphaticMonoalcohols", "eye.rules.Rule14Aliphatic_glycerol_monoethers", "eye.rules.Rule15", "eye.rules.Rule16", "eye.rules.Rule17", "eye.rules.Rule18", "eye.rules.Rule19", "eye.rules.Rule20", "eye.rules.Rule21", "eye.rules.Rule22", "eye.rules.Rule23", "eye.rules.Rule24", "eye.rules.Rule25", "eye.rules.Rule26", "eye.rules.Rule27", "eye.rules.Rule28", "eye.rules.Rule29", "eye.rules.Rule30", "eye.rules.Rule31", "eye.rules.Rule32", "eye.rules.Rule33", "eye.rules.Rule34", "eye.rules.Rule35", "eye.rules.Rule36", "eye.rules.Rule37", "eye.rules.Rule38", "eye.rules.Rule39" };

    private static final transient int c_transitions[][] = { { 2, 0, 0, 1 }, { 3, 0, 0, 2 }, { 4, 0, 0, 1 }, { 5, 0, 0, 1 }, { 6, 0, 0, 4 }, { 7, 0, 0, 3 }, { 8, 0, 0, 4 }, { 13, 9, 0, 0 }, { 10, 0, 0, 1 }, { 11, 0, 0, 2 }, { 12, 0, 0, 7 }, { 13, 0, 0, 2 }, { 18, 14, 0, 0 }, { 15, 0, 0, 1 }, { 16, 0, 0, 1 }, { 17, 0, 0, 1 }, { 18, 0, 0, 1 }, { 24, 19, 0, 0 }, { 20, 0, 0, 5 }, { 21, 0, 0, 3 }, { 22, 0, 0, 1 }, { 23, 0, 0, 1 }, { 24, 0, 0, 1 }, { 32, 25, 0, 0 }, { 26, 0, 0, 6 }, { 27, 0, 0, 4 }, { 28, 0, 0, 1 }, { 29, 0, 0, 1 }, { 30, 0, 0, 3 }, { 31, 0, 0, 4 }, { 32, 0, 0, 7 }, { 37, 33, 0, 0 }, { 34, 0, 0, 6 }, { 35, 0, 0, 1 }, { 36, 0, 0, 1 }, { 37, 0, 0, 5 }, { 38, 0, 0, 8 }, { 39, 0, 0, 8 }, { 40, 0, 0, 8 }, { 41, 0, 0, 8 }, { 42, 0, 0, 8 }, { 43, 0, 0, 8 }, { 44, 0, 0, 8 }, { 45, 0, 0, 8 }, { 46, 0, 0, 8 }, { 47, 0, 0, 8 }, { 48, 0, 0, 8 }, { 49, 0, 0, 8 }, { 50, 0, 0, 8 }, { 51, 0, 0, 8 }, { 52, 0, 0, 8 }, { 53, 0, 0, 8 }, { 54, 0, 0, 8 }, { 55, 0, 0, 9 }, { 56, 0, 0, 9 }, { 57, 0, 0, 9 }, { 58, 0, 0, 9 }, { 59, 0, 0, 10 }, { 60, 0, 0, 10 }, { 61, 0, 0, 10 }, { 62, 0, 0, 10 }, { 63, 0, 0, 10 }, { 0, 0, 11, 10 } };

    private static final transient String c_categories[] = { "eye.categories.CategoryNotCorrosive2Skin", "eye.categories.CategoryNotCorrosive2SkinAndIrritating2Eye", "eye.categories.CategoryNotCorrosive2Eye", "eye.categories.CategoryNotIrritating2Eye", "eye.categories.CategoryNotCorrosive2SkinEye", "eye.categories.CategoryNotCorrosive2SkinOrIrritating2Eye", "eye.categories.CategoryNotIrritatingOrCorrosive2Eye", "eye.categories.CategoryCorrosiveEye", "eye.categories.CategoryModerateIrritation2Eye", "eye.categories.CategoryCorrosiveSkin", "eye.categories.CategoryUnknown" };

    /**
	 * 
	 */
    public EyeIrritationRules() throws DecisionMethodException {
        super(new CategoriesList(c_categories, true), c_rules, c_transitions, new DecisionNodesFactory(true));
        setChanged();
        notifyObservers();
        setTitle("Eye irritation and corrosion");
        setPriority(8);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        if (changes == null) changes = new PropertyChangeSupport(this);
        changes.addPropertyChangeListener(l);
        for (int i = 0; i < rules.size(); i++) if (rules.getRule(i) != null) rules.getRule(i).addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        if (changes == null) {
            changes.removePropertyChangeListener(l);
            for (int i = 0; i < rules.size(); i++) if (rules.getRule(i) != null) rules.getRule(i).removePropertyChangeListener(l);
        }
    }

    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

    public StringBuffer explainRules(IDecisionResult result, boolean verbose) throws DecisionMethodException {
        try {
            StringBuffer b = result.explain(verbose);
            return b;
        } catch (DecisionResultException x) {
            throw new DecisionMethodException(x);
        }
    }

    public boolean isResiduesIDVisible() {
        return residuesIDVisible;
    }

    public void setResiduesIDVisible(boolean residuesIDVisible) {
        this.residuesIDVisible = residuesIDVisible;
        for (int i = 0; i < rules.size(); i++) {
            rules.getRule(i).hideResiduesID(!residuesIDVisible);
        }
    }

    public void setEditable(boolean value) {
        editable = value;
        for (int i = 0; i < rules.size(); i++) rules.getRule(i).setEditable(value);
    }

    @Override
    public void setParameters(IAtomContainer mol) {
        if (interactive) {
            JComponent c = optionsPanel(mol);
            if (c != null) JOptionPane.showMessageDialog(null, c, "Enter properties", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public boolean getInteractive() {
        return interactive;
    }

    public void setInteractive(boolean value) {
        interactive = value;
    }

    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification("http://toxtree.sourceforge.net/eye.html", getTitle(), this.getClass().getName(), "Toxtree plugin");
    }
}

package eye.rules;

/**
 * Aqueous solubility [g/l] < 2.0E-5 Expects property to be read from IMolecule.getProperty({@link #AqueousSolubility}).
 * @author Nina Jeliazkova
 * @author ania Tsakovska
 */
public class RuleAqueousSolubility extends sicret.rules.RuleAqueousSolubility {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5545033439943037006L;

    public RuleAqueousSolubility() {
        this(AqueousSolubility, "g/l", condition_lower, 2.0E-5);
    }

    public RuleAqueousSolubility(String propertyName, String units, String condition, double value) {
        super(propertyName, units, condition, value);
        setID("6");
        setTitle(getPropertyName() + getCondition() + getProperty());
        examples[0] = "c1([C@@](COC(N)=O)(CC)O)ccccc1";
        examples[1] = "C(c1ccc(Cl)cc1)(c1ccc(Cl)cc1)C(Cl)(Cl)Cl";
    }

    public javax.swing.JComponent optionsPanel(org.openscience.cdk.interfaces.IAtomContainer atomContainer) {
        return null;
    }

    ;
}

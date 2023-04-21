package sicret.rules;

/**
 * LipidSolubility < 4.0
 * @author Nina Jeliazkova
 *
 */
public class Rule22 extends RuleLipidSolubility {

    private static final long serialVersionUID = 0;

    public Rule22() {
        super();
        id = "25";
        title = "LipidSolubility < 4.0";
        propertyStaticValue = 4.0;
        condition = condition_lower;
        propertyName = LipidSolubility;
    }

    public javax.swing.JComponent optionsPanel(org.openscience.cdk.interfaces.IAtomContainer atomContainer) {
        return null;
    }

    ;
}

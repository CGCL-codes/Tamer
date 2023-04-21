package verhaar.rules;

import toxTree.tree.rules.DefaultAlertCounter;
import toxTree.tree.rules.IAlertCounter;
import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;

/**
 * 
 * Possess a three-membered heterocyclic ring. Compounds containing an epoxide or azaridine function.
 * @author Nina Jeliazkova jeliazkova.nina@gmail.com
 * <b>Modified</b> July 12, 2011
 */
public class Rule34 extends RuleSMARTSSubstructureAmbit {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9151362364966315658L;

    protected IAlertCounter alertsCounter;

    protected String[][] smarts = { { "epoxide", "C1OC1" }, { "aziridine", "C1NC1" }, { "3-membered heterocyclic ring", "[r3;!$([#6])]" } };

    public Rule34() {
        super();
        id = "3.4";
        setTitle("Possess a three-membered heterocyclic ring. Compounds containing an epoxide or azaridine function");
        explanation = new StringBuffer();
        editable = false;
        alertsCounter = new DefaultAlertCounter();
        explanation.append("<html><ul>");
        for (String[] smart : smarts) try {
            addSubstructure(smart[0], smart[1]);
            explanation.append(String.format("<li>%s SMARTS: %s", smart[0], smart[1]));
        } catch (Exception x) {
            x.printStackTrace();
        }
        explanation.append("</ul></html>");
        setExamples(new String[] { "C1CC1", "C1CN1CCC" });
    }
}

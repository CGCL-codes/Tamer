package toxtree.plugins.moa.rules;

import toxTree.tree.rules.RuleVerifyAlertsCounter;

public class VerifyAlertsNarcosis2 extends RuleVerifyAlertsCounter {

    /**
	 * 
	 */
    private static final long serialVersionUID = -424872251765946518L;

    public VerifyAlertsNarcosis2() {
        super();
        setID("Narcosis2 alert?");
        setTitle("At least one alert for Narcosis2 fired?");
    }
}

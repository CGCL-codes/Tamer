package verhaar.rules;

import verhaar.rules.helper.RuleElementsCounter;

/**
 * 
 * Contain only C&H.
 * @author Nina Jeliazkova jeliazkova.nina@gmail.com
 * <b>Modified</b> July 12, 2011
 */
public class Rule13 extends RuleElementsCounter {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3138693766571092656L;

    public Rule13() {
        super();
        setComparisonMode(modeAllSpecifiedElements);
        id = "1.3";
        setTitle("Contain only C&H");
        examples[0] = "C=O";
        examples[1] = "CCCC";
        addElement("C");
        addElement("H");
        setExplanation("Contain only C and H");
        editable = false;
    }
}

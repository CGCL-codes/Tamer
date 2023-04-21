package org.dbe.businessModeller.vocabulary.enterprise;

public class Rule extends RuleSet {

    /**
	 * @uml.property  name="name"
	 */
    private int Name;

    private int Description;

    private String text;

    public Rule(String newText) {
        text = newText;
    }

    /**
	 * Title getter
	 * @return title
	 */
    public String getText() {
        return text;
    }
}

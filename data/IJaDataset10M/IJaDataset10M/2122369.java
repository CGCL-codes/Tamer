package minire;

/**
 * Represents a rule.
 * Contains the name of the rule and all production rules,
 * represented as an array of rules.
 * Can also be a terminal.
 * @author Owen Cox
 */
public class Rule {

    private String name;

    private boolean terminal, start;

    private Rule[][] rules;

    /**
	 * Creates an empty non terminal rule.
	 * @param name
	 */
    public Rule(String name) {
        this.name = name;
        terminal = false;
        start = false;
    }

    /**
	 * Set whether or not the rule is a terminal.
	 * @param t
	 */
    public void setTerminal(boolean t) {
        this.terminal = t;
    }

    /**
	 * Set whether or not this rule is the starting rule.
	 * @param t
	 */
    public void setStart(boolean t) {
        this.start = t;
    }

    /**
	 * @return whether or not this rule is a terminal.
	 */
    public boolean isTerminal() {
        return terminal;
    }

    /**
	 * @return all the production rules.
	 */
    public Rule[][] getRules() {
        return rules;
    }

    /**
	 * Add a production rule.
	 * @param rule
	 */
    public void addProductionRule(Rule[] rule) {
        if (rules == null) {
            rules = new Rule[1][];
            rules[0] = rule;
        } else {
            Rule[][] newRules = new Rule[rules.length + 1][];
            for (int i = 0; i < rules.length; i++) {
                newRules[i] = rules[i];
            }
            newRules[rules.length] = rule;
            rules = newRules;
        }
    }

    /**
	 * @return the ruleName.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the rule's name.
	 * @param name
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return a string representation of the rule.
	 */
    public String toString() {
        String s = "";
        if (start) s += "[START] ";
        s += name + ":";
        if (terminal) {
            return s + " terminal\n";
        }
        s += "\n";
        for (Rule[] rs : rules) {
            for (Rule r : rs) {
                s += r.getName();
            }
            s += "\n";
        }
        return s;
    }

    /**
	 * @return whether or not this is the starting rule
	 */
    public boolean isStart() {
        return start;
    }
}

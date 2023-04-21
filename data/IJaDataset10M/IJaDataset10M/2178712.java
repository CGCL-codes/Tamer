package com.agentfactory.eclipse.editors;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class TeleoReactivePartitionScanner extends RuleBasedPartitionScanner {

    public static final String COMMENT = "__teleoReactive_comment";

    public TeleoReactivePartitionScanner() {
        IToken comment = new Token(COMMENT);
        IPredicateRule[] rules = new IPredicateRule[2];
        rules[0] = new MultiLineRule("/*", "*/", comment);
        rules[1] = new SingleLineRule("//", "\n", comment);
        setPredicateRules(rules);
    }
}

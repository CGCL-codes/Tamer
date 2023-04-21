package es.gavab.eclipse.pascalfc.editors;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class PascalFCPartitionScanner extends RuleBasedPartitionScanner {

    public static final String PAS_COMMENT = "__pas_comment";

    public PascalFCPartitionScanner() {
        IToken pasComment = new Token(PAS_COMMENT);
        PatternRule[] rules = new PatternRule[] { new MultiLineRule("(*", "*)", pasComment), new MultiLineRule("{", "}", pasComment) };
        setPredicateRules(rules);
    }
}

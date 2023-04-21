package net.sourceforge.sqlexplorer.sqleditor;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.sqlexplorer.IConstants;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

public class SQLPartitionScanner extends RuleBasedPartitionScanner {

    /**
	 * Detector for empty comments.
	 */
    static class EmptyCommentDetector implements IWordDetector {

        public boolean isWordStart(char c) {
            return (c == '/');
        }

        public boolean isWordPart(char c) {
            return (c == '*' || c == '/');
        }
    }

    ;

    static class EmptyCommentRule extends WordRule implements IPredicateRule {

        private IToken fSuccessToken;

        /**
		 * Constructor for EmptyCommentRule.
		 * @param defaultToken
		 */
        public EmptyCommentRule(IToken successToken) {
            super(new EmptyCommentDetector());
            fSuccessToken = successToken;
            addWord("/**/", fSuccessToken);
        }

        public IToken evaluate(org.eclipse.jface.text.rules.ICharacterScanner scanner, boolean resume) {
            return evaluate(scanner);
        }

        public IToken getSuccessToken() {
            return fSuccessToken;
        }
    }

    ;

    /**
	 * Creates the partitioner and sets up the appropriate rules.
	 */
    public SQLPartitionScanner() {
        super();
        IToken string = new Token(IConstants.SQL_STRING);
        IToken multiLineComment = new Token(IConstants.SQL_MULTILINE_COMMENT);
        IToken singleLineComment = new Token(IConstants.SQL_SINGLE_LINE_COMMENT);
        List rules = new ArrayList();
        rules.add(new EndOfLineRule("--", singleLineComment));
        rules.add(new SingleLineRule("'", "'", string));
        EmptyCommentRule wordRule = new EmptyCommentRule(multiLineComment);
        rules.add(wordRule);
        rules.add(new MultiLineRule("/*", "*/", multiLineComment));
        IPredicateRule[] result = new IPredicateRule[rules.size()];
        rules.toArray(result);
        setPredicateRules(result);
    }
}

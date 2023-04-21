package orcajo.azada.mdx.editors.mdx;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.rules.*;

public class MDXPartitionScanner extends RuleBasedPartitionScanner {

    public static final String MDX_COMMENT = "__mdx_comment";

    public static final String[] MDX_PARTITION_TYPES = new String[] { MDX_COMMENT };

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

    /**
	 *
	 */
    static class WordPredicateRule extends WordRule implements IPredicateRule {

        private IToken fSuccessToken;

        public WordPredicateRule(IToken successToken) {
            super(new EmptyCommentDetector());
            fSuccessToken = successToken;
            addWord("/**/", fSuccessToken);
        }

        public IToken evaluate(ICharacterScanner scanner, boolean resume) {
            return super.evaluate(scanner);
        }

        public IToken getSuccessToken() {
            return fSuccessToken;
        }
    }

    /**
	 * Creates the partitioner and sets up the appropriate rules.
	 */
    public MDXPartitionScanner() {
        super();
        IToken comment = new Token(MDX_COMMENT);
        List<IPredicateRule> rules = new ArrayList<IPredicateRule>();
        rules.add(new EndOfLineRule("//", comment));
        rules.add(new EndOfLineRule("--", comment));
        rules.add(new WordPredicateRule(comment));
        rules.add(new MultiLineRule("/*", "*/", comment, (char) 0, true));
        IPredicateRule[] result = new IPredicateRule[rules.size()];
        rules.toArray(result);
        setPredicateRules(result);
    }
}

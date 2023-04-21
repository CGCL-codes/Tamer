package weird;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

public class XMLPartitionScanner extends RuleBasedScanner {

    public static final int TYPE1 = 1;

    private int myGlobalOffset;

    private int myGlobalLength;

    public XMLPartitionScanner(String type) {
        assert (type != null && (type.length() > 0));
        IToken tag = new Token(type);
        IPredicateRule[] rules = new IPredicateRule[1];
        rules[0] = new TagRule(tag);
        setRules(rules);
    }

    public void setRange(IDocument document, int offset, int length) {
        if (true) {
            super.setRange(document, offset, length);
            return;
        }
    }

    public IToken nextToken() {
        IToken token = super.nextToken();
        return token;
    }

    public IDocument getDocument() {
        return fDocument;
    }
}

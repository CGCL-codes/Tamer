package faneclipse.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * The FanSingleTokenScaner is used when a whole partition has only one type of color, like comments.
 * @author BEGAUDEAU stephane
 */
public class FanSingleTokenScanner extends BufferedRuleBasedScanner {

    /**
	 * The constructor.
	 * @param attribute The attribute of the token.
	 */
    public FanSingleTokenScanner(final TextAttribute attribute) {
        setDefaultReturnToken(new Token(attribute));
    }
}

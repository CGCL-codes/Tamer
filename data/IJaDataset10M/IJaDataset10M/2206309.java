package ti.oscript.eclipse.ui.editor.oscript;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import ti.oscript.eclipse.ui.editor.oscript.parser.EndOfLineCommentRule;
import ti.oscript.eclipse.ui.editor.oscript.parser.OScriptNumberRule;
import ti.oscript.eclipse.ui.editor.oscript.parser.RegExStatementRule;
import ti.oscript.eclipse.ui.editor.util.OScriptColorProvider;
import ti.oscript.eclipse.ui.editor.util.OScriptWhitespaceDetector;
import ti.oscript.eclipse.ui.editor.util.OScriptWordDetector;

/**
 * A Java code scanner.
 */
public class OScriptSyntaxHighlightingScanner extends RuleBasedScanner {

    /**
	 * Creates a Java code scanner with the given color provider.
	 * 
	 * @param provider
	 *            the color provider
	 */
    public OScriptSyntaxHighlightingScanner(OScriptColorProvider provider) {
        IToken keyword = new Token(new TextAttribute(provider.getColor(OScriptColorProvider.KEYWORD), null, SWT.BOLD));
        IToken qualifyer = new Token(new TextAttribute(provider.getColor(OScriptColorProvider.QUALIFYER), null, SWT.ITALIC));
        IToken declaration = new Token(new TextAttribute(provider.getColor(OScriptColorProvider.DECLARATION), null, SWT.BOLD));
        IToken thisVar = new Token(new TextAttribute(provider.getColor(OScriptColorProvider.THIS_VAR), null, SWT.BOLD | SWT.ITALIC));
        IToken type = new Token(new TextAttribute(provider.getColor(OScriptColorProvider.TYPE)));
        IToken string = new Token(new TextAttribute(provider.getColor(OScriptColorProvider.STRING)));
        IToken constant = new Token(new TextAttribute(provider.getColor(OScriptColorProvider.CONSTANT)));
        IToken comment = new Token(new TextAttribute(provider.getColor(OScriptColorProvider.COMMENT)));
        IToken other = new Token(new TextAttribute(provider.getColor(OScriptColorProvider.DEFAULT)));
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(new EndOfLineCommentRule(comment));
        rules.add(new SingleLineRule("\"", "\"", string, '\\'));
        rules.add(new RegExStatementRule(string));
        rules.add(new WhitespaceRule(new OScriptWhitespaceDetector()));
        WordRule wordRule = new WordRule(new OScriptWordDetector(), other);
        for (int i = 0; i < ISyntaxConsts.KEYWORDS.length; i++) {
            wordRule.addWord(ISyntaxConsts.KEYWORDS[i], keyword);
        }
        for (int i = 0; i < ISyntaxConsts.TYPES.length; i++) {
            wordRule.addWord(ISyntaxConsts.TYPES[i], type);
        }
        for (int i = 0; i < ISyntaxConsts.CONSTANTS.length; i++) {
            wordRule.addWord(ISyntaxConsts.CONSTANTS[i], constant);
        }
        for (int i = 0; i < ISyntaxConsts.QUALIFYERS.length; i++) {
            wordRule.addWord(ISyntaxConsts.QUALIFYERS[i], qualifyer);
        }
        for (int i = 0; i < ISyntaxConsts.DECLARATIONS.length; i++) {
            wordRule.addWord(ISyntaxConsts.DECLARATIONS[i], declaration);
        }
        for (int i = 0; i < ISyntaxConsts.THIS_VAR.length; i++) {
            wordRule.addWord(ISyntaxConsts.THIS_VAR[i], thisVar);
        }
        rules.add(wordRule);
        OScriptNumberRule numberRule = new OScriptNumberRule(constant);
        rules.add(numberRule);
        IRule[] result = new IRule[rules.size()];
        rules.toArray(result);
        setRules(result);
    }
}

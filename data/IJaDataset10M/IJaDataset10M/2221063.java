package it.kion.util.ui.ulog2.editor.scanners;

import it.kion.util.ui.ulog2.Log4jPlugin;
import it.kion.util.ui.ulog2.editor.TokenManager;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class DefaultScanner extends RuleBasedScanner {

    public DefaultScanner(TokenManager tokenManager) {
        IToken propertyToken = tokenManager.getToken(Log4jPlugin.PREF_PROPERTY_COLOR);
        setDefaultReturnToken(propertyToken);
        setRules(new IRule[] { new WhitespaceRule(new WhitespaceDetector()) });
    }
}

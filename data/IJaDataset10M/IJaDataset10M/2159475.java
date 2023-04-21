package org.evokit.ui.internal.text.scanners;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.evokit.ui.internal.text.ParameterFileColorProvider;

public class ParameterNameScanner extends RuleBasedScanner {

    public ParameterNameScanner(ParameterFileColorProvider provider) {
        TextAttribute ta = new TextAttribute(provider.getColor(ParameterFileColorProvider.NAME));
        IToken t = new Token(ta);
        IRule[] rules = new IRule[] { new WordRule(new IWordDetector() {

            @Override
            public boolean isWordStart(char c) {
                return !Character.isWhitespace(c);
            }

            @Override
            public boolean isWordPart(char c) {
                return !Character.isWhitespace(c);
            }
        }, t) };
        setRules(rules);
    }
}

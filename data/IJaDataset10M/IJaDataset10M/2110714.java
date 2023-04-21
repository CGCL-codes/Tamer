package org.vexi.vexidev.editor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.vexi.vexidev.IConstants;

public class XMLTagScanner extends RuleBasedScanner {

    public XMLTagScanner(ColorManager colorManager) {
        IToken string = new Token(new TextAttribute(colorManager.getPreferenceColor(IConstants.PREF_COL_STRING)));
        IRule[] rules = new IRule[3];
        rules[0] = new MultiLineRule("\"", "\"", string, '\\');
        rules[1] = new SingleLineRule("'", "'", string, '\\');
        rules[2] = new WhitespaceRule(new XMLWhitespaceDetector());
        setRules(rules);
    }
}

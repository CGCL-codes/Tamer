package com.agentfactory.eclipse.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class TeleoReactiveScanner extends RuleBasedScanner {

    String[] keywords = { "action", "sensor", "module", "#include", "rule", "function", "#agent", "#extends", "#override", "#abstract" };

    public TeleoReactiveScanner(ColorManager manager) {
        IToken keyword = new Token(new TextAttribute(manager.getColor(IAgentFactoryColorConstants.KEYWORD)));
        IToken operator = new Token(new TextAttribute(manager.getColor(IAgentFactoryColorConstants.OPERATOR)));
        IToken variable = new Token(new TextAttribute(manager.getColor(IAgentFactoryColorConstants.STRING)));
        WordRule keywordRule = new WordRule(new IWordDetector() {

            public boolean isWordStart(char c) {
                return Character.isJavaIdentifierStart(c);
            }

            public boolean isWordPart(char c) {
                return Character.isJavaIdentifierPart(c);
            }
        });
        for (String kwd : this.keywords) {
            keywordRule.addWord(kwd, keyword);
        }
        IRule[] rules = new IRule[5];
        rules[0] = keywordRule;
        rules[1] = new WordRule(new IWordDetector() {

            boolean implies;

            boolean implement;

            public boolean isWordStart(char c) {
                this.implies = false;
                this.implement = false;
                if (c == '>') {
                    this.implies = true;
                    return true;
                }
                if (c == '-') {
                    this.implement = true;
                    return true;
                }
                return ((c == '&') || (c == '(') || (c == ')') || (c == ',') || (c == ';') || (c == '~') || (c == '>') || (c == ':'));
            }

            public boolean isWordPart(char c) {
                if ((this.implies) && (c == '-')) {
                    this.implies = false;
                    return true;
                }
                if ((this.implement) && (c == '>')) {
                    this.implement = false;
                    return true;
                }
                return false;
            }
        }, operator);
        rules[2] = new WordRule(new IWordDetector() {

            public boolean isWordStart(char c) {
                return c == '?';
            }

            public boolean isWordPart(char c) {
                return Character.isJavaIdentifierPart(c);
            }
        }, variable);
        rules[3] = new WordRule(new IWordDetector() {

            boolean inString;

            public boolean isWordStart(char c) {
                if (c == '"') {
                    this.inString = true;
                    return true;
                }
                return false;
            }

            public boolean isWordPart(char c) {
                if (this.inString) {
                    if (c == '"') {
                        this.inString = false;
                    }
                    return true;
                }
                return false;
            }
        }, variable);
        rules[4] = new WhitespaceRule(new TeleoReactiveWhitespaceDetector());
        setRules(rules);
    }
}

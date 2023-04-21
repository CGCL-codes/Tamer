package net.sf.signs.editors;

import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;
import java.util.ArrayList;
import java.util.List;

public class ISCASScanner extends RuleBasedScanner {

    class ISCASWordDetector implements IWordDetector {

        public boolean isISCASIdentifierPart(char ch) {
            if (Character.isLetter(ch)) {
                return true;
            } else {
                if (Character.toString(ch).equals("_")) {
                    return true;
                }
            }
            return false;
        }

        public boolean isISCASIdentifierStart(char ch) {
            return Character.isLetterOrDigit(ch);
        }

        public boolean isWordPart(char character) {
            return this.isISCASIdentifierPart(character);
        }

        public boolean isWordStart(char character) {
            return this.isISCASIdentifierStart(character);
        }
    }

    private String[] fgKeywords = { "input", "output", "and", "nand", "or", "nor", "xor", "xnor", "not", "dff", "mux", "latch", "buf" };

    public ISCASScanner() {
        ColorManager colorManager = ColorManager.getInstance();
        IToken keyword = new Token(new TextAttribute(colorManager.getColor(SignsSourceViewerConfiguration.RGB_KEYWORD)));
        IToken string = new Token(new TextAttribute(colorManager.getColor(SignsSourceViewerConfiguration.RGB_STRING)));
        IToken other = new Token(new TextAttribute(colorManager.getColor(SignsSourceViewerConfiguration.RGB_DEFAULT)));
        IToken comment = new Token(new TextAttribute(colorManager.getColor(SignsSourceViewerConfiguration.RGB_COMMENT)));
        setDefaultReturnToken(other);
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(new EndOfLineRule("#", comment));
        rules.add(new SingleLineRule("\"", "\"", string, '\\'));
        rules.add(new SingleLineRule("\'", "\'", string, '\\'));
        WordRule wordRule = new WordRule(new ISCASWordDetector(), other);
        for (int i = 0; i < fgKeywords.length; i++) wordRule.addWord(fgKeywords[i], keyword);
        rules.add(wordRule);
        IRule[] result = new IRule[rules.size()];
        rules.toArray(result);
        setRules(result);
    }
}

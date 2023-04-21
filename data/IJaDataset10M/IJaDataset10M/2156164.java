package org.deft.language.antlrcsharp.editor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

/**
 * A C Sharp code scanner..
 */
public class CSharpCodeScanner extends RuleBasedScanner implements ICSharpLanguageWords {

    /**
	 * Creates a C Sharp code scanner
	 */
    public CSharpCodeScanner(CSharpColorProvider provider) {
        IToken keyword = new Token(new TextAttribute(provider.getColor(CSharpColorProvider.KEYWORD), null, SWT.BOLD));
        IToken type = new Token(new TextAttribute(provider.getColor(CSharpColorProvider.TYPE), null, SWT.BOLD));
        IToken string = new Token(new TextAttribute(provider.getColor(CSharpColorProvider.STRING)));
        IToken comment = new Token(new TextAttribute(provider.getColor(CSharpColorProvider.SINGLE_LINE_COMMENT)));
        IToken number = new Token(new TextAttribute(provider.getColor(CSharpColorProvider.NUMBER)));
        IToken pp_keyword = new Token(new TextAttribute(provider.getColor(CSharpColorProvider.CSHARP_PP), null, SWT.BOLD));
        IToken identifier = new Token(new TextAttribute(provider.getColor(CSharpColorProvider.DEFAULT)));
        IToken other = new Token(new TextAttribute(provider.getColor(CSharpColorProvider.DEFAULT)));
        setDefaultReturnToken(other);
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(new WhitespaceRule(new CSharpWhitespaceDetector()));
        CSharpPreprocessorRule ppRule = new CSharpPreprocessorRule(pp_keyword);
        for (int i = 0; i < ppKeywords.length; i++) {
            ppRule.addWord(ppKeywords[i]);
        }
        rules.add(ppRule);
        rules.add(new EndOfLineRule("//", comment));
        rules.add(new MultiLineRule("/*", "*/", comment));
        rules.add(new SingleLineRule("\"", "\"", string, '\\'));
        rules.add(new SingleLineRule("'", "'", string, '\\'));
        rules.add(new ExtendedMultiLineRule("@\"", "\"", string, '\"'));
        rules.add(new CSharpFloatingPointLiteralRule(number));
        rules.add(new CSharpIntegerLiteralRule(number));
        WordRule wordRule = new CSharpWordRule(new CSharpWordDetector(), other);
        for (int i = 0; i < keywords.length; i++) wordRule.addWord(keywords[i], keyword);
        for (int i = 0; i < types.length; i++) wordRule.addWord(types[i], type);
        for (int i = 0; i < constants.length; i++) wordRule.addWord(constants[i], type);
        rules.add(wordRule);
        rules.add(new CSharpIdentifierRule(identifier));
        IRule[] result = new IRule[rules.size()];
        rules.toArray(result);
        setRules(result);
    }

    /**
	 * This rule works as a normal MultiLineRule but additionally covers the special case that
	 * the escape character is the same as the end character. This is needed for C# verbatim
	 * strings, which have a " as end delimiter. Quotes are represented as "" within the string.
	 * The rule must make sure that "" is not interpreted as the end of the verbatim string.
	 *  
	 * @author abartho
	 */
    private class ExtendedMultiLineRule extends MultiLineRule {

        public ExtendedMultiLineRule(String startSequence, String endSequence, IToken token, char escapeCharacter) {
            super(startSequence, endSequence, token, escapeCharacter);
        }

        protected boolean endSequenceDetected(ICharacterScanner scanner) {
            if (String.valueOf(fEscapeCharacter).equals(String.valueOf(fEndSequence))) {
                int c;
                while ((c = scanner.read()) != ICharacterScanner.EOF) {
                    if (c == fEscapeCharacter) {
                        int c2 = scanner.read();
                        if (c2 != fEscapeCharacter) {
                            scanner.unread();
                            return true;
                        }
                    }
                }
                return true;
            } else {
                return super.endSequenceDetected(scanner);
            }
        }
    }
}

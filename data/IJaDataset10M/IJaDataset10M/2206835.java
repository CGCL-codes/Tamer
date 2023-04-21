package ch.wess.ezclipse.tpl.editor.internal.scanners;

import java.util.Iterator;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import ch.wess.ezclipse.core.ColorManager;
import ch.wess.ezclipse.tpl.TPLLanguageConstants;
import ch.wess.ezclipse.tpl.editor.internal.rules.TPLParameterRule;
import ch.wess.ezclipse.tpl.editor.internal.rules.TPLVariableRule;
import ch.wess.ezclipse.tpl.preferences.internal.TPLColorConstants;

/**
 * Define more granular rules inside template code partition.
 * 
 * @author Alain Sahli (a.sahli[at]wess.ch)
 */
public class TPLCodeScanner extends RuleBasedScanner {

    /**
     * Constructor, create rules.
     * 
     * @param manager
     */
    public TPLCodeScanner(ColorManager manager) {
        Color color = manager.getColor(TPLColorConstants.getDefault());
        TextAttribute textAttribute = new TextAttribute(color);
        Token token = new Token(textAttribute);
        setDefaultReturnToken(token);
        color = manager.getColor(TPLColorConstants.getString());
        textAttribute = new TextAttribute(color);
        IToken string = new Token(textAttribute);
        color = manager.getColor(TPLColorConstants.getVariable());
        textAttribute = new TextAttribute(color);
        IToken variable = new Token(textAttribute);
        color = manager.getColor(TPLColorConstants.getParameter());
        textAttribute = new TextAttribute(color, null, SWT.ITALIC);
        IToken parameter = new Token(textAttribute);
        IRule[] rules = new IRule[5];
        rules[0] = new SingleLineRule("\"", "\"", string, '\\');
        rules[1] = new SingleLineRule("'", "'", string, '\\');
        rules[2] = new TPLVariableRule(variable);
        rules[3] = new TPLParameterRule(parameter);
        rules[4] = createOperatorRule(manager);
        setRules(rules);
    }

    /**
     * Create the operator rules.
     * 
     * @param manager
     * @return a new operator rule
     */
    private IRule createOperatorRule(ColorManager manager) {
        WordRule rule = new WordRule(new OperatorWordDetector()) {

            private StringBuffer fBuffer = new StringBuffer();

            private boolean fIgnoreCase = false;

            @SuppressWarnings("unchecked")
            @Override
            public IToken evaluate(ICharacterScanner scanner) {
                int c = scanner.read();
                if (c != ICharacterScanner.EOF && fDetector.isWordStart((char) c)) {
                    if (fColumn == UNDEFINED || (fColumn == scanner.getColumn() - 1)) {
                        fBuffer.setLength(0);
                        do {
                            fBuffer.append((char) c);
                            c = scanner.read();
                        } while (c != ICharacterScanner.EOF && fDetector.isWordPart((char) c));
                        scanner.unread();
                        String buffer = fBuffer.toString();
                        IToken token = (IToken) fWords.get(buffer);
                        if (fIgnoreCase) {
                            Iterator<String> iter = fWords.keySet().iterator();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                if (buffer.equalsIgnoreCase(key)) {
                                    token = (IToken) fWords.get(key);
                                    break;
                                }
                            }
                        } else token = (IToken) fWords.get(buffer);
                        if (token != null) return token;
                        if (fDefaultToken.isUndefined()) unreadBuffer(scanner);
                        return fDefaultToken;
                    }
                }
                scanner.unread();
                return Token.UNDEFINED;
            }
        };
        Color color = manager.getColor(TPLColorConstants.getOperator());
        TextAttribute textAttribute = new TextAttribute(color, null, SWT.BOLD);
        Token operatorToken = new Token(textAttribute);
        for (String[] operator : TPLLanguageConstants.OPERATOR_LIST) {
            rule.addWord(operator[0], operatorToken);
        }
        return rule;
    }

    /**
     * Method added for testing purpose.
     * 
     * @param attr
     * @return
     */
    public TextAttribute getAttr(Object attr) {
        if (attr instanceof TextAttribute) return (TextAttribute) attr;
        if (fDefaultReturnToken.getData() instanceof TextAttribute) return (TextAttribute) fDefaultReturnToken.getData();
        return null;
    }

    /**
     * Class which detect if the current char is part of an operator word.
     * 
     * @author Alain Sahli (a.sahli[at]wess.ch)
     * 
     */
    private final class OperatorWordDetector implements IWordDetector {

        @Override
        public boolean isWordPart(char currentChar) {
            if ((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || (currentChar >= '0' && currentChar <= '9') || currentChar == '_' || currentChar == '-') return true;
            return false;
        }

        @Override
        public boolean isWordStart(char currentChar) {
            if ((currentChar >= 'a' && currentChar <= 'z')) return true;
            return false;
        }
    }
}

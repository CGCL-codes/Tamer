package org.deved.antlride.internal.ui.text.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class AntlrMultilineRule implements IPredicateRule {

    private IToken fSuccesToken;

    private int fCount;

    private char fStartChar;

    private char fEndChar;

    public AntlrMultilineRule(IToken succesToken, char startChar, char endChar) {
        fSuccesToken = succesToken;
        fStartChar = startChar;
        fEndChar = endChar;
    }

    public IToken evaluate(ICharacterScanner scanner) {
        int ch = scanner.read();
        if (ch != ICharacterScanner.EOF && ch == fStartChar) {
            boolean end = false;
            fCount = -1;
            do {
                if (ch == fStartChar) {
                    fCount++;
                } else if (ch == fEndChar) {
                    fCount--;
                    end = fCount < 0;
                }
                if (end) {
                    return fSuccesToken;
                }
                ch = scanner.read();
            } while (ch != ICharacterScanner.EOF);
            return fSuccesToken;
        }
        scanner.unread();
        return Token.UNDEFINED;
    }

    public IToken evaluate(ICharacterScanner scanner, boolean resume) {
        return evaluate(scanner);
    }

    public IToken getSuccessToken() {
        return fSuccesToken;
    }
}

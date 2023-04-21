package org.aspencloud.simple9.builder.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class CharacterRule implements IRule {

    private char[] chars;

    private IToken token;

    public CharacterRule(String string, IToken token) {
        this.chars = string.toCharArray();
        this.token = token;
    }

    public IToken evaluate(ICharacterScanner scanner) {
        int reads = 0;
        for (char wc : chars) {
            int c = scanner.read();
            reads++;
            if (c != wc) {
                for (int i = 0; i < reads; i++) {
                    scanner.unread();
                }
                return Token.UNDEFINED;
            }
        }
        return token;
    }
}

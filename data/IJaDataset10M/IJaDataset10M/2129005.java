package org.eclipse.dltk.freemarker.internal.ui.text.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import freemarker.provisionnal.ext.ide.syntax.FTLSyntaxUtils;
import freemarker.provisionnal.ext.ide.syntax.TagSyntaxProvider;

public class FTLEndTagRule implements IRule {

    private TagSyntaxProvider tagSyntaxProvider;

    private IToken token;

    public FTLEndTagRule(TagSyntaxProvider tagSyntaxProvider, IToken token) {
        this.tagSyntaxProvider = tagSyntaxProvider;
        this.token = token;
    }

    public IToken evaluate(ICharacterScanner scanner) {
        int c = scanner.read();
        if (FTLSyntaxUtils.isEndBracket(c, tagSyntaxProvider)) {
            int charNext = scanner.read();
            scanner.unread();
            if (charNext == ICharacterScanner.EOF) {
                return token;
            }
        } else {
            if (c == '/') {
                int charNext = scanner.read();
                if (FTLSyntaxUtils.isEndBracket(charNext, tagSyntaxProvider)) {
                    charNext = scanner.read();
                    scanner.unread();
                    scanner.unread();
                    if (charNext == ICharacterScanner.EOF) {
                        return token;
                    }
                } else {
                    scanner.unread();
                }
            }
        }
        scanner.unread();
        return Token.UNDEFINED;
    }
}

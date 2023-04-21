package org.aspencloud.simple9.builder.editor;

import org.aspencloud.simple9.builder.Simple9Plugin;
import org.aspencloud.simple9.builder.editor.util.EspColorProvider;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public class EspScanner extends RuleBasedScanner {

    private class LevelRule implements IRule {

        private IToken token;

        public LevelRule() {
            EspColorProvider provider = Simple9Plugin.getDefault().getEspColorProvider();
            Color fg = provider.getColor(EspColorProvider.DEFAULT);
            Color bg = provider.getColor(EspColorProvider.JAVA_BG);
            token = new Token(new TextAttribute(fg, bg, SWT.NORMAL));
        }

        public IToken evaluate(ICharacterScanner scanner) {
            if (isFirstChar()) {
                int count = 0;
                while (scanner.read() == '\t') {
                    count++;
                }
                scanner.unread();
                if (count > 0) {
                    return token;
                }
            }
            return Token.UNDEFINED;
        }
    }

    @Override
    public void setRules(IRule[] rules) {
        IRule[] realRules = new IRule[rules.length + 1];
        realRules[0] = new LevelRule();
        System.arraycopy(rules, 0, realRules, 1, rules.length);
        super.setRules(realRules);
    }

    public char getChar(int offset) {
        try {
            return fDocument.getChar(fOffset + offset);
        } catch (BadLocationException e) {
            return (char) 0;
        }
    }

    public boolean isFirstChar() {
        try {
            int line = fDocument.getLineOfOffset(fOffset);
            return fDocument.getLineOffset(line) == fOffset;
        } catch (BadLocationException e) {
            e.printStackTrace();
            return false;
        }
    }
}

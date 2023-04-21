package org.antlride.internal.ui.text.rules;

import org.antlride.support.ui.text.rules.CharArraySymbolDetector;
import org.antlride.support.ui.text.rules.CharacterScannerReader;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

public class VariableReferenceHighlighter implements IRule {

    private final IWordDetector detector;

    private final IToken token;

    public VariableReferenceHighlighter(IWordDetector detector, IToken token) {
        Assert.isNotNull(detector);
        Assert.isNotNull(token);
        this.detector = detector;
        this.token = token;
    }

    public IToken evaluate(ICharacterScanner scanner) {
        CharacterScannerReader reader = new CharacterScannerReader(scanner, detector, new CharArraySymbolDetector('$', '='));
        String dollar = reader.nextSymbol();
        if ("$".equals(dollar)) {
            String reference = reader.nextWord(false);
            if (reference.length() > 0) {
                return token;
            }
            reader.unread(reference.length());
            return token;
        }
        reader.unread();
        return Token.UNDEFINED;
    }
}

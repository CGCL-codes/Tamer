package aurora.ide.editor.textpage.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class XMLTextPredicateRule implements IPredicateRule {

    private IToken token;

    private int charsRead;

    private boolean whiteSpaceOnly;

    boolean inCdata;

    public XMLTextPredicateRule(IToken text) {
        this.token = text;
    }

    public IToken getSuccessToken() {
        return token;
    }

    public IToken evaluate(ICharacterScanner scanner, boolean resume) {
        return evaluate(scanner);
    }

    public IToken evaluate(ICharacterScanner scanner) {
        reinit();
        int c = 0;
        while (isOK(c = read(scanner), scanner)) {
            if (c == ICharacterScanner.EOF) {
                return Token.UNDEFINED;
            }
            whiteSpaceOnly = whiteSpaceOnly && (Character.isWhitespace((char) c));
        }
        unread(scanner);
        if (whiteSpaceOnly) {
            rewind(scanner, charsRead);
            return Token.UNDEFINED;
        }
        return token;
    }

    private boolean isOK(int cc, ICharacterScanner scanner) {
        char c = (char) cc;
        if (!inCdata) {
            if (c == '<') {
                int cdataCharsRead = 0;
                for (int i = 0; i < "![CDATA[".length(); i++) {
                    c = (char) read(scanner);
                    cdataCharsRead++;
                    if (c != "![CDATA[".charAt(i)) {
                        rewind(scanner, cdataCharsRead);
                        inCdata = false;
                        return false;
                    }
                }
                inCdata = true;
                return true;
            }
        } else {
            if (c == ']') {
                for (int i = 0; i < "]>".length(); i++) {
                    c = (char) read(scanner);
                    if (c != "]>".charAt(i)) {
                        return true;
                    }
                }
                inCdata = false;
                return true;
            }
        }
        return true;
    }

    private void rewind(ICharacterScanner scanner, int theCharsRead) {
        while (theCharsRead > 0) {
            theCharsRead--;
            unread(scanner);
        }
    }

    private void unread(ICharacterScanner scanner) {
        scanner.unread();
        charsRead--;
    }

    private int read(ICharacterScanner scanner) {
        int c = scanner.read();
        charsRead++;
        return c;
    }

    private void reinit() {
        charsRead = 0;
        whiteSpaceOnly = true;
    }
}

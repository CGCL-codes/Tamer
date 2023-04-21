package net.sf.elbe.ui.editors.ldif.text;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Rule to detect LDIF records. A LDIF record must start with "dn:" at column 0
 * and end with new line at column 0.
 * 
 * 
 */
public class LdifRecordRule implements IPredicateRule {

    private static char[] DN_SEQUENCE = new char[] { 'd', 'n', ':' };

    private IToken recordToken;

    public LdifRecordRule(IToken recordToken) {
        this.recordToken = recordToken;
    }

    public IToken getSuccessToken() {
        return this.recordToken;
    }

    /**
	 * Checks for new line "\n", "\r" or "\r\n".
	 * 
	 * @param scanner
	 * @return
	 */
    private int matchNewline(ICharacterScanner scanner) {
        int c = scanner.read();
        if (c == '\r') {
            c = scanner.read();
            if (c == '\n') {
                return 2;
            } else {
                scanner.unread();
                return 1;
            }
        } else if (c == '\n') {
            c = scanner.read();
            if (c == '\r') {
                return 2;
            } else {
                scanner.unread();
                return 1;
            }
        } else {
            scanner.unread();
            return 0;
        }
    }

    /**
	 * Checks for "dn:".
	 * 
	 * @param scanner
	 * @return
	 */
    private int matchDnAndColon(ICharacterScanner scanner) {
        for (int i = 0; i < DN_SEQUENCE.length; i++) {
            int c = scanner.read();
            if (c != DN_SEQUENCE[i]) {
                while (i >= 0) {
                    scanner.unread();
                    i--;
                }
                return 0;
            }
        }
        return DN_SEQUENCE.length;
    }

    private boolean matchEOF(ICharacterScanner scanner) {
        int c = scanner.read();
        if (c == ICharacterScanner.EOF) {
            return true;
        } else {
            scanner.unread();
            return false;
        }
    }

    public IToken evaluate(ICharacterScanner scanner, boolean resume) {
        if (scanner.getColumn() != 0) {
            return Token.UNDEFINED;
        }
        int c;
        do {
            c = scanner.read();
            if (c == '\r' || c == '\n') {
                scanner.unread();
                if (this.matchNewline(scanner) > 0) {
                    int nlCount = this.matchNewline(scanner);
                    if (nlCount > 0) {
                        int dnCount = this.matchDnAndColon(scanner);
                        if (dnCount > 0) {
                            while (dnCount > 0) {
                                scanner.unread();
                                dnCount--;
                            }
                            return this.recordToken;
                        } else if (this.matchEOF(scanner)) {
                            return this.recordToken;
                        } else {
                            while (nlCount > 0) {
                                scanner.unread();
                                nlCount--;
                            }
                        }
                    } else if (this.matchEOF(scanner)) {
                        return this.recordToken;
                    }
                } else if (this.matchEOF(scanner)) {
                    return this.recordToken;
                }
            } else if (c == ICharacterScanner.EOF) {
                return this.recordToken;
            }
        } while (true);
    }

    public IToken evaluate(ICharacterScanner scanner) {
        return this.evaluate(scanner, false);
    }
}

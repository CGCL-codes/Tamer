package monet.editors.rules;

import monet.editors.model.DeclarationConstants;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;

public class StartTagRule extends MultiLineRule {

    public StartTagRule(IToken token) {
        this(token, false);
    }

    protected StartTagRule(IToken token, boolean endAsWell) {
        super("<", endAsWell ? "/>" : ">", token);
    }

    protected boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed) {
        int c = scanner.read();
        if (sequence[0] == '<') {
            if ((c == '?') || (c == '!')) {
                scanner.unread();
                return false;
            }
            if (c == 'f') {
                char character = (char) c;
                String tag = Character.toString(character);
                int d = -1;
                while (d != ' ') {
                    d = scanner.read();
                    character = (char) d;
                    tag += Character.toString(character);
                }
                tag = tag.trim();
                if ((!(tag.equalsIgnoreCase(DeclarationConstants.DECLARATION_FIELD_SECTION))) && (!(tag.equalsIgnoreCase("form"))) && (!(tag.equalsIgnoreCase("filter"))) && (!(tag.equalsIgnoreCase("format"))) && (!(tag.equalsIgnoreCase("fields-order")))) {
                    scanner.unread();
                    return false;
                }
            }
        } else if (sequence[0] == '>') {
            scanner.unread();
        }
        return super.sequenceDetected(scanner, sequence, eofAllowed);
    }
}

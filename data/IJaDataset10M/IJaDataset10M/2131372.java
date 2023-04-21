package parser3;

import org.antlr.runtime.Token;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements the rules for comparing two tokens.
 *
 * @author brianegge
 */
public class TokenComparator implements Comparator<Token> {

    /**
     * List of token types for which we don't want to compare the values
     */
    private final Set<Integer> equate = new HashSet<Integer>();

    public TokenComparator(int... tokens) {
        for (int token : tokens) {
            equate.add(token);
        }
    }

    public int compare(Token left, Token right) {
        if (left.getType() != right.getType()) {
            return left.getType() - right.getType();
        }
        if (equate(left.getType())) {
            return 0;
        }
        return left.getText().compareTo(right.getText());
    }

    public boolean equate(int type) {
        return equate.contains(type);
    }
}

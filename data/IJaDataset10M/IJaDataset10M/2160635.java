package nl.adaptivity.parser.tokens;

import nl.adaptivity.parser.LinePosition;
import nl.adaptivity.parser.languages.Language;

/**
 * A leaf node for an expression.
 * 
 * @author Paul de Vrieze
 * @version 0.1 $Revision: 477 $
 * @param <T> The enumeration of all tokentypes that could be returned.
 * @param <V> The type of the element contained.
 */
public class LiteralToken<T extends Enum<T> & Language<T>, V> extends AbstractLinedToken<T> implements ExprToken<T> {

    private final V aValue;

    /**
   * Create a new Leaf Token.
   * 
   * @param pTokenType The type of the token.
   * @param pPos The position.
   * @param pValue The value contained.
   */
    public LiteralToken(final T pTokenType, final LinePosition pPos, final V pValue) {
        super(pTokenType, pPos);
        if (pValue == null) {
            throw new NullPointerException("Null literals should be wrapped");
        }
        aValue = pValue;
    }

    public V getValue() {
        return aValue;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(super.toString());
        result.setCharAt(result.length() - 1, ':');
        result.append(getValue().toString());
        result.append(']');
        return result.toString();
    }
}

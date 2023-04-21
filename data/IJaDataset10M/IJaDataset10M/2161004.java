package riverbed.jelan.parser.standardtokens;

import riverbed.jelan.lexer.CharSource;
import riverbed.jelan.lexer.ErrorToken;
import riverbed.jelan.lexer.Token;
import riverbed.jelan.lexer.TokenFactory;
import riverbed.jelan.parser.Rule;
import riverbed.jelan.parser.softparser.TokenTypeRule;

public class DoubleNumberToken extends AbstractToken {

    double value;

    public DoubleNumberToken(String text) {
        super(text);
        value = Double.valueOf(text);
    }

    public static final TokenFactory FACTORY = new TokenFactory() {

        public Token makeToken(CharSource cs) {
            String text = cs.getSaved();
            try {
                return new DoubleNumberToken(text);
            } catch (NumberFormatException e) {
                return new ErrorToken(text, "Double format error: " + e.getMessage());
            }
        }
    };

    public static final Rule TYPE_RULE = new TokenTypeRule(DoubleNumberToken.class);

    public double doubleValue() {
        return value;
    }

    public boolean matches(Token token) {
        return equals(token);
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !other.getClass().equals(getClass())) return false;
        return this.value == ((DoubleNumberToken) other).value;
    }

    public int hashCode() {
        long lvalue = Double.doubleToLongBits(value);
        return (int) (lvalue ^ (lvalue >>> 32));
    }

    public String toString() {
        return "double(" + text + ")";
    }
}

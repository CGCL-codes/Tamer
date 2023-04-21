package ng.compiler.lexer.tokens;

import java.io.IOException;
import java.io.Reader;
import ng.compiler.lexer.NgLexer;
import ng.compiler.parser.State;
import ng.compiler.parser.State.Value;

/**
 * @author John
 * 
 */
public class IdentifierToken extends Token {

    private final String name;

    public IdentifierToken(final int firstChar, final Reader reader) throws IOException {
        final StringBuilder buf = new StringBuilder();
        buf.append(firstChar);
        while (true) {
            reader.mark(1);
            final int c = reader.read();
            if (NgLexer.isIdentifierPartCharacter(c)) {
                buf.append(c);
            } else {
                break;
            }
        }
        reader.reset();
        this.name = buf.toString();
    }

    @Override
    protected void transform(final State state, final Value currentValue) {
        switch(currentValue) {
            case expectingPackageName:
                state.setCurrentState(Value.possiblePackageQualifierDot);
                break;
            case expectingImportName:
                state.setCurrentState(Value.possibleImportQualifierDot);
                break;
            case expectingImportAsName:
                state.setCurrentState(Value.importDeclared);
                break;
            default:
                super.transform(state, currentValue);
        }
    }
}

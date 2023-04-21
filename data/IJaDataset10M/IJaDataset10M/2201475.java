package jsyntaxpane.lexers;

import jsyntaxpane.*;
import java.util.List;
import javax.swing.text.Segment;

/**
 * A lexer that does nothing.  Used for plain document editing.
 * @author Ayman Al-Sairafi
 */
public class EmptyLexer implements Lexer {

    @Override
    public void parse(Segment segment, int ofst, List<Token> tokens) {
    }
}

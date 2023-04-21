package jsyntaxpane.syntaxkits;

import jsyntaxpane.lexers.GroovyLexer;

/**
 *
 * @author Ayman Al-Sairafi
 */
public class GroovySyntaxKit extends JavaSyntaxKit {

    public GroovySyntaxKit() {
        super(new GroovyLexer());
    }
}

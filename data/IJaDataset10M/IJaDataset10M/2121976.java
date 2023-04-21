package jsyntaxpane.syntaxkits;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.lexers.BashLexer;

/**
 *
 * @author Ayman Al-Sairafi
 */
public class BashSyntaxKit extends DefaultSyntaxKit {

    public BashSyntaxKit() {
        super(new BashLexer());
    }
}

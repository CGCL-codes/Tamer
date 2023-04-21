package jsyntaxpane.syntaxkits;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.lexers.SqlLexer;

/**
 *
 * @author Ayman Al-Sairafi
 */
public class SqlSyntaxKit extends DefaultSyntaxKit {

    public SqlSyntaxKit() {
        super(new SqlLexer());
    }
}

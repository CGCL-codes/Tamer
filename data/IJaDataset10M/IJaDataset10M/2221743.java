package bm.vm.comp;

import bm.vm.lang.Block;
import bm.core.tools.StreamParser;
import bm.core.tools.ParserException;

/**
 * The &quot;var&quot; keyword.
 *
 * @author <a href="mailto:narciso@elondra.com">Narciso Cerezo</a>
 * @version $Revision$
 */
public class VarKeyword extends Keyword {

    public VarKeyword() {
        name = "var";
    }

    public void parse(final ClassCompiler compiler, final StreamParser parser, final Block parent) throws CompilerException {
        final CompVar command = new CompVar();
        try {
            String token = parser.next(ClassCompiler.WS, false, false, false);
            compiler.getClass(token);
            command.setClassName(token);
            token = parser.next(";", false, false, false);
            command.setName(token);
            parent.add(command);
        } catch (ParserException e) {
            throw new CompilerException(e);
        }
    }
}

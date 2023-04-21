package bm.vm.comp;

import bm.vm.lang.Block;
import bm.core.tools.StreamParser;
import bm.core.tools.ParserException;

/**
 * Synthetic block keyword.
 *
 * @author <a href="mailto:narciso@elondra.com">Narciso Cerezo</a>
 * @version $Revision$
 */
public class BlockKeyword extends Keyword {

    public BlockKeyword() {
        name = "block";
    }

    public void parse(final ClassCompiler compiler, final StreamParser parser, final Block parent) throws CompilerException {
        final Block block = new Block();
        while (true) {
            String token;
            try {
                token = parser.next(ClassCompiler.WS + "(", false, true, true);
            } catch (ParserException e) {
                throw new CompilerException(e);
            }
            if (token == null) {
                throw new CompilerException("Unexpected end of file", parser.getStartLine(), parser.getStartChar());
            }
            if (token.equals("}")) {
                parent.add(block);
                return;
            } else {
                final Keyword keyword = compiler.getKeyword(token);
                if (keyword != null) {
                    keyword.parse(compiler, parser, block);
                } else {
                    parser.pushback(token);
                    block.add(compiler.parseExpression(";"));
                }
            }
        }
    }
}

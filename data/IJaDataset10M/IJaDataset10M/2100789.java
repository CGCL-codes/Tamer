package bm.vm.comp;

import bm.vm.lang.Block;
import bm.core.tools.StreamParser;
import bm.core.tools.ParserException;

/**
 * The &quot;if&quot; keyword.
 *
 * @author <a href="mailto:narciso@elondra.com">Narciso Cerezo</a>
 * @version $Revision$
 */
public class IfKeyword extends Keyword {

    public IfKeyword() {
        name = "if";
    }

    public void parse(final ClassCompiler compiler, final StreamParser parser, final Block parent) throws CompilerException {
        final CompIf command = new CompIf();
        try {
            final int c = parser.nextChar();
            if (c != '(') {
                throw new CompilerException("( expected", parser.getStartLine(), parser.getStartChar());
            }
            command.setCondition(compiler.parseExpression(")"));
            parser.searchNext('{');
            final Block body = new Block();
            final BlockKeyword keyword = new BlockKeyword();
            keyword.parse(compiler, parser, body);
            command.setIfBranch(body);
            String token = parser.next(ClassCompiler.WS + "{}", false, true, true);
            if (token == null) {
                throw new CompilerException("Error reading file", parser.getStartLine(), parser.getStartChar());
            } else if (token.equals("else")) {
                parser.searchNext('{');
                final Block elseBody = new Block();
                final BlockKeyword elsekKeyword = new BlockKeyword();
                elsekKeyword.parse(compiler, parser, elseBody);
                command.setElseBranch(elseBody);
            } else {
                parser.pushback(token);
            }
            parent.add(command);
        } catch (ParserException e) {
            throw new CompilerException(e);
        }
    }
}

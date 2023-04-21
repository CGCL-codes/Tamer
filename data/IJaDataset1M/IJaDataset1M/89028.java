package verifier.ast;

import sexpression.*;

public abstract class ComparisonFactory extends ASTFactory {

    private IConstructor _constructor;

    protected ComparisonFactory(IConstructor constructor) {
        _constructor = constructor;
    }

    /**
	 * @see verifier.ast.ASTFactory#make(sexpression.ListExpression,
	 *      verifier.ast.ASTParser)
	 */
    @Override
    public AST make(ASExpression from, ListExpression matchresult, ASTParser parser) {
        return _constructor.make(from, parser.parse(matchresult.get(0)), parser.parse(matchresult.get(1)));
    }

    /**
	 * @see verifier.ast.ASTFactory#getPattern()
	 */
    @Override
    public String getPattern() {
        return "(" + getName() + " #any #any)";
    }
}

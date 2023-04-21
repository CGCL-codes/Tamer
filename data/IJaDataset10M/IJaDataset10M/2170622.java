package MScheme.syntax;

import MScheme.util.Arity;
import MScheme.code.Code;
import MScheme.environment.StaticEnvironment;
import MScheme.values.Value;
import MScheme.values.List;
import MScheme.exceptions.TypeError;

final class QuoteToken extends Syntax {

    static final Syntax INSTANCE = new QuoteToken();

    private QuoteToken() {
        super(Arity.exactly(1));
    }

    protected Code checkedTranslate(StaticEnvironment syntax, int len, List arguments) throws TypeError {
        Value v = arguments.getHead();
        v.setConst();
        return v.getLiteral();
    }
}

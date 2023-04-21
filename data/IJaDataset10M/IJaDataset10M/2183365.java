package pl.omtt.lang.grammar;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import pl.omtt.lang.model.ast.CommonNode;

public class CommonNodeAdaptor extends CommonTreeAdaptor {

    public CommonNodeAdaptor() {
        super();
    }

    @Override
    public Object create(Token token) {
        return new CommonNode(token);
    }
}

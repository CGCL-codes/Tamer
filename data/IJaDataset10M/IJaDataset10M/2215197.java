package net.sf.ngrease.core.metalanguage;

import net.sf.ngrease.core.ast.Element;
import net.sf.ngrease.core.ast.ElementDefaultImpl;
import net.sf.ngrease.core.ast.ElementList;

public class SplitSymbolExpression extends BaseExpression {

    private final Element source;

    public SplitSymbolExpression(Element expressionElement, Element source) {
        super(expressionElement);
        this.source = source;
    }

    public EvaluationResult evaluate(EvaluationContext context, ServiceConfiguration serviceConfiguration) {
        Element evaluatedSource = metaEvaluateToElement(source, context, serviceConfiguration);
        ElementList charElements = split(evaluatedSource.getSymbol());
        Element result = new ElementDefaultImpl("split-symbol", ElementList.EMPTY_LIST, charElements);
        return constant(result);
    }

    /**
	 * TODO something fancier and lazier, *if* needed
	 */
    private ElementList split(String symbol) {
        ElementList.Builder builder = ElementList.Builder.with(symbol.length());
        for (int i = 0; i < symbol.length(); i++) {
            builder.element(new ElementDefaultImpl(symbol.substring(i, i + 1)));
        }
        return builder.build();
    }
}
